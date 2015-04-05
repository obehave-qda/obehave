package org.obehave.view.components.observation.coding;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
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
    private DoubleProperty msPlayed = new SimpleDoubleProperty(this, "msPlayed");
    private DoubleProperty subjectHeightProperty = new SimpleDoubleProperty(this, "subjectHeightProperty");

    private CodingRange codingRange = new CodingRange();
    private Map<Coding, Rectangle> codings = new HashMap<>();
    private Map<Coding, ChangeListener<Number>> openListener = new HashMap<>();

    private void drawPointCoding(Coding coding) {
        log.trace("Drawing rectangle for point coding {}", coding);

        final double x = secondWidthProperty.get() * (coding.getStartMs() / 1000);

        Rectangle rectangle = getRectangle(x, 0, subjectHeightProperty.get(), subjectHeightProperty.get(),
                coding.getSubject().getColor());

        rectangle.widthProperty().bind(rectangle.heightProperty().multiply(0.75));
        rectangle.translateXProperty().bind(rectangle.widthProperty().divide(-2));

        getChildren().add(rectangle);
        codings.put(coding, rectangle);
        codingRange.addOrUpdate(coding);
    }

    private void drawStateCoding(Coding coding) {
        log.trace("Drawing rectangle for finished state coding {}", coding);

        final double positionStart = secondWidthProperty.get() * (coding.getStartMs() / 1000);
        final double positionEnd = secondWidthProperty.get() * (coding.getDuration() / 1000);

        Rectangle rectangle = getRectangle(positionStart, 0, positionEnd, subjectHeightProperty.get(),
                coding.getSubject().getColor());

        getChildren().add(rectangle);
        codings.put(coding, rectangle);
        codingRange.addOrUpdate(coding);
    }

    private void startStateCoding(Coding coding) {
        log.trace("Drawing growing rectangle for state coding {}", coding);

        final double positionStart = secondWidthProperty.get() * (coding.getStartMs() / 1000);

        final DoubleBinding width = new MinimumDoubleBinding(secondWidthProperty.multiply(msPlayed.divide(1000)).subtract(positionStart));

        Rectangle rectangle = getRectangle(positionStart, 0, 0, subjectHeightProperty.get(),
                coding.getSubject().getColor());
        rectangle.widthProperty().bind(width);

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
        rectangle.setStrokeWidth(2);

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

        adjustCurrentOverlappingPositions(coding);
        planFutureAdjustments(coding);
    }

    private void adjustCurrentOverlappingPositions(Coding coding) {
        CodingRange.Overlappings overlappings = codingRange.overlappingCodings(coding, (long) (msPlayed.get()));

        List<List<Coding>> lanes = overlappings.arrangeCurrentOverlaps();
        for (int row = 0; row < lanes.size(); row++) {
            for (Coding c : lanes.get(row)) {
                adjustCodingRectangle(c, row, lanes.size());
            }
        }
    }

    private void planFutureAdjustments(Coding coding) {
        CodingRange.Overlappings overlappings = codingRange.overlappingCodings(coding, (long) (msPlayed.get()));

        final List<Coding> futureOverlappings = overlappings.getFutureOverlaps();

        for (int i = 0; i < futureOverlappings.size(); i++) {
            final Coding futureOverlapping = futureOverlappings.get(i);

            final OneTimeFutureAdjuster adjuster = new OneTimeFutureAdjuster(futureOverlapping);

            final ChangeListener<Number> adjustListener = (observable, oldValue, newValue) -> {
                if (newValue.longValue() * 1000 >= futureOverlapping.getStartMs()) {
                    adjuster.adjust();
                }
            };

            openListener.put(futureOverlapping, adjustListener);
            msPlayed.addListener(adjustListener);
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

    public DoubleProperty msPlayed() {
        return msPlayed;
    }

    public DoubleProperty subjectHeightProperty() {
        return subjectHeightProperty;
    }

    private static class MinimumDoubleBinding extends DoubleBinding {
        private static final double MIN = 5;
        private final DoubleBinding binding;

        public MinimumDoubleBinding(DoubleBinding binding) {
            this.binding = binding;
            binding.addListener(observable -> invalidate());
        }

        @Override
        protected double computeValue() {
            final double bindingValue = binding.get();
            if (bindingValue <= MIN) {
                return MIN;
            } else {
                return bindingValue;
            }
        }
    }

    private class OneTimeFutureAdjuster {
        private final Coding coding;

        private boolean adjusted = false;

        public OneTimeFutureAdjuster(Coding coding) {
            this.coding = coding;
        }

        public void adjust() {
            if (!adjusted) {
                log.trace("Adjusting {}", coding);
                adjustCurrentOverlappingPositions(coding);

                final ChangeListener<Number> listener = openListener.get(coding);
                if (listener != null) {
                    log.trace("Found {} listeners; removing {}", openListener.size(), listener);
                    msPlayed.removeListener(listener);
                }

                adjusted = true;
            }
        }
    }
}
