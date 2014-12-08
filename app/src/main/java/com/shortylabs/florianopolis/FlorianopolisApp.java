package com.shortylabs.florianopolis;

import android.app.Activity;
import android.app.Application;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jeri on 12/7/14.
 */
public class FlorianopolisApp  extends Application {

    private static final String TAG = FlorianopolisApp.class.getSimpleName();

    // Use a small in-memory cache for the departures json responses by routeId
    // Sized to hold departure info for about 20 routes
    private LruCache<Integer,String> departuresCache = new LruCache<Integer, String>(3 * 1024 * 20);

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            // Experiment with HttpResponseCache
            File httpCacheDir = new File(getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        }
        catch (IOException e) {
            Log.i(TAG, "HTTP response cache installation failed:" + e);
        }
    }


    // Experiment with HttpResponseCache
    // method to flush cache contents to the filesystem
    public void flushCache() {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            Log.d(TAG, "flushCache");
            cache.flush();
        }
    }

    // Experiment with HttpResponseCache
    private class FlorianopolisAppActivityLifeCycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        public void onActivityStopped (Activity activity) {
            flushCache();
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }

        // other methods
    }

    public String getDepartures(Integer routeId){
        String json = null;
        synchronized (departuresCache) {
                json =  departuresCache.get(routeId);
        }
        return json;
    }

    public void putDepartures(Integer routeId, String json) {
        synchronized (departuresCache) {
           departuresCache.put(routeId, json);
        }

    }

}
