package org.obehave.view.components.observation.timeline;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import org.obehave.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The SubjectsList draws every Subject's title on the left screen side
 */
public class SubjectsList extends Pane {
    private static Logger log = LoggerFactory.getLogger(SubjectsList.class);

    private List<Subject> subjects = new ArrayList<>();
    private IntegerProperty subjectCount = new SimpleIntegerProperty(this, "subjectCount", 0);

    private DoubleProperty subjectHeightProperty = new SimpleDoubleProperty(this, "subjectHeightProperty");

    public SubjectsList() {
        prefHeightProperty().bind(subjectHeightProperty.multiply(subjectCount).add(subjectHeightProperty().divide(2)));
    }

    public DoubleProperty subjectHeightProperty() {
        return subjectHeightProperty;
    }

    public void refresh() {
        getChildren().clear();

        for (int i = 0; i < subjects.size(); i++) {
            final Subject subject = subjects.get(i);

            Text subjectText = new Text(subject.getDisplayString());
            subjectText.xProperty().setValue(2);
            subjectText.yProperty().bind(subjectHeightProperty.multiply(i));
            subjectText.setTranslateY(subjectText.getBoundsInParent().getHeight());
            subjectText.getStyleClass().add("subjectLabel");
            subjectText.setFontSmoothingType(FontSmoothingType.LCD);
            getChildren().add(subjectText);
        }
    }

    public IntegerProperty subjectCount() {
        return subjectCount;
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
        subjectCount.setValue(subjects.size());
        refresh();
    }

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
        subjectCount.setValue(subjects.size());
        refresh();
    }

    public void clear() {
        subjects.clear();
        subjectCount.setValue(subjects.size());
        refresh();
    }
}
