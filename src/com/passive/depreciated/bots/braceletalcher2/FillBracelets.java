package com.runemate.passive.bots.braceletalcher2;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import static com.passive.api.runescape.interfaces.Interfaces.closeInterfacesOrWait;
import static com.passive.api.runescape.interfaces.Interfaces.openBankOrWait;

public class FillBracelets extends Task {
    public Main mainBot;
    public FillBracelets(Main bot){
        mainBot = bot;
    }


    @Override
    public void execute() {
        closeInterfacesOrWait();

        System.out.println("FillBracelets");
        if (Inventory.contains(mainBot.RevenantEther)){
            SpriteItem ether = Inventory.getItems(mainBot.RevenantEther).first();
            if (ether != null){
                if (ether.getQuantity() == 1) {
                    ether.interact("Use");
                    Execution.delay(150, 300);
                } else {
                    openBankOrWait();
                    if (Bank.isOpen()){
                        Bank.deposit(mainBot.RevenantEther, 0);
                        return;
                    }
                }
            }
        } else {
            return;
        }

        SpriteItem selected = Inventory.getSelectedItem();
        if (selected != null){
            if (selected.getQuantity() == 1){
                SpriteItem bracelet = Inventory.getItems(mainBot.UnchargedBracelet).first();
                if (bracelet != null){
                    bracelet.click();
                    Execution.delay(150, 300);
                }
            }
        }
    }

    @Override
    public boolean validate() {
        if (Inventory.contains(mainBot.UnchargedBracelet)){
            return true;
        }

        return false;
    }
}
