package org.obehave.model.modifier;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.obehave.model.Subject;

/**
 * @author Markus Möslinger
 */
public class SubjectModifierTest {
    private static final Subject SUBJECT = new Subject("Sub");
    private Modifier modifier;
    private ModifierFactory modifierFactory;

    @Before
    public void prepare() {
        modifierFactory = new ModifierFactory(SUBJECT);
        modifier = new Modifier(modifierFactory, SUBJECT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noConstructionWithNull() {
        new Modifier(modifierFactory, (Subject) null);
    }

    @Test
    public void getGetsValue() {
        Assert.assertEquals(SUBJECT, modifier.get());
    }
}
