package org.obehave.service;

import org.obehave.exceptions.ServiceException;
import org.obehave.model.Node;
import org.obehave.model.Observation;
import org.obehave.model.Subject;

import java.util.List;

/**
 * @author Markus Möslinger
 */
public interface Exporter {
    void export(List<Observation> observations, List<Subject> subjects, List<Node> actionNode) throws ServiceException;
}
