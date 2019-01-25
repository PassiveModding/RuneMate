package com.runemate.passive.bots.TutorialIslandOld;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;

public class Mage {
    public static void Interact(){
        String compText = GetCompUi.GetCompText();
        String dialog = ChatDialog.getText();
        Npc guide = Npcs.newQuery().names("Magic Instructor").results().nearest();
        if (guide == null){
            MoveToGuide();
            return;
        }

        ChatDialog.Option yesOpt = ChatDialog.getOption("Yes.");
        if (yesOpt != null)
        {
            yesOpt.select();
            return;
        }

        ChatDialog.Option noIron = ChatDialog.getOption("No, I'm not planning to do that.");
        if (noIron != null)
        {
            noIron.select();
            return;
        }

        if (dialog != null){
            if (ChatDialog.getContinue() != null)
            {
                ChatDialog.getContinue().select();
            }
            return;
        }

        if (compText != null){
            if (compText.startsWith("Your final")){
                if (guide != null){

                    if (!guide.isVisible()){
                        Camera.turnTo(guide);
                    } else{
                        guide.click();
                    }

                    if (guide.getPosition().distanceTo(Players.getLocal().getPosition()) < 3){
                        guide.click();
                    } else{
                        MoveToGuide();
                    }
                }
                else{
                    MoveToGuide();
                }
            } else if (compText.startsWith("Open up your final")){
                InterfaceComponent mage = Interfaces.newQuery().actions("Magic").results().first();

                if (mage != null){
                    mage.interact("Magic");
                }
            } else if (compText.startsWith("Magic") && compText.contains("This is your magic")){
                if (!guide.isVisible()){
                    Camera.turnTo(guide);
                } else{
                    guide.click();
                }
            } else if (compText.startsWith("Magic casting")){
                Magic.WIND_STRIKE.activate();
                Npc chicken = Npcs.newQuery().names("Chicken").results().nearest();

                Coordinate pos = Players.getLocal().getPosition();

                if ((pos.getX() > 3141 && pos.getY() < 3140) || (pos.getY() < 3089 && pos.getY() > 3091)) {
                    MoveToCast();
                    return;
                }

                if (chicken != null){
                    if (!chicken.isVisible()){
                        Camera.turnTo(chicken);
                    } else{
                        chicken.click();
                    }
                }
            } else if (compText.startsWith("To the mainland")){
                if (!guide.isVisible()){
                    Camera.turnTo(guide);
                } else{
                    guide.click();
                }
            }
        }
    }

    public static void MoveToCast(){
        BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3140, 3089));

        if (p != null){
            p.step();
        }
    }

    public static void MoveToGuide(){
        BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3139, 3087));

        if (p != null){
            p.step();
        }
    }

}
