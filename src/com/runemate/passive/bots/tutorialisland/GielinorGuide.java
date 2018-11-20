package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;

public class GielinorGuide {
    public static void InteractGuide() {
        Npc Guide = Npcs.newQuery().names("Gielinor Guide").results().nearest();
        String dialog = ChatDialog.getText();

        ChatDialog.Option option = ChatDialog.getOption("I am an experienced player.");

        if (option != null){
            option.select();
            return;
        }

        if (dialog != null){
            if (ChatDialog.getContinue() != null){
                ChatDialog.getContinue().select();
                return;
            }
        }

        NonGuideClick(Guide);
    }

    public static void NonGuideClick(Npc Guide) {

        String compText = GetCompUi.GetCompText();

        if (compText != null) {
            if (compText.startsWith("Getting started")) {
                Guide.click();
                System.out.println("Selected guide due to specific id");
            } else if (compText.startsWith("Options menu")){
                if (compText.contains("On the side panel")){
                    Guide.click();
                }
                else {
                    com.runemate.game.api.osrs.local.hud.interfaces.OptionsTab.getOpenedMenu().open();
                }
            } else if (compText.startsWith("Moving on")) {
                GameObject door = GameObjects.newQuery().names("Door").results().nearest();
                if (door != null){
                    if (door.isVisible()){
                        door.click();
                    } else{
                        Camera.turnTo(door);
                    }
                }
            } else if (compText.startsWith("Moving around")){
                if (Npcs.newQuery().names("Gielinor Guide").reachable().results().isEmpty()) {
                    Npc Expert = Npcs.newQuery().names("Survival Expert").reachable().results().first();
                    if (Expert != null)
                    {
                        BresenhamPath p = BresenhamPath.buildTo(Expert.getPosition());
                        if (p != null)
                        {
                            p.step();
                        }
                    }
                }
            }
        }
    }
}
