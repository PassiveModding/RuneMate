package com.runemate.passive.bots.TutorialIslandOld;

import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;

public class GetCompUi {

    public static String GetCompText() {

        Object[] interfaces = Interfaces.newQuery().results().toArray();
        for (int i = 0; i < interfaces.length; i++) {
            InterfaceComponent comp = (InterfaceComponent) interfaces[i];

            if (comp.getText() != null || comp.getName() != null) {

                if (comp.getText() != null && comp.getText() != "") {
                    if (comp.getId() == 17235969) {
                        System.out.println("Text: " + comp.getText() + " ID: " + comp.getId());
                        return comp.getText();
                    }
                }
            }
        }
        return null;
    }
}
