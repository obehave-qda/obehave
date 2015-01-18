package org.obehave.model.modifier;

import org.obehave.model.Subject;

/**
 * A {@code SubjectModifier} modifies a coded action with another {@link org.obehave.model.Subject}
 */
public class SubjectModifier extends Modifier<Subject> {
    private final Subject subject;

    SubjectModifier(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("subject must not be null!");
        }

        this.subject = subject;
    }

    public Subject get() {
        return subject;
    }
}
