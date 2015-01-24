package org.obehave.android.ui.adapters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.obehave.android.R;

public class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ListView lvSimple = (ListView) rootView.findViewById(R.id.lvRunning);

            String[] values = new String[] { "Amarok - spielt - Peter", "Amarok - markiert",  "Lessie - frisst"};


            ArrayAdapter adapter = new ArrayAdapter(this.getActivity(),
                    R.layout.list_item_running,R.id.liListHeader, values);
            lvSimple.setAdapter(adapter);
            return rootView;
        }
    }