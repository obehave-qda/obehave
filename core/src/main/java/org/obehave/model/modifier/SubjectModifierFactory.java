package org.obehave.model.modifier;

import org.obehave.exceptions.FactoryException;
import org.obehave.model.Subject;

import java.util.*;

/**
 * Objects of this class can create
 */
public class SubjectModifierFactory extends ModifierFactory<SubjectModifier> {
    private List<Subject> validSubjects = new ArrayList<>();

    public SubjectModifierFactory() {

    }

    public SubjectModifierFactory(Subject... subjects) {
        addValidSubjects(subjects);
    }

    /**
     * If {@code subjectName} is parsable to a valid {@code Subject} stored in this factory, return a new {@code SubjectModifier} containing the parsed {@code Subject}
     * @param subjectName a string matching either the name or the alias of a subject valid in this factory's context
     * @return a new {@code SubjectModifier} containing the matched subject
     */
    @Override
    public SubjectModifier create(String subjectName) throws FactoryException {
        for (Subject subject : validSubjects) {
            if (subjectName.equals(subject.getName()) || subjectName.equals(subject.getAlias())) {
                return new SubjectModifier(subject);
            }
        }

        throw new FactoryException("This subject isn't allowed here!"); // more precise message
    }

    public boolean addValidSubjects(Subject... subjects) {
        if (subjects == null) {
            throw new IllegalArgumentException("Subjects must not be null");
        }

        return validSubjects.addAll(Arrays.asList(subjects));
    }

    public List<Subject> getValidSubjects() {
        return Collections.unmodifiableList(validSubjects);
    }
}
