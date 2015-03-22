package org.obehave.view.controller.components.edit;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.obehave.model.Action;
import org.obehave.model.Node;
import org.obehave.service.ActionService;
import org.obehave.service.NodeService;
import org.obehave.service.Study;
import org.obehave.view.util.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Markus Möslinger
 */
public class ActionGroupEditController {
    private static final Logger log = LoggerFactory.getLogger(ActionGroupEditController.class);
    private static final ActionService actionService = ActionService.getInstance();
    private static final NodeService nodeService = NodeService.getInstance();

    private Node loadedActionNode;
    private Runnable saveCallback;

    private boolean edit;

    private Study study;

    @FXML
    private TextField name;

    @FXML
    private CheckBox exclusive;

    public String getName() {
        return name.getText();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void loadActionGroupEdit(Node node) {
        loadedActionNode = node;
        edit = true;

        setName(node.getTitle());
        exclusive.setSelected(node.getExclusivity() != Node.Exclusivity.NOT_EXCLUSIVE);

        name.requestFocus();
    }

    public void loadActionGroupNew(Node parent) {
        loadedActionNode = parent;
        edit = false;

        setName("");
        exclusive.setSelected(false);

        name.requestFocus();
    }

    public void saveCurrent() {
        if (name.getText().isEmpty()) {
            AlertUtil.showError("Validation error", "Action group must have a name");
            return;
        }

        Node node;

        if (!edit) {
            log.debug("Creating new action");
            node = new Node(Action.class);
            loadedActionNode.addChild(node);
        } else {
            node = loadedActionNode;
        }

        node.setTitle(name.getText());

        if (exclusive.isSelected()) {
            node.setExclusivity(Node.Exclusivity.TOTAL_EXCLUSIVE);
        } else {
            node.setExclusivity(Node.Exclusivity.NOT_EXCLUSIVE);
        }

        nodeService.save(study.getActions());

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
    }
}
