package nl.tudelft.oopp.askit.controllers.abstractclasses;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javax.swing.JFileChooser;
import nl.tudelft.oopp.askit.communicationlogic.QuestionLogic;
import nl.tudelft.oopp.askit.communicationlogic.RoomLogic;
import nl.tudelft.oopp.askit.data.Question;
import nl.tudelft.oopp.askit.data.Room;
import nl.tudelft.oopp.askit.data.User;
import nl.tudelft.oopp.askit.methods.TimeControl;
import nl.tudelft.oopp.askit.views.scenecomponents.QuestionCell;




public abstract class RoomController {

    @FXML
    private ListView<Question> questionList;
    @FXML
    private ListView<Question> answeredQuestionList;
    @FXML
    private Label timeLabel;
    @FXML
    private Slider slider;
    @FXML
    private ToggleButton speedButton;
    @FXML
    private Label roomName;

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

        Tooltip tooltip = new Tooltip("Feedback on speed of lecture");
        tooltip.setShowDelay(Duration.seconds(0.2));
        speedButton.setTooltip(tooltip);

        this.slider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                if (n < 1) {
                    return "Too Slow";
                }
                if (n < 2) {
                    return "Slow";
                }
                if (n < 3) {
                    return "Normal";
                }
                if (n < 4) {
                    return "Fast";
                }
                return "Too Fast";
            }

            @Override
            public Double fromString(String s) {
                switch (s) {
                    case "Too Slow":
                        return 0d;
                    case "Slow":
                        return 1d;
                    case "Normal":
                        return 2d;
                    case "Fast":
                        return 3d;
                    default:
                        return 4d;
                }
            }
        });
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
        roomName.setText(room.getName());
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

        if (ZonedDateTime.now().isBefore(room.getLocalOpenTime())) {
            timeLabel.setVisible(true);
            String time = "Room opens at " + room.getLocalOpenTime().format(formatter);
            timeLabel.setText(time);
        } else {
            timeLabel.setVisible(false);
        }
    }

    /**
     * Exports answered questions into text file.
     */

    public void exportQuestions() throws FileNotFoundException {
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        f.showSaveDialog(null);
        File file = new File(f.getSelectedFile().toString()
                + "\\" + room.getName() + " Export.txt");
        System.out.println(file.getAbsolutePath());
        PrintWriter writer = new PrintWriter(file);

        List<Question> questions = QuestionLogic.getAnswered(getRoomId());
        for (Question question : questions) {
            String prettyTime = TimeControl.getPrettyTime(question.getAnswerTime()).trim();
            String questionContent = question.getContent().trim();
            String username = question.getUsername();
            writer.print("[" + username + "/" + prettyTime + "]  " + questionContent + "\n\n");
        }
        writer.flush();
        writer.close();

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
        this.room = RoomLogic.getRoomStatus(room.getId().toString());
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

    /**
     * When the speedButton is clicked, it toggles to show/hide the slider for speed.
     */
    public void toggleSlider() {
        slider.setDisable(!slider.isDisabled());
        slider.setVisible(!slider.isVisible());
    }

    public String getRoomId() {
        return this.room.getId().toString();
    }

    public User getUser() {
        return this.user;
    }
}
