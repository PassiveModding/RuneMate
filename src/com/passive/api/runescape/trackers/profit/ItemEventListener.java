package com.passive.api.runescape.trackers.profit;

/**
 * Created by SlashnHax on 22/12/2015.
 */
public interface ItemEventListener {
    void inventoryModified(int id, ProfitTracker.ItemInfo info);

    void update(int id, ProfitTracker.ItemInfo info);

    void itemInfoAdded(int id, ProfitTracker.ItemInfo info);

    void itemInfoRemoved(int id, ProfitTracker.ItemInfo info);
}
