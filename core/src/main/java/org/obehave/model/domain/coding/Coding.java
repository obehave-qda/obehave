package org.obehave.model.domain.coding;

import org.obehave.exceptions.FactoryException;
import org.obehave.model.domain.Action;
import org.obehave.model.domain.Subject;
import org.obehave.model.domain.modifier.Modifier;

/**
 * @author Markus Möslinger
 */
public abstract class Coding {
    private Subject subject;
    private Action action;
    private Modifier modifier;

    public Coding(Subject subject, Action action) {
        setSubject(subject);
        setAction(action);
    }

    public Coding(Subject subject, Action action, String modifierInput) throws FactoryException {
        setSubject(subject);
        setAction(action);
        setModifier(modifierInput);
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject must not be null!");
        }

        this.subject = subject;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        if (action == null) {
            throw new IllegalArgumentException("Action must not be null!");
        }
        this.action = action;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(String input) throws FactoryException {
        this.modifier = action.getModifierFactory().create(input);
    }
}
