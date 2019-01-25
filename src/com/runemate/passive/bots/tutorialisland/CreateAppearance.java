package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.script.framework.task.Task;

import java.util.Random;

public class CreateAppearance extends Task {
    @Override
    public boolean validate() {
        GetContextItems.Context context = GetContextItems.GetContextItems();

        if (context.altChatText.startsWith("Setting your appearance")){
            System.out.println("Setting Appearance");
            return true;
        }

        return false;
    }

    public void InteractInterface(String action){
        InteractInterface(action, false);
    }

    public void InteractInterface(String action, Boolean multiple){
        if (action != null){
            InterfaceComponent intFace;
            //if (multiple){
            //    intFace = Interfaces.newQuery().actions(action).results().random();
            //} else{
                intFace = Interfaces.newQuery().actions(action).results().first();
            //}
            if (intFace != null){
                intFace.interact(action);
            }
        }
    }

    @Override
    public void execute() {
        int randomSet = new Random().nextInt(16);

        if (randomSet == 0){
            InteractInterface("Recolour hair", true);
        } else if (randomSet == 1){
            InteractInterface("Recolour torso", true);
        }else if (randomSet == 2){
            InteractInterface("Recolour legs", true);
        }else if (randomSet == 3){
            InteractInterface("Recolour skin", true);
        }else if (randomSet == 4){
            InteractInterface("Change head", true);
        }else if (randomSet == 5){
            InteractInterface("Change jaw", true);
        }else if (randomSet == 6){
            InteractInterface("Change torso", true);
        }else if (randomSet == 7){
            InteractInterface("Change arms", true);
        }else if (randomSet == 8){
            InteractInterface("Change hands", true);
        }else if (randomSet == 9){
            InteractInterface("Change legs", true);
        }else if (randomSet == 10){
            InteractInterface("Female", true);
        }else if (randomSet == 10){
            InteractInterface("Male", true);
        }else if (randomSet == 12){
            InteractInterface("Recolour feet", true);
        }else if (randomSet == 13){
            InteractInterface("Change feet", true);
        }else if (randomSet >= 14){
            InteractInterface("Accept", true);
        }
    }
}
