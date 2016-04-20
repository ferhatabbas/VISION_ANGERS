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

import com.example.lamas.testdataxml.list_activities.InformationActivity;

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
        int id_poi = intent.getIntExtra("id", -1 );
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
            Intent next_activity = new Intent(context, InformationActivity.class);
            next_activity.putExtra("id", id_poi);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, next_activity, 0);
            notification.setContentIntent(pendingIntent);
            notificationManager.notify(Constants.POI_NOTIFICATION_ID, notification.build());
            context.startActivity(next_activity);

        }
    }

}

