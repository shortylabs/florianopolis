package com.shortylabs.florianopolis;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.shortylabs.florianopolis.service.RouteService;


public class RouteDetailActivity extends ActivityGroup {

    public static final String  EXTRA_ROUTE_ID = "extraRouteId";
    public static final String  EXTRA_ROUTE_NAME = "extraRouteName";
    public static final String  CURR_TAB_INDEX = "currTabIndex";

    private Integer mRouteId;

    private TabHost mTabs;
    private int mCurrTabIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            mCurrTabIndex = savedInstanceState.getInt(CURR_TAB_INDEX);
        }
        setTitle(getIntent().getStringExtra(EXTRA_ROUTE_NAME));
        mRouteId = getIntent().getIntExtra(EXTRA_ROUTE_ID, -1);

        setContentView(R.layout.activity_route_detail);

        mTabs = (TabHost) findViewById(R.id.tabhost);

        // http://stackoverflow.com/questions/3272500/android-exception-did-you-forget-to-call-public-void-setup-localactivitymanag
//        mTabs.setup();
        mTabs.setup(this.getLocalActivityManager());

        TabHost.TabSpec spec = mTabs.newTabSpec("tab1");
        spec.setIndicator(getString(R.string.stops));
        Intent stopsIntent = new Intent(this, StopsActivity.class);
        stopsIntent.putExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, mRouteId);
        spec.setContent(stopsIntent);
        mTabs.addTab(spec);

        spec = mTabs.newTabSpec("tab2");
        spec.setIndicator(getString(R.string.weekday));
        Intent weekdayIntent = new Intent(this, WeekdayActivity.class);
        weekdayIntent.putExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, mRouteId);
        spec.setContent(weekdayIntent);
        mTabs.addTab(spec);

        spec = mTabs.newTabSpec("tab3");
        spec.setIndicator(getString(R.string.saturday));
        Intent saturdayIntent = new Intent(this, SaturdayActivity.class);
        saturdayIntent.putExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, mRouteId);
        spec.setContent(saturdayIntent);
        mTabs.addTab(spec);

        spec = mTabs.newTabSpec("tab4");
        spec.setIndicator(getString(R.string.sunday));
        Intent sundayIntent = new Intent(this, SundayActivity.class);
        sundayIntent.putExtra(RouteService.ROUTE_SERVICE_ROUTE_ID_KEY, mRouteId);
        spec.setContent(sundayIntent);
        mTabs.addTab(spec);

        if (mCurrTabIndex > -1) {
            mTabs.setCurrentTab(mCurrTabIndex);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURR_TAB_INDEX, mTabs.getCurrentTab());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.route_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}