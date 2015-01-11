package org.obehave.model;

/**
 * By implementing this interface, a class can be shown in the UI as a string
 */
public interface Displayable {
    /**
     * Get the string to display in the UI
     * @return the displaystring
     */
    String getDisplayString();
}
