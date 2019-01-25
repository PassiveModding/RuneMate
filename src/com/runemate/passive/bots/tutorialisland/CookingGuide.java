package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.framework.task.Task;

import static com.passive.api.runescape.navigation.Travel.TravelTo;
import static com.passive.api.runescape.npcs.Npcs.TalkTo;

public class CookingGuide extends Task {
    GetContextItems.Context cont;

    @Override
    public void execute() {
        cont = GetContextItems.GetContextItems();

        // Because we are still in the room at the end of this stage, ensure the initial movement to the area
        // is only done when bread is not included in the text
        if (cont.altChatText.startsWith("Moving on") && !cont.altChatText.contains("bread")){
            GameObject door = GameObjects.newQuery().names("Door").reachable().results().first();
            if (door != null){
                if (door.isVisible()){
                    door.interact("Open");
                } else {
                    Camera.turnTo(door);
                }
            } else {
                TravelTo(Positions.CookGuideStartDoorOutside);
            }
        }

        Npc cook = Npcs.newQuery().names("Master Chef").reachable().results().first();

        if (cook != null){
            // This is placed above the 'Cooking' check because StartsWith 'Cooking' will be true for both
            if (cont.altChatText.startsWith("Cooking dough")){
                GameObject range = GameObjects.newQuery().names("Range").results().nearest();
                if (range != null){
                    if (range.isVisible()){
                        range.interact("Cook");
                    } else {
                        Camera.turnTo(range);
                    }
                }
            } else if (cont.altChatText.startsWith("Cooking")){
                TalkTo(cook);
                return;
            } else if (cont.altChatText.startsWith("Making dough")) {
                SpriteItem potOfFlour = Inventory.getItems("Pot of flour").first();
                SpriteItem bucketOfWater = Inventory.getItems("Bucket of water").first();

                /*
                if (Inventory.getSelectedItem() != null){
                    Inventory.getSelectedItem().click();
                    return;
                }
                */

                if (potOfFlour != null && bucketOfWater != null){

                    SpriteItem selected = Inventory.getSelectedItem();
                    if (selected != null){
                        if (selected.getId() == potOfFlour.getId()){
                            bucketOfWater.click();
                        } else if (selected.getId() == bucketOfWater.getId()){
                            potOfFlour.click();
                        } else {
                            selected.click();
                        }
                    } else {
                        potOfFlour.click();
                        bucketOfWater.click();
                    }
                } else {
                    TalkTo(cook);
                }

                return;
            } else if (cont.altChatText.startsWith("Moving on")){
                MovingOn();
            }
        }
    }

    public void MovingOn(){
        GameObject door = GameObjects.newQuery().names("Door").results().nearest();
        if (door != null){
            if (door.distanceTo(Positions.CookGuideExitDoorInside) <= 3){
                if (door.isVisible()){
                    door.interact("Open");
                } else {
                    Camera.turnTo(door);
                }
            } else {
                TravelTo(Positions.CookGuideExitDoorInside);
            }
        } else {
            TravelTo(Positions.CookGuideExitDoorInside);
        }

    }

    @Override
    public boolean validate() {

        Npc cook = Npcs.newQuery().names("Master Chef").reachable().results().first();
        if (cook != null){
            System.out.println("Cooking Guide");
            return true;
        }

        if (Positions.CookGuideStartDoorOutside.isReachable()){
            System.out.println("Cooking Guide Travel");
            return true;
        }

        return false;
    }
}
