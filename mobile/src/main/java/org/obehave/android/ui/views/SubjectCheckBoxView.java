package org.obehave.android.ui.views;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.obehave.android.R;
import org.obehave.model.Subject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubjectCheckBoxView {
    private LinearLayout linearLayout;
    private List<Subject> subjects;
    private LayoutInflater inflater;
    private List<DataHolder> data;
    private static final String LOG_TAG = SubjectCheckBoxView.class.getSimpleName();

    private static class DataHolder {

        private final TextView title;
        private final CheckBox checkBox;
        private final Subject subject;

        public DataHolder(TextView title, CheckBox checkBox, Subject subject) {
            this.title = title;
            this.checkBox = checkBox;
            this.subject = subject;
        }

        public boolean isChecked(){
            return checkBox.isChecked();
        }

        public Subject getSubject() {
            return subject;
        }

        public TextView getTitle() {
            return title;
        }

        public CheckBox getCheckBox(){
            return checkBox;
        }
    }

    public SubjectCheckBoxView(Context context, LinearLayout linearLayout, List<Subject> subjects){
        this.linearLayout = linearLayout;
        this.subjects = subjects;
        inflater = ((Activity) context).getLayoutInflater();
        data = new ArrayList<DataHolder>();
    }

    public void render(){
        for(Subject subject : subjects){
            View v = getView(subject);
            linearLayout.addView(v);
        }
    }

    private View getView(Subject subject){
        View convertView = inflater.inflate(R.layout.subject_item, null);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.lvCheckbox);
        tvTitle.setText(subject.getDisplayString());
        checkBox.setChecked(false);
        data.add(new DataHolder(tvTitle,checkBox, subject));

        return convertView;
    }

    public List<Subject> getSelectedSubjects(){
        List<Subject> returnSubjects = new ArrayList<Subject>();
        for(DataHolder item:data){
            if(item.isChecked()){
                returnSubjects.add(item.getSubject());
            }
        }

        Log.d(LOG_TAG, "" + returnSubjects.size());

        return Collections.unmodifiableList(returnSubjects);
    }
}
