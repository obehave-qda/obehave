package org.obehave.view.components.tree.edit;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.CheckListView;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Displayable;
import org.obehave.model.Node;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.service.Study;
import org.obehave.util.DisplayWrapper;
import org.obehave.util.I18n;
import org.obehave.view.util.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Markus Möslinger
 */
public class ModifierFactoryEditControl implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(ModifierFactoryEditControl.class);

    private static final String COMBO_SUBJECT_LIST = I18n.get("ui.modifierfactory.edit.combo.subjectlist");
    private static final String COMBO_ENUMERATION_LIST = I18n.get("ui.modifierfactory.edit.combo.enumerationlist");
    private static final String COMBO_NUMBER_RANGE = I18n.get("ui.modifierfactory.edit.combo.numberrange");

    private Study study;

    private Runnable saveCallback;

    @FXML
    private AnchorPane enumerationPane;
    @FXML
    private AnchorPane subjectPane;
    @FXML
    private AnchorPane numberRangePane;
    @FXML
    private AnchorPane visiblePane;

    @FXML
    private TextField name;

    @FXML
    private ComboBox<String> combobox;

    @FXML
    private ListView<String> enumerationList;

    @FXML
    private TextField enumerationEntry;

    @FXML
    private CheckListView<DisplayWrapper<Subject>> checkedSubjects;

    @FXML
    private TextField rangeFrom;

    @FXML
    private TextField rangeTo;

    private Node loadedModifierFactoryNode;

    public void enumerationAdd() {
        final String text = enumerationEntry.getText();
        if (!text.isEmpty() && !enumerationList.getItems().contains(text)) {
            log.debug("Adding {} to list", text);
            enumerationList.getItems().add(text);
        }

        enumerationEntry.setText("");
        enumerationEntry.requestFocus();
    }

    public void enumerationAddEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            enumerationAdd();
        }
    }

    public void enumerationRemove() {
        final List<Integer> selectedIndexes = new ArrayList<>(enumerationList.getSelectionModel().getSelectedIndices());
        if (selectedIndexes.size() > 0) {
            log.debug("Selected indizes to remove: {}", selectedIndexes);
            Collections.reverse(selectedIndexes);

            for (Integer i : selectedIndexes) {
                log.trace("Removing index {}", i);
                enumerationList.getItems().remove((int) i);
            }
        }

        enumerationList.getSelectionModel().clearSelection();
    }

    public void loadModifierFactory(Node node) {
        loadedModifierFactoryNode = node;

        ModifierFactory mf = (ModifierFactory) node.getData();

        checkedSubjects.getCheckModel().clearChecks();
        enumerationList.getItems().clear();

        if (mf == null) {
            name.setText("");
            combobox.setVisible(true);
            rangeFrom.setText("");
            rangeTo.setText("");
        } else {
            name.setText(mf.getName());
            combobox.setVisible(false);

            switch (mf.getType()) {
                case SUBJECT_MODIFIER_FACTORY:
                    mf.getValidSubjects().forEach(s -> checkedSubjects.getCheckModel().check(DisplayWrapper.of(s)));
                    combobox.setValue(COMBO_SUBJECT_LIST);
                    break;
                case ENUMERATION_MODIFIER_FACTORY:
                    mf.getValidValues().forEach(s -> enumerationList.getItems().add(s));
                    combobox.setValue(COMBO_ENUMERATION_LIST);
                    break;
                case DECIMAL_RANGE_MODIFIER_FACTORY:
                    rangeFrom.setText(String.valueOf(mf.getFrom()));
                    rangeTo.setText(String.valueOf(mf.getTo()));
                    combobox.setValue(COMBO_NUMBER_RANGE);
                    break;
            }
        }

        name.requestFocus();
    }

    public void saveCurrent() {
        if (name.getText().isEmpty()) {
            AlertUtil.showError("Validation error", "Modifier must have a name");
            return;
        }

        ModifierFactory mf = (ModifierFactory) loadedModifierFactoryNode.getData();

        boolean mfNull = mf == null;

        if (combobox.getValue().equals(COMBO_SUBJECT_LIST)) {
            if (mfNull) {
                mf = new ModifierFactory(getCheckedSubjects());
            } else {
                mf.setValidSubjects(getCheckedSubjects());
            }
        } else if (combobox.getValue().equals(COMBO_ENUMERATION_LIST)) {
            if (mfNull) {
                mf = new ModifierFactory(getAddedValues());
            } else {
                mf.setValidValues(getAddedValues());
            }
        } else if (combobox.getValue().equals(COMBO_NUMBER_RANGE)) {
            if (mfNull) {
                mf = new ModifierFactory(Integer.valueOf(rangeFrom.getText()), Integer.valueOf(rangeTo.getText()));
            } else {
                mf.setRange(Integer.valueOf(rangeFrom.getText()), Integer.valueOf(rangeTo.getText()));
            }
        } else {
            throw new IllegalStateException("ModifierFactory has to have a type");
        }

        mf.setName(name.getText());

        try {
            study.getModifierFactoryService().save(mf);
            if (mfNull) {
                loadedModifierFactoryNode.addChild(mf);
            }
            study.getNodeService().save(loadedModifierFactoryNode);

            loadedModifierFactoryNode = null;
            saveCallback.run();
        } catch (ServiceException exception) {
            AlertUtil.showError("Error", exception.getMessage(), exception);
        }
    }

    private String[] getAddedValues() {
        ObservableList<String> items = enumerationList.getItems();
        return items.toArray(new String[items.size()]);
    }

    private Subject[] getCheckedSubjects() {
        final ObservableList<DisplayWrapper<Subject>> checkedItems = checkedSubjects.getCheckModel().getCheckedItems();

        Subject[] subjects = new Subject[checkedItems.size()];
        for (int i = 0; i < checkedItems.size(); i++) {
            subjects[i] = checkedItems.get(i).get();
        }

        return subjects;
    }

    public void comboboxChange(ActionEvent e) {
        log.trace("Comboboxevent: {}", e);

        if (combobox.getValue().equals(COMBO_SUBJECT_LIST)) {
            showPane(subjectPane);
        } else if (combobox.getValue().equals(COMBO_ENUMERATION_LIST)) {
            showPane(enumerationPane);
        } else if (combobox.getValue().equals(COMBO_NUMBER_RANGE)) {
            showPane(numberRangePane);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combobox.getItems().addAll(COMBO_SUBJECT_LIST, COMBO_ENUMERATION_LIST, COMBO_NUMBER_RANGE);
        combobox.setValue(COMBO_SUBJECT_LIST);

        checkedSubjects.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        enumerationList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void cancel() {
        saveCallback.run();
    }

    private void showPane(AnchorPane pane) {
        log.trace("Showing pane {}", pane);

        visiblePane.getChildren().clear();
        visiblePane.getChildren().add(pane);
    }

    public void setStudy(Study study) {
        this.study = study;

        checkedSubjects.getItems().clear();
        Node subjectNode = study.getSubjects();

        for (Displayable displayable : subjectNode.flatten()) {
            Subject subject = (Subject) displayable;
            checkedSubjects.getItems().add(DisplayWrapper.of(subject));
        }
    }

    public void setSaveCallback(Runnable saveCallback) {
        this.saveCallback = saveCallback;
    }
}
