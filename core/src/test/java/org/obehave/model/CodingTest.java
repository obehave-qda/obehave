package org.obehave.model;

import org.junit.Before;
import org.junit.Test;
import org.obehave.exceptions.FactoryException;
import org.obehave.model.Action;
import org.obehave.model.Coding;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class CodingTest {
    private final Subject subject = new Subject("Test subject");
    private final Action action = new Action("Test action");
    private final long millis = 500;
    private final long endMillis = millis + 250;

    private Coding coding;
    private Coding stateCoding;

    @Before
    public void prepare() throws FactoryException {
        coding = new Coding(subject, action, millis);
        stateCoding = new Coding(subject, action, millis, endMillis);

        action.setModifierFactory(new ModifierFactory(0, 10));

        coding.setModifier("3");
        stateCoding.setModifier("3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithoutSubject() {
        new Coding(null, action, millis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithoutAction() {
        new Coding(subject, null, millis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithNullAsModifierInput() throws FactoryException {
        new Coding(subject, action, null, millis);
    }

    @Test
    public void accessingFieldsShouldWork() throws FactoryException {
        assertEquals(subject, stateCoding.getSubject());
        assertEquals(action, stateCoding.getAction());
        assertEquals(millis, stateCoding.getStartMs());
        assertEquals(BigDecimal.valueOf(3), stateCoding.getModifier().get());
        assertEquals(endMillis - millis, stateCoding.getDuration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithoutSubjectButMillis() {
        new Coding(null, action, millis, endMillis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithoutActionButMillis() {
        new Coding(subject, null, millis, endMillis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithNullAsModifierInputButMillis() throws FactoryException {
        new Coding(subject, action, null, millis, endMillis);
    }
}
