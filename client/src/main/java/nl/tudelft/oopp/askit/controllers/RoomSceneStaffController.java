package nl.tudelft.oopp.askit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.MenuItem;
import nl.tudelft.oopp.askit.communicationlogic.RoomLogic;
import nl.tudelft.oopp.askit.communicationlogic.SpeedLogic;
import nl.tudelft.oopp.askit.controllers.abstractclasses.RoomController;

public class RoomSceneStaffController extends RoomController {

    @FXML
    private MenuItem closeRoomItem;
    @FXML
    private MenuItem modeItem;
    private Button closeRoomButton;
    @FXML
    private Slider slider;
    @FXML
    private ToggleButton speedButton;
    @FXML
    private CheckBox slowModeToggle;

    private boolean mode;

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
    public void setMode() {
        if (modeItem.getText().equals("Enable 'DoubleClick to answer'")) {
            this.mode = true;
            modeItem.setText("Disable 'DoubleClick to answer'");
        } else {
            this.mode = false;
            modeItem.setText("Enable 'DoubleClick to answer'");
        }
    }

    /**
     * Gets the status of the room and updates the UI accordingly.
     */
    public void updateRoomStatus() {
        super.updateRoomStatus();
        boolean isOpen = super.getRoom().isOpen();
        closeRoomButton.setDisable(!isOpen);
        slider.setVisible(isOpen);
        speedButton.setDisable(!isOpen);
        speedButton.setVisible(isOpen);
        boolean isOpen = RoomLogic.getRoomStatus(super.getRoomId());
        closeRoomItem.setDisable(!isOpen);
    }

    /**
     * check if answering mode is enabled.
     * @return boolean value for mode
     */
    public boolean getMode() {
        return mode;
    }

    /**
     * Either enable or disable slow mode depending on checkbox value.
     * Called in both cases through JavaFX onAction attribute of checkbox.
     */
    public void setSlowMode() {
        boolean slowMode = slowModeToggle.isSelected();
        System.out.println("Slow mode = " + slowMode);

        // Slow mode is hard coded to a fixed question interval of 20 seconds
        int slowModeSeconds = slowMode ? 20 : 0;
        RoomLogic.setSlowMode(super.getRoom().getId().toString(), slowModeSeconds);
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
