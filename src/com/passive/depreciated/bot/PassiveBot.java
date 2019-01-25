package com.passive.api.bot;

import com.passive.api.runescape.LocalPlayer;
import com.passive.api.runescape.caching.NameCache;
import com.passive.api.runescape.net.PriceLookup;
import com.passive.depreciated.ui.passive_ui.PassiveUI;
import com.passive.depreciated.util.Lazy;
import com.passive.api.util.Logger;
import com.passive.depreciated.util.MethodTimer;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.data.ScriptMetaData;
import com.runemate.game.api.script.framework.AbstractBot;
import com.runemate.game.api.script.framework.core.LoopingThread;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public abstract class PassiveBot extends AbstractBot implements EmbeddableUI {
    protected String botOverviewPage;
    protected StopWatch runtime;
    protected EventHandler eventHandler;
    protected AntiPatternHandler antiPatternHandler;
    protected BreakHandler breakHandler;
    protected PlayerTracker playerTracker;
    protected ProfitTracker profitTracker;
    protected SkillTracker skillTracker;
    protected PriceLookup priceLookup;
    protected NameCache nameCache;
    protected String status = "";
    protected PassiveUI alphaUi;
    protected Lazy<PassiveUI> botInterfaceProperty = new Lazy<>(() -> alphaUi = new PassiveUI(this));
    protected Logger logger;
    protected MethodTimer methodTimer = new MethodTimer();
    protected int loopDelayMin = 600;
    protected int loopDelayMax = 600;
    protected Pair<Integer, TimeUnit> idleStopTime;
    protected BooleanProperty fitToHeight = new SimpleBooleanProperty(true);

    boolean bytecode;

    public PassiveBot() {
        this.runtime = new StopWatch();
        setEmbeddableUI(this);
    }

    public static boolean isLocalAndSdk() {
        AbstractBot bot;
        ScriptMetaData metaData;
        return Environment.isSDK() && (bot = Environment.getBot()) != null && (metaData = bot.getMetaData()) != null && metaData.isLocal();
    }

    public static PassiveBot getBot() {
        return (PassiveBot) Environment.getBot();
    }

    @Override
    public ObjectProperty<PassiveUI> botInterfaceProperty() {
        return botInterfaceProperty;
    }

    @Override
    public BooleanProperty fitToHeightProperty() {
        return fitToHeight;
    }

    public void debug(String message) {
        String out = "[DEBUG]  " + message;
        logger().log(out);
    }

    @Override
    public final void onStart(String... args) {
        if (Environment.isRS3()) {
            throw new UnsupportedOperationException();
        }
        botOverviewPage = "https://www.runemate.com/community/resources/" + getMetaData().getId();
        logger = new Logger();
        playerTracker = new PlayerTracker(this);
        breakHandler = new BreakHandler();
        eventHandler = new EventHandler();
        antiPatternHandler = new AntiPatternHandler();
        priceLookup = new PriceLookup();
        nameCache = new NameCache();
        idleStopTime = new Pair<>(Random.nextInt(200, 350), TimeUnit.SECONDS);
        setLoopDelay(150);
        getEventDispatcher().addListener(profitTracker = new ProfitTracker(this));
        getEventDispatcher().addListener(skillTracker = new SkillTracker(this));
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

    public void run() {
        while (!State.STOPPED.equals(getState())) {
            switch (getState()) {
                case UNSTARTED:
                    throw new IllegalArgumentException();
                case RUNNING:
                    if (!RuneScape.isLoggedIn()) {
                        //skillTracker.delayListening(5000);
                        profitTracker.delayListening(5000);
                    }
                    if (getGameEventController().validate()) {
                        getGameEventController().execute();
                    } else if (breakHandler != null && breakHandler.validate()) {
                        breakHandler.execute();
                    } else if (eventHandler != null && eventHandler.validate()) {
                        eventHandler.execute();
                    } else if (antiPatternHandler != null && antiPatternHandler.validate()) {
                        antiPatternHandler.execute();
                    } else if (playerTracker.hasBeenIdleFor(idleStopTime.getKey(), idleStopTime.getValue())) {
                        setStatus("Player has been idle for " + idleStopTime.getKey() + " " + idleStopTime.getValue() + ", stopping bot.");
                        stop("Player has been idle for " + idleStopTime.getKey() + " " + idleStopTime.getValue());
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
        /*logger().log("Method timings(ms):");
        logger().log("  Min  -  Avg  -  Max  - Samples");
        for (String key : methodTimer.getMethodNames()) {
            logger().log(key);
            logger().log(pad(String.valueOf(methodTimer.getMinTime(key)), 5) +
                    " - " + pad(String.valueOf(methodTimer.getAverageTime(key)), 5) +
                    " - " + pad(String.valueOf(methodTimer.getMaxTime(key)), 5) +
                    " - " + pad(String.valueOf(methodTimer.getSampleCount(key)), 5));
        }*/
        //logger().close();
    }

    private String pad(String string, int length) {
        while (string.length() < length) {
            string = " " + string;
        }
        return string;
    }

    public boolean onStart() {
        return true;
    }

    public abstract void onLoop();

    @Override
    public void onPause() {
        runtime.stop();
    }

    @Override
    public void onResume() {
        runtime.start();
    }

    @Override
    public void onStop() {
    }

    public void setLoopDelay(int min, int max) {
        loopDelayMin = min;
        loopDelayMax = max;
    }

    public void setLoopDelay(int delay) {
        setLoopDelay(delay, delay);
    }

    public boolean isPremium() {
        return getMetaData().getHourlyPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    public NameCache getNameCache() {
        return nameCache;
    }

    public StopWatch getRuntimeStopWatch() {
        return runtime;
    }

    public long getRuntime() {
        return runtime.getRuntime();
    }

    public String getFormattedRuntime() {
        return runtime.getRuntimeAsString();
    }

    public PlayerTracker getPlayerTracker() {
        return playerTracker;
    }

    public SkillTracker getSkillTracker() {
        return skillTracker;
    }

    public ProfitTracker getProfitTracker() {
        return profitTracker;
    }

    public BreakHandler getBreakHandler() {
        return breakHandler;
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

    public PassiveUI getUi() {
        return botInterfaceProperty().get();
    }

    public void setStatus(String newStatus) {
        if (newStatus != null) {
            this.status = newStatus;
            getUi().setStatus(status);
            String out = "[STATUS] " + status;
            logger.log(out);
        }
    }

    public PriceLookup getPriceLookup() {
        return priceLookup;
    }

    public boolean canBreak() {
        return LocalPlayer.canLogout();
    }

    public Logger logger() {
        return logger;
    }

    public MethodTimer getMethodTimer() {
        return methodTimer;
    }

}
