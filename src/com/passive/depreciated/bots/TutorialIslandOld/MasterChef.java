package com.runemate.passive.bots.TutorialIslandOld;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;

import static com.runemate.passive.bots.TutorialIslandOld.Coordinates.QuestGuideDoorOutside;

public class MasterChef {

    public static void InteractGuide() {
        Npc Guide = Npcs.newQuery().names("Master Chef").results().nearest();
        String dialog = ChatDialog.getText();

        SpriteItem bucket = Inventory.getItems("Bucket of water").first();
        if (bucket != null)
        {
            if (Inventory.getSelectedItem() != null)
            {
                Inventory.getSelectedItem().click();
            }

            bucket.click();
            SpriteItem flour = Inventory.getItems("Pot of flour").first();
            flour.click();
        }

        if (dialog != null){
            ChatDialog.getContinue().select();
            return;
        }

        NonGuideClick(Guide);
    }

    public static void NonGuideClick(Npc Guide) {

        String compText = GetCompUi.GetCompText();

        if (compText != null) {
            if (compText.startsWith("Moving on")) {
                if (compText.contains("bread")){
                    GameObject door = GameObjects.newQuery().names("Door").results().nearestTo(Coordinates.ChefExitInside);
                    if (door != null) {
                        if (door.isVisible()){
                            door.click();
                        } else {
                            Camera.turnTo(door);
                            BresenhamPath p = BresenhamPath.buildTo(Coordinates.ChefExitInside);
                            if (p != null){
                                p.step();
                            }
                        }
                    }
                } else{
                    GameObject door = GameObjects.newQuery().names("Door").reachable().results().nearestTo(Coordinates.ChefExitInside);
                    if (door != null) {
                        if (door.isVisible()){
                            door.click();
                        } else {
                            Camera.turnTo(door);
                            BresenhamPath p = BresenhamPath.buildTo(Coordinates.ChefExitInside);
                            if (p != null){
                                p.step();
                            }
                        }
                    } else{
                        BresenhamPath p = BresenhamPath.buildTo(Coordinates.ChefExitInside);
                        if (p != null){
                            p.step();
                        }
                    }
                }

            } else if (compText.startsWith("Cooking dough")){
                System.out.println("Getting range");
                GameObject range = GameObjects.newQuery().names("Range").results().nearest();
                if (range != null)
                {
                    SpriteItem dough = Inventory.getItems("Bread dough").first();
                    if (dough != null)
                    {
                        System.out.println("Getting dough");
                        if (Inventory.getSelectedItem() != null)
                        {
                            Inventory.getSelectedItem().click();
                        }

                        if (range.isVisible()){
                            dough.click();
                            range.click();
                        } else{
                            Camera.turnTo(range);
                        }
                    }
                }
            }
            else if (compText.startsWith("Cooking")){
                Guide.click();
            } else if (compText.startsWith("Fancy a run?")){
                BresenhamPath p = BresenhamPath.buildTo(QuestGuideDoorOutside);

                if (p != null) {
                    p.step();
                    TutorialIsland.StateOverride = "QuestMasterRun";
                }
            }
        }
    }
}
