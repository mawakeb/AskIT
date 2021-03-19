package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorController {
    @FXML
    private Button closeWindowBTN;
    @FXML
    private Label errorDescription;
    @FXML
    private Label errorTitle;

    private String errorName;
    private String stackTrace;

    public void setErrorInfo(String errorName, String stackTrace) {
        this.errorName = errorName;
        this.stackTrace = stackTrace;
        errorTitle.setText(errorName);
        errorDescription.setText(stackTrace);
    }

    public void closeWindow() {
        Stage stage = (Stage) closeWindowBTN.getScene().getWindow();
        stage.close();
    }

}
