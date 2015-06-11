package org.obehave.android.ui.events;

public class DecimalRangeModifierSelectedEvent extends GuiEvent{
    private String value;

    public DecimalRangeModifierSelectedEvent(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
