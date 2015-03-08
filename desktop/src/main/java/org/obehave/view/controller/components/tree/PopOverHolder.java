package org.obehave.view.controller.components.tree;

import javafx.fxml.FXMLLoader;
import org.controlsfx.control.PopOver;
import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
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
    private static final FXMLLoader actionLoader = getFxmlLoader("subjectEdit.fxml");
    private static final FXMLLoader observationLoader = getFxmlLoader("observationEdit.fxml");

    // maybe one PopOver with changing content is enough. Don't know.
    private static final PopOver subjectPopOver = createPopOver(subjectLoader);
    private static final PopOver actionPopOver = createPopOver(actionLoader);
    private static final PopOver observationPopOver = createPopOver(observationLoader);

    public static PopOver hideAllAndGetSubject(Subject s) {
        log.debug("Getting popover for subject {}", s);
        hidePopOvers(subjectPopOver, actionPopOver);

        SubjectEditController controller = subjectLoader.getController();
        controller.loadSubject(s);

        return subjectPopOver;
    }

    public static PopOver hideAllAndGetAction(Action a) {
        log.debug("Getting popover for action {}", a);
        hidePopOvers(subjectPopOver, observationPopOver);

        return actionPopOver;
    }

    public static PopOver hideAllAndGetObservation(Observation o) {
        log.debug("Getting popover for observation {}", o);
        hidePopOvers(subjectPopOver, actionPopOver);

        ObservationEditController controller = observationLoader.getController();
        controller.loadObservation(o);

        return observationPopOver;
    }

    public static void hidePopOvers(PopOver... popOvers) {
        log.trace("Hiding all pop overs");

        for (PopOver popOver : popOvers) {
            popOver.hide();
        }
    }

    public static void hideAllPopOvers() {
        hidePopOvers(subjectPopOver, actionPopOver, observationPopOver);
    }

    private static FXMLLoader getFxmlLoader(String fxmlFile) {
        return new FXMLLoader(PopOverHolder.class.getResource("/ui/components/edit/" + fxmlFile));
    }


    private static PopOver createPopOver(FXMLLoader loader) {
        PopOver popOver = null;
        try {
            popOver = new PopOver(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        popOver.setDetachable(false);
        popOver.setArrowSize(12);
        popOver.setArrowIndent(12);
        popOver.setCornerRadius(6);
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);

        return popOver;
    }
}
