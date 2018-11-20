package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.*;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;

public class CombatInstructor {

    public static void InteractGuide() {

        Npc Guide = Npcs.newQuery().names("Combat Instructor").results().nearest();

        if (Guide == null || !Guide.isVisible()) {
            String ct = GetCompUi.GetCompText();
            if (!ct.startsWith("Combat interface") && !ct.startsWith("Attacking") && !ct.startsWith("Sit back and watch") && !ct.startsWith("Well done")  && !ct.startsWith("Rat ranging")  && !ct.startsWith("Moving on")){
                BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3105, 9507));
                if (p != null) {
                    p.step();
                    return;
                }
            }
        }


        String dialog = ChatDialog.getText();
        String dialogTitle = ChatDialog.getTitle();

        System.out.println("Dialog: " + dialog);
        System.out.println("Dialog Title: " + dialogTitle);

        if (dialog != null){
            ChatDialog.getContinue().select();
            return;
        }

        NonGuideClick(Guide);
    }

    public static void NonGuideClick(Npc Guide) {

        String compText = GetCompUi.GetCompText();

        if (compText != null) {
            if (compText.startsWith("Combat") && compText.contains("this area")) {
                if (Guide.isVisible()){
                    Guide.click();
                } else{
                    Camera.turnTo(Guide);
                }
            } else if (compText.startsWith("Equipping items") && compText.contains("You now have access")){
                com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceWindows.getEquipment().open();
            } else if (compText.startsWith("Worn inventory")) {
                InterfaceComponent eqStats = Interfaces.newQuery().visible().actions("View equipment stats").results().first();

                if (eqStats != null){
                    eqStats.interact("View equipment stats");
                }
            } else if (compText.startsWith("Equipment stats") && compText.contains("You're now holding")){
                InterfaceComponent eqStats = Interfaces.newQuery().visible().actions("Close").results().first();

                if (eqStats != null){
                    eqStats.interact("Close");
                } else{
                    if (Guide.isVisible()){
                        Guide.click();
                    } else{
                        Camera.turnTo(Guide);
                    }
                }
            } else if (compText.startsWith("Equipment stats")) {

                SpriteItem dagger = Inventory.getItems("Bronze dagger").first();

                if (dagger != null){
                    dagger.click();
                }
            } else if (compText.startsWith("Unequipping items")) {

                SpriteItem BronzeSword = Inventory.getItems("Bronze sword").first();
                if (BronzeSword != null){
                    BronzeSword.click();
                }
                SpriteItem WoodShield = Inventory.getItems("Wooden shield").first();
                if (WoodShield != null){
                    WoodShield.click();
                }
            } else if (compText.startsWith("Combat interface") && compText.contains("This is your combat interface")) {
                GameObject gate = GameObjects.newQuery().names("Gate").results().nearestTo(new Coordinate(3111, 9518));

                if (gate == null) {

                    BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3111, 9518));

                    if (p != null){
                        p.step();
                    }
                } else if (!gate.isVisible()) {
                    Camera.turnTo(gate);
                } else {
                    gate.click();
                }
            } else if (compText.startsWith("Combat interface")) {
                InterfaceComponent eqStats = Interfaces.newQuery().visible().actions("Combat Options").results().first();

                if (eqStats != null){
                    eqStats.interact("Combat Options");
                }
            } else if (compText.startsWith("Attacking")) {
                Npc rat = Npcs.newQuery().names("Giant rat").reachable().results().nearest();

                if (rat != null)
                {
                    if (rat.getHealthGauge() == null){
                        if (rat.isVisible()){
                            rat.interact("Attack");
                            //rat.click();
                        } else{
                            Camera.turnTo(rat);
                        }
                    }
                } else {
                    GoToGate();
                }
            } else if (compText.startsWith("Sit back and watch")) {
                Npc rat = Npcs.newQuery().names("Giant rat").results().nearest();

                if (Players.getLocal().getHealthGauge() == null)
                {
                    if (rat != null)
                    {
                        if (rat.getHealthGauge() == null){
                            if (rat.isVisible()){
                                rat.click();
                            } else{
                                Camera.turnTo(rat);
                            }
                        }
                    }
                }
            } else if (compText.startsWith("Well done, you've made")) {
                if (Guide != null){
                    if (!Guide.getPosition().isReachable()){
                        GoToGate();
                    } else {
                        if (Guide.isVisible()){
                            Guide.click();
                        } else{
                            BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3105, 9507));
                            if (p != null) {
                                p.step();
                            }
                        }
                    }
                } else {
                    GoToGate();
                }
            } else if (compText.startsWith("Rat ranging")) {
                if (Equipment.containsAllOf("Shortbow", "Bronze arrow")) {
                    Npc rat = Npcs.newQuery().names("Giant rat").results().nearest();

                    if (rat != null)
                    {
                        if (rat.getHealthGauge() == null){
                            rat.interact("Attack");
                            //rat.click();
                        }
                    }
                } else {
                    if (InterfaceWindows.getInventory().isOpen()){
                        SpriteItem BronzeArrows = Inventory.getItems("Bronze arrow").first();

                        if (BronzeArrows != null){
                            BronzeArrows.click();
                        }

                        SpriteItem Shortbow = Inventory.getItems("Shortbow").first();

                        if (Shortbow != null){
                            Shortbow.click();
                        }

                    } else {
                        InterfaceWindows.getInventory().open();
                    }
                }
            } else if (compText.startsWith("Moving on")) {

                if (compText.contains("you've made your first weapon")){
                    MiningTutor.InteractGuide();
                    return;
                }

                GameObject ladder = GameObjects.newQuery().names("Ladder").results().nearestTo(new Coordinate(3111, 9524));

                if (ladder != null && ladder.isVisible()){
                    ladder.click();
                } else{
                    BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3111, 9524));
                    if (p != null) {
                        p.step();
                    }
                }
            }
        }
    }

    public static void GoToGate(){
        GameObject gate = GameObjects.newQuery().names("Gate").results().nearestTo(new Coordinate(3109, 9518));

        if (gate == null || !gate.isVisible()) {
            BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3109, 9518));

            if (p != null){
                p.step();
            }
        } else {
            gate.click();
        }
    }

}
