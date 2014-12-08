package org.obehave.view.controller.components.coding;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// == Weekpane
public class TimelinePane extends Pane {
    private static final Logger log = LoggerFactory.getLogger(TimelinePane.class);

    private DoubleProperty msProperty = new SimpleDoubleProperty(this, "msProperty");
    private DoubleProperty secondWidthProperty = new SimpleDoubleProperty(this, "secondWidthProperty");
    private DoubleProperty subjectHeightProperty = new SimpleDoubleProperty(this, "subjectHeightProperty");
    private DoubleProperty timelineHeightProperty = new SimpleDoubleProperty(this, "timelineHeightProperty");


    public TimelinePane() {
        getStyleClass().add("TimelinePane");

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

        final int interval = 5;

        for (int second = 0; second < msProperty.doubleValue() / 1000; second += interval) {
            if (second % (60 / interval) == 0) {
                Text secondLabel = new Text(String.valueOf(second));
                secondLabel.xProperty().bind(secondWidthProperty.multiply(second));
                secondLabel.yProperty().bind(heightProperty().subtract(5));
                secondLabel.setTranslateX(-(secondLabel.getBoundsInParent().getWidth() / 2));
                secondLabel.getStyleClass().add("secondLabel");
                secondLabel.setFontSmoothingType(FontSmoothingType.LCD);
                getChildren().add(secondLabel);
                log.trace("Added text label {}", secondLabel);
            }
        }
    }
}
