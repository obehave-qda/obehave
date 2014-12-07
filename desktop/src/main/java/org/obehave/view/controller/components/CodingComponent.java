package org.obehave.view.controller.components;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CodingComponent extends AnchorPane {
    private static final Logger log = LoggerFactory.getLogger(CodingComponent.class);

    @FXML
    private Rectangle rect;

    public CodingComponent() {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ui/components/codingComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void changeColor() {
        Color c = new Color(random(), random(), random(), 1);
        log.trace("Setting color to {}", c);
        rect.setFill(c);
    }

    private double random() {
        return Math.random();
    }
}
