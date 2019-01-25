package com.runemate.passive.bots.braceletalcher2;

import com.runemate.game.api.script.framework.task.TaskBot;

public class Main extends TaskBot {

    public String UnchargedBracelet = "Bracelet of ethereum (uncharged)";
    public String RevenantEther = "Revenant ether";
    public String ChargedBracelet = "Bracelet of ethereum";
    public BraceletStage OutOfBracelets = BraceletStage.MoreInBank;
    public boolean OutOfEthers = false;

    public enum BraceletStage{
        MoreInBank,
        OutOfBracelets,
        WaitingInGe
    }

    public double CurrentlyExchangedPercent = 0;

    public int maxBuyPrice = 44000;
    public int buyPrice = 43750;

    public Main(){

    }

    @Override
    public void onStart (String... args){
        System.out.println("Running bot");
        setLoopDelay(100, 2000);
        add(new Finaliser(this), new ExchangeTask(this), new BankTask(this), new FillBracelets(this), new AlchTask(this));
    }
}
