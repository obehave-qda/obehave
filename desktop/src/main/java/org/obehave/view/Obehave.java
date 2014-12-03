package org.obehave.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Obehave extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        final URL mainFxml = getClass().getClassLoader().getResource("ui/main.fxml");
        if (mainFxml == null) {
            throw new IllegalStateException("Couldn't find main.fxml!");
        }

        Parent root = FXMLLoader.load(mainFxml);

        Scene scene = new Scene(root);

        stage.setTitle("obehave");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
