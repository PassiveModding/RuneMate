package com.passive.api.runescape.entities;

import com.passive.api.eclipsesource.json.JsonArray;
import com.passive.api.eclipsesource.json.JsonObject;
import com.passive.api.eclipsesource.json.JsonValue;
import com.passive.api.runescape.LocalPlayer;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.queries.NpcQueryBuilder;
import com.runemate.game.api.hybrid.region.Npcs;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class NpcProfile extends LocatableEntityProfile<Npc, NpcQueryBuilder> {
    private final ObservableList<Integer> levels = new ObservableListWrapper<>(new ArrayList<>());
    private final Predicate<Npc> levelPredicate = n -> this.levels.contains(n.getLevel());
    private final BooleanProperty singleTarget = new SimpleBooleanProperty(true);
    private Pattern[] actionsArray = {};
    private int[] appearances;

    public NpcProfile(String profileName, JsonObject obj) {
        super(profileName);
        this.levels.addListener((ListChangeListener<Integer>) c -> {
            c.next();
            if (c.getList().isEmpty()) {
                predicates.remove(levelPredicate);
            } else if (c.getList().size() == c.getAddedSize()) {
                predicates.add(levelPredicate);
            }
        });

        this.actions.addListener((ListChangeListener<Pattern>) c -> {
            c.next();
            actionsArray = actions.toArray(new Pattern[actions.size()]);
        });

        if (obj != null) {
            JsonArray names = obj.get("names").asArray();
            for (JsonValue p : names) {
                String val = p.asString();
                String[] split = val.split(":");
                String pattern = split[0];
                int flags = 0;
                if (split.length == 2) {
                    flags = Integer.parseInt(split[1]);
                }
                this.names.add(Pattern.compile(pattern, flags));
            }
            JsonArray actions = obj.get("actions").asArray();
            for (JsonValue p : actions) {
                this.actions.add(Pattern.compile(p.asString()));
            }
            reachableOnly.set(obj.getBoolean("reachable", true));
            JsonArray areas = obj.get("areas").asArray();
            for (JsonValue a : areas) {
                JsonObject aO = a.asObject();
                this.areas.add(new Area.Rectangular(new Coordinate(aO.getInt("bl", 0)), new Coordinate(aO.getInt("tr", 0))));
            }
            JsonArray levels = obj.get("levels").asArray();
            for (JsonValue l : levels) {
                this.levels.add(l.asInt());
            }
            singleTarget.set(obj.getBoolean("single_target", true));
        }
    }

    @Override
    public NpcQueryBuilder apply(NpcQueryBuilder builder) {
        if (!names.isEmpty()) {
            builder.names(names);
        }
        if (!actions.isEmpty()) {
            builder.actions(actionsArray);
        }
        if (appearances != null) {
            builder.appearance(appearances);
        }
        if (!areas.isEmpty()) {
            builder.within(areas);
        }
        if (!predicates.isEmpty()) {
            builder.filter(predicateFilter);
        }
        if (singleTarget.get()) {
            builder.targeting(null, LocalPlayer.getPlayer());
            builder.targetedBy(null, LocalPlayer.getPlayer());
        }
        return builder;
    }

    @Override
    public NpcQueryBuilder newQuery() {
        return Npcs.newQuery();
    }

    public ObservableList<Integer> getLevels() {
        return levels;
    }

    public void setAppearances(int... appearances) {
        this.appearances = appearances;
    }

    public BooleanProperty getSingleTarget() {
        return singleTarget;
    }

    public JsonObject toJsonObject() {
        JsonObject res = new JsonObject();
        JsonArray names = new JsonArray();
        for (Pattern p : this.names) {
            names.add(p.pattern() + ":" + p.flags());
        }
        res.add("names", names);
        JsonArray actions = new JsonArray();
        for (Pattern p : actionsArray) {
            actions.add(p.pattern());
        }
        res.add("actions", actions);
        res.add("reachable", reachableOnly.get());
        JsonArray areas = new JsonArray();
        for (Area a : this.areas) {
            Area.Rectangular rect = a.toRectangular();
            JsonObject aO = new JsonObject();
            aO.add("bl", rect.getBottomLeft().hashCode());
            aO.add("tr", rect.getTopRight().hashCode());
            areas.add(aO);
        }
        res.add("areas", areas);
        JsonArray levels = new JsonArray();
        for (int i : this.levels) {
            levels.add(i);
        }
        res.add("levels", levels);
        res.add("single_target", singleTarget.get());
        return res;
    }

}
