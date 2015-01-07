package org.obehave.model.domain.modifier;

import org.obehave.model.domain.Subject;

import java.util.Set;

/**
 * @author Markus Möslinger
 */
public class SubjectModifierFactory extends ModifierFactory<SubjectModifier> {
    private Set<Subject> validSubjects;

    @Override
    public SubjectModifier create(String subjectName) {
        Subject s = null; // look in study for subject with same name
        if (validSubjects.contains(s)) {
            return new SubjectModifier(s);
        } else {
            throw new IllegalArgumentException("This subject isn't allowed here!"); // more precise message
        }
    }
}
