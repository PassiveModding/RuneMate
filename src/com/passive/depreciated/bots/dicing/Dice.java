package com.runemate.passive.bots.dicing;

import com.runemate.game.api.script.framework.task.Task;
import com.runemate.passive.bots.dicing.ui.PassiveDicerUIController;

public class Dice extends Task {
    @Override
    public boolean validate() {
        if (SessionData.currentUser != null){
            return true;
        }

        return false;
    }

    @Override
    public void execute() {
        Logger.Log("Dicing: " + SessionData.currentUser);
        SessionData.RollResult res = SessionData.Roll();

        if (res.Win){
            //
        } else {
            Logger.Log("Rolled: " + res.RollNumber + " Player Lost: " + res.user, true);
            PassiveDicerUIController.DiceResult x = new PassiveDicerUIController.DiceResult(res.user, res.BetAmount, 0, SessionData.GetCurrentBalance());

            PassiveDicerUIController.historyValues.add(x);
        }

    }


}
