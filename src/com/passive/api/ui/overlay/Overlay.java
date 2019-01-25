package com.passive.api.ui.overlay;

import com.passive.api.bot.Passive_BOT;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Overlay {
    private MouseApp f;


    public Overlay(Passive_BOT bot){
        Platform.runLater(() -> createAndShowGUI(bot));
    }

    public MouseApp getFrame() { return f; }

    private void createAndShowGUI(Passive_BOT bot) {
        f = new MouseApp(bot);
        try {
            f.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
