package com.shortylabs.florianopolis.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.shortylabs.florianopolis.model.ParamsStopName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public static final String EXTRA_ROUTES_BY_STOP_RESULTS_KEY = "extraRoutesByStopResultsKey"; // JSON returned from Routes by Stop request

    private static final String URI_BASE = "https://api.appglu.com/v1/queries/";
    private static final String PATH_ROUTES_BY_STOP = "findRoutesByStopName";
    private static final String PATH_DEPARTURES_BY_ROUTE = "findDeparturesByRouteId";
    private static final String PATH_STOPS_BY_ROUTE = "findStopsByRouteId";
    private static final String PATH_RUN = "run";
    //https://api.appglu.com/v1/queries/findRoutesByStopName/run
    //https://api.appglu.com/v1/queries/findDeparturesByRouteId/run
    //https://api.appglu.com/v1/queries/findStopsByRouteId/run




    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public RouteService() {
        super(ROUTE_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Messenger messenger;
        String streetName;
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
        else {
            Log.e(TAG, "Expected street name extra not found in intent");
            return;
        }

        Uri builtUri = Uri.parse(URI_BASE).buildUpon()
                .appendPath(PATH_ROUTES_BY_STOP)
                .appendPath(PATH_RUN)
                .build();

        StringBuilder builder = new StringBuilder();
        HttpURLConnection con;
        try {



            con = (HttpURLConnection) ( new URL(builtUri.toString())).openConnection();
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

        sendResult(builder.toString(), messenger);
    }

    /**
     *	Use the provided Messenger to send a Message to a Handler in
     *	another process.
     *
     * 	The provided string, result, should be put into a Bundle
     * 	and included with the sent Message.
     */
    public static void sendResult(String result,
                                  Messenger messenger) {
        Log.d(TAG, "sendResult returning RESULT");
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putString(EXTRA_ROUTES_BY_STOP_RESULTS_KEY,
                result);

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

        return intent;

    }
}
