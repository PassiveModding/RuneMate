package com.runemate.passive.bots.passiveherblore;

import com.runemate.passive.bots.passiveherblore.framework.BotAction;
import com.runemate.passive.bots.passiveherblore.framework.Finish;
import com.runemate.passive.bots.passiveherblore.framework.Herb;
import com.runemate.passive.bots.passiveherblore.framework.HerbAction;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Info {
    //Display Strings
    public final StringProperty currentHerbDisplayName = new SimpleStringProperty();
    public final StringProperty finalPotName = new SimpleStringProperty();
    public final StringProperty finishActionDisplay = new SimpleStringProperty();
    public final StringProperty herbActionDisplay = new SimpleStringProperty();
    public final StringProperty mainTaskDisplay = new SimpleStringProperty();
    public final StringProperty sellOnFinishDisplay = new SimpleStringProperty();

    public boolean changeHerbs = false;
    public Finish FinishAction = Finish.LOGOUT_ON_FINISH;
    public HerbAction herbAction = HerbAction.GRIMY_AND_CLEAN;
    public BotAction mainTask = BotAction.MAKE_UNFINISHED_POTIONS;
    public Herb herbInfo = Herb.GUAM;
    public Herb.Potion potionToMake = null;

    public boolean sellOnFinish = false;
    public int sellOnFinishValue = -1;

    public boolean run = false;

    public void updateLabels(){
        Platform.runLater(() -> {
            finishActionDisplay.setValue(FinishAction.message);
            if (mainTask != null){
                mainTaskDisplay.setValue(mainTask.getMessage());
            }

            if (herbInfo != null){
                if (mainTask == BotAction.CLEAN_HERBS){
                    currentHerbDisplayName.setValue(herbInfo.getCleanDisplayName());
                } else {
                    currentHerbDisplayName.setValue(herbInfo.getUnfinishedDisplayName());
                }
            } else {
                currentHerbDisplayName.setValue("N/A");
            }

            if (herbAction != null){
                herbActionDisplay.setValue(herbAction.displayName);
            }

            if (potionToMake != null) {
                finalPotName.setValue(potionToMake.displayName());
            }

            if (sellOnFinish){
                sellOnFinishDisplay.setValue("Sell for " + sellOnFinishValue);
            } else {
                sellOnFinishDisplay.setValue("N/A");
            }
        });
    }
}
