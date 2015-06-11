package org.obehave.android.ui.events;

import java.util.List;

public class EnumerationModifierSelectedEvent extends GuiEvent{
    List<String> values;

    public EnumerationModifierSelectedEvent(List<String> values) {
        this.values = values;
    }

    public List<String> getValues(){
        return values;
    }
}
