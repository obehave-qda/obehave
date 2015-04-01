package org.obehave.view.components.observation.coding;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.obehave.model.Action;
import org.obehave.model.Coding;
import org.obehave.model.Subject;
import org.obehave.view.util.ColorConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A SubjectPane draws every single event per subject on the timeline
 */
public class SubjectPane extends Pane {
    private static final Logger log = LoggerFactory.getLogger(SubjectPane.class);

    private final Subject subject;
    private DoubleProperty secondWidthProperty = new SimpleDoubleProperty(this, "secondWidthProperty");
    private DoubleProperty currentTimeProperty = new SimpleDoubleProperty(this, "currentTimeProperty");
    private DoubleProperty subjectHeightProperty = new SimpleDoubleProperty(this, "subjectHeightProperty");

    private Map<Coding, Rectangle> openCodings = new HashMap<>();

    public SubjectPane(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }

    private void drawPointCoding(Coding coding) {
        final double position = secondWidthProperty.get() * (coding.getStartMs() / 1000);
        final double x = position - subjectHeightProperty.get() / 2;

        Rectangle rectangle = getRectangle(x, 0, subjectHeightProperty.get(), subjectHeightProperty.get(),
                coding.getSubject().getColor());

        getChildren().add(rectangle);
    }

    private void drawStateCoding(Coding coding) {
        final double positionStart = secondWidthProperty.get() * (coding.getStartMs() / 1000);
        final double positionEnd = secondWidthProperty.get() * (coding.getDuration() / 1000);

        Rectangle rectangle = getRectangle(positionStart, 0, subjectHeightProperty.get(), positionEnd,
                coding.getSubject().getColor());

        getChildren().add(rectangle);
    }

    private void startStateCoding(Coding coding) {
        final double positionStart = secondWidthProperty.get() * (coding.getStartMs() / 1000);
        final DoubleBinding positionEnd = secondWidthProperty.multiply(currentTimeProperty);

        Rectangle rectangle = getRectangle(positionStart, 0, subjectHeightProperty.get(), positionEnd.get(),
                coding.getSubject().getColor());
        rectangle.widthProperty().bind(positionEnd);

        getChildren().add(rectangle);

        openCodings.put(coding, rectangle);
    }

    private Rectangle getRectangle(double x, double y, double width, double height, org.obehave.model.Color color) {
        Rectangle rectangle = new Rectangle(x, y, width, height);
        rectangle.setArcHeight(subjectHeightProperty().get());
        rectangle.setArcWidth(subjectHeightProperty().get());
        rectangle.setFill(color == null ? Color.BLACK : ColorConverter.convertToJavaFX(color));

        return rectangle;
    }

    public void drawCoding(Coding coding) {
        if (coding.getAction().getType() == Action.Type.POINT) {
            drawPointCoding(coding);
        } else {
            if (!coding.isRunning()) {
                drawStateCoding(coding);
            } else {
                startStateCoding(coding);
            }
        }
    }

    public void endCoding(Coding coding) {
        final double positionEnd = secondWidthProperty.get() * (coding.getDuration() / 1000);

        openCodings.get(coding).setWidth(positionEnd);
    }

    public DoubleProperty secondWidthProperty() {
        return secondWidthProperty;
    }

    public DoubleProperty currentTimeProperty() {
        return currentTimeProperty;
    }

    public DoubleProperty subjectHeightProperty() {
        return subjectHeightProperty;
    }
}
