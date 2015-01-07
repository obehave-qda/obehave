package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.obehave.android.R;
import org.obehave.android.ui.events.CodingButtonClickedEvent;
import org.obehave.events.EventBusHolder;


public class CodingFragment extends BaseFragment {

    private Button btnCoding;

    public CodingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        btnCoding = (Button)rootView.findViewById(R.id.btnCoding);

        btnCoding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCodingBtnClicked(v);
                // throw Event!
            }
        });


        return rootView;
    }

    public void onCodingBtnClicked(View v){
        Log.d("event", "Coding Btn Clicked");
        EventBusHolder.post(new CodingButtonClickedEvent(v));
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("fragment", "ExpandableListview created!");

        // only on init
        if(savedInstanceState == null){

        }
    }

}
