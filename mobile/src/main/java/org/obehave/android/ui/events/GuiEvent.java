package org.obehave.android.ui.events;

import android.view.View;


public class GuiEvent {
    final private View view;

    public GuiEvent(View view){
        this.view = view;
    }

    public View getComponent(){
        return view;
    }

}
