package com.shortylabs.florianopolis;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shortylabs.florianopolis.model.Departures;
import com.shortylabs.florianopolis.service.RouteService;

import java.lang.ref.WeakReference;

/**
 * A placeholder fragment containing a simple view.
 */
public class SaturdayFragment extends Fragment {


    private static final String TAG = SaturdayFragment.class.getSimpleName();

    private DeparturesListAdapter mAdapter;

    private ListView mListView;

    private String mJsonResult;

    private Integer mRouteId;

    public static final String EXTRA_DEPARTURE_TIME = "extraDepartureTime";

    public SaturdayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saturday, container, false);

        Intent intent = getActivity().getIntent();

        if (intent.getExtras().containsKey(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY)) {
            mRouteId = intent.getIntExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, -1);
        }

        mListView = (ListView) rootView.findViewById(R.id.tab3_saturday);
        mListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        runService();
        return rootView;

    }


    /**
     * Instantiate the MessengerHandler, passing in the
     * Activity to be stored as a WeakReference
     */
    MessengerHandler handler = new MessengerHandler(this);

    public DeparturesListAdapter getStopsListAdapter() {
        return mAdapter;
    }

    public void setJsonResult(String json) {
        this.mJsonResult = json;
    }


    public void runService() {
        Intent intent =
                RouteService.makeIntent(getActivity(),
                        handler, mRouteId, RouteService.ROUTE_SERVICE_DEPARTURES_REQUEST);

        getActivity().startService(intent);
    }


    private void showResults() {

        Log.d(TAG, mJsonResult);
        Departures departures;
        Gson gson = new Gson();
        departures = gson.fromJson(mJsonResult, Departures.class);
        if (departures != null && departures.rows.size() > 0 && departures.saturday().size() > 0) {
            mAdapter = new DeparturesListAdapter(this,
                    departures.saturday());

            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {

            Toast.makeText(getActivity(),
                    getString(R.string.format_no_saturday_departures),
                    Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * This is the handler used for handling messages sent by a
     * Messenger.
     */
    static class MessengerHandler extends Handler {

        // A weak reference to the enclosing class
        WeakReference<SaturdayFragment> outerClass;

        /**
         * A constructor that gets a weak reference to the enclosing class.
         * We do this to avoid memory leaks during Java Garbage Collection.
         *
         * @ see https://groups.google.com/forum/#!msg/android-developers/1aPZXZG6kWk/lIYDavGYn5UJ
         */
        public MessengerHandler(SaturdayFragment outer) {
            outerClass = new WeakReference<SaturdayFragment>(outer);
        }

        // Handle any messages that get sent to this Handler
        @Override
        public void handleMessage(Message msg) {

            Log.d(TAG, "handleMessage");

            final SaturdayFragment saturdayFragment = outerClass.get();

            // If SaturdayFragment hasn't been garbage collected
            // (closed by user), proceed.
            if (saturdayFragment != null && saturdayFragment.getActivity() != null) {

                // Extract the data from Message, which is in the form
                // of a Bundle that can be passed across processes.
                Bundle data = msg.getData();
                String json;
                if (data.containsKey(RouteService.EXTRA_DEPARTURES_BY_ROUTEID_KEY)) {
                    saturdayFragment.setJsonResult(data.getString(RouteService.EXTRA_DEPARTURES_BY_ROUTEID_KEY));
                    saturdayFragment.showResults();

                }


            }
        }
    }

}
