package org.obehave.model;

import org.junit.Before;
import org.junit.Test;
import org.obehave.exceptions.FactoryException;
import org.obehave.model.modifier.ModifierFactory;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class CodingTest {
    private final Subject subject = new Subject("Test subject");
    private final Action stateAction = new Action("Test state action", Action.Type.STATE);
    private final long millis = 500;
    private final long endMillis = millis + 250;

    private Coding stateCoding;

    @Before
    public void prepare() throws FactoryException {
        stateCoding = new Coding(subject, stateAction, millis, endMillis);

        stateAction.setModifierFactory(new ModifierFactory(0, 10));
        stateCoding.setModifier("3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithoutSubject() {
        new Coding(null, stateAction, millis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithoutAction() {
        new Coding(subject, null, millis);
    }

    @Test
    public void accessingFieldsShouldWork() throws FactoryException {
        assertEquals(subject, stateCoding.getSubject());
        assertEquals(stateAction, stateCoding.getAction());
        assertEquals(millis, stateCoding.getStartMs());
        assertEquals(BigDecimal.valueOf(3), stateCoding.getModifier().get());
        assertEquals(endMillis - millis, stateCoding.getDuration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithoutSubjectButMillis() {
        new Coding(null, stateAction, millis, endMillis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithoutActionButMillis() {
        new Coding(subject, null, millis, endMillis);
    }
}
