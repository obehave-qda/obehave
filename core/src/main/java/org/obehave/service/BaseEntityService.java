package org.obehave.service;

import org.obehave.exceptions.ServiceException;
import org.obehave.model.Displayable;
import org.obehave.model.Node;

/**
 * @author Markus Möslinger
 */
abstract class BaseEntityService<T> {
    private final Study study;
    private final Node parent;

    public BaseEntityService(Study study, Node parent) {
        this.study = study;
        this.parent = parent;
    }

    protected abstract void checkBeforeSave(T entity) throws ServiceException;

    @SuppressWarnings("unchecked")
    protected T getForName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        for (Displayable d : parent.flatten()) {
            if (d.getDisplayString().equals(name)) {
                return (T) d;
            }
        }

        return null;
    }

    protected Study getStudy() {
        return study;
    }
}
