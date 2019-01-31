package com.passive.api.runescape.trackers.profit;
import com.passive.api.bot.Passive_BOT;
import com.passive.api.util.Formatting;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.util.calculations.CommonMath;
import com.runemate.game.api.script.framework.listeners.InventoryListener;
import com.runemate.game.api.script.framework.listeners.MoneyPouchListener;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import com.runemate.game.api.script.framework.listeners.events.MoneyPouchEvent;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ProfitTracker implements InventoryListener, MoneyPouchListener {

    private final Map<Integer, ItemInfo> info = new HashMap<>();
    private final ItemInfo totalInfo = new ItemInfo(-1) {

        @Override
        public void updateTimeBasedProperties(long runtime) {
            super.updateTimeBasedProperties(runtime);
            synchronized (info) {
                int profit = info.values().stream().filter(i -> i != totalInfo).mapToInt(ItemInfo::getProfit).sum();
                getProfitProperty().set(profit);
                getProfitHrProperty().set((int) CommonMath.rate(TimeUnit.HOURS, runtime, profit));
            }
        }
    };
    private final Collection<ItemEventListener> receivers = new ArrayList<>();
    private Passive_BOT bot;
    private long delayTime;

    public ProfitTracker(Passive_BOT bot) {
        this.bot = bot;
    }

    @Override
    public void onItemAdded(ItemEvent e) {
        if (bot.getRuntime() > delayTime && !Bank.isOpen()) {
            synchronized (info) {
                info.compute(e.getItem().getId(), (id, info) -> {
                    if (info == null) {
                        info = addInfo(id);
                    }
                    info.addAmount(e.getQuantityChange());
                    return info;
                });
                totalInfo.addAmount(e.getQuantityChange());
                receivers.forEach(r -> r.inventoryModified(e.getItem().getId(), info.get(e.getItem().getId())));
                Collection<Integer> coll = info.entrySet().stream()
                        .filter(entry -> entry.getValue().getAmount() == 0)
                        .map(Map.Entry::getKey).collect(Collectors.toList());
                coll.forEach(this::removeInfo);
            }
        }
    }

    @Override
    public void onItemRemoved(ItemEvent e) {
        if (bot.getRuntime() > delayTime && !Bank.isOpen()) {
            synchronized (info) {
                info.compute(e.getItem().getId(), (id, info) -> {
                    if (info == null) {
                        info = addInfo(id);
                    }
                    info.subtractAmount(e.getQuantityChange());
                    return info;
                });
                totalInfo.subtractAmount(e.getQuantityChange());
                receivers.forEach(r -> r.inventoryModified(e.getItem().getId(), info.get(e.getItem().getId())));
                Collection<Integer> coll = info.entrySet().stream()
                        .filter(entry -> entry.getValue().getAmount() == 0)
                        .map(Map.Entry::getKey).collect(Collectors.toList());
                coll.forEach(this::removeInfo);
            }
        }
    }

    @Override
    public void onContentsChanged(MoneyPouchEvent e) {
        if (bot.getRuntime() > delayTime && !Bank.isOpen()) {
            synchronized (info) {
                info.compute(995, (id, info) -> {
                    if (info == null) {
                        info = addInfo(id);
                    }
                    info.addAmount(e.getChange());
                    return info;
                });
                receivers.forEach(r -> r.inventoryModified(995, info.get(995)));
            }
        }
    }

    public ItemInfo addInfo(int id) {
        ItemInfo info = new ItemInfo(id);
        bot.getPriceLookup().addLookupListener(id, info);
        bot.getPriceLookup().addLookupListener(id, totalInfo);
        info.setName(bot.getNameCache().getItemName(id));
        info.setPrice(bot.getPriceLookup().getPrice(id));
        receivers.forEach(r -> r.itemInfoAdded(id, info));
        if (this.info.size() == 1) {
            receivers.forEach(r -> r.itemInfoAdded(-1, totalInfo));
            this.info.put(-1, totalInfo);
        }
        return info;
    }

    public void removeInfo(int id) {
        synchronized (info) {
            ItemInfo removed = info.remove(id);
            receivers.forEach(r -> r.itemInfoRemoved(id, removed));
            if (this.info.size() == 1) {
                receivers.forEach(r -> r.itemInfoRemoved(-1, totalInfo));
            }
        }
    }

    public void delayListening(long millis) {
        delayTime = bot.getRuntime() + millis;
    }

    public void addListener(ItemEventListener r) {
        receivers.add(r);
    }

    public void removeReceiver(ItemEventListener r) {
        receivers.remove(r);
    }

    public void clearReceivers() {
        receivers.clear();
    }

    public void updateAll() {
        synchronized (info) {
            info.values().forEach(this::update);
            totalInfo.updateTimeBasedProperties(bot.getRuntime());
        }
    }

    public void update(ItemInfo info) {
        info.updateTimeBasedProperties(bot.getRuntime());
    }

    public ItemInfo getTotalInfo() {
        return totalInfo;
    }

    public void printGains() {
        bot.logger().log("Profit:");
        updateAll();
        bot.logger().log("  Total - " + Formatting.format(totalInfo.getAmount()) + " items: " + Formatting.format(totalInfo.getProfit()) + " gp");
        synchronized (info) {
            info.entrySet().stream()
                    .filter(e -> e.getValue().getAmount() != 0)
                    .forEach(e -> bot.logger().log("  " + e.getValue().getName() + " " + Formatting.format(e.getValue().getAmount())
                            + " items, " + Formatting.format(e.getValue().getProfit()) + " gp"));
        }
    }

    public boolean isEmpty() {
        return info.isEmpty();
    }

    public static class ItemInfo implements PriceLookup.LookupListener {
        private final int id;
        private SimpleIntegerProperty price = new SimpleIntegerProperty();
        private SimpleStringProperty name = new SimpleStringProperty();
        private SimpleIntegerProperty amount = new SimpleIntegerProperty(0);
        private SimpleIntegerProperty amountHr = new SimpleIntegerProperty(0);
        private SimpleIntegerProperty profit = new SimpleIntegerProperty(0);
        private SimpleIntegerProperty profitHr = new SimpleIntegerProperty(0);

        public ItemInfo(int id) {
            this.id = id;
            if (id == -1) {
                setName("Total");
            } else {
                setName("LOADING (" + this.id + ")");
                profit.bind(price.multiply(amount));
                profitHr.bind(amountHr.multiply(price));
            }
        }

        //region Property Getters

        public SimpleIntegerProperty getPriceProperty() {
            return price;
        }

        public SimpleStringProperty getNameProperty() {
            return name;
        }

        public SimpleIntegerProperty getAmountProperty() {
            return amount;
        }

        public SimpleIntegerProperty getAmountHrProperty() {
            return amountHr;
        }

        public SimpleIntegerProperty getProfitProperty() {
            return profit;
        }

        public SimpleIntegerProperty getProfitHrProperty() {
            return profitHr;
        }
        //endregion

        public String getName() {
            return name.get();
        }

        public void setName(String newName) {
            name.set(newName);
        }

        public void addAmount(int change) {
            amount.set(amount.get() + change);
        }

        public void subtractAmount(int change) {
            amount.set(amount.get() - change);
        }

        public int getAmount() {
            return amount.get();
        }

        public void setPrice(int price) {
            this.price.set(price);
        }

        public int getProfit() {
            return profit.get();
        }

        public int getId() { return this.id; }

        public void updateTimeBasedProperties(long runtime) {
            amountHr.set((int) (amount.get() * 3600000d / runtime));
        }

        @Override
        public void onLookup(int price) {
            setPrice(price);
        }
    }
}
