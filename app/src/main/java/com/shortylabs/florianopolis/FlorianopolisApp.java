package com.shortylabs.florianopolis;

import android.app.Activity;
import android.app.Application;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jeri on 12/7/14.
 */
public class FlorianopolisApp  extends Application {

    private static final String TAG = FlorianopolisApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            File httpCacheDir = new File(getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB , stops & departures ~ 6000 bytes
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
            Log.d(TAG, "HttpResponseCache installed, requests:  " + HttpResponseCache.getInstalled().getRequestCount());
        }
            catch (IOException e){
                Log.i(TAG, "HTTP response cache installation failed:" + e);
            }
    }

    // method to flush cache contents to the filesystem
    public void flushCache() {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    private class FlorianopolisActivityLifeCycleCallbacks implements ActivityLifecycleCallbacks {
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
}
