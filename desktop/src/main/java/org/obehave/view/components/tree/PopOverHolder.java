package org.obehave.view.components.tree;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.controlsfx.control.PopOver;
import org.obehave.model.Action;
import org.obehave.model.Node;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.service.Study;
import org.obehave.view.components.tree.edit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PopOverHolder {
    private static final Logger log = LoggerFactory.getLogger(PopOverHolder.class);

    private final FXMLLoader subjectLoader = getFxmlLoader("subjectEdit.fxml");
    private final FXMLLoader actionLoader = getFxmlLoader("actionEdit.fxml");
    private final FXMLLoader actionGroupLoader = getFxmlLoader("actionGroupEdit.fxml");
    private final FXMLLoader modifierFactoryLoader = getFxmlLoader("modifierFactoryEdit.fxml");
    private final FXMLLoader observationLoader = getFxmlLoader("observationEdit.fxml");
    private final PopOver popOver = createPopOver();
    private final Study study;
    private Parent subjectParent;
    private Parent actionParent;
    private Parent actionGroupParent;
    private Parent modifierFactoryParent;
    private Parent observationParent;

    public PopOverHolder(Study study) {
        this.study = study;
    }

    public PopOver getSubject(Node node) {
        log.trace("Getting popover for subject {}", node.getData());
        popOver.setContentNode(getSubjectParent());

        SubjectEditControl controller = subjectLoader.getController();
        controller.setStudy(study);
        controller.setSaveCallback(this::hidePopOver);
        controller.loadSubject(node);

        return popOver;
    }

    public PopOver getActionEdit(Node node) {
        log.trace("Getting popover for action {}", node);
        popOver.setContentNode(getActionParent());

        ActionEditControl controller = actionLoader.getController();
        controller.setStudy(study);
        controller.setSaveCallback(this::hidePopOver);
        controller.loadActionEdit(node);

        return popOver;
    }

    public PopOver getActionNew(Node parent) {
        log.trace("Getting popover for action {}", parent);
        popOver.setContentNode(getActionParent());

        ActionEditControl controller = actionLoader.getController();
        controller.setStudy(study);
        controller.setSaveCallback(this::hidePopOver);
        controller.loadActionNew(parent);

        return popOver;
    }

    public PopOver getActionGroupEdit(Node node) {
        log.trace("Getting popover for action group {}", node);
        popOver.setContentNode(getActionGroupParent());

        ActionGroupEditControl controller = actionGroupLoader.getController();
        controller.setStudy(study);
        controller.setSaveCallback(this::hidePopOver);
        controller.loadActionGroupEdit(node);

        return popOver;
    }

    public PopOver getActionGroupNew(Node parent) {
        log.trace("Getting popover for action group {}", parent);
        popOver.setContentNode(getActionGroupParent());

        ActionGroupEditControl controller = actionGroupLoader.getController();
        controller.setStudy(study);
        controller.setSaveCallback(this::hidePopOver);
        controller.loadActionGroupNew(parent);

        return popOver;
    }

    public PopOver getModifierFactory(Node mf) {
        log.trace("Getting popover for modifier factory {}", mf);
        popOver.setContentNode(getModifierFactoryParent());

        ModifierFactoryEditControl controller = modifierFactoryLoader.getController();
        controller.setStudy(study);
        controller.setSaveCallback(this::hidePopOver);
        controller.loadModifierFactory(mf);

        return popOver;
    }

    public PopOver getObservation(Node node) {
        log.trace("Getting popover for observation {}", node);
        popOver.setContentNode(getObservationParent());

        ObservationEditControl controller = observationLoader.getController();
        controller.setStudy(study);
        controller.setSaveCallback(this::hidePopOver);
        controller.loadObservation(node);

        return popOver;
    }

    public PopOver getNewActionGroupWithParent(Node parent) {
        return getActionGroupNew(parent);
    }

    public PopOver get(Node node) {
        Class<?> type = node.getDataType();

        if (type == Subject.class) {
            return getSubject(node);
        } else if (type == Action.class) {
            if (node.getData() != null) {
                return getActionEdit(node);
            } else {
                return getActionGroupEdit(node);
            }
        } else if (type == ModifierFactory.class) {
            return getModifierFactory(node);
        } else if (type == Observation.class) {
            return getObservation(node);
        }

        throw new IllegalArgumentException("Invalid node " + node + "! Class isn't recognized: " + type);
    }

    public void hidePopOver() {
        popOver.hide();
    }

    private FXMLLoader getFxmlLoader(String fxmlFile) {
        return new FXMLLoader(PopOverHolder.class.getResource("/org/obehave/view/components/tree/edit/" + fxmlFile));
    }

    private PopOver createPopOver() {
        PopOver popOver = new PopOver();

        popOver.setDetachable(false);
        popOver.setArrowSize(12);
        popOver.setArrowIndent(12);
        popOver.setCornerRadius(6);
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
        popOver.setAutoFix(true);
        popOver.setAutoHide(false);

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

    private Parent getActionGroupParent() {
        if (actionGroupParent == null) {
            actionGroupParent = load(actionGroupLoader);
        }

        return actionGroupParent;
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
