package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.persistence.impl.NodeDaoImpl;

import java.util.*;

/**
 * A {@code Group} contains children of the type {@code T} and other groups of the same type. No duplicates are allowed, regardless if it's in the same group or in one of the subgroups.
 * <p/>
 * {@code Group} is {@see Iterable}, so using a for each loop will return every item contained in this group or one of it's subgroups.
 */
@DatabaseTable(tableName = "Node", daoClass = NodeDaoImpl.class)
public class Node extends BaseEntity implements Iterable<Displayable>, Displayable {
    public static final String COLUMN_PARENT = "parent";
    public static final String COLUMN_TYPE = "type";

    public static enum Exclusivity {
        /**
         * Multiple state actions are allowed at the same time
         */
        NOT_EXCLUSIVE,
        /**
         * Not recursive - only one element (regardless if subgroup or action) is allowed to be/contain active state actions
         */
        EXCLUSIVE,
        /**
         * Recursive - only one element is allowed to be active
         */
        TOTAL_EXCLUSIVE
    }

    @DatabaseField(columnName = "actionType")
    private Exclusivity exclusivity;

    @DatabaseField(columnName = "initialAction", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Action initialAction;

    @ForeignCollectionField(eager = true)
    private Collection<Node> children = new ArrayList<>();

    // Fields to store the actual data. Don't call them directly, use getData() and setData()!
    // We could have just one generic field "T data", but then there would be problems with ORMLite. This. Sucks.
    @DatabaseField(columnName = COLUMN_TYPE)
    private Class<?> dataType;

    @DatabaseField(columnName = "subject", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Subject subject;
    @DatabaseField(columnName = "action", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Action action;
    @DatabaseField(columnName = "modifierFactory", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private ModifierFactory modifierFactory;
    @DatabaseField(columnName = "observation", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Observation observation;
    @DatabaseField(columnName = COLUMN_PARENT, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Node parent;

    @DatabaseField(columnName = "title")
    private String title;

    private Node() {
        // for frameworks
    }

    public Node(Class<?> dataType) {
        this.dataType = dataType;
    }

    public Node(Displayable data, Class<?> dataType) {
        this(dataType);
        setData(data);
    }

    /**
     * Recursively look for {@code element}.
     * @param element the element to look for
     * @return true, if the element is found either within this group itself, or in one of it's subgroups
     */
    public boolean contains(Displayable element) {
        if (element == null) {
            return false;
        }

        if (element.equals(getData())) {
            return true;
        }

        for(Node node : children) {
            if (node.contains(element)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Recursively look for a {@code child}.
     * @param child the child to look for
     * @return true, if the subgroup is found either within this group itself, or in one of it's subgroups
     */
    public boolean contains(Node child) {
        if (children.contains(child)) {
            return true;
        }

        for(Node node : children) {
            if (node.contains(child)) {
                return true;
            }
        }

        return false;
    }

    public void setData(Displayable data) {
        if (dataType == Subject.class) {
            subject = (Subject) data;
        } else if (dataType == ModifierFactory.class) {
            modifierFactory = (ModifierFactory) data;
        } else if (dataType == Action.class) {
            action = (Action) data;
        } else if (dataType == Observation.class) {
            observation = (Observation) data;
        } else {
            throw new IllegalArgumentException("Can't set data for " + data);
        }

        title = null;
    }

    public Displayable getData() {
        if (dataType == Subject.class) {
            return subject;
        } else if (dataType == ModifierFactory.class) {
            return modifierFactory;
        } else if (dataType == Action.class) {
            return action;
        } else if (dataType == Observation.class) {
            return observation;
        }

        throw new IllegalArgumentException("Can't get data - dataType isn't set correctly!");
    }

    public Node addChild(Displayable data) {
        final Node node = new Node(data, dataType);
        addChild(node);

        return node;
    }

    public boolean addChild(Node node) {
        if (contains(node.getData())) {
            throw new IllegalArgumentException("node " + node.toString() + " already there");
        }

        makeToParent();

        final boolean add = children.add(node);
        node.setParent(this);
        return add;
    }

    public void makeToParent() {
        if (getData() != null) {
            Displayable data = getData();
            setData(null);
            addChild(data);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (getData() != null) {
            makeToParent();
        }

        this.title = title;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        if (this == parent) {
            throw new IllegalArgumentException("Cannot make the node itself as a parent");
        }

        this.parent = parent;
    }

    public Node getParentOf(Displayable data) {
        if (getChildren().isEmpty()) {
            return null;
        }

        for (Node child : getChildren()) {
            if (child.getData() != null && child.getData().equals(data)) {
                return this;
            }

            Node parent = child.getParentOf(data);
            if (parent != null) {
                return parent;
            }
        }

        return null;
    }

    public boolean remove(Node node) {
        return children.remove(node);
    }

    public boolean remove(Displayable data) {
        throw new UnsupportedOperationException("Has to be implemented! Data was " + data);
    }

    public Node getChildren(int i) {
        return getChildren().get(i);
    }

    /**
     * Returns an unmodifiable list of all the children in this group
     * @return an unmodifiable list of all the children in this group
     * @see java.util.Collections#unmodifiableList(java.util.List)
     */
    public List<Node> getChildren() {
        return Collections.unmodifiableList(new ArrayList<>(children));
    }

    /**
     * This method resolves all nested groups to one flattened list.
     * @return a flattened, unmodifiable list, containing first all children of each subgroup, and then the own children
     */
    public List<Displayable> flatten() {
        List<Displayable> flattened = new ArrayList<>();

        for (Node child : children) {
            flattened.addAll(child.flatten());
        }

        if (getData() != null) {
            flattened.add(getData());
        }

        return Collections.unmodifiableList(flattened);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> flattenAs(Class<T> type) {
        List<T> items = new ArrayList<>();

        for (Displayable displayable : flatten()) {
            items.add((T) displayable);
        }

        return items;
    }

    @Override
    public Iterator<Displayable> iterator() {
        // there could be a better way than to flatten the group first. But I don't want to implement a new Iterator...
        return flatten().iterator();
    }

    @Override
    public String getDisplayString() {
        return getData() != null ? getData().getDisplayString() : getTitle();
    }

    private void validateActionNode() {
        if (dataType != Action.class) {
            throw new IllegalStateException("Exlusitivity can only be set for Action nodes!");
        }
    }

    public Exclusivity getExclusivity() {
        validateActionNode();

        return exclusivity;
    }

    public void setExclusivity(Exclusivity exclusivity) {
        validateActionNode();

        if (exclusivity == null) {
            throw new IllegalArgumentException("Exclusivity must not be null!");
        }

        this.exclusivity = exclusivity;
    }

    public Action getInitialAction() {
        validateActionNode();

        return initialAction;
    }

    public void setInitialAction(Action initialAction) {
        validateActionNode();

        this.initialAction = initialAction;
    }

    public Class<?> getDataType() {
        return dataType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Node rhs = (Node) obj;

        return new EqualsBuilder().append(title, rhs.title).append(dataType, rhs.dataType).append(getData(), rhs.getData()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(title).append(dataType).append(getData()).build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("title", title).append("data", getData())
                .append("dataType", dataType).toString();
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }
}
