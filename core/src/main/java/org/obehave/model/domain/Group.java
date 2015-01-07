package org.obehave.model.domain;

import java.util.List;

/**
 * @author Markus Möslinger
 */
public class Group<T> {
    private List<Group<T>> subGroups;
    private List<T> elements;

    public boolean contains(T element) {
        if (elements.contains(element)) {
            return true;
        }

        for(Group<T> group : subGroups) {
            if (group.contains(element)) {
                return true;
            }
        }

        return false;
    }

    public boolean add(T element) {
        return elements.add(element);
    }

    public boolean add(Group<T> subGroup) {
        return subGroups.add(subGroup);
    }

    public boolean remove(T element) {
        return elements.remove(element);
    }

    public boolean remove(Group<T> subGroup) {
        return subGroups.remove(subGroup);
    }
}
