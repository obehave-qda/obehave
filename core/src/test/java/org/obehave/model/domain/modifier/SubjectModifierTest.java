package org.obehave.model.domain.modifier;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.obehave.model.domain.Subject;

/**
 * @author Markus Möslinger
 */
public class SubjectModifierTest {
    private static final Subject SUBJECT = new Subject("Sub");
    private SubjectModifier modifier;

    @Before
    public void prepare() {
        modifier = new SubjectModifier(SUBJECT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noConstructionWithNull() {
        new SubjectModifier(null);
    }

    @Test
    public void getGetsValue() {
        Assert.assertEquals(modifier.get(), SUBJECT);
    }
}
