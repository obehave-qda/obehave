package org.obehave.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * @author Markus Möslinger
 */
public class NodeTest {
    private static final Subject SUBJECT1 = new Subject("Testname1");
    private static final Subject SUBJECT2 = new Subject("Testname2");
    private static final Subject SUBJECT3 = new Subject("Testname3");
    private static final Subject SUBJECT4 = new Subject("Testname4");
    private Node rootNode;

    @Before
    public void prepare() {
        rootNode = new Node(Subject.class);
    }

    @Test
    public void emptyNodeIsEmpty() {
        assertTrue(rootNode.getChildren().isEmpty());
        assertNull(rootNode.getData());
    }

    @Test
    public void singleNodeDoesntHaveChildren() {
        Node node = new Node(SUBJECT1, Subject.class);

        assertTrue(node.getChildren().isEmpty());
        assertEquals(SUBJECT1, node.getData());
    }

    @Test
    public void addingChildToSingleNodeMakesToChildren() {
        Node node = new Node(SUBJECT1, Subject.class);

        assertTrue(node.getChildren().isEmpty());

        node.addChild(SUBJECT2);

        assertNull(node.getData());
        assertEquals(SUBJECT1, node.getChildren().get(0).getData());
        assertEquals(SUBJECT2, node.getChildren().get(1).getData());
    }

    @Test
    public void settingTitleForSingleNodeMovesDataToChild() {
        rootNode.setData(SUBJECT1);

        assertTrue(rootNode.getChildren().isEmpty());

        rootNode.setTitle("Abc");
        assertEquals("Abc", rootNode.getTitle());

        assertEquals(1, rootNode.getChildren().size());
        assertEquals(SUBJECT1, rootNode.getChildren(0).getData());
    }

    @Test
    public void settingTitleForNodeWithChildWorks() {
        rootNode.addChild(SUBJECT1);

        assertEquals(SUBJECT1, rootNode.getChildren(0).getData());

        rootNode.setTitle("Abc");
        assertEquals("Abc", rootNode.getTitle());

        assertEquals(SUBJECT1, rootNode.getChildren(0).getData());
        assertNull(rootNode.getChildren(0).getTitle());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cannotModifyChildrenFromGetter() {
        rootNode.addChild(SUBJECT1);
        rootNode.addChild(SUBJECT2);

        rootNode.getChildren().add(new Node(SUBJECT3, Subject.class));
    }

    @Test
    public void iteratingWorks() {
        rootNode.addChild(SUBJECT1);
        Node nodeOfSubject2 = rootNode.addChild(SUBJECT2);
        nodeOfSubject2.addChild(SUBJECT4);
        nodeOfSubject2.getChildren().get(0).addChild(SUBJECT3);

        assertEquals(4, rootNode.flatten().size());

        Iterator iter = rootNode.iterator();
        assertEquals(SUBJECT1, iter.next());
        assertEquals(SUBJECT2, iter.next());
        assertEquals(SUBJECT3, iter.next());
        assertEquals(SUBJECT4, iter.next());
        assertFalse(iter.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void onlyOneEqualItem() {
        rootNode.addChild(SUBJECT1);
        Node nodeOfSubject2 = rootNode.addChild(SUBJECT2);
        nodeOfSubject2.addChild(SUBJECT4);
        nodeOfSubject2.getChildren().get(0).addChild(SUBJECT3);

        rootNode.addChild(SUBJECT3);
    }

    @Test
    public void settingExlusitivityForActionNodeWorks() {
        final Action action = new Action("Testaction");
        Node n = new Node(action, Action.class);
        n.setExclusivity(Node.Exclusivity.TOTAL_EXCLUSIVE);

        assertEquals(action, n.getData());
        assertEquals(Node.Exclusivity.TOTAL_EXCLUSIVE, n.getExclusivity());
    }

    @Test(expected = IllegalStateException.class)
    public void settingExlusitivityForSubjectNodeThrowsException() {
        Node n = new Node(SUBJECT1, Subject.class);
        n.setExclusivity(Node.Exclusivity.TOTAL_EXCLUSIVE);
    }

    @Test
    public void displayStringReturnsNameOfAction() {
        Action action = new Action("Testaction");
        Node node = new Node(action, Action.class);

        assertEquals("Testaction", node.getDisplayString());
    }
}
