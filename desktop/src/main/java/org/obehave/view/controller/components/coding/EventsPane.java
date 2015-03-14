package org.obehave.view.controller.components.coding;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import org.obehave.model.Subject;
import org.obehave.view.util.NodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
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

    private DoubleProperty subjectListWidthProperty = new SimpleDoubleProperty(this, "subjectListWidthProperty");

    public EventsPane() {
        prefHeightProperty().bind(subjectHeightProperty().multiply(subjectPanesSize).add(subjectHeightProperty().divide(2)));

        msProperty.addListener((observable, oldValue, newValue) -> refresh());
    }

    public DoubleProperty msProperty() {
        return msProperty;
    }

    public DoubleProperty secondWidthProperty() {
        return secondWidthProperty;
    }

    public DoubleProperty subjectListWidthProperty() {
        return subjectListWidthProperty;
    }

    public DoubleProperty subjectHeightProperty() {
        return subjectHeightProperty;
    }

    public void addSubject(Subject subject) {
        SubjectPane pane = new SubjectPane(subject);
        int currentSubjectPanes = subjectPanes.size();

        pane.setId("subjectPane" + currentSubjectPanes);

        pane.layoutXProperty().set(0);
        pane.layoutYProperty().bind(subjectHeightProperty.multiply(currentSubjectPanes));
        pane.prefHeightProperty().bind(subjectHeightProperty);
        pane.prefWidthProperty().bind(widthProperty());

        getChildren().add(pane);
        subjectPanes.add(pane);
        subjectPanesSize.setValue(subjectPanes.size());
    }

    public void removeSubject(Subject subject) {
        Iterator<SubjectPane> iter = subjectPanes.iterator();
        while (iter.hasNext()) {
            SubjectPane pane = iter.next();
            if (pane.getSubject().equals(subject)) {
                getChildren().remove(pane);
                iter.remove();
                break;
            }
        }
        subjectPanesSize.setValue(subjectPanes.size());
    }

    public void refresh() {
        final int interval = 5;

        for (int second = 0; second < msProperty.doubleValue() / 1000; second += interval) {
            Line line;
            if (second % (60 / interval) == 0) {
                // a line every 60*interval seconds (60)
                line = createLine("minuteline", second);
            } else if (second % (15 / interval) == 0) {
                // a line every 3*interval seconds (15)
                line = createLine("fifteensecondsline", second);
            } else {
                // a line every interval seconds (normally: 5)
                line = createLine("fivesecondsline", second);
            }
            getChildren().add(line);
        }
    }

    private Line createLine(String className, int nth) {
        Line line = new Line(0, 10, 100, 10);
        line.setId(className + nth);
        line.getStyleClass().add(className);

        line.startXProperty().bind(NodeUtil.snapXY(secondWidthProperty.multiply(nth).add(subjectListWidthProperty)));
        line.endXProperty().bind(NodeUtil.snapXY(line.startXProperty()));

        line.startYProperty().setValue(0);
        line.endYProperty().bind(NodeUtil.snapXY(prefHeightProperty()));

        return line;
    }
}
