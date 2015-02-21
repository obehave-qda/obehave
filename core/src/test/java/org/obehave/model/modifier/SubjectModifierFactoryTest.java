package org.obehave.model.modifier;

import org.junit.Before;
import org.junit.Test;
import org.obehave.exceptions.FactoryException;
import org.obehave.model.Subject;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Markus Möslinger
 */
public class SubjectModifierFactoryTest {
    private static final Subject[] VALID_SUBJECTS = new Subject[]{new Subject("NORTH"), new Subject("EAST"), new Subject("WEST"), new Subject("SOUTH")};

    private ModifierFactory factory;

    @Before
    public void prepare() {
        factory = new ModifierFactory(VALID_SUBJECTS);
    }

    @Test
    public void constructionWithoutSubjectsWorks() {
        assertTrue(new ModifierFactory((Subject[]) null).getValidSubjects().isEmpty());
    }

    @Test
    public void constructionWithSubjectsWorks() {
        assertEquals(new ArrayList<>(Arrays.asList(VALID_SUBJECTS)), factory.getValidSubjects());
    }

    @Test
    public void addingSubjectsWorks() {
        ModifierFactory factory = new ModifierFactory((Subject[]) null);
        factory.addValidSubjects(VALID_SUBJECTS);

        assertEquals(new ArrayList<>(Arrays.asList(VALID_SUBJECTS)), factory.getValidSubjects());
    }

    @Test
    public void constructionWithNullDoesWork() {
        assertTrue(new ModifierFactory((Subject[]) null).getValidSubjects().isEmpty());
    }

    @Test(expected = FactoryException.class)
    public void noModifierWithInvalidSubject() throws FactoryException {
        factory.create("Not a valid subject");
    }

    @Test
    public void modifierWithValidSubject() throws FactoryException {
        Modifier modifier = factory.create(VALID_SUBJECTS[0].getName());

        assertEquals(modifier.get(), VALID_SUBJECTS[0]);
    }

    @Test
    public void factoryNameAndDisplayString() {
        factory.setName("Random name");
        assertEquals("Random name", factory.getName());
        assertEquals("Random name", factory.getDisplayString());
    }

    @Test
    public void factoryAlias() {
        factory.setAlias("Random alias");
        assertEquals("Random alias", factory.getAlias());
    }
}
