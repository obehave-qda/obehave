package org.obehave.service;

import org.junit.Before;
import org.junit.Test;
import org.obehave.exceptions.FactoryException;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.*;
import org.obehave.model.modifier.ModifierFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MatrixExporterTest {


    private Action action;
    private List<Subject> subjects;
    private MatrixExporter exportService = new MatrixExporter(new File("../studies"));
    private List<Observation> observations;
    private Observation observation;
    private Node nodes;

    @Before
    public void prepareData() {

        subjects = new ArrayList<Subject>();

        for (int i = 0; i < 10; i++) {
            subjects.add(new Subject("Subject" + i));
        }

        action = new Action("Playing");
        observation = new Observation();

        observations = new ArrayList<Observation>();

        for (int i = 0; i < 10; i++) {
            observation.addCoding(new Coding(subjects.get(i), action, 1, i + 10));
        }
        observations.add(observation);

    }


    @Test
    public void testExportServiceForActionData() throws ServiceException {
        prepareData();
        action.setName("boobling");

        exportService.exportAction(observations, subjects, action);
    }

    @Test
    public void testExportServiceForNodeActionData() throws ServiceException {
        prepareData();
        action.setName("soodling");
        nodes = new Node(action, Action.class);

        exportService.exportActionGroup(observations, subjects, nodes);
    }

    @Test
    public void testExportServiceForNodeActionDataWithMoreThanOneNode() throws ServiceException {
        prepareData();

        nodes = new Node(action, Action.class);
        Action action2 = new Action("Doodling");
        Node node2 = new Node(action2, Action.class);
        nodes.addChild(node2);
        nodes.setTitle("playing and doodling");
        exportService.exportActionGroup(observations, subjects, nodes);
    }

    @Test
    public void testExportServiceWithSubjectModifier() throws FactoryException, ServiceException {

        prepareData();
        Subject sub = new Subject("sub");
        ModifierFactory mf = new ModifierFactory(sub);

        for (Subject s : subjects) {
            mf.addValidSubjects(s);
        }

        assertEquals(mf.getValidSubjects().size(), subjects.size() + 1);
        action.setModifierFactory(mf);
        action.setName("extralongshittynametotestifthereisanycharrestrictionwiththeapachecommonsapi");

        for (Observation o : observations) {
            for (Coding c : o.getCodings()) {
                c.setModifier("Subject1");
            }
        }

        exportService.exportAction(observations, subjects, action);

    }
}