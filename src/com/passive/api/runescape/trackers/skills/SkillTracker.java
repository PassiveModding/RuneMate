package com.passive.api.runescape.trackers.skills;

import com.passive.api.bot.Passive_BOT;
import com.passive.api.util.Formatting;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.Skills;
import com.runemate.game.api.script.framework.listeners.SkillListener;
import com.runemate.game.api.script.framework.listeners.events.SkillEvent;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Tracker responsible for the listening of SkillEvents and distribution of relevant information.
 */
public class SkillTracker implements SkillListener {

    private final Map<Skill, SkillInfo> info = new HashMap<>();
    private final Collection<SkillEventListener> receivers = new CopyOnWriteArrayList<>();
    private final SkillInfo totalInfo = new SkillInfo(null) {
        @Override
        public void updateExpHr(long runtime) {
            if (getLevelProperty().get() < 0) {
                setLevel(Arrays.stream(Skill.values()).mapToInt(Skill::getBaseLevel).sum());
            }
            getExpPerHourProperty().set((int) (getExperienceGained() / (double) runtime * 3600000));
            SkillInfo nextLevel = info.values().stream().min(Comparator.comparing(info -> info.getTimeToLevelProperty().get())).orElse(null);
            if (nextLevel != null) {
                getTimeToLevelProperty().set(nextLevel.getTimeToLevelProperty().get());
                getPercentToLevel().set(nextLevel.getPercentToLevel().get());
            }
        }

        @Override
        public void calculatePercentToLevel() {
        }
    };
    private Collection<Skill> whitelist = new HashSet<>();
    private long delayTime = 0;
    private Passive_BOT bot;

    public SkillTracker(Passive_BOT bot) {
        this.bot = bot;
    }

    public SkillInfo getTotalInfo() {
        return totalInfo;
    }

    @Override
    public void onLevelUp(SkillEvent e) {
        if (bot.getRuntime() > delayTime) {
            synchronized (info) {
                if (whitelist.isEmpty() || whitelist.contains(e.getSkill())) {
                    info.compute(e.getSkill(), (skill, info) -> {
                        if (info == null)
                            info = new SkillInfo(skill);
                        info.setLevel(e.getCurrent());
                        info.addLevelsGained(e.getChange());
                        return info;
                    });
                    totalInfo.addLevelsGained(e.getChange());
                    totalInfo.setLevel(totalInfo.getLevelProperty().get() + e.getChange());
                    receivers.forEach(l -> l.onLevelUp(e.getSkill(), info.get(e.getSkill())));
                }
            }
        }
    }

    @Override
    public void onExperienceGained(SkillEvent e) {
        if (bot.getRuntime() > delayTime) {
            synchronized (info) {
                if (whitelist.isEmpty() || whitelist.contains(e.getSkill())) {
                    info.compute(e.getSkill(), (skill, info) -> {
                        if (info == null) {
                            final SkillInfo newInfo = new SkillInfo(skill);
                            info = newInfo;
                            receivers.forEach(l -> l.skillInfoAdded(skill, newInfo));
                            if (this.info.size() == 1) {
                                receivers.forEach(l -> l.skillInfoAdded(null, totalInfo));
                            }
                        }
                        info.addExperienceGained(e.getChange());
                        info.calculatePercentToLevel();
                        if (info.getExperienceGained() == 0) {
                            final SkillInfo infoCopy = info;
                            receivers.forEach(l -> l.skillInfoRemoved(skill, infoCopy));
                            info = null;
                            if (this.info.size() == 1) {
                                receivers.forEach(l -> l.skillInfoRemoved(null, totalInfo));
                            }
                        }
                        return info;
                    });
                    totalInfo.addExperienceGained(e.getChange());
                    receivers.forEach(l -> l.onExperienceGained(e.getSkill(), info.get(e.getSkill())));
                }
            }
        }
    }

    public void delayListening(long millis) {
        delayTime = bot.getRuntime() + millis;
    }

    public Collection<Skill> getChangedSkills() {
        return info.keySet();
    }

    public void updateAll() {
        synchronized (info) {
            info.entrySet().forEach(m -> update(m.getKey(), m.getValue()));
            update(null, totalInfo);
        }
    }

    public void update(Skill skill, SkillInfo info) {
        info.updateExpHr(bot.getRuntime());
        receivers.forEach(l -> l.update(skill, info));
    }

    public void printGains() {
        bot.logger().log("Skills:");
        bot.logger().log("  Total - Levels: " + totalInfo.getLevelsGained() + " Experience: " + Formatting.format(totalInfo.getExperienceGained()));
        synchronized (info) {
            info.entrySet().stream()
                    .filter(e -> e.getValue().getExperienceGained() != 0)
                    .forEach(e -> bot.logger().log("  " + e.getValue().getSkillName()
                            + " Levels: " + e.getValue().getLevelsGained()
                            + ", Experience: " + Formatting.format(e.getValue().getExperienceGained())));
        }
    }

    public void addToWhitelist(Skill s) {
        whitelist.add(s);
    }

    public void addListener(SkillEventListener l) {
        receivers.add(l);
        synchronized (info) {
            if (info.size() > 1) {
                l.skillInfoAdded(null, totalInfo);
            }
            for (Map.Entry<Skill, SkillInfo> e : info.entrySet()) {
                l.skillInfoAdded(e.getKey(), e.getValue());
            }
        }
    }

    public void removeReceiver(SkillEventListener r) {
        receivers.remove(r);
    }

    public void clearReceivers() {
        receivers.clear();
    }

    public boolean isEmpty() {
        return info.isEmpty();
    }

    public class SkillInfo {
        private final SimpleStringProperty name = new SimpleStringProperty();
        private final SimpleIntegerProperty expGained = new SimpleIntegerProperty(0);
        private final SimpleIntegerProperty expPerHour = new SimpleIntegerProperty(0);
        private final SimpleIntegerProperty level = new SimpleIntegerProperty(0);
        private final SimpleIntegerProperty levelsGained = new SimpleIntegerProperty(0);
        private final SimpleStringProperty timeToLevel = new SimpleStringProperty("00:00:00");
        private final SimpleDoubleProperty percentToLevel = new SimpleDoubleProperty(0);

        private Skill skill;
        private int expToNextLvl;

        public SkillInfo(Skill skill) {
            this.skill = skill;
            if (skill == null) {
                name.set("Total");
                setLevel(Arrays.stream(Skill.values()).mapToInt(Skill::getBaseLevel).sum());
            } else {
                name.set(skill.name().substring(0, 1) + skill.name().substring(1).toLowerCase());
                setLevel(skill.getBaseLevel());
            }
        }

        //region Property Getters
        public SimpleStringProperty getNameProperty() {
            return name;
        }

        public SimpleIntegerProperty getExpGainedProperty() {
            return expGained;
        }

        public SimpleIntegerProperty getExpPerHourProperty() {
            return expPerHour;
        }

        public SimpleIntegerProperty getLevelProperty() {
            return level;
        }

        public SimpleIntegerProperty getLevelsGainedProperty() {
            return levelsGained;
        }

        public SimpleStringProperty getTimeToLevelProperty() {
            return timeToLevel;
        }

        public SimpleDoubleProperty getPercentToLevel() {
            return percentToLevel;
        }

        //endregion

        public Skill getSkill() {
            return skill;
        }

        public String getSkillName() {
            return name.get();
        }

        public void setLevel(int level) {
            this.level.set(level);
        }

        public void addLevelsGained(int gain) {
            levelsGained.set(levelsGained.get() + gain);
        }

        public void addExperienceGained(int gain) {
            expGained.set(expGained.get() + gain);
        }

        public int getExperienceGained() {
            return expGained.get();
        }

        public int getLevelsGained() {
            return levelsGained.get();
        }

        public void updateExpHr(long runtime) {
            double expMilli = getExperienceGained() / (double) runtime;
            expPerHour.set((int) (expMilli * 3600000));
            timeToLevel.set(Formatting.formatAsTime((long) (expToNextLvl / expMilli)));
        }

        public void calculatePercentToLevel() {
            expToNextLvl = skill.getExperienceToNextLevel();
            percentToLevel.set((skill.getExperience() - Skills.getExperienceAt(skill, level.get())) / ((double) Skills.getExperienceAt(skill, level.get() + 1) - Skills.getExperienceAt(skill, level.get())));
        }
    }

}
