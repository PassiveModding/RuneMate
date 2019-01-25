package com.passive.api.ui.passive_ui.profit_pane.item_tab;

import com.passive.api.bot.Passive_BOT;
import com.passive.api.runescape.trackers.profit.ProfitTracker;
import com.passive.api.ui.javafx.JavaFX;
import com.passive.api.util.Formatting;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Rodrick Jones on 6/03/2016.
 */
public class ItemTab extends TitledPane implements Initializable {

    @FXML
    private MenuItem removeContext;
    @FXML
    private Label priceLabel;
    @FXML
    private Label profitPerHourLabel;
    @FXML
    private Label amountLabel;
    @FXML
    private Label amountPerHourLabel;
    @FXML
    private Label profitLabel;
    @FXML
    private ProgressBar titleProgressBar;
    @FXML
    private Label titleLabel;

    private Passive_BOT bot;
    private ProfitTracker.ItemInfo info;
    private ExecutorService animThread = Executors.newSingleThreadExecutor();

    public ItemTab(Passive_BOT bot, ProfitTracker.ItemInfo info) {
        this.bot = bot;
        this.info = info;
        JavaFX.loadFxml(bot, "com/passive/api/ui/passive_ui/profit_pane/item_tab/item_tab.fxml", this, this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        priceLabel.setText(Formatting.format(info.getPriceProperty().intValue()));
        profitPerHourLabel.setText(Formatting.format(info.getProfitHrProperty().get()));
        amountLabel.setText(Formatting.format(info.getAmountProperty().get()));
        amountPerHourLabel.setText(Formatting.format(info.getAmountHrProperty().get()));
        profitLabel.setText(Formatting.format(info.getProfitProperty().get()));
        removeContext.setOnAction(event -> bot.getProfitTracker().removeInfo(info.getId()));
        updateTitleLabelText();
        titleProgressBar.progressProperty().set(0);

        info.getPriceProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> priceLabel.setText(Formatting.format(newValue.intValue())))));
        info.getProfitHrProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> profitPerHourLabel.setText(Formatting.format(newValue.intValue())))));
        info.getAmountProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> amountLabel.setText(Formatting.format(newValue.intValue())))));
        info.getAmountHrProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> amountPerHourLabel.setText(Formatting.format(newValue.intValue())))));
        info.getProfitProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> profitLabel.setText(Formatting.format(newValue.intValue())))));

        info.getNameProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(this::updateTitleLabelText)));
        info.getAmountProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(this::updateTitleLabelText)));
        info.getProfitHrProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(this::updateTitleLabelText)));
        info.getAmountProperty().addListener(((observable, oldValue, newValue) -> animateProgressBar(oldValue.intValue() < newValue.intValue())));

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        titleProgressBar.prefWidthProperty().bind(bot.getUi().getContentScrollPane().widthProperty().subtract(70));
        titleLabel.prefWidthProperty().bind(titleProgressBar.prefWidthProperty());
    }

    public void updateTitleLabelText() {
        titleLabel.setText(info.getName() + " | " + info.getAmount() + " | " + Formatting.format((long) info.getProfitHrProperty().get()) + "gp/hr");
    }

    public void animateProgressBar(boolean gain) {
        animThread.submit(() -> {
            int start = gain ? 0 : 100;
            int end = gain ? 100 : 0;
            int step = gain ? 1 : -1;
            for (int i = start; i < end; i += step) {
                final double progress = i / 100d;
                Platform.runLater(() -> titleProgressBar.setProgress(progress));
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> titleProgressBar.setProgress(0));
        });
    }

    public ProfitTracker.ItemInfo getInfo() {
        return info;
    }
}
