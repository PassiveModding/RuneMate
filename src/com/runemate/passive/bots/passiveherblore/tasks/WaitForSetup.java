package com.runemate.passive.bots.passiveherblore.tasks;

import com.passive.api.bot.task_bot.Task;
import com.runemate.game.api.script.Execution;
import com.runemate.passive.bots.passiveherblore.Main;

public class WaitForSetup extends Task {
    public WaitForSetup(Main bot){ this.mainBot = bot; }
    public Main mainBot;

    @Override
    public boolean validate() {
        return mainBot.info.run == false || mainBot.info.herbInfo == null;

    }

    @Override
    public void execute() {
        bot.setStatus("Waiting for setup...");
        Execution.delay(500);
    }
}
