package com.runemate.passive.bots.passiveherblore;

import com.passive.api.bot.task_bot.PassiveTaskBot;
import com.runemate.game.api.script.framework.core.LoopingThread;
import com.runemate.passive.bots.passiveherblore.tasks.*;
import com.runemate.passive.bots.passiveherblore.ui.Local_UI;
import javafx.scene.control.TitledPane;

public class Main extends PassiveTaskBot {
    public Info info;
    public Main(){
        info = new Info();
    }

    @Override
    public boolean onStart(){

        TitledPane localUi = new Local_UI(this);
        addAdditionalNode(localUi);
        getUi().addContent(0, localUi);
        addTasks(new WaitForSetup(this), new Finisher(this), new BankTask(this), new CleanHerbs(this), new MakeFinishedPots(this), new MakeUnfinishedPots(this), new Fallback(this));

        new LoopingThread(() -> {
            info.updateLabels();
        }, 1000).start();

        return true;
    }
}
