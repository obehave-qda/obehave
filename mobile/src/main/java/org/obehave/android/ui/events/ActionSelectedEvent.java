package org.obehave.android.ui.events;

import org.obehave.model.Action;

public class ActionSelectedEvent extends GuiEvent {
    private final Action action;

    public ActionSelectedEvent(Action action) {
        this.action = action;
    }

    public Action getAction(){
        return action;
    }



}
