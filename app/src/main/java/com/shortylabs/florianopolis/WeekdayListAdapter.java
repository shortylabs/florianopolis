package com.shortylabs.florianopolis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shortylabs.florianopolis.model.Departure;

import java.util.List;

/**
 * Created by Jeri on 12/6/14.
 */
public class WeekdayListAdapter extends ArrayAdapter<Departure> {

    private static final String TAG =  WeekdayListAdapter.class.getSimpleName();
    private List<Departure> mList;
    private final WeekdayFragment mWeekdayFragment;
    /**
     * Instantiate the MessengerHandler, passing in the
     * Activity to be stored as a WeakReference
     */
//    private MessengerHandler handler;

    public WeekdayListAdapter(WeekdayFragment fragment, List<Departure> list) {
        super(fragment.getActivity(), 0, list);
        this.mWeekdayFragment = fragment;
        this.mList = list;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        final Context context = parent.getContext();
        ViewHolder viewHolder = null;

        final Departure item = mList.get(position);

        if(rowView == null) {

            // Get a new instance of the row layout view
            LayoutInflater inflater = mWeekdayFragment.getActivity().getLayoutInflater();
            rowView = inflater.inflate(R.layout.departure_list_item, null);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }


        viewHolder.departureTimeTextView.setText(item.time);


        return rowView;
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final TextView departureTimeTextView;

        public ViewHolder(View view) {
            departureTimeTextView = (TextView) view.findViewById(R.id.departure_time_textview);
        }
    }



}
