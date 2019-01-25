package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

import static com.passive.api.runescape.navigation.Travel.TravelTo;
import static com.passive.api.runescape.npcs.Npcs.TalkTo;

public class Magic extends Task {
    @Override
    public void execute() {
        ChatDialog.Option yesOpt = ChatDialog.getOption("Yes.");
        if (yesOpt != null) {
            yesOpt.select();
            return;
        }

        ChatDialog.Option noIron = ChatDialog.getOption("No, I'm not planning to do that.");
        if (noIron != null) {
            noIron.select();
            return;
        }

        GetContextItems.Context cont = GetContextItems.GetContextItems();
        Npc guide = Npcs.newQuery().names("Magic Instructor").reachable().results().nearest();
        if (guide != null){
            if (cont.altChatText.startsWith("Your final instructor")){
                if (guide.getPosition().distanceTo(cont.position()) > 5){
                    TravelTo(guide.getPosition());
                    return;
                }

                if (guide.isVisible()){
                    TalkTo(guide);
                } else {
                    Camera.turnTo(guide);
                }
            } else if (cont.altChatText.startsWith("Open up your final")){
                InterfaceComponent mage = Interfaces.newQuery().actions("Magic").results().first();

                if (mage != null){
                    mage.interact("Magic");
                }
            } else if (cont.altChatText.startsWith("Magic") && cont.altChatText.contains("This is your magic")){
                if (!guide.isVisible()){
                    Camera.turnTo(guide);
                } else{
                    guide.click();
                }
            } else if (cont.altChatText.startsWith("Magic casting")){
                if (!com.runemate.game.api.osrs.local.hud.interfaces.Magic.WIND_STRIKE.isSelected()){
                    com.runemate.game.api.osrs.local.hud.interfaces.Magic.WIND_STRIKE.activate();
                    return;
                }

                Npc chicken = Npcs.newQuery().names("Chicken").results().nearest();

                Coordinate pos = Players.getLocal().getPosition();
                if (pos != null){
                    if ((pos.getX() > 3141 && pos.getY() < 3140) || (pos.getY() < 3089 && pos.getY() > 3091)) {
                        MoveToCast();
                        return;
                    }
                }

                if (chicken != null){
                    if (!chicken.isVisible()){
                        Camera.turnTo(chicken);
                    } else{
                        chicken.click();
                    }
                }
            } else if (cont.altChatText.startsWith("To the mainland")){
                if (!guide.isVisible()){
                    Camera.turnTo(guide);
                } else{
                    guide.click();
                }
            }
        } else {
            TravelTo(new Coordinate(3140,3086));
        }
    }


    public static void MoveToCast(){
        TravelTo(new Coordinate(3140, 3089));
    }

    @Override
    public boolean validate() {
        GetContextItems.Context cont = GetContextItems.GetContextItems();

        if (cont.altChatText.startsWith("Your final instructor")){
            System.out.println("Magic");
            return true;
        }

        Npc guide = Npcs.newQuery().names("Magic Instructor").reachable().results().nearest();
        if (guide != null){
            System.out.println("Magic");
            return true;
        }

        return false;
    }
}
