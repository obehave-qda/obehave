package org.obehave.persistence;

import org.junit.Test;
import org.obehave.model.Node;
import org.obehave.model.Subject;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class NodeDaoTest extends DatabaseTest {
    @Test
    public void persistingNode() throws SQLException {
        Subject s1 = new Subject("First Subject");
        Subject s2 = new Subject("Second Subject");
        Subject s3 = new Subject("Third Subject");
        Subject s4 = new Subject("Fourth Subject");
        Daos.subject().create(s1);
        Daos.subject().create(s2);
        Daos.subject().create(s3);
        Daos.subject().create(s4);

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


        Daos.node().create(node1);

        Node loadedNode1 = Daos.node().queryForSameId(node1);
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
}
