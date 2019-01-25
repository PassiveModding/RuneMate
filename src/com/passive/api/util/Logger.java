package com.passive.api.util;

import com.passive.api.bot.Passive_BOT;

public class Logger {
    private final Passive_BOT bot;

    public Logger() {
        this.bot = Passive_BOT.getBot();
    }

    public void log(String message) {
        System.out.println(bot.getFormattedRuntime() + " - " + message);
    }
}
