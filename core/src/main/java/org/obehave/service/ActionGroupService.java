package org.obehave.service;

import org.obehave.exceptions.ServiceException;
import org.obehave.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Markus.Moeslinger on 05.06.2015.
 */
public class ActionGroupService extends NodeService {
    protected ActionGroupService(Study study) {
        super(study);
    }

    @Override
    public void save(Node node) throws ServiceException {
        checkBeforeSave(node);
        super.save(node);
    }

    private void checkBeforeSave(Node node) throws ServiceException {
        for (Node n : getActionGroups()) {
            if (node.getTitle().equals(n.getTitle())) {
                throw new ServiceException("There is already an existing action group with the name " + node.getTitle());
            }
        }
    }

    private List<Node> getActionGroups() {
        final List<Node> actionGroups = new ArrayList<>();

        for (Node actionNode : study.getActions().getChildren()) {
            if (actionNode.getData() == null) {
                actionGroups.add(actionNode);
            }
        }

        return actionGroups;
    }
}
