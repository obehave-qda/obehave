package org.obehave.view.controller.components.tree;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.controlsfx.control.PopOver;
import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.view.controller.components.edit.ObservationEditController;
import org.obehave.view.controller.components.edit.SubjectEditController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Markus Möslinger
 */
public class PopOverHolder {
    private static final Logger log = LoggerFactory.getLogger(PopOverHolder.class);

    private static final FXMLLoader subjectLoader = getFxmlLoader("subjectEdit.fxml");
    private static final FXMLLoader actionLoader = getFxmlLoader("actionEdit.fxml");
    private static final FXMLLoader modifierFactoryLoader = getFxmlLoader("modifierFactoryEdit.fxml");
    private static final FXMLLoader observationLoader = getFxmlLoader("observationEdit.fxml");

    private static Parent subjectParent;
    private static Parent actionParent;
    private static Parent modifierFactoryParent;
    private static Parent observationParent;

    private static final PopOver popOver = createPopOver();

    public static PopOver getSubject(Subject s) {
        log.debug("Getting popover for subject {}", s);
        popOver.setContentNode(getSubjectParent());

        SubjectEditController controller = subjectLoader.getController();
        controller.loadSubject(s);

        return popOver;
    }

    public static PopOver getAction(Action a) {
        log.debug("Getting popover for action {}", a);
        popOver.setContentNode(getActionParent());

        return popOver;
    }

    public static PopOver getModifierFactory(ModifierFactory mf) {
        log.debug("Getting popover for modifier factory {}", mf);
        popOver.setContentNode(getModifierFactoryParent());

        return popOver;
    }

    public static PopOver getObservation(Observation o) {
        log.debug("Getting popover for observation {}", o);
        popOver.setContentNode(getObservationParent());

        ObservationEditController controller = observationLoader.getController();
        controller.loadObservation(o);

        return popOver;
    }

    public static void hidePopOver() {
        popOver.hide();
    }

    private static FXMLLoader getFxmlLoader(String fxmlFile) {
        return new FXMLLoader(PopOverHolder.class.getResource("/ui/components/edit/" + fxmlFile));
    }

    private static PopOver createPopOver() {
        PopOver popOver = new PopOver();

        popOver.setDetachable(false);
        popOver.setArrowSize(12);
        popOver.setArrowIndent(12);
        popOver.setCornerRadius(6);
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);

        return popOver;
    }

    private static Parent getSubjectParent() {
        if (subjectParent == null) {
            subjectParent = load(subjectLoader);
        }

        return subjectParent;
    }

    private static Parent getActionParent() {
        if (actionParent == null) {
            actionParent = load(actionLoader);
        }

        return actionParent;
    }

    private static Parent getObservationParent() {
        if (observationParent == null) {
            observationParent = load(observationLoader);
        }

        return observationParent;
    }

    private static Parent getModifierFactoryParent() {
        if (modifierFactoryParent == null) {
            modifierFactoryParent = load(modifierFactoryLoader);
        }

        return modifierFactoryParent;
    }

    private static Parent load(FXMLLoader loader) {
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
