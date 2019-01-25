package com.runemate.passive.bots.passiveherblore.tasks;

import com.passive.api.bot.task_bot.Task;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.script.Execution;
import com.runemate.passive.bots.passiveherblore.Main;

import static com.passive.api.runescape.interfaces.Interfaces.closeInterfacesOrWait;

public class CleanHerbs extends Task {
    public Main mainBot;
    public CleanHerbs(Main bot){
        mainBot = bot;
    }

    @Override
    public boolean validate() {
        return Inventory.contains(mainBot.info.herbInfo.grimy);

    }

    @Override
    public void execute() {
        bot.setStatus("Cleaning Herbs");
        closeInterfacesOrWait();

        if (Inventory.getItems(mainBot.info.herbInfo.grimy).first() != null) {
            for (SpriteItem weed : Inventory.getItems(mainBot.info.herbInfo.grimy)) {
                SpriteItem selected = Inventory.getSelectedItem();
                if (selected != null){
                    selected.click();
                }

                if (!weed.interact("Clean"))
                {
                    weed.click();
                }
                Execution.delay(50, 150);
            }
        }
    }
}
