package nl.tudelft.oopp.askit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorController {
    @FXML
    private Button closeWindowButton;
    @FXML
    private Label errorDescription;
    @FXML
    private Label errorTitle;

    /** Sets the variables and UI text.
     *
     * @param errorName name that is displayed on top
     * @param stackTrace stacktrace or just an error message
     */
    public void setErrorInfo(String errorName, String stackTrace) {
        errorTitle.setText(errorName);
        errorDescription.setText(stackTrace);
    }

    /** Closes a window.
     *
     */
    public void closeWindow() {
        Stage stage = (Stage) closeWindowButton.getScene().getWindow();
        stage.close();
    }

}
