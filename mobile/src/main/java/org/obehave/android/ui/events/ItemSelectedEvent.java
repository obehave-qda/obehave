package org.obehave.android.ui.events;


import org.obehave.model.BaseEntity;

public class ItemSelectedEvent {
    private final BaseEntity item;

    public ItemSelectedEvent(final BaseEntity item){
        this.item = item;
    }

    public BaseEntity getItem(){
        return item;
    }
}
