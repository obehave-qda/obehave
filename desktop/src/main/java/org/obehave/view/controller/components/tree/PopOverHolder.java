package org.obehave.view.controller.components.tree;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.controlsfx.control.PopOver;
import org.obehave.model.Action;
import org.obehave.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * @author Markus Möslinger
 */
public class PopOverHolder {
    private static final Logger log = LoggerFactory.getLogger(PopOverHolder.class);

    private static final PopOver subjectPopOver = createPopOver("subjectEdit.fxml");
    private static final PopOver actionPopOver = createPopOver("subjectEdit.fxml");

    public static PopOver hideAllAndGetSubject(Subject s) {
        log.debug("Getting popover for subject {}", s);
        hidePopOvers(actionPopOver);

        return subjectPopOver;
    }

    public static PopOver hideAllAndGetAction(Action a) {
        log.debug("Getting popover for action {}", a);
        hidePopOvers(subjectPopOver);

        return actionPopOver;
    }

    public static void hidePopOvers(PopOver... popOvers) {
        log.trace("Hiding all pop overs");

        for (PopOver popOver : popOvers) {
            popOver.hide();
        }
    }

    private static PopOver createPopOver(String fxmlFile) {
        Parent p;
        try {
            final URL url = PopOverHolder.class.getResource("/ui/components/edit/" + fxmlFile);
            p = FXMLLoader.load(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PopOver popOver = new PopOver(p);
        popOver.setDetachable(false);
        popOver.setArrowSize(12);
        popOver.setArrowIndent(12);
        popOver.setCornerRadius(6);
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);

        return popOver;
    }
}
