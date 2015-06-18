package org.obehave.view.components.observation.timeline;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

// == Weekpane
public class TimelinePane extends Pane {
    private static final PeriodFormatter FORMATTER = new PeriodFormatterBuilder().
            appendMinutes().appendSuffix("m").appendSeconds().appendSuffix("s").toFormatter();

    private DoubleProperty msProperty = new SimpleDoubleProperty(this, "msProperty");
    private DoubleProperty secondWidthProperty = new SimpleDoubleProperty(this, "secondWidthProperty");
    private DoubleProperty subjectHeightProperty = new SimpleDoubleProperty(this, "subjectHeightProperty");
    private DoubleProperty timelineHeightProperty = new SimpleDoubleProperty(this, "timelineHeightProperty");


    public TimelinePane() {
        getStyleClass().add("timelinepane");

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

    public DoubleProperty timelineHeightProperty() {
        return timelineHeightProperty;
    }

    public void refresh() {
        getChildren().clear();

        final long everyMs = 5 * 1000;

        for (long ms = 0; ms < msProperty.doubleValue(); ms += everyMs) {
            Text secondLabel = new Text(FORMATTER.print(Duration.millis(ms).toPeriod()));
            secondLabel.xProperty().bind(secondWidthProperty.multiply(ms / 1000));
            secondLabel.yProperty().bind(heightProperty().subtract(5));

            // don't translate first label
            if (ms != 0) {
                secondLabel.setTranslateX(-(secondLabel.getBoundsInParent().getWidth() / 2));
            }

            secondLabel.getStyleClass().add("secondLabel");
            secondLabel.setFontSmoothingType(FontSmoothingType.LCD);
            getChildren().add(secondLabel);
        }
    }
}
