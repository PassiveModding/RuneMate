package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.*;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

import static com.passive.api.runescape.navigation.Travel.TravelTo;
import static com.passive.api.runescape.npcs.Npcs.TalkTo;

public class CombatInstructor extends Task {
    GetContextItems.Context cont;

    @Override
    public void execute() {
        Npc guide = Npcs.newQuery().names("Combat Instructor").reachable().results().nearest();

        if (cont.altChatText.startsWith("Combat")) {
            if (guide != null){
                TalkTo(guide);
            }
        } else if (cont.altChatText.startsWith("Equipping items") && cont.altChatText.contains("You now have access")){

            InterfaceComponent equip = Interfaces.newQuery().actions("Worn Equipment").results().first();
            if (equip != null){
                equip.click();
            } else {
                InterfaceWindows.getEquipment().open();
            }
        } else if (cont.altChatText.startsWith("Worn inventory")) {

            if (InterfaceWindows.getEquipment().isOpen()){
                InterfaceComponent eqStats = Interfaces.newQuery().visible().actions("View equipment stats").results().first();

                if (eqStats != null){
                    eqStats.interact("View equipment stats");
                }
            } else {
                InterfaceWindows.getEquipment().open();
            }

        } else if (cont.altChatText.startsWith("Equipment stats") && cont.altChatText.contains("You're now holding")){
            InterfaceComponent closeBtn = Interfaces.newQuery().visible().actions("Close").results().first();

            if (closeBtn != null){
                closeBtn.interact("Close");
            } else{
                if (guide != null) {
                    if (guide.isVisible()) {
                        guide.click();
                    } else {
                        Camera.turnTo(guide);
                    }
                }
            }
        } else if (cont.altChatText.startsWith("Equipment stats")) {

            SpriteItem dagger = Inventory.getItems("Bronze dagger").first();

            if (dagger != null){
                dagger.click();
            }
        } else if (cont.altChatText.startsWith("Unequipping items")) {

            SpriteItem BronzeSword = Inventory.getItems("Bronze sword").first();
            if (BronzeSword != null){
                BronzeSword.click();
            }
            SpriteItem WoodShield = Inventory.getItems("Wooden shield").first();
            if (WoodShield != null){
                WoodShield.click();
            }
        } if (cont.altChatText.startsWith("Combat interface")){
            CombatInterface();
        } else if (cont.altChatText.startsWith("Attacking")) {
            AttackInCage();
        } else if (cont.altChatText.startsWith("Sit back and watch")) {
            Npc rat = Npcs.newQuery().names("Giant rat").results().nearest();

            if (Players.getLocal().getHealthGauge() == null)
            {
                if (rat != null)
                {
                    if (rat.getHealthGauge() == null){
                        if (rat.isVisible()){
                            rat.interact("Attack");
                        } else{
                            Camera.turnTo(rat);
                        }
                    }
                }
            }
        } else if (cont.altChatText.startsWith("Well done, you've made")) {
            System.out.println("Well done stuff");
            if (guide != null){
                if (!guide.getPosition().isReachable()){
                    GoToGate();
                } else {
                    if (guide.isVisible()){
                        guide.click();
                    } else{
                        TravelTo(new Coordinate(3105, 9507));
                    }
                }
            } else {
                GoToGate();
            }
        } else if (cont.altChatText.startsWith("Rat ranging")) {
            if (Equipment.containsAllOf("Shortbow", "Bronze arrow")) {
                Npc rat = Npcs.newQuery().names("Giant rat").results().nearest();

                if (rat != null)
                {
                    if (rat.isVisible()){
                        if (rat.getHealthGauge() == null){
                            rat.interact("Attack");
                            //rat.click();
                        }
                    } else {
                        Camera.turnTo(rat);
                    }

                }
            } else {
                EquipRange();
            }
        } else if (cont.altChatText.startsWith("Moving on")) {
            GameObject ladder = GameObjects.newQuery().names("Ladder").results().nearestTo(Positions.CombatInstructorExitLadder);

            if (ladder != null){
                if (ladder.isVisible()){
                    ladder.interact("Climb-up");
                } else {
                    Coordinate lPos = ladder.getPosition();
                    if (lPos != null){
                        if (cont.position().distanceTo(lPos) <= 5){
                            Camera.turnTo(ladder);
                        } else {
                            TryExitRatStage();
                        }
                    } else {
                        TryExitRatStage();
                    }
                }
            } else{
                TryExitRatStage();
            }
        }
    }

    public void TryExitRatStage(){
        if (!TravelTo(Positions.CombatInstructorExitLadder)) {
            if (!TravelTo(Positions.CombatInstructorRatGateOutside)){
                System.out.println("Having trouble exiting rat stage");
            }
        }
    }

    public void AttackInCage() {
        Npc rat = Npcs.newQuery().names("Giant rat").reachable().results().nearest();

        if (rat != null)
        {
            if (rat.getHealthGauge() == null){
                if (rat.isVisible()){
                    rat.interact("Attack");
                    //rat.click();
                } else{
                    Camera.turnTo(rat);
                }
            } else {
                return;
            }
        } else {
            GoToGate();
        }
    }

    public void EquipRange() {
        if (InterfaceWindows.getInventory().isOpen()){
            SpriteItem BronzeArrows = Inventory.getItems("Bronze arrow").first();

            if (BronzeArrows != null){
                BronzeArrows.click();
            }

            SpriteItem Shortbow = Inventory.getItems("Shortbow").first();

            if (Shortbow != null){
                Shortbow.click();
            }

        } else {
            InterfaceWindows.getInventory().open();
        }
    }

    public void CombatInterface(){
        if (cont.altChatText.contains("This is your combat interface")) {
            //Move towards the gate
            GameObject gate = GameObjects.newQuery().names("Gate").results().nearestTo(Positions.CombatInstructorRatGateOutside);

            if (gate == null) {
                TravelTo(Positions.CombatInstructorRatGateOutside);
            } else if (!gate.isVisible()) {
                Camera.turnTo(gate);
            } else {
                //gate.click();
                gate.interact("Open");
            }
        } else {
            InterfaceComponent eqStats = Interfaces.newQuery().visible().actions("Combat Options").results().first();

            if (eqStats != null){
                eqStats.interact("Combat Options");
            }
        }
    }

    public static void GoToGate(){
        GameObject gate = GameObjects.newQuery().names("Gate").results().nearestTo(Positions.CombatInstructorRatGateInside);


        if (gate == null || !gate.isVisible()) {
            TravelTo(Positions.CombatInstructorRatGateInside);
        } else {
            gate.click();
        }
    }

    @Override
    public boolean validate() {
        cont = GetContextItems.GetContextItems();

        if (cont.altChatText.startsWith("Combat") || cont.altChatText.startsWith("Attacking") || cont.altChatText.contains("combat instructor")) {
            System.out.println("Combat Instructor Txt");
            return true;
        }

        Npc guide = Npcs.newQuery().names("Combat Instructor").reachable().results().nearest();
        if (guide != null){
            System.out.println("Combat Instructor");
            return true;
        }

        Npc rat = Npcs.newQuery().names("Giant rat").reachable().results().nearest();
        if (rat != null){
            System.out.println("Combat Instructor Rat");
            return true;
        }


        return false;
    }
}
