package org.obehave.model.domain;

import java.util.List;

/**
 * @author Markus Möslinger
 */
public class Group<T> {
    private List<Group<T>> subGroups;
    private List<T> elements;
}
