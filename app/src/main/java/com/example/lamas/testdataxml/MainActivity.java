package com.example.lamas.testdataxml;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.lamas.testdataxml.data.Data;
import com.example.lamas.testdataxml.data.Monument;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.Locale;
import java.util.Map;


public class MainActivity extends Activity implements TextToSpeech.OnInitListener{

    private MapView map;
    private TextToSpeech textToSpeech;
    private float radius = 50;
    private LocationManager locationManager;
    private HandlerThread handlerThread;
    private Handler checkGPShandler, safetyChackHandler;
    private Handler mainHandler = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == MSG_SHOW_GPS_ALERT){
                waitForGPSDialog.show();
                convertTextToSpeech("Recherche d'un signal GPS");
                poorAccuracyCounter++;
                if(poorAccuracyCounter==5){

                }
            }
            else if(msg.what == MSG_DISMISS_GPS_ALERT){
                waitForGPSDialog.dismiss();
                convertTextToSpeech("Signal GPS trouvé, reprise de l'itinéraire");
            }
            else if(msg.what == MSG_SHOW_LOST_ALERT){
                safetyCheckDialog.show();
                convertTextToSpeech("Êtes-vous perdu? Tous se passe bien? ");
            }
        }
    };
    private volatile long lastUpdateTimestamp;
    private volatile long lastCheckGPSTimestamp = System.currentTimeMillis();
    private volatile long delay;
    private int poorAccuracyCounter=0;
    private MyLocationListener mylistener;
    private volatile Location myLocation;
    private Location myPreviousLocation;
    private int samePreviousLocationCounter =0;
    private Marker instantMarker;
    private Polygon instantAccuracy;
    private IMapController mapController;
    private Data data;
    private IntentFilter mIntentFilter;
    private ProximityReceiver proximityReceiver;
    private volatile boolean alertsAreActivated = false;
    private AlertDialog waitForGPSDialog;
    private AlertDialog safetyCheckDialog;
    private MockLocation mock;
    String ACTION_FILTER = "com.example.lamas.testdataxml.ProximityReceiver";
    private static int MSG_SHOW_GPS_ALERT = 0;
    private static int MSG_DISMISS_GPS_ALERT = 1;
    private static int MSG_SHOW_LOST_ALERT = 2;


    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        checkGPShandler.removeCallbacks(checkGPS);
        safetyChackHandler.removeCallbacks(checkLost);
        handlerThread.quit();
        if(alertsAreActivated){
            removeProximityAlerts();
            //alertsAreActivated = false;
        }
        unregisterReceiver(proximityReceiver);
        textToSpeech.shutdown();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Introduction
        super.onCreate(savedInstanceState);

        //Check if the user have coarse location permission
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);
        }
        textToSpeech = new TextToSpeech(this, this);
        AlertDialog.Builder alertBuilder= new AlertDialog.Builder(this);
        alertBuilder.setMessage("En attente d'une meilleure réception GPS ...");
        waitForGPSDialog = alertBuilder.create();

        alertBuilder= new AlertDialog.Builder(this);
        alertBuilder.setMessage("Vous semblez perdu. Est-que tout va bien ?");
        alertBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                safetyCheckDialog.dismiss();
            }
        });

        alertBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        safetyCheckDialog = alertBuilder.create();

        //i'm registering my Receiver First
        mIntentFilter = new IntentFilter(ACTION_FILTER);
        proximityReceiver = new ProximityReceiver();
        registerReceiver(proximityReceiver, mIntentFilter);

        //configuration of the map
        setContentView(R.layout.activity_main);
        map = (MapView) findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        mapController = map.getController();
        mapController.setZoom(18);

        //Add data
        data = Data.getInstance(getApplicationContext());
        for (Map.Entry<Integer, Monument> entry : data.getMonuments().entrySet()) {
            Marker temp = new Marker(map);
            GeoPoint geo = new GeoPoint(entry.getValue().getLatitude(), entry.getValue().getLongitude());
            temp.setPosition(geo);
            mapController.setCenter(geo);
            temp.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            temp.setTitle(entry.getValue().getName());
            map.getOverlays().add(temp);
            map.getOverlays().add(createCircle(geo, Color.RED, radius));

        }

        //Enabled GPS
        if (Constants.allow_mock_location) {
            mock = new MockLocation(LocationManager.GPS_PROVIDER, this);
        }
        waitForGPSDialog.show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mylistener = new MyLocationListener();
        // location updates: at least 1 meter and 200millsecs change
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mylistener);

        //Create thread
        handlerThread = new HandlerThread("myWorker", HandlerThread.MAX_PRIORITY);
        handlerThread.start();
        checkGPShandler = new Handler(handlerThread.getLooper());
        safetyChackHandler = new Handler(handlerThread.getLooper());
        checkGPS.run();
        checkLost.run();


    }


    public void activateProximityAlerts(){
        for(Map.Entry<Integer, Monument> entry : data.getMonuments().entrySet()){
            Intent temp_intent= new Intent(ACTION_FILTER);
            temp_intent.putExtra("name", entry.getValue().getName());
            temp_intent.putExtra("id", entry.getValue().getId());
            PendingIntent temp_pi = PendingIntent.getBroadcast(getApplicationContext(), entry.getValue().getId(), temp_intent, PendingIntent.FLAG_CANCEL_CURRENT);
            locationManager.addProximityAlert(entry.getValue().getLatitude(), entry.getValue().getLongitude(), radius, 100000000, temp_pi);
        }
    }

    public void removeProximityAlerts(){
        for(Map.Entry<Integer, Monument> entry : data.getMonuments().entrySet()){
            Intent temp_intent= new Intent(ACTION_FILTER);
            temp_intent.putExtra("name", entry.getValue().getName());
            temp_intent.putExtra("id", entry.getValue().getId());
            PendingIntent temp_pi = PendingIntent.getBroadcast(getApplicationContext(), entry.getValue().getId(), temp_intent, PendingIntent.FLAG_CANCEL_CURRENT);
            locationManager.removeProximityAlert(temp_pi);
        }
    }

    private Polygon createCircle(GeoPoint geopoint, int color, float radius){
        Polygon circle = new Polygon(this);
        circle.setPoints(Polygon.pointsAsCircle(geopoint, radius));
        circle.setFillColor(0x12121212);
        circle.setStrokeColor(color);
        circle.setStrokeWidth(2);
        return circle;
    }

    public Marker getInstantMarker() {
        return instantMarker;
    }

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.CANADA_FRENCH);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "This Language is not supported");
            }
            else{
                convertTextToSpeech("En attente du signal GPS");
            }
        } else {
            Log.e("error", "Initilization Failed!");
        }
    }

    private void convertTextToSpeech(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            //long duration = System.currentTimeMillis()-lastUpdateTimestamp;
            //Toast.makeText(MainActivity.this, "Délais "+duration, Toast.LENGTH_SHORT).show();
            myLocation = location;
            lastUpdateTimestamp = System.currentTimeMillis();
            if(instantMarker == null){
                instantMarker = new Marker(map);
            }
            else{
                map.getOverlays().remove(instantMarker);
            }

            GeoPoint instantGeopoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            if(location.hasAccuracy()){
                if(instantAccuracy==null){
                    instantAccuracy = new Polygon(getApplicationContext());
                }
                else {
                    map.getOverlays().remove(instantAccuracy);
                }

                instantAccuracy = createCircle(instantGeopoint, Color.BLUE, location.getAccuracy());
                map.getOverlays().add(instantAccuracy);

            }

            instantMarker.setPosition(instantGeopoint);
            instantMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            instantMarker.setTitle("I'm here!");
            instantMarker.setIcon(getResources().getDrawable(R.drawable.direction_arrow));
            map.getOverlays().add(instantMarker);
            mapController.setCenter(instantGeopoint);
            map.invalidate();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    }

    public void pushNewLocation(double lon, double lat, float accuracy){
        mock.pushLocation(lon, lat, accuracy);
    }

    public AlertDialog getWaitForGPSDialog() {
        return waitForGPSDialog;
    }

    public AlertDialog getSafetyCheckDialog() {
        return safetyCheckDialog;
    }

    private Runnable checkGPS = new Runnable() {
        @Override
        public void run() {
            delay = System.currentTimeMillis() - lastCheckGPSTimestamp;
            lastCheckGPSTimestamp = System.currentTimeMillis();
            if(myLocation!=null && myLocation.getAccuracy() <= Constants.MIN_ACCURACY){
                if(!alertsAreActivated){
                    activateProximityAlerts();
                    alertsAreActivated = true;
                    mainHandler.sendEmptyMessage(MSG_DISMISS_GPS_ALERT);
                }
            }
            else{
                if(alertsAreActivated){
                    removeProximityAlerts();
                    alertsAreActivated = false;
                    mainHandler.sendEmptyMessage(MSG_SHOW_GPS_ALERT);
                }
            }
            mainHandler.sendEmptyMessage(MSG_DISMISS_GPS_ALERT);
            checkGPShandler.postDelayed(checkGPS, 3000);
        }
    };

    private Runnable checkLost = new Runnable() {
        @Override
        public void run() {
            if(myLocation!=null) {
                if (samePreviousLocationCounter == 0) {
                    myPreviousLocation = myLocation;
                    samePreviousLocationCounter++;
                } else {
                    if (myLocation.distanceTo(myPreviousLocation) < 10.0) {
                        samePreviousLocationCounter++;
                        if (samePreviousLocationCounter == 5) {
                            mainHandler.sendEmptyMessage(MSG_SHOW_LOST_ALERT);
                        }
                    }
                }
            }
            safetyChackHandler.postDelayed(checkLost, 3000);
        }
    };

}


