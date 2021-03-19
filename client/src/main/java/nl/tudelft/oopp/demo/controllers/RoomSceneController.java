package nl.tudelft.oopp.demo.controllers;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TimeZone;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.QuestionCell;

public class RoomSceneController {
    @FXML private TextArea question;
    @FXML private ListView<Question> questionList;
    @FXML private Button sendButton;
    @FXML private Label roomName;
    @FXML private Label timeLabel;

    private String roomId;
    private ZonedDateTime openTime;

    /**
     * Use @FXML initialize() instead of constructor.
     * This method is called after linking the @FXML elements.
     * so only at this point can ui elements be addressed from code.
     */
    @FXML
    public void initialize() {
        // initialize cellFactory, to have questions be rendered using the QuestionCell class
        questionList.setCellFactory((Callback<ListView<Question>, ListCell<Question>>) params -> {
            return new QuestionCell(this);
        });
    }

    /**
     * shows name of the room in the scene and sets value for roomId.
     *
     * @param roomId - UUID of the room
     * @param roomName - Name of the room
     * @param stringTime - openTime of the room in Sting
     */
    public void setRoomInfo(String roomId, String roomName, String stringTime) {
        this.roomId = roomId;
        this.roomName.setText(roomName);
        ZonedDateTime zonedTime = ZonedDateTime.parse(stringTime)
                .withZoneSameInstant(TimeZone.getDefault().toZoneId());
        this.openTime = zonedTime;
        updateAll();
    }

    /**
     * Handles clicking the button.
     */
    public void sendButtonClicked() {
        if (!question.getText().trim().equals("")) {
            ServerCommunication.sendQuestion(question.getText(),roomId);
        }
        updateAll();
        question.clear();
    }

    /**
     * Fetches all questions from the server.
     * Then updates the listview contents to display them.
     */
    public void updateQuestionList() {
        List<Question> questions = ServerCommunication.getQuestions(roomId);
        questionList.getItems().clear();
        questionList.getItems().addAll(questions);
    }

    /**
     * Shows openTime if the room is not open because it's not the scheduled time yet.
     */
    public void checkOpenTime() {

        if (!timeLabel.isVisible()) {
            return;
        }

        if (openTime.isAfter(ZonedDateTime.now())) {
            timeLabel.setVisible(true);
            String time = "Room opens at " + openTime.truncatedTo(ChronoUnit.MINUTES).toString();
            timeLabel.setText(time);
        } else {
            timeLabel.setVisible(false);
        }
    }

    /**
     * Gets the status of the room and updates the UI accordingly.
     */
    public void updateRoomStatus() {
        boolean isOpen = ServerCommunication.getRoomStatus(roomId);
        sendButton.setDisable(!isOpen);
        question.setDisable(!isOpen);
        if (!isOpen) {
            question.setPromptText("The room is closed.");
        } else {
            question.setPromptText("Ask a question...");
        }
    }

    /**
     * Gets all possible updates from the server.
     */
    //TODO: Use some sort of polling to call this method, instead of from refresh/send
    public void updateAll() {
        updateQuestionList();
        updateRoomStatus();
        checkOpenTime();
    }
}
