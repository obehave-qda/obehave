package org.obehave.view.controller.components;

import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import org.obehave.events.EventBusHolder;
import org.obehave.events.LoadObservationEvent;
import org.obehave.view.controller.components.coding.CodingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author Markus Möslinger
 */
public class ObservationController extends BorderPane {
    private final Logger log = LoggerFactory.getLogger(ObservationController.class);

    @FXML
    private VideoComponent videoComponent;
    @FXML
    private CodingController codingController;

    public ObservationController() {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ui/components/observationComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        EventBusHolder.register(this);

        videoComponent.maxHeightProperty().bind(heightProperty().divide(1.5));
        codingController.maxHeightProperty().bind(heightProperty().divide(3));
    }

    public void loadVideo(File video) {
        videoComponent.loadFile(video);
    }

    @Subscribe
    public void loadObservation(LoadObservationEvent event) {
        log.debug("Loading observation, because of {}", event);
    }
}
