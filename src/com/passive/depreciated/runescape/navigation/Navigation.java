package com.passive.api.runescape.navigation;

import com.passive.api.bot.PassiveBot;
import com.passive.api.runescape.LocalPlayer;
import com.passive.depreciated.util.MethodTimer;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.location.navigation.web.Web;
import com.runemate.game.api.hybrid.util.calculations.Random;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Taffy on 13/12/2015.
 */
public class Navigation {
    public static Path build(Locatable start, Locatable dest, Web web, Coordinate[] predefined, PathType... redundancy) {
        MethodTimer methodTimer = PassiveBot.getBot().getMethodTimer();
        return methodTimer.timeAndGet(() -> {
            Collection<Coordinate> destinations = dest instanceof GameObject
                    ? dest.getArea().getSurroundingCoordinates()
                    : Collections.singletonList(dest.getPosition());
            Path result = null;
            for (PathType pathType : redundancy) {
                switch (pathType) {
                    case WEB:
                        result = methodTimer.timeAndGet(() -> web.getPathBuilder().build(start, Random.nextElement(destinations)));
                        PassiveBot.getBot().debug("WebPath from " + start + " to " + dest + ": " + result);
                        break;
                    case REGION:
                        result = methodTimer.timeAndGet(() -> RegionPath.buildBetween(start, destinations));
                        PassiveBot.getBot().debug("RegionPath from " + start + " to " + dest + ": " + result);
                        break;
                    case BRESENHAM:
                        result = methodTimer.timeAndGet(() -> BresenhamPath.buildBetween(start, Random.nextElement(destinations)));
                        PassiveBot.getBot().debug("BresenhamPath from " + start + " to " + dest + ": " + result);
                        break;
                    case PREDEFINED:
                        result = methodTimer.timeAndGet(() -> PredefinedPath.create(predefined));
                        PassiveBot.getBot().debug("PredefinedPath from " + start + " to " + dest + ": " + result);
                        break;
                }
                if (result != null) {
                    break;
                }
            }
            return result;
        });
    }

    public static Path build(Locatable start, Locatable dest) {
        return build(start, dest, Traversal.getDefaultWeb(), null, PathType.REGION, PathType.WEB, PathType.BRESENHAM);
    }

    public static Path buildTo(Locatable destination) {
        return build(LocalPlayer.getPosition(), destination);
    }

    public enum PathType {
        WEB, REGION, BRESENHAM, PREDEFINED
    }
}
