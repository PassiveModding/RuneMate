package com.runemate.passive.bots.TutorialIslandOld;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;

public class MiningTutor {

    public static void InteractGuide() {

        Npc Guide = Npcs.newQuery().names("Mining Instructor").results().nearest();

        if (Guide == null) {
            if (TutorialIsland.StateOverride == "MiningInstructor") {
                BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3079,9505));
                if (p != null) {
                    p.step();
                    TutorialIsland.StateOverride = "MiningInstructor";
                }

                return;
            }
        }


        String dialog = ChatDialog.getText();

        if (dialog != null){
            if (ChatDialog.getContinue() != null){
                ChatDialog.getContinue().select();
            }
            return;
        }

        NonGuideClick(Guide);
    }

    public static void NonGuideClick(Npc Guide) {

        String compText = GetCompUi.GetCompText();

        if (compText != null) {
            if (compText.startsWith("Mining and Smithing")) {
                if (Guide == null || !Guide.isVisible())
                {
                    BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3079,9505));
                    System.out.println("Mining Instructor not found, traversing...");
                    if (p != null) {
                        System.out.println("Traversing...");
                        p.step();
                    }
                } else{
                    if (Guide.isVisible()){
                        Guide.click();
                    } else{
                        Camera.turnTo(Guide);
                    }
                }
            } else if (compText.startsWith("Mining") && compText.contains("Now that you have some tin ore")) {
                Coordinate Copper = new Coordinate(3083, 9501);
                if (Copper.isVisible()){
                    Copper.click();
                } else{
                    Camera.turnTo(Copper);
                }
            } else if (compText.startsWith("Mining")) {
                Coordinate Tin = new Coordinate(3077, 9504);
                if (Tin.isVisible()){
                    Tin.click();
                } else{
                    Camera.turnTo(Tin);
                }
            } else if (compText.startsWith("Smelting") && compText.contains("You've made a bronze bar")) {
                if (Guide != null){
                    if (Guide.isVisible()){
                        Guide.click();
                    } else{
                        Camera.turnTo(Guide);
                    }
                }
            } else if (compText.startsWith("Smelting")) {
                GameObject furnace = GameObjects.newQuery().names("Furnace").results().nearest();

                if (furnace != null){
                    if (furnace.isVisible()){
                        furnace.click();
                    } else{
                        Camera.turnTo(furnace);
                    }
            }
            } else if (compText.startsWith("Smithing a dagger") && compText.contains("Now you have the smithing menu")){
                InterfaceComponent smithInt = Interfaces.newQuery().visible().actions("Smith 1").results().first();
                if (smithInt != null){
                    smithInt.interact("Smith 1", "Bronze dagger");
                } else{
                    GameObject anvil = GameObjects.newQuery().names("Anvil").results().nearest();

                    if (anvil != null){
                        if (anvil.isVisible()){
                            anvil.click();
                        } else{
                            Camera.turnTo(anvil);
                        }
                    }
                }
            } else if (compText.startsWith("Smithing a dagger")){
                GameObject anvil = GameObjects.newQuery().names("Anvil").results().nearest();

                if (anvil != null){
                    if (anvil.isVisible()){
                        anvil.click();
                    } else{
                        Camera.turnTo(anvil);
                    }
                }
            } else if (compText.startsWith("Moving on")){
                GameObject Gate = GameObjects.newQuery().names("Gate").visible().results().nearest();
                if (Gate != null){
                    if (Gate.isVisible()){
                        Gate.click();
                    } else{
                        Camera.turnTo(Gate);
                    }
                    TutorialIsland.StateOverride = null;
                } else{
                    BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3094, 9503));
                    if (p != null){
                        p.step();
                    }
                }
            }
        }
    }
}
