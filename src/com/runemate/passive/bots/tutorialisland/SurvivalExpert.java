package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;

import java.util.Random;

import static com.runemate.passive.bots.tutorialisland.Coordinates.*;

public class SurvivalExpert {

    public static void InteractGuide() {
        Npc Guide = Npcs.newQuery().names("Survival Expert").results().nearest();
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
            if (compText.startsWith("Moving around")) {

                if (!Guide.isVisible()) {
                    Camera.turnTo(Guide);
                } else {
                    Guide.click();
                }

            } else if (compText.startsWith("You've been given")){
                System.out.println("Opening inventory");

                com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceWindows.getInventory().open();
                com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces.newQuery().actions("Inventory").results().first().click();
            } else if (compText.startsWith("Fishing")) {
                System.out.println("Fishing Stage");
               // Npcs.newQuery().names("Fishing spot").results().nearest().click();
                System.out.println("Running fishing");
                Coordinate co = new Coordinate(3101, 3092);

                if (co.isVisible()){
                    co.click();
                } else{
                    Camera.turnTo(co);
                }




            } else if (compText.startsWith("You've gained")) {
                com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceWindows.getSkills().open();
                Interfaces.newQuery().actions("Skills").results().first().click();
            } else if (compText.startsWith("Skills and Experience")) {

                if (!Guide.isVisible()) {
                    Camera.turnTo(Guide);
                } else {
                    Guide.click();
                }
            } else if (compText.startsWith("The survival expert gives you")){
                ChatDialog.getContinue().select();
            } else if (compText.startsWith("Woodcutting")) {
                com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceWindows.getInventory().open();
                GameObject tree = GameObjects.newQuery().names("Tree").results().nearest();

                if (tree != null){
                    if (!tree.isVisible()) {
                        Camera.turnTo(tree);
                    } else {
                        tree.click();
                    }
                }
            } else if (compText.startsWith("Firemaking")) {
                SpriteItem item = Inventory.getSelectedItem();

                if (item != null) {
                    item.click();
                }

                GameObject fire = GameObjects.newQuery().names("Fire").results().nearest();
                if (fire != null) {
                    System.out.println("Fire Detected");
                    Coordinate firePos = fire.getPosition();
                    Coordinate playerPos = Players.getLocal().getPosition();
                    if (firePos.getX() == playerPos.getX() && firePos.getY() == playerPos.getY()) {
                        int x, y;
                        System.out.println("Fire on position... moving");
                        if (new Random().nextBoolean()) {
                            x = 1;
                        } else {
                            x = -1;
                        }
                        if (new Random().nextBoolean()) {
                            y = 1;
                        } else {
                            y = -1;
                        }

                        BresenhamPath p = BresenhamPath.buildTo(new Coordinate(fire.getPosition().getX() + x, fire.getPosition().getY() + y));

                        if (p != null) {
                            p.step();
                            Inventory.getItems("Logs").first().click();

                            Inventory.getItems("Tinderbox").first().click();
                        }

                    }
                    else {
                        Inventory.getItems("Logs").first().click();
                        Inventory.getItems("Tinderbox").first().click();
                    }
                } else {
                    Inventory.getItems("Logs").first().click();
                    Inventory.getItems("Tinderbox").first().click();
                }


            } else if (compText.startsWith("Cooking")) {

                if (Inventory.contains("Shrimps")) {
                    MasterChef.InteractGuide();
                    return;
                }


                //Inventory.getItems("Raw shrimps").first().click();
                GameObject fire = GameObjects.newQuery().names("Fire").results().nearest();
                if (fire != null) {
                    if (fire.isVisible()){
                        Inventory.getItems("Raw shrimps").first().click();
                        fire.interact("Use");
                        //fire.click();
                    }
                    else {
                        Camera.turnTo(fire);
                    }
                } else {
                    if (Inventory.contains("Logs")){
                        Inventory.getItems("Logs").first().click();
                        Inventory.getItems("Tinderbox").first().click();
                    } else{
                        GameObject tree = GameObjects.newQuery().names("Tree").results().nearest();

                        if (tree != null){
                            if (!tree.isVisible()) {
                                Camera.turnTo(tree);
                            } else {
                                tree.click();
                            }
                        }
                    }
                }
            } else if (compText.startsWith("Moving on")) {

                if ((Players.getLocal().getPosition().getX() == GateSurvivalSide.getX() && Players.getLocal().getPosition().getY() == GateSurvivalSide.getY()) ||
                        (Players.getLocal().getPosition().getX() == GateSurvivalSide1.getX() && Players.getLocal().getPosition().getY() == GateSurvivalSide1.getY())) {
                    System.out.println("Finding gate");
                    GameObject gate = GameObjects.newQuery().names("Gate").results().nearest();

                    if (gate != null) {

                        if (!gate.isVisible()) {
                            Camera.turnTo(gate);
                        } else {
                            gate.click();
                        }
                    }
                } else if ((Players.getLocal().getPosition().getX() == GateCookingSide.getX() && Players.getLocal().getPosition().getY() == GateCookingSide.getY()) ||
                        (Players.getLocal().getPosition().getX() == GateCookingSide1.getX() && Players.getLocal().getPosition().getY() == GateCookingSide1.getY())) {
                    // Go to the cooking door
                    BresenhamPath p = BresenhamPath.buildTo(GateCookingOutside);

                    if (p != null) {
                        p.step();
                    }
                } else if (Npcs.newQuery().names("Survival Expert").reachable().results().nearest() != null) {
                    System.out.println("Going to gate location");
                    BresenhamPath p = BresenhamPath.buildTo(GateCookingSide);
                    if (p != null) {
                        System.out.println("Moving to gate location");
                        p.step();
                    }
                } else {
                    System.out.println("Going to gate location");
                    BresenhamPath p = BresenhamPath.buildTo(GateCookingOutside);
                    GameObject door = GameObjects.newQuery().names("Door").reachable().results().nearest();

                    if (door != null) {
                        door.click();
                    } else {
                        if (p != null) {
                            System.out.println("Moving to door");
                            p.step();
                        }
                    }
                }


                /*
                WebPath trav = Traversal.getDefaultWeb().getPathBuilder().useTeleports(false).buildTo(new Coordinate(3079, 64));

                if (trav != null)
                {
                    trav.step(false);
                }

                /*System.out.println("Moving to gate");
                GameObject gate = GameObjects.newQuery().names("Gate").results().nearest();

                if (Players.getLocal().getPosition().getX() <= 3089){
                    System.out.println("Cook Path available");
                    BresenhamPath dpath = BresenhamPath.buildTo(new Coordinate(3079, 64));
                    dpath.step();

                    T
                }
                else {
                    if (gate != null) {
                        System.out.println("Gate found");
                        BresenhamPath p = BresenhamPath.buildTo(gate.getPosition());
                        if (p != null) {
                            System.out.println("Gate Path available");
                            p.step();
                            if (gate.isVisible()) {
                                gate.click();
                            }
                        }
                    } else {
                        BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3089, 64));
                        if (p != null) {
                            System.out.println("Gate Path available");
                            p.step();
                        }
                    }
                }*/
            }
        }
    }
}
