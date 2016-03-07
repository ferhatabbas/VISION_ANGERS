package com.example.lamas.testdataxml;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.Map;


/**
 * This is the implementation of OSMBonusPack tutorials.
 * Sections of code can be commented/uncommented depending on the progress in the tutorials.
 *
 * @author M.Kergall
 * @see <a href="https://github.com/MKergall/osmbonuspack">OSMBonusPack on GitHub</a>
 */
public class MainActivity extends Activity {

    MapView map;
    private float radius = 50;
    private LocationManager locationManager;
    private MyLocationListener mylistener;
    private String provider;
    private Criteria criteria;
    private Marker instantMarker;
    private Polygon instantAccuracy;
    private IMapController mapController;
    private Data data;
    private IntentFilter mIntentFilter;
    private ProximityReceiver proximityReceiver;
    private boolean receiverIsRegistered = false;
    String ACTION_FILTER = "com.example.PROXIMITY_ALERT";

    @Override
    protected void onResume(){
        super.onResume();
        //proximityReceiver = new ProximityReceiver();
        //registerReceiver(proximityReceiver, mIntentFilter);
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

        //i'm registering my Receiver First
        mIntentFilter = new IntentFilter(ACTION_FILTER);
        proximityReceiver = new ProximityReceiver();

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
            Polygon circle = createCircle(geo, Color.RED, radius);
            map.getOverlays().add(circle);

        }

        //Enabled GPS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default
        criteria.setCostAllowed(false);
        provider = locationManager.getBestProvider(criteria, false);

        //Location location = locationManager.getLastKnownLocation(provider);
        mylistener = new MyLocationListener();
        /*if (location != null) {
            mylistener.onLocationChanged(location);
        } else {
            // leads to the settings because there is no last known location
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }*/
        // location updates: at least 1 meter and 200millsecs change
        locationManager.requestLocationUpdates(provider, 2000, 15, mylistener);

        //Add proximity alerts
        for(Map.Entry<Integer, Monument> entry : data.getMonuments().entrySet()){
            Intent temp_intent= new Intent(ACTION_FILTER);
            temp_intent.putExtra("name", entry.getValue().getName());
            temp_intent.putExtra("id", entry.getValue().getId());
            PendingIntent temp_pi = PendingIntent.getBroadcast(getApplicationContext(), entry.getKey(), temp_intent, PendingIntent.FLAG_CANCEL_CURRENT);
            locationManager.addProximityAlert(entry.getValue().getLatitude(), entry.getValue().getLongitude(), radius, -1, temp_pi);
            //data.getMonumentsWithproximityAlerts().add(entry.getValue());
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

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            //Toast.makeText(MainActivity.this, "Changement de position avec précision de "+location.getAccuracy(),
            //				Toast.LENGTH_SHORT).show();
            if(instantMarker == null){
                instantMarker = new Marker(map);
            }
            else{
                map.getOverlays().remove(instantMarker);
            }

            GeoPoint instantGeopoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            if(location.hasAccuracy()){
                if(location.getAccuracy() > 19){
                    if(receiverIsRegistered) {
                        unregisterReceiver(proximityReceiver);
                        receiverIsRegistered = false;
                    }
                }
                else{
                    registerReceiver(proximityReceiver, mIntentFilter);
                    receiverIsRegistered = true;
                }
                //else {
                //    if(proximityReceiver!=null){
                //        registerReceiver(proximityReceiver, mIntentFilter);
                //    }
                //}
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
            Toast.makeText(MainActivity.this, provider + "'s status changed to "+status +"!",
                    Toast.LENGTH_SHORT).show();
            if(status!= LocationProvider.AVAILABLE){
                unregisterReceiver(proximityReceiver);
            }
            else {
                if(proximityReceiver!=null){
                    registerReceiver(proximityReceiver, mIntentFilter);
                }
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MainActivity.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MainActivity.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();
            unregisterReceiver(proximityReceiver);
        }
    }

}


