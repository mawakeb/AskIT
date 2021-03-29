package nl.tudelft.oopp.askit.controllers;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    @FXML
    private CheckBox slowModeToggle;

    /**
     * Closes the current room through the server.
     * To prevent new questions from being asked.
     * Method called through JavaFX onAction attribute.
     */
    public void closeRoomButtonClicked() {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	alert.setTitle("Confirm");
    	alert.setContentText("Are you sure?");
    	ButtonType btnYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    	
    	ButtonType btnNo = new ButtonType("No", ButtonBar.ButtonData.NO);
    	
    	alert.getButtonTypes().setAll(btnYes, btnNo);
    	
    	alert.showAndWait().ifPresent(type -> {
    		if (type == btnYes) {
    			RoomLogic.closeRoom(super.getRoomId());
    	        updateRoomStatus();
    		}
    	});
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
