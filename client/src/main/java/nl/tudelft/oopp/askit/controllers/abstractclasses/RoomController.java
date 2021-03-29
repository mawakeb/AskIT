package nl.tudelft.oopp.askit.controllers.abstractclasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import nl.tudelft.oopp.askit.communicationlogic.QuestionLogic;
import nl.tudelft.oopp.askit.data.Question;
import nl.tudelft.oopp.askit.data.User;
import nl.tudelft.oopp.askit.methods.TimeControl;
import nl.tudelft.oopp.askit.views.scenecomponents.QuestionCell;


public abstract class RoomController {

    @FXML
    private ListView<Question> questionList;
    @FXML
    private ListView<Question> answeredQuestionList;
    @FXML
    private Label roomName;
    @FXML
    private Label timeLabel;

    private String roomId;
    private ZonedDateTime openTime;
    private DateTimeFormatter formatter;
    private User user;
    private TimerTask update;
    private Timer timer;

    /**
     * Use @FXML initialize() instead of constructor.
     * This method is called after linking the @FXML elements.
     * so only at this point can ui elements be addressed from code.
     */
    @FXML
    public void initialize() {
        // initialize cellFactory, to have questions be rendered using the QuestionCell class
        questionList.setCellFactory(params -> {
            return new QuestionCell(this);
        });
        answeredQuestionList.setCellFactory(params -> {
            return new QuestionCell(this);
        });
        this.formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm");

        this.update = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    updateAll();
                });
            }
        };
        this.timer = new Timer();
    }

    /**
     * shows name of the room in the scene and sets value for roomId.
     *
     * @param roomId     - UUID of the room
     * @param roomName   - Name of the room
     * @param stringTime - openTime of the room in Sting
     */
    public void setRoomInfo(String roomId, String roomName, String stringTime, User user) {
        this.roomId = roomId;
        this.roomName.setText(roomName);
        ZonedDateTime zonedTime = ZonedDateTime.parse(stringTime)
                .withZoneSameInstant(TimeZone.getDefault().toZoneId());
        this.openTime = zonedTime;
        this.user = user;
        updateAll();
        timer.scheduleAtFixedRate(update, 0, 10000);
    }

    /**
     * Fetches all questions from the server.
     * Then updates the listview contents to display them.
     */
    public void updateQuestionList() {
        List<Question> questions = QuestionLogic.getQuestions(roomId);
        questionList.getItems().clear();
        questionList.getItems().addAll(questions);
    }

    /**
     * Fetches all answered questions from the server.
     * Then updates the listview contents to display them.
     */
    public void updateAnsweredQuestionList() {
        List<Question> questions = QuestionLogic.getAnswered(roomId);
        answeredQuestionList.getItems().clear();
        answeredQuestionList.getItems().addAll(questions);
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
            String time = "Room opens at " + openTime.format(formatter);
            timeLabel.setText(time);
        } else {
            timeLabel.setVisible(false);
        }
    }

    /**
     * Update room-specific elements.
     * Implemented differently for staff and student room.
     */
    public abstract void updateRoomStatus();

    /**
     * Gets all possible updates from the server.
     */
    public void updateAll() {
        updateQuestionList();
        updateRoomStatus();
        checkOpenTime();
        updateAnsweredQuestionList();
    }

    /**
     * Returns width of questionList to estimate window size.
     *
     * @return width of questionList
     */
    
    public void exportQuestions() throws FileNotFoundException {
    	List<Question> questions = QuestionLogic.getAnswered(getRoomId());
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

    
    public double getListWidth() {
        return this.questionList.getWidth();
    }

    public String getRoomId() {
        return this.roomId;
    }

    public User getUser() {
        return this.user;
    }

    public ZonedDateTime getOpenTime() {
        return this.openTime;
    }
}
