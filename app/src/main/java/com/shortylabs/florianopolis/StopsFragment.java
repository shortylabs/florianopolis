package com.shortylabs.florianopolis;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.shortylabs.florianopolis.model.Stop;
import com.shortylabs.florianopolis.model.Stops;
import com.shortylabs.florianopolis.service.RouteService;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 *
 * A tab in the detail view for showing the stops for a route
 */
public class StopsFragment extends Fragment {

    private static final String TAG = StopsFragment.class.getSimpleName();

    private StopsListAdapter mAdapter;

    private ListView mStopsListView;

    private String mJsonResult;

    private Integer mRouteId;

    public static final String EXTRA_STOP_NAME = "extraStopName";

    private ProgressBar mProgressBar;

    /**
     * Instantiate the MessengerHandler, passing in the
     * Activity to be stored as a WeakReference
     */
    MessengerHandler handler = new MessengerHandler(this);

    public StopsFragment() {
    }

    public StopsListAdapter getStopsListAdapter() {
        return mAdapter;
    }

    public void setJsonResult (String json) {
        this.mJsonResult = json;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stops, container, false);

        Intent intent = getActivity().getIntent();

        if (intent.getExtras().containsKey(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY)) {
            mRouteId = intent.getIntExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, -1);
        }

        mStopsListView =  (ListView)rootView.findViewById(R.id.tab1_stops);
        mStopsListView.setChoiceMode(ListView.CHOICE_MODE_NONE);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.stops_progress);
        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams)mProgressBar.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.weight = 1.0f;
        mProgressBar.setLayoutParams(layoutParams);
        mProgressBar.setVisibility(View.VISIBLE);

        runService();
        return rootView;

    }

    public void runService() {
        Intent intent =
                RouteService.makeIntent(getActivity(),
                        handler, mRouteId, RouteService.ROUTE_SERVICE_STOPS_REQUEST);

        getActivity().startService(intent);
    }


    private void showResults() {

        Log.d(TAG, mJsonResult);
        mProgressBar.setVisibility(View.GONE);
        Stops stops;
        Gson gson = new Gson();
        stops = gson.fromJson(mJsonResult, Stops.class);
        List<Stop> stopsList = null;
        if (stops != null) {
            stopsList = stops.rows;
        }

        mAdapter = new StopsListAdapter(this,
                stopsList);

        mStopsListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    /**
     * This is the handler used for handling messages sent by a
     * Messenger.
     */
    static class MessengerHandler extends Handler {

        // A weak reference to the enclosing class
        WeakReference<StopsFragment> outerClass;

        /**
         * A constructor that gets a weak reference to the enclosing class.
         * We do this to avoid memory leaks during Java Garbage Collection.
         *
         * @ see https://groups.google.com/forum/#!msg/android-developers/1aPZXZG6kWk/lIYDavGYn5UJ
         */
        public MessengerHandler(StopsFragment outer) {
            outerClass = new WeakReference<StopsFragment>(outer);
        }

        // Handle any messages that get sent to this Handler
        @Override
        public void handleMessage(Message msg) {

            Log.d(TAG, "handleMessage");

            final StopsFragment stopsFragment = outerClass.get();

            // If StopsFragment hasn't been garbage collected
            // (closed by user), proceed.
            if (stopsFragment != null && stopsFragment.getActivity() != null ) {

                // Extract the data from Message, which is in the form
                // of a Bundle that can be passed across processes.
                Bundle data = msg.getData();
                String json;
                if (data.containsKey(RouteService.EXTRA_STOPS_BY_ROUTEID_KEY)) {
                    stopsFragment.setJsonResult(data.getString(RouteService.EXTRA_STOPS_BY_ROUTEID_KEY));
                    stopsFragment.showResults();

                }

//                // is this necessary when using a foreground service?
//                stopsFragment.getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
////                        notifyDataSetChanged();
//                        RouteListAdapter adapter = stopsFragment.getRouteListAdapter();
//                        if (adapter != null) {
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                });


            }
        }
    }

}
