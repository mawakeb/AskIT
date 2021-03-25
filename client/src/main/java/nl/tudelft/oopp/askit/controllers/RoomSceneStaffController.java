package nl.tudelft.oopp.askit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import nl.tudelft.oopp.askit.communicationlogic.RoomLogic;
import nl.tudelft.oopp.askit.communicationlogic.SpeedLogic;
import nl.tudelft.oopp.askit.controllers.abstractclasses.RoomController;

public class RoomSceneStaffController extends RoomController {

    @FXML
    private Button closeRoomButton;
    @FXML
    private Slider slider;
    @FXML
    private ToggleButton speedButton;

    /**
     * Closes the current room through the server.
     * To prevent new questions from being asked.
     * Method called through JavaFX onAction attribute.
     */
    public void closeRoomButtonClicked() {
        RoomLogic.closeRoom(super.getRoomId());
        updateRoomStatus();
    }

    /**
     * Gets the status of the room and updates the UI accordingly.
     */
    public void updateRoomStatus() {
        boolean isOpen = RoomLogic.getRoomStatus(super.getRoomId());
        closeRoomButton.setDisable(!isOpen);
        slider.setVisible(isOpen);
        speedButton.setDisable(!isOpen);
        speedButton.setVisible(isOpen);
    }

    protected void updateRoomSpeed() {
        int currentRoomSpeed = SpeedLogic.getSpeed(super.getRoomId());
        if (currentRoomSpeed != 2) {
            System.out.println(currentRoomSpeed);
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
