package org.obehave.service;

import org.obehave.exceptions.ServiceException;

import java.util.List;


public interface ExportDataWriter {
    void write(List<ExportData> data) throws ServiceException;
}
