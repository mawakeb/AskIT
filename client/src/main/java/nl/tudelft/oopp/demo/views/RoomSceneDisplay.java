package nl.tudelft.oopp.demo.views;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RoomSceneDisplay {

    /**
     * Opens the roomScene in a new window.
     */
    public static void open(String roomScene) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = RoomSceneDisplay.class.getResource(roomScene);
            loader.setLocation(xmlUrl);
            Parent root = loader.load();
            Stage roomStage = new Stage();

            roomStage.setScene(new Scene(root));
            roomStage.setTitle("AskIT");
            roomStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
