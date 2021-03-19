package nl.tudelft.oopp.demo.views;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.controllers.ErrorController;

public class ErrorDisplay {

    /**
     * Opens the roomScene in a new window.
     */
    public static void open(String errorName, String stackTrace) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = RoomSceneDisplay.class.getResource("/errorScene.fxml");
            loader.setLocation(xmlUrl);
            Parent root = loader.load();

            ErrorController controller = loader.getController();
            controller.setErrorInfo(errorName,stackTrace);


            Stage roomStage = new Stage();
            roomStage.setScene(new Scene(root));
            roomStage.setTitle(errorName);
            roomStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
