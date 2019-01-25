package com.runemate.passive.bots.herblore;

import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import javafx.application.Platform;

import static com.passive.api.runescape.LocalPlayer.ensureLogout;

public class Fallback extends Task {
    public Main mainBot;
    public Fallback(Main bot){
        mainBot = bot;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void execute() {
        Platform.runLater(() -> mainBot.store.state.setValue("Stopping"));
        System.out.println("All tasks failed");

        String finishMessage;
        mainBot.store.counter.stop();

        if (mainBot.store.FinishAction == Finish.LOGOUT_ON_FINISH) {
            Platform.runLater(() -> mainBot.store.state.setValue("Stopped. Tasks Complete."));
            finishMessage = "Bot tasks have finished. The session has been stopped and the player has been logged out";
            ensureLogout();
            ClientUI.sendTrayNotification(finishMessage);
            ClientUI.showAlert(finishMessage);
            mainBot.stop("out of items");
        } else if (mainBot.store.FinishAction == Finish.IDLE_ON_FINISH) {
            Platform.runLater(() -> mainBot.store.state.setValue("Idling"));
            Execution.delay(10000);
        } else if (mainBot.store.FinishAction == Finish.PAUSE_ON_FINISH) {
            Platform.runLater(() -> mainBot.store.state.setValue("Paused. Tasks Complete."));
            finishMessage = "Bot tasks have finished. The session has been paused and the player will remain idle";
            ClientUI.sendTrayNotification(finishMessage);
            ClientUI.showAlert(finishMessage);
            mainBot.pause();
        }
    }
}
