package com.runemate.passive.bots.passiveherblore.tasks;

import com.passive.api.bot.task_bot.Task;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.passive.bots.passiveherblore.Main;
import com.runemate.passive.bots.passiveherblore.framework.BotAction;
import com.runemate.passive.bots.passiveherblore.framework.HerbAction;

import static com.passive.api.runescape.interfaces.Interfaces.openBankOrWait;

public class BankTask extends Task {
    public BankTask(Main bot){ this.mainBot = bot; }
    public Main mainBot;

    @Override
    public boolean validate() {
        if (mainBot.info.mainTask == BotAction.CLEAN_HERBS){
            return !Inventory.contains(mainBot.info.herbInfo.grimy);
        }

        if (Inventory.contains(mainBot.info.herbInfo.unfinishedPot) && mainBot.info.potionToMake != null) {
            if (Inventory.contains(mainBot.info.potionToMake.secondaryIngredient)) {
                return false;
            }
        }

        if (!Inventory.contains(mainBot.info.herbInfo.vialContents)) {
            return true;
        }

        if (Inventory.contains(mainBot.info.herbInfo.vialContents) && (Inventory.contains(mainBot.info.herbInfo.clean) || Inventory.contains(mainBot.info.herbInfo.grimy))) {
            return false;
        }

        if (Bank.isOpen()){
            return !Inventory.contains(mainBot.info.herbInfo.grimy) && !Inventory.contains(mainBot.info.herbInfo.clean);
        } else {
            if (Inventory.containsOnly(mainBot.info.herbInfo.unfinishedPot)) {
                return true;
            }

            if (Inventory.containsOnly(mainBot.info.herbInfo.unfinishedPot, mainBot.info.herbInfo.clean)) {
                return true;
            }

            return Inventory.containsOnly(mainBot.info.herbInfo.unfinishedPot, mainBot.info.herbInfo.vialContents);

        }
    }

    @Override
    public void execute() {
        bot.setStatus("Getting items from bank");
        openBankOrWait();

        if (mainBot.info.mainTask == BotAction.CLEAN_HERBS){
            Bank.depositInventory();
            Bank.withdraw(mainBot.info.herbInfo.grimy, 0);
            return;
        }

        if (Inventory.contains(mainBot.info.herbInfo.unfinishedPot)) {
            if (mainBot.info.potionToMake == null || Inventory.contains(mainBot.info.potionToMake.potion + "(3)")) {
                Bank.depositInventory();
                return;
            }
        } else if (mainBot.info.potionToMake != null) {
            if (Inventory.contains(mainBot.info.potionToMake.potion + "(3)")) {
                Bank.depositInventory();
                return;
            }
        }

        if (Inventory.isEmpty()){
            boolean withdrawUnfinished = false;
            if (Bank.contains(mainBot.info.herbInfo.unfinishedPot)) {
                if (mainBot.info.potionToMake != null) {
                    if (Bank.contains(mainBot.info.potionToMake.secondaryIngredient) && !Inventory.contains(mainBot.info.herbInfo.unfinishedPot)) {
                        Bank.withdraw(mainBot.info.herbInfo.unfinishedPot, 14);
                        withdrawUnfinished = true;
                    }
                }
            }
            if (!withdrawUnfinished) {
                Bank.withdraw(mainBot.info.herbInfo.vialContents, 14);
            }
            return;
        }

        if (Inventory.contains(mainBot.info.herbInfo.unfinishedPot)) {
            System.out.println("FinishedPotionStage");
            if (mainBot.info.potionToMake != null) {
                if (Bank.contains(mainBot.info.potionToMake.secondaryIngredient)) {
                    Bank.withdraw(mainBot.info.potionToMake.secondaryIngredient, 14);
                }
            }
        } else if (Inventory.contains(mainBot.info.herbInfo.vialContents)) {
            System.out.println("VialContentsStage");
            if (Inventory.getQuantity(mainBot.info.herbInfo.vialContents) > 15) {
                Bank.depositInventory();
                return;
            }

            if (mainBot.info.herbAction == HerbAction.GRIMY_ONLY) {
                if (Bank.contains(mainBot.info.herbInfo.grimy)) {
                    Bank.withdraw(mainBot.info.herbInfo.grimy, 14);
                }
            } else if (mainBot.info.herbAction == HerbAction.CLEAN_ONLY) {
                if (Bank.contains(mainBot.info.herbInfo.clean)) {
                    Bank.withdraw(mainBot.info.herbInfo.clean, 14);
                }
            } else if (mainBot.info.herbAction == HerbAction.GRIMY_AND_CLEAN) {
                if (Bank.contains(mainBot.info.herbInfo.grimy)) {
                    Bank.withdraw(mainBot.info.herbInfo.grimy, 14);
                } else if (Bank.contains(mainBot.info.herbInfo.clean)) {
                    Bank.withdraw(mainBot.info.herbInfo.clean, 14);
                }
            }

            if (mainBot.info.herbAction == HerbAction.GRIMY_AND_CLEAN) {
                if (Inventory.contains(mainBot.info.herbInfo.grimy) && !Inventory.isFull()) {
                    if (Bank.contains(mainBot.info.herbInfo.clean)) {
                        Bank.withdraw(mainBot.info.herbInfo.clean, 14);
                    }
                }
            }
        }
    }
}
