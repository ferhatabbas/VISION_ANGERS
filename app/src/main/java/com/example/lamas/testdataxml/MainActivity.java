package com.example.lamas.testdataxml;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.lamas.testdataxml.data.Data;
import com.example.lamas.testdataxml.data.Monument;
import com.example.lamas.testdataxml.data.ParcoursABC;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    private MapView map;
    private Intent temp_intent = new Intent(Constants.ACTION_FILTER);
    private TextToSpeech textToSpeech;
    private LocationManager locationManager;
    private HandlerThread handlerThread = new HandlerThread("myWorker", HandlerThread.MAX_PRIORITY);
    private Handler checkGPShandler, safetyChackHandler;
    private MessagesHandler mainHandler = new MessagesHandler(this);
    private volatile long lastCheckGPSTimestamp = System.currentTimeMillis();
    private int poorAccuracyCounter = 0;
    private GeoPoint instantGeopoint;
    private MyLocationListener mylistener;
    private volatile Location myLocation;
    private Marker instantMarker;
    private Polygon instantAccuracy;
    private IMapController mapController;
    private Data data;
    private IntentFilter mIntentFilter;
    private ProximityReceiver proximityReceiver;
    private volatile boolean alertsAreActivated = false;
    private AlertDialog waitForGPSDialog, safetyCheckDialog;
    private Notification notificationGPSCheck, notificationSafetyCheck;
    private NotificationManager notificationManager;
    private MockLocation mock;
    private static int MSG_SHOW_GPS_ALERT = 0;
    private static int MSG_DISMISS_GPS_ALERT = 1;
    private static int MSG_SHOW_LOST_ALERT = 2;
    private ParcoursABC parcours;
    private Monument monument;
    private int currentPOI = 0;
    private int idParcours = 1;


    @Override
    protected void onResume() {

        super.onResume();
        if (data.getParameters().isGooglemaps()) {
            parcours = data.getParcourses().get(idParcours);
            monument = parcours.getMonuments().get(currentPOI);
            currentPOI++;
            googleMapMode(monument.getLatitude(), monument.getLongitude());

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(mylistener);
        checkGPShandler.removeCallbacks(checkGPS);
        safetyChackHandler.removeCallbacks(checkLost);
        handlerThread.quit();
        if (alertsAreActivated) {
            removeProximityAlerts();
            //alertsAreActivated = false;
        }
        unregisterReceiver(proximityReceiver);
        unregisterReceiver(batteryChangeReceiver);
        if(textToSpeech != null){
            textToSpeech.shutdown();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Introduction
        super.onCreate(savedInstanceState);

        // Retrieve course id
        if(!Constants.ALLOW_MOCK_LOCATION){
            Bundle b = getIntent().getExtras();
            idParcours = b.getInt("id");
        }
        //Check if the user have coarse location permission
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);
        }

        // Initialize textToSpeech
        textToSpeech = new TextToSpeech(this, this);

        // Build all dialog alerts required
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("En attente d'une meilleure réception GPS ...");
        waitForGPSDialog = alertBuilder.create();

        alertBuilder = new AlertDialog.Builder(this);
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
                Intent intent = new Intent(Intent.ACTION_DIAL);
                startActivity(intent);
            }
        });
        safetyCheckDialog = alertBuilder.create();

        // Build all notifications required
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification = new Notification.Builder(getApplicationContext());
        notification.setSmallIcon(R.drawable.navto_small);
        notification.setContentTitle("Aucun signal GPS disponible!");
        notification.setContentText("Aucun signal GPS n'est disponible pour le moment, en attente d'un signal valide.");
        notification.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        notificationGPSCheck = notification.build();

        notification.setSmallIcon(R.drawable.person);
        notification.setContentTitle("Vous semblez perdu. Est-que tout va bien ?");
        notification.setContentText("Vous semblez perdu. Est-que tout va bien ?");
        notificationSafetyCheck = notification.build();

        // Registering ProximityReveiver
        mIntentFilter = new IntentFilter(Constants.ACTION_FILTER);
        proximityReceiver = new ProximityReceiver();
        registerReceiver(proximityReceiver, mIntentFilter);

        // Registering BatteryChangeReceiver
        registerReceiver(batteryChangeReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_LOW));

        //configuration of the map
        setContentView(R.layout.activity_main);
        map = (MapView) findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        mapController = map.getController();
        mapController.setZoom(18);

        //Add data
        data = Data.getInstance(getApplicationContext());
        ArrayList<Monument> monuments = data.getParcourses().get(idParcours).getMonuments();
        int size = monuments.size();
        Monument entry;
        for(int i=0; i<size; i++){
            entry = monuments.get(i);
            Marker temp = new Marker(map);
            GeoPoint geo = new GeoPoint(entry.getLatitude(), entry.getLongitude());
            temp.setPosition(geo);
            mapController.setCenter(geo);
            temp.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            temp.setTitle(entry.getName());

            if (Constants.DEBUG_MODE) {
                map.getOverlays().add(temp);
                map.getOverlays().add(createCircle(geo, Color.RED, entry.getRadius()));
            }
        }


        //Enabled GPS
        if (Constants.ALLOW_MOCK_LOCATION) {
            mock = new MockLocation(LocationManager.GPS_PROVIDER, this);
        }
        waitForGPSDialog.show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mylistener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.REQUEST_LOCATION_MANAGER_TIME, 0, mylistener);

        //Create thread
        //handlerThread = new HandlerThread("myWorker", HandlerThread.MAX_PRIORITY);
        handlerThread.start();
        checkGPShandler = new Handler(handlerThread.getLooper());
        safetyChackHandler = new Handler(handlerThread.getLooper());
        //checkGPS.run();
        //checkLost.run();

        // verifier si google map sera utilisé ou pas
        if (data.getParameters().isGooglemaps()) {
            parcours = data.getParcourses().get(idParcours);
            monument = parcours.getMonuments().get(currentPOI);
            currentPOI++;
            googleMapMode(monument.getLatitude(), monument.getLongitude());

        }

    }

    public void googleMapMode(double lat, double lng) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng + "&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(mapIntent);
    }

    public void activateProximityAlerts() {
        ArrayList<Monument> monuments = data.getParcourses().get(idParcours).getMonuments();
        int size = monuments.size();
        for(int i=0; i<size; i++){
            Monument temp = monuments.get(i);
            temp_intent.putExtra("name", temp.getName());
            temp_intent.putExtra("id", temp.getId());
            PendingIntent temp_pi = PendingIntent.getBroadcast(getApplicationContext(), temp.getId(), temp_intent, PendingIntent.FLAG_CANCEL_CURRENT);
            locationManager.addProximityAlert(temp.getLatitude(), temp.getLongitude(), temp.getRadius(), Integer.MAX_VALUE, temp_pi);
        }
    }


    private void manage_proximity_alerts(Location location){
        if(location.getAccuracy() <= Constants.MIN_ACCURACY){
            if(!alertsAreActivated){
                activateProximityAlerts();
                alertsAreActivated = true;
                if(waitForGPSDialog.isShowing()){
                    waitForGPSDialog.dismiss();
                }
            }
        }
        else{
            if(alertsAreActivated){
                removeProximityAlerts();
                alertsAreActivated = false;
                if(!waitForGPSDialog.isShowing()){
                    waitForGPSDialog.show();
                }
            }
        }
    }

    public void removeProximityAlerts() {
        ArrayList<Monument> monuments = data.getParcourses().get(idParcours).getMonuments();
        int size = monuments.size();
        for(int i=0; i<size; i++){
            Monument temp = monuments.get(i);
            temp_intent.putExtra("name", temp.getName());
            temp_intent.putExtra("id", temp.getId());
            PendingIntent temp_pi = PendingIntent.getBroadcast(getApplicationContext(), temp.getId(), temp_intent, PendingIntent.FLAG_CANCEL_CURRENT);
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

    @Override
    public void onLowMemory(){
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        pm.reboot(null);
    }

    private void convertTextToSpeech(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            //long duration = System.currentTimeMillis()-lastUpdateTimestamp;
            //Toast.makeText(MainActivity.this, "Délais "+duration, Toast.LENGTH_SHORT).show();
            if(myLocation == null){
                initialize_position_layer(location);
            }
            else{
                map.getOverlays().remove(instantMarker);
                map.getOverlays().remove(instantAccuracy);
            }
            myLocation = location;
            lastCheckGPSTimestamp = System.currentTimeMillis();

            instantGeopoint.setLatitudeE6( (int) (location.getLatitude() * 1E6));
            instantGeopoint.setLongitudeE6((int) (location.getLongitude() * 1E6));

            if(location.hasAccuracy()){
                manage_proximity_alerts(location);
                instantAccuracy.setPoints(Polygon.pointsAsCircle(instantGeopoint, location.getAccuracy()));
                map.getOverlays().add(instantAccuracy);
            }

            instantMarker.setPosition(instantGeopoint);
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

    private void initialize_position_layer(Location location){
        instantMarker = new Marker(map);
        instantMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        instantMarker.setTitle("Vous êtes ici.");
        instantMarker.setIcon(getResources().getDrawable(R.drawable.direction_arrow));
        instantGeopoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        if(location.hasAccuracy()){
            instantAccuracy = createCircle(instantGeopoint, Color.BLUE, location.getAccuracy());
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

    public Marker getInstantMarker() {
        return instantMarker;
    }

    private Runnable checkGPS = new Runnable() {
        @Override
        public void run() {
            if(myLocation!=null && (System.currentTimeMillis()-lastCheckGPSTimestamp <= Constants.REQUEST_LOCATION_MANAGER_TIME+1000)
                    &&(myLocation.getAccuracy() <= Constants.MIN_ACCURACY)){
                if(waitForGPSDialog.isShowing())
                    mainHandler.sendEmptyMessage(MSG_DISMISS_GPS_ALERT);
            }
            else{

                if(!waitForGPSDialog.isShowing())
                    mainHandler.sendEmptyMessage(MSG_SHOW_GPS_ALERT);

            }
            checkGPShandler.postDelayed(checkGPS, Constants.WAIT_FOR_GPS_TIMEOUT);
        }
    };

    private Runnable checkLost = new Runnable() {

        private Location lastLocation;
        private int sameLocationCount = 0;
        @Override
        public void run() {
            if(myLocation!=null){
                if(sameLocationCount == 0){
                    lastLocation = myLocation;
                    sameLocationCount++;
                }
                else{
                    if(myLocation.distanceTo(lastLocation) < 10.0){
                        sameLocationCount++;
                        if(sameLocationCount == 5){
                            mainHandler.sendEmptyMessage(MSG_SHOW_LOST_ALERT);
                            sameLocationCount = 0;
                        }
                    }
                    lastLocation = myLocation;
                }
            }
            safetyChackHandler.postDelayed(checkLost, Constants.SAFETY_CHECK_TIMEOUT);
        }
    };

    private BroadcastReceiver batteryChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            locationManager.removeUpdates(mylistener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    Constants.REQUEST_LOCATION_MANAGER_TIME*10, 0, mylistener);
        }
    };

    private static class MessagesHandler extends Handler{
        private final WeakReference<MainActivity> myClassWeakReference;
        public MessagesHandler(MainActivity myClassInstance) {
            myClassWeakReference = new WeakReference<>(myClassInstance);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity myClass = myClassWeakReference.get();
            if (myClass != null) {
                if (msg.what == MSG_SHOW_GPS_ALERT) {
                    myClass.waitForGPSDialog.show();
                    myClass.convertTextToSpeech("Recherche d'un signal GPS");
                    myClass.poorAccuracyCounter++;
                    if (myClass.poorAccuracyCounter == 5) {
                        myClass.notificationManager.notify(Constants.CHECK_GPS_NOTIFICATION_ID, myClass.notificationGPSCheck);
                        myClass.poorAccuracyCounter = 0;
                    }
                } else if (msg.what == MSG_DISMISS_GPS_ALERT) {
                    myClass.waitForGPSDialog.dismiss();
                    myClass.convertTextToSpeech("Signal GPS trouvé, reprise de l'itinéraire");
                    myClass.notificationManager.cancelAll();
                } else if (msg.what == MSG_SHOW_LOST_ALERT) {
                    myClass.safetyCheckDialog.show();
                    myClass.convertTextToSpeech("Êtes-vous perdu? Tous se passe bien? ");
                    myClass.notificationManager.notify(Constants.SAFETYCHECK_NOTIFICATION_ID, myClass.notificationSafetyCheck);
                }
            }
        }
    }

}


