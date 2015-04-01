package org.obehave.android.ui.events;

import java.io.File;

public class FileChoosenEvent {
    private final File file;

    public FileChoosenEvent(File file) {
        this.file = file;
    }

    public File getFile(){
        return this.file;
    }
}
