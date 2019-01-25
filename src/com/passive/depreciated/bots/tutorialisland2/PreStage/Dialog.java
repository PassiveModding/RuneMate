package com.runemate.passive.bots.tutorialisland2.PreStage;

import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.passive.bots.tutorialisland.GetContextItems;

public class Dialog extends Task {

    ChatDialog.Continue continueButton;

    @Override
    public boolean validate() {
        ChatDialog.Continue cont = ChatDialog.getContinue();

        if (cont != null){
            continueButton = cont;
            return true;
        }

        return false;
    }

    @Override
    public void execute() {
        continueButton.select(true);
    }
}
