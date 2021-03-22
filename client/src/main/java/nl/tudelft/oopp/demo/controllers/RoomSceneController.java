package nl.tudelft.oopp.demo.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nl.tudelft.oopp.demo.communication.ServerCommunication;


public class RoomSceneController extends RoomController {
    @FXML
    private TextArea question;
    @FXML
    private Button sendButton;

    private boolean ban;

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
        if (!question.getText().trim().equals("")) {
            this.ban = ServerCommunication.sendQuestion(question.getText(),
                    super.getRoomId(), super.getUser().getId(), super.getOpenTime());
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
        boolean isOpen = ServerCommunication.getRoomStatus(super.getRoomId());
        sendButton.setDisable(!isOpen);
        question.setDisable(!isOpen);
        if (!isOpen) {
            question.setPromptText("The room is closed.");
        } else {
            question.setPromptText("Ask a question...");
        }
    }

    @Override
    public void updateAll() {
        updateQuestionList();
        updateRoomStatus();
        checkOpenTime();
        updateAnsweredQuestionList();
        checkBan();
    }
}
