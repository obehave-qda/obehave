package org.obehave.service;

import org.obehave.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * @author Markus Möslinger
 */
public class ExportService {
    private static final Logger log = LoggerFactory.getLogger(ExportService.class);
    private File savePath;

    public ExportService(File savePath) {
        this.savePath = savePath;
    }

    public void exportActionGroup(List<Observation> observations, List<Subject> subjects, Node actionGroup) {

    }

    public void exportAction(List<Observation> observations, List<Subject> subjects, Action action) {
        // matrix erstellen mit subjects.size() länge

        for (Observation o : observations) {
            for (Coding c : o.getCodings()) {
                if (isInterestingCoding(c, subjects) && c.getAction().equals(action)) {

                }
            }
        }
    }

    private boolean isInterestingCoding(Coding c, List<Subject> subjects) {
        return subjects.contains(c.getSubject()) || subjects.contains(c.getModifier().get());
    }
}
