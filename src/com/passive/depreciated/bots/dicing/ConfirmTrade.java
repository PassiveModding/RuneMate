package com.runemate.passive.bots.dicing;

import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.local.hud.interfaces.Trade;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class ConfirmTrade extends Task {
    @Override
    public boolean validate() {
        if (Trade.isOpen() && Trade.atConfirmationScreen()){
            return true;
        }

        return false;
    }

    int prevBalance = 0;

    @Override
    public void execute() {

        Logger.Log("Confirming Trade: " + Trade.getTradersName());

        Execution.delayUntil(() -> Trade.hasOtherPlayerAccepted(), 30000);

        if (Trade.hasOtherPlayerAccepted() && Trade.atConfirmationScreen()){
            Logger.Log("Player Accepted");
            SpriteItem coins = Trade.Incoming.newQuery().names("Coins").results().first();

            String name = Trade.getTradersName();

            if (coins != null){
                int quantity = coins.getQuantity();

                if (quantity < SessionData.GetMinBetAllowed() || quantity > SessionData.GetMaxBetAllowed()){
                    Trade.decline();
                    Logger.Log("Declining Trade, out of accepted quantity!");
                    return;
                }

                prevBalance = SessionData.GetCurrentBalance();

                Trade.accept();

                Execution.delayUntil(() -> Trade.isOpen() == false, 5000);

                Execution.delay(2000);
                Logger.Log(""+prevBalance);
                Logger.Log(""+SessionData.GetCurrentBalance());
                if (prevBalance <= SessionData.GetCurrentBalance()){
                    Logger.Log("Begun Game with " + name + " Bet: " + quantity + " Possible Return: " + (quantity*SessionData.Multiplier), true);
                    SessionData.currentAmount = quantity;
                    SessionData.currentUser = name;
                }  else {
                    execute();
                    return;
                    /*
                    Logger.Log("Error, trade issue", true);
                    Trade.decline();
                    */
                }
            } else {
                Logger.Log("No Coins received.");
            }
        }
    }
}
