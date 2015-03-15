package org.obehave.view.controller.components.edit;

import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.obehave.events.EventBusHolder;
import org.obehave.events.RepaintStudyEvent;
import org.obehave.model.Action;
import org.obehave.model.Displayable;
import org.obehave.model.Node;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.service.ActionService;
import org.obehave.service.NodeService;
import org.obehave.service.Study;
import org.obehave.util.DisplayWrapper;
import org.obehave.view.util.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Markus Möslinger
 */
public class ActionEditController {
    private static final Logger log = LoggerFactory.getLogger(ActionEditController.class);
    private static final ActionService actionService = ActionService.getInstance();
    private static final NodeService nodeService = NodeService.getInstance();

    private Node loadedActionNode;
    private Runnable saveCallback;

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

    public ActionEditController() {
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

    public void loadAction(Node node) {
        loadedActionNode = node;
        Action a = (Action) node.getData();

        if (a != null) {
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
        } else {
            setName("");
            setAlias("");
            radioPoint.setSelected(true);
            modifierFactoryCombo.getSelectionModel().select(0);
        }

        name.requestFocus();
    }

    public void saveCurrent() {
        if (name.getText().isEmpty()) {
            AlertUtil.showError("Validation error", "Action must have a name");
            return;
        }

        Action action;

        if (loadedActionNode.getData() == null) {
            log.debug("Creating new action");
            action = new Action();
            loadedActionNode.addChild(action);
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

        actionService.save(action);
        nodeService.save(loadedActionNode);

        loadedActionNode = null;
        saveCallback.run();
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
    public void refreshModifierFactories(RepaintStudyEvent event) {
        modifierFactoryCombo.getItems().clear();
        modifierFactoryCombo.getItems().add(DisplayWrapper.of(null));

        Node modifierFactories = study.getModifierFactories();

        for (Displayable displayable : modifierFactories.flatten()) {
            ModifierFactory modifierAction = (ModifierFactory) displayable;
            modifierFactoryCombo.getItems().add(DisplayWrapper.of(modifierAction));
        }
    }
}
