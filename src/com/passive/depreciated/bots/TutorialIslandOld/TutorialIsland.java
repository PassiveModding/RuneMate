package com.runemate.passive.bots.TutorialIslandOld;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.LoopingBot;

import java.util.Random;
import java.util.UUID;

public class TutorialIsland extends LoopingBot {

    public static String StateOverride = null;

    @Override
    public void onLoop() {
        if (!com.runemate.game.api.hybrid.RuneScape.isLoggedIn()){
            return;
        }

        Stage currentState = getCurrentStage();

        if (Npcs.newQuery().names("Lumbridge Guide").results().first() != null){
            com.runemate.game.api.hybrid.RuneScape.logout();
            this.stop("Finished Tutorial");
            return;
        }

        if (currentState == null) {
            return;
        }

        if (currentState == Stage.GielinorGuide) {
            System.out.println("Gielinor Guide");
            GielinorGuide.InteractGuide();

        } else if (currentState == Stage.SurvivalExpert) {
            System.out.println("Interacting Survival Expert");
            SurvivalExpert.InteractGuide();

        } else if (currentState == Stage.MasterChef) {
            System.out.println("Interacting Master Chef");
            MasterChef.InteractGuide();

        } else if (currentState == Stage.QuestMaster) {
            System.out.println("Interacting Quest Master");
            QuestMaster.InteractGuide();

        } else if (currentState == Stage.MiningMaster) {
            System.out.println("Interacting Mining Master");
            MiningTutor.InteractGuide();

        } else if (currentState == Stage.CombatInstructor) {
            System.out.println("Interacting Combat Instructor");
            CombatInstructor.InteractGuide();

        } else if (currentState == Stage.Banking){
            System.out.println("Interacting Bank Stage");
            Banking.BankMethod();
        } else if (currentState == Stage.AccoungMgmt){
            System.out.println("Interacting Account Management");
            AccountManagement.Interact();
        } else if (currentState == Stage.Prayer) {
            System.out.println("Interacting Prayer");
            Prayer.Interact();
        } else if (currentState == Stage.Mage){
            System.out.println("Interacting Mage");
            Mage.Interact();
        } else {
            System.out.println(currentState);
        }
    }


    public enum Stage
    {
        GielinorGuide,
        SurvivalExpert,
        MasterChef,
        QuestMaster,
        MiningMaster,
        CombatInstructor,
        Banking,
        AccoungMgmt,
        Prayer,
        Mage
    }

    private Stage getCurrentStage(){
        Player cp = Players.getLocal();
        Coordinate cpp = cp.getPosition();
        String ct = GetCompUi.GetCompText();
        String cdt = ChatDialog.getText();
        String ctt = ChatDialog.getTitle();
        ChatDialog.Continue cco = ChatDialog.getContinue();

        if (ctt == null){
            ctt = "";
        }

        if (cdt == null){
            cdt = "";
        }

        if (ct == null){
            ct = "";
        }
        System.out.println("Position: (" + cpp.getX() + "," + cpp.getY() + ")");

        if (cdt != null){
            if (cdt.startsWith("I can't reach") || cdt.startsWith("Nothing interesting") || cdt.startsWith("None of them")|| cdt.startsWith("Someone else is")){

                InterfaceComponent comp = Interfaces.newQuery().texts("Click to continue").results().first();

                if (comp != null){
                    comp.click();
                } else{
                    ChatDialog.Option cont = ChatDialog.getOption("Click to continue");

                    if (cont != null){
                        cont.select();
                    } else if (cco != null){
                        cco.select();
                    }
                }

                return null;
            }
        }

        if (ct.startsWith("Choosing a Display")){

            InterfaceComponent sName = Interfaces.newQuery().actions("Set name").visible().results().first();
            if (sName != null){
                System.out.println("Setting Name");
                //sName.interact("Set name");
                sName.click();
                return null;
            }

            InterfaceComponent compText = Interfaces.newQuery().texts("What name would you like to check (maximum of 12 characters)?").results().first();

            if (compText != null){
                System.out.println("Generating Name");

                String newName = UUID.randomUUID().toString().replaceAll("-", "");

                if (newName.length() > 12){
                    newName = newName.substring(0, 12);
                }

                Keyboard.type(newName, true);
                return null;
            }

            InterfaceComponent compName = Interfaces.newQuery().visible().actions("Look up name").results().first();
            if (compName != null){
                System.out.println("Looking up name");
                compName.interact("Look up name");
                return null;
            }
        }

        if (StateOverride != null)
        {
            System.out.println("Override: " + StateOverride);

            if (StateOverride == "QuestMasterRun"){
                return Stage.QuestMaster;
            } else if (StateOverride == "MiningInstructor"){
                return Stage.MiningMaster;
            }
        }

        Npc g = Npcs.newQuery().results().nearest();
        if (g != null) {
            String name = g.getName();

            if (ct.startsWith("Setting your")){
                int randomSet = new Random().nextInt(15);

                if (randomSet == 0){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Recolour hair").results().first();
                    if (appearance != null){
                        appearance.interact("Recolour hair");
                    }
                } else if (randomSet == 1){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Recolour torso").results().first();
                    if (appearance != null){
                        appearance.interact("Recolour torso");
                    }
                }else if (randomSet == 2){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Recolour legs").results().first();
                    if (appearance != null){
                        appearance.interact("Recolour legs");
                    }
                }else if (randomSet == 3){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Recolour feet").results().first();
                    if (appearance != null){
                        appearance.interact("Recolour skin");
                    }
                }else if (randomSet == 4){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Change head").results().first();
                    if (appearance != null){
                        appearance.interact("Change head");
                    }
                }else if (randomSet == 5){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Change jaw").results().first();
                    if (appearance != null){
                        appearance.interact("Change jaw");
                    }
                }else if (randomSet == 6){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Change torso").results().first();
                    if (appearance != null){
                        appearance.interact("Change torso");
                    }
                }else if (randomSet == 7){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Change arms").results().first();
                    if (appearance != null){
                        appearance.interact("Change arms");
                    }
                }else if (randomSet == 8){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Change hands").results().first();
                    if (appearance != null){
                        appearance.interact("Change hands");
                    }
                }else if (randomSet == 9){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Change legs").results().first();
                    if (appearance != null){
                        appearance.interact("Change legs");
                    }
                }else if (randomSet == 10){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Female").results().first();
                    if (appearance != null){
                        appearance.interact("Female");
                    }
                }else if (randomSet == 11){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Recolour skin").results().first();
                    if (appearance != null){
                        appearance.interact("CRecolour skin");
                    }
                }else if (randomSet == 12){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Change feet").results().first();
                    if (appearance != null){
                        appearance.interact("Change feet");
                    }
                }else if (randomSet >= 13){
                    InterfaceComponent appearance = Interfaces.newQuery().actions("Accept").results().first();
                    if (appearance != null){
                        appearance.interact("Accept");
                    }
                }
            } else if (name == "Lumbridge Guide" || ctt.startsWith("Lumbridge Guide") ||  cdt.startsWith("If you have a question or")){
                com.runemate.game.api.hybrid.RuneScape.logout();
                this.stop("Made it to lumbridge :)");
                return null;
            } else if (name.startsWith("Gielinor Guide")) {
                return Stage.GielinorGuide;
            } else if (name.startsWith("Survival Expert")) {
                return Stage.SurvivalExpert;
            } else if (name.startsWith("Fishing spot")){
                return Stage.SurvivalExpert;
            } else if (name.startsWith("Master Chef")) {
                return Stage.MasterChef;
            } else if (Inventory.contains("Shrimps") && !Inventory.contains("Bucket") && !Inventory.contains("Bucket of water")) {
                return Stage.SurvivalExpert;
            }else if (cpp.getY() < 4000 && Npcs.newQuery().names("Master Chef").reachable().results().first() == null && GameObjects.newQuery().names("Gate").visible().results().first() != null){
                return Stage.SurvivalExpert;
            }  else if (name.startsWith("Quest Guide")){
                return  Stage.QuestMaster;
            } else if (cpp.getY() > 9000){
                if (name.startsWith("Combat Instructor")){
                    return Stage.CombatInstructor;
                } else if (cpp.getX() >= 3094 && (cpp.getY() == 9503 || cpp.getY() == 9502) ) {
                    System.out.println("Combat Instructor Gate");
                    return Stage.CombatInstructor;
                } else if (cpp.getX() >= 3095) {
                    System.out.println("Combat Instructor Rat");
                    return Stage.CombatInstructor;
                } else if ((new Coordinate(3088,9520)).isReachable() && cpp.getX() <= 3094){
                    System.out.println("Mining Master Area");
                    return Stage.MiningMaster;
                } else
                {
                    GameObject ladder = GameObjects.newQuery().names("Ladder").actions("Climb-up").results().nearest();
                    if (ladder != null){
                        StateOverride = "MiningInstructor";
                        System.out.println("Mining Master Ladder");
                        return Stage.MiningMaster;
                    }
                    System.out.println("(" + cpp.getX() + "," + cpp.getY() + ")");
                }
            } else if (cpp.getY() <= 3102){
                  return Stage.Mage;
            } else if (ct.startsWith("Prayer") || name.startsWith("Brother Brace")){
                return Stage.Prayer;
            } else if (ct.startsWith("Account Management") || name.startsWith("Account Guide")) {
                return Stage.AccoungMgmt;
            } else if (ct.startsWith("Banking") || name.startsWith("Banker")){
                return Stage.Banking;
            }
        }
        return null;
    }

    @Override
    public void onStart (String... args){
        System.out.println("Running bot");
        setLoopDelay(100, 2000);
    }

    public TutorialIsland ()
    {
        onStart();
    }
}
