package com.runemate.passive.bots.braceletalcher2;

import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceWindows;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import static com.passive.api.runescape.interfaces.Interfaces.closeInterfacesOrWait;

public class AlchTask extends Task {
    public Main mainBot;
    public AlchTask(Main bot){
        mainBot = bot;
    }

    @Override
    public void execute() {
        closeInterfacesOrWait();

        System.out.println("AlchTask");
        if (Magic.getSelected() == Magic.HIGH_LEVEL_ALCHEMY && InterfaceWindows.getInventory().isOpen()){
            SpriteItem bracelet = Inventory.getItems(mainBot.ChargedBracelet).first();
            SpriteItem slot16 = Inventory.getItemIn(16);
                if (slot16 != null){
                    if (slot16.getDefinition().getName() == mainBot.ChargedBracelet){
                        bracelet = slot16;
                        System.out.println("Slot16 found");
                    }
                }

                if (bracelet != null){
                    bracelet.click();
                    System.out.println("Alching");
                    Execution.delayUntil(() -> InterfaceWindows.getMagic().isOpen(), 3000);
                }
        } else {
            System.out.println("Activating alch spell");
            if (InterfaceWindows.getMagic().isOpen()){
                Magic.HIGH_LEVEL_ALCHEMY.activate();
            } else {
                InterfaceWindows.getMagic().open();
            }
        }
    }

    @Override
    public boolean validate() {
        if (Inventory.contains(mainBot.ChargedBracelet) && Inventory.contains("Nature rune") && Equipment.contains("Staff of fire")){
            return true;
        }

        return false;
    }
}
