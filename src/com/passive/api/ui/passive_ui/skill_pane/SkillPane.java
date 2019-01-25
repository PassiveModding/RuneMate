package com.passive.api.ui.passive_ui.skill_pane;

import com.passive.api.bot.Passive_BOT;
import com.passive.api.runescape.trackers.skills.SkillEventListener;
import com.passive.api.runescape.trackers.skills.SkillTracker;
import com.passive.api.ui.javafx.JavaFX;
import com.passive.api.ui.passive_ui.skill_pane.tabs.SkillTab;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.util.Resources;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * Created by SlashnHax on 16/01/2016.
 */
public class SkillPane extends TitledPane implements Initializable, SkillEventListener {
    @FXML
    private VBox contentBox;

    private Passive_BOT bot;

    public SkillPane(Passive_BOT bot) {
        this.bot = bot;
        bot.getSkillTracker().addListener(this);
        JavaFX.loadFxml(bot, "com/passive/api/ui/passive_ui/skill_pane/skill_ui.fxml", this, this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setGraphic(new ImageView(new Image(bot.getPlatform().invokeLater(() -> Resources.getAsStream("com/passive/api/ui/images/skills-icon-30x30.png")).get())));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    //region SkillEventListener Methods
    @Override
    public void onLevelUp(Skill skill, SkillTracker.SkillInfo info) {
    }

    @Override
    public void onExperienceGained(Skill skill, SkillTracker.SkillInfo info) {
    }

    @Override
    public void update(Skill skill, SkillTracker.SkillInfo info) {
    }

    @Override
    public void skillInfoAdded(Skill skill, SkillTracker.SkillInfo info) {
        Platform.runLater(() -> {
            if (!bot.getUi().containsContent(this)) {
                bot.getUi().addContent(this);
            }
            SkillTab tab = new SkillTab(bot, info);
            if (skill == null) {
                contentBox.getChildren().add(0, tab);
            } else {
                contentBox.getChildren().add(tab);
                tab.setExpanded(false);
            }
        });
    }

    @Override
    public void skillInfoRemoved(Skill skill, SkillTracker.SkillInfo info) {
        if (bot.getUi().containsContent(this) && bot.getSkillTracker().isEmpty()) {
            bot.getUi().removeContent(this);
        }
        Platform.runLater(() -> contentBox.getChildren().removeIf(tab -> ((SkillTab) tab).getInfo() == info));
    }
    //endregion

}
