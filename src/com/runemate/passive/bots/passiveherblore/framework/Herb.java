package com.runemate.passive.bots.passiveherblore.framework;


import com.runemate.game.api.hybrid.local.Skill;

import java.util.ArrayList;
import java.util.List;

public enum Herb {
    GUAM("Guam leaf", "Grimy guam leaf", 3, 3, "Vial of water", "Guam potion (unf)", new Potion[]{
            new Potion("Attack potion", "Eye of newt", 3)
    }),
    MARRENTILL("Marrentill", "Grimy marrentill", 5, 5, "Vial of water", "Marrentill potion (unf)", new Potion[]{
            new Potion("Antipoison", "Unicorn horn dust", 5)
    }),
    TARROMIN("Tarromin", "Grimy tarromin", 11, 12, "Vial of water", "Tarromin potion (unf)", new Potion[]{
            new Potion("Strength potion", "Limpwurt root", 12),
            new Potion("Serum 207 ", "Ashes", 15)
    }),
    HARRALANDER("Harralander", "Grimy harralander", 20, 21, "Vial of water", "Harralander potion (unf)", new Potion[]{
            new Potion("Compost potion", "Volcanic ash", 21),
            new Potion("Restore potion", "Red spiders' eggs", 22),
            new Potion("Energy potion", "Chocolate dust", 26),
            new Potion("Combat potion", "Goat horn dust", 36)
    }),
    RANARR_WEED("Ranarr weed", "Grimy ranarr weed", 25, 30, "Vial of water", "Ranarr potion (unf)", new Potion[]{
            new Potion("Defense potion", "White berries", 30),
            new Potion("Prayer potion", "Snape grass", 38)
    }),
    TOADFLAX("Toadflax", "Grimy toadflax", 30, 34, "Vial of water", "Toadflax potion (unf)", new Potion[]{
            new Potion("Agility potion", "Toad's legs", 34),
            new Potion("Saradomin brew", "Crushed nest", 81)
    }),
    IRIT_LEAF("Irit leaf", "Grimy irit leaf", 40, 45, "Vial of water", "Irit potion (unf)", new Potion[]{
            new Potion("Super attack", "Eye of newt", 45),
            new Potion("Superantipoison", "Unicorn horn dust", 48)
    }),
    AVANTOE("Avantoe", "Grimy avantoe", 48, 50, "Vial of water", "Avantoe potion (unf)", new Potion[]{
            new Potion("Super energy potion", "Mort myre fungus", 52),
            new Potion("Fishing potion", "Snape grass", 50),
            new Potion("Hunter potion", "Kebbit teeth dust", 53)
    }),
    KWUARM("Kwuarm", "Grimy kwuarm", 54, 55, "Vial of water", "Kwuarm potion (unf)", new Potion[]{
            new Potion("Super strength", "Limpwurt root", 55)
            //new Potion("Weapon poison", "Dragon scale dust", 60)
    }),
    SNAPDRAGON("Snapdragon", "Grimy snapdragon", 59, 63, "Vial of water", "Snapdragon potion (unf)", new Potion[]{
            new Potion("Super restore", "Red spiders' eggs", 63)
    }),
    CADANTINE("Cadantine", "Grimy cadantine", 65, 66, "Vial of water", "Cadantine potion (unf)", new Potion[]{
            new Potion("Super defence", "White berries", 66)
    }),
    LANTADYME("Lantadyme", "Grimy lantadyme", 67, 69, "Vial of water", "Lantadyme potion (unf)", new Potion[]{
            new Potion("Anti-fire potion", "Dragon scale dust", 69),
            new Potion("Magic potion", "Potato cactus", 76)
    }),
    DWARF_WEED("Dwarf weed", "Grimy dwarf weed", 70, 72, "Vial of water", "Dwarf weed potion (unf)", new Potion[]{
            new Potion("Ranging potion", "Wine of Zamorak", 72)
    }),
    TORSTOL("Torstol", "Grimy torstol", 75, 78, "Vial of water", "Torstol potion (unf)", new Potion[]{
            new Potion("Zamorak brew", "Jangerberries", 78)
    });


    public final String grimy;
    public final String clean;
    public final int cleanLevel;
    public final int unfinishedLevel;
    public final String vialContents;
    public final String unfinishedPot;
    public final Potion[] potions;

    Herb(final String clean, final String grimy, final int cleanLevel, final int unfinishedLevel, final String vial, final String unfinishedPot, Potion[] potions) {
        this.clean = clean;
        this.grimy = grimy;
        this.cleanLevel = cleanLevel;
        this.unfinishedLevel = unfinishedLevel;
        this.vialContents = vial;
        this.unfinishedPot = unfinishedPot;
        this.potions = potions;
    }

    public final List<String> potionDisplayNames() {
        List<String> names = new ArrayList<>();
        for (Potion pot : potions) {
            names.add(pot.displayName());
        }
        return names;
    }

    public boolean canUse() {
        int baseLevel = Skill.HERBLORE.getBaseLevel();
        int level = level();
        if (baseLevel >= level) {
            return true;
        }
        System.out.println("Base Level: " + baseLevel + " Required: " + level);
        return false;
    }

    public static Herb getInfo(String DisplayTxt) {
        for (Herb herb : Herb.values()) {
            if (DisplayTxt.equalsIgnoreCase(herb.getUnfinishedDisplayName()) || DisplayTxt.equalsIgnoreCase(herb.getCleanDisplayName()) || DisplayTxt.equalsIgnoreCase(herb.grimy) || DisplayTxt.equalsIgnoreCase(herb.clean) || DisplayTxt.equalsIgnoreCase(herb.unfinishedPot)) {
                return herb;
            }
        }

        return null;
    }

    public final int level() {
        if (cleanLevel > unfinishedLevel) {
            return cleanLevel;
        }

        return unfinishedLevel;
    }

    public final String getUnfinishedDisplayName() {
        return clean + " - Lvl:" + Integer.toString(level());
    }
    public final String getCleanDisplayName() {
        return clean + " - Lvl:" + cleanLevel;
    }

    public static class Potion {
        public final String potion;
        public final String secondaryIngredient;
        public final int minLevel;

        Potion(String potionName, String secondaryIngredient, int minLevel) {
            this.potion = potionName;
            this.secondaryIngredient = secondaryIngredient;
            this.minLevel = minLevel;
        }

        public final String displayName() {
            return potion + " - Lvl:" + Integer.toString(minLevel) + " Req:" + secondaryIngredient;
        }

        public boolean canCreate() {
            int baseLevel = Skill.HERBLORE.getBaseLevel();
            if (baseLevel >= minLevel) {
                return true;
            }
            System.out.println("Base Level: " + baseLevel + " Required: " + minLevel);
            return false;
        }
    }
}
