package com.runemate.passive.bots.dicing;

import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.util.Calendar;
import java.util.Random;

public class Advertise extends Task {
    @Override
    public boolean validate() {
        if (SessionData.currentUser == null){
            return true;
        }

        return false;
    }

    public String fixInt(int input){
        if (input < 10){
            return "0"+input;
        }

        return ""+input;
    }

    public String GetTime(){
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if (hours > 12){
            hours = hours - 12;
        }



        return "" + fixInt(hours) + ":" + fixInt(calendar.get(Calendar.MINUTE)) + ":" + fixInt(calendar.get(Calendar.SECOND));
    }

    @Override
    public void execute() {
        Random rnd = new Random();

        if (rnd.nextBoolean()){
            if (rnd.nextBoolean()){
                Logger.Log(  "glow3:|" + GetTime() + "| [Win Money]Trade " + SessionData.GetQuantityBoundsString() + "Odds to Win " + SessionData.Multiplier + "x Money are" + SessionData.GetBoundsString(), true);
            } else {
                Logger.Log( "glow3:|"+ GetTime() + "| Random Roll 1-100, if it's within " + SessionData.GetBoundsString() + " you win " + SessionData.Multiplier + "x Money", true);
                //Logger.Log(GetTime() + "[Dicing]Bet" + SessionData.GetQuantityBoundsString() + "Odds" + SessionData.GetBoundsString(), true);
            }
        } else {
            if (rnd.nextBoolean()){
                Logger.Log("glow3:|" + GetTime() + "| [Dicing]Bet" + SessionData.GetQuantityBoundsString() + "Return[" + SessionData.Multiplier + "x] Odds" + SessionData.GetBoundsString(), true);
            } else {
                Logger.Log("glow3:|" + GetTime() + "| Trade between " + SessionData.GetQuantityBoundsString() + "to begin the game!", true);
            }
        }
        Execution.delay(3500, 7000);
    }
}
