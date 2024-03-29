package nl.tudelft.oopp.askit.views;

import java.net.URL;
import java.net.http.HttpResponse;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.askit.controllers.ErrorController;

public class ErrorDisplay {

    /**
     * Opens the errorDisplay in a new window.
     */
    public static void open(String errorName, String stackTrace) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = RoomSceneDisplay.class.getResource("/fxml/errorScene.fxml");
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

    /**
     * Opens the errorDisplay for errors over http.
     */
    public static void open(HttpResponse<String> response) {
        String errorName = "Response code: " + response.statusCode();
        String stackTrace = response.body();
        open(errorName,stackTrace);
    }
}
