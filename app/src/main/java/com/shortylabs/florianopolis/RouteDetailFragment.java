package com.shortylabs.florianopolis;

import android.app.Fragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteDetailFragment extends Fragment {

    private final String TAG = RouteDetailFragment.class.getSimpleName();
//    public static final String  EXTRA_ROUTE_ID = "extraRouteId";
//    public static final String  EXTRA_ROUTE_NAME = "extraRouteName";
//    public static final String  CURR_TAB_INDEX = "currTabIndex";

//    private Integer mRouteId;
//
//    private TabHost mTabs;
//    private Integer mCurrTabIndex;

    public RouteDetailFragment() {
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//
//
//        getActivity().setTitle(getActivity().getIntent().getStringExtra(EXTRA_ROUTE_NAME));
//        mRouteId = getActivity().getIntent().getIntExtra(EXTRA_ROUTE_ID, -1);
//
//        mTabs = (TabHost) inflater.inflate(R.layout.route_tabhost, container, false);
//
//        // http://stackoverflow.com/questions/3163884/android-tabhost-without-tabactivity
//        LocalActivityManager mLocalActivityManager = new LocalActivityManager(getActivity(), false);
//        mLocalActivityManager.dispatchCreate(savedInstanceState);
//        mTabs.setup(mLocalActivityManager);
////        mTabs.setup();
//
//        TabHost.TabSpec spec=mTabs.newTabSpec("tab1");
//        spec.setIndicator(getString(R.string.stops));
//        Intent stopsIntent = new Intent(getActivity(), StopsActivity.class);
//        stopsIntent.putExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, mRouteId);
//        spec.setContent(stopsIntent);
//        mTabs.addTab(spec);
//
//        spec=mTabs.newTabSpec("tab2");
//        spec.setIndicator(getString(R.string.weekday));
//        Intent weekdayIntent = new Intent(getActivity(), WeekdayActivity.class);
//        weekdayIntent.putExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, mRouteId);
//        spec.setContent(weekdayIntent);
//        mTabs.addTab(spec);
//
//        spec=mTabs.newTabSpec("tab3");
//        spec.setIndicator(getString(R.string.saturday));
//        Intent saturdayIntent = new Intent(getActivity(), SaturdayActivity.class);
//        saturdayIntent.putExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, mRouteId);
//        spec.setContent(saturdayIntent);
//        mTabs.addTab(spec);
//
//        spec=mTabs.newTabSpec("tab4");
//        spec.setIndicator(getString(R.string.sunday));
//        Intent sundayIntent = new Intent(getActivity(), SundayActivity.class);
//        sundayIntent.putExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, mRouteId);
//        spec.setContent(sundayIntent);
//        mTabs.addTab(spec);
//
//        return mTabs;
//    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState != null) {
//            mCurrTabIndex = savedInstanceState.getInt(CURR_TAB_INDEX);
//            mTabs.setCurrentTab(mCurrTabIndex);
//        }
//    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt(CURR_TAB_INDEX, mTabs.getCurrentTab());
//    }
}
