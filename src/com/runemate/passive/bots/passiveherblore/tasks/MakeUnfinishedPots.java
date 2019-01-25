package com.runemate.passive.bots.passiveherblore.tasks;

import com.passive.api.bot.task_bot.Task;
import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.osrs.local.hud.interfaces.MakeAllInterface;
import com.runemate.game.api.script.Execution;
import com.runemate.passive.bots.passiveherblore.Main;

import java.util.Arrays;

import static com.passive.api.runescape.interfaces.Interfaces.closeInterfacesOrWait;
import static com.passive.api.runescape.interfaces.Interfaces.ensureMakingAll;
import static com.passive.api.runescape.items.Items.randomCombine;

public class MakeUnfinishedPots extends Task {
    public Main mainBot;

    public MakeUnfinishedPots(Main bot) {
        mainBot = bot;
    }

    @Override
    public boolean validate() {
        return Inventory.contains(mainBot.info.herbInfo.clean) && Inventory.contains(mainBot.info.herbInfo.vialContents);
    }

    @Override
    public void execute() {
        bot.setStatus("Making Unfinished Potions");
        closeInterfacesOrWait();

        SpriteItem cleanHerb = Inventory.getItems(mainBot.info.herbInfo.clean).first();
        SpriteItem vialWater = Inventory.getItems(mainBot.info.herbInfo.vialContents).last();
        if (cleanHerb != null && vialWater != null) {
            SpriteItem selected = Inventory.getSelectedItem();
            if (selected != null) {
                ItemDefinition def = selected.getDefinition();
                if (def != null) {
                    String name = def.getName();
                    if (name != null) {
                        if (name == mainBot.info.herbInfo.clean) {
                            randomCombine(selected, vialWater);
                        } else if (name == mainBot.info.herbInfo.vialContents) {
                            randomCombine(cleanHerb, selected);
                        } else {
                            randomCombine(cleanHerb, vialWater);
                        }
                    }
                }
            } else {
                randomCombine(cleanHerb, vialWater);
            }

            Execution.delayUntil(() -> MakeAllInterface.isOpen(), 250, 1500);
        }


        ensureMakingAll(mainBot.info.herbInfo.unfinishedPot, Arrays.asList(mainBot.info.herbInfo.vialContents, mainBot.info.herbInfo.clean));
    }
}
