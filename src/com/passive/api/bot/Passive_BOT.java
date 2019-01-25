package com.passive.api.bot;

import com.passive.api.bot.task_bot.anti_pattern.AntiPatternHandler;
import com.passive.api.bot.task_bot.break_handler.BreakHandler;
import com.passive.api.bot.task_bot.event_handler.EventHandler;
import com.passive.api.runescape.LocalPlayer;
import com.passive.api.runescape.cache.NameCache;
import com.passive.api.runescape.playersense.CustomPlayerSense;
import com.passive.api.runescape.trackers.player.PlayerTracker;
import com.passive.api.runescape.trackers.profit.PriceLookup;
import com.passive.api.runescape.trackers.profit.ProfitTracker;
import com.passive.api.runescape.trackers.skills.SkillTracker;
import com.passive.api.ui.overlay.Overlay;
import com.passive.api.ui.passive_ui.PassiveUI;
import com.passive.api.util.Lazy;
import com.passive.api.util.Logger;
import com.passive.api.util.MethodTimer;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.osrs.local.hud.interfaces.OptionsTab;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.AbstractBot;
import com.runemate.game.api.script.framework.core.LoopingThread;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TitledPane;
import javafx.util.Pair;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Passive_BOT extends AbstractBot implements EmbeddableUI  {
    protected StopWatch runtime;
    protected String botOverviewPage;
    protected Logger logger;
    protected String status = "";
    protected PriceLookup priceLookup;
    protected ProfitTracker profitTracker;
    protected EventHandler eventHandler;
    protected SkillTracker skillTracker;
    protected Pair<Integer, TimeUnit> idleStopTime;
    protected PlayerTracker playerTracker;
    protected AntiPatternHandler antiPatternHandler;
    protected BreakHandler breakHandler;
    protected NameCache nameCache;
    protected MethodTimer methodTimer = new MethodTimer();
    protected int loopDelayMin = 600;
    protected int loopDelayMax = 600;
    protected Overlay overlay;
    protected List<TitledPane> additionalNodes = new ArrayList<>();
    protected String processId;
    protected Boolean loggedIn;
    protected OptionsTab.CanvasMode canvasMode;

    public boolean isLoggedIn(){
        if (loggedIn != null){
            return loggedIn;
        }

        return false;
    }

    public OptionsTab.CanvasMode getCanvasMode(){
        if (isLoggedIn()){
            if (canvasMode != null){
                return canvasMode;
            } else {
                return OptionsTab.CanvasMode.FIXED;
            }
        } else {
            return OptionsTab.CanvasMode.FIXED;
        }
    }

    public String getProcessId() {
        if (processId == null) {
            return Environment.getRuneScapeProcessId();
        } else {
            return processId;
        }
    }

    public Passive_BOT(){
        this.runtime = new StopWatch();
        setEmbeddableUI(this);
        currentMousePosition = Mouse.getPosition();
        currentClientBounds = Environment.getFrameBounds();
    }

    public void debug(String message){
        String out = "[DEBUG]  " + message;
        logger().log(out);
    }

    public boolean onStart() {
        return true;
    }

    @Override
    public final void onStart(String... args){
        CustomPlayerSense.initializeKeys();

        processId = Environment.getRuneScapeProcessId();
        botOverviewPage = "https://www.runemate.com/community/resources/" + getMetaData().getId();
        this.logger = new Logger();
        playerTracker = new PlayerTracker(this);
        priceLookup = new PriceLookup();
        nameCache = new NameCache();
        breakHandler = new BreakHandler();
        profitTracker = new ProfitTracker(this);
        skillTracker = new SkillTracker(this);
        eventHandler = new EventHandler();
        antiPatternHandler = new AntiPatternHandler();
        idleStopTime = new Pair<>(Random.nextInt(200, 350), TimeUnit.SECONDS);

        setLoopDelay(150);
        getEventDispatcher().addListener(profitTracker);
        getEventDispatcher().addListener(skillTracker);
        skillTracker.addListener(playerTracker);
        profitTracker.addListener(playerTracker);
        new LoopingThread(() -> {
            if (profitTracker != null) {
                profitTracker.updateAll();
            }
            if (skillTracker != null) {
                skillTracker.updateAll();
            }
        }, 1000).start();
        new LoopingThread(() -> {
            currentMousePosition = Mouse.getPosition();
            currentClientBounds = Environment.getFrameBounds();
            loggedIn = RuneScape.isLoggedIn();
            canvasMode = OptionsTab.getCanvasMode();
        }, 50).start();
        Platform.runLater(() -> {
            botInterfaceProperty().get().createPanes();
            botInterfaceProperty().get().startRuntimeUpdater();
        });
        getEventDispatcher().addListener((ChatboxListener) event -> {
            if (Chatbox.Message.Type.SERVER.equals(event.getType()) && "Oh dear, you are dead!".equals(event.getMessage())) {
                setStatus("Player died, stopping bot.");
                for (int i = 0; i < 5 && RuneScape.isLoggedIn(); i++) {
                    RuneScape.logout();
                }
                stop("Player Died");
            }
        });
        GameEvents.Universal.UNEXPECTED_ITEM_HANDLER.disable();
        if (!onStart()) {
            setStatus("ERROR: onStart() returned false. Stopping bot");
            stop("onStart() returned false");
        } else {
            setStatus("Starting " + getMetaData().getName());
            runtime.start();
        }
    }

    @Override
    public void onPause() {
        runtime.stop();
    }

    @Override
    public void onResume() {
        runtime.start();
    }

    public void setLoopDelay(int min, int max) {
        loopDelayMin = min;
        loopDelayMax = max;
    }

    public Point currentMousePosition;
    public Rectangle currentClientBounds;

    public NameCache getNameCache() {
        return nameCache;
    }

    public SkillTracker getSkillTracker() {
        return skillTracker;
    }

    public ProfitTracker getProfitTracker() {
        return profitTracker;
    }

    public PlayerTracker getPlayerTracker() {
        return playerTracker;
    }

    public EventHandler getEventHandler() { return eventHandler; }

    public BreakHandler getBreakHandler() {
        return breakHandler;
    }

    public void disableOverlay(){
        if (overlay != null){
            overlay.getFrame().stopUpdateThread();
            try {
                overlay.getFrame().stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                overlay.getFrame().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            overlay = null;
        }
    }

    public void enableOverlay(){
        if (overlay == null){
            overlay = new Overlay(this);
        }
    }

    public void toggleoverlay() {
         if (overlay == null){
             enableOverlay();
         } else {
             disableOverlay();
         }
    }

    public void removeSkillTracker() {
        getEventDispatcher().removeListener(skillTracker);
        skillTracker.clearReceivers();
        skillTracker = null;
    }

    public void removeProfitTracker() {
        getEventDispatcher().removeListener(profitTracker);
        profitTracker.clearReceivers();
        profitTracker = null;
    }

    public void removeBreakHandler() {
        breakHandler = null;
    }

    public PriceLookup getPriceLookup() {
        return priceLookup;
    }

    public void setLoopDelay(int delay) {
        setLoopDelay(delay, delay);
    }

    public boolean isPremium() {
        return getMetaData().getHourlyPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    public void addAdditionalNode(TitledPane node){
        additionalNodes.add(node);
    }
    public List<TitledPane> getAdditionalNodes(){
        return additionalNodes;
    }

    public void run() {
        while (!State.STOPPED.equals(getState())){
            switch (getState()){
                case UNSTARTED:
                    throw new IllegalArgumentException();
                case RUNNING:
                    if (!RuneScape.isLoggedIn()) {
                        profitTracker.delayListening(5000);
                    }

                    if (getGameEventController().validate()){
                        getGameEventController().execute();
                    } else if (breakHandler != null && breakHandler.validate()) {
                        breakHandler.execute();
                    } else if (eventHandler != null && eventHandler.validate()) {
                        eventHandler.execute();
                    } else if (antiPatternHandler != null && antiPatternHandler.validate()) {
                        antiPatternHandler.execute();
                    } else {
                        onLoop();
                    }
                    Execution.delay(loopDelayMin, loopDelayMax);
                    break;
                case PAUSED:
                    Execution.delay(500);
                    break;
                case STOPPED:
                    break;
            }
        }

        disableOverlay();

        setStatus("Time ran: " + getFormattedRuntime());
        if (profitTracker != null && profitTracker.getTotalInfo().getProfit() != 0) {
            profitTracker.printGains();
        }
        if (skillTracker != null && skillTracker.getTotalInfo().getExperienceGained() != 0) {
            skillTracker.printGains();
        }
        getEventDispatcher().removeListener(profitTracker);
        getEventDispatcher().removeListener(skillTracker);
        setStatus("Thanks for using " + getMetaData().getName());
    }

    public abstract void onLoop();

    public long getRuntime() {
        return runtime.getRuntime();
    }
    public boolean canBreak() {
        return LocalPlayer.canLogout();
    }
    public String getFormattedRuntime() {
        return runtime.getRuntimeAsString();
    }
    public String getBotOverviewPage() { return botOverviewPage; }
    public static Passive_BOT getBot() {
        return (Passive_BOT) Environment.getBot();
    }
    public MethodTimer getMethodTimer() {
        return methodTimer;
    }

    protected PassiveUI Ui;
    protected Lazy<PassiveUI> botInterfaceProperty = new Lazy<>(() -> Ui = new PassiveUI(this));
    @Override
    public ObjectProperty<PassiveUI> botInterfaceProperty() {
        return botInterfaceProperty;
    }
    public PassiveUI getUi() {
        return botInterfaceProperty().get();
    }

    public Logger logger() {
        return logger;
    }

    public void setStatus(String newStatus) {
        if (newStatus != null) {
            this.status = newStatus;
            getUi().setStatus(status);
            String out = "[STATUS] " + status;
            logger.log(out);
        }
    }
}
