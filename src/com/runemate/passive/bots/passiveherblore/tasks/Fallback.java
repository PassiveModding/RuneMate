package com.runemate.passive.bots.passiveherblore.tasks;

import com.passive.api.bot.task_bot.Task;
import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.script.Execution;
import com.runemate.passive.bots.passiveherblore.Main;
import com.runemate.passive.bots.passiveherblore.framework.Finish;

import static com.passive.api.runescape.LocalPlayer.ensureLogout;

public class Fallback extends Task {
    public Main mainBot;
    public Fallback(Main bot){
        mainBot = bot;
    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public void execute() {
        bot.setStatus("Finishing...");

        String finishMessage;

        if (mainBot.info.FinishAction == Finish.LOGOUT_ON_FINISH) {
            bot.setStatus("Stopping. Tasks Complete.");
            finishMessage = "Bot tasks have finished. The session has been stopped and the player has been logged out";
            ensureLogout();
            ClientUI.sendTrayNotification(finishMessage);
            ClientUI.showAlert(finishMessage);
            mainBot.stop("out of items");
        } else if (mainBot.info.FinishAction == Finish.IDLE_ON_FINISH) {
            bot.setStatus("Idling. Tasks Complete.");
            Execution.delay(10000);
        } else if (mainBot.info.FinishAction == Finish.PAUSE_ON_FINISH) {
            bot.setStatus("Paused. Tasks Complete.");
            finishMessage = "Bot tasks have finished. The session has been paused and the player will remain idle";
            ClientUI.sendTrayNotification(finishMessage);
            ClientUI.showAlert(finishMessage);
            bot.pause();
        }
    }
}
