package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.framework.task.Task;

import static com.passive.api.runescape.navigation.Travel.TravelTo;
import static com.passive.api.runescape.npcs.Npcs.TalkTo;

public class MiningSmithing extends Task {
    GetContextItems.Context cont;

    @Override
    public void execute() {
        Npc instructor = Npcs.newQuery().names("Mining Instructor").reachable().results().first();
        if (instructor != null) {
            if (cont.altChatText.startsWith("Mining and Smithing")) {
                if (instructor.isVisible()) {
                    TalkTo(instructor);
                } else {
                    Camera.turnTo(instructor);
                }
            } else if (cont.altChatText.startsWith("Mining") && cont.altChatText.contains("Now that you have some tin ore")) {
                //TODO: Dynamically check rocks rather than relying on co-ordinates
                //TODO: Additionally also use the 'mine' interaction on rocks to reduce mis-clicks
                Coordinate Copper = new Coordinate(3083, 9501);
                if (Copper.isVisible()) {
                    Copper.click();
                } else {
                    Camera.turnTo(Copper);
                }
            } else if (cont.altChatText.startsWith("Mining")) {
                //TODO: Dynamically check rocks rather than relying on co-ordinates
                Coordinate Tin = new Coordinate(3077, 9504);
                if (Tin.isVisible()) {
                    Tin.click();
                } else {
                    Camera.turnTo(Tin);
                }
            } else if (cont.altChatText.startsWith("Smelting") && cont.altChatText.contains("You've made a bronze bar")) {
                if (instructor.isVisible()) {
                    instructor.click();
                } else {
                    Camera.turnTo(instructor);
                }
            } else if (cont.altChatText.startsWith("Smelting")) {
                GameObject furnace = GameObjects.newQuery().names("Furnace").results().nearest();

                if (furnace != null) {
                    if (furnace.isVisible()) {
                        furnace.interact("Use");
                    } else {
                        Camera.turnTo(furnace);
                    }
                }
            } else if (cont.altChatText.startsWith("Smithing a dagger") && cont.altChatText.contains("Now you have the smithing menu")){
                InterfaceComponent smithInt = Interfaces.newQuery().visible().actions("Smith 1").results().first();
                if (smithInt != null){
                    smithInt.interact("Smith 1", "Bronze dagger");
                } else{
                    InteractAnvil();
                }
            } else if (cont.altChatText.startsWith("Smithing a dagger")){
                InteractAnvil();
            } else if (cont.altChatText.startsWith("Moving on")) {
                MovingOn();
            }
        } else {
            TravelTo(Positions.MiningInstructor);
        }
    }

    public void MovingOn(){
        GameObject gate = GameObjects.newQuery().names("Gate").results().nearest();
        if (gate != null){
            if (gate.isVisible()){
                //gate.click();
                gate.interact("Open");
            } else {
                Camera.turnTo(gate);
            }
        } else {
            TravelTo(Positions.MiningTutorGateExit);
        }
    }


    public void InteractAnvil(){
        GameObject anvil = GameObjects.newQuery().names("Anvil").results().nearest();

        if (anvil != null){
            if (anvil.isVisible()){
                anvil.interact("Smith");
            } else{
                Camera.turnTo(anvil);
            }
        }
    }

    @Override
    public boolean validate() {
        cont = GetContextItems.GetContextItems();

        if (cont.altChatText.startsWith("Mining and Smithing")){
            System.out.println("Mining and Smithing");
            return true;
        }

        Npc instructor = Npcs.newQuery().names("Mining Instructor").reachable().results().first();
        if (instructor != null){
            System.out.println("Mining and Smithing");
            return true;
        }

        return false;
    }
}
