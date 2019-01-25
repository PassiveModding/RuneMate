package com.passive.api.runescape.items;

import com.runemate.game.api.hybrid.entities.details.Interactable;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.util.calculations.Random;

public class Items {

    public static void randomCombine(SpriteItem sprite1, SpriteItem sprite2) {
        boolean rnd = Random.nextBoolean();
        if (rnd) {
            combine(sprite1, sprite1.getDefinition().getName(), sprite2, sprite2.getDefinition().getName());
        } else {
            combine(sprite2, sprite2.getDefinition().getName(), sprite1, sprite1.getDefinition().getName());
        }
    }

    public static <T1 extends Interactable, T2 extends Interactable> boolean combine(final T1 i1, final String n1, final T2 i2, final String n2) {
        if (n1 != null && n2 != null) {
            final SpriteItem selected = Inventory.getSelectedItem();
            if (selected != null) {
                if (selected.getDefinition().getName().equalsIgnoreCase(n1)) {
                    return i2.interact("Use", n1 + " -> " + n2);
                } else if (selected.getDefinition().getName().equalsIgnoreCase(n2)) {
                    return i1.interact("Use", n2 + " -> " + n1);
                } else {
                    return selected.click() && i1.interact("Use", n1) && i2.interact("Use", n1 + " -> " + n2);
                }
            } else {
                return i1.interact("Use", n1) && i2.interact("Use", n1 + " -> " + n2);
            }
        }
        return false;
    }
}
