package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.*;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.framework.task.Task;

import static com.passive.api.runescape.navigation.Travel.TravelTo;
import static com.passive.api.runescape.npcs.Npcs.TalkTo;

public class SurvivalExpert extends Task {
    GetContextItems.Context cont;

    @Override
    public boolean validate() {
        cont = GetContextItems.GetContextItems();
        Npc expert = Npcs.newQuery().names("Survival Expert").results().nearest();
        if (cont.altChatText.startsWith("Moving around")){
            System.out.println("Survival Expert");
            return true;
        } else if (expert != null){
            if (expert.getPosition().isReachable()){
                System.out.println("Survival Expert");
                return true;
            }
        }

        return false;
    }

    @Override
    public void execute() {
        Npc Expert = Npcs.newQuery().names("Survival Expert").reachable().results().first();

        if (Expert != null){
            if (Expert.isVisible() || cont.altChatText.startsWith("Moving on")){
                if (cont.altChatText.startsWith("Moving around") || cont.altChatText.startsWith("Skills and Experience")) {
                    TalkTo(Expert);
                    return;
                } else if (cont.altChatText.startsWith("You've been given")){
                    if (!InterfaceWindows.getInventory().isOpen()){
                        InterfaceComponent inv = Interfaces.newQuery().actions("Inventory").results().first();
                        if (inv != null){
                            inv.click();
                        } else {
                            InterfaceWindows.getInventory().open();
                        }
                    }
                } else if (cont.altChatText.startsWith("Fishing")){
                    Fishing();
                    return;
                } else if (cont.altChatText.startsWith("You've gained some")){
                    InterfaceComponent skills = Interfaces.newQuery().actions("Skills").results().first();
                    if (skills != null){
                        skills.click();
                    } else {
                        InterfaceWindows.getSkills().open();
                    }
                    return;
                } else if (cont.altChatText.startsWith("Woodcutting")){
                    Woodcutting();
                } else if (cont.altChatText.startsWith("Firemaking")){
                    Firemaking();
                } else if (cont.altChatText.startsWith("Cooking")) {
                    Cooking();
                } else if (cont.altChatText.startsWith("Moving on")) {
                    MovingOn();
                }
            } else {
                Camera.turnTo(Expert);
                return;
            }
        }

        if (cont.altChatText.startsWith("Moving around")){
            if (Expert != null)
            {
                TravelTo(Expert.getArea());
            } else {
                TravelTo(Positions.SurvivalExpert);
            }

            return;
        }
    }

    public void MovingOn(){
        GameObject gate = GameObjects.newQuery().names("Gate").results().nearest();
        if (gate != null){
            if (gate.isVisible()){
                System.out.println("Clicking Gate");
                gate.click();
            } else {
                if (cont.position().distanceTo(gate.getPosition()) > 5){
                    System.out.println("Travelling to Gate");
                    TravelTo(Positions.GateSurvivalSide);
                } else {
                    System.out.println("Turning to Gate");
                    Camera.turnTo(gate);
                }
            }
        } else {
            System.out.println("Travelling to Gate");
            TravelTo(Positions.GateSurvivalSide);
        }
    }

    public void Cooking(){
        GameObject fire = GameObjects.newQuery().names("Fire").results().nearest();
        if (fire != null){
            if (fire.isVisible()){

                SpriteItem shr = Inventory.getItems("Raw shrimps").first();
                if (shr != null){
                    if (Inventory.getSelectedItem() != null){
                        fire.click();
                    } else {
                        shr.click();
                    }
                    /*
                    if (Inventory.getSelectedItem() != null){
                        fire.interact("Use", " " + Inventory.getSelectedItem() + " -> " + "Fire");
                    }*/
                    /*
                    Object[] fireOpts = fire.getDefinition().getActions().toArray();
                    for (int i = 0; i < fireOpts.length; i++){
                        System.out.println(fireOpts[i].toString());
                    }
                    //fire.interact("Use Raw shrimps");
                    fire.interact("Use Raw shrimps -> Fire");
                    */
                }
            } else {
                Camera.turnTo(fire);
            }

        } else {
            if (Inventory.contains("Logs")){
                Firemaking();
            } else {
                Woodcutting();
            }
        }
    }

    public void Firemaking(){
        if (Inventory.contains("Logs")){
            SpriteItem logs = Inventory.getItems("Logs").first();
            SpriteItem tinderbox = Inventory.getItems("Tinderbox").first();

            if (logs != null && tinderbox != null){
                GameObject fire = GameObjects.newQuery().names("Fire").results().nearest();

                if (fire != null){
                    if (fire.getPosition().getX() == cont.position().getX() && fire.getPosition().getY() == cont.position().getY()){
                        TravelTo(new Area.Circular(cont.position(), 3).getRandomCoordinate());
                        return;
                    }
                }

                logs.interact("Use");
                tinderbox.click();
            }
        } else {
            return;
        }
    }

    public void Woodcutting(){
        if (!InterfaceWindows.getInventory().isOpen()){
            InterfaceWindows.getInventory().open();
            return;
        }

        if (!Inventory.contains("Logs")){
            GameObject tree = GameObjects.newQuery().names("Tree").results().nearest();

            if (tree != null){
                if (tree.isVisible()){
                    tree.interact("Chop down");
                } else {
                    Camera.turnTo(tree);
                }
                return;
            }
            return;
        }
    }

    public void Fishing(){
        if (!Inventory.contains("Raw shrimps")){
            Npc fishing = Npcs.newQuery().names("Fishing spot").results().nearest();
            if (fishing != null){
                System.out.println("Fishing");
                if (!fishing.isVisible()){
                    Camera.turnTo(fishing);
                    return;
                }
                fishing.interact("Net");
                return;
            } else {

            }
        }
    }
}
