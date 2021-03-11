package nl.tudelft.oopp.demo.controllers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.demo.communication.ServerCommunication;

public class MainSceneController {
    @FXML
    private TextField name;
    @FXML
    private TextField link;
    @FXML
    private ListView<String> list;

    /**
     * Handles clicking the create button.
     */
    public void createButtonClicked() {
        List<String> links = ServerCommunication.createRoom(name.getText());
        name.clear();
        list.getItems().clear();
        if (links.size() == 1) {
            list.getItems().add("error " + links.get(0));
        } else {
            list.getItems().add("Click on link (individually) to copy to clipboard");
            list.getItems().add("staff link: ");
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
        ServerCommunication.joinRoom(link.getText());
        link.clear();
    }
}
