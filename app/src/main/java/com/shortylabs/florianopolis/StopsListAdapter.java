package com.shortylabs.florianopolis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shortylabs.florianopolis.model.Stop;

import java.util.List;

/**
 * Created by Jeri on 12/6/14.
 */
public class StopsListAdapter extends ArrayAdapter<Stop> {

    private static final String TAG =  StopsListAdapter.class.getSimpleName();
    private List<Stop> mList;
    private final StopsFragment mStopsFragment;
    /**
     * Instantiate the MessengerHandler, passing in the
     * Activity to be stored as a WeakReference
     */
//    private MessengerHandler handler;

    public StopsListAdapter(StopsFragment fragment, List<Stop> list) {
        super(fragment.getActivity(), 0, list);
        this.mStopsFragment = fragment;
        this.mList = list;
//        this.handler =  new MessengerHandler(fragment);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        final Context context = parent.getContext();
        ViewHolder viewHolder = null;

        /** Set data to your Views. */
        final Stop item = mList.get(position);

        if(rowView == null) {

            // Get a new instance of the row layout view
            LayoutInflater inflater = mStopsFragment.getActivity().getLayoutInflater();
            rowView = inflater.inflate(R.layout.stop_list_item, null);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }


        viewHolder.stopNameTextView.setText(item.name);


        return rowView;
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final TextView stopNameTextView;

        public ViewHolder(View view) {
            stopNameTextView = (TextView) view.findViewById(R.id.stop_name_textview);
        }
    }



}
