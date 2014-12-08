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
import android.widget.Toast;

import com.google.gson.Gson;
import com.shortylabs.florianopolis.model.Departures;
import com.shortylabs.florianopolis.service.RouteService;

import java.lang.ref.WeakReference;

/**
 * A placeholder fragment containing a simple view.
 */
public class WeekdayFragment extends Fragment {


    private static final String TAG = WeekdayFragment.class.getSimpleName();

    private DeparturesListAdapter mAdapter;

    private ListView mWeekdayListView;

    private String mJsonResult;

    private Integer mRouteId;

    public static final String EXTRA_DEPARTURE_TIME = "extraDepartureTime";

    private ProgressBar mProgressBar;

    /**
     * Instantiate the MessengerHandler, passing in the
     * Activity to be stored as a WeakReference
     */
    MessengerHandler handler = new MessengerHandler(this);

    public WeekdayFragment() {
    }


    public DeparturesListAdapter getStopsListAdapter() {
        return mAdapter;
    }

    public void setJsonResult(String json) {
        this.mJsonResult = json;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weekday, container, false);

        Intent intent = getActivity().getIntent();

        if (intent.getExtras().containsKey(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY)) {
            mRouteId = intent.getIntExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, -1);
        }

        mWeekdayListView = (ListView) rootView.findViewById(R.id.tab2_weekday);
        mWeekdayListView.setChoiceMode(ListView.CHOICE_MODE_NONE);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.weekday_progress);
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
                        handler, mRouteId, RouteService.ROUTE_SERVICE_DEPARTURES_REQUEST);

        getActivity().startService(intent);
    }


    private void showResults() {

        Log.d(TAG, mJsonResult);

        mProgressBar.setVisibility(View.GONE);
        Departures departures;
        Gson gson = new Gson();
        departures = gson.fromJson(mJsonResult, Departures.class);
        if (departures != null && departures.rows.size() > 0 && departures.weekday().size() > 0) {
            mAdapter = new DeparturesListAdapter(this,
                    departures.weekday());

            mWeekdayListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {

            Toast.makeText(getActivity(),
                    getString(R.string.format_no_weekday_departures),
                    Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * This is the handler used for handling messages sent by a
     * Messenger.
     */
    static class MessengerHandler extends Handler {

        // A weak reference to the enclosing class
        WeakReference<WeekdayFragment> outerClass;

        /**
         * A constructor that gets a weak reference to the enclosing class.
         * We do this to avoid memory leaks during Java Garbage Collection.
         *
         * @ see https://groups.google.com/forum/#!msg/android-developers/1aPZXZG6kWk/lIYDavGYn5UJ
         */
        public MessengerHandler(WeekdayFragment outer) {
            outerClass = new WeakReference<WeekdayFragment>(outer);
        }

        // Handle any messages that get sent to this Handler
        @Override
        public void handleMessage(Message msg) {

            Log.d(TAG, "handleMessage");

            final WeekdayFragment weekdayFragment = outerClass.get();

            // If WeekdayFragment hasn't been garbage collected
            // (closed by user), proceed.
            if (weekdayFragment != null && weekdayFragment.getActivity() != null) {

                // Extract the data from Message, which is in the form
                // of a Bundle that can be passed across processes.
                Bundle data = msg.getData();
                String json;
                if (data.containsKey(RouteService.EXTRA_DEPARTURES_BY_ROUTEID_KEY)) {
                    weekdayFragment.setJsonResult(data.getString(RouteService.EXTRA_DEPARTURES_BY_ROUTEID_KEY));
                    weekdayFragment.showResults();

                }


            }
        }
    }
}



