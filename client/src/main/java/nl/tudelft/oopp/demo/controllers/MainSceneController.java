package nl.tudelft.oopp.demo.controllers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.views.TimeSpinner;

/**
 * Controller for mainScene.fxml.
 */
public class MainSceneController {

    @FXML private TextField userText;
    @FXML private ListView<String> list;
    @FXML private Label tabTitle;
    @FXML private Button createButton;
    @FXML private Button joinButton;
    @FXML private Button scheduleButton;
    @FXML private Label tabIcon;
    @FXML private Label timeLabel;
    @FXML private TextField username;
    @FXML private ToggleButton createTab;
    @FXML private ToggleButton joinTab;
    @FXML private ToggleButton scheduleTab;
    @FXML private GridPane grid;
    @FXML private DatePicker datePicker;

    private Spinner<LocalTime> timeSpinner;

    /**
     * Use @FXML initialize() instead of constructor.
     * This method is called after linking the @FXML elements.
     * so only at this point can ui elements be addressed from code.
     */
    @FXML
    public void initialize() {

        // initialize timePicker
        this.timeSpinner = new TimeSpinner();
        grid.add(timeSpinner, 4, 4);
        timeSpinner.getStyleClass().add("spinner");
        timeSpinner.getStylesheets().add(getClass()
                .getResource("/mainSceneStyle.css").toExternalForm());
        timeSpinner.setPrefHeight(30);
        timeSpinner.setVisible(false);
        timeSpinner.setDisable(true);

        datePicker.setValue(LocalDate.now());

        joinTabClicked();
        joinTab.setSelected(true);
    }

    /**
     * Handles clicking the create button.
     */
    public void createButtonClicked() {
        List<String> links = ServerCommunication.createRoom(userText.getText(),
                ZonedDateTime.now());
        userText.clear();
        list.getItems().clear();
        if (links.size() == 1) {
            list.getItems().add("error " + links.get(0));
        } else {
            list.getItems().add("Click on link (individually) to copy"
                    + "\nstaff link: ");
            list.getItems().add(links.get(0));
            list.getItems().add("student link: ");
            list.getItems().add(links.get(1));
        }
    }

    /**
     * Copy the link to the clipboard when the list is clicked.
     */
    public void copyList() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String link = list.getSelectionModel().getSelectedItem();
        StringSelection linkCopy = new StringSelection(link);
        clipboard.setContents(linkCopy, null);
    }

    /**
     * Handles clicking the join button.
     */
    public void joinButtonClicked() {
        ServerCommunication.joinRoom(userText.getText());
        userText.clear();
    }

    /**
     * Handles clicking the schedule button.
     */
    public void scheduleButtonClicked() {

        LocalDate date = datePicker.getValue();
        LocalTime time = timeSpinner.getValue();
        LocalDateTime localTime = LocalDateTime.of(date, time);

        //convert to ZonedDateTime
        TimeZone tz = TimeZone.getDefault();
        ZonedDateTime openTime = localTime.atZone(ZoneId.of(tz.getID()));

        List<String> links = ServerCommunication.createRoom(userText.getText(), openTime);
        userText.clear();
        list.getItems().clear();
        if (links.size() == 1) {
            list.getItems().add("error " + links.get(0));
        } else {
            list.getItems().add("Click on link (individually) to copy"
                    + "\nstaff link: ");
            list.getItems().add(links.get(0));
            list.getItems().add("student link: ");
            list.getItems().add(links.get(1));
        }
    }

    /**
     * Handles switching Create Tab.
     */
    public void createTabClicked() {
        tabTitle.setText("Create Lecture Room");
        tabIcon.setText("+ ");
        userText.setPromptText("Enter name of the lecture...");
        createButton.toFront();
        list.setVisible(true);
        joinButton.setVisible(false);
        joinButton.setDisable(true);
        scheduleButton.setVisible(false);
        scheduleButton.setDisable(true);
        username.setVisible(false);
        username.setDisable(true);
        joinTab.setSelected(false);
        scheduleTab.setSelected(false);
        timeSpinner.setVisible(false);
        timeSpinner.setDisable(true);
        datePicker.toBack();
        datePicker.setVisible(false);
        datePicker.setDisable(true);
        timeLabel.setVisible(false);

    }

    /**
     * Handles switching Join Tab.
     */
    public void joinTabClicked() {
        tabTitle.setText("Join Lecture Room");
        tabIcon.setText("# ");
        userText.setPromptText("Enter link to join the lecture...");
        username.toFront();
        list.setVisible(false);
        joinButton.setVisible(true);
        joinButton.setDisable(false);
        scheduleButton.toBack();
        username.setVisible(true);
        username.setDisable(false);
        createTab.setSelected(false);
        scheduleTab.setSelected(false);
        timeSpinner.setVisible(false);
        timeSpinner.setDisable(true);
        datePicker.setVisible(false);
        datePicker.setDisable(true);
        timeLabel.setVisible(false);
    }

    /**
     * Handles switching Join Tab.
     */
    public void scheduleTabClicked() {
        tabTitle.setText("Schedule Lecture");
        tabIcon.setText("@ ");
        userText.setPromptText("Enter name of the lecture...");
        joinButton.toBack();
        scheduleButton.toFront();
        list.setVisible(true);
        joinButton.setVisible(false);
        joinButton.setDisable(true);
        createTab.setSelected(false);
        joinTab.setSelected(false);
        scheduleTab.setSelected(true);
        timeSpinner.setVisible(true);
        timeSpinner.setDisable(false);
        datePicker.setVisible(true);
        datePicker.setDisable(false);
        datePicker.toFront();
        timeLabel.setVisible(true);
        scheduleButton.setVisible(true);
        scheduleButton.setDisable(false);
    }
}
