package org.obehave.events;

import org.obehave.model.Coding;

/**
 * @author Markus Möslinger
 */
public class UiEvent {
    public static class NewCoding {
        private final Coding coding;
        public NewCoding(Coding coding) {
            this.coding = coding;
        }
    }

    public static class FinishedCoding {
        private final Coding coding;
        public FinishedCoding(Coding coding) {
            this.coding = coding;
        }
    }
}
