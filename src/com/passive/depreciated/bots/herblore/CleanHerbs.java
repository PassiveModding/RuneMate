package com.runemate.passive.bots.herblore;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import javafx.application.Platform;

import static com.passive.api.runescape.interfaces.Interfaces.closeInterfacesOrWait;

public class CleanHerbs extends Task {
    public Main mainBot;
    public CleanHerbs(Main bot){
        mainBot = bot;
    }

    @Override
    public boolean validate() {
        return Inventory.contains(mainBot.store.herbInfo.grimy);

    }

    @Override
    public void execute() {
        Platform.runLater(() -> mainBot.store.state.setValue("Cleaning herbs"));
        closeInterfacesOrWait();

        int herbsToClean = Inventory.getItems(mainBot.store.herbInfo.grimy).size();
        mainBot.store.profitCountValue += (herbsToClean * (mainBot.store.herbInfo.cleanPrice - mainBot.store.herbInfo.grimyPrice));

        while (Inventory.getItems(mainBot.store.herbInfo.grimy).first() != null) {
            for (SpriteItem weed : Inventory.getItems(mainBot.store.herbInfo.grimy)) {
                SpriteItem selected = Inventory.getSelectedItem();
                if (selected != null){
                    selected.click();
                }
                weed.interact("Clean");
                Execution.delay(50, 150);
            }
        }
    }
}
