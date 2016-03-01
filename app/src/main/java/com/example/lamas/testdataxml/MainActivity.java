package com.example.lamas.testdataxml;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
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
    private float radius=100;
    private LocationManager locationManager;
    private MyLocationListener mylistener;
    private String provider;
    private Criteria criteria;
    private Marker instantMarker;
    String ACTION_FILTER = "com.example.PROXIMITY_ALERT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Introduction
        super.onCreate(savedInstanceState);

        //i'm registering my Receiver First
        registerReceiver(new ProximityReceiver(), new IntentFilter(ACTION_FILTER));

        //configuration of the map
        setContentView(R.layout.activity_main);
        map = (MapView) findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(18);

        //Add data
        Data data = Data.getInstance(getApplicationContext());
        for(Map.Entry<Integer, Monument> entry : data.getMonuments().entrySet()){
            Marker temp = new Marker(map);
            GeoPoint geo = new GeoPoint(entry.getValue().getLatitude(),entry.getValue().getLongitude());
            temp.setPosition(geo);
            mapController.setCenter(geo);
            temp.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            temp.setTitle(entry.getValue().getName());
            map.getOverlays().add(temp);

        }

        //Enabled GPS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default
        criteria.setCostAllowed(false);
        provider = locationManager.getBestProvider(criteria, false);

        Location location = locationManager.getLastKnownLocation(provider);
        mylistener = new MyLocationListener();
        if (location != null) {
            mylistener.onLocationChanged(location);
        } else {
            // leads to the settings because there is no last known location
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        // location updates: at least 1 meter and 200millsecs change
        locationManager.requestLocationUpdates(provider, 200, 1, mylistener);

        for(Map.Entry<Integer, Monument> entry : data.getMonuments().entrySet()){
            Intent temp_intent= new Intent(ACTION_FILTER);
            temp_intent.putExtra("name", entry.getValue().getName());
            temp_intent.putExtra("id", entry.getValue().getId());
            PendingIntent temp_pi = PendingIntent.getBroadcast(getApplicationContext(), entry.getKey(), temp_intent, 0);
            locationManager.addProximityAlert(entry.getValue().getLatitude(), entry.getValue().getLongitude(), radius, -1, temp_pi);

        }
    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(MainActivity.this, "Changement de position! "+
            				location.getLatitude()+" "+location.getLongitude(),
            				Toast.LENGTH_SHORT).show();
            if(instantMarker == null){
                instantMarker = new Marker(map);
            }
            else{
                map.getOverlays().remove(instantMarker);
            }
            // Initialize the location fields
            GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            instantMarker.setPosition(startPoint);
            instantMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            instantMarker.setTitle("I'm here!");
            map.getOverlays().add(instantMarker);
            map.invalidate();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(MainActivity.this, provider + "'s status changed to "+status +"!",
                    Toast.LENGTH_SHORT).show();
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
        }
    }

}


