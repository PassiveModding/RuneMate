package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.script.framework.task.Task;

import java.util.Random;
import java.util.UUID;

//This checks and sets the player name based on a random UUID
public class InitialSetup extends Task {



    @Override
    public boolean validate() {
        GetContextItems.Context context = GetContextItems.GetContextItems();
        if (context.altChatText.startsWith("Choosing a Display")){
            System.out.println("InitialSetup");
            return true;
        }

        return false;
    }

    @Override
    public void execute() {

        InterfaceComponent sName = Interfaces.newQuery().actions("Set name").visible().results().first();
        if (sName != null){
            System.out.println("Setting Name");
            sName.click();
            return;
        }

        InterfaceComponent compText = Interfaces.newQuery().texts("What name would you like to check (maximum of 12 characters)?").results().first();

        InterfaceComponent notAvail = Interfaces.newQuery().texts("not available").results().first();

        boolean rnd = new Random().nextBoolean();

        if (rnd){
            InterfaceComponent compName = Interfaces.newQuery().visible().actions("Look up name").results().first();
            if (compName != null){
                System.out.println("Looking up name");
                compName.interact("Look up name");
                return;
            }
        } else {
            if (compText != null){
                System.out.println("Generating Name");

                String newName = UUID.randomUUID().toString().replaceAll("-", "");

                //Ensure the random name is less than 13 characters long
                if (newName.length() > 12){
                    newName = newName.substring(0, 12);
                }

                Keyboard.type(newName, true);
                return;
            }
        }
    }
}
