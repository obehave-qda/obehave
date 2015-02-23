package org.obehave.service;

import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.persistence.Daos;
import org.obehave.util.DatabaseProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class StudyLoader {
    private static final Logger log = LoggerFactory.getLogger(StudyLoader.class);

    public static void load(Study study) throws SQLException {
        log.info("Starting loading of entities");
        long start = System.currentTimeMillis();

        int subjects = 0;
        for (Subject s : Daos.get().subject().queryForAll()) {
            log.trace("Loaded subject: {}", s);
            study.addSubject(s);
            subjects++;
        }

        int actions = 0;
        for (Action a : Daos.get().action().queryForAll()) {
            log.trace("Loaded action: {}", a);
            study.addAction(a);
            actions++;
        }

        int modifierFactories = 0;
        for (ModifierFactory m : Daos.get().modifierFactory().queryForAll()) {
            log.trace("Loaded modifierFactory: {}", m);
            study.addModifierFactory(m);
            modifierFactories++;
        }

        int observations = 0;
        for (Observation o : Daos.get().observation().queryForAll()) {
            log.trace("Loaded observation: {}", o);
            study.addObservation(o);
            observations++;
        }

        final String studyName = DatabaseProperties.get(DatabaseProperties.STUDY_NAME);
        study.setName(studyName);

        long duration = System.currentTimeMillis() - start;
        log.info("Took {}ms for loading of {} subjects, {} actions, {} modifierFactories and {} observations",
                duration, subjects, actions, modifierFactories, observations);
    }
}
