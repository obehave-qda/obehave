package org.obehave.view.components.observation;

import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.textfield.TextFields;
import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.model.Observation;
import org.obehave.service.Study;
import org.obehave.view.components.observation.coding.CodingControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ObservationControl extends BorderPane {
    private final Logger log = LoggerFactory.getLogger(ObservationControl.class);

    private Study study;
    private Observation observation;

    @FXML
    private VideoControl videoControl;
    @FXML
    private CodingControl codingControl;

    @FXML
    private TextField inputSubject;
    @FXML
    private TextField inputAction;
    @FXML
    private TextField inputModifier;

    public ObservationControl() {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("org/obehave/view/components/observation/observationControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        EventBusHolder.register(this);

        videoControl.maxHeightProperty().bind(heightProperty().divide(1.5));
        codingControl.maxHeightProperty().bind(heightProperty().divide(3));

        TextFields.bindAutoCompletion(inputSubject,
                p -> (study.getSuggestionServiceBuilder().build(observation).getSubjectSuggestions(p.getUserText())))
                .setOnAutoCompleted(e -> inputAction.requestFocus());
        TextFields.bindAutoCompletion(inputAction,
                p -> (study.getSuggestionServiceBuilder().build(observation).getActionSuggestions(p.getUserText())))
                .setOnAutoCompleted(e -> inputModifier.requestFocus());
        TextFields.bindAutoCompletion(inputModifier,
                p -> (study.getSuggestionServiceBuilder().build(observation).getModifierSuggestions(inputAction.getText(), p.getUserText())));
    }

    public void loadVideo(File video) {
        videoControl.loadVideo(video);
    }

    @Subscribe
    public void loadObservation(UiEvent.LoadObservation event) {
        log.debug("Loading observation, because of {}", event);

        observation = event.getObservation();
        if (observation.getVideo() != null) {
            loadVideo(observation.getVideo());

            videoControl.currentTime().addListener((observable, oldValue, newValue) -> {
                codingControl.currentTime().setValue(newValue.toSeconds());
            });
        }

        codingControl.loadObservation(observation);
    }

    public void setStudy(Study study) {
        this.study = study;
    }
}
