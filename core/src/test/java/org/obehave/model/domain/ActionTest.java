package org.obehave.model.domain;

import org.junit.Before;
import org.junit.Test;
import org.obehave.model.Action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Markus Möslinger
 */
public class ActionTest {
    private static final String NAME = "Fighting";
    private Action action;

    @Before
    public void prepare() {
        action = new Action(NAME);
    }

    @Test
    public void getWorks() {
        assertEquals(NAME, action.getName());
        assertEquals(0, action.getRecurring());
        assertEquals(null, action.getAlias());
        assertEquals(NAME, action.getDisplayString());
        assertEquals(null, action.getModifierFactory());
        assertEquals(Action.Type.POINT, action.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void noNegativeRecurring() {
        action.setRecurring(-1);
    }

    @Test
    public void reccuringWorks() {
        assertFalse(action.isRecurring());
        action.setRecurring(5);
        assertTrue(action.isRecurring());
        assertEquals(5, action.getRecurring());
    }
}
