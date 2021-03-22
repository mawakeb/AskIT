package nl.tudelft.oopp.demo.data;

import java.util.HashSet;
import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.controllers.RoomController;
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
    private boolean staffRole;
    private final RoomController roomController;

    /**
     * QuestionCell Constructor, passed as lambda function in SetCellFactory.
     *
     * @param roomController the RoomController containing this ListView.
     */
    public QuestionCell(RoomController roomController) {
        this.roomController = roomController;
        this.staffRole = false;
        if (roomController instanceof RoomSceneStaffController) {
            this.staffRole = true;
        }
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

            //allow text wrapping of questions if a line is too long
            questionText.setMaxWidth(roomController.getListWidth() * 0.65);

            questionText.setWrapText(true);

            //create menu button for question
            MenuButton menuBtn = new MenuButton();
            menuBtn.getStyleClass().add("menu");
            menuBtn.getStylesheets().add(getClass()
                    .getResource("/roomSceneStyle.css").toExternalForm());


            //items for edit and delete (onclick method should be added)
            MenuItem editItem = new MenuItem("Edit");
            MenuItem deleteItem = new MenuItem("Delete");


            //create containers for nickname(in the future), timestamp and question
            HBox info = new HBox(10);
            VBox question = new VBox(0);

            info.getChildren().addAll(nickname, timestamp);
            question.getChildren().addAll(info, questionText);
            box.getChildren().addAll(question, center, upvoteBtn,upvoteText);

            // show ban option in menu if you are staff
            if (staffRole) {
                MenuItem banItem = new MenuItem("Ban User");
                menuBtn.getItems().addAll(editItem, deleteItem, banItem);
                box.setStyle("-fx-padding: 7 0 7 12");
                banItem.setOnAction(event -> useBanBtn(event, q));
                box.getChildren().add(menuBtn);

                if (!q.isAnswered()) {
                    MenuItem answerItem = new MenuItem("Answer");
                    answerItem.setOnAction(event -> useAnswerBtn(event, q));
                    menuBtn.getItems().add(answerItem);
                }
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
     * Automatically Called when clicking the answer button.
     *
     * @param event JavaFx button press event
     * @param q     question the button relates to
     */
    private void useAnswerBtn(ActionEvent event, Question q) {
        ServerCommunication.answerQuestion(q.getId());
        roomController.updateAll();
    }

    /**
     * Automatically Called when clicking the upvote button.
     *
     * @param event     JavaFX button press event
     * @param upvoteBtn button that was pressed
     * @param q         question the button relates to
     */
    private void useUpvoteBtn(ActionEvent event, Button upvoteBtn, Question q) {
        upvoteBtn.setDisable(true);
        ServerCommunication.upvoteQuestion(q.getId());
        upvotedQuestionIds.add(q.getId());
        roomController.updateQuestionList();
    }
}
