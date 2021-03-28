package nl.tudelft.oopp.askit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import nl.tudelft.oopp.askit.communicationlogic.RoomLogic;
import nl.tudelft.oopp.askit.controllers.abstractclasses.RoomController;

public class RoomSceneStaffController extends RoomController {

    @FXML
    private MenuItem closeRoomItem;
    @FXML
    private MenuItem modeItem;

    private boolean mode;

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
}
