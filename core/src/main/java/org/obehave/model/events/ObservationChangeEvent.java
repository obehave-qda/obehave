package org.obehave.model.events;

import org.obehave.events.ChangeEvent;
import org.obehave.events.ChangeType;
import org.obehave.model.Observation;
import org.obehave.model.Subject;

/**
 * Created by Markus on 07.12.2014.
 */
public class ObservationChangeEvent extends ChangeEvent<Observation> {
    public ObservationChangeEvent(Observation changedObservation, ChangeType changeType) {
        super(changedObservation, changeType);
    }
}
