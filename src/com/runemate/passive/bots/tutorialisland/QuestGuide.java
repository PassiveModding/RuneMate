package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.framework.task.Task;

import static com.passive.api.runescape.navigation.Travel.TravelTo;
import static com.passive.api.runescape.npcs.Npcs.TalkTo;

public class QuestGuide extends Task {
    GetContextItems.Context cont;

    @Override
    public void execute() {
        if (cont.altChatText.startsWith("Fancy a run")){
            TravelToGuide();
            return;
        }

        Npc guide = Npcs.newQuery().names("Quest Guide").reachable().results().nearest();
        if (guide != null){
            if (guide.getPosition().isReachable() == false){
                TravelToGuide();
                return;
            }


            if (cont.altChatText.startsWith("Quests")){
                TalkTo(guide);
            }
            // Ensure the text contains the word started as the start does not change when you open the menu
            else if (cont.altChatText.startsWith("Quest journal") && cont.altChatText.contains("Click on")){
                InterfaceComponent questWindow = Interfaces.newQuery().actions("Quest List").results().first();

                if (questWindow != null){
                    questWindow.click();
                }
            } else if (cont.altChatText.startsWith("Quest journal")){
                TalkTo(guide);
            } else if (cont.altChatText.startsWith("Moving on")) {
                GameObject ladder = GameObjects.newQuery().names("Ladder").results().nearest();

                if (ladder != null){
                    if (ladder.isVisible()){
                        ladder.interact("Climb-down");
                    } else {
                        Camera.turnTo(ladder);
                    }
                }
            }
        } else {
            TravelToGuide();
        }
    }

    public void TravelToGuide(){
        GameObject door = GameObjects.newQuery().names("Door").results().nearest();

        if (door != null){
            if (cont.position().distanceTo(Positions.QuestGuideDoorOutside) > 5){
                TravelTo(Positions.QuestGuideDoorOutside);
            } else {
                if (door.isVisible()){
                    if (door.interact("Open")){
                        return;
                    } else {
                        door.click();
                    }
                } else {
                    Camera.turnTo(door);
                }


            }
        } else {
            TravelTo(Positions.QuestGuideDoorOutside);
        }
    }

    @Override
    public boolean validate() {
        cont = GetContextItems.GetContextItems();

        if (cont.altChatText.startsWith("Fancy a run")){
            System.out.println("Moving to Quest Guide");
            return true;
        }

        Npc guide = Npcs.newQuery().names("Quest Guide").results().nearest();
        if (guide != null){
            System.out.println("Quest Guide");
            return true;
        }

        return false;
    }
}
