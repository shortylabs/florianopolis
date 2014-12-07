package com.shortylabs.florianopolis;

import android.app.Fragment;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.shortylabs.florianopolis.service.RouteService;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteDetailFragment extends Fragment {

    private final String TAG = RouteDetailFragment.class.getSimpleName();
    public static final String  EXTRA_ROUTE_ID = "extraRouteId";
    public static final String  EXTRA_ROUTE_NAME = "extraRouteName";
    private Integer mRouteId;

    public RouteDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {


        getActivity().setTitle(getActivity().getIntent().getStringExtra(EXTRA_ROUTE_NAME));
        mRouteId = getActivity().getIntent().getIntExtra(EXTRA_ROUTE_ID, -1);

        TabHost tabs = (TabHost) inflater.inflate(R.layout.route_tabhost, container, false);

        // http://stackoverflow.com/questions/3163884/android-tabhost-without-tabactivity
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(getActivity(), false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        tabs.setup(mLocalActivityManager);
//        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("tab1");
        spec.setIndicator(getString(R.string.stops));
        Intent stopsIntent = new Intent(getActivity(), StopsActivity.class);
        stopsIntent.putExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, mRouteId);
        spec.setContent(stopsIntent);
        tabs.addTab(spec);

        spec=tabs.newTabSpec("tab2");
        spec.setIndicator(getString(R.string.weekday));
        Intent weekdayIntent = new Intent(getActivity(), WeekdayActivity.class);
        weekdayIntent.putExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, mRouteId);
        spec.setContent(weekdayIntent);
        tabs.addTab(spec);

        spec=tabs.newTabSpec("tab3");
        spec.setIndicator(getString(R.string.saturday));
        Intent saturdayIntent = new Intent(getActivity(), SaturdayActivity.class);
        saturdayIntent.putExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, mRouteId);
        spec.setContent(saturdayIntent);
        tabs.addTab(spec);

        spec=tabs.newTabSpec("tab4");
        spec.setIndicator(getString(R.string.sunday));
        Intent sundayIntent = new Intent(getActivity(), SundayActivity.class);
        sundayIntent.putExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, mRouteId);
        spec.setContent(sundayIntent);
        tabs.addTab(spec);

        return tabs;
    }
}
