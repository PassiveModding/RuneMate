package com.passive.api.runescape.navigation.distance;

import com.passive.api.runescape.LocalPlayer;
import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by SlashnHax on 19/01/2016.
 */
public class Distance {
    public static double distance(Locatable start, Locatable end) {
        return Algorithm.MANHATTAN.distance(start, end);
    }

    public static double distanceTo(Locatable end) {
        return distance(LocalPlayer.getPosition(), end);
    }

    public static <T extends Locatable> T getNearest(Collection<T> locatables) {
        T res = null;
        Coordinate start = LocalPlayer.getPosition();
        double shortestDistance = -1;
        for (T t : locatables) {
            double currentDistance = distance(start, t);
            if (shortestDistance == -1 || currentDistance != -1 && currentDistance < shortestDistance) {
                shortestDistance = currentDistance;
                res = t;
            }
        }
        return res;
    }

    public static <T extends Locatable> T getNearest(T... locatables) {
        return getNearest(Arrays.asList(locatables));
    }

    public static int getWalkingTime(Locatable l) {
        return (int) (Math.max(distanceTo(l), 0) * Random.nextGaussian(1200, 2000, 1300));
    }

    public enum Algorithm implements AlgorithmInterface {
        MANHATTAN {
            @Override
            public double distance(Locatable start, Locatable end) {
                Coordinate c1 = start == null ? null : start.getPosition();
                Coordinate c2 = end == null ? null : end.getPosition();
                if (c1 == null || c2 == null)
                    return -1;
                return Math.abs(c1.getX() - c2.getX())
                        + Math.abs(c1.getY() - c2.getY());
            }
        }, EUCLIDEAN {
            @Override
            public double distance(Locatable start, Locatable end) {
                Coordinate c1 = start == null ? null : start.getPosition();
                Coordinate c2 = end == null ? null : end.getPosition();
                if (c1 == null || c2 == null)
                    return -1;
                return Math.sqrt(Math.pow(c1.getX() - c2.getX(), 2)
                        + Math.pow(c1.getY() - c2.getY(), 2));
            }
        }, EUCLIDEAN_SQUARED {
            @Override
            public double distance(Locatable start, Locatable end) {
                Coordinate c1 = start == null ? null : start.getPosition();
                Coordinate c2 = end == null ? null : end.getPosition();
                if (c1 == null || c2 == null)
                    return -1;
                return Math.pow(c1.getX() - c2.getX(), 2)
                        + Math.pow(c1.getY() - c2.getY(), 2);
            }
        }
    }

    public interface AlgorithmInterface {
        double distance(Locatable start, Locatable end);

        default double distanceTo(Locatable end) {
            return distance(Players.getLocal(), end);
        }
    }
}
