package org.obehave.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Obehave extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui.fxml"));

        Scene scene = new Scene(root, 1024, 768);

        stage.setTitle("obehave");
        stage.setScene(scene);
        stage.show();
    }
}
