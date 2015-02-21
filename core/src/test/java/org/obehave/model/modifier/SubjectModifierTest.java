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

    @Before
    public void prepare() {
        modifier = new Modifier(SUBJECT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noConstructionWithNull() {
        new Modifier((Subject) null);
    }

    @Test
    public void getGetsValue() {
        Assert.assertEquals(modifier.get(), SUBJECT);
    }
}
