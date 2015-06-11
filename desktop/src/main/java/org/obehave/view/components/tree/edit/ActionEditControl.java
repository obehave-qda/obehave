package org.obehave.view.components.tree.edit;

import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Action;
import org.obehave.model.Displayable;
import org.obehave.model.Node;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.service.Study;
import org.obehave.util.DisplayWrapper;
import org.obehave.view.util.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Markus Möslinger
 */
public class ActionEditControl {
    private static final Logger log = LoggerFactory.getLogger(ActionEditControl.class);

    private Node loadedActionNode;
    private Runnable saveCallback;

    private boolean edit;

    private Study study;

    @FXML
    private TextField name;

    @FXML
    private TextField alias;

    @FXML
    private ComboBox<DisplayWrapper<ModifierFactory>> modifierFactoryCombo;

    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton radioPoint;
    @FXML
    private RadioButton radioState;

    public ActionEditControl() {
        EventBusHolder.register(this);
    }

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

    public void loadActionEdit(Node node) {
        loadedActionNode = node;
        edit = true;
        Action a = (Action) node.getData();

        setName(a.getName());
        setAlias(a.getAlias());

        if (a.getModifierFactory() != null) {
            modifierFactoryCombo.getSelectionModel().select(DisplayWrapper.of(a.getModifierFactory()));
        } else {
            modifierFactoryCombo.getSelectionModel().clearSelection();
        }

        if (a.getType() == Action.Type.POINT) {
            radioPoint.setSelected(true);
        } else {
            radioState.setSelected(true);
        }

        name.requestFocus();
    }

    public void loadActionNew(Node parent) {
        loadedActionNode = parent;
        edit = false;

        setName("");
        setAlias("");
        radioPoint.setSelected(true);
        modifierFactoryCombo.getSelectionModel().select(0);

        name.requestFocus();
    }

    public void saveCurrent() {
        if (name.getText().isEmpty()) {
            AlertUtil.showError("Validation error", "Action must have a name");
            return;
        }

        Action action;

        if (!edit) {
            log.debug("Creating new action");
            action = new Action();
        } else {
            action = (Action) loadedActionNode.getData();
        }

        action.setName(getName());
        action.setAlias(getAlias());

        if (radioPoint.isSelected()) {
            action.setType(Action.Type.POINT);
        } else {
            action.setType(Action.Type.STATE);
        }

        action.setModifierFactory(modifierFactoryCombo.getSelectionModel().getSelectedItem().get());


        try {
            study.getActionService().save(action);
            if (!edit) {
                loadedActionNode.addChild(action);
            }
            study.getNodeService().save(loadedActionNode);

            loadedActionNode = null;
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
        refreshModifierFactories(null);
    }

    @Subscribe
    public void refreshModifierFactories(UiEvent.RepaintStudyTree event) {
        modifierFactoryCombo.getItems().clear();
        modifierFactoryCombo.getItems().add(DisplayWrapper.of((ModifierFactory) null));

        Node modifierFactories = study.getModifierFactories();

        for (Displayable displayable : modifierFactories.flatten()) {
            ModifierFactory modifierAction = (ModifierFactory) displayable;
            modifierFactoryCombo.getItems().add(DisplayWrapper.of(modifierAction));
        }
    }
}
