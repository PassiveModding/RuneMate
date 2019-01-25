package com.runemate.passive.bots.passiveherblore.framework;

public enum HerbAction {
    GRIMY_ONLY("Use Grimy Herbs Only"),
    GRIMY_AND_CLEAN("Use Grimy and Clean Herbs"),
    CLEAN_ONLY("Use Cleaned Herbs Only");

    public final String displayName;

    HerbAction(String displayName) {
        this.displayName = displayName;
    }

    public static HerbAction getInfo(String DisplayTxt) {
        for (HerbAction herbAction : HerbAction.values()) {
            if (DisplayTxt.equalsIgnoreCase(herbAction.displayName)) {
                return herbAction;
            }
        }

        return null;
    }
}
