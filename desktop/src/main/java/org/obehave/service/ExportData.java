package org.obehave.service;

import com.google.common.collect.Table;

public class ExportData<D> {
    private final String type;
    private final String caption;
    private final Table<String, String, D> data;

    public ExportData(String type, String caption, Table<String, String, D> data) {
        this.type = type;
        this.caption = caption;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getCaption() {
        return caption;
    }

    public Table<String, String, D> getData() {
        return data;
    }
}
