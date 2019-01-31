package com.runemate.passive.bots.passiveherblore.tasks;

import com.passive.api.bot.task_bot.Task;
import com.passive.api.runescape.interfaces.Interfaces;
import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.net.GrandExchange;
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

    public boolean DepositState(int attempts){
        if (attempts > 3) {
            return false;
        }

        if (!Inventory.isEmpty()){
            Bank.depositInventory();
            Execution.delayUntil(() -> Inventory.isEmpty(), 250, 1000);
            if (!Inventory.isEmpty()){

                return DepositState(attempts +1);
            }
        }

        return true;
    }

    public boolean WithdrawState(int attempts){
        if (attempts > 3){
            return false;
        }

        if (mainBot.info.mainTask == BotAction.MAKE_UNFINISHED_POTIONS){
            Bank.withdraw(mainBot.info.herbInfo.unfinishedPot, 0);
            if (!Inventory.contains(mainBot.info.herbInfo.unfinishedPot)){
                return WithdrawState(attempts + 1);
            }
        } else if (mainBot.info.mainTask == BotAction.MAKE_FINISHED_POTIONS){
            Bank.withdraw(mainBot.info.potionToMake.potion + "(3)", 0);
            if (!Inventory.contains(mainBot.info.potionToMake.potion+ "(3)")){
                return WithdrawState(attempts + 1);
            }
        } else if (mainBot.info.mainTask == BotAction.CLEAN_HERBS){
            Bank.withdraw(mainBot.info.herbInfo.clean, 0);
            if (!Inventory.contains(mainBot.info.herbInfo.clean)){
                return WithdrawState(attempts + 1);
            }
        }

        return true;
    }

    public boolean NoteState(int attempts){
        if (attempts > 3){
            return false;
        }

        if (Bank.getWithdrawMode() != Bank.WithdrawMode.NOTE){
            Bank.setWithdrawMode(Bank.WithdrawMode.NOTE);

            if (Bank.getWithdrawMode() != Bank.WithdrawMode.NOTE){
                return NoteState(attempts + 1);
            }
        }

        return true;
    }

    public boolean InventoryContainsNoted(String item){
        SpriteItem itemMatch = Inventory.getItems(item).first();
        if (itemMatch != null){
            ItemDefinition def = itemMatch.getDefinition();
            if (def != null){
                if (def.isNoted()){
                    return true;
                }
            }
        }

        return false;
    }

    public boolean BankAction(){
        if (mainBot.info.mainTask == BotAction.MAKE_UNFINISHED_POTIONS){
            if (InventoryContainsNoted(mainBot.info.herbInfo.unfinishedPot)){
                return true;
            }
        } else if (mainBot.info.mainTask == BotAction.MAKE_FINISHED_POTIONS){
            if (InventoryContainsNoted(mainBot.info.potionToMake.potion+ "(3)")){
                return true;
            }
        } else if (mainBot.info.mainTask == BotAction.CLEAN_HERBS){
            if (InventoryContainsNoted(mainBot.info.herbInfo.clean)){
                return true;
            }
        }

        if (Interfaces.openBankOrWait()){
            if (DepositState(0)){
                if (NoteState(0)){
                    if (WithdrawState(0)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean SlotAction(int attempts){
        if (attempts > 3){
            return false;
        }

        if (GrandExchange.getUnusedSlots().first() == null){
            return SlotAction(attempts + 1);
        }

        return true;
    }

    public boolean SubSaleAction(String item, int attempts){
        if (attempts > 5){
            return false;
        }

        SpriteItem spriteItem = Inventory.getItems(item).first();
        if (spriteItem != null){
            int quantity = spriteItem.getQuantity();

            if (GrandExchange.placeSellOffer(item, quantity, mainBot.info.sellOnFinishValue)){
                Execution.delayUntil(() -> GrandExchange.newQuery().sellOffers().itemNames(item).results().first() != null, 2500, 5000);
                if (GrandExchange.newQuery().sellOffers().itemNames(item).results().first() == null){
                    return SubSaleAction(item, attempts + 1);
                }
            } else {
                return SubSaleAction(item, attempts + 1);
            }

            return true;
        } else {
            return SubSaleAction(item, attempts + 1);
        }
    }

    public boolean SaleAction(){
        if (mainBot.info.mainTask == BotAction.MAKE_UNFINISHED_POTIONS){
            return SubSaleAction(mainBot.info.herbInfo.unfinishedPot, 0);
        } else if (mainBot.info.mainTask == BotAction.MAKE_FINISHED_POTIONS){
            return SubSaleAction(mainBot.info.potionToMake.potion + "(3)", 0);
        } else if (mainBot.info.mainTask == BotAction.CLEAN_HERBS){
            return SubSaleAction(mainBot.info.herbInfo.clean, 0);
        }

        return false;
    }

    public boolean ExchangeAction(){
        if (Interfaces.openGEOrWait()){
            if (SlotAction(0)){
                if (SaleAction()){
                    return true;
                }
            }
        }

        return false;
    }


    @Override
    public void execute() {
        if (mainBot.info.sellOnFinish && mainBot.info.sellOnFinishValue > 0 && !mainBot.info.changeHerbs){
            if (BankAction()){
                if (ExchangeAction()){
                    finishAction();
                    return;
                }
            }
        }

        finishAction();
    }

    public void finishAction(){
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
