package org.obehave.android.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.obehave.android.R;
import org.obehave.android.database.AbstractDbAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyCheckboxCursorAdapter<T> extends MyCursorAdapter {
    private LayoutInflater layoutInflater;
    private List<Long> selectedItemIds;
    private int textViewDefaultColor;

    public List<Long> getSelectedItemIds() {
        return selectedItemIds;
    }

    private class ViewHolder<T> {
        private TextView txtView;
        private CheckBox checkbox;
        private RelativeLayout clickableArea;
        private Long id;

        public TextView getTxtView() {
            return txtView;
        }

        public void setTxtView(TextView txtView) {
            this.txtView = txtView;
        }

        public CheckBox getCheckbox() {
            return checkbox;
        }

        public void setCheckbox(CheckBox checkbox) {
            this.checkbox = checkbox;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public RelativeLayout getClickableArea() {
            return clickableArea;
        }

        public void setClickableArea(RelativeLayout clickableArea) {
            this.clickableArea = clickableArea;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) { return false; }
            if (obj == this) { return true; }
            if (obj.getClass() != getClass()) {
                return false;
            }
            return EqualsBuilder.reflectionEquals(this, obj);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }
    }

    public MyCheckboxCursorAdapter(Context context, AbstractDbAdapter dbAdapter) {
        super(context, dbAdapter);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        selectedItemIds = new ArrayList<Long>();
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v =  layoutInflater.inflate(R.layout.listview_item_checkbox, parent, false);
        ViewHolder holder = new ViewHolder();

        holder.setTxtView((TextView) v.findViewById(R.id.lvTitle));
        holder.setCheckbox((CheckBox) v.findViewById(R.id.lvCheckbox));
        holder.setClickableArea((RelativeLayout) v.findViewById(R.id.lvClickArea));
        holder.getCheckbox().setChecked(false);
        holder.setId(getId(cursor));
        v.setTag(holder);
        textViewDefaultColor = holder.getTxtView().getTextColors().getDefaultColor();
        return v;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.getTxtView().setText(getColumnValue(cursor));


        holder.getClickableArea().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getCheckbox().isChecked()) {
                    selectedItemIds.remove(holder.getId());
                    holder.getCheckbox().setChecked(false);
                    holder.getClickableArea().setBackgroundColor(android.R.drawable.screen_background_dark);
                    //holder.getTxtView().setTextColor(textViewDefaultColor);
                } else {
                    selectedItemIds.add(holder.getId());
                    holder.getCheckbox().setChecked(true);
                    //holder.getTxtView().setTextColor(context.getResources().getColor(R.color.highlightTextColor));
                    holder.getClickableArea().setBackgroundColor(context.getResources().getColor(R.color.highlight));
                }
                Log.d("count", "" + selectedItemIds.size());
            }
        });
    }

    public Collection<T> getSelectedItems(){
        return null;
    }
}
