package com.passive.api.runescape.trackers.player;

import com.passive.api.bot.Passive_BOT;
import com.passive.api.runescape.LocalPlayer;
import com.passive.api.runescape.trackers.profit.ItemEventListener;
import com.passive.api.runescape.trackers.profit.ProfitTracker;
import com.passive.api.runescape.trackers.skills.SkillEventListener;
import com.passive.api.runescape.trackers.skills.SkillTracker;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.core.LoopingThread;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;

import java.util.concurrent.TimeUnit;

public class PlayerTracker implements SkillEventListener, ItemEventListener {
    private long lastAnimationTime = 0;
    private long lastMovementTime = 0;
    private long lastExpChangeTime = 0;
    private long lastInventoryChangeTime = 0;
    private long lastTargettedTime = 0;
    private long lastGuageTime = 0;
    private SimpleIntegerProperty animation = new SimpleIntegerProperty(-1);
    private SimpleBooleanProperty moving = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty targetted = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty hpGuage = new SimpleBooleanProperty(false);
    private LoopingThread thread;

    private Passive_BOT bot;

    public PlayerTracker(Passive_BOT bot) {
        this.bot = bot;
        animation.addListener(((observable, oldValue, newValue) -> lastAnimationTime = bot.getRuntime()));
        moving.addListener(((observable, oldValue, newValue) -> lastMovementTime = bot.getRuntime()));
        targetted.addListener((observable, oldValue, newValue) -> lastTargettedTime = bot.getRuntime());
        hpGuage.addListener((observable, oldValue, newValue) -> lastGuageTime = bot.getRuntime());
        thread = new LoopingThread(() -> {
            Player player = Players.getLocal();
            animation.set(LocalPlayer.getAnimation());
            moving.set(LocalPlayer.isMoving());
            targetted.set(LocalPlayer.isTargeted());
            hpGuage.set(player != null && player.getHealthGauge() != null);
        }, 300);
        thread.start();
    }

    public boolean hasBeenIdleFor(long amount, TimeUnit unit) {
        long time = bot.getRuntime() - unit.toMillis(amount);
        return animation.get() == -1 && lastAnimationTime < time && !moving.get() && lastMovementTime < time;
    }

    public boolean hasNoGuageUpFor(long amount, TimeUnit unit) {
        long time = bot.getRuntime() - unit.toMillis(amount);
        return !hpGuage.get() && lastGuageTime < time;
    }

    public boolean hasHadNoTargetFor(long amount, TimeUnit unit) {
        long time = bot.getRuntime() - unit.toMillis(amount);
        return !targetted.get() && lastTargettedTime < time;
    }

    public void addAnimationChangeListener(ChangeListener<? super Number> listener) {
        animation.addListener(listener);
    }

    public int getAnimationId() {
        return animation.get();
    }

    public boolean isMoving() {
        return moving.get();
    }

    public void setLastAnimationTime(long time) {
        lastAnimationTime = time;
    }

    @Override
    public void inventoryModified(int id, ProfitTracker.ItemInfo info) {
        lastInventoryChangeTime = bot.getRuntime();
    }

    @Override
    public void update(int id, ProfitTracker.ItemInfo info) {

    }

    @Override
    public void itemInfoAdded(int id, ProfitTracker.ItemInfo info) {

    }

    @Override
    public void itemInfoRemoved(int id, ProfitTracker.ItemInfo info) {

    }

    @Override
    public void onLevelUp(Skill skill, SkillTracker.SkillInfo info) {

    }

    @Override
    public void onExperienceGained(Skill skill, SkillTracker.SkillInfo info) {
        lastExpChangeTime = bot.getRuntime();
    }

    @Override
    public void update(Skill skill, SkillTracker.SkillInfo info) {

    }

    @Override
    public void skillInfoAdded(Skill skill, SkillTracker.SkillInfo info) {

    }

    @Override
    public void skillInfoRemoved(Skill skill, SkillTracker.SkillInfo info) {

    }
}
