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

public class Prayer extends Task {
    @Override
    public void execute() {
        GetContextItems.Context cont = GetContextItems.GetContextItems();

        Npc guide = Npcs.newQuery().names("Brother Brace").reachable().results().nearest();

        if (guide == null){
            //TODO: Add to positions
            TravelTo(new Coordinate(3125,3107));
            return;
        } else {
            if (cont.altChatText.startsWith("Prayer") && cont.altChatText.contains("Follow the path")){
                TalkTo(guide);
            } else if (cont.altChatText.startsWith("Prayer menu") && cont.altChatText.contains("Talk with")){
                TalkTo(guide);
            } else if (cont.altChatText.startsWith("Prayer menu")){
                InterfaceComponent prayWindow = Interfaces.newQuery().actions("Prayer").results().first();
                if (prayWindow != null){
                    prayWindow.click();
                }
            } else if (cont.altChatText.startsWith("Friends and Ignore") && cont.altChatText.contains("Speak with Brother")){
                guide.click();
            } else if (cont.altChatText.startsWith("Friends and Ignore")){
                InterfaceComponent window = Interfaces.newQuery().actions("Friends List").results().first();
                if (window != null){
                    window.click();
                }
            } else if (cont.altChatText.startsWith("Your final")){
                GameObject door = GameObjects.newQuery().names("Door").results().nearest();
                if (door != null){
                    if (door.isVisible()){
                        door.interact("Open");
                        return;
                    }

                    if (door.getPosition().distanceTo(Players.getLocal().getPosition()) < 2){
                        door.interact("Open");
                    } else {
                        TravelTo(new Coordinate(3122,3103));
                    }
                } else{
                    TravelTo(new Coordinate(3122,3103));
                }
            }
        }
    }

    @Override
    public boolean validate() {
        GetContextItems.Context cont = GetContextItems.GetContextItems();
        if (cont.altChatText.startsWith("Prayer")){
            System.out.println("Prayer");
            return true;
        }

        Npc guide = Npcs.newQuery().names("Brother Brace").reachable().results().nearest();
        if (guide != null){
            System.out.println("Prayer");
            return true;
        }

        return false;
    }
}
