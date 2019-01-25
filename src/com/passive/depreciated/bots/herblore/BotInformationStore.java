package com.runemate.passive.bots.herblore;

import com.runemate.game.api.hybrid.util.StopWatch;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.TimeUnit;

public class BotInformationStore {
    public final StringProperty currentHerbDisplayName = new SimpleStringProperty();
    public final StringProperty finalPotName = new SimpleStringProperty();
    public final StringProperty runtimeLabel = new SimpleStringProperty();
    public final StringProperty state = new SimpleStringProperty();
    public final StringProperty gpRateCount = new SimpleStringProperty();
    public final StringProperty potCount = new SimpleStringProperty();
    public StopWatch counter = new StopWatch();
    public final StringProperty profitCount = new SimpleStringProperty();
    public final StringProperty grimyProfitGP = new SimpleStringProperty();
    public final StringProperty recommendedSalePrice = new SimpleStringProperty();
    public final StringProperty cleanProfitGP = new SimpleStringProperty();


    public final StringProperty finishedProfit = new SimpleStringProperty();
    public final StringProperty finPotCount = new SimpleStringProperty();
    public final StringProperty finPrice = new SimpleStringProperty();
    public boolean changeHerbs = false;
    public Finish FinishAction = Finish.LOGOUT_ON_FINISH;
    public HerbAction herbAction = HerbAction.GRIMY_AND_CLEAN;
    public Herb herbInfo = Herb.GUAM;
    public Herb.Potion potionToMake = null;

    public boolean run = false;
    //public boolean logoutOnFinish = false;
    public int potCountValue = 0;
    public int profitCountValue = 0;
    public int finPotCountValue = 0;

    public void Update_Counts(){
        double GP_Rate = (double) profitCountValue * 60 * 60 * 1000 / counter.getRuntime(TimeUnit.MILLISECONDS);
        int GP_RateInt = (int) GP_Rate;
        Platform.runLater(() -> {
            runtimeLabel.setValue(counter.getRuntimeAsString());
            potCount.setValue(Integer.toString(potCountValue));
            profitCount.setValue(Integer.toString(profitCountValue) + "gp");
            gpRateCount.setValue(Integer.toString(GP_RateInt) + "gp/h");

            if (potionToMake != null) {
                finalPotName.setValue(potionToMake.displayName());
                finishedProfit.setValue(Integer.toString((potionToMake.potionPrice - potionToMake.secondaryPrice) - herbInfo.unfinishedPrice) + "((Pot-Secondary)-unfinished");
                finPotCount.setValue(Integer.toString(finPotCountValue));
                finPrice.setValue(Integer.toString(potionToMake.potionPrice));
            }

            if (herbInfo != null) {
                cleanProfitGP.setValue(Integer.toString(herbInfo.unfinishedPrice - herbInfo.cleanPrice) + " (" + Integer.toString(herbInfo.cleanPrice) + "gp)");
                grimyProfitGP.setValue(Integer.toString(herbInfo.unfinishedPrice - herbInfo.grimyPrice) + " (" + Integer.toString(herbInfo.grimyPrice) + "gp)");
                recommendedSalePrice.setValue(Integer.toString(herbInfo.unfinishedPrice));
            }
        });
    }
}
