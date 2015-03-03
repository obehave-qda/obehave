package org.obehave;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.obehave.android.R;
import org.obehave.android.ui.activities.MainActivity;


public class StartActivity extends Activity {
    private static final String ARG_FILENAME = "filename";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ChooseStudyFragment())
                    .commit();
        }
    }


    /**
     * Choose Study Fragment. Starts MainActivity if file is chosen.
     */
    public static class ChooseStudyFragment extends Fragment {
        private final String LOG_TAG = this.getClass().getSimpleName();
        private static final int PICK_CONTENT = 1;
        private Button btnChooseFile;
        private Button btnImport;

        public ChooseStudyFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_start, container, false);
            btnChooseFile = (Button) rootView.findViewById(R.id.btnChooseStudyFile);
            btnImport = (Button) rootView.findViewById(R.id.btnChooseStudyWLAN);

            btnChooseFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    fileIntent.setType("*/*");
                    startActivityForResult(fileIntent, PICK_CONTENT);
                }
            });

            btnImport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    throw new UnsupportedOperationException();
                }
            });

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK) {
                Log.d(LOG_TAG, "RequestCode: " + requestCode);
                Log.d(LOG_TAG, "ResultCode: " + resultCode);
                Uri file = data.getData();
                Log.d(LOG_TAG, "FilePath: " + file.getPath());
            }
        }

        private void startMainActivity(String filename){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(ARG_FILENAME,filename);
            startActivity(intent);
        }
    }
}
