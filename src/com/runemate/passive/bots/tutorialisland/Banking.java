package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;

public class Banking {
    public static void BankMethod(){
        String compText = GetCompUi.GetCompText();

        String dialog = ChatDialog.getText();
        String dialogTitle = ChatDialog.getTitle();

        System.out.println("Dialog: " + dialog);
        System.out.println("Dialog Title: " + dialogTitle);

        if (dialog != null){
            ChatDialog.getContinue().select();
            return;
        }


        if (compText != null){
            if  (compText.startsWith("Banking") && compText.contains("This is your bank")){
                if (Bank.isOpen()){
                    Bank.close();
                } else {
                    GameObject poll_booth = GameObjects.newQuery().names("Poll booth").results().nearest();

                    if (poll_booth != null){

                        if (poll_booth.isVisible()){
                            poll_booth.click();
                            return;
                        } else {
                            Camera.turnTo(poll_booth);
                        }

                        if (poll_booth.distanceTo(Players.getLocal().getPosition()) < 5){
                            System.out.println("Poll booth located");
                            poll_booth.click();
                        } else{
                            System.out.println("Moving to poll booth");
                            GoToPollBooth();
                        }
                    } else {
                        System.out.println("Unable to locate poll booth");
                        GoToPollBooth();
                    }
                }
            }
            else if (compText.startsWith("Banking")){
                GameObject bankBooth = GameObjects.newQuery().names("Bank booth").results().nearest();

                if (bankBooth != null){
                    if (bankBooth.distanceTo(Players.getLocal().getPosition()) < 2){
                        System.out.println("Bank booth located");
                        bankBooth.click();
                    } else{
                        GoToBankBooth();
                    }
                } else {
                    System.out.println("Unable to locate bank booth");
                    GoToBankBooth();
                }
            } else if (compText.startsWith("Moving on")) {
                InterfaceComponent pollint = Interfaces.newQuery().actions("Close").visible().results().first();
                if (pollint != null){
                    System.out.println("Closing poll booth");
                    pollint.interact("Close");
                } else {
                    System.out.println("Finding Exit door");
                    GameObject door = GameObjects.newQuery().names("Door").results().nearestTo(new Coordinate(3124, 3124));
                    if (door != null){
                        if (door.isVisible()){
                            System.out.println("Door found and visible");
                            door.click();
                        } else {
                            GoToDoor();
                        }
                    } else {
                        GoToDoor();
                    }
                }
            }
        }
    }

    public static void GoToDoor(){
        System.out.println("Moving to door location");
        BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3124, 3124));

        if (p != null){
            p.step();
        }
    }


    public static void GoToPollBooth(){
        BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3120, 3121));

        if (p != null){
            p.step();
        }
    }

    public static void GoToBankBooth(){
        BresenhamPath p = BresenhamPath.buildTo(new Coordinate(3122, 3123));

        if (p != null){
            p.step();
        }
    }


}
