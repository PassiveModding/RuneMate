package com.runemate.passive.bots.tutorialisland2.PreStage;

import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.script.framework.task.Task;
import com.passive.api.util.JavaFixes;

public class Misclick extends Task {
    @Override
    public boolean validate() {
        String cdt = JavaFixes.EnsureStringIsNotNull(ChatDialog.getText());
        if (cdt.startsWith("I can't reach") || cdt.startsWith("Nothing interesting") || cdt.startsWith("You can't attack") || cdt.startsWith("None of them")|| cdt.startsWith("Someone else is")){
            return true;
        }

        return false;
    }

    @Override
    public void execute() {
        InterfaceComponent comp = Interfaces.newQuery().texts("Click to continue").results().first();

        if (comp != null){
            comp.click();
        } else{
            ChatDialog.Option cont = ChatDialog.getOption("Click to continue");
            ChatDialog.Continue continueBtn = ChatDialog.getContinue();

            if (cont != null){
                cont.select();
            } else if (continueBtn != null){
                continueBtn.select();
            }
        }
    }
}
