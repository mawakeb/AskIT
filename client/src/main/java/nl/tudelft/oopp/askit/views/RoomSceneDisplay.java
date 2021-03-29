package nl.tudelft.oopp.askit.views;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.askit.controllers.RoomSceneController;
import nl.tudelft.oopp.askit.controllers.RoomSceneStaffController;
import nl.tudelft.oopp.askit.data.Room;
import nl.tudelft.oopp.askit.data.User;

public class RoomSceneDisplay {

    /**
     * Opens the roomScene in a new window.
     */
    public static void open(String roomScene, Room room, User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = RoomSceneDisplay.class.getResource(roomScene);
            loader.setLocation(xmlUrl);
            Parent root = loader.load();

            if (roomScene.equals("/fxml/roomScene.fxml")) {
                RoomSceneController controller = loader.getController();
                controller.setRoomInfo(room, user);
            } else if (roomScene.equals("/fxml/roomSceneStaff.fxml")) {
                RoomSceneStaffController controller = loader.getController();
                controller.setRoomInfo(room, user);
            }

            Stage roomStage = new Stage();
            roomStage.setScene(new Scene(root));
            roomStage.setTitle(room.getName());
            roomStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
