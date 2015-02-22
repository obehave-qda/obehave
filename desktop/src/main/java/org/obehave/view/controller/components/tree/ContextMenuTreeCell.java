package org.obehave.view.controller.components.tree;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldTreeCell;
import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.model.Study;
import org.obehave.model.Subject;
import org.obehave.util.I18n;


public class ContextMenuTreeCell extends TextFieldTreeCell<String> {
    private ContextMenu addSubject = new ContextMenu();
    private ContextMenu addAction = new ContextMenu();
    private ContextMenu addObservation = new ContextMenu();
    private ContextMenu removeSubject = new ContextMenu();
    private ContextMenu removeAction = new ContextMenu();
    private ContextMenu removeObservation = new ContextMenu();

    public ContextMenuTreeCell(Study study) {
        super();


        MenuItem addSubjectItem = new MenuItem(I18n.getString("ui.subject.add"));
        MenuItem addActionItem = new MenuItem(I18n.getString("ui.action.add"));
        MenuItem addObservationItem = new MenuItem(I18n.getString("ui.observation.add"));
        MenuItem removeSubjectItem = new MenuItem(I18n.getString("ui.subject.remove"));
        MenuItem removeActionItem = new MenuItem(I18n.getString("ui.action.remove"));
        MenuItem removeObservationItem = new MenuItem(I18n.getString("ui.observation.remove"));

        addSubject.getItems().add(addSubjectItem);
        addAction.getItems().add(addActionItem);
        addObservation.getItems().add(addObservationItem);
        removeSubject.getItems().add(removeSubjectItem);
        removeAction.getItems().add(removeActionItem);
        removeObservation.getItems().add(removeObservationItem);


        addSubject.setOnAction(t -> study.addRandomSubject(""));
        addAction.setOnAction(t -> study.addRandomAction(""));
        addObservation.setOnAction(t -> study.addRandomObservation(""));
        removeSubject.setOnAction(t -> study.removeSubject(new Subject(getText())));
        removeAction.setOnAction(t -> study.removeAction(new Action(getText())));
        removeObservation.setOnAction(t -> study.removeObservation(new Observation(getText())));
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty) {
            TreeItem parent = getTreeItem().getParent();

            // first level
            if (parent != null) {
                // second level
                if (parent.getParent() == null) {
                    if (item.equals(I18n.getString("ui.subject.plural"))) {
                        setContextMenu(addSubject);
                    } else if (item.equals(I18n.getString("ui.action.plural"))) {
                        setContextMenu(addAction);
                    } else if (item.equals(I18n.getString("ui.observation.plural"))) {
                        setContextMenu(addObservation);
                    }
                } else {
                    if (parent.getValue().equals(I18n.getString("ui.subject.plural"))) {
                        setContextMenu(removeSubject);
                    } else if (parent.getValue().equals(I18n.getString("ui.action.plural"))) {
                        setContextMenu(removeAction);
                    } else if (parent.getValue().equals(I18n.getString("ui.observation.plural"))) {
                        setContextMenu(removeObservation);
                    }
                }
            }
        }
    }
}
