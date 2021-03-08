package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.Quote;

import java.util.List;

public class MainSceneController {
    @FXML
    private TextField search;
    @FXML
    private ListView<Question> questionList;

    /**
     * Handles clicking the button.
     */
    public void buttonClicked() {
        ServerCommunication.sendQuestion(search.getText());
        updateQuestionList();
    }

    /**
     * Fetches all questions from the server
     * Then updates the listview contents to display them
     * TODO: Use some sort of polling to call this method, instead of from buttonClicked
     */
    public void updateQuestionList(){
        List<Question> questionStrings = ServerCommunication.getQuestions();
        questionList.getItems().clear();
        questionList.getItems().addAll(questionStrings);
    }
}
