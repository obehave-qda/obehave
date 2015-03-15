package org.obehave.view.controller.components.tree;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.controlsfx.control.PopOver;
import org.obehave.model.*;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.service.Study;
import org.obehave.view.controller.components.edit.ModifierFactoryEditController;
import org.obehave.view.controller.components.edit.ObservationEditController;
import org.obehave.view.controller.components.edit.SubjectEditController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PopOverHolder {
    private static final Logger log = LoggerFactory.getLogger(PopOverHolder.class);

    private final FXMLLoader subjectLoader = getFxmlLoader("subjectEdit.fxml");
    private final FXMLLoader actionLoader = getFxmlLoader("actionEdit.fxml");
    private final FXMLLoader modifierFactoryLoader = getFxmlLoader("modifierFactoryEdit.fxml");
    private final FXMLLoader observationLoader = getFxmlLoader("observationEdit.fxml");
    private final PopOver popOver = createPopOver();
    private final Study study;
    private Parent subjectParent;
    private Parent actionParent;
    private Parent modifierFactoryParent;
    private Parent observationParent;

    public PopOverHolder(Study study) {
        this.study = study;
    }

    public PopOver getSubject(Subject s) {
        log.debug("Getting popover for subject {}", s);
        popOver.setContentNode(getSubjectParent());

        SubjectEditController controller = subjectLoader.getController();
        controller.setSaveCallback(this::hidePopOver);
        controller.loadSubject(s);

        return popOver;
    }

    public PopOver getAction(Action a) {
        log.debug("Getting popover for action {}", a);
        popOver.setContentNode(getActionParent());

        return popOver;
    }

    public PopOver getModifierFactory(ModifierFactory mf) {
        log.debug("Getting popover for modifier factory {}", mf);
        popOver.setContentNode(getModifierFactoryParent());

        ModifierFactoryEditController controller = modifierFactoryLoader.getController();
        controller.setStudy(study);
        controller.setSaveCallback(this::hidePopOver);
        controller.loadModifierFactory(mf);

        return popOver;
    }

    public PopOver getObservation(Observation o) {
        log.debug("Getting popover for observation {}", o);
        popOver.setContentNode(getObservationParent());

        ObservationEditController controller = observationLoader.getController();
        controller.setSaveCallback(this::hidePopOver);
        controller.loadObservation(o);

        return popOver;
    }

    public PopOver get(Node n) {
        Displayable data = n.getData();

        if (data instanceof Subject) {
            return getSubject((Subject) data);
        } else if (data instanceof Action) {
            return getAction((Action) data);
        } else if (data instanceof ModifierFactory) {
            return getModifierFactory((ModifierFactory) data);
        } else if (data instanceof Observation) {
            return getObservation((Observation) data);
        }

        return null;
    }

    public void hidePopOver() {
        popOver.hide();
    }

    private FXMLLoader getFxmlLoader(String fxmlFile) {
        return new FXMLLoader(PopOverHolder.class.getResource("/ui/components/edit/" + fxmlFile));
    }

    private PopOver createPopOver() {
        PopOver popOver = new PopOver();

        popOver.setDetachable(false);
        popOver.setArrowSize(12);
        popOver.setArrowIndent(12);
        popOver.setCornerRadius(6);
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);

        return popOver;
    }

    private Parent getSubjectParent() {
        if (subjectParent == null) {
            subjectParent = load(subjectLoader);
        }

        return subjectParent;
    }

    private Parent getActionParent() {
        if (actionParent == null) {
            actionParent = load(actionLoader);
        }

        return actionParent;
    }

    private Parent getObservationParent() {
        if (observationParent == null) {
            observationParent = load(observationLoader);
        }

        return observationParent;
    }

    private Parent getModifierFactoryParent() {
        if (modifierFactoryParent == null) {
            modifierFactoryParent = load(modifierFactoryLoader);
        }

        return modifierFactoryParent;
    }

    private Parent load(FXMLLoader loader) {
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
