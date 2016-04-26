package com.example.lamas.testdataxml;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.example.lamas.testdataxml.list_activities.InformationActivity;

/**
 * Created by Maxence on 16-02-09.
 */
public class ProximityReceiver extends BroadcastReceiver {
    private Intent next_activity = new Intent();

    @Override
    public void onReceive(Context context, Intent intent) {

        //Gives whether the user is entering or leaving in boolean form
        boolean entering = intent.getBooleanExtra(
                LocationManager.KEY_PROXIMITY_ENTERING, false);
        int id_poi = intent.getIntExtra("id", -1);

        if(entering && id_poi != -1){
            next_activity.setClass(context, InformationActivity.class);
            next_activity.putExtra("id", id_poi);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            sendNotification(intent.getStringExtra("name"), context, pendingIntent);
            context.startActivity(next_activity);

        }
    }

    private void sendNotification(String name, Context context, PendingIntent pendingIntent){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification = new Notification.Builder(context);
        notification.setSmallIcon(R.drawable.direction_arrow);
        notification.setContentTitle(name + " est à proximité!");
        notification.setContentText(name + " est à proximité!");
        notification.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        notification.setContentIntent(pendingIntent);
        notificationManager.notify(Constants.POI_NOTIFICATION_ID, notification.build());
    }

}

