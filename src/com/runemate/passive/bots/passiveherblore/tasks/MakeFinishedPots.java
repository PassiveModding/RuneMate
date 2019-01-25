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

public class MakeFinishedPots extends Task {
    public Main mainBot;

    public MakeFinishedPots(Main bot) {
        mainBot = bot;
    }

    @Override
    public boolean validate() {
        if (Inventory.contains(mainBot.info.herbInfo.unfinishedPot)) {
            if (mainBot.info.potionToMake != null) {
                return Inventory.contains(mainBot.info.potionToMake.secondaryIngredient);
            }
        }

        return false;
    }

    @Override
    public void execute() {
        bot.setStatus("Making Finished Potions");
        closeInterfacesOrWait();

        SpriteItem secondaryIng = Inventory.getItems(mainBot.info.potionToMake.secondaryIngredient).first();
        SpriteItem vialUnf = Inventory.getItems(mainBot.info.herbInfo.unfinishedPot).last();
        if (secondaryIng != null && vialUnf != null) {
            SpriteItem selected = Inventory.getSelectedItem();
            if (selected != null) {
                ItemDefinition def = selected.getDefinition();
                if (def != null) {
                    String name = def.getName();
                    if (name != null) {
                        if (name == mainBot.info.herbInfo.unfinishedPot) {
                            randomCombine(selected, vialUnf);
                        } else if (name == mainBot.info.potionToMake.secondaryIngredient) {
                            randomCombine(secondaryIng, selected);
                        } else {
                            randomCombine(secondaryIng, vialUnf);
                        }
                    }
                }
            } else {
                randomCombine(secondaryIng, vialUnf);
            }

            Execution.delayUntil(() -> MakeAllInterface.isOpen(), 250, 3000);
        }


        ensureMakingAll(mainBot.info.potionToMake.potion + "(3)", Arrays.asList(mainBot.info.herbInfo.unfinishedPot, mainBot.info.potionToMake.secondaryIngredient));
    }
}
