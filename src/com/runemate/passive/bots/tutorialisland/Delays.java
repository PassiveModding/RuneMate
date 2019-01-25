package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.script.framework.task.Task;

public class Delays extends Task {
    @Override
    public boolean validate() {
        if (RuneScape.isLoggedIn()){
            return false;
        }

        return true;
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
