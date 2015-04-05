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
import java.util.List;
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
    private Map<Coding, Rectangle> openListener = new HashMap<>();

    private void drawPointCoding(Coding coding, int i, int max) {
        log.trace("Drawing rectangle for point coding {}", coding);

        final double x = secondWidthProperty.get() * (coding.getStartMs() / 1000);

        Rectangle rectangle = getRectangle(x, 0, subjectHeightProperty.get(), subjectHeightProperty.get(),
                coding.getSubject().getColor());

        rectangle.widthProperty().bind(rectangle.heightProperty().multiply(0.75));
        rectangle.translateXProperty().bind(rectangle.widthProperty().divide(-2));

        adjustCodingRectangle(rectangle, i, max);

        getChildren().add(rectangle);
        codings.put(coding, rectangle);
        codingRange.addOrUpdate(coding);
    }

    private void drawStateCoding(Coding coding, int i, int max) {
        log.trace("Drawing rectangle for finished state coding {}", coding);

        final double positionStart = secondWidthProperty.get() * (coding.getStartMs() / 1000);
        final double positionEnd = secondWidthProperty.get() * (coding.getDuration() / 1000);

        Rectangle rectangle = getRectangle(positionStart, 0, positionEnd, subjectHeightProperty.get(),
                coding.getSubject().getColor());

        adjustCodingRectangle(rectangle, i, max);

        getChildren().add(rectangle);
        codings.put(coding, rectangle);
        codingRange.addOrUpdate(coding);
    }

    private void startStateCoding(Coding coding, int i, int max) {
        log.trace("Drawing growing rectangle for state coding {}", coding);

        final double positionStart = secondWidthProperty.get() * (coding.getStartMs() / 1000);

        final DoubleBinding width = new AtLeastOneDoubleBinding(secondWidthProperty.multiply(currentTimeProperty).subtract(positionStart));

        Rectangle rectangle = getRectangle(positionStart, 0, 0, subjectHeightProperty.get(),
                coding.getSubject().getColor());
        rectangle.widthProperty().bind(width);

        adjustCodingRectangle(rectangle, i, max);

        getChildren().add(rectangle);
        codings.put(coding, rectangle);
        codingRange.addOrUpdate(coding);
    }

    private Rectangle getRectangle(double x, double y, double width, double height, org.obehave.model.Color color) {
        Rectangle rectangle = new Rectangle(x, y, width, height);
        rectangle.arcHeightProperty().bind(rectangle.heightProperty());
        rectangle.arcWidthProperty().bind(rectangle.heightProperty());

        rectangle.setFill(Color.BEIGE.deriveColor(0, 1, 1, 0.5));
        rectangle.setStroke(color == null ? Color.BLACK : ColorConverter.convertToJavaFX(color));

        return rectangle;
    }

    public void drawCoding(Coding coding) {
        CodingRange.Overlappings overlappings = codingRange.overlappingCodings(coding, (long) (currentTimeProperty.get() * 1000));

        List<Coding> currentOverlappings = overlappings.getCurrentOverlaps();

        for (int i = 0; i < currentOverlappings.size(); i++) {
            adjustCodingRectangle(currentOverlappings.get(i), i, currentOverlappings.size() + 1);
        }

        if (coding.getAction().getType() == Action.Type.POINT) {
            drawPointCoding(coding, currentOverlappings.size(), currentOverlappings.size() + 1);
        } else {
            if (!coding.isOpen()) {
                drawStateCoding(coding, currentOverlappings.size(), currentOverlappings.size() + 1);
            } else {
                startStateCoding(coding, currentOverlappings.size(), currentOverlappings.size() + 1);
            }
        }
    }

    private void adjustCodingRectangle(Coding coding, int position, int size) {
        adjustCodingRectangle(codings.get(coding), position, size);
    }

    private void adjustCodingRectangle(Rectangle r, int position, int size) {
        DoubleBinding height = subjectHeightProperty().divide(size);
        DoubleBinding y = height.multiply(position);

        r.heightProperty().bind(height);
        r.yProperty().bind(y);
    }

    public void endCoding(Coding coding) {
        log.trace("Stopping rectanlge for coding {}", coding);

        final double positionEnd = secondWidthProperty.get() * (coding.getDuration() / 1000);

        final Rectangle codingRectangle = codings.get(coding);
        codingRectangle.widthProperty().unbind();
        codingRectangle.setWidth(positionEnd);
        codingRange.addOrUpdate(coding);
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
