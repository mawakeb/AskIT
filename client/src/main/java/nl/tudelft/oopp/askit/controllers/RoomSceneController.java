package nl.tudelft.oopp.askit.controllers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nl.tudelft.oopp.askit.communicationlogic.QuestionLogic;
import nl.tudelft.oopp.askit.communicationlogic.RoomLogic;
import nl.tudelft.oopp.askit.controllers.abstractclasses.RoomController;
import nl.tudelft.oopp.askit.data.Question;
import nl.tudelft.oopp.askit.methods.TimeControl;


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
            this.ban = QuestionLogic.sendQuestion(question.getText(),
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
        boolean isOpen = RoomLogic.getRoomStatus(super.getRoomId());
        sendButton.setDisable(!isOpen);
        question.setDisable(!isOpen);
        if (!isOpen) {
            question.setPromptText("The room is closed.");
        } else {
            question.setPromptText("Ask a question...");
        }
    }

    public void exportQuestions() throws FileNotFoundException {
    	List<Question> questions = QuestionLogic.getAnswered(super.getRoomId());
    	PrintWriter writer = new PrintWriter(new File("ExportedQuestions.txt"));
    	for(Question question : questions) {
    		String prettyTime = TimeControl.getPrettyTime(question.getAnswerTime()).trim();
    		String questionContent = question.getContent().trim();
    		writer.print(questionContent + " " + prettyTime + "\n");
    	}
    	writer.flush();
    	writer.close();
    	//ErrorDisplay.open("Success!", "Successfully expored questions.");
  	
    }

    @Override
    public void updateAll() {
        super.updateAll();
        checkBan();
    }
}
