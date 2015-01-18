package org.obehave.model.coding;

import org.junit.Before;
import org.junit.Test;
import org.obehave.exceptions.FactoryException;
import org.obehave.model.Action;
import org.obehave.model.Subject;
import org.obehave.model.modifier.DecimalRangeModifierFactory;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class StateCodingTest {
    private final Subject subject = new Subject("Test subject");
    private final Action action = new Action("Test action");
    private final long millis = 500;
    private final long endMillis = millis + 250;

    private StateCoding coding;

    @Before
    public void prepare() throws FactoryException {
        coding = new StateCoding(subject, action, millis, endMillis);

        action.setModifierFactory(new DecimalRangeModifierFactory(0, 10));

        coding.setModifier("3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithoutSubject() {
        new StateCoding(null, action, millis, endMillis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithoutAction() {
        new StateCoding(subject, null, millis, endMillis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithNullAsModifierInput() throws FactoryException {
        new StateCoding(subject, action, null, millis, endMillis);
    }

    @Test
    public void accessingFieldsShouldWork() {
        StateCoding coding = new StateCoding(new Subject("Dummy"), new Action("Dummy"), 300, 400);

        coding.setSubject(subject);
        coding.setAction(action);
        coding.setStartMs(millis);

        assertEquals(coding.getSubject(), subject);
        assertEquals(coding.getAction(), action);
        assertEquals(coding.getStartMs(), millis);
        assertEquals(coding.getModifier().get(), BigDecimal.valueOf(3));
        assertEquals(coding.getDuration(), 400 - 300);
    }
}
