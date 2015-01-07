package org.obehave.android.ui.events;

import android.view.View;

public class ListViewItemSelectedEvent<T> extends GuiEvent {
    private final T eventObject;
    private final String type;

    public ListViewItemSelectedEvent(View view, T eventObject, String type) {
        super(view);
        this.eventObject = eventObject;
        this.type = type;
    }

    public T getEventObject(){
        return eventObject;
    }

    public String getType(){
        return this.type;
    }
}
