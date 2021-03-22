package nl.tudelft.oopp.demo.controllers;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.QuestionCell;
import nl.tudelft.oopp.demo.data.User;

public abstract class RoomController {

    @FXML private ListView<Question> questionList;
    @FXML private Label roomName;
    @FXML private Label timeLabel;

    private String roomId;
    private ZonedDateTime openTime;
    private DateTimeFormatter formatter;
    private User user;

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
        this.formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm");
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
    //TODO: Use some sort of polling to call this method, instead of from refresh/send
    public void updateAll() {
        updateQuestionList();
        updateRoomStatus();
        checkOpenTime();
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
