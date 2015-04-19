package org.obehave.view.components.tree;

import com.google.common.eventbus.Subscribe;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.model.Node;
import org.obehave.service.Study;
import org.obehave.util.DisplayWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProjectTreeControl extends TreeView<DisplayWrapper<?>> {
    private static final Logger log = LoggerFactory.getLogger(ProjectTreeControl.class);

    private Study study;

    public ProjectTreeControl() {
        super();

        EventBusHolder.register(this);
    }

    public void setStudy(Study study) {
        this.study = study;

        PopOverHolder popOverHolder = new PopOverHolder(study);
        setCellFactory(param -> new EntityEditTreeCell(study, popOverHolder));

        redoTree();
    }

    @Subscribe
    public void repaintStudy(UiEvent.RepaintStudyTree event) {
        if (study != null) {
            redoTree();
        }
    }

    private void redoTree() {
        if (study == null) {
            log.warn("Not redoing tree, since study is null");
            return;
        }

        log.trace("Redoing project tree");

        TreeItem root = new TreeItem(DisplayWrapper.of(study.getName()));

        TreeItem<DisplayWrapper<Node>> subjectNode = createTreeItem(study.getSubjects());
        TreeItem<DisplayWrapper<Node>> actionNode = createTreeItem(study.getActions());
        TreeItem<DisplayWrapper<Node>> modifierFactoryNode = createTreeItem(study.getModifierFactories());
        TreeItem<DisplayWrapper<Node>> observationsNode = createTreeItem(study.getObservations());

        root.getChildren().addAll(subjectNode, actionNode, modifierFactoryNode, observationsNode);
        root.setExpanded(true);

        setRoot(root);
    }

    private TreeItem<DisplayWrapper<Node>> createTreeItem(Node node) {
        TreeItem<DisplayWrapper<Node>> treeItem = new TreeItem<>();
        treeItem.setExpanded(true);
        treeItem.setValue(DisplayWrapper.of(node));

        List<Node> children = node.getChildren();

        for (Node child : children) {
            treeItem.getChildren().add(createTreeItem(child));
        }

        return treeItem;
    }
}
