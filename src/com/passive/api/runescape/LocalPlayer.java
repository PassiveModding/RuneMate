package com.passive.api.runescape;

import com.passive.api.bot.Passive_BOT;
import com.passive.api.runescape.interfaces.Interfaces;
import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.entities.Actor;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by Taffy on 13/12/2015.
 */
public class LocalPlayer {
    public static Player getPlayer() {
        Passive_BOT bot = Passive_BOT.getBot();
        return bot == null ? null : bot.getMethodTimer().timeAndGet(Players::getLocal);
    }

    public static boolean isMoving() {
        Player p = getPlayer();
        return p != null && p.isMoving();
    }

    public static boolean isAnimated() {
        Player p = getPlayer();
        return p != null && p.getAnimationId() != -1;
    }

    public static boolean canReach(Locatable l) {
        Coordinate c = l == null ? null : l.getPosition();
        return c != null && c.isReachable();
    }

    public static Coordinate getPosition() {
        Player p = getPlayer();
        return p == null ? null : p.getPosition();
    }

    public static int getAnimation() {
        Player p = getPlayer();
        return p == null ? -1 : p.getAnimationId();
    }

    public static int getAnimationId() {
        return getAnimation();
    }

    public static String getName() {
        Player p = getPlayer();
        return p == null ? null : p.getName();
    }

    public static Actor getTarget() {
        Player p = getPlayer();
        return p == null ? null : p.getTarget();
    }

    public static Collection<Npc> getNpcsTargeting() {
        Collection<Npc> npcs = new ArrayList<>();
        Player p = getPlayer();
        if (p == null)
            return npcs;
        else
            return Npcs.getLoaded(p);
    }

    public static void ensureLogout() {
        for (int i = 0; i < 5; i++) {
            if (RuneScape.isLoggedIn()){
                Interfaces.closeInterfacesOrWait();

                RuneScape.logout();
                Execution.delayUntil(() -> !RuneScape.isLoggedIn(), 500, 5000);
            }
        }
    }

    public static boolean hasTarget() {
        return Passive_BOT.getBot().getMethodTimer().timeAndGet(() -> {
            Actor target;
            return (target = getTarget()) != null && target.isValid();
        });
    }

    public static boolean isTargetedBy(Actor a) {
        return Passive_BOT.getBot().getMethodTimer().timeAndGet(() -> {
            Actor target;
            return a != null && (target = a.getTarget()) != null && target.equals(getPlayer());
        });
    }

    public static boolean canLogout() {
        //TODO: Check for health Gauge
        return !RuneScape.isLoggedIn() || !LocalPlayer.hasTarget() && Passive_BOT.getBot().getPlayerTracker().hasNoGuageUpFor(5, TimeUnit.SECONDS);
    }

    public static boolean isWithin(Area a) {
        Player p = getPlayer();
        return a != null && p != null && a.contains(p);
    }

    public static boolean isWithin(Collection<Area> areas) {
        Player p = getPlayer();
        return p != null && areas.stream().anyMatch(a -> a.contains(p));
    }

    public static boolean isWithin(Area... areas) {
        Player p = getPlayer();
        if (p != null) {
            for (Area a : areas) {
                if (a.contains(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isTargeted() {
        return !getNpcsTargeting().isEmpty();
    }

    public static boolean isInCombat() {
        return hasTarget() || isTargeted();
    }
}
