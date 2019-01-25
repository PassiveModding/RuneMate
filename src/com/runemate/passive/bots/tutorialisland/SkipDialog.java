package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.script.framework.task.Task;

public class SkipDialog extends Task {
    @Override
    public void execute() {
        cont.contButton.select();
    }

    GetContextItems.Context cont;

    @Override
    public boolean validate() {
        cont = GetContextItems.GetContextItems();


        if (cont.contButton != null){
            return true;
        }

        return false;
    }
}
