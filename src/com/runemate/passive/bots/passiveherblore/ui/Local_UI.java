package com.runemate.passive.bots.passiveherblore.ui;

import com.passive.api.ui.javafx.JavaFX;
import com.runemate.game.api.hybrid.net.GrandExchange;
import com.runemate.game.api.osrs.net.OSBuddyExchange;
import com.runemate.passive.bots.passiveherblore.Main;
import com.runemate.passive.bots.passiveherblore.framework.BotAction;
import com.runemate.passive.bots.passiveherblore.framework.Finish;
import com.runemate.passive.bots.passiveherblore.framework.Herb;
import com.runemate.passive.bots.passiveherblore.framework.HerbAction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Local_UI extends TitledPane implements Initializable {
    private Main bot;

    public ChoiceBox FINISH_POT;
    @FXML
    public ChoiceBox POT_TYPE;
    @FXML
    public ChoiceBox HERB_ACTION;
    @FXML
    public ChoiceBox FINISH_ACTION;
    @FXML
    public ChoiceBox MAIN_TASK_TYPE;

    @FXML
    public Button START_BTN;
    @FXML
    public CheckBox HERB_CHANGE;
    @FXML
    public Label HERB_ACTION_LBL_CURRENT;
    @FXML
    public Label HERB_TYPE_LBL_CURRENT;
    @FXML
    public Label FINISH_POT_LBL;
    @FXML
    public Label FINISH_ACTION_LBL;
    @FXML
    public Label MAIN_TASK_LBL;

    @FXML
    public TextField SALE_VALUE_FIELD;
    @FXML
    public CheckBox SELL_ON_FINISH;
    @FXML
    public Label SELL_ON_FINISH_LBL;

    public Local_UI(Main bot){
        this.bot = bot;
        JavaFX.loadFxml(bot, "com/runemate/passive/bots/passiveherblore/ui/local_ui.fxml", this, this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HERB_TYPE_LBL_CURRENT.textProperty().bind(bot.info.currentHerbDisplayName);
        FINISH_ACTION_LBL.textProperty().bind(bot.info.finishActionDisplay);
        HERB_ACTION_LBL_CURRENT.textProperty().bind(bot.info.herbActionDisplay);
        FINISH_POT_LBL.textProperty().bind(bot.info.finalPotName);
        MAIN_TASK_LBL.textProperty().bind(bot.info.mainTaskDisplay);
        SELL_ON_FINISH_LBL.textProperty().bind(bot.info.sellOnFinishDisplay);

        HERB_CHANGE.setOnAction(HERB_CHANGE_CLICK());
        START_BTN.setOnAction(START_BTN_CLICK());
        POT_TYPE.setOnAction(POT_SELECTED());
        MAIN_TASK_TYPE.setOnAction(MAIN_TASK_CHANGE());

        FINISH_POT_LBL.textProperty().bind(bot.info.finalPotName);
        List<String> newGuamArray = Herb.GUAM.potionDisplayNames();
        newGuamArray.add("None");
        FINISH_POT.getItems().setAll(newGuamArray);
        MAIN_TASK_TYPE.getItems().setAll(BotAction.CLEAN_HERBS.getMessage(), BotAction.MAKE_UNFINISHED_POTIONS.getMessage(), BotAction.MAKE_FINISHED_POTIONS.getMessage());
        FINISH_ACTION.getItems().setAll(Finish.LOGOUT_ON_FINISH.message, Finish.PAUSE_ON_FINISH.message, Finish.IDLE_ON_FINISH.message);
        POT_TYPE.getItems().setAll(Herb.GUAM.getUnfinishedDisplayName(), Herb.MARRENTILL.getUnfinishedDisplayName(), Herb.TARROMIN.getUnfinishedDisplayName(),
                Herb.HARRALANDER.getUnfinishedDisplayName(), Herb.RANARR_WEED.getUnfinishedDisplayName(), Herb.TOADFLAX.getUnfinishedDisplayName(),
                Herb.IRIT_LEAF.getUnfinishedDisplayName(), Herb.AVANTOE.getUnfinishedDisplayName(), Herb.KWUARM.getUnfinishedDisplayName(), Herb.SNAPDRAGON.getUnfinishedDisplayName(),
                Herb.CADANTINE.getUnfinishedDisplayName(), Herb.LANTADYME.getUnfinishedDisplayName(), Herb.DWARF_WEED.getUnfinishedDisplayName(), Herb.TORSTOL.getUnfinishedDisplayName());
        HERB_ACTION.getItems().setAll(HerbAction.GRIMY_ONLY.displayName, HerbAction.GRIMY_AND_CLEAN.displayName, HerbAction.CLEAN_ONLY.displayName);
    }


    public String getValueFromChoice(ChoiceBox box){
        Object choiceValue = box.getValue();
        if (choiceValue != null){
            String choiceString = choiceValue.toString();
            if (choiceString != null){
                return choiceString;
            }
        }

        return null;
    }

    public EventHandler<ActionEvent> MAIN_TASK_CHANGE() {
        return event -> {
            try {
                String comboValue = getValueFromChoice(MAIN_TASK_TYPE);
                if (comboValue != null) {
                    BotAction info = BotAction.getInfo(comboValue);

                    if (info != null) {
                        //Platform.runLater(() -> bot.info.mainTaskDisplay.setValue(info.getMessage()));
                        //bot.info.mainTask = info;

                        if (info == BotAction.CLEAN_HERBS){
                            FINISH_POT.setDisable(true);
                            HERB_ACTION.getItems().setAll(HerbAction.GRIMY_ONLY.displayName);
                            bot.info.herbAction = HerbAction.GRIMY_ONLY;
                            Platform.runLater(() -> bot.info.herbActionDisplay.setValue("Use Grimy Herbs Only"));
                            POT_TYPE.getItems().setAll(Herb.GUAM.getCleanDisplayName(), Herb.MARRENTILL.getCleanDisplayName(), Herb.TARROMIN.getCleanDisplayName(),
                                    Herb.HARRALANDER.getCleanDisplayName(), Herb.RANARR_WEED.getCleanDisplayName(), Herb.TOADFLAX.getCleanDisplayName(),
                                    Herb.IRIT_LEAF.getCleanDisplayName(), Herb.AVANTOE.getCleanDisplayName(), Herb.KWUARM.getCleanDisplayName(), Herb.SNAPDRAGON.getCleanDisplayName(),
                                    Herb.CADANTINE.getCleanDisplayName(), Herb.LANTADYME.getCleanDisplayName(), Herb.DWARF_WEED.getCleanDisplayName(), Herb.TORSTOL.getCleanDisplayName());
                            if (bot.info.herbInfo != null){
                                bot.info.currentHerbDisplayName.setValue(bot.info.herbInfo.getCleanDisplayName());
                            }
                        } else {
                            HERB_ACTION.getItems().setAll(HerbAction.GRIMY_ONLY.displayName, HerbAction.GRIMY_AND_CLEAN.displayName, HerbAction.CLEAN_ONLY.displayName);
                            POT_TYPE.getItems().setAll(Herb.GUAM.getUnfinishedDisplayName(), Herb.MARRENTILL.getUnfinishedDisplayName(), Herb.TARROMIN.getUnfinishedDisplayName(),
                                    Herb.HARRALANDER.getUnfinishedDisplayName(), Herb.RANARR_WEED.getUnfinishedDisplayName(), Herb.TOADFLAX.getUnfinishedDisplayName(),
                                    Herb.IRIT_LEAF.getUnfinishedDisplayName(), Herb.AVANTOE.getUnfinishedDisplayName(), Herb.KWUARM.getUnfinishedDisplayName(), Herb.SNAPDRAGON.getUnfinishedDisplayName(),
                                    Herb.CADANTINE.getUnfinishedDisplayName(), Herb.LANTADYME.getUnfinishedDisplayName(), Herb.DWARF_WEED.getUnfinishedDisplayName(), Herb.TORSTOL.getUnfinishedDisplayName());
                            if (bot.info.herbInfo != null){
                                bot.info.currentHerbDisplayName.setValue(bot.info.herbInfo.getUnfinishedDisplayName());
                            }

                            if (info == BotAction.MAKE_UNFINISHED_POTIONS){
                                FINISH_POT.setDisable(true);
                                bot.info.potionToMake = null;
                                Platform.runLater(() -> bot.info.finalPotName.setValue("None"));
                            } else if (info == BotAction.MAKE_FINISHED_POTIONS){
                                FINISH_POT.setDisable(false);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public EventHandler<ActionEvent> POT_SELECTED() {
        return event -> {
            try {
                String comboValue = getValueFromChoice(POT_TYPE);
                if (comboValue != null) {
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
                bot.info.changeHerbs = HERB_CHANGE.isSelected();
                if (bot.info.changeHerbs){
                    if (SELL_ON_FINISH.isSelected()){
                        SELL_ON_FINISH.setSelected(false);
                    }

                    if (bot.info.sellOnFinish){
                        bot.info.sellOnFinish = false;
                        bot.info.sellOnFinishValue = -1;
                        SELL_ON_FINISH.setSelected(false);
                        com.runemate.game.api.client.ClientUI.showAlert("Sell on finish cannot be used if changing herbs");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }

    public EventHandler<ActionEvent> START_BTN_CLICK() {
        return event -> {
            try {
                String finishTypeString = getValueFromChoice(FINISH_ACTION);
                if (finishTypeString != null) {
                    Finish info = Finish.getInfo(finishTypeString);
                    if (info != null) {
                        bot.info.FinishAction = info;
                    }
                }

                String potTypeString = getValueFromChoice(POT_TYPE);
                if (potTypeString != null){
                    Herb info = Herb.getInfo(potTypeString);

                    if (info != null){

                        String herbActionString = getValueFromChoice(HERB_ACTION);
                        if (herbActionString != null) {
                            HerbAction herbAction = HerbAction.getInfo(herbActionString);
                            if (herbAction != null) {
                                bot.info.herbAction = herbAction;
                                bot.info.herbActionDisplay.setValue(herbActionString);
                            }
                        }

                        Object pot_finalValue = FINISH_POT.getValue();
                        if (pot_finalValue != null) {
                            String potValue = pot_finalValue.toString();
                            if (potValue != null) {
                                Herb.Potion[] pots = info.potions;

                                boolean potionToMakeFound = false;
                                for (Herb.Potion pot : pots) {
                                    if (pot.displayName().equalsIgnoreCase(potValue)) {
                                        bot.info.potionToMake = pot;
                                        potionToMakeFound = true;
                                    }
                                }

                                if (!potionToMakeFound){
                                    bot.info.potionToMake = null;
                                }
                            }
                        }

                        bot.info.currentHerbDisplayName.setValue(potTypeString);
                        bot.info.herbInfo = info;

                        String mainTask = getValueFromChoice(MAIN_TASK_TYPE);
                        if (mainTask != null) {
                            BotAction botAction = BotAction.getInfo(mainTask);

                            if (info != null) {
                                bot.info.mainTaskDisplay.setValue(botAction.getMessage());
                                bot.info.mainTask = botAction;
                            }
                        }

                        bot.info.sellOnFinish = SELL_ON_FINISH.isSelected();
                        if (bot.info.sellOnFinish){
                            try {
                                String saleValueText = SALE_VALUE_FIELD.getText();
                                int saleValue = Integer.parseInt(saleValueText);
                                bot.info.sellOnFinishValue = saleValue;
                                bot.info.sellOnFinishDisplay.setValue("Sell for " + saleValue);

                            } catch (NumberFormatException nfe){
                                nfe.printStackTrace();
                            }
                        }

                        bot.info.run = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }
}
