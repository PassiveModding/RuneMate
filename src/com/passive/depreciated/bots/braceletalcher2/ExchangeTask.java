package com.runemate.passive.bots.braceletalcher2;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.net.GrandExchange;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import static com.passive.api.runescape.interfaces.Interfaces.openBankOrWait;
import static com.passive.api.runescape.interfaces.Interfaces.openGEOrWait;

public class ExchangeTask extends Task {
    public Main mainBot;
    public ExchangeTask(Main bot){
        mainBot = bot;
    }

    public enum OutOf {
        OutOfBracelets,
        OutOfEthers,
        None
    }

    public OutOf TypeOut;

    @Override
    public void execute() {
        System.out.println("ExchangeTask");
        if (TypeOut == OutOf.OutOfBracelets){
            System.out.println("Out of Bracelets");
            if (OutOfBracelets()) {
                TypeOut = OutOf.None;
                mainBot.OutOfBracelets = Main.BraceletStage.MoreInBank;
                return;
            }
        }

        if (TypeOut == OutOf.OutOfEthers){
            //
            return;
        }

        TypeOut = OutOf.None;
    }

    @Override
    public boolean validate() {
        if (!Inventory.contains(mainBot.UnchargedBracelet) && !Inventory.contains(mainBot.ChargedBracelet)){
            if (mainBot.OutOfBracelets == Main.BraceletStage.OutOfBracelets || mainBot.OutOfBracelets == Main.BraceletStage.WaitingInGe){
                if (mainBot.OutOfBracelets == Main.BraceletStage.OutOfBracelets){
                    System.out.println("Out of bracelets");
                }

                if (mainBot.OutOfBracelets == Main.BraceletStage.WaitingInGe){
                    System.out.println("Waiting in GE");
                }
                TypeOut = OutOf.OutOfBracelets;
                return true;
            }
        }

        /*
        if (mainBot.OutOfEthers){
            System.out.println("Out of ethers");
            TypeOut = OutOf.OutOfEthers;
            return true;
        }
        */

        return false;
    }

    public boolean OutOfBracelets(){
        openGEOrWait();

        if (GrandExchange.isOpen()){
            GrandExchange.Slot exchangeSlot = GrandExchange.newQuery().buyOffers().itemNames(mainBot.UnchargedBracelet).results().first();
            if (exchangeSlot != null){
                System.out.println("Already Exchanging");
                if (exchangeSlot.getOffer().getCompletion() == 1){
                    System.out.println("Exchange Complete");
                    mainBot.CurrentlyExchangedPercent = 0;
                    //Collect ITEMS to bank
                    GrandExchange.collectToBank();
                    GrandExchange.close();
                    return true;
                } else if (exchangeSlot.getOffer().getCompletion() == 0){
                    System.out.println("Zero bracelets transferred");
                    //Delay until purchased
                    Execution.delayUntil(() -> exchangeSlot.getOffer().getCompletion() > 0, 20000, 80000);
                }

                //Check if any are able to be collected and collect, otherwise delay
                if (mainBot.CurrentlyExchangedPercent >= exchangeSlot.getOffer().getCompletion()){
                    System.out.println("Waiting for more bracelets to transfer");
                    Execution.delayUntil(() -> exchangeSlot.getOffer().getCompletion() >= mainBot.CurrentlyExchangedPercent, 20000, 80000);
                }

                if (mainBot.CurrentlyExchangedPercent < exchangeSlot.getOffer().getCompletion()) {
                    System.out.println("Collecting bracelets to bank");
                    GrandExchange.collectToBank();
                    GrandExchange.close();
                    return true;
                } else {
                    GrandExchange.abortOffer(exchangeSlot);
                    System.out.println("Increasing buy price for bracelets to speed up purchase rate");
                    mainBot.buyPrice += 50;
                    Execution.delay(600, 5000);
                    if (mainBot.buyPrice > mainBot.maxBuyPrice){
                        mainBot.stop("Not profitable");
                    }
                    GrandExchange.collectToBank();
                }
            } else {
                int quantity = GetAvailableBraceletQuantity();
                if (quantity > 0){
                    System.out.println("Sending buy offer for bracelets");
                    GrandExchange.placeBuyOffer(mainBot.UnchargedBracelet, quantity, mainBot.buyPrice);
                    mainBot.CurrentlyExchangedPercent = 0;
                    Execution.delay(1000, 5000);

                    GrandExchange.Slot exchangeSlot2 = GrandExchange.newQuery().buyOffers().itemNames(mainBot.UnchargedBracelet).results().first();
                    if (exchangeSlot2 != null) {
                        System.out.println("Checking to see sales");
                        if (exchangeSlot2.getOffer().getCompletion() == 1) {
                            System.out.println("Exchange Complete");
                            mainBot.buyPrice -= 10;
                            System.out.println("Reducing buy brice by 10gp to check for better deals");
                        }
                    }
                }
            }
        }

        return false;
    }


    public int GetAvailableBraceletQuantity (){
        if (Inventory.contains("Coins")) {
            SpriteItem coins = Inventory.getItems("Coins").first();
            if (coins == null) {
                return 0;
            }

            if (coins.getQuantity() >= mainBot.buyPrice) {
                return coins.getQuantity() / mainBot.buyPrice;
            }
        }

            openBankOrWait();

            if (Bank.contains("Coins")){
                Bank.withdraw("Coins", 1125000);

                SpriteItem coins = Inventory.getItems("Coins").first();
                if (coins == null) {
                    return 0;
                }

                if (coins.getQuantity() >= mainBot.buyPrice) {
                    return coins.getQuantity() / mainBot.buyPrice;
                }
            }

            return 0;
    }
}
