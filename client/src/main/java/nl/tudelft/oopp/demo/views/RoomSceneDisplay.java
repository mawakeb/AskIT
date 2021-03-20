package nl.tudelft.oopp.demo.views;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.controllers.RoomSceneController;
import nl.tudelft.oopp.demo.controllers.RoomSceneStaffController;

public class RoomSceneDisplay {

    /**
     * Opens the roomScene in a new window.
     */
    public static void open(String roomScene, String roomId, String roomName, String roleId) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = RoomSceneDisplay.class.getResource(roomScene);
            loader.setLocation(xmlUrl);
            Parent root = loader.load();

            if (roomScene.equals("/roomScene.fxml")) {
                RoomSceneController controller = loader.getController();
                controller.setRoomInfo(roomId,roomName, roleId);
            } else if (roomScene.equals("/roomSceneStaff.fxml")) {
                RoomSceneStaffController controller = loader.getController();
                controller.setRoomInfo(roomId,roomName, roleId);
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
