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
     * This is the handler used for handling messages sent by a
     * Messenger.
     */
//    static class MessengerHandler extends Handler {
//
//        // A weak reference to the enclosing class
//        WeakReference<StopsFragment> outerClass;
//
//        /**
//         * A constructor that gets a weak reference to the enclosing class.
//         * We do this to avoid memory leaks during Java Garbage Collection.
//         *
//         * @ see https://groups.google.com/forum/#!msg/android-developers/1aPZXZG6kWk/lIYDavGYn5UJ
//         */
//        public MessengerHandler(StopsFragment outer) {
//            outerClass = new WeakReference<StopsFragment>(outer);
//        }
//
//        // Handle any messages that get sent to this Handler
//        @Override
//        public void handleMessage(Message msg) {
//
//            Log.d(TAG, "handleMessage");
//
//            final StopsFragment stopsFragment = outerClass.get();
//
//            // If StopsFragment hasn't been garbage collected
//            // (closed by user), proceed.
//            if (stopsFragment != null && stopsFragment.getActivity() != null ) {
//
//                // Extract the data from Message, which is in the form
//                // of a Bundle that can be passed across processes.
//                Bundle data = msg.getData();
//
//                // is this necessary when using a foreground service?
//                stopsFragment.getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
////                        notifyDataSetChanged();
//                        StopsListAdapter adapter = stopsFragment.getStopsListAdapter();
//                        if (adapter != null) {
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//
//
//            }
//        }
//    }

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
