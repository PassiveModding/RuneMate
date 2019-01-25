package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

import static com.passive.api.runescape.navigation.Travel.TravelTo;

public class Banking extends Task {
    GetContextItems.Context cont;

    @Override
    public void execute() {

        if (cont.altChatText.startsWith("Banking") && cont.altChatText.contains("This is your bank")){
            if (Bank.isOpen()){
                Bank.close();
                return;
            }

            PollBooth();
        } else if (cont.altChatText.startsWith("Banking")){
            GameObject bankBooth = GameObjects.newQuery().names("Bank booth").results().nearest();
            if (bankBooth != null){
                if (bankBooth.isVisible()){
                    bankBooth.click();
                    //bankBooth.interact("Use");
                } else {
                    Camera.turnTo(bankBooth);
                }
            } else {
                TravelTo(Positions.BankRoom);
            }
        } else if (cont.altChatText.startsWith("Moving on")) {
            InterfaceComponent pollint = Interfaces.newQuery().actions("Close").visible().results().first();
            if (pollint != null){
                pollint.interact("Close");
            } else {
                GameObject door = GameObjects.newQuery().names("Door").results().nearestTo(Positions.ExitBankRoom);
                if (door != null){
                    if (door.isVisible()){
                        //door.click();
                        door.interact("Open");
                    } else {
                        GoToDoor();
                    }
                } else {
                    GoToDoor();
                }
            }
        }
    }

    public static void GoToDoor(){
        TravelTo(Positions.ExitBankRoom);
    }

    public void PollBooth() {
        GameObject poll_booth = GameObjects.newQuery().names("Poll booth").results().nearest();
        if (poll_booth != null){

            if (poll_booth.isVisible()){
                //poll_booth.click();
                poll_booth.interact("Use");
                return;
            } else {
                Camera.turnTo(poll_booth);
            }

            if (poll_booth.distanceTo(Players.getLocal().getPosition()) < 5){
                //poll_booth.click();
                poll_booth.interact("Use");
            } else{
                GoToPollBooth();
            }
        } else {
            GoToPollBooth();
        }
    }

    public void GoToPollBooth() {
        TravelTo(Positions.PollBooth);
    }

    @Override
    public boolean validate() {
        cont = GetContextItems.GetContextItems();

        if (cont.altChatText.startsWith("Banking")){
            System.out.println("Banking");
            return true;
        }

        /*
        GameObject bankBooth = GameObjects.newQuery().names("Bank booth").results().first();
        if (bankBooth != null){
            System.out.println("Banking");
            return true;
        }
        */

        return Positions.BankBoothInfront.isReachable();

    }
}
