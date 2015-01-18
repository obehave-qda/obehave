package org.obehave.model.tree;

import org.obehave.model.Displayable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A {@code Group} contains children of the type {@code T} and other groups of the same type. No duplicates are allowed, regardless if it's in the same group or in one of the subgroups.
 * <p/>
 * {@code Group} is {@see Iterable}, so using a for each loop will return every item contained in this group or one of it's subgroups.
 * @param <T> the type of the children to store in this {@code Group}
 */
public class Node<T extends Displayable> implements Iterable<T>, Displayable {
    private final ArrayList<Node<T>> children = new ArrayList<>();
    private T data;
    private String title = "";

    public Node() {

    }

    public Node(T data) {
        this.data = data;
    }

    /**
     * Recursively look for {@code element}.
     * @param element the element to look for
     * @return true, if the element is found either within this group itself, or in one of it's subgroups
     */
    public boolean contains(T element) {
        if (data.equals(element)) {
            return true;
        }

        for(Node<T> node : children) {
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

        for(Node<T> node : children) {
            if (node.contains(child)) {
                return true;
            }
        }

        return false;
    }

    public void setData(T data) {
        title = "";
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public boolean addAsChild(T data) {
        if (this.data != null) {
            children.add(new Node<>(this.data));
            this.data = null;
        }

        return children.add(new Node<>(data));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (data != null) {
            throw new IllegalStateException("Can't set title if there is data");
        }

        this.title = title;
    }

    public boolean remove(Node<T> node) {
        return children.remove(node);
    }

    public boolean remove(T data) {
        return false;
    }

    /**
     * Returns an unmodifiable list of all the children in this group
     * @return an unmodifiable list of all the children in this group
     * @see java.util.Collections#unmodifiableList(java.util.List)
     */
    public List<Node<T>> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Moves a subgroup to a new position within it's list
     * @param subgroup the subgroup to move
     * @param position the position to move the element to
     * @throws IllegalArgumentException if the subgroup isn't in this group
     * @throws IndexOutOfBoundsException if the new position isn't a valid list index
     */
    public void move(Node<T> subgroup, int position) {
        move(children, subgroup, position);
    }

    /**
     * Moves an element of a list to a new position.
     * @param list the list containing the element to move
     * @param element the element to move
     * @param position the position to move the element to
     * @param <L> the type of the element to move
     * @throws IllegalArgumentException if the element isn't in the given list
     * @throws IndexOutOfBoundsException if the new position isn't a valid list index
     */
    private <L> void move(List<L> list, L element, int position) {
        final int oldIndex = list.indexOf(element);
        if (oldIndex == -1) {
            throw new IllegalArgumentException("Could not find element in list");
        }

        if (position == oldIndex) {
            return;
        }

        if (position < 0 && position >= list.size()) {
            throw new IndexOutOfBoundsException("Position isn't valid");
        }

        children.remove(oldIndex);

        // if we have to move the element more to the end of the list, we have to decrease the index by one, because of element's removal
        final int newIndex = position > oldIndex ? position - 1 : position;

        list.add(newIndex, element);
    }

    /**
     * This method resolves all nested groups to one flattened list.
     * @return a flattened, unmodifiable list, containing first all children of each subgroup, and then the own children
     */
    public List<T> flatten() {
        List<T> flattened = new ArrayList<>();

        for (Node<T> child : children) {
            flattened.addAll(child.flatten());
        }

        flattened.add(data);

        return Collections.unmodifiableList(flattened);
    }

    @Override
    public Iterator<T> iterator() {
        // there could be a better way than to flatten the group first. But I don't want to implement a new Iterator...
        return flatten().iterator();
    }

    @Override
    public String getDisplayString() {
        return data != null ? data.getDisplayString() : getTitle();
    }
}
