package org.obehave.android.ui.events;


public class ModifierSelectedEvent {
    private final Object item;
    private final ModifierType type;



    public ModifierSelectedEvent(final Object item, final ModifierType type){
        this.item = item;
        this.type = type;
    }

    public Object getItem(){
        return item;
    }

    public ModifierType getType(){
        return type;
    }
}
