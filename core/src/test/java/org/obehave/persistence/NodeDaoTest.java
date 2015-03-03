package org.obehave.persistence;

import org.junit.Before;
import org.junit.Test;
import org.obehave.model.Action;
import org.obehave.model.Node;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Markus Möslinger
 */
public class NodeDaoTest extends DatabaseTest {
    @Before
    public void prepare() throws SQLException {
        Daos.get().node().executeRawNoArgs("DELETE FROM Node");
    }


    @Test
    public void persistingNode() throws SQLException {
        Subject s1 = new Subject("First Subject");
        Subject s2 = new Subject("Second Subject");
        Subject s3 = new Subject("Third Subject");
        Subject s4 = new Subject("Fourth Subject");
        Daos.get().subject().create(s1);
        Daos.get().subject().create(s2);
        Daos.get().subject().create(s3);
        Daos.get().subject().create(s4);

        Node node1 = new Node(Subject.class);
        Node node2 = new Node(Subject.class);
        Node node3 = new Node(Subject.class);
        Node node4 = new Node(Subject.class);
        Node node5 = new Node(Subject.class);
        Node node6 = new Node(Subject.class);
        Node node7 = new Node(Subject.class);

        node1.addChild(node2);
        node1.addChild(node3);
        node3.addChild(node4);
        node3.addChild(node7);
        node4.addChild(node5);
        node4.addChild(node6);

        node1.setTitle("Root - Parent of Node 2 (Subject 1) and Node 3");
        node2.setData(s1);
        node3.setTitle("Parent of Node 4, 5, 6 and 7 (Subject 4)");
        node4.setTitle("Parent of Node 5 (Subject 2) and Node 6 (Subject 3");
        node5.setData(s2);
        node6.setData(s3);
        node7.setData(s4);


        Daos.get().node().create(node1);

        Node loadedNode1 = Daos.get().node().queryForSameId(node1);
        Node loadedNode2 = loadedNode1.getChildren(0);
        Node loadedNode3 = loadedNode1.getChildren(1);
        Node loadedNode4 = loadedNode3.getChildren(0);
        Node loadedNode5 = loadedNode4.getChildren(0);
        Node loadedNode6 = loadedNode4.getChildren(1);
        Node loadedNode7 = loadedNode3.getChildren(1);

        assertEquals(node1, loadedNode1);
        assertEquals(node2, loadedNode2);
        assertEquals(node3, loadedNode3);
        assertEquals(node4, loadedNode4);
        assertEquals(node5, loadedNode5);
        assertEquals(node6, loadedNode6);
        assertEquals(node7, loadedNode7);
    }

    @Test
    public void getRootWorks() throws SQLException {
        Subject subject = new Subject("Subject");
        Action action = new Action("Action");
        ModifierFactory modifierFactory = new ModifierFactory("Modifiervalue1", "Modifiervalue2");
        Observation observation1 = new Observation("Observation1");
        Observation observation2 = new Observation("Observation2");

        Daos.get().subject().create(subject);
        Daos.get().action().create(action);
        Daos.get().modifierFactory().create(modifierFactory);
        Daos.get().observation().create(observation1);
        Daos.get().observation().create(observation2);

        Node s1 = new Node(Subject.class);
        Node a1 = new Node(Action.class);
        Node a2 = new Node(Action.class);
        Node m1 = new Node(ModifierFactory.class);
        Node m2 = new Node(ModifierFactory.class);
        Node o1 = new Node(Observation.class);
        Node o2 = new Node(Observation.class);

        s1.setData(subject);
        s1.setTitle("Parent of subject");

        a1.addChild(a2);
        a2.setData(action);

        m1.setTitle("Parent of Modifier");
        m1.addChild(m2);
        m2.setData(modifierFactory);

        o1.setData(observation1);
        o2.setData(observation2);

        Daos.get().node().create(s1);
        Daos.get().node().create(a1);
        Daos.get().node().create(a2);
        Daos.get().node().create(m1);
        Daos.get().node().create(m2);
        Daos.get().node().create(o1);
        Daos.get().node().create(o2);

        // s1, a1 and m1 should be the only roots, but o1 and o2 are both ones
        List<Node> rootSubject = Daos.get().node().getRoot(Subject.class);
        assertEquals(1, rootSubject.size());
        assertEquals("Parent of subject", rootSubject.get(0).getTitle());
        assertNull(rootSubject.get(0).getData());
        assertEquals(subject, rootSubject.get(0).getChildren(0).getData());

        List<Node> rootAction = Daos.get().node().getRoot(Action.class);
        assertEquals(1, rootAction.size());
        assertNull(rootAction.get(0).getTitle());
        assertNull(rootAction.get(0).getData());
        assertEquals(action, rootAction.get(0).getChildren(0).getData());

        List<Node> rootModifierFactory = Daos.get().node().getRoot(ModifierFactory.class);
        assertEquals(1, rootModifierFactory.size());
        assertEquals("Parent of Modifier", rootModifierFactory.get(0).getTitle());
        assertNull(rootModifierFactory.get(0).getData());
        assertEquals(modifierFactory, rootModifierFactory.get(0).getChildren(0).getData());

        List<Node> rootObservation = Daos.get().node().getRoot(Observation.class);
        assertEquals(2, rootObservation.size());
        assertNull(rootObservation.get(0).getTitle(), rootObservation.get(1).getTitle());
        assertEquals(observation1, rootObservation.get(0).getData());
        assertEquals(observation2, rootObservation.get(1).getData());
        assertEquals(0, rootObservation.get(0).getChildren().size());
        assertEquals(0, rootObservation.get(1).getChildren().size());
    }
}
