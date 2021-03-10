package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.QuestionCell;

public class RoomSceneController {
    @FXML
    private TextField question;
    @FXML
    private ListView<Question> questionList;

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
        updateQuestionList();
    }

    /**
     * Handles clicking the button.
     */
    public void sendButtonClicked() {
        ServerCommunication.sendQuestion(question.getText());
        updateQuestionList();
        question.clear();
    }

    /**
     * Fetches all questions from the server.
     * Then updates the listview contents to display them.
     * TODO: Use some sort of polling to call this method, instead of from buttonClicked
     */
    public void updateQuestionList() {
        List<Question> questions = ServerCommunication.getQuestions();
        questionList.getItems().clear();
        questionList.getItems().addAll(questions);
    }
}
