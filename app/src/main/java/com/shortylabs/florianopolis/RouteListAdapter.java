package com.shortylabs.florianopolis;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shortylabs.florianopolis.model.Row;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Jeri on 12/6/14.
 */
public class RouteListAdapter extends ArrayAdapter<Row> {

    private static final String TAG =  RouteListAdapter.class.getSimpleName();
    private List<Row> mList;
    private final RouteListFragment mRouteListFragment;
    /**
     * Instantiate the MessengerHandler, passing in the
     * Activity to be stored as a WeakReference
     */
    private MessengerHandler handler;

    public RouteListAdapter(RouteListFragment fragment, List<Row> list) {
        super(fragment.getActivity(), 0, list);
        this.mRouteListFragment = fragment;
        this.mList = list;
        this.handler =  new MessengerHandler(fragment);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder = null;

        final Context context = parent.getContext();

        /** Set data to your Views. */
        final Row item = mList.get(position);

        if(rowView == null) {

            // Get a new instance of the row layout view
            LayoutInflater inflater = mRouteListFragment.getActivity().getLayoutInflater();
            rowView = inflater.inflate(R.layout.route_list_item, null);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }


        viewHolder.routeNameTextView.setText(item.longName);
        viewHolder.routeNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "detail goes here", Toast.LENGTH_SHORT).show();
            }
        });


        return rowView;
    }

    /**
     * This is the handler used for handling messages sent by a
     * Messenger.
     */
    static class MessengerHandler extends Handler {

        // A weak reference to the enclosing class
        WeakReference<RouteListFragment> outerClass;

        /**
         * A constructor that gets a weak reference to the enclosing class.
         * We do this to avoid memory leaks during Java Garbage Collection.
         *
         * @ see https://groups.google.com/forum/#!msg/android-developers/1aPZXZG6kWk/lIYDavGYn5UJ
         */
        public MessengerHandler(RouteListFragment outer) {
            outerClass = new WeakReference<RouteListFragment>(outer);
        }

        // Handle any messages that get sent to this Handler
        @Override
        public void handleMessage(Message msg) {

            Log.d(TAG, "handleMessage");

            final RouteListFragment RouteListFragment = outerClass.get();

            // If RouteListFragment hasn't been garbage collected
            // (closed by user), proceed.
            if (RouteListFragment != null && RouteListFragment.getActivity() != null ) {

                // Extract the data from Message, which is in the form
                // of a Bundle that can be passed across processes.
                Bundle data = msg.getData();

                // is this necessary when using a foreground service?
                RouteListFragment.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
//                        notifyDataSetChanged();
                        RouteListAdapter adapter = RouteListFragment.getRouteListAdapter();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });


            }
        }
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final TextView routeNameTextView;

        public ViewHolder(View view) {
            routeNameTextView = (TextView) view.findViewById(R.id.route_name_textview);
        }
    }

}
