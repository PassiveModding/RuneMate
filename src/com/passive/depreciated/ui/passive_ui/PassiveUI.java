package com.passive.depreciated.ui.passive_ui;

import com.passive.api.bot.PassiveBot;
import com.passive.api.pc.Browser;
import com.passive.api.ui.javafx.JavaFX;
import com.passive.depreciated.ui.passive_ui.break_handler_pane.BreakHandlerPane;
import com.passive.depreciated.ui.passive_ui.profit_pane.ProfitPane;
import com.passive.depreciated.ui.passive_ui.skill_pane.SkillPane;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.framework.core.LoopingThread;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PassiveUI extends GridPane implements Initializable {
    public PassiveBot bot;
    public PassiveUI(PassiveBot bot){
        this.bot = bot;
        JavaFX.loadFxml(bot, "com/passive/depreciated/ui/passive_ui/passive_ui.fxml", this, this);
    }

    @FXML
    private Label BOT_AUTHOR_LABEL;
    @FXML
    private Label BOT_VERSION_LABEL;
    @FXML
    private Label BOT_NAME_LABEL;
    @FXML
    private Label RUNTIME_LABEL;
    @FXML
    private Label STATUS_LABEL;
    @FXML
    private VBox contentBox;
    @FXML
    private VBox buttonBox;
    @FXML
    private ScrollPane contentScrollPane;
    @FXML
    private Button BOT_PAGE_BUTTON;
    private SkillPane skillPane;
    private ProfitPane profitPane;
    private BreakHandlerPane breakHandlerPane;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BOT_NAME_LABEL.setText(bot.getMetaData().getName());
        BOT_VERSION_LABEL.setText("Version " + bot.getMetaData().getVersion());
        BOT_AUTHOR_LABEL.setText("By " + bot.getMetaData().getAuthor());
        BOT_PAGE_BUTTON.setOnAction(event -> Browser.openWebpage(bot.getBotOverviewPage()));
    }

    public void setStatus(String status) {
        Platform.runLater(() -> this.STATUS_LABEL.setText(status));
    }

    public void startRuntimeUpdater() {
        bot.getPlatform().invokeLater(() -> new LoopingThread(() -> Platform.runLater(() -> RUNTIME_LABEL.setText(bot.getFormattedRuntime())), 1000).start());
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


    public void addContent(int index, TitledPane node) {
        Platform.runLater(() -> {
            Future<InputStream> stream =
                    node.getGraphic() == null
                            ? bot.getPlatform().invokeLater(() -> Resources.getAsStream("com/passive/depreciated/ui/images/settings-icon-30x30.png"))
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

    private ObjectProperty<Node> draggingTab = new SimpleObjectProperty<>();
    private static final String TAB_DRAG_KEY = "titledpane";
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

    public void removeContent(TitledPane node) {
        Platform.runLater(() -> contentBox.getChildren().remove(node));
    }

    public boolean containsContent(TitledPane n) {
        return contentBox.getChildren().contains(n);
    }

    /*public void addButton(Button button) {
        buttonBox.getChildren().add(button);
    }

    public void removeButton(Button button) {
        buttonBox.getChildren().remove(button);
    }

    public void takeSnapshot() {
        Image img = snapshot(null, null);
        try {
            Future<File> fileFuture = bot.getPlatform().invokeLater(() -> new File(Environment.getStorageDirectory(), new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + ".png"));
            BufferedImage image = SwingFXUtils.fromFXImage(img, null);
            File file = fileFuture.get();
            if (file != null) {
                ImageIO.write(image, "png", fileFuture.get());
                ClientUI.sendTrayNotification("Screenshot saved to " + file);
                System.out.println("Screenshot saved to " + file);
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }*/

    public void createPanes() {
        getSkillPane();
        getProfitPane();
        getBreakHandlerPane();
    }

    public ScrollPane getContentScrollPane() {
        return contentScrollPane;
    }

    public synchronized SkillPane getSkillPane() {
        if (skillPane == null) {
            skillPane = new SkillPane(bot);
            addContent(skillPane);
        }
        return skillPane;
    }

    public synchronized ProfitPane getProfitPane() {
        if (profitPane == null) {
            profitPane = new ProfitPane(bot);
            addContent(profitPane);
        }
        return profitPane;
    }

    public synchronized BreakHandlerPane getBreakHandlerPane() {
        if (breakHandlerPane == null) {
            breakHandlerPane = new BreakHandlerPane(bot);
            addContent(breakHandlerPane);
        }
        return breakHandlerPane;
    }
}
