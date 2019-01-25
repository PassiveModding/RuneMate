package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.local.hud.HintArrow;
import com.runemate.game.api.hybrid.local.hud.HintArrows;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.script.framework.task.Task;

import java.util.Random;

import static com.passive.api.runescape.navigation.Travel.TravelTo;

public class AntiError extends Task {
    @Override
    public boolean validate() {
        GetContextItems.Context cont = GetContextItems.GetContextItems(true);

        if (cont.currentPlayer == null){
            return true;
        }

        if (cont.position().getX() == 0 && cont.position().getY() == 0){
            return true;
        }

        int rnd = new Random().nextInt(20);
        return rnd > 18;

    }

    @Override
    public void execute() {

        GetContextItems.Context cont = GetContextItems.GetContextItems();

        if (cont.currentPlayer != null){
            HintArrow hint = HintArrows.newQuery().results().nearest();
            if (hint != null){
                Coordinate pos = hint.getPosition();
                if (pos != null){
                    int y = pos.getY();
                    int x = pos.getX();
                    Coordinate hintPlace = new Coordinate(x,y);
                    if (hintPlace != null){
                        TravelTo(hintPlace);
                    }
                }
            }

            Area.Circular close = new Area.Circular(cont.position(), 2);
            Coordinate randSpace = close.getRandomCoordinate();
            if (randSpace.isReachable()){
                TravelTo(randSpace);
            }
        }

        return;
    }
}
