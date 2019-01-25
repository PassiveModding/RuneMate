package com.runemate.passive.bots.dicing;

import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SessionData {
    public static boolean AllowReplay = true;

    public static RollResult CurrentWin = null;

    public static String LastAcceptedTrade = null;

    public static int fakeMoney = 6000000;
    public static boolean usefakedBalance = true;

    public static int MinWin = 55;
    public static int MaxWin = 100;
    public static int Multiplier = 2;

    public static boolean FalsifiedOdds = true;
    public static boolean ForceWin = true;
    public static boolean ForceLoss = true;

    public static Random rnd = new Random();

    public static String currentUser = null;
    public static int currentAmount = 0;

    public static void EnsureMinAndMaxCorrect(){
        if (MinWin < 1 || MinWin > MaxWin){
            MinWin = 55;
        }

        if (MaxWin < MinWin || MaxWin > 100){
            MaxWin = 100;
        }

        if (Multiplier <= 1){
            Multiplier = 2;
        }
    }

    public static String GetBoundsString(){
        return "[" + MinWin + "-" + MaxWin + "]";
    }

    public static List<Chatbox.Message> getTradeMessages(){

        Object[] msgs = Chatbox.getMessages().toArray();
        List<Chatbox.Message> tradeMessages = new ArrayList<>();

        for (int i = 0; i < msgs.length; i++){
            Chatbox.Message msg = (Chatbox.Message) msgs[i];

            String message = msg.getMessage();
            if (message == null){
                message = "";
            }

            if (message.contains("trade")){
                tradeMessages.add(msg);
            }
        }

        return tradeMessages;
    }

    public static int GetCurrentBalance(){
        if (usefakedBalance){
            return fakeMoney;
        }
        SpriteItem coin = Inventory.getItems("Coins").first();

        if (coin == null){
            return 0;
        }

        return coin.getQuantity();
    }

    public static double GetMinBetAllowed() {
        return GetMaxBetAllowed()/(Multiplier*8);
    }

    public static double GetMaxBetAllowed(){
        return GetCurrentBalance() / Multiplier;
    }

    public static String GetQuantityBoundsString(){
        int coins = GetCurrentBalance();

        if (coins == 0){
            return "NO VALID BALANCE";
        }


        return "[" + withSuffix(GetMinBetAllowed()) + "-" + withSuffix(GetMaxBetAllowed()) + "]";
    }

    public static String withSuffix(double count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f%c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp-1));
    }

    public static RollResult Roll(){

        EnsureMinAndMaxCorrect();

        int rollResult = rnd.nextInt(100);
        boolean win = false;
        //Result will be from 1-100
        /*
        if (FalsifiedOdds){
            if (rollResult >= 90 && rollResult < 100){
                win = true;

            }
        } else {
            if (rollResult >= MinWin && rollResult <= MaxWin){
                win = true;
            }
        }
        */

        if (FalsifiedOdds){
            if (rnd.nextBoolean()) {
                rollResult = rnd.nextInt(MinWin);
            }
        }

        if (ForceWin){
            rollResult = MinWin + (int)(Math.random() * ((MaxWin - MinWin) + 1));
        }

        if (ForceLoss){
            rollResult = (int)(Math.random() * ((MinWin) + 1));
        }

        if (rollResult >= MinWin && rollResult <= MaxWin){
            win = true;
        }

        RollResult result = new RollResult();

        if (win){
            result.Win = true;
            result.BetAmount = currentAmount;
            result.ReturnAmount = currentAmount * Multiplier;
            result.user = currentUser;
            result.RollNumber = rollResult;
            CurrentWin = result;
        } else {
            result.Win = false;
            result.BetAmount = currentAmount;
            result.ReturnAmount = 0;
            result.user = currentUser;
        }


        result.RollNumber = rollResult;

        currentAmount = 0;
        currentUser = null;



        return result;
    }

    public static class RollResult{
        public boolean Win;
        public int ReturnAmount;
        public int BetAmount;
        public String user;
        public int RollNumber;
    }
}
