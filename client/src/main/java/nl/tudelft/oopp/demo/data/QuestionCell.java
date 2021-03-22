package nl.tudelft.oopp.demo.data;

import java.util.HashSet;
import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.controllers.RoomSceneController;
import nl.tudelft.oopp.demo.controllers.RoomSceneStaffController;
import nl.tudelft.oopp.demo.methods.TimeControl;

/**
 * Class that forms an entry in the question listview.
 * Also controls up voting behaviour.
 */
public class QuestionCell extends ListCell<Question> {

    // Set containing ID's of the questions that the user
    // up voted this session, to prevent up voting multiple times
    private static final HashSet<UUID> upvotedQuestionIds = new HashSet();

    private RoomSceneController roomSceneController;
    private RoomSceneStaffController roomSceneStaffController;
    private final boolean staffRole;

    /**
     * QuestionCell Constructor, passed as lambda function in SetCellFactory.
     *
     * @param roomSceneController the RoomSceneController containing this ListView.
     */
    public QuestionCell(RoomSceneController roomSceneController) {
        this.roomSceneController = roomSceneController;
        this.staffRole = false;
        this.setStyle("-fx-background-color: #0000;"
                + "-fx-padding: 7 0 0 0;"
                + "-fx-text-fill: #fff;");

    }

    /**
     * QuestionCell Constructor, passed as lambda function in SetCellFactory.
     * Separate constructor for roomSceneStaff
     *
     * @param roomSceneStaffController the RoomSceneController containing this ListView.
     */
    public QuestionCell(RoomSceneStaffController roomSceneStaffController) {
        this.roomSceneStaffController = roomSceneStaffController;
        this.staffRole = true;
        this.setStyle("-fx-background-color: #0000;"
                + "-fx-padding: 7 0 0 0;"
                + "-fx-text-fill: #fff;");
    }

    /**
     * Method that is called when updating an entry in a question ListView.
     *
     * @param q     Question this entry relates to.
     * @param empty Boolean that is true iff this entry of the list is empty.
     *              inspired by https://stackoverflow.com/questions/53602086/having-two-button-in-a-list-view-in-javafx-with-xml-file
     */
    @Override
    protected void updateItem(Question q, boolean empty) {
        super.updateItem(q, empty);

        if (q == null || empty) {
            setText(null);
            setGraphic(null);
        } else {

            HBox box = new HBox(10);
            box.setAlignment(Pos.CENTER_LEFT);
            box.getStyleClass().add("box");
            box.getStylesheets().add(getClass()
                    .getResource("/roomSceneStyle.css").toExternalForm());

            // expanding region that pushes elements to the sides
            Region center = new Region();
            HBox.setHgrow(center, Priority.ALWAYS);

            // nickname should be added when it is implemented
            Label nickname = new Label();
            nickname.setText("nickname");
            nickname.setStyle("-fx-text-fill: #9e9e9e;"
                    + "-fx-font-size: 80%;");

            // create a creation timestamp
            Label timestamp = new Label();
            String creationTimeStamp = TimeControl.getPrettyTime(q.getCreateTime());
            timestamp.setText(creationTimeStamp);
            timestamp.setStyle("-fx-text-fill: #9e9e9e;"
                    + "-fx-font-size: 70%;");


            // create upvote button
            Button upvoteBtn = new Button("");
            upvoteBtn.setDisable(upvotedQuestionIds.contains(q.getId()));
            upvoteBtn.setOnAction(event -> useUpvoteBtn(event, upvoteBtn, q));
            upvoteBtn.getStyleClass().add("upvote");
            upvoteBtn.getStylesheets().add(getClass()
                    .getResource("/roomSceneStyle.css").toExternalForm());

            // combine elements in box and set the cell display to it
            Label questionText = new Label(q.toString());
            questionText.getStyleClass().add("question");

            Label upvoteText = new Label(Integer.toString(q.getUpvotes()));
            upvoteText.getStyleClass().add("question");

            //create containers for nickname(in the future), timestamp and question
            HBox info = new HBox(10);
            VBox question = new VBox(0);

            info.getChildren().addAll(nickname, timestamp);
            question.getChildren().addAll(info, questionText);
            box.getChildren().addAll(question, center, upvoteText, upvoteBtn);

            // create ban button if created using RoomSceneStaffController
            if (staffRole) {
                Button banBtn = new Button("Ban");
                banBtn.setOnAction(event -> useBanBtn(event, q));
                banBtn.getStyleClass().add("ban");
                banBtn.getStylesheets().add(getClass()
                        .getResource("/roomSceneStyle.css").toExternalForm());
                box.getChildren().add(banBtn);
            }

            setText(null);
            setGraphic(box);
        }
    }

    /**
     * Automatically Called when clicking the ban button.
     *
     * @param event JavaFx button press event
     * @param q     question the button relates to
     */
    private void useBanBtn(ActionEvent event, Question q) {
        ServerCommunication.banUser(q.getUserId());
    }

    /**
     * Automatically Called when clicking the upvote button.
     *
     * @param event     JavaFX button press event
     * @param upvoteBtn button that was pressed
     * @param q         question the button relates to
     */
    private void useUpvoteBtn(ActionEvent event, Button upvoteBtn, Question q) {
        if (roomSceneController != null) {
            upvoteBtn.setDisable(true);
            ServerCommunication.upvoteQuestion(q.getId());
            upvotedQuestionIds.add(q.getId());
            roomSceneController.updateQuestionList();
        }
    }
}
