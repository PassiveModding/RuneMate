package com.runemate.passive.bots.tutorialisland2;

import com.runemate.game.api.hybrid.util.StopWatch;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BotInformationStore {
    public boolean run = false;
    public boolean logoutOnFinish = false;
    public String name = "";
    public final StringProperty state = new SimpleStringProperty();
    public StopWatch counter = new StopWatch();
    public final StringProperty runtimeLabel = new SimpleStringProperty();
    public final StringProperty nameText = new SimpleStringProperty();

    public void Update_Counts(){
        Platform.runLater(() -> {
            nameText.setValue(name);
            runtimeLabel.setValue(counter.getRuntimeAsString());
        });
    }
}
