package org.obehave.model.domain.modifier;

import org.junit.Before;
import org.junit.Test;
import org.obehave.exceptions.FactoryException;
import org.obehave.model.domain.Subject;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Markus Möslinger
 */
public class SubjectModifierFactoryTest {
    private static final Subject[] VALID_SUBJECTS = new Subject[]{new Subject("NORTH"), new Subject("EAST"), new Subject("WEST"), new Subject("SOUTH")};

    private SubjectModifierFactory factory;

    @Before
    public void prepare() {
        factory = new SubjectModifierFactory(VALID_SUBJECTS);
    }

    @Test
    public void constructionWithoutSubjectsWorks() {
        assertTrue(new SubjectModifierFactory().getValidSubjects().isEmpty());
    }

    @Test
    public void constructionWithSubjectsWorks() {
        assertEquals(factory.getValidSubjects(), new ArrayList<>(Arrays.asList(VALID_SUBJECTS)));
    }

    @Test
    public void addingSubjectsWorks() {
        SubjectModifierFactory factory = new SubjectModifierFactory();
        factory.addValidSubjects(VALID_SUBJECTS);

        assertEquals(factory.getValidSubjects(), new ArrayList<>(Arrays.asList(VALID_SUBJECTS)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructionWithNullDoesntWork() {
        new SubjectModifierFactory((Subject[]) null);
    }

    @Test(expected = FactoryException.class)
    public void noModifierWithInvalidSubject() throws FactoryException {
        factory.create("Not a valid subject");
    }

    @Test
    public void modifierWithValidSubject() throws FactoryException {
        SubjectModifier modifier = factory.create(VALID_SUBJECTS[0].getName());

        assertEquals(modifier.get(), VALID_SUBJECTS[0]);
    }

    @Test
    public void factoryNameAndDisplayString() {
        factory.setName("Random name");
        assertEquals(factory.getName(), "Random name");
        assertEquals(factory.getDisplayString(), "Random name");
    }

    @Test
    public void factoryAlias() {
        factory.setAlias("Random alias");
        assertEquals(factory.getAlias(), "Random alias");
    }
}
