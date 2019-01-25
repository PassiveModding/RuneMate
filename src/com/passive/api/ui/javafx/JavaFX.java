package com.passive.api.ui.javafx;

import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.framework.AbstractBot;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Rodrick Jones on 5/04/2016.
 */
public class JavaFX {

    public static Runnable wrap(Runnable runnable) {
        return Platform.isFxApplicationThread()
                ? runnable
                : () -> Platform.runLater(runnable);
    }

    public static <T extends Node> T loadFxml(AbstractBot bot, String fxml, T root, Object controller) {
        Future<InputStream> stream = bot.getPlatform().invokeLater(() -> Resources.getAsStream(fxml));
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(root);
        loader.setController(controller);
        try {
            return loader.load(stream.get());
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends Node> T loadFxml(AbstractBot bot, String fxml, Object controller) {
        Future<InputStream> stream = bot.getPlatform().invokeLater(() -> Resources.getAsStream(fxml));
        FXMLLoader loader = new FXMLLoader();
        loader.setController(controller);
        try {
            return loader.load(stream.get());
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean removeSelected(ListView list) {
        synchronized (list.getItems()) {
            Object sel = list.getSelectionModel().getSelectedItem();
            return sel != null && list.getItems().remove(sel);
        }
    }

    public static void runLater(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }

    public static void showAlert(Alert.AlertType type, String header, String content, ButtonType... buttons) {
        Alert a = new Alert(type, content, buttons);
        a.setHeaderText(header);
        a.show();
    }
}
