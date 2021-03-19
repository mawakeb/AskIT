package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;

public class ErrorController {
    @FXML
    private Button closeWindow;
    @FXML
    private DialogPane errorDescription;
    @FXML
    private DialogPane errorTitle;

    private String errorName;
    private String stackTrace;

    public void setErrorInfo(String errorName, String stackTrace) {
        this.errorName = errorName;
        this.stackTrace = stackTrace;
        errorTitle.setContentText(errorName);
        errorDescription.setContentText(stackTrace);
    }

}
