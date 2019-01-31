package com.passive.api.ui.passive_ui.profit_pane;

import com.passive.api.bot.Passive_BOT;
import com.passive.api.runescape.trackers.profit.ItemEventListener;
import com.passive.api.runescape.trackers.profit.ProfitTracker;
import com.passive.api.ui.javafx.JavaFX;
import com.passive.api.ui.passive_ui.profit_pane.item_tab.ItemTab;
import com.runemate.game.api.hybrid.util.Resources;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * Created by SlashnHax on 16/01/2016.
 */
public class ProfitPane extends TitledPane implements Initializable, ItemEventListener {
    @FXML
    private VBox contentBox;
    /*@FXML
    private Button clearButton;*/
    private Passive_BOT bot;

    public ProfitPane(Passive_BOT bot) {
        this.bot = bot;
        bot.getProfitTracker().addListener(this);
        JavaFX.loadFxml(bot, "com/passive/api/ui/passive_ui/profit_pane/profit_pane.fxml", this, this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setGraphic(new ImageView(new Image(bot.getPlatform().invokeLater(() -> Resources.getAsStream("com/passive/api/ui/images/profit-icon-30x30.png")).get())));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        /*clearButton.setOnAction(event -> bot.getProfitTracker().);*/
        //itemInfoAdded(-1, bot.getProfitTracker().getTotalInfo());
    }

    //region ItemEventListener Methods
    @Override
    public void inventoryModified(int id, ProfitTracker.ItemInfo info) {

    }

    @Override
    public void update(int id, ProfitTracker.ItemInfo info) {

    }

    @Override
    public void itemInfoAdded(int id, ProfitTracker.ItemInfo info) {
        Platform.runLater(() -> {
            if (!bot.getUi().containsContent(this)) {
                bot.getUi().addContent(this);
            }
            ItemTab tab = new ItemTab(bot, info);
            if (id == -1) {
                contentBox.getChildren().add(0, tab);
            } else {
                contentBox.getChildren().add(tab);
                tab.setExpanded(false);
            }
        });
    }

    @Override
    public void itemInfoRemoved(int id, ProfitTracker.ItemInfo info) {
        if (bot.getUi().containsContent(this) && bot.getProfitTracker().isEmpty()) {
            bot.getUi().removeContent(this);
        }
        if (info.getName().equals("Total")){
            return;
        }

        Platform.runLater(() -> contentBox.getChildren().removeIf(tab -> ((ItemTab) tab).getInfo() == info));
    }
    //endregion
}
