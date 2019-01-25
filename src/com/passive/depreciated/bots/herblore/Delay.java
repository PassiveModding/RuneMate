package com.runemate.passive.bots.herblore;

import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import javafx.application.Platform;

public class Delay extends Task {
    public Main mainBot;
    public Delay(Main bot){
        mainBot = bot;
    }

    @Override
    public boolean validate() {
        if (mainBot.store.herbInfo == null || mainBot.store.run == false) {
            return true;
        }

        System.out.println("---INFO---\nclean:" + mainBot.store.herbInfo.clean +
                "\ngrimy:" + mainBot.store.herbInfo.grimy +
                "\nVial:" + mainBot.store.herbInfo.vialContents +
                "\nUnfinished:" + mainBot.store.herbInfo.unfinishedPot +
                "\nRequired level:" + mainBot.store.herbInfo.level() +
                "\n---EXTRA---\nclean Herbs:" + mainBot.store.herbAction.displayName +
                "\nstate:" + mainBot.store.state.getValue() +
                "\nPotionFinal:" + mainBot.store.potionToMake == null);

        return false;
    }

    @Override
    public void execute() {
        Platform.runLater(() -> mainBot.store.state.setValue("Waiting for Setup..."));
        Execution.delay(100);
    }
}
