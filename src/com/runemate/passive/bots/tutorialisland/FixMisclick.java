package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.script.framework.task.Task;

public class FixMisclick extends Task {
    @Override
    public boolean validate() {
        GetContextItems.Context context = GetContextItems.GetContextItems();
        String cdt = context.chatText;
        return cdt.startsWith("I can't reach") || cdt.startsWith("Nothing interesting") || cdt.startsWith("You can't attack") || cdt.startsWith("None of them") || cdt.startsWith("Someone else is");

    }

    @Override
    public void execute() {
        GetContextItems.Context context = GetContextItems.GetContextItems();
        InterfaceComponent comp = Interfaces.newQuery().texts("Click to continue").results().first();

        if (comp != null){
            comp.click();
        } else{
            ChatDialog.Option cont = ChatDialog.getOption("Click to continue");

            if (cont != null){
                cont.select();
            } else if (context.contButton != null){
                context.contButton.select();
            }
        }
    }
}
