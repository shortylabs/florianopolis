package com.shortylabs.florianopolis.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.shortylabs.florianopolis.FlorianopolisApp;
import com.shortylabs.florianopolis.model.ParamsRouteId;
import com.shortylabs.florianopolis.model.ParamsStopName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Jeri on 12/2/14.
 */
public class RouteService extends IntentService {

    private static final String ROUTE_SERVICE = "routeService";
    private static final String TAG = RouteService.class.getSimpleName();
    private static final String HEADER_AUTH = "Authorization";
    private static final String AUTH_RAW = "WKD4N7YMA1uiM8V:DtdTtzMLQlA0hk2C1Yi5pLyVIlAQ68";
    private static final String HEADER_AUTH_VALUE = "Basic " + Base64.encodeToString(AUTH_RAW.getBytes(), Base64.DEFAULT);
    private static final String HEADER_APPGLU = "X-AppGlu-Environment";
    private static final String HEADER_APPGLU_VALUE = "staging";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_CONTENT_TYPE_VALUE = "application/json";

    /**
     * The key used to store/retrieve a Messenger extra from a Bundle.
     */
    public static final String ROUTE_SERVICE_MESSENGER_KEY = "ROUTE_SERVICE_MESSENGER";
    public static final String ROUTE_SERVICE_STREET_NAME_KEY = "ROUTE_SERVICE_STREET_NAME";
    public static final String ROUTE_SERVICE_ROUTE_ID_KEY = "ROUTE_SERVICE_ROUTE_ID";

    public static final String ROUTE_SERVICE_REQUEST_TYPE = "ROUTE_SERVICE_REQUEST_TYPE";
    public static final String ROUTE_SERVICE_SEARCH_REQUEST = "ROUTE_SERVICE_SEARCH_REQUEST";
    public static final String ROUTE_SERVICE_STOPS_REQUEST = "ROUTE_SERVICE_STOPS_REQUEST";
    public static final String ROUTE_SERVICE_DEPARTURES_REQUEST = "ROUTE_SERVICE_DEPARTURES_REQUEST";

    public static final String EXTRA_ROUTES_BY_STOP_RESULTS_KEY = "extraRoutesByStopResultsKey";
    public static final String EXTRA_STOPS_BY_ROUTEID_KEY = "extraStopsByRouteIdKey";
    public static final String EXTRA_DEPARTURES_BY_ROUTEID_KEY = "extraDeparturesByRouteIdKey";

    private static final String URI_BASE = "https://api.appglu.com/v1/queries/";
    private static final String PATH_ROUTES_BY_STOP = "findRoutesByStopName";
    private static final String PATH_DEPARTURES_BY_ROUTE = "findDeparturesByRouteId";
    private static final String PATH_STOPS_BY_ROUTE = "findStopsByRouteId";
    private static final String PATH_RUN = "run";
    //https://api.appglu.com/v1/queries/findRoutesByStopName/run
    //https://api.appglu.com/v1/queries/findDeparturesByRouteId/run
    //https://api.appglu.com/v1/queries/findStopsByRouteId/run




    /**
     * Creates an IntentService.
     *
     */
    public RouteService() {
        super(ROUTE_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Messenger messenger;
        String streetName = null;
        String requestType = null;
        Integer routeId = null;
        if (intent.hasExtra(ROUTE_SERVICE_MESSENGER_KEY)) {
            messenger = (Messenger) intent.getExtras().get(ROUTE_SERVICE_MESSENGER_KEY);
        }
        else {
            Log.e(TAG, "Expected Messenger extra not found in intent");
            return;
        }

        if (intent.hasExtra(ROUTE_SERVICE_STREET_NAME_KEY)) {
            streetName = intent.getStringExtra(ROUTE_SERVICE_STREET_NAME_KEY);
        }

        if (intent.hasExtra(ROUTE_SERVICE_REQUEST_TYPE)) {
            requestType = intent.getStringExtra(ROUTE_SERVICE_REQUEST_TYPE);
        }

        if (intent.hasExtra(ROUTE_SERVICE_ROUTE_ID_KEY)) {
            routeId = intent.getIntExtra(ROUTE_SERVICE_ROUTE_ID_KEY, -1);
        }



        HttpsURLConnection con = null;

        StringBuilder builder = new StringBuilder();

        try {
            if (ROUTE_SERVICE_SEARCH_REQUEST.equals(requestType) && streetName != null) {
                con = setSearchParams(streetName);
            }
            else if (ROUTE_SERVICE_STOPS_REQUEST.equals(requestType)) {
                con = setStopsParams(routeId);
            }
            else if (ROUTE_SERVICE_DEPARTURES_REQUEST.equals(requestType)){
               con = setDeparturesParams(routeId);
            }
            else {
                Log.e(TAG, "Not an expected request type");
                return;
            }
            con.connect();
            InputStream is = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpResponseCache cache = HttpResponseCache.getInstalled();
        Log.d(TAG, "HttpResponseCache requests: "  + cache.getRequestCount() +
                ", hits: " + cache.getHitCount() +
                ", network: " + cache.getNetworkCount());

        sendResult(builder.toString(), messenger, requestType);
    }

    private HttpsURLConnection setSearchParams(String streetName){

        Uri builtUri = Uri.parse(URI_BASE).buildUpon()
                .appendPath(PATH_ROUTES_BY_STOP)
                .appendPath(PATH_RUN)
                .build();

        HttpsURLConnection con = null;

        try {
            con = (HttpsURLConnection) ( new URL(builtUri.toString())).openConnection();
            con.setRequestMethod("POST");

            con.setRequestProperty(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE);
            con.setRequestProperty(HEADER_AUTH, HEADER_AUTH_VALUE);
            con.setRequestProperty(HEADER_APPGLU, HEADER_APPGLU_VALUE);

            ParamsStopName params = new ParamsStopName(streetName);
            Gson gson = new Gson();

            // convert java object to JSON format,
            // and returned as JSON formatted string
            String json = gson.toJson(params);
            byte[] outputInBytes = json.getBytes("UTF-8");
            OutputStream os = con.getOutputStream();
            os.write( outputInBytes );
            os.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return con;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FlorianopolisApp application = (FlorianopolisApp) getApplication();
        application.flushCache();
    }

    private HttpsURLConnection setStopsParams(Integer routeId){

        Uri builtUri = Uri.parse(URI_BASE).buildUpon()
                .appendPath(PATH_STOPS_BY_ROUTE)
                .appendPath(PATH_RUN)
                .build();

        HttpsURLConnection con = null;

        try {
            con = (HttpsURLConnection) ( new URL(builtUri.toString())).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE);
            con.setRequestProperty(HEADER_AUTH, HEADER_AUTH_VALUE);
            con.setRequestProperty(HEADER_APPGLU, HEADER_APPGLU_VALUE);


            con.addRequestProperty("Cache-Control", "max-age=0");
            ParamsRouteId params = new ParamsRouteId(routeId);
            Gson gson = new Gson();

            // convert java object to JSON format,
            // and returned as JSON formatted string
            String json = gson.toJson(params);
            byte[] outputInBytes = json.getBytes("UTF-8");
            OutputStream os = con.getOutputStream();
            os.write( outputInBytes );
            os.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return con;
    }


    private HttpsURLConnection setDeparturesParams(Integer routeId){

        Uri builtUri = Uri.parse(URI_BASE).buildUpon()
                .appendPath(PATH_DEPARTURES_BY_ROUTE)
                .appendPath(PATH_RUN)
                .build();

        HttpsURLConnection con = null;

        try {
            con = (HttpsURLConnection) ( new URL(builtUri.toString())).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE);
            con.setRequestProperty(HEADER_AUTH, HEADER_AUTH_VALUE);
            con.setRequestProperty(HEADER_APPGLU, HEADER_APPGLU_VALUE);
            ParamsRouteId params = new ParamsRouteId(routeId);
            Gson gson = new Gson();

            // convert java object to JSON format,
            // and returned as JSON formatted string
            String json = gson.toJson(params);
            byte[] outputInBytes = json.getBytes("UTF-8");
            OutputStream os = con.getOutputStream();
            os.write( outputInBytes );
            os.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return con;
    }

    /**
     *	Use the provided Messenger to send a Message to a Handler in
     *	another process.
     *
     * 	The provided string, result, should be put into a Bundle
     * 	and included with the sent Message.
     */
    public static void sendResult(String result,
                                  Messenger messenger,
                                  String requestType) {
        Log.d(TAG, "sendResult: " + requestType);
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        if (ROUTE_SERVICE_SEARCH_REQUEST.equals(requestType)) {
            data.putString(EXTRA_ROUTES_BY_STOP_RESULTS_KEY, result);
        }
        else if (ROUTE_SERVICE_STOPS_REQUEST.equals(requestType)) {
            data.putString(EXTRA_STOPS_BY_ROUTEID_KEY, result);
        }
        else if (ROUTE_SERVICE_DEPARTURES_REQUEST.equals(requestType)){
            data.putString(EXTRA_DEPARTURES_BY_ROUTEID_KEY,
                    result);
        }

        // Make the Bundle the "data" of the Message.
        msg.setData(data);

        try {
            // Send the Message back to the client Activity.
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    /**
     * Make an intent that will start this service if supplied to
     * startService() as a parameter.
     *
     * @param context		The context of the calling component.
     * @param handler		The handler that the service should
     *                          use to respond with a result
     * @param streetName    The search target
     *
     */
    public static Intent makeIntent(Context context,
                                    Handler handler,
                                    String streetName) {

        Messenger messenger = new Messenger(handler);

        Intent intent = new Intent(context,
                RouteService.class);
        intent.putExtra(ROUTE_SERVICE_MESSENGER_KEY,
                messenger);
        intent.putExtra(ROUTE_SERVICE_STREET_NAME_KEY,
                streetName);
        intent.putExtra(ROUTE_SERVICE_REQUEST_TYPE,
                ROUTE_SERVICE_SEARCH_REQUEST);

        return intent;

    }


    /**
     * Make an intent that will start this service if supplied to
     * startService() as a parameter.
     *
     * @param context		The context of the calling component.
     * @param handler		The handler that the service should
     *                          use to respond with a result
     * @param routeId       The routeId for the request
     *
     * @param requestType   STOPS or DEPARTURES
     *
     */
    public static Intent makeIntent(Context context,
                                    Handler handler,
                                    Integer routeId,
                                    String requestType) {

        Messenger messenger = new Messenger(handler);

        Intent intent = new Intent(context,
                RouteService.class);
        intent.putExtra(ROUTE_SERVICE_MESSENGER_KEY,
                messenger);
        intent.putExtra(ROUTE_SERVICE_ROUTE_ID_KEY,
                routeId);
        intent.putExtra(ROUTE_SERVICE_REQUEST_TYPE,
                requestType);

        return intent;

    }
}
