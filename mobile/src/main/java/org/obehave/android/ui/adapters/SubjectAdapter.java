package org.obehave.android.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.obehave.android.R;
import org.obehave.android.ui.views.Circle;
import org.obehave.model.Displayable;
import org.obehave.model.Node;
import org.obehave.model.Subject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SubjectAdapter extends BaseAdapter{


    static class ViewHolderSubject {
        public Circle circle;
        public TextView txtView;
    }

    static class ViewHolderNode {
        public TextView txtView;
    }

    private Context mContext;
    private LayoutInflater inflater;
    private List<Subject> subjects;
    private List<Subject> defaultOrder;
    private List<Node> nodes;

    private static final int TYPE_NODE = 0;
    private static final int TYPE_SUBJECT = 1;
    private static final int TYPE_MAX_COUNT = 2;

    @Override
    public int getItemViewType(int position) {
        if(position < nodes.size()){
            return TYPE_NODE;
        }
        else {
            return TYPE_SUBJECT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    public SubjectAdapter(Context context, List<Subject> subjects, List<Node> nodes) {
        super();
        mContext = context;
        setSubjects(subjects);
        setNodes(nodes);
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects =  new ArrayList<Subject>();
        this.subjects.addAll(subjects);
        defaultOrder = new ArrayList<Subject>();
        defaultOrder.addAll(subjects);
        notifyDataSetChanged();
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (subjects.size() + nodes.size());
    }

    @Override
    public Object getItem(int position) {

        if(position < nodes.size()){
            return nodes.get(position);
        }
        else {
            return subjects.get(position -(nodes.size()));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("Adapter", "COUNT: " + getCount());
        Log.d("Adapter", "POSITION: " + position);
        int type = getItemViewType(position);
        Object item = getItem(position);
        if(type == TYPE_NODE){
            convertView = getView((Node) item, convertView, parent);
            fillViewHolder((ViewHolderNode)convertView.getTag(), (Node) item);
        }
        else if(type == TYPE_SUBJECT){
            convertView = getView((Subject) item, convertView, parent);
            fillViewHolder((ViewHolderSubject)convertView.getTag(), (Subject) item);
        }

        return convertView;
    }


    private View getView(Node item, View convertView, ViewGroup parent){
        inflater = ((Activity)mContext).getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_node, null);
            // cache view fields into the holder
            ViewHolderNode holder = new ViewHolderNode();
            holder.txtView = (TextView) convertView.findViewById(R.id.liListHeader);
            // associate the holder with the view for later lookup
            convertView.setTag(holder);
        }

        return convertView;
    }

    private void fillViewHolder(ViewHolderNode holder, Node node){
        if(node != null){
            holder.txtView.setText(node.getDisplayString());
        }
    }

    private View getView(Subject item, View convertView, ViewGroup parent){
        inflater = ((Activity)mContext).getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_subject, null);
            // cache view fields into the holder
            ViewHolderSubject holder = new ViewHolderSubject();
            holder.txtView = (TextView) convertView.findViewById(R.id.liListHeader);
            holder.circle = (Circle) convertView.findViewById(R.id.circle);
            // associate the holder with the view for later lookup
            convertView.setTag(holder);
        }

        return convertView;
    }

    private void fillViewHolder(ViewHolderSubject holder, Subject subject){
        if(subject != null){
            holder.txtView.setText(subject.getDisplayString());
            holder.circle.setCircleColor(subject.getColor());
        }
    }

    public void sortByName(Comparator<Displayable> comparator){
        Collections.sort(this.subjects, comparator);
        this.notifyDataSetChanged();
    }

    public void sortDefault(){
        if(defaultOrder != null && !defaultOrder.isEmpty()){
           this.subjects.clear();
           this.subjects.addAll(defaultOrder);
        }

        this.notifyDataSetChanged();
    }
}
