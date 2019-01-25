package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;

public class Positions {
    public static Area.Circular SurvivalExpert = new Area.Circular(new Coordinate(3103,3096), 5);
    public static Area.Rectangular GateSurvivalSide = new Area.Rectangular(new Coordinate(3090, 3091), new Coordinate(3090, 3092));
    public static Coordinate CookGuideStartDoorOutside = new Coordinate(3079,3084);
    public static Coordinate CookGuideExitDoorInside = new Coordinate(3074,3090);
    public static Coordinate QuestGuideDoorOutside = new Coordinate(3086,3126);
    public static Area.Circular MiningInstructor = new Area.Circular(new Coordinate(3079,9505), 5);
    // TODO: Make this an area instead of single square
    public static Coordinate MiningTutorGateExit = new Coordinate(3094, 9503);

    public static Coordinate CombatInstructorExitLadder = new Coordinate(3111, 9524);
    public static Coordinate CombatInstructorRatGateOutside = new Coordinate(3111, 9518);
    public static Coordinate CombatInstructorRatGateInside = new Coordinate(3109, 9518);

    public static Area.Circular BankRoom = new Area.Circular(new Coordinate(3121,3123), 3);
    public static Coordinate PollBooth = new Coordinate(3120, 3121);
    public static Coordinate ExitBankRoom = new Coordinate(3124, 3124);
    public static Coordinate BankBoothInfront = new Coordinate(3122,3123);
}
