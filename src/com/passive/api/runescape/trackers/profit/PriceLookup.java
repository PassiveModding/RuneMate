package com.passive.api.runescape.trackers.profit;

import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition;
import com.runemate.game.api.hybrid.net.GrandExchange;
import com.runemate.game.api.script.framework.core.LoopingThread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PriceLookup {
    private HashMap<Integer, Item> cache = new HashMap<>();
    private LoopingThread updater;
    private HashMap<Integer, Collection<LookupListener>> listenerMap = new HashMap<>();

    public PriceLookup() {
        cache = new HashMap<>();
        cache.put(995, new Item(995, false, "Coins", 1));
        updater = new LoopingThread(() -> cache.values().stream().filter(Item::isTradable).forEach(i -> {
            GrandExchange.Item item = GrandExchange.lookup(i.getId());
            if (item != null) {
                i.setPrice(item.getPrice());
            }
        }), (int) TimeUnit.MINUTES.toMillis(15));
        updater.start();
    }

    public void addLookupListener(int id, LookupListener listener) {
        listenerMap.compute(id, (i, listeners) -> {
            if (listeners == null)
                listeners = new ArrayList<>();
            listeners.add(listener);
            return listeners;
        });
    }

    public Item lookup(int id) {
        Item res;
        if (cache.containsKey(id)) {
            res = cache.get(id);
        } else {
            ItemDefinition def = ItemDefinition.get(id);
            if (def == null)
                res = Item.NULL_DEF;
            else if (!def.isTradeable())
                res = new Item(id, false, def.getName(), 0);
            else {
                GrandExchange.Item item = GrandExchange.lookup(id);
                if (item == null) {
                    return Item.ERROR;
                } else {
                    return new Item(id, true, item.getName(), item.getPrice());
                }
            }
            cache.put(id, res);
            if (listenerMap.containsKey(id)) {
                int price = res.getPrice();
                listenerMap.get(id).forEach(l -> l.onLookup(price));
            }
        }
        return res;
    }

    public int getPrice(int id) {
        return lookup(id).getPrice();
    }

    public interface LookupListener {
        void onLookup(int price);
    }

    static class Item {
        public final static Item TEMP = new Item(-1, true, "Loading...", 0);
        public final static Item ERROR = new Item(-1, true, "Error", 0);
        public final static Item NULL_DEF = new Item(-1, false, "Null def", 0);
        private int id;
        private boolean tradable;
        private String name;
        private int price;

        public Item(int id, boolean tradable, String name, int price) {
            this.id = id;
            this.tradable = tradable;
            this.name = name;
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public boolean isTradable() {
            return tradable;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return name + " (" + id + ") $" + price;
        }
    }
}
