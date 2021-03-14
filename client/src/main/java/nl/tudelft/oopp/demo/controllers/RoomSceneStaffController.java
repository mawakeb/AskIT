package nl.tudelft.oopp.demo.controllers;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.QuestionCell;

public class RoomSceneStaffController {

    @FXML
    private ListView<Question> questionList;
    @FXML
    private Button closeRoomButton;

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
        updateAll();
    }

    /**
     * Closes the current room through the server.
     * To prevent new questions from being asked.
     * Method called through JavaFX onAction attribute.
     */
    public void closeRoomButtonClicked() {
        ServerCommunication.closeRoom();
        updateRoomStatus();

    }

    /**
     * Fetches all questions from the server.
     * Then updates the listview contents to display them.
     */
    public void updateQuestionList() {
        List<Question> questions = ServerCommunication.getQuestions();
        questionList.getItems().clear();
        questionList.getItems().addAll(questions);
    }

    /**
     * Gets the status of the room and updates the UI accordingly.
     */
    public void updateRoomStatus() {
        boolean isOpen = ServerCommunication.getRoomStatus();
        closeRoomButton.setDisable(!isOpen);
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
