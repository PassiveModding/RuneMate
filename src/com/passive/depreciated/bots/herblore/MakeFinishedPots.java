package com.runemate.passive.bots.herblore;

import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.osrs.local.hud.interfaces.MakeAllInterface;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import javafx.application.Platform;

import static com.passive.api.runescape.interfaces.Interfaces.closeInterfacesOrWait;
import static com.passive.api.runescape.items.Items.randomCombine;

public class MakeFinishedPots extends Task {
    public Main mainBot;

    public MakeFinishedPots(Main bot) {
        mainBot = bot;
    }

    @Override
    public boolean validate() {
        if (Inventory.contains(mainBot.store.herbInfo.unfinishedPot)) {
            if (mainBot.store.potionToMake != null) {
                return Inventory.contains(mainBot.store.potionToMake.secondaryIngredient);
            }
        }

        return false;
    }

    @Override
    public void execute() {
        Platform.runLater(() -> mainBot.store.state.setValue("Making Finished Potions"));
        System.out.println("Make fin pots");
        closeInterfacesOrWait();

        SpriteItem secondaryIng = Inventory.getItems(mainBot.store.potionToMake.secondaryIngredient).first();
        SpriteItem vialUnf = Inventory.getItems(mainBot.store.herbInfo.unfinishedPot).last();
        if (secondaryIng != null && vialUnf != null) {
            SpriteItem selected = Inventory.getSelectedItem();
            if (selected != null) {
                ItemDefinition def = selected.getDefinition();
                if (def != null) {
                    String name = def.getName();
                    if (name != null) {
                        if (name == mainBot.store.herbInfo.unfinishedPot) {
                            randomCombine(selected, vialUnf);
                        } else if (name == mainBot.store.potionToMake.secondaryIngredient) {
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


        if (MakeAllInterface.isOpen()) {
            ensureMakingAll();
        }
    }

    public void ensureMakingAll() {
        System.out.println("Ensuring");
        if (!MakeAllInterface.isOpen()) {
            execute();
            return;
        }

        while (MakeAllInterface.isOpen()) {
            MakeAllInterface.selectItem(mainBot.store.potionToMake.potion + "(3)");
            Execution.delay(100, 500);
        }

        Execution.delayUntil(() -> noIngredients(), 10000, 12000);

        if (!noIngredients()) {
            ensureMakingAll();
        } else {
            int potCount = Inventory.getQuantity(mainBot.store.potionToMake.potion + "(3)");
            mainBot.store.finPotCountValue += potCount;
            mainBot.store.profitCountValue += potCount * (mainBot.store.potionToMake.potionPrice - mainBot.store.potionToMake.secondaryPrice);
        }
    }

    public boolean noIngredients() {
        return !Inventory.contains(mainBot.store.herbInfo.unfinishedPot) || !Inventory.contains(mainBot.store.potionToMake.secondaryIngredient);
    }
}
