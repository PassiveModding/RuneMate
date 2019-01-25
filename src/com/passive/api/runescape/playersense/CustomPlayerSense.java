package com.passive.api.runescape.playersense;

import com.runemate.game.api.hybrid.player_sense.PlayerSense;
import com.runemate.game.api.hybrid.util.calculations.Random;

import java.util.function.Supplier;

public class CustomPlayerSense {
    public enum Key {
        MAKE_ALL_INTERFACE_HOTKEY("passive_make_all_hotkey", () -> Random.nextBoolean());

        private final String name;
        private final Supplier supplier;

        Key(String name, Supplier supplier) {
            this.name = name;
            this.supplier = supplier;
        }

        public String getKey() {
            return name;
        }

        public Integer getAsInteger() {
            return PlayerSense.getAsInteger(name);
        }

        public Double getAsDouble() {
            return PlayerSense.getAsDouble(name);
        }

        public Long getAsLong() {
            return PlayerSense.getAsLong(name);
        }

        public Boolean getAsBoolean() {
            return PlayerSense.getAsBoolean(name);
        }
    }

    public static void initializeKeys() {
        for (Key key : Key.values()) {
            if (PlayerSense.get(key.name) == null) {
                PlayerSense.put(key.name, key.supplier.get());
            }
        }
    }
}
