package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

import static com.passive.api.runescape.navigation.Travel.TravelTo;
import static com.passive.api.runescape.npcs.Npcs.TalkTo;

public class AccountManagement extends Task {
    @Override
    public void execute() {
        Npc guide = Npcs.newQuery().names("Account Guide").reachable().results().nearest();
        GetContextItems.Context cont = GetContextItems.GetContextItems();

        if (guide == null){
            return;
        }

        if (cont.altChatText.startsWith("Account Management") && cont.altChatText.contains("The guide here")){
            TalkTo(guide);
        } else if (cont.altChatText.startsWith("Account Management") && cont.altChatText.contains("Click on the")){
            InterfaceComponent acc = Interfaces.newQuery().actions("Account Management").results().first();
            if (acc != null){
                acc.interact("Account Management");
            }
        } else if (cont.altChatText.startsWith("Account Management") && cont.altChatText.contains("This is your")){
            TalkTo(guide);
        } else if (cont.altChatText.startsWith("Moving on")) {
            if (Players.getLocal().getPosition().getX() != 3129){
                // TODO: Add to Positions
                TravelTo(new Coordinate(3129, 3124));
            }
            else{

                GameObject door = GameObjects.newQuery().names("Door").results().nearestTo(new Coordinate(3129, 3124));
                if (door != null){
                    if (door.distanceTo(Players.getLocal().getPosition()) < 2){
                        door.click();
                    }
                }
            }

        }
    }

    @Override
    public boolean validate() {
        Npc guide = Npcs.newQuery().names("Account Guide").reachable().results().nearest();
        if (guide != null){
            System.out.println("Account Management");
            return true;
        }

        return false;
    }
}
