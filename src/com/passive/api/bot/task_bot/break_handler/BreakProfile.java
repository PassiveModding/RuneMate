package com.passive.api.bot.task_bot.break_handler;

import com.passive.api.ui.javafx.JavaFX;
import com.passive.api.util.Formatting;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by SlashnHax on 20/12/2015.
 */
public class BreakProfile {
    public static BreakProfile DISABLED = new BreakProfile("Disabled", 0, 2, 1, 0, 2, 1) {
        public Break getCurrentBreak(long currentTime) {
            return null;
        }
    };
    public static BreakProfile DEFAULT = new BreakProfile("Default", 300000, 7200000, 2700000, 15000, 2700000, 900000);

    private final StringProperty nameProperty = new SimpleStringProperty();
    private final IntegerProperty minGapProperty = new SimpleIntegerProperty();
    private final IntegerProperty maxGapProperty = new SimpleIntegerProperty();
    private final IntegerProperty avgGapProperty = new SimpleIntegerProperty();

    private final IntegerProperty minLengthProperty = new SimpleIntegerProperty();
    private final IntegerProperty maxLengthProperty = new SimpleIntegerProperty();
    private final IntegerProperty avgLengthProperty = new SimpleIntegerProperty();

    private final ObservableList<Break> breaks = new ObservableListWrapper<>(new CopyOnWriteArrayList<>());

    public BreakProfile(String name, int minGap, int maxGap, int avgGap, int minLength, int maxLength, int avgLength) {
        this.nameProperty.set(name);
        addValidationListeners();
        minGapProperty.set(minGap);
        maxGapProperty.set(maxGap);
        avgGapProperty.set(avgGap);
        minLengthProperty.set(minLength);
        maxLengthProperty.set(maxLength);
        avgLengthProperty.set(avgLength);
    }

    public BreakProfile(String name) {
        this(name, 300000, 7200000, 2700000, 15000, 2700000, 900000);
    }

    public void removeExpiredBreaks(long currentTime) {
        Platform.runLater(() -> {
            synchronized (breaks) {
                breaks.removeIf(b -> b.isExpired(currentTime));
            }
        });
    }

    public Break getCurrentBreak(long currentTime) {
        removeExpiredBreaks(currentTime);
        if (breaks.isEmpty())
            generateBreaks(25);
        synchronized (breaks) {
            return breaks.stream().filter(b -> b.isActive(currentTime)).findAny().orElse(null);
        }
    }

    public void generateBreaks(int iterations) {
        long currentTime = 0;
        synchronized (breaks) {
            currentTime = breaks.stream().mapToLong(Break::getEndTime).max().orElse(0);
        }
        long breakLength;
        long gap;
        for (int i = 0; i < iterations; i++) {
            if (avgGapProperty.get() > maxGapProperty.get()) {
                avgGapProperty.set(DEFAULT.avgGapProperty.get());
                maxGapProperty.set(DEFAULT.maxGapProperty.get());
            } else if (avgGapProperty.get() < minGapProperty.get()) {
                minGapProperty.set(DEFAULT.minGapProperty.get());
                avgGapProperty.set(DEFAULT.avgGapProperty.get());
            }
            if (avgLengthProperty.get() > maxLengthProperty.get()) {
                avgLengthProperty.set(DEFAULT.avgLengthProperty.get());
                maxLengthProperty.set(DEFAULT.maxLengthProperty.get());
            } else if (avgLengthProperty.get() < minLengthProperty.get()) {
                minLengthProperty.set(DEFAULT.minLengthProperty.get());
                avgLengthProperty.set(DEFAULT.avgLengthProperty.get());
            } else {
                gap = (long) Random.nextGaussian(minGapProperty.get(), maxGapProperty.get(), avgGapProperty.get());
                currentTime += gap;
                breakLength = (long) Random.nextGaussian(minLengthProperty.get(), maxLengthProperty.get(), avgLengthProperty.get());
                synchronized (breaks) {
                    breaks.add(new Break(currentTime, breakLength));
                }
                currentTime += breakLength;
            }
        }
    }

    public void unbind(StringProperty minGapTxt, StringProperty maxGapTxt, StringProperty averageGapTxt,
                       StringProperty minLengthTxt, StringProperty maxLengthTxt, StringProperty averageLengthTxt) {
        JavaFX.wrap(() -> {
            minGapTxt.unbindBidirectional(minGapProperty);
            maxGapTxt.unbindBidirectional(maxGapProperty);
            averageGapTxt.unbindBidirectional(avgGapProperty);
            minLengthTxt.unbindBidirectional(minLengthProperty);
            maxLengthTxt.unbindBidirectional(maxLengthProperty);
            averageLengthTxt.unbindBidirectional(avgLengthProperty);
        }).run();
    }

    public void bind(StringProperty minGapTxt, StringProperty maxGapTxt, StringProperty averageGapTxt,
                     StringProperty minLengthTxt, StringProperty maxLengthTxt, StringProperty averageLengthTxt) {
        JavaFX.wrap(() -> {
            minGapTxt.bindBidirectional(minGapProperty, Formatting.getTimeStringConverter());
            maxGapTxt.bindBidirectional(maxGapProperty, Formatting.getTimeStringConverter());
            averageGapTxt.bindBidirectional(avgGapProperty, Formatting.getTimeStringConverter());
            minLengthTxt.bindBidirectional(minLengthProperty, Formatting.getTimeStringConverter());
            maxLengthTxt.bindBidirectional(maxLengthProperty, Formatting.getTimeStringConverter());
            averageLengthTxt.bindBidirectional(avgLengthProperty, Formatting.getTimeStringConverter());
        }).run();
    }

    public String getName() {
        return nameProperty.get();
    }

    public void setName(String name) {
        this.nameProperty.set(name);
    }

    @Override
    public String toString() {
        return nameProperty.get();
    }

    public void addValidationListeners() {
        atLeastZero(minGapProperty);
        inBetween(avgGapProperty, minGapProperty, maxGapProperty);
        greaterThan(maxGapProperty, minGapProperty);

        atLeastZero(minLengthProperty);
        inBetween(avgLengthProperty, minLengthProperty, maxLengthProperty);
        greaterThan(maxLengthProperty, minLengthProperty);
    }

    private void atLeastZero(IntegerProperty property) {
        property.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() < 0)
                property.set(oldValue.intValue());
        });
    }

    private void inBetween(IntegerProperty property, IntegerProperty min, IntegerProperty max) {
        min.addListener((obs, oldV, newV) -> {
            if (newV.intValue() > property.get())
                property.set(oldV.intValue());
        });
        max.addListener((obs, oldV, newV) -> {
            if (newV.intValue() < property.get())
                property.set(oldV.intValue());
        });
    }

    private void greaterThan(IntegerProperty property, IntegerProperty min) {
        min.addListener((obs, oldV, newV) -> {
            if (newV.intValue() > property.get())
                property.set(oldV.intValue());
        });
    }

    public ObservableList<Break> getBreaks() {
        return breaks;
    }

    public void addSaveListener(BreakHandler handler) {
        nameProperty.addListener(((observable, oldValue, newValue) -> handler.saveProfiles()));
        minLengthProperty.addListener(((observable, oldValue, newValue) -> handler.saveProfiles()));
        maxLengthProperty.addListener(((observable, oldValue, newValue) -> handler.saveProfiles()));
        avgLengthProperty.addListener(((observable, oldValue, newValue) -> handler.saveProfiles()));
        minGapProperty.addListener(((observable, oldValue, newValue) -> handler.saveProfiles()));
        maxGapProperty.addListener(((observable, oldValue, newValue) -> handler.saveProfiles()));
        avgGapProperty.addListener(((observable, oldValue, newValue) -> handler.saveProfiles()));
    }

}
