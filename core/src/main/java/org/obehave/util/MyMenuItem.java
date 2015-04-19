package org.obehave.util;

public class MyMenuItem {
    private final String title;
    private final String key;

    public MyMenuItem(String title, String key) {
        this.title = title;
        this.key = key;
    }

    @Override
    public String toString() {
        return title;
    }
}
