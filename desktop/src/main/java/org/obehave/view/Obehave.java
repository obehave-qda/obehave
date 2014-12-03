package org.obehave.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Obehave extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/main.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("obehave");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
