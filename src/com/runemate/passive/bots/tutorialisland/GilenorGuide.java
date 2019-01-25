package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.framework.task.Task;

import static com.passive.api.runescape.npcs.Npcs.TalkTo;

public class GilenorGuide extends Task {
    @Override
    public boolean validate() {

        //This stage is only accepted if the gielinor guide is reachable
        //This can only ever be the Guide Stage as after this stage the player should ALWAYS be outside the building
        Npc guide = Npcs.newQuery().names("Gielinor Guide").results().nearest();

        if (guide != null){
            if (guide.getPosition().isReachable()){
                System.out.println("GielinorGuide");
                return true;
            }
        }

        return false;
    }

    @Override
    public void execute() {
        GetContextItems.Context cont = GetContextItems.GetContextItems();
        Npc guide = Npcs.newQuery().names("Gielinor Guide").results().nearest();

        ChatDialog.Option experiencedPlayer = ChatDialog.getOption("I am an experienced player.");
        if (experiencedPlayer != null){
            experiencedPlayer.select();
            return;
        }

        if (cont.altChatText.startsWith("Getting started")){
            if (guide != null){
                TalkTo(guide);
                return;
            }
        }

        if (cont.altChatText.startsWith("Options menu")) {
            if (cont.altChatText.contains("Talk to the Gielinor Guide")){
                TalkTo(guide);
                return;
            }

            InterfaceComponent opt = Interfaces.newQuery().actions("Options").results().first();
            if (opt != null){
                opt.click();
            } else {
                //
            }
            return;
        }

        if (cont.altChatText.startsWith("Moving on") || cont.altChatText.startsWith("Moving around")) {
            GameObject door = GameObjects.newQuery().names("Door").results().nearest();

            if (door != null){
                if (door.isVisible()){
                    door.interact("Open");
                } else {
                    Camera.turnTo(door);
                }
            }

            return;
        }
    }
}
