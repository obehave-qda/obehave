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
    private boolean dragged;
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

            dragged = false;
        });
        // visualize resize
        setOnMouseDragged((mouseEvent) -> {
            if (resizeRectangle == null) {
                return;
            }

            // - calculate the number of pixels from onscreen nodeY (layoutY) to onscreen mouseY
            double lWidth = mouseEvent.getScreenX() - NodeUtil.screenX(resizeRectangle);
            if (!rectangleToRight) {
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

            // no one else
            mouseEvent.consume();
            dragged = true;
        });
        // end resize
        setOnMouseReleased((mouseEvent) -> {
            if (resizeRectangle == null) {
                return;
            }

            // no one else
            mouseEvent.consume();

            // reset ui
            setCursor(Cursor.HAND);
            //getChildren().remove(resizeRectangle);

            // must have dragged (otherwise it is considered an "unselect all" action)
            if (dragged == false) {
                return;
            }
        });
    }
}
