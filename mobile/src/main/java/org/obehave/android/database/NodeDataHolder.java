package org.obehave.android.database;

import org.obehave.model.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BaseClass of data which where hold in Nodes. f.e. Subjects, Actions
 */
abstract public class NodeDataHolder<T> {

    public List<T> getData(Node node) {
        if(node == null){
            node = getRootNode();
        }

        List<T> objects = new ArrayList<T>();

        for(Node currentNode: node.getChildren()){
            if(currentNode.getData() != null){
                T subject  = (T) currentNode.getData();
                objects.add(subject);
            }
        }

        return objects;
    }

    public List<Node> getChildren(Node node) {
        List<Node> nodes = new ArrayList<Node>();

        if(node == null){
            node = getRootNode();
        }

        for(Node currentNode: node.getChildren()){
            if(currentNode.getChildren() != null && currentNode.getData() == null){
                nodes.add(currentNode);
            }
        }

        return Collections.unmodifiableList(nodes);
    }
    public abstract Node getRootNode();

}
