package com.runemate.passive.bots.herblore.ui;

import com.runemate.passive.bots.herblore.Finish;
import com.runemate.passive.bots.herblore.Herb;
import com.runemate.passive.bots.herblore.HerbAction;
import com.runemate.passive.bots.herblore.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UIController implements Initializable {
    private Main bot;
    public UIController (Main bot){
        this.bot = bot;
    }
    @FXML
    public Label GRIMY_PROFIT;
    @FXML
    public Label CLEAN_PROFIT;
    @FXML
    public Label RUNTIME_LBL;
    @FXML
    public Label BOT_STATE;
    @FXML
    public Label GP_RATE_COUNT;
    @FXML
    public Label PROFIT_COUNT;
    @FXML
    public Label POTS_MADE_COUNT;
    @FXML
    public Label RSP_LBL;
    @FXML
    public Label HERB_ACTION_LBL_CURRENT;
    @FXML
    public Label HERB_TYPE_LBL_CURRENT;
    @FXML
    public Label FINISH_POT_LBL;
    @FXML
    public Label FINISHED_PROFIT;
    @FXML
    public ChoiceBox FINISH_POT;
    @FXML
    public ChoiceBox POT_TYPE;
    @FXML
    public ChoiceBox HERB_ACTION;
    @FXML
    public ChoiceBox FINISH_ACTION;
    @FXML
    public Button START_BTN;
    @FXML
    public CheckBox COLLECTION_BOX;
    @FXML
    public Label FIN_POTS_MADE_LBL;
    @FXML
    public Label FINISHED_PRICE;
    @FXML
    public CheckBox HERB_CHANGE;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BOT_STATE.textProperty().bind(bot.store.state);
        GP_RATE_COUNT.textProperty().bind(bot.store.gpRateCount);
        POTS_MADE_COUNT.textProperty().bind(bot.store.potCount);
        PROFIT_COUNT.textProperty().bind(bot.store.profitCount);
        GRIMY_PROFIT.textProperty().bind(bot.store.grimyProfitGP);
        CLEAN_PROFIT.textProperty().bind(bot.store.cleanProfitGP);
        RUNTIME_LBL.textProperty().bind(bot.store.runtimeLabel);
        FINISHED_PROFIT.textProperty().bind(bot.store.finishedProfit);
        RSP_LBL.textProperty().bind(bot.store.recommendedSalePrice);
        HERB_TYPE_LBL_CURRENT.textProperty().bind(bot.store.currentHerbDisplayName);
        FIN_POTS_MADE_LBL.textProperty().bind(bot.store.finPotCount);
        FINISHED_PRICE.textProperty().bind(bot.store.finPrice);
        START_BTN.setOnAction(START_BTN_CLICK());
        //CLEAN_BTN.setOnAction(CLEAN_BTN_CLICK());
        HERB_CHANGE.setOnAction(HERB_CHANGE_CLICK());
        FINISH_POT_LBL.textProperty().bind(bot.store.finalPotName);
        List<String> newGuamArray = Herb.GUAM.potionDisplayNames();
        newGuamArray.add("None");
        FINISH_POT.getItems().setAll(newGuamArray);
        POT_TYPE.setOnAction(POT_SELECTED());
        FINISH_ACTION.getItems().setAll(Finish.LOGOUT_ON_FINISH.message, Finish.PAUSE_ON_FINISH.message, Finish.IDLE_ON_FINISH.message);
        POT_TYPE.getItems().setAll(Herb.GUAM.displayName(), Herb.MARRENTILL.displayName(), Herb.TARROMIN.displayName(),
                Herb.HARRALANDER.displayName(), Herb.RANARR_WEED.displayName(), Herb.TOADFLAX.displayName(),
                Herb.IRIT_LEAF.displayName(), Herb.AVANTOE.displayName(), Herb.KWUARM.displayName(), Herb.SNAPDRAGON.displayName(),
                Herb.CADANTINE.displayName(), Herb.LANTADYME.displayName(), Herb.DWARF_WEED.displayName(), Herb.TORSTOL.displayName());
        HERB_ACTION.getItems().setAll(HerbAction.GRIMY_ONLY.displayName, HerbAction.GRIMY_AND_CLEAN.displayName, HerbAction.CLEAN_ONLY.displayName);
    }


    public EventHandler<ActionEvent> POT_SELECTED() {
        return event -> {
            try {
                Object pot_typeValue = POT_TYPE.getValue();
                if (pot_typeValue != null) {
                    String comboValue = pot_typeValue.toString();
                    Herb info = Herb.getInfo(comboValue);

                    if (info != null) {
                        List<String> newPotArray = info.potionDisplayNames();
                        newPotArray.add("None");
                        FINISH_POT.getItems().setAll(newPotArray);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }

    public EventHandler<ActionEvent> HERB_CHANGE_CLICK() {
        return event -> {
            try {
                bot.store.changeHerbs = HERB_CHANGE.isSelected();
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }

    public EventHandler<ActionEvent> START_BTN_CLICK() {
        return event -> {
            try {
                Object pot_typeValue = POT_TYPE.getValue();
                Object herb_typeValue = HERB_ACTION.getValue();
                Object finish_typeValue = FINISH_ACTION.getValue();
                if (finish_typeValue != null) {
                    String finValue = finish_typeValue.toString();
                    Finish info = Finish.getInfo(finValue);
                    if (info != null) {
                        bot.store.FinishAction = info;
                    }
                }

                if (pot_typeValue != null){
                    String comboValue = pot_typeValue.toString();
                    Herb info = Herb.getInfo(comboValue);

                    if (info != null){
                        info.setPrices();

                        if (herb_typeValue != null) {
                            String herbActionValue = herb_typeValue.toString();
                            HerbAction herbAction = HerbAction.getInfo(herbActionValue);
                            if (herbAction != null) {
                                bot.store.herbAction = herbAction;
                                HERB_ACTION_LBL_CURRENT.setText(herbActionValue);
                            }
                        }

                        Object pot_finalValue = FINISH_POT.getValue();
                        if (pot_finalValue != null) {
                            String potValue = pot_finalValue.toString();
                            if (potValue != null) {
                                Herb.Potion[] pots = info.potions;

                                for (Herb.Potion pot : pots) {
                                    if (pot.displayName().equalsIgnoreCase(potValue)) {
                                        bot.store.potionToMake = pot;
                                    }
                                }

                                if (bot.store.potionToMake != null){
                                    bot.store.potionToMake.setPrices();
                                }
                            }
                        }

                        bot.store.currentHerbDisplayName.setValue(comboValue);
                        //HERB_TYPE_LBL_CURRENT.setText(comboValue);
                        bot.store.herbInfo = info;
                        bot.store.run = true;
                        bot.store.counter.start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }
}
