package org.obehave.view.controller.components.tree;

import com.google.common.collect.Lists;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldTreeCell;
import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.model.Study;
import org.obehave.model.Subject;


public class ContextMenuTreeCell extends TextFieldTreeCell<String> {
    private ContextMenu addSubject = new ContextMenu();
    private ContextMenu addAction = new ContextMenu();
    private ContextMenu addObservation = new ContextMenu();
    private ContextMenu removeSubject = new ContextMenu();
    private ContextMenu removeAction = new ContextMenu();
    private ContextMenu removeObservation = new ContextMenu();

    private Study study;

    public ContextMenuTreeCell(Study study) {
        super();
        this.study = study;


        MenuItem addSubjectItem = new MenuItem("Add subject");
        MenuItem addActionItem = new MenuItem("Add action");
        MenuItem addObservationItem = new MenuItem("Add observation");
        MenuItem removeSubjectItem = new MenuItem("Remove subject");
        MenuItem removeActionItem = new MenuItem("Remove action");
        MenuItem removeObservationItem = new MenuItem("Remove observation");

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
                    if (item.equals("Subjects")) {
                        setContextMenu(addSubject);
                    } else if (item.equals("Actions")) {
                        setContextMenu(addAction);
                    } else if (item.equals("Observations")) {
                        setContextMenu(addObservation);
                    }
                } else {
                    if (parent.getValue().equals("Subjects")) {
                        setContextMenu(removeSubject);
                    } else if (parent.getValue().equals("Actions")) {
                        setContextMenu(removeAction);
                    } else if (parent.getValue().equals("Observations")) {
                        setContextMenu(removeObservation);
                    }
                }
            }
        }
    }
}
