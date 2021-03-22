package nl.tudelft.oopp.demo.views;

import java.net.URL;
import java.time.LocalDateTime;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.controllers.RoomSceneController;
import nl.tudelft.oopp.demo.controllers.RoomSceneStaffController;
import nl.tudelft.oopp.demo.data.User;

public class RoomSceneDisplay {

    /**
     * Opens the roomScene in a new window.
     */
    public static void open(String roomScene, String roomId, String roomName,
                            String openTime, User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = RoomSceneDisplay.class.getResource(roomScene);
            loader.setLocation(xmlUrl);
            Parent root = loader.load();

            if (roomScene.equals("/roomScene.fxml")) {
                RoomSceneController controller = loader.getController();
                controller.setRoomInfo(roomId, roomName, openTime, user);
            } else if (roomScene.equals("/roomSceneStaff.fxml")) {
                RoomSceneStaffController controller = loader.getController();
                controller.setRoomInfo(roomId, roomName, openTime, user);
            }

            Stage roomStage = new Stage();
            roomStage.setScene(new Scene(root));
            roomStage.setTitle("AskIT");
            roomStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
