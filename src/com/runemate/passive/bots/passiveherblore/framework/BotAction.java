package com.runemate.passive.bots.passiveherblore.framework;

public enum BotAction {
    CLEAN_HERBS("Clean Herbs"),
    MAKE_UNFINISHED_POTIONS("Make Unfinished Potions"),
    MAKE_FINISHED_POTIONS("Make Finished Potions");

    private final String message;

    BotAction(String message){
        this.message = message;
    }

    public String getMessage() { return this.message; }
    public static BotAction getInfo(String message){
        for (BotAction act: BotAction.values()){
            if (act.message.equalsIgnoreCase(message)){
                return act;
            }
        }

        return null;
    }
}
