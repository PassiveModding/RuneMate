package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.game.api.script.framework.task.TaskBot;

public class Lumbridge extends Task {

    TaskBot tBot;
    public  Lumbridge(TaskBot main){
        tBot = main;
    }


    @Override
    public boolean validate() {
        Npc guide = Npcs.newQuery().names("Lumbridge Guide").results().nearest();
        return guide != null;

    }

    @Override
    public void execute() {
        if (RuneScape.isLoggedIn()){
            RuneScape.logout();
            com.runemate.game.api.script.Execution.delay(3000);
            execute();
            //tBot.stop("Completed Tutorial Island");
        } else {
            tBot.stop("Completed Tutorial Island");
            return;
        }
    }
}
