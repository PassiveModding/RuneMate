package com.runemate.passive.bots.herblore;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.task.Task;
import javafx.application.Platform;

import static com.passive.api.runescape.interfaces.Interfaces.openBankOrWait;

public class GetStuff extends Task {
    public Main mainBot;
    public GetStuff(Main bot){
        mainBot = bot;
    }

    /*
    public String herbName() {
        if (mainBot.store.cleanHerbs) {
            return mainBot.store.herbInfo.grimy;
        } else {
            return mainBot.store.herbInfo.clean;
        }
    }
    */

    public boolean makingFinishedPotions = false;

    @Override
    public boolean validate() {
        if (Inventory.contains(mainBot.store.herbInfo.unfinishedPot) && mainBot.store.potionToMake != null) {
            if (Inventory.contains(mainBot.store.potionToMake.secondaryIngredient)) {
                return false;
            }
        }

        if (!Inventory.contains(mainBot.store.herbInfo.vialContents)) {
            return true;
        }

        if (Inventory.contains(mainBot.store.herbInfo.vialContents) && (Inventory.contains(mainBot.store.herbInfo.clean) || Inventory.contains(mainBot.store.herbInfo.grimy))) {
            return false;
        }

        if (Bank.isOpen()){
            return !Inventory.contains(mainBot.store.herbInfo.grimy) && !Inventory.contains(mainBot.store.herbInfo.clean);
        } else {
            if (Inventory.containsOnly(mainBot.store.herbInfo.unfinishedPot)) {
                return true;
            }

            if (Inventory.containsOnly(mainBot.store.herbInfo.unfinishedPot, mainBot.store.herbInfo.clean)) {
                return true;
            }

            return Inventory.containsOnly(mainBot.store.herbInfo.unfinishedPot, mainBot.store.herbInfo.vialContents);

        }
    }

    @Override
    public void execute() {
        Platform.runLater(() -> mainBot.store.state.setValue("Getting items from bank"));
        System.out.println("Get stuff");
        openBankOrWait();

        if (Inventory.contains(mainBot.store.herbInfo.unfinishedPot)) {
            if (mainBot.store.potionToMake == null || Inventory.contains(mainBot.store.potionToMake.potion + "(3)")) {
                Bank.depositInventory();
                return;
            }
        } else if (mainBot.store.potionToMake != null) {
            if (Inventory.contains(mainBot.store.potionToMake.potion + "(3)")) {
                Bank.depositInventory();
                return;
            }
        }

        if (Inventory.isEmpty()){
            boolean withdrawUnfinished = false;
            if (Bank.contains(mainBot.store.herbInfo.unfinishedPot)) {
                if (mainBot.store.potionToMake != null) {
                    if (Bank.contains(mainBot.store.potionToMake.secondaryIngredient) && !Inventory.contains(mainBot.store.herbInfo.unfinishedPot)) {
                        Bank.withdraw(mainBot.store.herbInfo.unfinishedPot, 14);
                        withdrawUnfinished = true;
                    }
                }
            }
            if (!withdrawUnfinished) {
                Bank.withdraw(mainBot.store.herbInfo.vialContents, 14);
            }
            return;
        }

        if (Inventory.contains(mainBot.store.herbInfo.unfinishedPot)) {
            System.out.println("FinishedPotionStage");
            if (mainBot.store.potionToMake != null) {
                if (Bank.contains(mainBot.store.potionToMake.secondaryIngredient)) {
                    Bank.withdraw(mainBot.store.potionToMake.secondaryIngredient, 14);
                }
            }
        } else if (Inventory.contains(mainBot.store.herbInfo.vialContents)) {
            System.out.println("VialContentsStage");
            if (Inventory.getQuantity(mainBot.store.herbInfo.vialContents) > 15) {
                Bank.depositInventory();
                return;
            }

            if (mainBot.store.herbAction == HerbAction.GRIMY_ONLY) {
                if (Bank.contains(mainBot.store.herbInfo.grimy)) {
                    Bank.withdraw(mainBot.store.herbInfo.grimy, 14);
                }
            } else if (mainBot.store.herbAction == HerbAction.CLEAN_ONLY) {
                if (Bank.contains(mainBot.store.herbInfo.clean)) {
                    Bank.withdraw(mainBot.store.herbInfo.clean, 14);
                }
            } else if (mainBot.store.herbAction == HerbAction.GRIMY_AND_CLEAN) {
                if (Bank.contains(mainBot.store.herbInfo.grimy)) {
                    Bank.withdraw(mainBot.store.herbInfo.grimy, 14);
                } else if (Bank.contains(mainBot.store.herbInfo.clean)) {
                    Bank.withdraw(mainBot.store.herbInfo.clean, 14);
                }
            }

            if (mainBot.store.herbAction == HerbAction.GRIMY_AND_CLEAN) {
                if (Inventory.contains(mainBot.store.herbInfo.grimy) && !Inventory.isFull()) {
                    if (Bank.contains(mainBot.store.herbInfo.clean)) {
                        Bank.withdraw(mainBot.store.herbInfo.clean, 14);
                    }
                }
            }
        }
    }
}
