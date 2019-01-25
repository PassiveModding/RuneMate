package com.passive.api.ui.passive_ui.skill_pane.tabs;

import com.passive.api.bot.Passive_BOT;
import com.passive.api.runescape.trackers.skills.SkillTracker;
import com.passive.api.ui.javafx.JavaFX;
import com.passive.api.util.Formatting;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TitledPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Rodrick Jones on 6/03/2016.
 */
public class SkillTab extends TitledPane implements Initializable {
    @FXML
    private Label currentLevelLabel;
    @FXML
    private Label levelsGainedLabel;
    @FXML
    private Label timeToLevelLabel;
    @FXML
    private Label expGainedLabel;
    @FXML
    private Label expPerHourLabel;
    @FXML
    private Label percentToNextLevelLabel;
    @FXML
    private ProgressBar titleProgressBar;
    @FXML
    private Label titleLabel;

    private Passive_BOT bot;
    private SkillTracker.SkillInfo info;

    public SkillTab(Passive_BOT bot, SkillTracker.SkillInfo info) {
        this.bot = bot;
        this.info = info;
        JavaFX.loadFxml(bot, "com/passive/api/ui/passive_ui/skill_pane/tabs/skill_tab.fxml", this, this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentLevelLabel.setText(String.valueOf(info.getLevelProperty().intValue()));
        levelsGainedLabel.setText(String.valueOf(info.getLevelsGained()));
        timeToLevelLabel.setText(info.getTimeToLevelProperty().get());
        expGainedLabel.setText(Formatting.format(info.getExperienceGained()));
        expPerHourLabel.setText(Formatting.format(info.getExpPerHourProperty().get()));
        percentToNextLevelLabel.setText(Formatting.twoDecPl(info.getPercentToLevel().get() * 100));
        updateTitleLabelText();
        titleProgressBar.progressProperty().set(info.getPercentToLevel().doubleValue());

        info.getLevelProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> currentLevelLabel.setText(newValue.toString()))));
        info.getLevelsGainedProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> levelsGainedLabel.setText(newValue.toString()))));
        info.getTimeToLevelProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> timeToLevelLabel.setText(newValue))));
        info.getExpGainedProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> expGainedLabel.setText(Formatting.format(newValue.intValue())))));
        info.getExpPerHourProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> expPerHourLabel.setText(Formatting.format(newValue.intValue())))));
        info.getPercentToLevel().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> percentToNextLevelLabel.setText(Formatting.twoDecPl(newValue.doubleValue() * 100)))));
        info.getLevelProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(this::updateTitleLabelText)));
        info.getExpPerHourProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(this::updateTitleLabelText)));
        info.getPercentToLevel().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> titleProgressBar.progressProperty().set(newValue.doubleValue()))));

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        titleProgressBar.prefWidthProperty().bind(bot.getUi().getContentScrollPane().widthProperty().subtract(70));
        titleLabel.prefWidthProperty().bind(titleProgressBar.prefWidthProperty());
    }

    public void updateTitleLabelText() {
        titleLabel.setText(info.getSkillName() + " | Lvl " + info.getLevelProperty().intValue() + " | " + Formatting.format(info.getExpPerHourProperty().intValue()) + " exp/hr");
    }

    public SkillTracker.SkillInfo getInfo() {
        return info;
    }
}
