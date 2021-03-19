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

    /**
     * QuestionCell Constructor, passed as lambda function in SetCellFactory.
     *
     * @param roomSceneController the RoomSceneController containing this ListView.
     */
    public QuestionCell(RoomSceneController roomSceneController) {
        this.roomSceneController = roomSceneController;
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

            // create a creation timestamp
            String creationTimeStamp = TimeControl.getPrettyTime(q.getCreateTime()); // this needs to be displayed
            System.out.println(creationTimeStamp);

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
            box.getChildren().addAll(questionText, center, upvoteText, upvoteBtn);
            setText(null);
            setGraphic(box);
        }
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
