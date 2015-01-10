package org.obehave.model.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@code Group} contains elements of the type {@code T} and other groups of the same type. No duplicates are allowed, regardless if it's in the same group or in one of the subgroups.
 * @param <T> the type of the elements to store in this {@code Group}
 */
public class Group<T> {
    private final List<Group<T>> subgroups = new ArrayList<>();
    private final List<T> elements = new ArrayList<>();

    /**
     * Recursively look for {@code element}.
     * @param element the element to look for
     * @return true, if the element is found either within this group itself, or in one of it's subgroups
     */
    public boolean contains(T element) {
        if (elements.contains(element)) {
            return true;
        }

        for(Group<T> group : subgroups) {
            if (group.contains(element)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Recursively look for {@code subgroup}.
     * @param subgroup the subgroup to look for
     * @return true, if the subgroup is found either within this group itself, or in one of it's subgroups
     */
    public boolean contains(Group subgroup) {
        if (subgroups.contains(subgroup)) {
            return true;
        }

        for(Group<T> group : subgroups) {
            if (group.contains(subgroup)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds an element to the end of this group, if there is not already an equal element in there
     * @param element the element to add
     * @return true, if there was an element added
     */
    public boolean add(T element) {
        return !elements.contains(element) && elements.add(element);
    }

    /**
     * Adds an subgroup to the end of this group, if there is not already an equal subgroup in there
     * @param subgroup the subgroup to add
     * @return true, if there was a subgroup added
     */
    public boolean add(Group<T> subgroup) {
        return !subgroups.contains(subgroup) && subgroups.add(subgroup);
    }

    /**
     * Gets a subgroup from the group
     * @param i the index of the subgroup to return
     * @return the subgroup
     * @see java.util.List#get(int)
     */
    public Group<T> getSubgroup(int i) {
        return subgroups.get(0);
    }

    /**
     * Gets an element from the group
     * @param i the index of the element to return
     * @return the element
     * @see java.util.List#get(int)
     */
    public T getElement(int i) {
        return elements.get(i);
    }

    /**
     * Returns an unmodifiable list of all the elements in this group
     * @return an unmodifiable list of all the elements in this group
     * @see java.util.Collections#unmodifiableList(java.util.List)
     */
    public List<T> getElements() {
        return Collections.unmodifiableList(elements);
    }

    /**
     * Returns an unmodifiable list of all the subgroups in this group
     * @return an unmodifiable list of all the subgroups in this group
     * @see java.util.Collections#unmodifiableList(java.util.List)
     */
    public List<Group<T>> getSubgroups() {
        return Collections.unmodifiableList(subgroups);
    }

    /**
     * Removes an element from this group at a given index
     * @param i the index of the element to remove
     * @return true, if the element was contained by this group and is now removed
     * @see java.util.List#remove(int)
     */
    public boolean removeElement(int i) {
        return elements.remove(elements.get(i));
    }

    /**
     * Removes an subgroup from this group at a given index
     * @param i the index of the subgroup to remove
     * @return true, if the subgroup was contained by this group and is now removed
     * @see java.util.List#remove(int)
     */
    public boolean removeSubgroup(int i) {
        return subgroups.remove(subgroups.get(i));
    }

    /**
     * Removes an element from this group
     * @param element the element to remove
     * @return true, if the element was contained by this group and is now removed
     * @see java.util.List#remove(Object)
     */
    public boolean remove(T element) {
        return elements.remove(element);
    }

    /**
     * Removes a subgroup from this group
     * @param subgroup the subgroup to remove
     * @return true, if the subgroup was contained by this group and is now removed
     * @see java.util.List#remove(Object)
     */
    public boolean remove(Group<T> subgroup) {
        return subgroups.remove(subgroup);
    }

    /**
     * Moves a position to a new position within it's list
     * @param position the position to move
     * @param position the position to move the element to
     * @throws java.lang.IllegalArgumentException if the element isn't in this group
     * @throws java.lang.IndexOutOfBoundsException if the new position isn't a valid list index
     */
    public void move(T element, int position) {
        move(elements, element, position);
    }

    /**
     * Moves a subgroup to a new position within it's list
     * @param subgroup the subgroup to move
     * @param position the position to move the element to
     * @throws java.lang.IllegalArgumentException if the subgroup isn't in this group
     * @throws java.lang.IndexOutOfBoundsException if the new position isn't a valid list index
     */
    public void move(Group subgroup, int position) {
        move(subgroups, subgroup, position);
    }

    /**
     * Moves an element of a list to a new position.
     * @param list the list containing the element to move
     * @param element the element to move
     * @param position the position to move the element to
     * @param <L> the type of the element to move
     * @throws java.lang.IllegalArgumentException if the element isn't in the given list
     * @throws java.lang.IndexOutOfBoundsException if the new position isn't a valid list index
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

        elements.remove(oldIndex);

        // if we have to move the element more to the end of the list, we have to decrease the index by one, because of element's removal
        final int newIndex = position > oldIndex ? position - 1 : position;

        list.add(newIndex, element);
    }
}
