package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;

public class AccountManagement {
    public static void Interact(){

        String compText = GetCompUi.GetCompText();
        String dialog = ChatDialog.getText();
        Npc guide = Npcs.newQuery().names("Account Guide").results().nearest();

        if (dialog != null){
            ChatDialog.getContinue().select();
            return;
        }

        if (compText != null){
            if (compText.startsWith("Account Management") && compText.contains("The guide here")){
                if (guide != null){
                    guide.click();
                }
            } else if (compText.startsWith("Account Management") && compText.contains("Click on the")){
                InterfaceComponent acc = Interfaces.newQuery().actions("Account Management").results().first();
                if (acc != null){
                    acc.interact("Account Management");
                }
            } else if (compText.startsWith("Account Management") && compText.contains("This is your")){
                if (guide != null){
                    guide.click();
                }
            } else if (compText.startsWith("Moving on")) {

                System.out.println("Leaving act mgmt area");

                if (Players.getLocal().getPosition().getX() != 3129){
                    BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3129, 3124));

                    if (p != null){
                        p.step();
                    }
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
    }

}
