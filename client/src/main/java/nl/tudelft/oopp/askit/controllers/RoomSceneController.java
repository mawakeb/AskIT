package nl.tudelft.oopp.askit.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nl.tudelft.oopp.askit.communicationlogic.QuestionLogic;
import nl.tudelft.oopp.askit.controllers.abstractclasses.RoomController;


public class RoomSceneController extends RoomController {
    @FXML
    private TextArea question;
    @FXML
    private Button sendButton;
    @FXML
    private Label slowModeLabel;

    private boolean ban;
    private int millisLeftForSlowMode;

    /**
     * Use @FXML initialize() instead of constructor.
     * This method is called after linking the @FXML elements.
     * so only at this point can ui elements be addressed from code.
     */
    @FXML
    @Override
    public void initialize() {
        super.initialize();

        this.ban = false;
        slowModeLabel.setVisible(false);
        question.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    keyEvent.consume();
                    if (keyEvent.isShiftDown()) {
                        question.appendText(System.getProperty("line.separator"));
                    } else {
                        sendButtonClicked();
                    }
                }
            }
        });
    }

    /**
     * Handles clicking the button.
     */
    public void sendButtonClicked() {
        updateSlowModeWaitTime();
        if (millisLeftForSlowMode > 0) {
            return;
        }

        if (!question.getText().trim().equals("")) {
            String questionStatus = QuestionLogic.sendQuestion(question.getText(),
                    super.getRoom().getId(),
                    super.getUser().getId(),
                    super.getRoom().getOpenTime());
            ban = questionStatus.equals("BANNED");
        }
        updateAll();
        question.clear();
    }

    /**
     * Check if user was banned and disable sending a question if so.
     */
    public void checkBan() {
        if (this.ban) {
            question.setPromptText("You are banned from asking questions");
            question.setDisable(true);
            sendButton.setDisable(true);
        }
    }

    /**
     * Gets the status of the room and updates the UI accordingly.
     */
    public void updateRoomStatus() {
        super.updateRoomStatus();
        boolean isOpen = super.getRoom().isOpen();
        sendButton.setDisable(!isOpen);
        question.setDisable(!isOpen);
        if (!isOpen) {
            question.setPromptText("The room is closed.");
        } else {
            question.setPromptText("Ask a question...");
        }
    }

    /**
     * Updates when the user is able to send questions (regarding slow mode).
     * Displays a message with the time left to wait.
     */
    public void updateSlowModeWaitTime() {
        millisLeftForSlowMode = QuestionLogic.getTimeLeft(
                super.getUser().getId().toString(),
                super.getRoom().getId().toString());
        if (millisLeftForSlowMode > 0) {
            String timeString = millisLeftForSlowMode / 1000 + " seconds";
            slowModeLabel.setText(
                    "Slow Mode: Wait " + timeString + " before asking a new question");
            slowModeLabel.setVisible(true);
        } else {
            slowModeLabel.setVisible(false);
        }
    }

    @Override
    public void updateAll() {
        super.updateAll();
        if (millisLeftForSlowMode > 0) {
            updateSlowModeWaitTime();
        }
        checkBan();
    }
}
