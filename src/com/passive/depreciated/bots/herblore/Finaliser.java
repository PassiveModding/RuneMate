package com.runemate.passive.bots.herblore;

import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.passive.api.runescape.LocalPlayer.ensureLogout;

public class Finaliser extends Task {
    public Main mainBot;
    public Finaliser(Main bot){
        mainBot = bot;
    }

    public String finaliserReason = "Unknown";

    @Override
    public void execute() {
        System.out.println("Finaliser failed " + finaliserReason);

        String finishMessage;
        mainBot.store.counter.stop();

        if (mainBot.store.FinishAction == Finish.LOGOUT_ON_FINISH) {
            Platform.runLater(() -> mainBot.store.state.setValue("Stopped. Tasks Complete."));
            finishMessage = "Bot tasks have finished. The session has been stopped and the player has been logged out";
            ensureLogout();
            ClientUI.sendTrayNotification(finishMessage);
            ClientUI.showAlert(finishMessage);
            mainBot.stop("Failed finaliser tests");
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

    @Override
    public boolean validate() {
        if (!mainBot.store.herbInfo.canUse()) {
            finaliserReason = "Cannot use herb";
            return true;
        }

        if (mainBot.store.potionToMake != null) {
            if (!mainBot.store.potionToMake.canCreate()) {
                finaliserReason = "Cannot create potion";
                return true;
            }
        }

        if (Bank.isOpen()){
            if (!Bank.contains(mainBot.store.herbInfo.clean) && !Bank.contains(mainBot.store.herbInfo.grimy)) {
                if (!Inventory.contains(mainBot.store.herbInfo.clean) && !Inventory.contains(mainBot.store.herbInfo.grimy)) {
                    finaliserReason = "Bank and inventory do not contain herb";
                    if (mainBot.store.changeHerbs) {
                        //Stop when out of guam
                        if (mainBot.store.herbInfo != Herb.GUAM) {
                            List<Herb> herbList = new ArrayList<Herb>(EnumSet.allOf(Herb.class));
                            int currentIndex = herbList.indexOf(mainBot.store.herbInfo);
                            if (currentIndex > 0) {
                                int prevIndex = --currentIndex;
                                Herb prevHerb = Herb.values()[prevIndex];
                                prevHerb.setPrices();
                                mainBot.store.herbInfo = prevHerb;
                                Platform.runLater(() -> mainBot.store.currentHerbDisplayName.setValue(mainBot.store.herbInfo.displayName()));
                                mainBot.store.potionToMake = null;
                                return false;
                            }
                        }
                    }

                    if (mainBot.store.potionToMake != null) {
                        if (Inventory.contains(mainBot.store.herbInfo.unfinishedPot)) {
                            if (Bank.contains(mainBot.store.potionToMake.secondaryIngredient) || Inventory.contains(mainBot.store.potionToMake.secondaryIngredient)) {
                                return false;
                            }
                        }

                        if (Bank.contains(mainBot.store.herbInfo.unfinishedPot)) {
                            Bank.withdraw(mainBot.store.herbInfo.unfinishedPot, 14);
                            return false;
                        }
                    }


                    return true;
                }
            }

            if (!Bank.contains(mainBot.store.herbInfo.vialContents)) {
                finaliserReason = "Out of vials";
                return !Inventory.contains(mainBot.store.herbInfo.vialContents);
            }
        }

        return false;
    }
}
