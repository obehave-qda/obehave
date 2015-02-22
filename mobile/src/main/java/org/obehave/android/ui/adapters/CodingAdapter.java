package org.obehave.android.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.obehave.android.R;
import org.obehave.model.Action;

import java.util.List;

public class CodingAdapter extends ArrayAdapter {

    static class ViewHolder{
        public TextView txtView;
    }

    private Context mContext;

    public CodingAdapter(Context context, List<Action> actions) {
        super(context, 0, actions);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder; // to reference the child views for later actions

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_action, null);
            // cache view fields into the holder
            holder = new ViewHolder();
            holder.txtView = (TextView) convertView.findViewById(R.id.liListHeader);
            convertView.setTag(holder);
        }
        else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) convertView.getTag();
        }

        Action action = (Action) getItem(position);
        if(action != null){

            holder.txtView.setText(action.getDisplayString());
        }
        return convertView;
    }
}
