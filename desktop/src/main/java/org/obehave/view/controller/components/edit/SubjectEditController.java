package org.obehave.view.controller.components.edit;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.obehave.model.Subject;
import org.obehave.service.SubjectService;
import org.obehave.view.util.ColorConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Markus Möslinger
 */
public class SubjectEditController {
    private static final Logger log = LoggerFactory.getLogger(SubjectEditController.class);
    private static final SubjectService subjectService = SubjectService.getInstance();

    private Subject loadedSubject;
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

    public void loadSubject(Subject s) {
        loadedSubject = s;

        setName(s.getName());
        setAlias(s.getAlias());
        colorPicker.setValue(ColorConverter.convertToJavaFX(s.getColor()));
    }

    public void saveCurrent() {
        if (loadedSubject == null) {
            log.debug("Creating new subject");
            loadedSubject = new Subject(getName());
        } else {
            log.debug("Saving existing subject");
            loadedSubject.setName(getName());
        }

        loadedSubject.setAlias(getAlias());
        loadedSubject.setColor(ColorConverter.convertToObehave(colorPicker.getValue()));

        subjectService.save(loadedSubject);

        loadedSubject = null;
        saveCallback.run();
    }

    public void setSaveCallback(Runnable saveCallback) {
        this.saveCallback = saveCallback;
    }
}
