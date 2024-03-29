package nl.tudelft.oopp.askit.views;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainSceneDisplay extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = getClass().getResource("/fxml/mainScene.fxml");
            loader.setLocation(xmlUrl);
            Parent root = loader.load();

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("AskIT");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }
}
