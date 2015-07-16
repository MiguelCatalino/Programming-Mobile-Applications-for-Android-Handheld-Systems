package com.example.miguelcatalino.lab_dailyselfie;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;


/**
 * Created by miguelcatalino on 7/14/15.
 */
public class Alarm extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 867;

    private PendingIntent getAlarmIntent(Context context) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0,
                new Intent(context, Alarm.class), 0);
        return pendingIntent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent nIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long[] vibratePatten = {0, 200, 200, 300};
        Notification.Builder notificacion = new Notification.Builder(context)
                .setTicker("Time for another selfie")
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setAutoCancel(true)
                .setContentTitle("Daily Selfie")
                .setContentText("Time for another selfie")
                .setContentIntent(pendingIntent)
                .setVibrate(vibratePatten);

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, notificacion.build());
    }

    public void setAlarm(Context context) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                1000 * 100,//set for 10 seconds
                getAlarmIntent(context));
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(getAlarmIntent(context));
    }
}
