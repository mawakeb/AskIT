package nl.tudelft.oopp.demo.controllers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.demo.communication.ServerCommunication;

/**
 * Controller for mainScene.fxml.
 */
public class MainSceneController {

    @FXML
    private TextField userText;
    @FXML
    private ListView<String> list;
    @FXML
    private Label tabTitle;
    @FXML
    private Button createButton;
    @FXML
    private Button joinButton;
    @FXML
    private Label tabIcon;
    @FXML
    private TextField username;


    /**
     * Handles switching Create Tab.
     */
    public void createTabClicked() {
        tabTitle.setText("Create Lecture Room");
        tabIcon.setText("+ ");
        userText.setPromptText("Enter name of the lecture...");
        createButton.toFront();
        list.setVisible(true);
        joinButton.setVisible(false);
        joinButton.setDisable(true);
        username.setVisible(false);
        username.setDisable(true);
    }

    /**
     * Handles switching Join Tab.
     */
    public void joinTabClicked() {
        tabTitle.setText("Join Lecture Room");
        tabIcon.setText("# ");
        userText.setPromptText("Enter link to join the lecture...");
        createButton.toBack();
        list.setVisible(false);
        joinButton.setVisible(true);
        joinButton.setDisable(false);
        username.setVisible(true);
        username.setDisable(false);
    }

    /**
     * Handles clicking the create button.
     */
    public void createButtonClicked() {
        List<String> links = ServerCommunication.createRoom(userText.getText());
        userText.clear();
        list.getItems().clear();
        if (links.size() == 1) {
            list.getItems().add("error " + links.get(0));
        } else {
            list.getItems().add("Click on link (individually) to copy"
                    + "\nstaff link: ");
            list.getItems().add(links.get(0));
            list.getItems().add("student link: ");
            list.getItems().add(links.get(1));
        }
    }

    /**
     * Copy the link to the clipboard when the list is clicked.
     */
    public void copyList() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String link = list.getSelectionModel().getSelectedItem();
        StringSelection linkCopy = new StringSelection(link);
        clipboard.setContents(linkCopy, null);
    }

    /**
     * Handles clicking the join button.
     */
    public void joinButtonClicked() {
        ServerCommunication.joinRoom(userText.getText());
        userText.clear();
    }
}
