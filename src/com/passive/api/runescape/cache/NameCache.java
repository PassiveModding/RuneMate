package com.passive.api.runescape.cache;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Item;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.definitions.GameObjectDefinition;
import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition;
import com.runemate.game.api.hybrid.entities.definitions.NpcDefinition;

public class NameCache {

    public String getName(Object obj) {
        if (obj instanceof Item) {
            return getItemName((Item) obj);
        } else if (obj instanceof Npc) {
            return getNpcName((Npc) obj);
        } else if (obj instanceof GameObject) {
            return getGameObjectName((GameObject) obj);
        }
        return null;
    }

    public String getItemName(int id) {
        ItemDefinition def = ItemDefinition.get(id);
        return def == null ? null : def.getName();
    }

    public String getItemName(Item item) {
        return getItemName(item.getId());
    }

    public String getNpcName(int id) {
        NpcDefinition def = NpcDefinition.get(id);
        return def == null ? null : def.getName();
    }

    public String getNpcName(Npc npc) {
        return npc.getName();
    }

    public String getGameObjectName(int id) {
        GameObjectDefinition def = GameObjectDefinition.get(id);
        return def == null ? null : def.getName();
    }

    public String getGameObjectName(GameObject object) {
        return getGameObjectName(object.getId());
    }
}
