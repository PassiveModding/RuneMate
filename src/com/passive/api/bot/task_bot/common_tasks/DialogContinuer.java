package com.passive.api.bot.task_bot.common_tasks;

import com.passive.api.bot.task_bot.Task;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;

public class DialogContinuer extends Task {
    private ChatDialog.Continue cont;

    public boolean validate() {
        return (cont = ChatDialog.getContinue()) != null;
    }

    @Override
    public void execute() {
        setStatus("Continuing dialog");
        cont.select();
    }
}
