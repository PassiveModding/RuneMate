package com.runemate.passive.bots.passiveherblore.tasks;

import com.passive.api.bot.task_bot.Task;
import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import com.runemate.passive.bots.passiveherblore.Main;
import com.runemate.passive.bots.passiveherblore.framework.BotAction;
import com.runemate.passive.bots.passiveherblore.framework.Finish;
import com.runemate.passive.bots.passiveherblore.framework.Herb;
import com.runemate.passive.bots.passiveherblore.framework.HerbAction;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.passive.api.runescape.LocalPlayer.ensureLogout;

public class Finisher extends Task {
    public Finisher(Main bot){ this.mainBot = bot; }
    public Main mainBot;

    public String finishReason = "UNKNOWN";

    @Override
    public boolean validate() {
        if (!mainBot.info.herbInfo.canUse()){
            finishReason = "Insufficient level to use " + mainBot.info.herbInfo.clean;
        }

        if (mainBot.info.potionToMake != null){
            if (mainBot.info.potionToMake.canCreate()){
                finishReason = "Insufficient level to use " + mainBot.info.potionToMake.potion;
            }
        }

        if (Bank.isOpen()){
            boolean hasCleanInInventory = Inventory.contains(mainBot.info.herbInfo.clean);
            boolean hasCleanInBank = Bank.contains(mainBot.info.herbInfo.clean);
            boolean hasGrimyInInventory = Inventory.contains(mainBot.info.herbInfo.grimy);
            boolean hasGrimyInBank = Bank.contains(mainBot.info.herbInfo.grimy);

            if (mainBot.info.potionToMake != null){
                if (Inventory.contains(mainBot.info.herbInfo.unfinishedPot)) {
                    if (Bank.contains(mainBot.info.potionToMake.secondaryIngredient) || Inventory.contains(mainBot.info.potionToMake.secondaryIngredient)){
                        return false;
                    }
                }

                if (Bank.contains(mainBot.info.herbInfo.unfinishedPot)){
                    Bank.withdraw(mainBot.info.herbInfo.unfinishedPot, 14);
                    return false;
                }
            }

            if (mainBot.info.herbAction == HerbAction.CLEAN_ONLY && !hasCleanInBank && !hasCleanInInventory){
                finishReason = "Out of " + mainBot.info.herbInfo.clean;
                return checkHerbChange();
            } else if (mainBot.info.herbAction == HerbAction.GRIMY_ONLY && !hasGrimyInBank && !hasGrimyInInventory){
                finishReason = "Out of " + mainBot.info.herbInfo.grimy;
                return checkHerbChange();
            } else if (mainBot.info.herbAction == HerbAction.GRIMY_AND_CLEAN && !hasCleanInBank && !hasCleanInInventory && !hasGrimyInBank && !hasGrimyInInventory){
                finishReason = "Out of " + mainBot.info.herbInfo.grimy + " & " + mainBot.info.herbInfo.clean;
                return checkHerbChange();
            }



            if (!Bank.contains(mainBot.info.herbInfo.vialContents) && !Inventory.contains(mainBot.info.herbInfo.vialContents)){
                finishReason = "Out of vials";
                return true;
            }

            return false;
        }

        return false;
    }

    @Override
    public void execute() {
        bot.setStatus("Finaliser Failed. Finishing");
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

    public boolean checkHerbChange(){
        if (mainBot.info.changeHerbs){
            //Stop only when out of lowest level herb (ie. guam)
            return !changeHerbToNextLowest();
        }

        return true;
    }

    public boolean changeHerbToNextLowest(){
        if (mainBot.info.herbInfo != Herb.GUAM){
            List<Herb> herbList = new ArrayList<>(EnumSet.allOf(Herb.class));
            int currentIndex = herbList.indexOf(mainBot.info.herbInfo);
            if (currentIndex > 0) {
                int prevIndex = --currentIndex;
                Herb prevHerb = Herb.values()[prevIndex];
                mainBot.info.herbInfo = prevHerb;
                if (mainBot.info.mainTask== BotAction.CLEAN_HERBS){
                    Platform.runLater(() -> mainBot.info.currentHerbDisplayName.setValue(mainBot.info.herbInfo.getCleanDisplayName()));
                } else {
                    Platform.runLater(() -> mainBot.info.currentHerbDisplayName.setValue(mainBot.info.herbInfo.getUnfinishedDisplayName()));
                }

                mainBot.info.potionToMake = null;
                return true;
            }
        }

        return false;
    }
}
