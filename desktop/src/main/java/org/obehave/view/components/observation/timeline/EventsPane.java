package org.obehave.view.components.observation.timeline;

import com.google.common.eventbus.Subscribe;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.model.Coding;
import org.obehave.model.Subject;
import org.obehave.model.modifier.Modifier;
import org.obehave.view.util.NodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * The EventsPane contains a list of SubjectPanes, to draw their events correclty
 */
public class EventsPane extends Pane {
    private static final Logger log = LoggerFactory.getLogger(EventsPane.class);

    private final Map<Subject, SubjectPane> subjectPanes = new HashMap<>();
    private final IntegerProperty subjectPanesSize = new SimpleIntegerProperty(this, "subjectPanesSize");

    private DoubleProperty msProperty = new SimpleDoubleProperty(this, "msProperty");
    private DoubleProperty secondWidthProperty = new SimpleDoubleProperty(this, "secondWidthProperty");
    private DoubleProperty subjectHeightProperty = new SimpleDoubleProperty(this, "subjectHeightProperty");

    private DoubleProperty subjectListWidthProperty = new SimpleDoubleProperty(this, "subjectListWidthProperty");

    private DoubleProperty msPlayed = new SimpleDoubleProperty(this, "msPlayed");

    private Line secondsLine = new Line();

    public EventsPane() {
        EventBusHolder.register(this);

        prefHeightProperty().bind(subjectHeightProperty().multiply(subjectPanesSize).add(subjectHeightProperty().divide(2)));

        msProperty.addListener((observable, oldValue, newValue) -> refresh());

        secondsLine.endXProperty().bind(NodeUtil.snapXY(secondsLine.startXProperty()));

        secondsLine.startYProperty().setValue(0);
        secondsLine.endYProperty().bind(NodeUtil.snapXY(prefHeightProperty()));

        getChildren().add(secondsLine);
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
        SubjectPane pane = new SubjectPane(msPlayed);
        int currentSubjectPanes = subjectPanes.size();

        pane.setId("subjectPane" + currentSubjectPanes);

        pane.secondWidthProperty().bind(secondWidthProperty);

        pane.layoutXProperty().set(0);
        pane.layoutYProperty().bind(subjectHeightProperty.multiply(currentSubjectPanes));
        pane.subjectHeightProperty().bind(subjectHeightProperty);
        pane.prefWidthProperty().bind(widthProperty());

        getChildren().add(pane);
        subjectPanes.put(subject, pane);
        subjectPanesSize.setValue(subjectPanes.size());
    }

    public void removeSubject(Subject subject) {
        subjectPanes.remove(subject);

        subjectPanesSize.setValue(subjectPanes.size());
    }

    public void clear() {
        log.trace("Clearing pane");

        subjectPanes.values().forEach(sp -> getChildren().remove(sp));
        subjectPanes.clear();

        subjectPanesSize.setValue(subjectPanes.size());
    }

    public void addCoding(Coding coding) {
        log.trace("Adding coding to pane: {}", coding);

        final Subject subject = coding.getSubject();
        final SubjectPane subjectPane = subjectPanes.get(subject);

        if (subjectPane != null) {
            subjectPane.drawCoding(coding, true);
        } else {
            log.warn("Couldn't find a subject pane for subject {} - is it participating in this observation?", subject);
        }

        if (coding.getModifier() != null && coding.getModifier().getType() == Modifier.Type.SUBJECT_MODIFIER) {
            final SubjectPane modifierSubject = subjectPanes.get((Subject) coding.getModifier().get());

            if (modifierSubject != null) {
                modifierSubject.drawCoding(coding, false);
            } else {
                log.warn("Couldn't find a subject pane for subject {} - is it participating in this observation?", subject);
            }
        }
    }

    public void stopCoding(Coding coding) {
        log.trace("Ending coding: {}", coding);

        final Subject subject = coding.getSubject();
        final SubjectPane subjectPane = subjectPanes.get(subject);

        if (subjectPane != null) {
            subjectPane.endCoding(coding);
        } else {
            log.warn("Couldn't find a subject pane for subject {} - is it participating in this observation?", subject);
        }
    }

    @Subscribe
    public void newCodingSubscriber(UiEvent.NewCoding event) {
        addCoding(event.getCoding());
    }

    @Subscribe
    public void finishedCodingSubscriber(UiEvent.FinishedCoding event) {
        stopCoding(event.getCoding());
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

    public void setMsPlayed(DoubleProperty msPlayed) {
        this.msPlayed = msPlayed;

        secondsLine.startXProperty().bind(
                NodeUtil.snapXY(secondWidthProperty.multiply(msPlayed.divide(1000)).add(subjectListWidthProperty)));
    }
}
