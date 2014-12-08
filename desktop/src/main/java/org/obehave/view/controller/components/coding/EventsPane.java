package org.obehave.view.controller.components.coding;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import org.obehave.model.Subject;
import org.obehave.view.NodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The EventsPane contains a list of SubjectPanes, to draw their events correclty
 */
public class EventsPane extends Pane {
    private static final Logger log = LoggerFactory.getLogger(EventsPane.class);

    private final List<SubjectPane> subjectPanes = new ArrayList<>();
    private final IntegerProperty subjectPanesSize = new SimpleIntegerProperty(this, "subjectPanesSize");

    private DoubleProperty msProperty = new SimpleDoubleProperty(this, "msProperty");
    private DoubleProperty secondWidthProperty = new SimpleDoubleProperty(this, "secondWidthProperty");
    private DoubleProperty subjectHeightProperty = new SimpleDoubleProperty(this, "subjectHeightProperty");

    public EventsPane() {
        getStyleClass().add("TimelinePane");

        prefHeightProperty().bind(subjectHeightProperty().multiply(subjectPanesSize));

        msProperty.addListener((observable, oldValue, newValue) -> refresh());
    }

    public DoubleProperty msProperty() {
        return msProperty;
    }

    public DoubleProperty secondWidthProperty() {
        return secondWidthProperty;
    }

    public DoubleProperty subjectHeightProperty() {
        return subjectHeightProperty;
    }

    public void addSubjectPane(Subject subject) {
        SubjectPane pane = new SubjectPane(subject);
        int currentSubjectPanes = subjectPanes.size();

        pane.setId("subjectPane" + currentSubjectPanes);

        pane.layoutXProperty().set(0);
        pane.layoutYProperty().bind(subjectHeightProperty.multiply(currentSubjectPanes));
        pane.prefHeightProperty().bind(subjectHeightProperty);

        getChildren().add(pane);
        subjectPanes.add(pane);
        subjectPanesSize.setValue(subjectPanes.size());
    }

    public void refresh() {
        getChildren().clear();

        final int interval = 5;
        // lines
        for (int second = 0; second < msProperty.doubleValue() / 1000; second += interval) {
            if (second % (60 / interval) == 0) {
                // a line every 60*interval seconds (60)
                Line line = new Line(0, 10, 100, 10);
                line.setId("minuteline" + second);
                line.getStyleClass().add("minuteline");

                line.startXProperty().bind(NodeUtil.snapXY(secondWidthProperty.multiply(second)));
                line.endXProperty().bind(NodeUtil.snapXY(line.startXProperty()));

                line.startYProperty().setValue(0);
                line.endYProperty().bind(heightProperty());

                log.trace("Created minute line {}", line);

                getChildren().add(line);
            } else if (second % (15 / interval) == 0) {
                // a line every 3*interval seconds (15)
                Line line = new Line(0, 10, 100, 10);
                line.setId("fifteensecondsline" + second);
                line.getStyleClass().add("fifteensecondsline");

                line.startXProperty().bind(NodeUtil.snapXY(secondWidthProperty.multiply(second)));
                line.endXProperty().bind(NodeUtil.snapXY(line.startXProperty()));

                line.startYProperty().setValue(0);
                line.endYProperty().bind(heightProperty());

                log.trace("Created 15 seconds line {}", line);

                getChildren().add(line);
            } else {
                // a line every interval seconds (normally: 5)
                Line line = new Line(0, 10, 100, 10);
                line.setId("fivesecondsline" + second);
                line.getStyleClass().add("fivesecondsline");

                line.startXProperty().bind(NodeUtil.snapXY(secondWidthProperty.multiply(second)));
                line.endXProperty().bind(NodeUtil.snapXY(line.startXProperty()));

                line.startYProperty().setValue(0);
                line.endYProperty().bind(heightProperty());

                log.trace("Created 5 seconds line {}", line);

                getChildren().add(line);
            }
        }
    }
}
