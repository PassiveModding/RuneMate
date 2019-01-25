package com.passive.api.ui.passive_ui.break_handler_pane;

import com.passive.api.bot.Passive_BOT;
import com.passive.api.bot.task_bot.break_handler.Break;
import com.passive.api.bot.task_bot.break_handler.BreakProfile;
import com.passive.api.ui.javafx.JavaFX;
import com.runemate.game.api.hybrid.util.Resources;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by SlashnHax on 16/01/2016.
 */
public class BreakHandlerPane extends TitledPane implements Initializable {
    @FXML
    private ComboBox<BreakProfile> profileSelector;
    @FXML
    private Button newButton;
    @FXML
    private Button renameButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField minGapTxt;
    @FXML
    private TextField maxGapTxt;
    @FXML
    private TextField avgGapTxt;
    @FXML
    private TextField minLengthTxt;
    @FXML
    private TextField maxLengthTxt;
    @FXML
    private TextField avgLengthTxt;
    @FXML
    private TableView<Break> breakTable;
    @FXML
    private TableColumn<Break, String> breakStartColumn;
    @FXML
    private TableColumn<Break, String> breakEndColumn;
    @FXML
    private TableColumn<Break, String> breakLengthColumn;
    @FXML
    private Button addBreakButton;
    @FXML
    private Button removeBreakButton;
    @FXML
    private Button clearBreaksButton;
    @FXML
    private Button generateBreaksButton;

    private Passive_BOT bot;

    public BreakHandlerPane(Passive_BOT bot) {
        this.bot = bot;
        JavaFX.loadFxml(bot, "com/passive/api/ui/passive_ui/break_handler_pane/break_handler_pane.fxml", this, this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Future<InputStream> stream = bot.getPlatform().invokeLater(() -> Resources.getAsStream("com/passive/api/ui/images/break-handler-icon-30x30.png"));
        profileSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.unbind(minGapTxt.textProperty(), maxGapTxt.textProperty(), avgGapTxt.textProperty(),
                        minLengthTxt.textProperty(), maxLengthTxt.textProperty(), avgLengthTxt.textProperty());
            }
            if (newValue != null) {
                newValue.bind(minGapTxt.textProperty(), maxGapTxt.textProperty(), avgGapTxt.textProperty(),
                        minLengthTxt.textProperty(), maxLengthTxt.textProperty(), avgLengthTxt.textProperty());
                breakTable.setItems(newValue.getBreaks());
            }
        });

        profileSelector.setItems(bot.getBreakHandler().getProfiles());
        profileSelector.getSelectionModel().select(bot.getBreakHandler().getSelectedProfile());
        profileSelector.valueProperty().bindBidirectional(bot.getBreakHandler().getSelectedProfileProperty());
        newButton.setOnAction(e -> {
            TextInputDialog input = new TextInputDialog();
            input.setContentText("Enter profile name:");
            String profileName = input.showAndWait().orElse(null);
            if (profileName != null) {
                bot.getBreakHandler().getProfiles().add(new BreakProfile(profileName));
            }
        });
        renameButton.setOnAction(e -> {
            BreakProfile profile = profileSelector.getValue();
            if (profile != null) {
                TextInputDialog input = new TextInputDialog();
                input.setContentText("Enter profile name:");
                String profileName = input.getResult();
                profile.setName(profileName);
                int index = profileSelector.getItems().indexOf(profile);
                profileSelector.getItems().remove(profile);
                profileSelector.getItems().add(index, profile);
            }
        });
        deleteButton.setOnAction(e -> bot.getBreakHandler().removeProfile(profileSelector.getValue()));

        breakStartColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        breakStartColumn.setCellValueFactory(param -> param.getValue().getStartTimeString());

        breakLengthColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        breakLengthColumn.setCellValueFactory(param -> param.getValue().getLengthString());

        breakEndColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        breakEndColumn.setCellValueFactory(param -> param.getValue().getEndTimeString());

        addBreakButton.setOnAction(event -> bot.getBreakHandler().getSelectedProfile().getBreaks().add(new Break(-1, 0)));
        removeBreakButton.setOnAction(event -> bot.getBreakHandler().getSelectedProfile().getBreaks().remove(breakTable.getSelectionModel().getSelectedItem()));
        clearBreaksButton.setOnAction(event -> bot.getBreakHandler().getSelectedProfile().getBreaks().clear());
        generateBreaksButton.setOnAction(event -> bot.getBreakHandler().getSelectedProfile().generateBreaks(30));

        try {
            setGraphic(new ImageView(new Image(stream.get())));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
