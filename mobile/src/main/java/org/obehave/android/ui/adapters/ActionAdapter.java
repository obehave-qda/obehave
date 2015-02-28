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
import org.obehave.model.Action;
import org.obehave.model.Displayable;
import org.obehave.model.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ActionAdapter extends BaseAdapter{


    static class ViewHolderAction {
        public TextView txtView;
    }

    static class ViewHolderNode {
        public TextView txtView;
    }

    private Context mContext;
    private LayoutInflater inflater;
    private List<Action> actions;
    private List<Action> defaultOrder;
    private List<Node> nodes;

    private static final int TYPE_NODE = 0;
    private static final int TYPE_ACTIONS = 1;
    private static final int TYPE_MAX_COUNT = 2;

    @Override
    public int getItemViewType(int position) {
        if(position < nodes.size()){
            return TYPE_NODE;
        }
        else {
            return TYPE_ACTIONS;
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    public ActionAdapter(Context context, List<Action> actions, List<Node> nodes) {
        super();
        mContext = context;
        setActions(actions);
        setNodes(nodes);
    }

    private void setActions(List<Action> actions) {
        this.actions =  new ArrayList<Action>();
        this.actions.addAll(actions);
        defaultOrder = new ArrayList<Action>();
        defaultOrder.addAll(actions);
    }

    private void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public int getCount() {
        return (actions.size() + nodes.size());
    }

    @Override
    public Object getItem(int position) {

        if(position < nodes.size()){
            return nodes.get(position);
        }
        else {
            return actions.get(position -(nodes.size()));
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
        else if(type == TYPE_ACTIONS){
            convertView = getView((Action) item, convertView, parent);
            fillViewHolder((ViewHolderAction)convertView.getTag(), (Action) item);
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

    private View getView(Action item, View convertView, ViewGroup parent){
        inflater = ((Activity)mContext).getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_action, null);
            // cache view fields into the holder
            ViewHolderAction holder = new ViewHolderAction();
            holder.txtView = (TextView) convertView.findViewById(R.id.liListHeader);
            // associate the holder with the view for later lookup
            convertView.setTag(holder);
        }

        return convertView;
    }

    private void fillViewHolder(ViewHolderAction holder, Action subject){
            holder.txtView.setText(subject.getDisplayString());
    }

    public void sortByName(Comparator<Displayable> comparator){
        Collections.sort(this.actions, comparator);
        this.notifyDataSetChanged();
    }

    public void sortDefault(){
        if(defaultOrder != null && !defaultOrder.isEmpty()){
           this.actions.clear();
           this.actions.addAll(defaultOrder);
        }

        this.notifyDataSetChanged();
    }



}
