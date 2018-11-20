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

public class Prayer {

    public static void Interact(){
        Npc guide = Npcs.newQuery().names("Brother Brace").results().nearest();

        String compText = GetCompUi.GetCompText();
        String dialog = ChatDialog.getText();

        if (guide == null){
            BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3125,3107));

            if (p != null){
                p.step();
            }
            return;
        }

        if (dialog != null){
            if (ChatDialog.getContinue() != null){
                ChatDialog.getContinue().select();
            }
            return;
        }

        if (compText != null){
            if (compText.startsWith("Prayer") && compText.contains("Follow the path")){
                guide.click();
            } else if (compText.startsWith("Prayer menu") && compText.contains("Talk with")){
                guide.click();
            } else if (compText.startsWith("Prayer menu")){
                InterfaceComponent prayWindow = Interfaces.newQuery().actions("Prayer").results().first();
                if (prayWindow != null){
                    prayWindow.click();
                }
            } else if (compText.startsWith("Friends and Ignore") && compText.contains("Speak with Brother")){
                guide.click();
            } else if (compText.startsWith("Friends and Ignore")){
                InterfaceComponent window = Interfaces.newQuery().actions("Friends List").results().first();
                if (window != null){
                    window.click();
                }
            } else if (compText.startsWith("Your final")){
                GameObject door = GameObjects.newQuery().names("Door").results().nearest();
                if (door != null){
                    if (door.isVisible()){
                        door.click();
                        return;
                    }

                    if (door.getPosition().distanceTo(Players.getLocal().getPosition()) < 2){
                        door.click();
                    } else {
                        BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3122,3103));
                        if (p != null){
                            p.step();
                        }
                    }
                } else{
                    BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3122,3103));
                    if (p != null){
                        p.step();
                    }
                }
            }
        }

    }

}
