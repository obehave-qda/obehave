package org.obehave.events;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.obehave.model.Observation;

/**
 * @author Markus Möslinger
 */
public class LoadObservationEvent {
    private final Observation observation;

    public LoadObservationEvent(Observation observation) {
        this.observation = observation;
    }

    public Observation getObservation() {
        return observation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("observation", observation).toString();
    }
}
