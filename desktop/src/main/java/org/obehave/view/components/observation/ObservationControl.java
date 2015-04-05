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
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.service.ActionService;
import org.obehave.service.CodingService;
import org.obehave.service.Study;
import org.obehave.service.SuggestionService;
import org.obehave.view.components.observation.coding.CodingControl;
import org.obehave.view.util.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ObservationControl extends BorderPane {
    private final Logger log = LoggerFactory.getLogger(ObservationControl.class);

    private Study study;

    private SuggestionService suggestionService;

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

    private AutoCompletionBinding<String> modifierCompletion;

    private DoubleProperty msPlayed = new SimpleDoubleProperty(this, "msPlayed");

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
                p -> (suggestionService.getSubjectSuggestions(p.getUserText(), isEndCodingMode(), (long) (msPlayed.get() * 1000))))
                .setOnAutoCompleted(e -> inputAction.requestFocus());
        TextFields.bindAutoCompletion(inputAction,
                p -> (suggestionService.getActionSuggestions(p.getUserText(), isEndCodingMode(), inputSubject.getText(), (long) (msPlayed.get() * 1000))))
                .setOnAutoCompleted(e -> inputModifier.requestFocus());

        // we have to redo the completion binding later, so store it in a variable
        modifierCompletion = createModifierAutocompletionBinding();

        inputModifier.focusedProperty().addListener((observable1, oldFocused, newFocused) -> {
            if (newFocused) {
                modifierCompletion.setUserInput(inputModifier.getText());
            }
        });

        inputAction.textProperty().addListener((observable, oldValue, newValue) -> handleActionValue(newValue));
        inputModifier.setText("No valid action entered");
        inputModifier.setDisable(true);
    }

    private AutoCompletionBinding<String> createModifierAutocompletionBinding() {
        return TextFields.bindAutoCompletion(inputModifier,
                p -> (suggestionService.getModifierSuggestions(inputAction.getText(), p.getUserText())));
    }

    public void loadVideo(File video) {
        videoControl.loadVideo(video);
    }

    @Subscribe
    public void loadObservation(UiEvent.LoadObservation event) {
        log.debug("Loading observation, because of {}", event);

        final Observation observation = event.getObservation();
        codingService = study.getCodingServiceBuilder().build(observation);
        suggestionService = study.getSuggestionServiceBuilder().build(observation);

        if (observation.getVideo() != null) {
            loadVideo(observation.getVideo());

            msPlayed.bind(videoControl.msPlayed());
            codingControl.msPlayed().bind(msPlayed);

            videoControl.totalDurationProperty().addListener((observable, oldValue, newValue) ->
                    codingControl.lengthMsProperty().setValue(newValue.toMillis()));
        } else {
            codingControl.msPlayed().unbind();
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
            if (a.getModifierFactory() == null && !inputModifier.getText().startsWith("No modifier for")) {
                log.trace("Disabling text field - no modifier factory for action {}", a);
                inputModifier.setText("No modifier for " + a.getDisplayString());
                inputModifier.setDisable(true);
            } else if (a.getModifierFactory() != null && inputModifier.isDisabled()) {
                log.trace("Enabling text field - action has a modifier factory {}", a);
                modifierCompletion.dispose();

                inputModifier.clear();
                inputModifier.setDisable(false);

                modifierCompletion = createModifierAutocompletionBinding();
            }
        } else {
            inputModifier.setText("No valid action entered");
            inputModifier.setDisable(true);
        }
    }

    @FXML
    public void codeEnter(KeyEvent event) {
        if (event.isShortcutDown() && event.getCode() == KeyCode.ENTER) {
            code();
        }
    }

    @FXML
    public void code() {
        try {
            final String subject = inputSubject.getText();
            final String action = inputAction.getText();

            if (subject != null && !subject.isEmpty() && action != null && !action.isEmpty()) {
                final String modifier = !inputModifier.isDisabled() ? inputModifier.getText() : null;

                if (!isEndCodingMode()) {
                    codingService.startCoding(subject, action, modifier, (long) (msPlayed.get() * 1000));
                } else {
                    codingService.endCoding(subject.substring(1), action, modifier, (long) (msPlayed.get() * 1000));
                }

                inputSubject.clear();
                inputModifier.clear();
                inputAction.clear();
            }
        } catch (ServiceException e) {
            AlertUtil.showError("Error while coding", "Couldn't code, because " + e.getMessage(), e);
        }
    }

    private boolean isEndCodingMode() {
        return inputSubject.getText().length() >= 1 && inputSubject.getText().charAt(0) == '/';
    }
}
