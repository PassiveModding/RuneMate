package com.passive.api.runescape.interfaces;

import com.passive.api.runescape.playersense.CustomPlayerSense;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.net.GrandExchange;
import com.runemate.game.api.hybrid.player_sense.PlayerSense;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.osrs.local.hud.interfaces.MakeAllInterface;
import com.runemate.game.api.script.Execution;

import java.util.List;

public class Interfaces {
    public static boolean openBankOrWait(){
        if (Bank.isOpen()){
            return true;
        }

        for (int i = 0; i < 2; i++) {
            if (Bank.isOpen()){
                return true;
            }

            if (GrandExchange.isOpen()){
                closeInterfacesOrWait();
            }

            /*
            GameObject geBooth = GameObjects.newQuery().names("Grand Exchange booth").actions("Bank").results().nearest();
            GameObject bankBooth = GameObjects.newQuery().names("Bank booth").results().nearest();
            Npc banker = Npcs.newQuery().names("Banker").results().nearest();
            boolean skip = false;
            if (geBooth != null) {
                if (geBooth.isVisible()) {
                    if (geBooth.interact("Bank")) {
                        skip = true;
                    }
                } else {
                    Camera.turnTo(geBooth);
                }
            }

            if (bankBooth != null && !skip) {
                if (bankBooth.isVisible()) {
                    bankBooth.click();
                    skip = true;
                } else {
                    Camera.turnTo(bankBooth);
                }
            }

            if (banker != null && !skip) {
                if (banker.isVisible()) {
                    banker.interact("Bank");
                } else {
                    Camera.turnTo(banker);
                }
            }

            Bank.open();
            */

            GameObject geBooth = GameObjects.newQuery().names("Grand Exchange booth").actions("Bank").results().nearest();
            boolean skip = false;
            if (geBooth != null){
                if (geBooth.interact("Bank")) {
                    Execution.delayUntil(() -> Bank.isOpen(), 150, 300);
                    if (Bank.isOpen()) {
                        skip = true;
                    }
                }
            }

            if (!skip){
                if (Bank.open()){
                    return true;
                }
            }

            Execution.delay(150, 300);
        }

        return Bank.isOpen();
    }

    /*
    public static void openBankOrWait() {
        while (!Bank.isOpen()){
            if (GrandExchange.isOpen()){
                GrandExchange.close();
                Execution.delay(250, 2000);
            }

            GameObject geBooth = GameObjects.newQuery().names("Grand Exchange booth").actions("Bank").results().nearest();
            GameObject bankBooth = GameObjects.newQuery().names("Bank booth").results().nearest();
            Npc banker = Npcs.newQuery().names("Banker").results().nearest();
            boolean skip = false;
            if (geBooth != null) {
                if (geBooth.isVisible()) {
                    if (geBooth.interact("Bank")) {
                        skip = true;
                    }
                } else {
                    Camera.turnTo(geBooth);
                }
            }

            if (bankBooth != null && !skip) {
                if (bankBooth.isVisible()) {
                    bankBooth.click();
                    skip = true;
                } else {
                    Camera.turnTo(bankBooth);
                }
            }

            if (banker != null && !skip) {
                if (banker.isVisible()) {
                    banker.interact("Bank");
                } else {
                    Camera.turnTo(banker);
                }
            }

            Bank.open();
            Execution.delay(150, 300);
        }
    }
    */

    public static boolean ensureMakingAll(String item, List<String> ingredients){
        return ensureMakingAll(item, ingredients, 0);
    }

    public static boolean ensureMakingAll(String item, List<String> ingredients, int retryCount){
        if (retryCount > 5){
            return false;
        }

        if (!MakeAllInterface.isOpen()) {
            return false;
        }

        if (MakeAllInterface.isOpen()) {
            Boolean useHotKey = CustomPlayerSense.Key.MAKE_ALL_INTERFACE_HOTKEY.getAsBoolean();
            if (useHotKey != null){
                MakeAllInterface.selectItem(item, useHotKey);
            } else {
                MakeAllInterface.selectItem(item);
            }
            Execution.delay(100, 500);
        }

        if (MakeAllInterface.isOpen()){
            return ensureMakingAll(item, ingredients, retryCount + 1);
        }

        Execution.delayUntil(() -> noIngredients(ingredients), 10000, 12000);

        return noIngredients(ingredients);

    }

    private static boolean noIngredients(List<String> ingredients){
        for(String ingredient:ingredients){
            if (!Inventory.contains(ingredient)){
                return true;
            }
        }
        return false;
    }

    public static boolean closeInterfacesOrWait() {
        if (!GrandExchange.isOpen() && !Bank.isOpen()){
            return true;
        }

        for (int i = 0; i < 2; i++) {
            if (Bank.isOpen() || GrandExchange.isOpen()){
                if (Bank.isOpen()){
                    //Bank.close();
                    Bank.close(PlayerSense.getAsBoolean(PlayerSense.Key.USE_BANK_HOTKEYS));
                    Execution.delayUntil(() -> !Bank.isOpen(), 250, 2000);
                }

                if (GrandExchange.isOpen()){
                    GrandExchange.close();
                    Execution.delayUntil(() -> !GrandExchange.isOpen(), 250, 2000);
                }
            } else {
                return true;
            }
        }

        return !Bank.isOpen() && !GrandExchange.isOpen();
    }

    public static boolean openGEOrWait() {
        if (GrandExchange.isOpen()){
            return true;
        }

        for (int i = 0; i < 2; i++) {
            if (GrandExchange.isOpen()){
                return true;
            }

            closeInterfacesOrWait();
            GameObject geBooth = GameObjects.newQuery().names("Grand Exchange booth").actions("Exchange").results().nearest();
            if (geBooth != null){
                if (!geBooth.isVisible()){
                    Camera.turnTo(geBooth);
                }

                geBooth.interact("Exchange");
                Execution.delayUntil(() -> GrandExchange.isOpen(), 250, 2000);
            }

            if (!GrandExchange.isOpen()){
                GrandExchange.open();
                Execution.delayUntil(() -> GrandExchange.isOpen(), 250, 2000);
            } else {
                return true;
            }
        }

        return GrandExchange.isOpen();
    }
}
