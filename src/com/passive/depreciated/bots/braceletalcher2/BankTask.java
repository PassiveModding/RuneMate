package com.runemate.passive.bots.braceletalcher2;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import static com.passive.api.runescape.interfaces.Interfaces.openBankOrWait;

public class BankTask extends Task {
    public Main mainBot;
    public BankTask(Main bot){
        mainBot = bot;
    }

    @Override
    public void execute() {
        System.out.println("Bank Task");
        openBankOrWait();

        if (!Inventory.contains(mainBot.RevenantEther)){
            System.out.println("Getting Ether");
            if (Bank.isOpen()){
                if (Bank.contains(mainBot.RevenantEther)){
                    Bank.withdraw(mainBot.RevenantEther, 1);
                } else {
                    System.out.println("Out of ether");
                    mainBot.OutOfEthers = true;
                    return;
                }
            } else {
                return;
            }
        }

        if (!Inventory.contains(mainBot.UnchargedBracelet)) {
            System.out.println("Getting Bracelet");
            if (Bank.contains(mainBot.UnchargedBracelet)){
                Bank.withdraw(mainBot.UnchargedBracelet, 25);

                Execution.delay(250, 2000);
                if (Bank.getQuantity(mainBot.UnchargedBracelet) > 0){
                    System.out.println("Noted More Bracelets in bank.");
                    mainBot.OutOfBracelets = Main.BraceletStage.MoreInBank;
                } else {
                    System.out.println("Noted no more bracelets in bank");
                    mainBot.OutOfBracelets = Main.BraceletStage.OutOfBracelets;
                }
            } else {
                System.out.println("Noted no more bracelets in bank");
                mainBot.OutOfBracelets = Main.BraceletStage.OutOfBracelets;
            }
        }
    }

    @Override
    public boolean validate() {
        if (!Inventory.contains(mainBot.UnchargedBracelet)){
            if (Inventory.contains("Bracelet of ethereum")){
                return false;
            } else {
                return true;
            }
        }

        if (!Inventory.contains(mainBot.RevenantEther)){
            return true;
        }

        return false;
    }
}
