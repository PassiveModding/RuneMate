package com.runemate.passive.bots.TutorialIslandOld;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;

import static com.runemate.passive.bots.TutorialIslandOld.Coordinates.QuestGuideDoorOutside;

public class QuestMaster {

    public static void InteractGuide() {

        Npc Guide = Npcs.newQuery().names("Quest Guide").results().nearest();

        if (Guide == null) {

            if (Players.getLocal().getPosition().getY() > 9000){
                TutorialIsland.StateOverride = null;
                return;
            }

            if (TutorialIsland.StateOverride == "QuestMasterRun") {
                BresenhamPath p = BresenhamPath.buildTo(QuestGuideDoorOutside);

                if (p != null) {
                    p.step();
                    TutorialIsland.StateOverride = "QuestMasterRun";
                }

                return;
            }
        } else if (!Guide.isVisible()) {
            Camera.turnTo(Guide);
        }

        if (Players.getLocal().getPosition().getY() >= 3126)
        {
            GameObject door = GameObjects.newQuery().names("Door").results().nearest();
            if (door != null){
                door.click();
            }
        }
        else if (Players.getLocal().getPosition().getY() <= 3118 || Players.getLocal().getPosition().getX() <= 3079 ||Players.getLocal().getPosition().getX() >= 3090){
            BresenhamPath p = BresenhamPath.buildTo(QuestGuideDoorOutside);

            if (p != null) {
                p.step();
                TutorialIsland.StateOverride = "QuestMasterRun";
            }

            return;
        }


        String dialog = ChatDialog.getText();

        if (dialog != null){
            if (ChatDialog.getContinue() != null) {
                ChatDialog.getContinue().select();
            }
            return;
        }

        NonGuideClick(Guide);
    }

    public static void NonGuideClick(Npc Guide) {

        String compText = GetCompUi.GetCompText();

        if (compText != null) {
            if (compText.startsWith("Quests")) {
                Guide.click();
            } else if (compText.startsWith("Quest journal") && compText.contains("Click on the flashing")) {
                com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces.newQuery().actions("Quest List").results().first().click();
            } else if (compText.startsWith("Quest journal") && compText.contains("This is your quest")){
                Guide.click();
            } else if (compText.startsWith("Moving on") || compText.startsWith("Mining")){

                TutorialIsland.StateOverride = null;
                GameObject ladder = GameObjects.newQuery().names("Ladder").results().nearest();

                if (ladder != null)
                {
                    ladder.click();
                }
                else {
                    BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3088, 3120));

                    if (p != null) {
                        p.step();
                    }
                }

            }
        }
    }
}
