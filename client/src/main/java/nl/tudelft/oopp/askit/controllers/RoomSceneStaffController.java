package nl.tudelft.oopp.askit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import nl.tudelft.oopp.askit.communicationlogic.RoomLogic;
import nl.tudelft.oopp.askit.communicationlogic.SpeedLogic;
import nl.tudelft.oopp.askit.controllers.abstractclasses.RoomController;

public class RoomSceneStaffController extends RoomController {

    @FXML
    private MenuItem closeRoomItem;
    @FXML
    private MenuItem answerModeItem;
    @FXML
    private Slider slider;
    @FXML
    private ToggleButton speedButton;
    @FXML
    private ToggleGroup slowMode;

    private boolean answerMode;

    /**
     * Closes the current room through the server.
     * To prevent new questions from being asked.
     * Method called through JavaFX onAction attribute.
     */
    public void closeRoomButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close room?");
        alert.setContentText("Students cannot ask new questions in the room anymore.");

        ButtonType btnYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(btnNo, btnYes);

        alert.showAndWait().ifPresent(type -> {
            if (type == btnYes) {
                RoomLogic.closeRoom(super.getRoomId(),super.getUser().getRoleId());
                updateRoomStatus();
            }
        });
    }

    /**
     * Sets answer mode when the item is selected.
     */
    public void setAnswerMode() {
        if (answerModeItem.getText().equals("Enable 'DoubleClick to answer'")) {
            this.answerMode = true;
            answerModeItem.setText("Disable 'DoubleClick to answer'");
        } else {
            this.answerMode = false;
            answerModeItem.setText("Enable 'DoubleClick to answer'");
        }
        updateAll();
    }

    /**
     * Gets the status of the room and updates the UI accordingly.
     */
    public void updateRoomStatus() {
        super.updateRoomStatus();
        boolean isOpen = super.getRoom().isOpen();
        slider.setVisible(isOpen);
        speedButton.setDisable(!isOpen);
        speedButton.setVisible(isOpen);
        closeRoomItem.setDisable(!isOpen);
    }

    /**
     * check if answering mode is enabled.
     * @return boolean value for mode
     */
    public boolean getAnswerMode() {
        return answerMode;
    }

    /**
     * Either enable or disable slow mode depending on checkbox value.
     * Called in both cases through JavaFX onAction attribute of checkbox.
     */
    public void setSlowMode() {

        RadioMenuItem selectedToggle = (RadioMenuItem) slowMode.getSelectedToggle();
        String selected = selectedToggle.getText();
        int slowModeSeconds = 0;

        switch (selected) {
            case "30 seconds":
                slowModeSeconds = 30;
                break;
            case "1 minute":
                slowModeSeconds = 60;
                break;
            case "5 minutes":
                slowModeSeconds = 300;
                break;
            default:
                slowModeSeconds = 0;
                break;
        }

        RoomLogic.setSlowMode(super.getRoom().getId().toString(),
                slowModeSeconds, super.getUser().getRoleId());
    }

    protected void updateRoomSpeed() {
        int currentRoomSpeed = SpeedLogic.getSpeed(super.getRoomId(), super.getUser().getRoleId());
        slider.setValue(currentRoomSpeed);
        if (currentRoomSpeed==2){
            slider.setVisible(false);
        }
    }

    @Override
    public void updateAll() {
        super.updateAll();
        updateRoomSpeed();
    }

    @Override
    public void toggleSlider() {
        slider.setVisible(!slider.isVisible());
    }
}
