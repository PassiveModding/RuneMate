package com.runemate.passive.bots.tutorialisland2.PreStage;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.util.Random;

public class DelayWhileMoving extends Task {
    public Player localPlayer;

    @Override
    public boolean validate() {
        Player localPlayer = Players.getLocal();
        if (localPlayer != null){
            if (localPlayer.isMoving()){
                int rnd = new Random().nextInt(10);

                if (rnd > 4){
                    this.localPlayer = localPlayer;
                    return true;
                }
                return false;
            }
        }

        return false;
    }

    @Override
    public void execute() {
        Execution.delayUntil(() -> !localPlayer.isMoving(), 2500, 5000);
    }
}
