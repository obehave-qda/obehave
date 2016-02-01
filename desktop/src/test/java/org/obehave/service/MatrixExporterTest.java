package org.obehave.service;

import org.junit.Before;
import org.junit.Test;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MatrixExporterTest {


    private Action action;
    private List<Subject> subjects;
    private Exporter exportService = new MatrixExporter(new ExcelWriter(new File("../studies")),false);
    private List<Observation> observations;
    private Observation observation;
    private Node nodes;
    private List<Node> nodeList = new ArrayList<>();

    @Before
    public void prepareData() {

        subjects = new ArrayList<Subject>();

        for (int i = 0; i < 10; i++) {
            subjects.add(new Subject("Subject" + i));
        }

        action = new Action("Playing");
        observation = new Observation();
        action.setType(Action.Type.STATE);
        observations = new ArrayList<>();
        Node nodes = new Node(action, Action.class);
        nodeList.add(nodes);
        for (int i = 0; i < 10; i++) {
            observation.addCoding(new Coding(subjects.get(i), action, 1, i + 10));
        }
        observations.add(observation);

    }


    // FIXME test deactivated - should not depend on directory ../studies!
    //@Test
    public void testExportServiceForActionData() throws ServiceException {
        prepareData();
        action.setName("boobling");


        exportService.export(observations, subjects, nodeList);
    }

//    @Test
//    public void testExportServiceForNodeActionData() throws ServiceException {
//        prepareData();
//        action.setName("soodling");
//        exportService.export(observations, subjects, nodeList);
//    }
//
//    @Test
//    public void testExportServiceForNodeActionDataWithMoreThanOneNode() throws ServiceException {
//        prepareData();
//
//        nodes = new Node(action, Action.class);
//        Action action2 = new Action("Doodling");
//        Node node2 = new Node(action2, Action.class);
//        nodes.addChild(node2);
//        nodes.setTitle("playing and doodling");
//        exportService.export(observations, subjects, nodeList);
//    }
//
//    @Test
//    public void testExportServiceWithSubjectModifier() throws FactoryException, ServiceException {
//
//        prepareData();
//        Subject sub = new Subject("sub");
//        ModifierFactory mf = new ModifierFactory(sub);
//
//        for (Subject s : subjects) {
//            mf.addValidSubjects(s);
//        }
//
//        assertEquals(mf.getValidSubjects().size(), subjects.size() + 1);
//        action.setModifierFactory(mf);
//        action.setName("extralongshittynametotestifthereisanycharrestrictionwiththeapachecommonsapi");
//
//        for (Observation o : observations) {
//            for (Coding c : o.getCodings()) {
//                c.setModifier("Subject1");
//            }
//        }
//
//        exportService.export(observations, subjects, nodeList);
//
//    }
}