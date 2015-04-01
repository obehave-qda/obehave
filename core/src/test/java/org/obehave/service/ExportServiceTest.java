package org.obehave.service;

import org.junit.Before;
import org.junit.Test;
import org.obehave.model.Action;
import org.obehave.model.Coding;
import org.obehave.model.Observation;
import org.obehave.model.Subject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExportServiceTest {

    private List<Coding> codings;
    private Action action;
    private List<Subject> subjects;
    private ExportService exportService;
    private List<Observation> observations;
    private Observation observation;

    @Before
    public void prepareData() {

        subjects = new ArrayList<Subject>();

        for (int i = 0; i < 10; i++) {
            subjects.add(new Subject("Subject" + i));
        }

        action = new Action("Playing");
        observation = new Observation();
        codings = new ArrayList<Coding>();
        observations = new ArrayList<Observation>();

        for (int i = 0 ; i < 10;i++){
            observation.addCoding(new Coding(subjects.get(i), action, 1, i+10));
        }

        observations.add(observation);

    }


    @Test
    public void testExportService(){
        prepareData();

        exportService = new ExportService(new File("/Users/xeno/Desktop"));
        exportService.exportAction(observations,subjects,action);

    }
}