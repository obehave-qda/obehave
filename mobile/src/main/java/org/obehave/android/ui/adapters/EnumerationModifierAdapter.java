package org.obehave.android.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.obehave.android.R;

import java.util.List;


public class EnumerationModifierAdapter extends ArrayAdapter{

    static class ViewHolder{
        public TextView txtView;
    }

    private Context mContext;

    public EnumerationModifierAdapter(Context context, List<String> values) {
        super(context, 0, values);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder; // to reference the child views for later actions

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_enumeration_modifier, null);
            // cache view fields into the holder
            holder = new ViewHolder();
            holder.txtView = (TextView) convertView.findViewById(R.id.liListHeader);
            // associate the holder with the view for later lookup
            convertView.setTag(holder);
        }
        else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) convertView.getTag();
        }

        String value = (String) getItem(position);
        if(value != null){
            holder.txtView.setText(value);
        }
        return convertView;
    }
}
