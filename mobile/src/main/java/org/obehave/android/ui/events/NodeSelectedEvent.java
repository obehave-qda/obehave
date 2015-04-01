package org.obehave.android.ui.events;

import org.obehave.android.ui.events.GuiEvent;
import org.obehave.model.Node;

/**
 * Created by patrick on 25.02.2015.
 */
public class NodeSelectedEvent extends GuiEvent {

    public enum NodeType{
        SUBJECT,
        ACTION
    }

    private final Node node;
    private final NodeType type;

    public NodeSelectedEvent(Node node, NodeType type) {
        this.node = node;
        this.type = type;
    }


    public Node getNode() {
        return this.node;
    }

    public NodeType getNodeType(){
        return type;
    }

}
