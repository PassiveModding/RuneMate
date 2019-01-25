package com.passive.api.runescape.navigation;

import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;

public class Travel {
    public static boolean TravelTo(Coordinate location) {

        BresenhamPath p = BresenhamPath.buildTo(location);
        if (p != null) {
            p.step();
            return true;
        }

        return false;
    }

    public static boolean TravelTo(Area location) {

        BresenhamPath p = BresenhamPath.buildTo(location);
        if (p != null) {
            p.step();
            return true;
        } else {
            return TravelTo(location.getCenter());
        }
    }
}
