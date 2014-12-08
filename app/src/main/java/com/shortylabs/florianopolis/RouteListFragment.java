package com.shortylabs.florianopolis;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shortylabs.florianopolis.model.Routes;
import com.shortylabs.florianopolis.service.RouteService;

import java.lang.ref.WeakReference;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteListFragment extends Fragment {

    private EditText mStreetNameEdit;
    private Button mSearchButton;
    private ListView mRouteListView;
    private static final String ROUTES_RESULT = "routes";
    private static final String STREET_NAME = "streetName";
    private String mJsonResult;
    private String mStreetName;
    private ProgressBar mProgressBar;


    private static final String TAG = RouteListFragment.class.getSimpleName();

    private RouteListAdapter mAdapter;

    /**
     * Instantiate the MessengerHandler, passing in the
     * Activity to be stored as a WeakReference
     */
    MessengerHandler handler = new MessengerHandler(this);


    public RouteListFragment() {
    }

    public void setJsonResult (String json) {
        this.mJsonResult = json;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_route_list, container, false);


        mStreetNameEdit = (EditText) rootView.findViewById(R.id.street_name_edit);
        mStreetNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchButton.setEnabled(!mStreetNameEdit.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchButton = (Button) rootView.findViewById(R.id.search_button);

        mProgressBar=(ProgressBar)rootView.findViewById(R.id.search_progress);
        mProgressBar.setVisibility(View.GONE);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager
                        mgr=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(mStreetNameEdit.getWindowToken(), 0);

                LinearLayout.LayoutParams layoutParams =
                        (LinearLayout.LayoutParams)mProgressBar.getLayoutParams();
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                layoutParams.weight = 1.0f;
                mProgressBar.setLayoutParams(layoutParams);
                mProgressBar.setVisibility(View.VISIBLE);

                if (mAdapter != null ) {
                    mAdapter.clear();
                }
                runService();
            }
        });

        mRouteListView =  (ListView)rootView.findViewById(R.id.route_listView);
        mRouteListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mStreetName = savedInstanceState.getString(STREET_NAME);
            mJsonResult = savedInstanceState.getString(ROUTES_RESULT);
            if (mStreetName != null) {
                mStreetNameEdit.setText(mStreetName);
            }
            if (mJsonResult != null) {
                showResults();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STREET_NAME, mStreetName);
        outState.putString(ROUTES_RESULT, mJsonResult);
    }

    public void runService() {
        mStreetName = mStreetNameEdit.getText().toString().trim();
        Intent intent =
                RouteService.makeIntent(getActivity(),
                        handler, mStreetName);

        getActivity().startService(intent);
    }

    public RouteListAdapter getRouteListAdapter() {
        return mAdapter;
    }


    private void showResults() {

        Log.d(TAG, mJsonResult);

        mProgressBar.setVisibility(View.GONE);
        Routes routes;
        Gson gson = new Gson();
        routes = gson.fromJson(mJsonResult, Routes.class);
        if (routes != null && routes.rows.size() > 0) {
            mAdapter = new RouteListAdapter(this,
                    routes.rows);

            mRouteListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }
        else {

            int formatId = R.string.format_no_results;

            Toast.makeText(getActivity(),
                    String.format(getString(formatId, mStreetName)),
                    Toast.LENGTH_SHORT).show();
        }




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

            final RouteListFragment routeListFragment = outerClass.get();

            // If RouteListFragment hasn't been garbage collected
            // (closed by user), proceed.
            if (routeListFragment != null && routeListFragment.getActivity() != null ) {

                // Extract the data from Message, which is in the form
                // of a Bundle that can be passed across processes.
                Bundle data = msg.getData();
                String json;
                if (data.containsKey(RouteService.EXTRA_ROUTES_BY_STOP_RESULTS_KEY)) {
                    routeListFragment.setJsonResult(data.getString(RouteService.EXTRA_ROUTES_BY_STOP_RESULTS_KEY));
                    routeListFragment.showResults();

                }

//                // is this necessary when using a foreground service?
//                routeListFragment.getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
////                        notifyDataSetChanged();
//                        RouteListAdapter adapter = routeListFragment.getRouteListAdapter();
//                        if (adapter != null) {
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                });


            }
        }
    }

}
