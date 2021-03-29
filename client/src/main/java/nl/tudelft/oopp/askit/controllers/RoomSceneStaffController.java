package nl.tudelft.oopp.askit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import nl.tudelft.oopp.askit.communicationlogic.RoomLogic;
import nl.tudelft.oopp.askit.communicationlogic.SpeedLogic;
import nl.tudelft.oopp.askit.controllers.abstractclasses.RoomController;

public class RoomSceneStaffController extends RoomController {

    @FXML
    private MenuItem closeRoomItem;
    @FXML
    private MenuItem answerModeItem;
    @FXML
    private MenuItem slowModeItem;
    @FXML
    private Slider slider;
    @FXML
    private ToggleButton speedButton;
    @FXML
    private CheckBox slowModeToggle;

    private boolean answerMode;

    /**
     * Closes the current room through the server.
     * To prevent new questions from being asked.
     * Method called through JavaFX onAction attribute.
     */
    public void closeRoomButtonClicked() {
        RoomLogic.closeRoom(super.getRoom().getId().toString());
        updateRoomStatus();
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

        boolean slowMode = slowModeItem.getText().equals("Enable Slow Mode");
        System.out.println("Slow mode = " + slowMode);


        // Slow mode is hard coded to a fixed question interval of 20 seconds
        int slowModeSeconds = slowMode ? 20 : 0;
        RoomLogic.setSlowMode(super.getRoom().getId().toString(), slowModeSeconds);

        String slowModeLabel = slowMode ? "Disable Slow Mode" : "Enable Slow Mode";
        slowModeItem.setText(slowModeLabel);

    }

    protected void updateRoomSpeed() {
        int currentRoomSpeed = SpeedLogic.getSpeed(super.getRoomId());
        slider.setValue(currentRoomSpeed);
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
