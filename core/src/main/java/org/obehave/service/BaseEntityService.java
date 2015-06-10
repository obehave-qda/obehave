package org.obehave.service;

import org.obehave.exceptions.ServiceException;
import org.obehave.model.Displayable;
import org.obehave.model.Node;

import java.io.Serializable;

/**
 * @author Markus Möslinger
 */
abstract class BaseEntityService<T> implements Serializable{
    private static final long serialVersionUID = 1L;
    private final Study study;
    private final Node parent;

    public BaseEntityService(Study study, Node parent) {
        this.study = study;
        this.parent = parent;
    }

    protected abstract void checkBeforeSave(T entity) throws ServiceException;

    /**
     * Since entities {@link org.obehave.model.Subject}, {@link org.obehave.model.Action},
     * {@link org.obehave.model.modifier.ModifierFactory} and  {@link org.obehave.model.Observation} must have a unique
     * name, this service method can fetch the entity of a study with a given name.
     * <p/>
     * This method is case in-sensitive.
     * @param name The name to look forr
     * @return the entity, or null if none found
     */
    @SuppressWarnings("unchecked")
    public T getForName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        for (Displayable d : parent.flatten()) {
            if (d.getDisplayString().equalsIgnoreCase(name)) {
                return (T) d;
            }
        }

        return null;
    }

    protected Study getStudy() {
        return study;
    }
}
