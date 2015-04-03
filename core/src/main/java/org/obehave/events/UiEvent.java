package org.obehave.events;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.obehave.model.Coding;
import org.obehave.model.Observation;

/**
 * Class to summarize all events which are created in {@code core} and handled in the presentation layer
 */
public class UiEvent {
    /**
     * Created when a coding (either a state or a point coding) was created
     */
    public static class NewCoding {
        private final Coding coding;
        public NewCoding(Coding coding) {
            this.coding = coding;
        }

        public Coding getCoding() {
            return coding;
        }
    }

    /**
     * Created when a state coding is finished
     */
    public static class FinishedCoding {
        private final Coding coding;
        public FinishedCoding(Coding coding) {
            this.coding = coding;
        }

        public Coding getCoding() {
            return coding;
        }
    }

    public static class RepaintStudyTree {
    }

    /**
     * Created when an observation was successfully loaded
     */
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
