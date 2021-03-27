package nl.tudelft.oopp.askit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import nl.tudelft.oopp.askit.communicationlogic.RoomLogic;
import nl.tudelft.oopp.askit.controllers.abstractclasses.RoomController;

public class RoomSceneStaffController extends RoomController {

    @FXML
    private Button closeRoomButton;
    @FXML
    private CheckBox slowModeToggle;

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
     * Gets the status of the room and updates the UI accordingly.
     */
    public void updateRoomStatus() {
        super.updateRoomStatus();
        boolean isOpen = super.getRoom().isOpen();
        closeRoomButton.setDisable(!isOpen);
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
}
