package com.passive.api.runescape.entities;

import com.passive.api.bot.PassiveBot;
import com.passive.api.runescape.LocalPlayer;
import com.passive.api.runescape.navigation.distance.Distance;
import com.passive.depreciated.util.MethodTimer;
import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.entities.details.Modeled;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.hud.InteractablePoint;
import com.runemate.game.api.hybrid.local.hud.InteractableRectangle;
import com.runemate.game.api.hybrid.local.hud.Model;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.queries.LocatableEntityQueryBuilder;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.Region;
import com.runemate.game.api.hybrid.util.Regex;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Created by SlashnHax on 2/02/2016.
 */
public abstract class LocatableEntityProfile<T extends LocatableEntity & Modeled, Q extends LocatableEntityQueryBuilder<T, Q>> {
    protected final SimpleStringProperty profileName = new SimpleStringProperty();
    protected final ObservableList<Pattern> names = new ObservableListWrapper<>(new ArrayList<>());
    protected final ObservableList<Pattern> actions = new ObservableListWrapper<>(new ArrayList<>());
    protected final ObservableList<Area> areas = new ObservableListWrapper<>(new ArrayList<>());
    protected final List<Predicate<T>> predicates = new ArrayList<>();
    protected final Predicate<T> predicateFilter = n -> predicates.stream().allMatch(p -> p.test(n));
    protected final SimpleBooleanProperty reachableOnly = new SimpleBooleanProperty(true);
    protected T current;
    //TODO: Change current to be an object property?
    protected List<T> invalidated = new ArrayList<>();

    public LocatableEntityProfile(String profileName) {
        this.profileName.set(profileName);
    }

    public double distanceTo(T entity, int[][][] flags) {
        Player local = LocalPlayer.getPlayer();
        if (flags != null) {
            RegionPath path = PassiveBot.getBot().getMethodTimer().timeAndGet("RegionPath.buildBetween(LocatableEntity, Collection<LocatableEntity>, int[][][])",
                    () -> RegionPath.buildBetween(local, Collections.singletonList(entity), flags));
            return path == null ? -1 : path.getVertices().size();
        } else {
            return Distance.distanceTo(entity);
        }
    }

    public abstract Q apply(Q builder);

    public abstract Q newQuery();

    public LocatableEntityQueryResults<T> getResults() {
        return PassiveBot.getBot().getMethodTimer().timeAndGet("LocatableEntityProfile.getResults()", () -> apply(newQuery()).results());
    }

    public T getEntity() {
        return current = isValid(current) ? current : getNewEntity();
    }

    public boolean isValid(T t) {
        return t != null && t.isValid() && !invalidated.contains(t) && apply(newQuery()).accepts(t);
    }

    public void invalidate(T t) {
        invalidated.add(t);
    }

    public T getNewEntity() {
        MethodTimer methodTimer = PassiveBot.getBot().getMethodTimer();
        return methodTimer.timeAndGet("LocatableEntityProfile.getNewEntry()", () -> {
            LocatableEntityQueryResults<T> queryResults = getResults();
            ArrayList<Double> distances = new ArrayList<>(queryResults.size());
            double lowestCoordDistance = -1;
            Iterator<T> queryIter = queryResults.iterator();
            int[][][] flags = reachableOnly.get()
                    ? methodTimer.timeAndGet("Region.getCollisionFlags()", Region::getCollisionFlags)
                    : null;
            while (queryIter.hasNext()) {
                double currentDistance = distanceTo(queryIter.next(), flags);
                if (currentDistance == -1) {
                    queryIter.remove();
                } else {
                    if (lowestCoordDistance == -1 || currentDistance < lowestCoordDistance) {
                        lowestCoordDistance = currentDistance;
                    }
                    distances.add(currentDistance);
                }
            }
            final Point mousePoint = Mouse.getPosition();
            if (mousePoint == null) {
                return null;
            }
            double bestMouseDistance = -1;
            T res = null;
            for (int i = 0; i < queryResults.size(); i++) {
                if (distances.get(i) < lowestCoordDistance + Random.nextInt(5)) {
                    @SuppressWarnings("unchecked")
                    T current = queryResults.get(i);
                    InteractablePoint point = methodTimer.timeAndGet("Projectable.getModel().projectBoundingRectangle().getCenterPoint()", () -> {
                        Model m;
                        InteractableRectangle rect;
                        return (m = methodTimer.timeAndGet("Modeled.getModel()", new Callable<Model>() {
                            @Override
                            public Model call() throws Exception {
                                return current.getModel();
                            }
                        })) == null
                                ? null
                                : (rect = methodTimer.timeAndGet("Model.projectBoundingRectangle()", m::projectBoundingRectangle)) == null
                                ? null
                                : methodTimer.timeAndGet("InteractableRectangle.getCenterPoint()", rect::getCenterPoint);
                    });
                    double currentMouseDistance = point == null ? Double.MAX_VALUE : (Math.pow(point.getX() - mousePoint.getX(), 2) + Math.pow(point.getY() - mousePoint.getY(), 2)) * Random.nextGaussian(0.85, 1.5, 1);
                    if (currentMouseDistance != -1 && (bestMouseDistance == -1 || currentMouseDistance < bestMouseDistance)) {
                        bestMouseDistance = currentMouseDistance;
                        res = current;
                    }
                }
            }
            return res;
        });
    }

    public StringProperty getProfileNameProperty() {
        return profileName;
    }

    public ObservableList<Pattern> getNames() {
        return names;
    }

    public boolean addNames(String... names) {
        return this.names.addAll(Regex.getPatternsForExactStrings(names));
    }

    public ObservableList<Pattern> getActions() {
        return actions;
    }

    public boolean addActions(String... actions) {
        return this.actions.addAll(Regex.getPatternsForExactStrings(actions));
    }

    public boolean addActions(Pattern... actions) {
        return this.actions.addAll(actions);
    }

    public ObservableList<Area> getAreas() {
        return areas;
    }

    public boolean addAreas(Area... areas) {
        return this.areas.addAll(areas);
    }

    public List<Predicate<T>> getPredicates() {
        return predicates;
    }

    public boolean addPredicate(Predicate<T> predicate) {
        return predicates.add(predicate);
    }

    public BooleanProperty getReachableOnly() {
        return reachableOnly;
    }
}
