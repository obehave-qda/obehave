package org.obehave.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

public class Obehave extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        final URL mainFxml = getClass().getClassLoader().getResource("ui/main.fxml");
        if (mainFxml == null) {
            throw new IllegalStateException("Couldn't load main.fxml!");
        }

        Parent root = FXMLLoader.load(mainFxml);

        Scene scene = new Scene(root);

        stage.setTitle("obehave");
        stage.setScene(scene);
        stage.setMaximized(true);

        stage.getIcons().addAll(new Image(getClass().getClassLoader().getResourceAsStream("icons/icon_16x16.png")),
                new Image(getClass().getClassLoader().getResourceAsStream("icons/icon_32x32.png")),
                new Image(getClass().getClassLoader().getResourceAsStream("icons/icon_48x48.png")),
                new Image(getClass().getClassLoader().getResourceAsStream("icons/icon_96x96.png")),
                new Image(getClass().getClassLoader().getResourceAsStream("icons/icon_144x144.png")),
                new Image(getClass().getClassLoader().getResourceAsStream("icons/icon_256x256.png")));

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}