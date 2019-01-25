package com.runemate.passive.bots.dicing;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.location.navigation.web.WebPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

public class LureData extends Task {
    public static int Tiles = 0;
    public static Coordinate origin = new Coordinate(0,0);
    public static boolean Enabled = false;


    @Override
    public boolean validate() {
        if (Enabled) {

            if (origin.getX() == 0 && origin.getY() == 0){
                return false;
            }

            Player local = Players.getLocal();
            if (local != null) {
                Coordinate pos = local.getPosition();
                if (pos != null) {
                    if (Tiles > 0){
                        if (pos.distanceTo(origin) > Tiles){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void execute() {
        WebPath path = Traversal.getDefaultWeb().getPathBuilder().buildTo(origin);

        if (path != null){
            path.step();
        } else {
            BresenhamPath p = BresenhamPath.buildTo(origin);
            if (p != null) {
                p.step();
            }
        }
    }
}
