package org.obehave.view.controller.components.coding;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableDoubleProperty;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.obehave.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CodingComponent extends BorderPane implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(CodingComponent.class);

    private DoubleProperty subjectHeightProperty = new SimpleDoubleProperty(this, "subjectHeightProperty");
    private DoubleProperty timelineHeightProperty = new SimpleDoubleProperty(this, "timelineHeightProperty");
    private DoubleProperty secondWithProperty = new SimpleDoubleProperty(this, "secondWithProperty");

    @FXML
    private TimelinePane timelinePane;

    @FXML
    private SubjectsList subjectsList;

    @FXML
    private EventsPane eventsPane;

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


        subjectHeightProperty.setValue(50);
        secondWithProperty.setValue(15);
        timelineHeightProperty.setValue(30);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventsPane.subjectHeightProperty().bind(subjectHeightProperty);
        eventsPane.secondWidthProperty().bind(secondWithProperty);

        timelinePane.subjectHeightProperty().bind(subjectHeightProperty);
        timelinePane.secondWidthProperty().bind(secondWithProperty);
        timelinePane.timelineHeightProperty().bind(timelineHeightProperty);

        subjectsList.subjectHeightProperty().bind(subjectHeightProperty);
        subjectsList.timelineHeightProperty().bind(timelineHeightProperty);


        Subject hans = new Subject("Hans");
        eventsPane.addSubjectPane(hans);
        subjectsList.addSubject(hans);
        Subject huns = new Subject("Huns");
        eventsPane.addSubjectPane(huns);
        subjectsList.addSubject(huns);
        Subject hins = new Subject("Hins");
        eventsPane.addSubjectPane(hins);
        subjectsList.addSubject(hins);

        timelinePane.msProperty().setValue(180 * 1000);
        eventsPane.msProperty().setValue(180 * 1000);
    }
}
