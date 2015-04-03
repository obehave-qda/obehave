package org.obehave.android.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.obehave.android.R;
import org.obehave.android.ui.views.Circle;
import org.obehave.model.Subject;

import java.util.List;


public class SubjectModifierAdapter extends ArrayAdapter{

    static class ViewHolder{
        public Circle circle;
        public TextView txtView;
    }

    private Context mContext;

    public SubjectModifierAdapter(Context context, List<Subject> subjects) {
        super(context, 0, subjects);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder; // to reference the child views for later actions

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_subject, null);
            // cache view fields into the holder
            holder = new ViewHolder();
            holder.txtView = (TextView) convertView.findViewById(R.id.liListHeader);
            holder.circle = (Circle) convertView.findViewById(R.id.circle);
            // associate the holder with the view for later lookup
            convertView.setTag(holder);
        }
        else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) convertView.getTag();
        }

        Subject subject = (Subject) getItem(position);
        if(subject != null){
            int red = 0;
            int blue = 0;
            int green = 0;
            if(subject.getColor() != null) {
                red = subject.getColor().getRed();
                blue = subject.getColor().getBlue();
                green = subject.getColor().getGreen();
            }

            holder.txtView.setText(subject.getDisplayString());
            holder.circle.setCircleColor(Color.rgb(red, green, blue));
        }
        return convertView;
    }
}
