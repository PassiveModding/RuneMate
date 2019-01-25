package com.runemate.passive.bots.dicing;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.passive.bots.dicing.ui.PassiveDicerUIController;

public class DelayStart extends Task {
    @Override
    public void execute() {
        Execution.delayUntil(() ->SessionData.GetCurrentBalance() > 0, 30000);
    }

    public static Coordinate currentPos = new Coordinate(0,0);

    @Override
    public boolean validate() {

        Logger.Log("---DATA---\nMultiplier: " + SessionData.Multiplier +
                "\nMin: " + SessionData.MinWin +
                "\nMax: " + SessionData.MaxWin +
                "\nCurrent Amount: " + SessionData.currentAmount +
                "\nRigged: " + SessionData.FalsifiedOdds +
                "\nForce Loss: " + SessionData.ForceLoss +
                "\nForce Win: " + SessionData.ForceWin +
                "\nLast Trade: " + SessionData.LastAcceptedTrade +
                "\nCurrent Balance: "  + SessionData.GetCurrentBalance() +
                "\nBounds String: " + SessionData.GetBoundsString() +
                "\nCurrent User: " + SessionData.currentUser +
                "\nAllow Replay: " + SessionData.AllowReplay +
                "\nMax Bet: " + SessionData.GetMaxBetAllowed() +
                "\nMin Bet: " + SessionData.GetMinBetAllowed());


        Coordinate pos = GetLocation();
        if (pos != null){
            currentPos = pos;
        }

        if (SessionData.GetCurrentBalance() == 0){
            return true;
        }

        return false;
    }



    public static Coordinate GetLocation(){
        Player local = Players.getLocal();
        if (local != null){
            Coordinate pos = local.getPosition();
            if (pos != null){
                return pos;
            }
        }

        return null;
    }
}
