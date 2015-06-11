package org.obehave.android.ui.fragments.events;

public class SortingChangedEvent {

    private int sortType;

    public SortingChangedEvent(int sortType){
        this.sortType = sortType;
    }

    public int getSortType(){
        return this.sortType;
    }
}
