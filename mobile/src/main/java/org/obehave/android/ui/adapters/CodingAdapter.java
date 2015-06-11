package org.obehave.android.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.joda.time.DateTime;
import org.obehave.android.R;
import org.obehave.android.ui.views.Circle;
import org.obehave.android.util.DateTimeHelper;
import org.obehave.model.Action;
import org.obehave.model.Coding;
import org.obehave.model.Subject;
import org.obehave.model.modifier.Modifier;

import java.util.ArrayList;
import java.util.List;


public class CodingAdapter extends BaseAdapter{

    private static final String LOG_TAG = BaseAdapter.class.getSimpleName();

    static class ViewHolder {
        public Circle circle;
        public TextView liHeader;
        public TextView liText;
    }


    private Context mContext;
    private LayoutInflater inflater;
    private List<Coding> codings;

    private static final int TYPE_RUNNING_CODING = 0;
    private static final int TYPE_POINT_CODING = 1;
    private static final int TYPE_STATE_FINISHED_CODING = 2;
    private static final int TYPE_MAX_COUNT = 3;

    @Override
    public int getItemViewType(int position) {
        if(codings.get(position).isStateCoding() && codings.get(position).isOpen()) {
            return TYPE_RUNNING_CODING;
        }
        else if(codings.get(position).isStateCoding() && !codings.get(position).isOpen()){
            return TYPE_STATE_FINISHED_CODING;
        }
        else{
            return TYPE_POINT_CODING;
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    public CodingAdapter(Context context, List<Coding> codings) {
        super();
        mContext = context;
        setCodings(codings);
    }

    public void setCodings(List<Coding> codings) {
        this.codings = new ArrayList<Coding>();
        this.codings.addAll(codings);
        notifyDataSetChanged();
    }

    public ArrayList<Coding> getCodings(){
        // we must return an array List because the interface List<Codings> is not serializeable
        return (ArrayList<Coding>)codings;
    }

    @Override
    public int getCount() {
        return codings.size();
    }

    @Override
    public Object getItem(int position) {
        return codings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        Object item = getItem(position);

        convertView = getView(convertView, parent, type);
        fillViewHolder((ViewHolder)convertView.getTag(), (Coding) item, type);

        return convertView;
    }


    private View getView(View convertView, ViewGroup parent, int type){
        inflater = ((Activity)mContext).getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_coding, null);
            // cache view fields into the holder
            ViewHolder holder = new ViewHolder();
            holder.liHeader = (TextView) convertView.findViewById(R.id.liListHeader);
            holder.liText = (TextView) convertView.findViewById(R.id.liListText);
            holder.circle = (Circle) convertView.findViewById(R.id.circle);
            // associate the holder with the view for later lookup
            convertView.setTag(holder);
        }

        return convertView;
    }

    private void fillViewHolder(ViewHolder holder, Coding item, int type){
        if(item != null){

            Subject subject = item.getSubject();
            Modifier modifier = item.getModifier();
            Action action = item.getAction();
            String modifierStr = "";
            if(modifier != null) {
                modifierStr = " - " + modifier.getBuildString();
            }

            holder.circle.setCircleColor(subject.getColor());
            holder.liHeader.setText(subject.getDisplayString() + " - " + action.getDisplayString() + modifierStr);
            holder.liText.setText(getTimeString(item, type));
        }
    }

    private String getTimeString(Coding item, int type){
        if(type == TYPE_RUNNING_CODING){
            DateTime startTime = item.getObservation().getDateTime().plus(item.getStartMs());
            DateTime endTime = DateTime.now();
            return DateTimeHelper.formatToTimer(startTime, endTime);
        }
        else if(type == TYPE_STATE_FINISHED_CODING){
            DateTime startTime = item.getObservation().getDateTime().plus(item.getStartMs());
            DateTime endTime = item.getObservation().getDateTime().plus(item.getStartMs()).plus(item.getDuration());
            return DateTimeHelper.formatToTimer(startTime, endTime);
        }
        else {
            return DateTimeHelper.formatToTimer(item.getObservation().getDateTime(), item.getObservation().getDateTime().plus(item.getStartMs()));
        }
    }
}
