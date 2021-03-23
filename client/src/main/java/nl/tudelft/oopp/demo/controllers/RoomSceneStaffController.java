package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nl.tudelft.oopp.demo.communication.ServerCommunication;

public class RoomSceneStaffController extends RoomController {

    @FXML
    private Button closeRoomButton;

    /**
     * Closes the current room through the server.
     * To prevent new questions from being asked.
     * Method called through JavaFX onAction attribute.
     */
    public void closeRoomButtonClicked() {
        ServerCommunication.closeRoom(super.getRoomId());
        updateRoomStatus();
    }

    /**
     * Gets the status of the room and updates the UI accordingly.
     */
    public void updateRoomStatus() {
        boolean isOpen = ServerCommunication.getRoomStatus(super.getRoomId());
        closeRoomButton.setDisable(!isOpen);
    }
}
