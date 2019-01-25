package com.passive.api.runescape.npcs;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;

public class Npcs {
    public static void TalkTo(Npc npc) {
        if (npc != null) {
            if (npc.getPosition().isReachable()) {
                if (npc.isVisible()) {
                    npc.interact("Talk-to");
                } else {
                    Camera.turnTo(npc);
                }
            } else {
                return;
            }
        }
    }


}
