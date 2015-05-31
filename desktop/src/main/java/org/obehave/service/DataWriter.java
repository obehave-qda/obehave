package org.obehave.service;

import java.util.List;

/**
 * @author Markus Möslinger
 */
public interface DataWriter {
    void write(List<ExportData> data);
}
