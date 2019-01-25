package com.runemate.passive.bots.dicing.ui;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.passive.bots.dicing.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class PassiveDicerUIController implements Initializable {
    private PassiveDicer bot;

    public static class DiceResult {
        private final SimpleStringProperty name;
        private final SimpleStringProperty index;
        private final SimpleStringProperty bet;
        private final SimpleStringProperty pay;
        private final SimpleStringProperty bal;

        public DiceResult(String NAME, int BET, int PAY, int BALANCE){
            this.bal = new SimpleStringProperty(""+BALANCE);
            this.pay = new SimpleStringProperty(""+PAY);
            this.bet = new SimpleStringProperty(""+BET);
            this.name = new SimpleStringProperty(NAME);
            this.index = new SimpleStringProperty(""+historyValues.size());
        }

        public String getName() {
            return name.get();
        }

        public void setName(String inName) {
            name.set(inName);
        }

        public String getIndex() {
            return index.get();
        }

        public void setIndex(String inIndex) {
            index.set(inIndex);
        }

        public String getBet() {
            return bet.get();
        }

        public void setBet(String inBet) {
            bet.set(inBet);
        }

        public String getPay() {
            return pay.get();
        }

        public void setPay(String inPay) {
            pay.set(inPay);
        }

        public String getBal() {
            return bal.get();
        }

        public void setBal(String inBal) {
            bal.set(inBal);
        }

    }

    public static final ObservableList<DiceResult> historyValues =
            FXCollections.observableArrayList();


    @FXML
    private Button REFRESH;

    @FXML
    private TableView<DiceResult> history;

    @FXML
    private TableColumn<DiceResult, String> indexCol;

    @FXML
    private TableColumn<DiceResult, String> nameCol;

    @FXML
    private TableColumn<DiceResult, String> payCol;

    @FXML
    private TableColumn<DiceResult, String> balCol;

    @FXML
    private TableColumn<DiceResult, String> tradeCol;

    public PassiveDicerUIController (PassiveDicer bot){
        this.bot = bot;
    }

    @FXML
    private TextField FAKE_BALANCE_BOX;

    @FXML
    private CheckBox FAKE_BALANCE_BTN;

    @FXML
    private TextField MIN_VAL_BOX;

    @FXML
    private TextField MAX_VAL_BOX;

    @FXML
    private Button SET_VAL_BTN;

    @FXML
    private CheckBox RIG_COMBO;

    @FXML
    private CheckBox WIN_COMBO;

    @FXML
    private CheckBox LOSS_COMBO;

    @FXML
    private Label MULTIPLIER_LBL;

    @FXML
    private TextField MULTIPLIER_BOX;

    @FXML
    private Label MIN_LABEL;

    @FXML
    private Label MAX_LABEL;

    @FXML
    private Label RIG_LABEL;

    @FXML
    private Label F_WIN_LABEL;

    @FXML
    private Label F_LOSS_LABEL;

    //Anti Lure
    @FXML
    private TextField ANTI_LURE_DIST;

    @FXML
    private Button GET_ORIGIN_BTN;

    @FXML
    private CheckBox LURE_ENABLE;

    @FXML
    private Button LURE_SET;

    @FXML
    private Label LURE_TILES_LABEL;

    @FXML
    private Label LOCATION_LABEL;

    @FXML
    private Label LURE_ENABLED_LABEL;

    @FXML
    private Label ORIGIN_LBL_PRE;
    private Coordinate ORIGIN_CO_PRE = new Coordinate(0,0);

    @FXML
    private Button C_ORIGIN_BTN;

    @FXML
    private TextField C_ORIGIN_X;
    @FXML
    private TextField C_ORIGIN_Y;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SET_VAL_BTN.setOnAction(SET_VAL_CLICK());
        LURE_SET.setOnAction(SET_LURE_CLICK());
        GET_ORIGIN_BTN.setOnAction(GET_ORIGIN_CLICK());
        C_ORIGIN_BTN.setOnAction(C_ORIGIN_CLICK());

        indexCol = new TableColumn("Index");
        indexCol.setCellValueFactory(
                new PropertyValueFactory<DiceResult,String>("index")
        );
        nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<DiceResult,String>("name")
        );
        tradeCol = new TableColumn("Bet");
        tradeCol.setCellValueFactory(
                new PropertyValueFactory<DiceResult,String>("bet")
        );
        payCol = new TableColumn("Payout");
        payCol.setCellValueFactory(
                new PropertyValueFactory<DiceResult,String>("pay")
        );
        balCol = new TableColumn("Total Balance");
        balCol.setCellValueFactory(
                new PropertyValueFactory<DiceResult,String>("bal")
        );

        history.setItems(historyValues);
        history.getColumns().addAll(indexCol, nameCol, tradeCol, payCol, balCol);
    }

    public int getIntFromString(String input){
        try {
            int res = Integer.parseInt(input);
            return res;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public EventHandler<ActionEvent> C_ORIGIN_CLICK() {
        return event -> {
            try {
                int C_X = getIntFromString(C_ORIGIN_X.getText());
                int C_Y = getIntFromString(C_ORIGIN_Y.getText());

                if (C_X != -1 && C_Y != -1){
                    Coordinate pos = new Coordinate(C_X, C_Y);
                    if (pos != null){
                        ORIGIN_CO_PRE = pos;
                        ORIGIN_LBL_PRE.setText("(" + pos.getX() + "," + pos.getY() + ")");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }

    public EventHandler<ActionEvent> GET_ORIGIN_CLICK() {
        return event -> {
            try {
                Coordinate pos = DelayStart.currentPos;
                if (pos != null){
                    ORIGIN_CO_PRE = pos;
                    ORIGIN_LBL_PRE.setText("(" + pos.getX() + "," + pos.getY() + ")");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }

    public EventHandler<ActionEvent> SET_LURE_CLICK() {
        return event -> {
            try {

                int TILES = getIntFromString(ANTI_LURE_DIST.getText());
                if (TILES != -1){
                    if (TILES > 1){
                        LURE_TILES_LABEL.setText(""+TILES);
                        LureData.Tiles = TILES;
                    }
                }

                LureData.Enabled = LURE_ENABLE.isSelected();
                LURE_ENABLED_LABEL.setText(""+LURE_ENABLE.isSelected());

                LureData.origin = ORIGIN_CO_PRE;
                LOCATION_LABEL.setText("(" + ORIGIN_CO_PRE.getX() + "," + ORIGIN_CO_PRE.getY() + ")");

            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }

    public EventHandler<ActionEvent> SET_VAL_CLICK() {
        return event -> {
            try {

                int MULT = getIntFromString(MULTIPLIER_BOX.getText());
                int MIN = getIntFromString(MIN_VAL_BOX.getText());
                int MAX = getIntFromString(MAX_VAL_BOX.getText());
                if (MIN != -1){
                    if (MIN < SessionData.MaxWin && MIN >= 0){
                        SessionData.MinWin = MIN;
                    }
                }

                if (MAX != -1){
                    if (MAX > SessionData.MinWin && MAX <= 100){
                        SessionData.MaxWin = MAX;
                    }
                }

                if (MULT != -1){
                    if (MULT > 1){
                        SessionData.Multiplier = MULT;
                    }
                }



                if (RIG_COMBO.isSelected()){
                    WIN_COMBO.setSelected(false);
                    LOSS_COMBO.setSelected(false);
                } else if (WIN_COMBO.isSelected()){
                    RIG_COMBO.setSelected(false);
                    LOSS_COMBO.setSelected(false);
                } else if (LOSS_COMBO.isSelected()){
                    RIG_COMBO.setSelected(false);
                    WIN_COMBO.setSelected(false);
                }

                SessionData.FalsifiedOdds = RIG_COMBO.isSelected();
                SessionData.ForceWin = WIN_COMBO.isSelected();
                SessionData.ForceLoss = LOSS_COMBO.isSelected();

                MULTIPLIER_LBL.setText(""+SessionData.Multiplier);
                MIN_LABEL.setText(""+SessionData.MinWin);
                MAX_LABEL.setText(""+SessionData.MaxWin);
                RIG_LABEL.setText(""+SessionData.FalsifiedOdds);
                F_WIN_LABEL.setText(""+SessionData.ForceWin);
                F_LOSS_LABEL.setText(""+SessionData.ForceLoss);

                SessionData.usefakedBalance = FAKE_BALANCE_BTN.isSelected();
                if (FAKE_BALANCE_BTN.isSelected()){
                    int balanceFake = getIntFromString(FAKE_BALANCE_BOX.getText());
                    if (balanceFake > 1){
                        SessionData.fakeMoney = balanceFake;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
