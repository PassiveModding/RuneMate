package com.passive.api.ui.passive_ui;

import com.passive.api.bot.Passive_BOT;
import com.passive.api.bot.task_bot.break_handler.BreakHandler;
import com.passive.api.pc.Browser;
import com.passive.api.ui.javafx.JavaFX;
import com.passive.api.ui.passive_ui.break_handler_pane.BreakHandlerPane;
import com.passive.api.ui.passive_ui.profit_pane.ProfitPane;
import com.passive.api.ui.passive_ui.skill_pane.SkillPane;
import com.passive.api.util.JavaFixes;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.framework.core.LoopingThread;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PassiveUI extends GridPane implements Initializable {
    private Passive_BOT bot;
    public PassiveUI(Passive_BOT bot){
        this.bot = bot;
        JavaFX.loadFxml(bot, "com/passive/api/ui/passive_ui/passive_ui.fxml", this, this);
    }

    @FXML
    private Label RUNTIME_LABEL;
    @FXML
    private Label STATUS_LABEL;
    @FXML
    private Label BOT_NAME_LABEL;
    @FXML
    private Label BOT_VERSION_LABEL;
    @FXML
    private Label BOT_AUTHOR_LABEL;
    @FXML
    private Button PASSIVE_DISCORD;
    @FXML
    private Button BOT_PAGE_BUTTON;
    @FXML
    private Button MOUSE_OVERLAY;
    @FXML
    private ScrollPane contentScrollPane;
    @FXML
    private VBox contentBox;
    @FXML
    private VBox buttonBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BOT_NAME_LABEL.setText(bot.getMetaData().getName());
        BOT_VERSION_LABEL.setText("Version " + bot.getMetaData().getVersion());
        BOT_AUTHOR_LABEL.setText("By " + bot.getMetaData().getAuthor());
        BOT_PAGE_BUTTON.setOnAction(event -> Browser.openWebpage(bot.getBotOverviewPage()));
        BOT_PAGE_BUTTON.setDisable(bot.getBotOverviewPage() == null);
        PASSIVE_DISCORD.setOnAction(event -> Browser.openWebpage("https://discord.me/passive"));
        MOUSE_OVERLAY.setOnAction(event -> bot.toggleoverlay());
        try {
            Future<InputStream> pageImage = bot.getPlatform().invokeLater(() -> Resources.getAsStream("com/passive/api/ui/images/web_page.png"));
            Future<InputStream> discordImage = bot.getPlatform().invokeLater(() -> Resources.getAsStream("com/passive/api/ui/images/discord.png"));
            Future<InputStream> mouseImage = bot.getPlatform().invokeLater(() -> Resources.getAsStream("com/passive/api/ui/images/mouse.png"));

            BOT_PAGE_BUTTON.setGraphic(com.passive.api.util.Image.getImageViewWithSize(pageImage, 30, 30));
            BOT_PAGE_BUTTON.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            PASSIVE_DISCORD.setGraphic(com.passive.api.util.Image.getImageViewWithSize(discordImage, 30, 30));
            PASSIVE_DISCORD.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            MOUSE_OVERLAY.setGraphic(com.passive.api.util.Image.getImageViewWithSize(mouseImage, 30, 30));
            MOUSE_OVERLAY.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            getStylesheets().add("com/passive/api/ui/javafx/stylesheets/style.css");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStatus(String status) {
        Platform.runLater(() -> this.STATUS_LABEL.setText(status));
    }

    private static final String TAB_DRAG_KEY = "titledpane";
    private ObjectProperty<Node> draggingTab = new SimpleObjectProperty<>();
    public void startRuntimeUpdater() {
        bot.getPlatform().invokeLater(() -> new LoopingThread(() -> Platform.runLater(() -> RUNTIME_LABEL.setText(bot.getFormattedRuntime())), 1000).start());
        bot.getPlatform().invokeLater(() -> new LoopingThread(() -> Platform.runLater(() -> {
            List<Node> children = contentBox.getChildren();

            /*
            ProfitTracker profitTracker = bot.getProfitTracker();
            if (profitTracker != null){
                if (!children.contains(profitTracker)){
                    getProfitPane();
                }
            }

            SkillTracker skillTracker = bot.getSkillTracker();
            if (skillTracker != null){
                if (!children.contains(skillTracker)){
                    getSkillPane();
                }
            }
            */

            BreakHandler breakHandler = bot.getBreakHandler();
            if (breakHandler != null){
                if (!children.contains(breakHandler)){
                    getBreakHandlerPane();
                }
            }




            for (TitledPane node : bot.getAdditionalNodes()){
                if (!children.contains(node)){
                    addContent(node);
                }
            }

            for(Node node: JavaFixes.findDuplicates(children)){
                contentBox.getChildren().remove(node);
            }

        }), 5000).start());
    }


    public ScrollPane getContentScrollPane() {
        return contentScrollPane;
    }
    public void addDragFunctionality(TitledPane pane) {
        pane.setOnDragOver(event -> {
            final Dragboard dragboard = event.getDragboard();
            if (dragboard.hasString()
                    && TAB_DRAG_KEY.equals(dragboard.getString())
                    && draggingTab.get() != null) {
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
        });
        pane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString() && getChildren().size() > 1) {
                Object source = event.getGestureSource();
                int sourceIndex = contentBox.getChildren().indexOf(source);
                int targetIndex = contentBox.getChildren().indexOf(pane);
                List<Node> nodes = new ArrayList<>(contentBox.getChildren());
                if (sourceIndex < targetIndex)
                    Collections.rotate(nodes.subList(sourceIndex, targetIndex + 1), -1);
                else
                    Collections.rotate(nodes.subList(targetIndex, sourceIndex + 1), 1);
                contentBox.getChildren().setAll(nodes);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        pane.setOnDragDetected(event -> {
            Dragboard dragboard = pane.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(TAB_DRAG_KEY);
            dragboard.setContent(clipboardContent);
            draggingTab.set(pane);
            event.consume();
        });
    }
    public boolean containsContent(TitledPane n) {
        return contentBox.getChildren().contains(n);
    }
    public void addContent(TitledPane... nodes) {
        Platform.runLater(() -> {
            for (TitledPane node : nodes) {
                if (!containsContent(node)) {
                    addContent(contentBox.getChildren().size(), node);
                }
                addDragFunctionality(node);
            }
        });
    }

    public void removeContent(TitledPane node) {
        Platform.runLater(() -> contentBox.getChildren().remove(node));
    }
    public void addContent(int index, TitledPane node) {
        Platform.runLater(() -> {
            Future<InputStream> stream =
                    node.getGraphic() == null
                            ? bot.getPlatform().invokeLater(() -> Resources.getAsStream("com/passive/api/ui/images/settings-icon-30x30.png"))
                            : null;
            if (!containsContent(node)) {
                contentBox.getChildren().add(index, node);
                addDragFunctionality(node);

                if (stream != null) {
                    try {
                        node.setGraphic(new ImageView(new Image(stream.get(), 30, 30, true, true)));
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void addContent(TitledPane node) {
        Platform.runLater(() -> {
            contentBox.getChildren().add(contentBox.getChildren().size(), node);
        });
    }

    public void createPanes() {
        //Add default panes here!
        getSkillPane();
        getProfitPane();
        getBreakHandlerPane();
    }

    private SkillPane skillPane;
    //EXAMPLE PANE ADDITION
    public synchronized SkillPane getSkillPane() {
        if (skillPane == null) {
            skillPane = new SkillPane(bot);
            addContent(skillPane);
        }
        return skillPane;
    }
    private ProfitPane profitPane;
    //EXAMPLE PANE ADDITION
    public synchronized ProfitPane getProfitPane() {
        if (profitPane == null) {
            profitPane = new ProfitPane(bot);
            addContent(profitPane);
        }
        return profitPane;
    }
    private BreakHandlerPane breakHandlerPane;
    public synchronized BreakHandlerPane getBreakHandlerPane() {
        if (breakHandlerPane == null) {
            breakHandlerPane = new BreakHandlerPane(bot);
            addContent(breakHandlerPane);
        }
        return breakHandlerPane;
    }
}
