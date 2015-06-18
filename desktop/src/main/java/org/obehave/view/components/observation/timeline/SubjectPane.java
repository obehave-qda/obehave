package org.obehave.view.components.observation.timeline;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.obehave.model.Action;
import org.obehave.model.Coding;
import org.obehave.model.Subject;
import org.obehave.util.CodingArranger;
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

    private CodingArranger codingArranger = new CodingArranger();
    private Map<Coding, Rectangle> codings = new HashMap<>();

    public SubjectPane(DoubleProperty msPlayed) {
        this.msPlayed = msPlayed;
    }

    private void drawPointCoding(Coding coding, Subject subject) {
        final double x = secondWidthProperty.get() * (coding.getStartMs() / 1000);

        Rectangle rectangle = getRectangle(x, 0, subjectHeightProperty.get(), subjectHeightProperty.get(), subject.getColor());
        installRectangleInfo(coding, rectangle);

        rectangle.widthProperty().bind(rectangle.heightProperty().multiply(0.75));
        rectangle.translateXProperty().bind(rectangle.widthProperty().divide(-2));

        getChildren().add(rectangle);
        codings.put(coding, rectangle);
    }

    private void drawStateCoding(Coding coding, Subject subject) {
        final double positionStart = secondWidthProperty.get() * (coding.getStartMs() / 1000);
        final double positionEnd = secondWidthProperty.get() * (coding.getDuration() / 1000);

        Rectangle rectangle = getRectangle(positionStart, 0, positionEnd, subjectHeightProperty.get(), subject.getColor());
        installRectangleInfo(coding, rectangle);

        getChildren().add(rectangle);
        codings.put(coding, rectangle);
    }

    private void startStateCoding(Coding coding, Subject subject) {
        log.trace("Drawing growing rectangle for state coding {}", coding);

        final double positionStart = secondWidthProperty.get() * (coding.getStartMs() / 1000);

        final DoubleBinding width = new MinimumDoubleBinding(secondWidthProperty.multiply(msPlayed.divide(1000)).subtract(positionStart));

        Rectangle rectangle = getRectangle(positionStart, 0, 0, subjectHeightProperty.get(), subject.getColor());
        rectangle.widthProperty().bind(width);
        installRectangleInfo(coding, rectangle);

        getChildren().add(rectangle);
        codings.put(coding, rectangle);
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

    private void installRectangleInfo(Coding coding, Rectangle rectangle) {
        Tooltip t = new Tooltip(coding.getAction().getDisplayString());
        Tooltip.install(rectangle, t);
    }

    public void drawCoding(Coding coding, boolean primary) {
        final Subject subject = primary ? coding.getSubject() : (Subject) coding.getModifier().get();

        if (coding.getAction().getType() == Action.Type.POINT) {
            drawPointCoding(coding, subject);
        } else {
            if (!coding.isOpen()) {
                drawStateCoding(coding, subject);
            } else {
                startStateCoding(coding, subject);
            }
        }

        final int lane = codingArranger.add(coding);

        if (lane >= 0) {
            adjustCodingRectangle(coding, lane, codingArranger.getLaneCount());
        } else {
            adjustAll();
        }
    }

    private void adjustAll() {
        List<List<Coding>> lanes = codingArranger.readjust();
        for (int row = 0; row < lanes.size(); row++) {
            for (Coding c : lanes.get(row)) {
                adjustCodingRectangle(c, row, lanes.size());
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

        adjustAll();
    }

    public DoubleProperty secondWidthProperty() {
        return secondWidthProperty;
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
}
