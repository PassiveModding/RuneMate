package com.runemate.passive.bots.dicing;

import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.local.hud.interfaces.Trade;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class DoTrade extends Task {
    @Override
    public boolean validate() {
        if (Trade.isOpen() && Trade.atOfferScreen()){
            return true;
        }

        return false;
    }

    @Override
    public void execute() {
        Logger.Log("Trading: " + Trade.getTradersName());

        Execution.delayUntil(() -> Trade.Incoming.newQuery().names("Coins").results().first() != null, 30000);

        if (Trade.Incoming.containsOnly("Coins") && Trade.hasOtherPlayerAccepted()){
            SpriteItem coins = Trade.Incoming.newQuery().names("Coins").results().first();

            if (coins != null){
                int quantity = coins.getQuantity();

                if (quantity < SessionData.GetMinBetAllowed() || quantity > SessionData.GetMaxBetAllowed()){
                    Trade.decline();

                    if (quantity < SessionData.GetMinBetAllowed()){
                        Logger.Log("Minimum trade amount is: " + SessionData.GetMinBetAllowed(), true);
                    } else {
                        Logger.Log("Maximum trade amount is: " + SessionData.GetMaxBetAllowed(), true);
                    }

                    return;
                }

                Trade.accept();
            }
        } else if (!Trade.Incoming.containsOnly("Coins")) {
            Trade.decline();
            Logger.Log("Declining Trade, Invalid Item Traded!",true);
            return;
        } else {
            return;
        }
    }
}
