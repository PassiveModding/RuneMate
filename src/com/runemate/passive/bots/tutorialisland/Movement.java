package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

import java.util.Random;

public class Movement extends Task {
    @Override
    public boolean validate() {
        int rnd = new Random().nextInt(10);

        if (Players.getLocal().isMoving()){

            if (rnd > 4){
                return true;
            }
        }


        return false;
    }

    @Override
    public void execute() {
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
}
