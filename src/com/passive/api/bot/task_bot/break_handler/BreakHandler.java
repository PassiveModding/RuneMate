package com.passive.api.bot.task_bot.break_handler;

import com.passive.api.bot.task_bot.Task;
import com.passive.api.runescape.interfaces.Interfaces;
import com.passive.api.util.Formatting;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * Handles Breaking strategies.
 */
public class BreakHandler extends Task {
    private Break currentBreak;
    private ObjectProperty<BreakProfile> selectedProfile = new SimpleObjectProperty<>();
    private ObservableList<BreakProfile> profiles = new ObservableListWrapper<>(new ArrayList<>());
    private Collection<GameEvents.GameEvent> handlers = new ArrayList<>();
    private BooleanBinding enabled = selectedProfile.isNotEqualTo(BreakProfile.DISABLED);

    public BreakHandler() {
        profiles.addAll(BreakProfile.DEFAULT, BreakProfile.DISABLED);
        loadProfiles();
        selectedProfile.addListener((observable, oldValue, newValue) -> bot.getSettings().setProperty("selected_break_profile", newValue == null ? BreakProfile.DEFAULT.getName() : newValue.getName()));
        String profileName = bot.getSettings().getProperty("selected_break_profile", BreakProfile.DEFAULT.getName());
        selectProfile(profiles.stream().filter(b -> profileName.equals(b.getName())).findAny().orElse(BreakProfile.DEFAULT));
        handlers = Arrays.asList(GameEvents.Universal.LOBBY_HANDLER, GameEvents.Universal.LOGIN_HANDLER);
    }

    @Override
    public boolean validate() {
        return enabled.get() && bot.canBreak() && (currentBreak = selectedProfile.get().getCurrentBreak(bot.getRuntime())) != null;
    }

    @Override
    public void execute() {
        handlers.forEach(GameEvents.GameEvent::disable);
        if (currentBreak.getRemainingLength(bot.getRuntime()) > Random.nextGaussian(2500, 15000, 5000) && RuneScape.isLoggedIn()) {
            bot.setStatus("Break Handler: Logging out");
            Interfaces.closeInterfacesOrWait();
            if (RuneScape.logout(true)) {
                bot.setStatus("Break Handler: Waiting until " + Formatting.formatAsTime(currentBreak.getEndTime()));
                BreakProfile selected;
                while (bot.canBreak() && bot.isRunning() && (selected = selectedProfile.get()) != null && selected.getBreaks().contains(currentBreak) && currentBreak.isActive(bot.getRuntime())) {
                    Execution.delay(500);
                }
            }
        } else {
            bot.setStatus("Break Handler: Waiting until " + Formatting.formatAsTime(currentBreak.getEndTime()));
            while (bot.canBreak() && bot.isRunning() && selectedProfile.get().getBreaks().contains(currentBreak) && currentBreak.isActive(bot.getRuntime())) {
                Execution.delay(500);
            }
        }
        bot.getPlayerTracker().setLastAnimationTime(bot.getRuntime());
        handlers.forEach(GameEvents.GameEvent::enable);
    }


    public void selectProfile(BreakProfile profile) {
        this.selectedProfile.set(profile);
    }

    public ObjectProperty<BreakProfile> getSelectedProfileProperty() {
        return selectedProfile;
    }

    public BreakProfile getSelectedProfile() {
        return selectedProfile.get();
    }

    public ObservableList<BreakProfile> getProfiles() {
        return profiles;
    }

    public void removeProfile(BreakProfile profile) {
        if (profile == BreakProfile.DEFAULT || profile == BreakProfile.DISABLED) {
            bot.setStatus("Cannot remove " + profile.getName() + ".");
        } else if (profiles.remove(profile)) {
            bot.setStatus(profile + " removed.");
        }
    }

    public void loadProfiles() {
        try {
            getProfiles().addAll(bot.getPlatform().invokeAndWait(() -> {
                Collection<BreakProfile> res = new ArrayList<>();
                return res;
            }));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void saveProfiles() {
        /*
        JsonArray array = new JsonArray();
        for (BreakProfile b : getProfiles()) {
            array.add(b.toJsonObject());
        }
        bot.getPlatform().invokeLater(() -> bot.getSettings().setProperty("break_profiles", array.toString()));
        */
    }
}
