package nl.tudelft.oopp.askit.controllers.abstractclasses;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import nl.tudelft.oopp.askit.communicationlogic.QuestionLogic;
import nl.tudelft.oopp.askit.communicationlogic.RoomLogic;
import nl.tudelft.oopp.askit.data.Question;
import nl.tudelft.oopp.askit.data.Room;
import nl.tudelft.oopp.askit.data.User;
import nl.tudelft.oopp.askit.views.scenecomponents.QuestionCell;


public abstract class RoomController {

    @FXML
    private ListView<Question> questionList;
    @FXML
    private ListView<Question> answeredQuestionList;
    @FXML
    private Label timeLabel;

    private Room room;
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
        questionList.setCellFactory(params -> new QuestionCell(this));
        answeredQuestionList.setCellFactory(params -> new QuestionCell(this));
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
     * @param room the room object created on the server
     * @param user the user object related to this window
     */
    public void setRoomInfo(Room room, User user) {
        this.room = room;
        this.user = user;
        updateAll();
        timer.scheduleAtFixedRate(update, 0, 10000);
    }

    /**
     * Fetches all questions from the server.
     * Then updates the listview contents to display them.
     */
    public void updateQuestionList() {
        List<Question> questions = QuestionLogic.getQuestions(room.getId().toString());
        questionList.getItems().clear();
        questionList.getItems().addAll(questions);
    }

    /**
     * Fetches all answered questions from the server.
     * Then updates the listview contents to display them.
     */
    public void updateAnsweredQuestionList() {
        List<Question> questions = QuestionLogic.getAnswered(room.getId().toString());
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

        if (!room.isOpen()) {
            timeLabel.setVisible(true);
            String time = "Room opens at " + room.getLocalOpenTime().format(formatter);
            timeLabel.setText(time);
        } else {
            timeLabel.setVisible(false);
        }
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Update room-specific elements.
     * Implemented differently for staff and student room.
     */
    public void updateRoomStatus() {
        RoomLogic.getRoomStatus(room.getId().toString());
    }

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
    public double getListWidth() {
        return this.questionList.getWidth();
    }

    public User getUser() {
        return this.user;
    }
}
