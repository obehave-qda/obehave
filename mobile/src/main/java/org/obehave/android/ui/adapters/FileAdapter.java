package org.obehave.android.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.obehave.android.R;
import org.obehave.android.util.DateTimeHelper;

import java.io.File;


public class FileAdapter extends BaseAdapter{


    static class ViewHolder {
        public TextView txtTitle;
        public TextView txtDate;
    }

    private Context mContext;
    private LayoutInflater inflater;
    private File[] files;


    public FileAdapter(Context context, File[] files) {
        super();
        mContext = context;
        this.files = files;
    }


    @Override
    public int getCount() {
        return (files.length);
    }

    @Override
    public Object getItem(int position) {
        return files[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder; // to reference the child views for later actions

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_file, null);
            // cache view fields into the holder
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.liListHeader);
            holder.txtDate = (TextView) convertView.findViewById(R.id.liListText);

            // associate the holder with the view for later lookup
            convertView.setTag(holder);
        }
        else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) convertView.getTag();
        }

        File fileVal = (File) getItem(position);

        if(fileVal != null){
            holder.txtDate.setText(DateTimeHelper.formatToTimeStr(fileVal.lastModified()));
            holder.txtTitle.setText(fileVal.getName());
        }

        return convertView;
    }



}
