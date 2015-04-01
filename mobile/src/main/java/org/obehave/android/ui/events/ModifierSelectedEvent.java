package org.obehave.android.ui.events;


import org.obehave.model.modifier.Modifier;

public class ModifierSelectedEvent {
    private final Modifier item;

    public ModifierSelectedEvent(final Modifier item){
        this.item = item;
    }

    public Modifier getItem(){
        return item;
    }
}
