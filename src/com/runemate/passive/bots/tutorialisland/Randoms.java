package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.framework.task.Task;

import java.util.Random;

public class Randoms extends Task {

    @Override
    public void execute() {
        boolean rnd = new Random().nextBoolean();
        boolean rnd2 = new Random().nextBoolean();
        boolean rnd3 = new Random().nextBoolean();
        if (rnd2){
            Mouse.scroll(true);
        } else {
            Camera.turnTo(GameObjects.newQuery().results().random());
        }
    }

    @Override
    public boolean validate() {
        int rnd = new Random().nextInt(20);
        if (rnd <= 2){
            return true;
        }

        return false;
    }
}
