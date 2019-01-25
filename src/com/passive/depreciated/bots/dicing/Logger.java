package com.runemate.passive.bots.dicing;

import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox;

public class Logger {

    public static void Log(String message){
        Log(message, false);
    }


    public static void Log(String message, boolean typed){
        System.out.println(message);

        if (typed){
            com.runemate.game.api.hybrid.input.Keyboard.type(message, true);
        }
    }
}
