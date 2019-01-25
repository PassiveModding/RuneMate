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

public class MakePots extends Task {
    public Main mainBot;
    public MakePots(Main bot){ mainBot = bot; }

    @Override
    public boolean validate() {
        return Inventory.contains(mainBot.store.herbInfo.clean) && Inventory.contains(mainBot.store.herbInfo.vialContents);
    }

    @Override
    public void execute() {
        Platform.runLater(() -> mainBot.store.state.setValue("Making Unfinished Potions"));

        System.out.println("Make pots");
        closeInterfacesOrWait();

        SpriteItem cleanHerb = Inventory.getItems(mainBot.store.herbInfo.clean).first();
        SpriteItem vialWater = Inventory.getItems(mainBot.store.herbInfo.vialContents).last();
        if (cleanHerb != null && vialWater != null) {
            SpriteItem selected = Inventory.getSelectedItem();
            if (selected != null){
                ItemDefinition def = selected.getDefinition();
                if (def != null){
                    String name = def.getName();
                    if (name != null){
                        if (name == mainBot.store.herbInfo.clean) {
                            randomCombine(selected, vialWater);
                        } else if (name == mainBot.store.herbInfo.vialContents) {
                            randomCombine(cleanHerb, selected);
                        } else {
                            randomCombine(cleanHerb, vialWater);
                        }
                    }
                }
            } else {
                randomCombine(cleanHerb, vialWater);
            }

            Execution.delayUntil(() -> MakeAllInterface.isOpen(), 250, 3000);
        }


        if (MakeAllInterface.isOpen()){
            ensureMakingAll();
        }
    }

    public void ensureMakingAll() {
        if (!MakeAllInterface.isOpen()){
            execute();
            return;
        }

        while (MakeAllInterface.isOpen()){
            MakeAllInterface.selectItem(mainBot.store.herbInfo.unfinishedPot);
            Execution.delay(100, 500);
        }

        Execution.delayUntil(() -> noIngredients(), 8500, 11500);

        if (!noIngredients()) {
            ensureMakingAll();
        } else {
            int potCount = Inventory.getQuantity(mainBot.store.herbInfo.unfinishedPot);
            mainBot.store.potCountValue += potCount;
            mainBot.store.profitCountValue += potCount * (mainBot.store.herbInfo.unfinishedPrice - mainBot.store.herbInfo.cleanPrice);
        }
    }

    public boolean noIngredients() {
        return !Inventory.contains(mainBot.store.herbInfo.vialContents) || !Inventory.contains(mainBot.store.herbInfo.clean);
    }
}
