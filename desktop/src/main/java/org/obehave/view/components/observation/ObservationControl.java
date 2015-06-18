package org.obehave.view.components.observation;

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
import org.obehave.events.UiEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.service.ActionService;
import org.obehave.service.CodingService;
import org.obehave.service.Study;
import org.obehave.service.SuggestionService;
import org.obehave.view.components.observation.buttoncoding.ButtonCodingPane;
import org.obehave.view.components.observation.timeline.CodingControl;
import org.obehave.view.util.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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

    @FXML
    private ButtonCodingPane buttonCodingPane;

    private Map<TextField, AutoCompletionBinding<String>> completions = new HashMap<>();

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

        videoControl.maxHeightProperty().bind(heightProperty().multiply(0.75));
        codingControl.maxHeightProperty().bind(heightProperty().multiply(0.25));
        videoControl.codingHeight().bind(codingControl.heightProperty());

        msPlayed = videoControl.msPlayed();

        // we have to redo the completion binding later, so store it in a variable
        createSubjectCompletionBinding();
        createActionCompletionBinding();
        createModifierCompletionBinding();

        inputModifier.focusedProperty().addListener((observable1, oldFocused, newFocused) -> {
            if (newFocused) {
                completions.get(inputModifier).setUserInput(inputModifier.getText());
            }
        });

        inputSubject.textProperty().addListener(((observable1, oldValue, newValue) -> {
            if ("".equals(oldValue) && !"".equals(newValue)) {
                videoControl.pause();
            }
        }));

        inputAction.textProperty().addListener((observable, oldValue, newValue) -> handleActionValue(newValue));
        inputModifier.setText("No valid action entered");
        inputModifier.setDisable(true);

        buttonCodingPane.setElapsedTimeProperty(msPlayed);
    }

    private void createSubjectCompletionBinding() {
        final AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(inputSubject,
                p -> (suggestionService.getSubjectSuggestions(p.getUserText(), isEndCodingMode(), (long) (msPlayed.get()))));
        binding.setOnAutoCompleted(e -> inputAction.requestFocus());

        completions.put(inputSubject, binding);
    }

    private void createActionCompletionBinding() {
        final AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(inputAction,
                p -> (suggestionService.getActionSuggestions(p.getUserText(), isEndCodingMode(), inputSubject.getText(), (long) (msPlayed.get()))));
        binding.setOnAutoCompleted(e -> inputModifier.requestFocus());

        completions.put(inputAction, binding);
    }

    private void createModifierCompletionBinding() {
        completions.put(inputModifier, TextFields.bindAutoCompletion(inputModifier,
                p -> (suggestionService.getModifierSuggestions(inputAction.getText(), p.getUserText()))));
    }

    public void loadObservation(UiEvent.LoadObservation event) {
        log.debug("Loading observation, because of {}", event);

        final Observation observation = event.getObservation();
        codingService = study.getCodingServiceBuilder().build(observation);
        suggestionService = study.getSuggestionServiceBuilder().build(observation);

        if (observation.getVideo() != null) {
            if (observation.getVideo().exists()) {
                videoControl.setVisible(true);
                videoControl.loadVideo(observation.getVideo());

                codingControl.setMsPlayed(msPlayed);

                videoControl.totalDurationProperty().addListener((observable, oldValue, newValue) ->
                        codingControl.lengthMsProperty().setValue(newValue.toMillis()));
            } else {
                videoControl.setVisible(false);
                AlertUtil.showError("Video doesn't exist", "Selected video wasn't found:\n" + observation.getVideo());
            }
        } else {
            videoControl.setVisible(false);
            codingControl.setMsPlayed(new SimpleDoubleProperty(this, "dummyDoubleProperty", 0));
            codingControl.lengthMsProperty().setValue(observation.getEndOfLastCoding());
        }

        codingControl.loadObservation(observation);
    }

    public void setStudy(Study study) {
        this.study = study;
        this.actionService = study.getActionService();

        buttonCodingPane.setStudy(study);
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

                clearCompletionTextfield(inputModifier);
                inputModifier.setDisable(false);

                createModifierCompletionBinding();
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
                    codingService.startCoding(subject, action, modifier, (long) (msPlayed.get()));
                } else {
                    codingService.endCoding(subject.substring(1), action, modifier, (long) (msPlayed.get()));
                }

                clearAllCompletionTextFields();

                createSubjectCompletionBinding();
                createActionCompletionBinding();
                createModifierCompletionBinding();

                inputSubject.requestFocus();
                videoControl.start();
            }
        } catch (ServiceException e) {
            AlertUtil.showError("Error while coding", "Couldn't code, because " + e.getMessage(), e);
        }
    }

    private boolean isEndCodingMode() {
        return inputSubject.getText().length() >= 1 && inputSubject.getText().charAt(0) == '/';
    }

    private void clearCompletionTextfield(TextField textField) {
        completions.get(textField).dispose();
        completions.remove(textField);

        textField.clear();
    }

    private void clearAllCompletionTextFields() {
        new HashSet<>(completions.keySet()).forEach(this::clearCompletionTextfield);
    }
}
