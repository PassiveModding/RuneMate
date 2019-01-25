package com.runemate.passive.bots.dicing;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.util.List;

public class OpenTrade extends Task {
    public Chatbox.Message msg;
    public Player player;

    @Override
    public void execute() {
        SessionData.LastAcceptedTrade = msg.getMessage();
    }

    @Override
    public boolean validate() {
        List<Chatbox.Message> msgs = SessionData.getTradeMessages();

        if (msgs.isEmpty()){
            return false;
        }

        if (SessionData.AllowReplay ){
            SessionData.LastAcceptedTrade = null;
        }

        Chatbox.Message last = msgs.get(msgs.size() - 1);
        if (last.getMessage().contains("wishes")){

            if (SessionData.LastAcceptedTrade != null)
            {
                if (SessionData.LastAcceptedTrade.equalsIgnoreCase(last.getMessage())){
                    return false;
                }
            }

            String sender = last.getSender();

            Player trader = Players.newQuery().names(sender).results().first();
            if (trader != null){
                if (!trader.isVisible()){
                    Camera.turnTo(trader);
                }

                Coordinate pos = trader.getPosition();
                if (pos != null){
                    if (LureData.Enabled){
                        if (pos.distanceTo(LureData.origin) > LureData.Tiles){
                            return false;
                        }
                    }

                    if (!pos.isReachable()){
                        return false;
                    } else {
                        if (trader.interact("Trade with")){
                            Execution.delay(1000);
                            player = trader;
                            msg = last;
                            return true;
                        } else {
                            if (trader.interact("Trade with")){
                                Execution.delay(1000);
                                player = trader;
                                msg = last;
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
}
