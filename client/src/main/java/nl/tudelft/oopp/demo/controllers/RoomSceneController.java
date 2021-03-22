package nl.tudelft.oopp.demo.controllers;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.QuestionCell;
import nl.tudelft.oopp.demo.data.User;

public class RoomSceneController extends RoomController {
    @FXML
    private TextArea question;
    @FXML
    private Button sendButton;

    /**
     * Handles clicking the button.
     */
    public void sendButtonClicked() {
        if (!question.getText().trim().equals("")) {
            ServerCommunication.sendQuestion(question.getText(),
                    super.getRoomId(), super.getUser().getId(), super.getOpenTime());
        }
        updateAll();
        question.clear();
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
}
