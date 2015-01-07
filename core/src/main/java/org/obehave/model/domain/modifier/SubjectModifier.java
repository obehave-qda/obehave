package org.obehave.model.domain.modifier;

import org.obehave.model.domain.Subject;

/**
 * @author Markus Möslinger
 */
public class SubjectModifier extends Modifier<Subject> {
    private final Subject subject;

    SubjectModifier(Subject subject) {
        this.subject = subject;
    }

    public Subject get() {
        return subject;
    }
}
