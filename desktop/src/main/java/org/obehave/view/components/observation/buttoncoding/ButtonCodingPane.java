package org.obehave.view.components.observation.buttoncoding;

import com.google.common.eventbus.Subscribe;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Action;
import org.obehave.model.Subject;
import org.obehave.service.CodingService;
import org.obehave.service.Study;
import org.obehave.view.util.AlertUtil;

public class ButtonCodingPane extends ScrollPane {
    private final Pane showingPane = new Pane();
    private final SubjectButtonPane subjectButtonPane = new SubjectButtonPane();
    private final ActionButtonPane actionButtonPane = new ActionButtonPane();
    private final ModifierPane modifierPane = new ModifierPane();

    private Study study;
    private CodingService codingService;

    private Subject selectedSubject;
    private Action selectedAction;

    private DoubleProperty elapsedTimeProperty = new SimpleDoubleProperty(0);

    public ButtonCodingPane() {
        EventBusHolder.register(this);

        setHbarPolicy(ScrollBarPolicy.NEVER);

        getChildren().addAll(showingPane, new OpenCodingPane());
        showingPane.getChildren().add(subjectButtonPane);
    }

    public void setStudy(Study study) {
        this.study = study;
        actionButtonPane.setStudy(study);
    }

    @Subscribe
    public void loadObservation(UiEvent.LoadObservation loadObservation) {
        codingService = study.getCodingServiceBuilder().build(loadObservation.getObservation());
    }

    @Subscribe
    public void subjectSelected(Eventsis.SubjectClicked subjectClicked) {
        selectedSubject = subjectClicked.get();

        switchToPane(actionButtonPane);
    }

    @Subscribe
    public void actionSelected(Eventsis.ActionClicked actionClicked) {
        selectedAction = actionClicked.get();

        if (selectedAction.getModifierFactory() != null) {
            modifierPane.forModifierFactory(selectedAction.getModifierFactory());
            switchToPane(modifierPane);
        } else {
            code("");
            switchToPane(subjectButtonPane);
        }
    }

    @Subscribe
    public void modifierSelected(Eventsis.ModifierClicked modifierClicked) {
        code(modifierClicked.get());

        switchToPane(subjectButtonPane);
    }

    private void switchToPane(Pane pane) {
        showingPane.getChildren().clear();
        showingPane.getChildren().add(pane);
    }

    private void code(String modifierInput) {
        try {
            codingService.startCoding(selectedSubject, selectedAction, modifierInput, (long) elapsedTimeProperty.get());
        } catch (ServiceException e) {
            AlertUtil.showError("Error while coding", "There was an error while coding", e);
        }

        selectedSubject = null;
        selectedAction = null;
    }

    public void setElapsedTimeProperty(DoubleProperty elapsedTimeProperty) {
        this.elapsedTimeProperty.bind(elapsedTimeProperty);
    }
}