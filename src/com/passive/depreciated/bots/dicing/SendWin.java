package com.runemate.passive.bots.dicing;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.local.hud.interfaces.Trade;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.passive.bots.dicing.ui.PassiveDicerUIController;

public class SendWin extends Task {
    @Override
    public void execute() {
        SendWinAmount(SessionData.CurrentWin);
    }

    @Override
    public boolean validate() {
        if (SessionData.CurrentWin != null){
            return true;
        }

        return false;
    }

    public void SendWinAmount(SessionData.RollResult res){
        if (Trade.isOpen()){
            if (Trade.getTradersName().equalsIgnoreCase(res.user)){
                EnsureMoneyIsSent(res);
                return;
            }
        }

        Logger.Log("Rolled: " + res.RollNumber + " Player Won! " + res.user + " Amount: " + res.ReturnAmount, true);

        Player player = Players.newQuery().names(res.user).results().first();
        if (player != null){
            if (player.interact("Trade with")){
                Logger.Log("Waiting for Open Trade");
                Execution.delayUntil(() -> Trade.isOpen(), 20000);


                if (Trade.isOpen() && Trade.atOfferScreen()){
                    if (Trade.getTradersName().equalsIgnoreCase(res.user)){
                        EnsureMoneyIsSent(res);
                    }
                }
            }
        }
    }

    public boolean EnsureMoneyIsSent(SessionData.RollResult res){
        SpriteItem myCoins = Inventory.newQuery().names("Coins").results().first();

        if (myCoins != null){
            if (!Trade.offer(myCoins, res.ReturnAmount)){
                EnsureMoneyIsSent(res);
                return false;
            }

            Logger.Log("Waiting for Deposit Action");
            Execution.delayUntil(() -> Trade.Outgoing.contains("Coins"), 10000);

            Logger.Log("Ensuring No Return on Win");
            if (Trade.Incoming.isEmpty() && Trade.Outgoing.contains("Coins")) {
                Trade.accept();

                Logger.Log("Ensuring at Confirmation Screen");
                Execution.delayUntil(() -> Trade.atConfirmationScreen(), 20000);

                if (Trade.atConfirmationScreen()) {
                    Trade.accept();
                    PassiveDicerUIController.DiceResult x = new PassiveDicerUIController.DiceResult(res.user, res.BetAmount, res.ReturnAmount, SessionData.GetCurrentBalance());
                    PassiveDicerUIController.historyValues.add(x);
                    SessionData.CurrentWin = null;
                    if (SessionData.AllowReplay) {
                        SessionData.LastAcceptedTrade = null;
                    }
                    return true;
                }
            }

        } else {
            Trade.decline();
            SendWinAmount(res);
            return false;
        }

        return false;
    }
}
