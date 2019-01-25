package com.passive.api.runescape.playersense;

public class PlayerSense {
    public static boolean useBankHotKeys(){
        return com.runemate.game.api.hybrid.player_sense.PlayerSense.getAsBoolean(com.runemate.game.api.hybrid.player_sense.PlayerSense.Key.USE_BANK_HOTKEYS);
    }

    public static boolean closeBankWithEscape(){ return com.runemate.game.api.hybrid.player_sense.PlayerSense.getAsBoolean(com.runemate.game.api.hybrid.player_sense.PlayerSense.Key.CLOSE_BANK_WITH_ESCAPE); }

    public static boolean dismissEventNpcs(){
        return com.runemate.game.api.hybrid.player_sense.PlayerSense.getAsBoolean(com.runemate.game.api.hybrid.player_sense.PlayerSense.Key.DISMISS_EVENT_NPCS);
    }

    public static boolean moveCameraWithMouse(){ return com.runemate.game.api.hybrid.player_sense.PlayerSense.getAsBoolean(com.runemate.game.api.hybrid.player_sense.PlayerSense.Key.MOVE_CAMERA_WITH_MOUSE); }

    public static boolean overBuyFromShop(){
        return com.runemate.game.api.hybrid.player_sense.PlayerSense.getAsBoolean(com.runemate.game.api.hybrid.player_sense.PlayerSense.Key.OVERBUY_FROM_SHOP);
    }

    public static boolean useNumpad(){
        return com.runemate.game.api.hybrid.player_sense.PlayerSense.getAsBoolean(com.runemate.game.api.hybrid.player_sense.PlayerSense.Key.USE_NUMPAD);
    }

    public static boolean useUnwantedInterfaceCloserHotkeys(){ return com.runemate.game.api.hybrid.player_sense.PlayerSense.getAsBoolean(com.runemate.game.api.hybrid.player_sense.PlayerSense.Key.USE_UNWANTED_INTERFACE_CLOSER_HOTKEYS); }

    public static boolean useWASDKeys(){
        return com.runemate.game.api.hybrid.player_sense.PlayerSense.getAsBoolean(com.runemate.game.api.hybrid.player_sense.PlayerSense.Key.USE_WASD_KEYS);
    }

    public static boolean withdrawFromBankViaSearch(){ return com.runemate.game.api.hybrid.player_sense.PlayerSense.getAsBoolean(com.runemate.game.api.hybrid.player_sense.PlayerSense.Key.WITHDRAW_FROM_BANK_VIA_SEARCH); }
}
