package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.script.framework.task.TaskBot;

public class TutorialIsland extends TaskBot {
    @Override
    public void onStart (String... args){
        System.out.println("Running bot");
        setLoopDelay(100, 2000);
        add(new Delays(), new AntiError(), new FixMisclick(), new Randoms(), new Movement(),
                new SkipDialog(), new InitialSetup(), new CreateAppearance(), new GilenorGuide(),
                new SurvivalExpert(), new CookingGuide(), new QuestGuide(), new MiningSmithing(),
                new CombatInstructor(), new Banking(), new AccountManagement(), new Prayer(),
                new Magic(), new Lumbridge(this));
        //super.onStart(args);
    }
}
