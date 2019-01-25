package com.passive.api.runescape.navigation.locations.banks;

import com.passive.api.runescape.navigation.distance.Distance;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.entities.details.Onymous;
import com.runemate.game.api.hybrid.local.WorldOverview;
import com.runemate.game.api.hybrid.local.Worlds;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Taffy on 5/1/2015.
 */

public enum BankLocation implements Locatable, Onymous {
    //Asgarnia
    FALADOR_WEST(new Area.Rectangular(new Coordinate(2943, 3368, 0), new Coordinate(2949, 3373, 0)), false),
    FALADOR_EAST(new Area.Rectangular(new Coordinate(3009, 3355, 0), new Coordinate(3018, 3358, 0)), false),
    CLAN_CAMP(new Area.Rectangular(new Coordinate(2953, 3294, 0), new Coordinate(2960, 3300, 0)), false),
    //PORT_SARIM_DEPOSITBOX(new Area.Rectangular(new Coordinate(3045, 3234, 0), new Coordinate(3049, 3237, 0)), false),
    CLAN_WARS(new Area.Rectangular(new Coordinate(3000, 9677, 0), new Coordinate(3003, 9681, 0)), false),
    FIST_OF_GUTHIX(new Area.Rectangular(new Coordinate(1700, 5595, 0), new Coordinate(1709, 5603, 0)), false),
    //STEALING_CREATION_DEPOSITBOX(new Area.Rectangular(new Coordinate(2967, 9702, 0), new Coordinate(2970, 9706, 0)), false),
    BLACK_KNIGHTS_FORTRESS(new Area.Rectangular(new Coordinate(3026, 3574, 3), new Coordinate(3028, 3579, 3)), false),
    TAVERLEY(new Area.Rectangular(new Coordinate(2877, 3415, 0), new Coordinate(2877, 3419, 0)), false),
    BURTHORPE(new Area.Rectangular(new Coordinate(2885, 3533, 0), new Coordinate(2892, 3540, 0)), false),
    //Members
    ROGUES_GUILD(new Area.Circular(new Coordinate(3030, 4956, 0), 7), true), //hoping this is rogue's den
    WARRIORS_GUILD(null, true),
    RIMMINGTON_LOCKER(null, true),
    PORT_SARIM(null, true),

    //Dwarven realm
    //DWARVEN_MINE_RESOURCE_DUNGEON_DEPOSITBOX(new Area.Rectangular(new Coordinate(1041, 4576, 0), new Coordinate(1044, 4579, 0)), false),
    //Members
    KELDAGRIM(null, true),

    //Feldip Hills members
    MOBILISING_ARMIES(new Area.Rectangular(new Coordinate(2400, 2840, 0), new Coordinate(2405, 2843, 0)), true),
    OOGLOG(new Area.Rectangular(new Coordinate(2553, 2836, 0), new Coordinate(2559, 2841, 0)), true),

    //Fremmnic Province members
    BARBARIAN_OUTPOST(new Area.Rectangular(new Coordinate(2534, 3570, 0), new Coordinate(2538, 3574, 0)), true),
    NEITZINOT(new Area.Rectangular(new Coordinate(2334, 3808, 0), new Coordinate(2339, 3806, 0)), true),
    JATIZSO(null, true),
    ETCETERIA(null, true),
    PEER_THE_SEER(null, true),

    //Lunar Isle members
    LUNAR_ISLE(new Area.Rectangular(new Coordinate(2097, 3917, 0), new Coordinate(2104, 3919, 0)), true),

    //Kandarin members
    CATHERBY(new Area.Rectangular(new Coordinate(2806, 3438, 0), new Coordinate(2812, 3443, 0)), true),
    SEERS_VILLAGE(new Area.Polygonal(new Coordinate(2720, 3495, 0), new Coordinate(2721, 3490, 0), new Coordinate(2723, 3489, 0), new Coordinate(2724, 3486, 0), new Coordinate(2728, 3487, 0), new Coordinate(2728, 3490, 0), new Coordinate(2731, 3490, 0), new Coordinate(2730, 3496, 0)), true),
    EAST_ARDOUGNE_NORTH(new Area.Rectangular(new Coordinate(2612, 3332, 0), new Coordinate(2621, 3335, 0)), true),
    EAST_ARDOUGNE_MARKET(new Area.Rectangular(new Coordinate(2649, 3280, 0), new Coordinate(2655, 3287, 0)), true),
    YANILLE(new Area.Rectangular(new Coordinate(2609, 3088, 0), new Coordinate(2613, 3097, 0)), true),
    LEGENDS_GUILD(null, true),
    OURANIA_CAVE(null, true),
    CASTLE_WARS(new Area.Rectangular(new Coordinate(2444, 3081, 0), new Coordinate(2448, 3087, 0)), true),
    TREE_GNOME_STRONGHOLD(null, true),
    PISCATORIS(null, true),
    PISCATORIS_DEPOSITBOX(null, true),
    PORT_KHAZARD_DEPOSITBOX(null, true),
    FISHING_GUILD(null, true),

    //Karamja members
    //Tzaar
    TZAAR_PLAZA(new Area.Rectangular(new Coordinate(4643, 5147, 0), new Coordinate(4646, 5152, 0)), true),
    FIGHT_KILN_ENTRANCE(new Area.Rectangular(new Coordinate(4740, 5170, 0), new Coordinate(4744, 5173, 0)), true),
    FIGHT_PIT_ENTRANCE(new Area.Rectangular(new Coordinate(4597, 5061, 0), new Coordinate(4604, 5065, 0)), true),
    FIGHT_CAVE_ENTRANCE(new Area.Rectangular(new Coordinate(4607, 5129, 0), new Coordinate(4614, 5133, 0)), true),
    FIGHT_CAULDRON_ENTRANCE(null, true),
    FIGHT_CAULDRON(null, true),
    //Shilo village
    SHILO_VILLAGE(null, true),
    //Tai bwo wannai
    RIONASTA(null, true),
    TAI_BWO_WANNAI_DEPOSITBOX(null, true),
    //Herblore habitat
    HERBLORE_HABITAT_DEPOSITBOX(null, true),

    //Misthalin
    LUMBRIDGE_CASTLE(new Area.Rectangular(new Coordinate(3207, 3215, 2), new Coordinate(3210, 3220, 2)), false),
    COMBAT_ACADEMY(new Area.Rectangular(new Coordinate(3211, 3255, 0), new Coordinate(3215, 3260, 0)), false),
    VARROCK_WEST(new Area.Rectangular(new Coordinate(3182, 3433, 0), new Coordinate(3189, 3446, 0)), false),
    VARROCK_EAST(new Area.Rectangular(new Coordinate(3250, 3419, 0), new Coordinate(3257, 3423, 0)), false),
    GRAND_EXCHANGE_NORTHWEST(new Area.Rectangular(new Coordinate(3145, 3501, 0), new Coordinate(3152, 3508, 0)), false),
    GRAND_EXCHANGE_NORTHEAST(new Area.Rectangular(new Coordinate(3177, 3501, 0), new Coordinate(3184, 3508, 0)), false),
    GRAND_EXCHANGE_SOUTHWEST(new Area.Rectangular(new Coordinate(3145, 3475, 0), new Coordinate(3152, 3482, 0)), false),
    GRAND_EXCHANGE_SOUTHEAST(new Area.Rectangular(new Coordinate(3177, 3475, 0), new Coordinate(3184, 3482, 0)), false),
    WELL_OF_GOODWILL(new Area.Rectangular(new Coordinate(3160, 3451, 0), new Coordinate(3165, 3456, 0)), false),
    EDGEVILLE(new Area.Rectangular(new Coordinate(3091, 3488, 0), new Coordinate(3098, 3499, 0)), false),
    DRAYNOR(new Area.Rectangular(new Coordinate(3092, 3240, 0), new Coordinate(3096, 3246, 0)), false),
    //RUNECRAFTING_GUILD_DEPOSITBOX(new Area.Rectangular(new Coordinate(1703, 5471, 2), new Coordinate(1706, 5474, 2)), false),
    //WIZARDS_TOWER_DEPOSITBOX(new Area.Rectangular(new Coordinate(3108, 3158, 3), new Coordinate(3111, 3162, 3)), false),
    //members
    CULINAROMANCERS_CHEST(null, true),
    THIEVES_GUILD(null, true),
    COOKS_GUILD(null, true),

    //Kharidian desert
    AL_KHARID(new Area.Rectangular(new Coordinate(3268, 3162, 0), new Coordinate(3272, 3168, 0)), false),
    DUEL_ARENA_ENTRANCE(new Area.Rectangular(new Coordinate(3346, 3236, 0), new Coordinate(3351, 3241, 0)), false),
    DUEL_ARENA_TENT(new Area.Rectangular(new Coordinate(3379, 3267, 0), new Coordinate(3384, 3273, 0)), false),
    //members
    NARDAH(new Area.Rectangular(new Coordinate(3426, 2889, 0), new Coordinate(3429, 2894, 0)), true),
    POLLNIVNEACH_DEPOSITBOX(null, true),
    SOPHANEM(null, true),
    SHANTAY_PASS(null, true),
    BEDABIN_CAMP_DEPOSITBOX(null, true),
    DOMINION_TOWER(null, true),

    //Morytania members
    CANIFIS(new Area.Rectangular(new Coordinate(3509, 3483, 0), new Coordinate(3512, 3478, 0)), true),
    //MORT_MYRE_DEPOSITBOX(null, true),
    PORT_PHASMATYS(null, true),
    BURGH_DE_ROTT(null, true),
    RAZORLOR_LAIR_ODOVACAR(null, true),
    DARKMEYER(null, true),

    //Tirannwn members
    LLETYA(null, true),
    //ELF_CAMP_DEPOSITBOX(null, true),
    TOWER_OF_VOICES(null, true),
    CRWYS_CLAN(null, true),
    MEILYR_CLAN(null, true),
    HEFIN_CLAN(null, true),
    ITHELL_CLAN(new Area.Rectangular(new Coordinate(2152, 3339, 1), new Coordinate(2155, 3342, 1)), true),
    IORWERTH_CLAN(null, true),
    TRAHEARN_CLAN(null, true),
    CADARN_CLAN(null, true),

    //Troll country members
    GOD_WARS_DUNGEON_ASHEULOT(null, true),

    //Wilderness members
    MAGE_ARENA(null, true),

    //Islands members
    VOID_KNIGHT_OUTPOST(null, true),
    MOS_LE_HARMLESS(null, true),
    //TROUBLE_BREWING_DEPOSITBOX(null, true),
    //HARMONY_ISLAND_DEPOSITBOX(null, true),
    SOUL_WARS(null, true),

    //Other places members
    ZANARIS(null, true),
    DORGESH_KAAN(null, true),

    //Daemonheim
    DAEMONHEIM(new Area.Rectangular(new Coordinate(3446, 3716, 0), new Coordinate(3451, 3723, 0)), false);

    private final static BankLocation[] osBanks = {LUMBRIDGE_CASTLE, VARROCK_EAST, VARROCK_WEST, AL_KHARID};
    private final String name;
    private Area area;
    private boolean members;

    BankLocation(Area area, boolean members) {
        this.area = area;
        this.members = members;
        this.name = name(); //TODO: use proper names
    }

    public static BankLocation getNearest() {
        return Distance.getNearest(Environment.isRS3() ? getRs3Banks() : getOsBanks());
    }

    public static BankLocation[] getRs3Banks() {
        return values();
    }

    public static BankLocation[] getOsBanks() {
        return osBanks;
    }

    public static Collection<? extends BankLocation> getMappedLocations() {
        ArrayList<BankLocation> mapped = new ArrayList<>();
        for (BankLocation l : values()) {
            int currentWorld = Worlds.getCurrent();
            WorldOverview currentWorldOverview = null;
            if (currentWorld != -1)
                currentWorldOverview = Worlds.getOverview(currentWorld);
            boolean membersWorld = currentWorldOverview != null && currentWorldOverview.isMembersOnly();
            if (l.getArea() != null && (membersWorld || !l.isMembers()))
                mapped.add(l);
        }
        return mapped;
    }

    public boolean isMembers() {
        return members;
    }

    @Override
    public Area.Rectangular getArea() {
        return area == null ? null : area.toRectangular();
    }

    @Override
    public Coordinate getPosition() {
        return area == null ? null : area.getPosition();
    }

    @Override
    public Coordinate.HighPrecision getHighPrecisionPosition() {
        return area == null ? null : area.getHighPrecisionPosition();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

}