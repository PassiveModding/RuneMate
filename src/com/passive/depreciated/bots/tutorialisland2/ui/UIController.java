package com.runemate.passive.bots.tutorialisland2.ui;

import com.runemate.passive.bots.tutorialisland2.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class UIController implements Initializable {
    private Main bot;
    public UIController (Main bot){
        this.bot = bot;
    }
    @FXML
    public Label RUNTIME_LBL;
    @FXML
    public Label BOT_STATE;
    @FXML
    public Button START_BTN;
    @FXML
    public TextField NAME_BOX;
    @FXML
    public CheckBox LOGOUT_CHECK;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BOT_STATE.textProperty().bind(bot.store.state);
        RUNTIME_LBL.textProperty().bind(bot.store.runtimeLabel);
        START_BTN.setOnAction(START_BTN_CLICK());
        LOGOUT_CHECK.setOnAction(LOGOUT_CHECK_CLICK());
        NAME_BOX.textProperty().bind(bot.store.nameText);
        NAME_BOX.setOnKeyTyped(NAME_BOX_TYPED_CHECK());
    }
    public EventHandler<KeyEvent> NAME_BOX_TYPED_CHECK() {
        return event -> {
            try {
                if (NAME_BOX.getText().length() > 16){
                    NAME_BOX.setText(NAME_BOX.getText().substring(0, 16));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }


    public EventHandler<ActionEvent> LOGOUT_CHECK_CLICK() {
        return event -> {
            try {
                bot.store.logoutOnFinish = LOGOUT_CHECK.isSelected();
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }
    public EventHandler<ActionEvent> START_BTN_CLICK() {
        return event -> {
            try {
                    bot.store.run = true;
                    bot.store.counter.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }
}
