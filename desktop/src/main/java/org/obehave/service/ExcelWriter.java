package org.obehave.service;

import java.io.File;
import java.util.List;

/**
 * @author Markus Möslinger
 */
public class ExcelWriter implements DataWriter {
    private File path;

    public ExcelWriter(File path) {
        this.path = path;
    }

    @Override
    public void write(List<ExportData> data) {

    }
}
