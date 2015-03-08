package org.obehave.view.controller.components.edit;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

/**
 * @author Markus Möslinger
 */
public class SubjectEditController {
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
}
