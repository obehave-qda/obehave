package org.obehave.view.components.observation;

import com.google.common.eventbus.Subscribe;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.textfield.TextFields;
import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.service.ActionService;
import org.obehave.service.CodingService;
import org.obehave.service.Study;
import org.obehave.view.components.observation.coding.CodingControl;
import org.obehave.view.util.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ObservationControl extends BorderPane {
    private final Logger log = LoggerFactory.getLogger(ObservationControl.class);

    private Study study;
    private Observation observation;

    private CodingService codingService;
    private ActionService actionService;

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

    private DoubleProperty currentTimeProperty = new SimpleDoubleProperty(this, "currentTimeProperty");

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
                p -> (study.getSuggestionServiceBuilder().build(observation).getSubjectSuggestions(p.getUserText(), isEndCodingMode())))
                .setOnAutoCompleted(e -> inputAction.requestFocus());
        TextFields.bindAutoCompletion(inputAction,
                p -> (study.getSuggestionServiceBuilder().build(observation).getActionSuggestions(p.getUserText(), isEndCodingMode(), inputSubject.getText())))
                .setOnAutoCompleted(e -> inputModifier.requestFocus());
        TextFields.bindAutoCompletion(inputModifier,
                p -> (study.getSuggestionServiceBuilder().build(observation).getModifierSuggestions(inputAction.getText(), p.getUserText())));

        inputAction.textProperty().addListener((observable, oldValue, newValue) -> handleActionValue(newValue));
        inputModifier.setText("No valid action entered");
        inputModifier.setDisable(true);
    }

    public void loadVideo(File video) {
        videoControl.loadVideo(video);
    }

    @Subscribe
    public void loadObservation(UiEvent.LoadObservation event) {
        log.debug("Loading observation, because of {}", event);

        observation = event.getObservation();
        codingService = study.getCodingServiceBuilder().build(observation);

        if (observation.getVideo() != null) {
            loadVideo(observation.getVideo());

            // FIXME there is some stupid bug with the seconds line
            videoControl.currentTime().addListener((observable, oldValue, newValue) ->
                currentTimeProperty.setValue(newValue.toSeconds()));
            codingControl.currentTime().bind(currentTimeProperty);

            videoControl.totalDurationProperty().addListener((observable, oldValue, newValue) ->
                    codingControl.lengthMsProperty().setValue(newValue.toMillis()));
        } else {
            codingControl.currentTime().unbind();
            codingControl.lengthMsProperty().setValue(observation.getEndOfLastCoding());
        }

        codingControl.loadObservation(observation);
    }

    public void setStudy(Study study) {
        this.study = study;
        this.actionService = study.getActionService();
    }

    private void handleActionValue(String newValue) {
        Action a = actionService.getForName(newValue);

        if (a != null) {
            if (a.getModifierFactory() == null && !inputModifier.isDisabled()) {
                log.trace("Disabling text field - no modifier factory for action {}", a);
                inputModifier.setText("No modifier for " + a.getDisplayString());
                inputModifier.setDisable(true);
            } else if (a.getModifierFactory() != null && inputModifier.isDisabled()) {
                log.trace("Enabling text field - action has a modifier factory {}", a);
                inputModifier.clear();
                inputModifier.setDisable(false);
            }
        } else {
            inputModifier.setText("No valid action entered");
            inputModifier.setDisable(true);
        }
    }

    @FXML
    public void code(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            code();
        }
    }

    private void code() {
        try {
            final String subject = inputSubject.getText();
            final String action = inputAction.getText();

            if (subject != null && !subject.isEmpty() && action != null && !action.isEmpty()) {
                final String modifier = !inputModifier.isDisabled() ? inputModifier.getText() : null;

                if (!isEndCodingMode()) {
                    codingService.startCoding(subject, action, modifier, (long) (currentTimeProperty.get() * 1000));
                } else {
                    codingService.endCoding(subject.substring(1), action, modifier, (long) (currentTimeProperty.get() * 1000));
                }
            }
        } catch (ServiceException e) {
            AlertUtil.showError("Error while coding", "Couldn't code, because " + e.getMessage(), e);
        }
    }

    private boolean isEndCodingMode() {
        return inputSubject.getText().length() >= 1 && inputSubject.getText().charAt(0) == '/';
    }
}
