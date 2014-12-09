package org.obehave.view.controller.components.coding;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jfxtras.util.NodeUtil;
import org.obehave.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A SubjectPane draws every single event per subject on the timeline
 */
public class SubjectPane extends Pane {
    private static final Logger log = LoggerFactory.getLogger(SubjectPane.class);

    private Rectangle resizeRectangle;
    private Subject subject;
    private boolean rectangleToRight = true;

    public SubjectPane(Subject subject) {
        this.subject = subject;

        addEventHandlers();
    }

    public Subject getSubject() {
        return subject;
    }

    private Color randomColor() {
        return new Color(Math.random(), Math.random(), Math.random(), 1);
    }

    private void addEventHandlers() {
        setOnMousePressed((mouseEvent) -> {
            // show the rectangle
            setCursor(Cursor.H_RESIZE);
            double rectX = mouseEvent.getScreenX() - NodeUtil.screenX(this);
            resizeRectangle = new Rectangle(rectX, 0, 10, heightProperty().get());
            resizeRectangle.setArcHeight(heightProperty().get());
            resizeRectangle.setArcWidth(heightProperty().get());
            resizeRectangle.setFill(randomColor());
            getChildren().add(resizeRectangle);

            // this event should not be processed by the appointment area
            mouseEvent.consume();
        });
        // visualize resize
        setOnMouseDragged((mouseEvent) -> {
            if (resizeRectangle == null) {
                return;
            }

            // calculate pixels between left rectangle border and mouse
            double lWidth = mouseEvent.getScreenX() - NodeUtil.screenX(resizeRectangle);
            if (!rectangleToRight) {
                // i don't know exactly why we have to do this here, but it works
                lWidth -= resizeRectangle.getWidth();
            }

            if (lWidth >= 0) {
                rectangleToRight = true;
                resizeRectangle.setWidth(lWidth);
                resizeRectangle.setTranslateX(0);
            } else {
                rectangleToRight = false;
                resizeRectangle.setWidth(lWidth * -1);
                resizeRectangle.setTranslateX(lWidth);
            }

            mouseEvent.consume();
        });
        // end resize
        setOnMouseReleased((mouseEvent) -> {
            if (resizeRectangle == null) {
                return;
            }

            log.debug("Painted rectangle {}", resizeRectangle);
            mouseEvent.consume();
            setCursor(Cursor.HAND);
        });
    }
}
