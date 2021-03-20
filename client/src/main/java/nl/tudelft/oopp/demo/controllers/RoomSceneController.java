package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import java.util.UUID;

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
import nl.tudelft.oopp.demo.data.User;

public class RoomSceneController {
    @FXML
    private TextArea question;
    @FXML
    private ListView<Question> questionList;
    @FXML
    private Button sendButton;
    @FXML
    private Label roomName;

    private String roomId;
    private User user;

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
     */
    public void setRoomInfo(String roomId, String roomName, String roleId) {
        this.roomId = roomId;
        this.roomName.setText(roomName);
        this.user = new User(UUID.fromString(roomId),"student","username default",roleId);
        updateAll();
    }

    /**
     * Handles clicking the button.
     */
    public void sendButtonClicked() {
        ServerCommunication.sendQuestion(question.getText(),roomId, user.getId());
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
     * Gets the status of the room and updates the UI accordingly.
     */
    public void updateRoomStatus() {
        boolean isOpen = ServerCommunication.getRoomStatus(roomId);
        sendButton.setDisable(!isOpen);
        question.setDisable(!isOpen);
        if (!isOpen) {
            question.setPromptText("The room is closed.");
        }
    }

    /**
     * Gets all possible updates from the server.
     */
    //TODO: Use some sort of polling to call this method, instead of from refresh/send
    public void updateAll() {
        updateQuestionList();
        updateRoomStatus();
    }
}
