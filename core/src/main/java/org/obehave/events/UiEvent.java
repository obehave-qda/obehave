package org.obehave.events;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.obehave.model.Coding;
import org.obehave.model.Observation;

/**
 * @author Markus M�slinger
 */
public class UiEvent {
    public static class NewCoding {
        private final Coding coding;
        public NewCoding(Coding coding) {
            this.coding = coding;
        }

        public Coding getCoding() {
            return coding;
        }
    }

    public static class FinishedCoding {
        private final Coding coding;
        public FinishedCoding(Coding coding) {
            this.coding = coding;
        }
    }

    public static class RepaintStudyTree {
    }

    public static class LoadObservation {
        private final Observation observation;

        public LoadObservation(Observation observation) {
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
}
