package org.obehave.view.components;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class WelcomeControl extends BorderPane {
    public WelcomeControl() {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("org/obehave/view/components/welcome.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
