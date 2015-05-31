package org.obehave.service;

import java.util.List;

/**
 * @author Markus Möslinger
 */
public interface ExportDataWriter {
    void write(List<ExportData> data);
}
