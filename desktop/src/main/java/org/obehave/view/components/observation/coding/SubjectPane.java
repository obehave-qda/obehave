package org.obehave.view.components.observation.coding;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.obehave.model.Action;
import org.obehave.model.Coding;
import org.obehave.util.CodingRange;
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

    private DoubleProperty secondWidthProperty = new SimpleDoubleProperty(this, "secondWidthProperty");
    private DoubleProperty currentTimeProperty = new SimpleDoubleProperty(this, "currentTimeProperty");
    private DoubleProperty subjectHeightProperty = new SimpleDoubleProperty(this, "subjectHeightProperty");

    private CodingRange codingRange = new CodingRange();
    private Map<Coding, Rectangle> codings = new HashMap<>();

    private void drawPointCoding(Coding coding) {
        log.trace("Drawing rectangle for point coding {}", coding);

        final double position = secondWidthProperty.get() * (coding.getStartMs() / 1000);
        final double x = position - subjectHeightProperty.get() / 2;

        Rectangle rectangle = getRectangle(x, 0, subjectHeightProperty.get(), subjectHeightProperty.get(),
                coding.getSubject().getColor());

        getChildren().add(rectangle);
        codings.put(coding, rectangle);
    }

    private void drawStateCoding(Coding coding) {
        log.trace("Drawing rectangle for finished state coding {}", coding);

        final double positionStart = secondWidthProperty.get() * (coding.getStartMs() / 1000);
        final double positionEnd = secondWidthProperty.get() * (coding.getDuration() / 1000);

        Rectangle rectangle = getRectangle(positionStart, 0, positionEnd, subjectHeightProperty.get(),
                coding.getSubject().getColor());

        getChildren().add(rectangle);
        codings.put(coding, rectangle);
    }

    private void startStateCoding(Coding coding) {
        log.trace("Drawing growing rectangle for state coding {}", coding);

        final double positionStart = secondWidthProperty.get() * (coding.getStartMs() / 1000);

        final DoubleBinding width = new AtLeastOneDoubleBinding(secondWidthProperty.multiply(currentTimeProperty).subtract(positionStart));

        Rectangle rectangle = getRectangle(positionStart, 0, 0, subjectHeightProperty.get(),
                coding.getSubject().getColor());
        rectangle.widthProperty().bind(width);

        getChildren().add(rectangle);

        codings.put(coding, rectangle);
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
            if (!coding.isOpen()) {
                drawStateCoding(coding);
            } else {
                startStateCoding(coding);
            }
        }
    }

    public void endCoding(Coding coding) {
        log.trace("Stopping rectanlge for coding {}", coding);

        final double positionEnd = secondWidthProperty.get() * (coding.getDuration() / 1000);

        final Rectangle codingRectangle = codings.get(coding);
        codingRectangle.widthProperty().unbind();
        codingRectangle.setWidth(positionEnd);
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

    private int getCodingsAtTime(long ms) {
        int codingsAtTime = 0;

        for (Coding c : codings.keySet()) {
            if (!c.isStateCoding()) {

            }
        }

        return codingsAtTime;
    }

    private static class AtLeastOneDoubleBinding extends DoubleBinding {
        private final DoubleBinding binding;

        public AtLeastOneDoubleBinding(DoubleBinding binding) {
            this.binding = binding;
            binding.addListener(observable -> invalidate());
        }

        @Override
        protected double computeValue() {
            final double bindingValue = binding.get();
            if (bindingValue <= 1) {
                return 1;
            } else {
                return bindingValue;
            }
        }
    }
}
