package org.obehave.view.components.observation.timeline;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main class for the whole coding component.
 * <p/>
 * A coding component consists of:
 * <ul>
 * <li>a {@link EventsPane}: visualize the actual codings per subject on a timeline</li>
 * <li>a {@link SubjectsList}: a list of all available subjects for coding</li>
 * <li>a {@link TimelinePane}: a simple timeline, labeled per second / minute</li>
 * </ul>
 */
public class CodingControl extends ScrollPane implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(CodingControl.class);

    private DoubleProperty subjectHeightProperty = new SimpleDoubleProperty(this, "subjectHeightProperty", 50);
    private DoubleProperty timelineHeightProperty = new SimpleDoubleProperty(this, "timelineHeightProperty", 30);
    private DoubleProperty secondWithProperty = new SimpleDoubleProperty(this, "secondWithProperty", 15);

    private DoubleProperty lengthMs = new SimpleDoubleProperty(this, "lengthMs", 180 * 1000);

    /**
     * A property containing the current visible x value of the view port
     */
    private DoubleProperty viewPortX = new SimpleDoubleProperty();

    /**
     * A property containing the current visible y value of the view port
     */
    private DoubleProperty viewPortY = new SimpleDoubleProperty();

    @FXML
    private TimelinePane timelinePane;

    @FXML
    private SubjectsList subjectsList;

    @FXML
    private EventsPane eventsPane;

    @FXML
    private Pane codingPane;

    @FXML
    private Rectangle cover;

    public CodingControl() {
        super();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("org/obehave/view/components/observation/timeline/codingControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ********* configuring eventsPane
        // *** eventsPane: properties
        eventsPane.subjectHeightProperty().bind(subjectHeightProperty);
        eventsPane.secondWidthProperty().bind(secondWithProperty);
        eventsPane.msProperty().bind(lengthMs);

        // *** eventsPane: layout
        eventsPane.toBack();
        eventsPane.layoutXProperty().bind(subjectsList.widthProperty());
        eventsPane.setLayoutY(0);


        // ********* configuring subjectsList
        // *** subjectsList: properties
        subjectsList.subjectHeightProperty().bind(subjectHeightProperty);

        // *** subjectsList: layout
        subjectsList.toFront();
        subjectsList.setLayoutX(0);
        subjectsList.setLayoutY(0);


        // ********* configuring timelinePane
        // *** timelinePane: properties
        timelinePane.subjectHeightProperty().bind(subjectHeightProperty);
        timelinePane.secondWidthProperty().bind(secondWithProperty);
        timelinePane.timelineHeightProperty().bind(timelineHeightProperty);
        timelinePane.msProperty().bind(lengthMs);

        // *** timelinePane: layout
        timelinePane.toFront();
        timelinePane.layoutXProperty().bind(subjectsList.widthProperty());


        // ********* configuring cover
        // *** cover: properties
        cover.widthProperty().bind(subjectsList.widthProperty());
        cover.heightProperty().bind(timelinePane.heightProperty());

        // *** cover: layout
        cover.toFront();
        cover.setLayoutX(0);
        cover.setLayoutY(0);

        // ********* configuring scroll bindings
        viewPortX.bind(hvalueProperty().multiply(codingPane.widthProperty().subtract(new BoundsProperties.ScrollPaneViewPortWidthBinding(this))));
        viewPortY.bind(vvalueProperty().multiply(heightProperty().subtract(new BoundsProperties.ScrollPaneViewPortHeightBinding(this))));

        // make subjectsList fixed when scrolling horizontal
        viewPortX.addListener((observable, oldValue, newValue) -> {
            subjectsList.layoutXProperty().set(newValue.doubleValue());
            cover.layoutXProperty().set(newValue.doubleValue());
        });

        // make timelinePane fixed when scrolling vertical - we have to do some shit do avoid an infinite growing of the pane. Whatever.
        viewPortY.addListener((observable, oldValue, newValue) -> {
            double newY = heightProperty().add(newValue.doubleValue()).subtract(timelinePane.heightProperty()).doubleValue();

            // would the new y value increase the overall size of the pane? Actually, we don't want that. Except it's the initial positioning!
            if (newY + timelinePane.heightProperty().doubleValue() > codingPane.heightProperty().doubleValue() && codingPane.heightProperty().get() != 0) {
                newY = codingPane.heightProperty().subtract(timelinePane.heightProperty()).doubleValue();
            }

            timelinePane.layoutYProperty().set(newY);
            cover.layoutYProperty().set(newY);
        });
    }

    public void addSubject(Subject subject) {
        eventsPane.addSubject(subject);
        subjectsList.addSubject(subject);
    }

    public void removeSubject(Subject subject) {
        eventsPane.removeSubject(subject);
        subjectsList.removeSubject(subject);
    }

    public void clear() {
        eventsPane.clear();
        subjectsList.clear();
    }

    public void loadObservation(Observation observation) {
        log.trace("Loading observation {}", observation);
        clear();

        observation.getParticipatingSubjects().forEach(this::addSubject);
        observation.getCodings().forEach(eventsPane::addCoding);
    }

    public DoubleProperty lengthMsProperty() {
        return lengthMs;
    }

    public void setMsPlayed(DoubleProperty msPlayed) {
        eventsPane.setMsPlayed(msPlayed);
    }
}
