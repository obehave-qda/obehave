package org.obehave.view.components.tree.edit;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Node;
import org.obehave.model.Subject;
import org.obehave.service.Study;
import org.obehave.view.util.AlertUtil;
import org.obehave.view.util.ColorConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Markus Möslinger
 */
public class SubjectEditControl {
    private static final Logger log = LoggerFactory.getLogger(SubjectEditControl.class);

    private Study study;

    private Node loadedSubjectNode;
    private Runnable saveCallback;

    @FXML
    private TextField name;

    @FXML
    private TextField alias;

    @FXML
    private ColorPicker colorPicker;

    public String getName() {
        return name.getText();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public String getAlias() {
        return alias.getText();
    }

    public void setAlias(String alias) {
        this.alias.setText(alias);
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public void setColorPicker(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
    }

    public void loadSubject(Node node) {
        loadedSubjectNode = node;
        Subject s = (Subject) node.getData();

        if (s != null) {
            setName(s.getName());
            setAlias(s.getAlias());
            colorPicker.setValue(ColorConverter.convertToJavaFX(s.getColor()));
        } else {
            setName("");
            setAlias("");
            colorPicker.setValue(Color.BLACK);
        }

        name.requestFocus();
    }

    public void saveCurrent() {
        if (name.getText().isEmpty()) {
            AlertUtil.showError("Validation error", "Subject must have a name");
            return;
        }

        Subject subject;

        if (loadedSubjectNode.getData() == null) {
            log.debug("Creating new subject");
            subject = new Subject();
        } else {
            subject = (Subject) loadedSubjectNode.getData();
        }

        subject.setName(getName());
        subject.setAlias(getAlias());
        subject.setColor(ColorConverter.convertToObehave(colorPicker.getValue()));

        try {
            study.getSubjectService().save(subject);
            if (loadedSubjectNode.getData() == null) {
                loadedSubjectNode.addChild(subject);
            }
            study.getNodeService().save(loadedSubjectNode);

            loadedSubjectNode = null;
            saveCallback.run();
        } catch (ServiceException exception) {
            AlertUtil.showError("Error", exception.getMessage(), exception);
        }
    }

    public void cancel() {
        saveCallback.run();
    }

    public void setSaveCallback(Runnable saveCallback) {
        this.saveCallback = saveCallback;
    }

    public void setStudy(Study study) {
        this.study = study;
    }
}
