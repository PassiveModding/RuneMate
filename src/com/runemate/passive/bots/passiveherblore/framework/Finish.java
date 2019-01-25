package com.runemate.passive.bots.passiveherblore.framework;

public enum Finish {
    LOGOUT_ON_FINISH("Logout when finished"),
    PAUSE_ON_FINISH("Pause when finished"),
    IDLE_ON_FINISH("Idle bot when finished");

    public final String message;

    Finish(String display) {
        this.message = display;
    }

    public static Finish getInfo(String message) {
        for (Finish finish : Finish.values()) {
            if (message.equalsIgnoreCase(finish.message)) {
                return finish;
            }
        }

        return null;
    }
}
