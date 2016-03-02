package com.example.lamas.testdataxml;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

/**
 * Created by Maxence on 16-02-09.
 */
public class ProximityReceiver extends BroadcastReceiver {
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Key for determining whether user is leaving or entering
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        String name = intent.getStringExtra("name");
        int id_poi = intent.getIntExtra("id", 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification = new Notification.Builder(context);
        notification.setSmallIcon(R.drawable.direction_arrow);
        notification.setContentTitle("POI à proximité !");
        notification.setContentText(name + " est à proximité!");
        notification.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        //Gives whether the user is entering or leaving in boolean form
        boolean state = intent.getBooleanExtra(key, false);

        if(state){
            // Call the Notification Service or anything else that you would like to do here
            Intent next_activity = new Intent(context, POIDetails.class);
            next_activity.putExtra("id", id_poi);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, next_activity, 0);
            notification.setContentIntent(pendingIntent);
            notificationManager.notify(1, notification.build());
            context.startActivity(next_activity);
            //notificationManager.cancel(1);
            Toast.makeText(context, "Welcome to my Area: "+name, Toast.LENGTH_SHORT).show();
        }else{
            //Other custom Notification
            Toast.makeText(context, "Thank you for visiting my Area,come back again !!", Toast.LENGTH_SHORT).show();
        }
    }


}
