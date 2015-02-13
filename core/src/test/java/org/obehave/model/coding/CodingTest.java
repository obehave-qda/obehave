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
public class CodingTest {
    private final Subject subject = new Subject("Test subject");
    private final Action action = new Action("Test action");
    private final long millis = 500;

    private Coding coding;

    @Before
    public void prepare() throws FactoryException {
        coding = new Coding(subject, action, millis);

        action.setModifierFactory(new DecimalRangeModifierFactory(0, 10));

        coding.setModifier("3");
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
        assertEquals(coding.getSubject(), subject);
        assertEquals(coding.getAction(), action);
        assertEquals(coding.getStartMs(), millis);
        assertEquals(coding.getModifier().get(), BigDecimal.valueOf(3));
    }
}
