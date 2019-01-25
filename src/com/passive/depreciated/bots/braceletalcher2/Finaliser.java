package com.runemate.passive.bots.braceletalcher2;

import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.task.Task;

public class Finaliser extends Task {
    public Main mainBot;
    public Finaliser(Main bot){
        mainBot = bot;
    }


    @Override
    public void execute() {
        mainBot.stop("Failed finaliser tests");
    }

    @Override
    public boolean validate() {
        System.out.println("---INFO---\nBracelet state: " + mainBot.OutOfBracelets.toString() + "\nBuy Price: " + mainBot.buyPrice + "\nMax Buy Price: " + mainBot.maxBuyPrice + "\n----------");

        if (!Equipment.contains("Staff of fire")){
            mainBot.stop("You must equip a staff of fire to begin");
            return true;
        }

        if (!Inventory.contains("Nature rune")){
            mainBot.stop("Please place all nature runes in your inventory");
            return true;
        }

        if (Skill.MAGIC.getBaseLevel() < 55){
            mainBot.stop("Magic level must be high enough to cast the high alchemy spell");
            return true;
        }

        return false;
    }
}
