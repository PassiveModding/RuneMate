package com.passive.api.runescape.navigation.locations.banks;

import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.local.Quest;
import com.runemate.game.api.hybrid.local.Quests;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.Worlds;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.osrs.local.AchievementDiary;
import com.runemate.game.api.osrs.local.KourendHouseFavour;

import java.util.concurrent.Callable;

public enum BankLocation implements Locatable {
    //Misthalin
    //Varrock
    GRAND_EXCHANGE(new Area.Polygonal(
            new Coordinate(3161, 3486, 0),
            new Coordinate(3168, 3486, 0),
            new Coordinate(3168, 3493, 0),
            new Coordinate(3161, 3493, 0)
    ), () -> true, false),
    VARROCK_EAST(new Area.Rectangular(new Coordinate(3250, 3422, 0), new Coordinate(3255, 3419, 0)),
            () -> true, false),
    VARROCK_WEST(new Area.Rectangular(new Coordinate(3185, 3444, 0), new Coordinate(3181, 3436, 0)),
            () -> true, false),
    COOKING_GUILD(new Area.Rectangular(new Coordinate(3147, 3449, 0), new Coordinate(3147, 3449, 0)),
            () -> {
                if (Worlds.getOverview(Worlds.getCurrent()).isMembersOnly()){
                    if (Equipment.containsAnyOf("Chef's hat", "Golden chef's hat", "Cooking cape", "Cooking hood", "Max cape", "Varrock armour 3")){
                        if (Skill.COOKING.getBaseLevel() >= 32){
                            return AchievementDiary.VARROCK.isHardComplete() || Skill.COOKING.getBaseLevel() == 99;
                        }
                    }
                }
                return false;
            }, true),
    //Lumbridge
    //TODO: LUMBRIDGE_CELLAR
    LUMBRIDGE_CASTLE(new Area.Rectangular(new Coordinate(3207, 3220, 2), new Coordinate(3210, 3217, 2)),
            () -> true, false),
    LUMBRIDGE_PVP(new Area.Rectangular(new Coordinate(3221, 3218, 0), new Coordinate(3223, 3215, 0)),
            () -> {
                return Worlds.getOverview(Worlds.getCurrent()).isPVP();

            }, false),
    //Edgeville
    EDVEVILLE(new Area.Polygonal(
            new Coordinate(3097, 3496, 0),
            new Coordinate(3097, 3498, 0),
            new Coordinate(3092, 3498, 0),
            new Coordinate(3092, 3496, 0),
            new Coordinate(3092, 3489, 0),
            new Coordinate(3094, 3489, 0),
            new Coordinate(3094, 3496, 0)
    ), () -> true, false),
    //Draynor
    DRAYNOR(new Area.Rectangular(new Coordinate(3092, 3245, 0), new Coordinate(3095, 3240, 0)),
            () -> true, false),
    //Asgarnia
    // Falador
    FALADOR_WEST(new Area.Rectangular(new Coordinate(2945, 3368, 0), new Coordinate(2947, 3367, 0)),
            () -> true, false),
    FALADOR_EAST(new Area.Rectangular(new Coordinate(3009, 3357, 0), new Coordinate(3016, 3355, 0)),
            () -> true, false),
    //TODO: Deposit Box Port Sarim
    ROGUES_DEN(new Area.Rectangular(new Coordinate(3044, 4975, 1), new Coordinate(3047, 4969, 1)),
            () -> true, true),
    WARRIORS_GUILD(new Area.Rectangular(new Coordinate(2843, 3544, 0), new Coordinate(2844, 3540, 0)),
            () -> {
        int atk = Skill.ATTACK.getBaseLevel();
        int str = Skill.STRENGTH.getBaseLevel();
                if (atk + str >= 130){
                    return true;
                }

                return atk >= 99 || str >= 99;

            }, true),
    CRAFTING_GUILD(new Area.Rectangular(new Coordinate(2935, 3280, 0), new Coordinate(2933, 3282, 0)),
            () -> {
                if (Skill.CRAFTING.getBaseLevel() >= 40){
                    if (Equipment.containsAnyOf("Brown apron", "Golden apron", "Max cape", "Crafting cape")){
                        return AchievementDiary.FALADOR.isHardComplete();
                    }
                }
                return false;
            }, true),
    //Dwarven Realm
    KELDAGRIM_PALACE(new Area.Rectangular(new Coordinate(2839, 10207, 0), new Coordinate(2835, 10208, 0)),
            () -> {
                Quest.Status status = Quests.get("The Giant Dwarf").getStatus();
                return status == Quest.Status.IN_PROGRESS || status == Quest.Status.COMPLETE;

            }, true),
    //Fremennik Province
    BARBARIAN_OUTPOST(new Area.Rectangular(new Coordinate(2537, 3574, 0), new Coordinate(2535, 3576, 0)),
            () -> true, true),
    NEITIZNOT(new Area.Rectangular(new Coordinate(2336, 3807, 0), new Coordinate(2338, 3807, 0)),
            () -> {
                if (Quests.get("The Fremennik Trials").getStatus() == Quest.Status.COMPLETE){
                    Quest.Status islesStatus = Quests.get("The Fremennik Isles").getStatus();
                    return islesStatus == Quest.Status.IN_PROGRESS || islesStatus == Quest.Status.COMPLETE;
                }

                return false;
            }, true),
    JATIZSO(new Area.Rectangular(new Coordinate(2417, 3801, 0), new Coordinate(2416, 3800, 0)),
            () -> {
                return Quests.get("The Fremennik Trials").getStatus() == Quest.Status.COMPLETE;

            }, true),
    MISCELLANIA_ETCETERIA(new Area.Rectangular(new Coordinate(2618, 3896, 0), new Coordinate(2620, 3894, 0)),
            () -> true, true),
    //TODO: Rellekka Peer the Seer
    LUNAR_ISLE(new Area.Rectangular(new Coordinate(2101, 3917, 0), new Coordinate(2098, 3919, 0)),
            () -> {
                if (Quests.get("Lunar Diplomacy").getStatus() == Quest.Status.COMPLETE){
                    if (Equipment.contains("Seal of passage")){
                        return true;
                    } else return Quests.get("Dream Mentor").getStatus() == Quest.Status.COMPLETE;
                }

                return false;
            }, true),
    //Khanidian
    CATHERBY(new Area.Rectangular(new Coordinate(2806, 3441, 0), new Coordinate(2812, 3439, 0)),
            () -> true, true),
    CAMELOT_PVP(new Area.Rectangular(new Coordinate(2759, 3480, 0), new Coordinate(2756, 3478, 0)),
            () -> {
                return Worlds.getOverview(Worlds.getCurrent()).isPVP();

            }, true),
    SEERS_VILLAGE(new Area.Rectangular(new Coordinate(2723, 3493, 0), new Coordinate(2727, 3490, 0)),
            () -> true, true),
    //Ardougne
    ARDOUGNE_NORTH(new Area.Rectangular(new Coordinate(2614, 3333, 0), new Coordinate(2618, 3331, 0)),
            () -> true, true),
    ARDOUGNE_SOUTH(new Area.Rectangular(new Coordinate(2655, 3286, 0), new Coordinate(2653, 3281, 0)),
            () -> true, true),

    YANILLE(new Area.Rectangular(new Coordinate(2614, 3097, 0), new Coordinate(2612, 3088, 0)),
            () -> true, true),
    LEGENDS_GUILD(new Area.Rectangular(new Coordinate(2733, 3376, 2), new Coordinate(2731, 3378, 2)),
            () -> {
                return Quests.get("Legends' Quest").getStatus() == Quest.Status.COMPLETE;

            }, true),
    CASTLE_WARS(new Area.Rectangular(new Coordinate(2444, 3082, 0), new Coordinate(2443, 3083, 0)),
            () -> true, true),
    TREE_GNOME_STRONGHOLD(new Area.Rectangular(new Coordinate(2447, 3425, 0), new Coordinate(2446, 3424, 0)),
            () -> true, true),
    GRAND_TREE_1(new Area.Rectangular(new Coordinate(2449, 3482, 1), new Coordinate(2449, 3478, 1)),
            () -> true, true),
    GRAND_TREE_2(new Area.Rectangular(new Coordinate(2442, 3488, 1), new Coordinate(2438, 3488, 1)),
            () -> true, true),
    //TODO: PISCATORIS_FISHING_COLONY
    PORT_KHAZARD(new Area.Rectangular(new Coordinate(2661, 3161, 0), new Coordinate(2665, 3161, 0)),
            () -> true, true),
    FISHING_GUILD(new Area.Rectangular(new Coordinate(2587, 3421, 0), new Coordinate(2584, 3419, 0)),
            () -> {
                return Skill.FISHING.getCurrentLevel() >= 68;
            }, true),
    MOR_UL_REK_MINE(new Area.Rectangular(new Coordinate(2443, 5180, 0), new Coordinate(2448, 5179, 0)),
            () -> true, true),
    MOR_UL_REK_INFERNO(new Area.Rectangular(new Coordinate(2540, 5142, 0), new Coordinate(2542, 5141, 0)),
            () -> {
                return Equipment.contains("Fire cape") || Inventory.contains("Fire cape");

            }, true),
    SHILO_VILLAGE(new Area.Rectangular(new Coordinate(2854, 2955, 0), new Coordinate(2850, 2953, 0)),
            () -> {
                return Quests.get("Shilo Village").getStatus() == Quest.Status.COMPLETE;

            }, true),
    //TODO: Tai Bwo Wannai
    //Kharidian Desert
    AL_KHARID( new Area.Rectangular(new Coordinate(3269, 3171, 0), new Coordinate(3272, 3163, 0)),
            () -> true, true),
    DUEL_ARENA(new Area.Rectangular(new Coordinate(3384, 3267, 0), new Coordinate(3382, 3269, 0)),
            () -> true, false),
    NARDAH(new Area.Rectangular(new Coordinate(3429, 2893, 0), new Coordinate(3424, 2890, 0)),
            () -> true, true),
    //TODO: SOPHANEM
    SHANTAY_PASS(new Area.Rectangular(new Coordinate(3308, 3121, 0), new Coordinate(3306, 3119, 0)),
            () -> true, true),
    //MORYTANIA
    CANIFIS(new Area.Rectangular(new Coordinate(3512, 3482, 0), new Coordinate(3513, 3478, 0)),
            () -> {
                return Quests.get("Priest in Peril").getStatus() == Quest.Status.COMPLETE;

            }, true),
    PORT_PHASMATYS(new Area.Rectangular(new Coordinate(3690, 3470, 0), new Coordinate(3687, 3462, 0)),
            () -> {
            //TODO: Ecto tokens check
                return Quests.get("Priest in Peril").getStatus() == Quest.Status.COMPLETE;

            }, true),
    BURGH_DE_ROTT(new Area.Rectangular(new Coordinate(3495, 3213, 0), new Coordinate(3499, 3210, 0)),
            () -> {
                if (Quests.get("Priest in Peril").getStatus() == Quest.Status.COMPLETE){
                    Quest.Status inAidStatus = Quests.get("In Aid of Myreque").getStatus();
                    return inAidStatus == Quest.Status.COMPLETE || inAidStatus == Quest.Status.IN_PROGRESS;
                }

                return false;
            }, true),
    //TODO: Lair of Tarn Razorlor
    VER_SINHAZA(new Area.Rectangular(new Coordinate(3652, 3208, 0), new Coordinate(3649, 3210, 0)),
            () -> {
                if (Quests.get("Priest in Peril").getStatus() == Quest.Status.COMPLETE){
                    return Quests.get("A Taste of Hope").getStatus() == Quest.Status.COMPLETE;
                }

                return false;
            }, true),
    //TIRANNWN
    LLETYA(new Area.Rectangular(new Coordinate(2351, 3166, 0), new Coordinate(2353, 3161, 0)),
            () -> {
                Quest.Status mourningsEndP1Status = Quests.get("Mourning's Ends Part I").getStatus();
                return mourningsEndP1Status == Quest.Status.COMPLETE || mourningsEndP1Status == Quest.Status.IN_PROGRESS;

            }, true),
    //TODO: WILDERNESS MAGE_ARENA
    //Zeah
    //TODO: CHECK ZEAH QUESTS
    ARCEUUS(new Area.Rectangular(new Coordinate(1627, 3748, 0), new Coordinate(1632, 3746, 0)),
            () -> true, true),
    PORT_PISCARILIUS(new Area.Rectangular(new Coordinate(1797, 3790, 0), new Coordinate(1810, 3788, 0)),
            () -> true, true),
    LOVAKENGI(new Area.Rectangular(new Coordinate(1533, 3742, 0), new Coordinate(1519, 3739, 0)),
            () -> true, true),
    //TODO: Blast mine, Sulphur mine, Lovakite mine
    WOODCUTTING_GUILD(new Area.Rectangular(new Coordinate(1589, 3480, 0), new Coordinate(1593, 3475, 0)),
            () -> {
                if (KourendHouseFavour.HOSIDIUS.getPercent() >= 70){
                    return Skill.WOODCUTTING.getCurrentLevel() >= 60;
                }

                return false;
            }, true),
    LANDS_END(new Area.Polygonal(
            new Coordinate(1513, 3419, 0),
            new Coordinate(1513, 3423, 0),
            new Coordinate(1508, 3423, 0),
            new Coordinate(1508, 3416, 0),
            new Coordinate(1509, 3416, 0),
            new Coordinate(1509, 3419, 0)
    ), () -> true, true),
    HOSIDIUS(new Area.Rectangular(new Coordinate(1674, 3574, 0), new Coordinate(1678, 3561, 0)),
            () -> true, true),
    HOSIDIUS_KITCHEN(new Area.Rectangular(new Coordinate(1653, 3613, 0), new Coordinate(1659, 3606, 0)),
            () -> true, true),
    //Islands
    VOID_KNIGHTS_OUTPOST(new Area.Rectangular(new Coordinate(2665, 2655, 0), new Coordinate(2667, 2651, 0)),
            () -> true, true),
    MOS_LE_HARMLESS(new Area.Rectangular(new Coordinate(3679, 2982, 0), new Coordinate(3682, 2981, 0)),
            () -> {
                return Quests.get("Cabin Fever").getStatus() == Quest.Status.COMPLETE;

            }, true),
    FOSSIL_ISLAND_A(new Area.Rectangular(new Coordinate(3740, 3805, 0), new Coordinate(3741, 3803, 0)),
            () -> {
                return Quests.get("Bone Voyage").getStatus() == Quest.Status.COMPLETE;

            }, true),
    FOSSIL_ISLAND_B(new Area.Rectangular(new Coordinate(3819, 3810, 0), new Coordinate(3818, 3807, 0)),
            () -> {
                return Quests.get("Bone Voyage").getStatus() == Quest.Status.COMPLETE;

            }, true),
    /*FOSSIL_ISLAND_C(new Area.Rectangular(new Coordinate(3771, 3898, 0), new Coordinate(3770, 3897, 0)),
            () -> {
                if (Quests.get("Bone Voyage").getStatus() == Quest.Status.COMPLETE){
                    return true;
                }

                return false;
            }, true),*/
    MYTHS_GUILD(new Area.Rectangular(new Coordinate(2463, 2848, 1), new Coordinate(2465, 2846, 1)),
            () -> {
                return Quests.get("Dragon Slayer II").getStatus() == Quest.Status.COMPLETE;

            }, true),
    ZANARIS(new Area.Rectangular(new Coordinate(2382, 4460, 0), new Coordinate(2385, 4455, 0)),
            () -> {
                return Quests.get("Lost City").getStatus() == Quest.Status.COMPLETE;

            }, true),
    DORGESH_KAAN(new Area.Rectangular(new Coordinate(2705, 5348, 0), new Coordinate(2699, 5351, 0)),
            () -> {
                return Quests.get("Death to the Dorgeshuun").getStatus() == Quest.Status.COMPLETE;

            }, true)

    ;

    private final Callable<Boolean> canUse;
    private final Area area;
    private final boolean members;
    private final String name;


    BankLocation(Area area, Callable<Boolean> canUse, boolean members){
        this.area = area;
        this.canUse = canUse;
        this.members = members;
        this.name = name();
    }

    public boolean isMembers() {return members;}

    public boolean canUse(){
        try {
            if (members){
                /*
                if (RuneScape.isLoggedIn()){
                    if (com.runemate.game.api.hybrid.local.)
                    //CHECK IF PLAYER IS MEMBER IF NOT ONLY ALLOW F2P BANKS
                }
                */
            }

            boolean result = canUse.call();
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Area.Rectangular getArea() {
        return null;
    }

    @Override
    public Coordinate getPosition() {
        return null;
    }

    @Override
    public Coordinate.HighPrecision getHighPrecisionPosition() {
        return null;
    }
}
