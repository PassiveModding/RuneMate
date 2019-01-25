package com.runemate.passive.bots.passiveherblore.framework;


import com.runemate.game.api.hybrid.local.Skill;

import java.util.ArrayList;
import java.util.List;

public enum Herb {
    GUAM("Guam leaf", "Grimy guam leaf", 3, 3, "Vial of water", "Guam potion (unf)", 199, 249, 91, new Potion[]{
            new Potion("Attack potion", 121, "Eye of newt", 221, 3)
    }),
    MARRENTILL("Marrentill", "Grimy marrentill", 5, 5, "Vial of water", "Marrentill potion (unf)", 201, 251, 93, new Potion[]{
            new Potion("Antipoison", 175, "Unicorn horn dust", 235, 5)
    }),
    TARROMIN("Tarromin", "Grimy tarromin", 11, 12, "Vial of water", "Tarromin potion (unf)", 203, 253, 95, new Potion[]{
            new Potion("Strength potion", 115, "Limpwurt root", 225, 12),
            new Potion("Serum 207 ", 3410, "Ashes", 592, 15)
    }),
    HARRALANDER("Harralander", "Grimy harralander", 20, 21, "Vial of water", "Harralander potion (unf)", 205, 255, 97, new Potion[]{
            new Potion("Compost potion", 6472, "Volcanic ash", 21622, 21),
            new Potion("Restore potion", 127, "Red spiders' eggs", 223, 22),
            new Potion("Energy potion", 3010, "Chocolate dust", 1975, 26),
            new Potion("Combat potion", 9741, "Goat horn dust", 9736, 36)
    }),
    RANARR_WEED("Ranarr weed", "Grimy ranarr weed", 25, 30, "Vial of water", "Ranarr potion (unf)", 207, 257, 99, new Potion[]{
            new Potion("Defense potion", 133, "White berries", 239, 30),
            new Potion("Prayer potion", 139, "Snape grass", 231, 38)
    }),
    TOADFLAX("Toadflax", "Grimy toadflax", 30, 34, "Vial of water", "Toadflax potion (unf)", 3049, 2998, 3002, new Potion[]{
            new Potion("Agility potion", 3034, "Toad's legs", 2152, 34),
            new Potion("Saradomin brew", 6687, "Crushed nest", 6693, 81)
    }),
    IRIT_LEAF("Irit leaf", "Grimy irit leaf", 40, 45, "Vial of water", "Irit potion (unf)", 209, 259, 101, new Potion[]{
            new Potion("Super attack", 145, "Eye of newt", 221, 45),
            new Potion("Superantipoison", 181, "Unicorn horn dust", 235, 48)
    }),
    AVANTOE("Avantoe", "Grimy avantoe", 48, 50, "Vial of water", "Avantoe potion (unf)", 211, 261, 103, new Potion[]{
            new Potion("Super energy potion", 3018, "Mort myre fungus", 2970, 52),
            new Potion("Fishing potion", 151, "Snape grass", 231, 50),
            new Potion("Hunter potion", 10000, "Kebbit teeth dust", 10111, 53)
    }),
    KWUARM("Kwuarm", "Grimy kwuarm", 54, 55, "Vial of water", "Kwuarm potion (unf)", 213, 263, 105, new Potion[]{
            new Potion("Super strength", 157, "Limpwurt root", 225, 55)
            //new Potion("Weapon poison", "Dragon scale dust", 60)
    }),
    SNAPDRAGON("Snapdragon", "Grimy snapdragon", 59, 63, "Vial of water", "Snapdragon potion (unf)", 3051, 3000, 3004, new Potion[]{
            new Potion("Super restore", 3026, "Red spiders' eggs", 223, 63)
    }),
    CADANTINE("Cadantine", "Grimy cadantine", 65, 66, "Vial of water", "Cadantine potion (unf)", 215, 265, 107, new Potion[]{
            new Potion("Super defence", 163, "White berries", 239, 66)
    }),
    LANTADYME("Lantadyme", "Grimy lantadyme", 67, 69, "Vial of water", "Lantadyme potion (unf)", 2485, 2481, 2483, new Potion[]{
            new Potion("Anti-fire potion", 2454, "Dragon scale dust", 241, 69),
            new Potion("Magic potion", 3042, "Potato cactus", 3138, 76)
    }),
    DWARF_WEED("Dwarf weed", "Grimy dwarf weed", 70, 72, "Vial of water", "Dwarf weed potion (unf)", 217, 267, 109, new Potion[]{
            new Potion("Ranging potion", 169, "Wine of Zamorak", 245, 72)
    }),
    TORSTOL("Torstol", "Grimy torstol", 75, 78, "Vial of water", "Torstol potion (unf)", 219, 269, 111, new Potion[]{
            new Potion("Zamorak brew", 189, "Jangerberries", 247, 78)
    });


    public final int grimyId;
    public final int cleanId;
    public final int unfinishedId;
    public final String grimy;
    public final String clean;
    public final int cleanLevel;
    public final int unfinishedLevel;
    public final String vialContents;
    public final String unfinishedPot;
    public final Potion[] potions;

    Herb(final String clean, final String grimy, final int cleanLevel, final int unfinishedLevel, final String vial, final String unfinishedPot, final int gID, final int cID, final int uID, Potion[] potions) {
        this.clean = clean;
        this.grimy = grimy;
        this.cleanLevel = cleanLevel;
        this.unfinishedLevel = unfinishedLevel;
        this.vialContents = vial;
        this.unfinishedPot = unfinishedPot;
        this.grimyId = gID;
        this.cleanId = cID;
        this.unfinishedId = uID;
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
        public final int potionId;
        public final int secondaryId;

        Potion(String potionName, int potionId, String secondaryIngredient, int secondaryId, int minLevel) {
            this.potion = potionName;
            this.secondaryIngredient = secondaryIngredient;
            this.minLevel = minLevel;
            this.potionId = potionId;
            this.secondaryId = secondaryId;
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
