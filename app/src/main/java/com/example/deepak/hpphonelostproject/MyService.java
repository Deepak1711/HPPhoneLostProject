package com.example.deepak.hpphonelostproject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created on 3/2/17.
 */


public class MyService extends Service {

    private IntentFilter intentFilter;
    Camera cam = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        smsBroadcastReceiver = new SmsServiceBroadcastReceiver();
//        intentFilter = new IntentFilter();
//        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
//        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String smsBody = intent.getStringExtra("smsBody");
            String address = intent.getStringExtra("address");
            setNotification(smsBody, address);
        }
        return START_REDELIVER_INTENT;
    }

    private void setNotification(String address, String smsBody) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MainActivity.instance())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(address)
                        .setContentText(smsBody);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(MainActivity.instance(), MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.instance());
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) MainActivity.instance().getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        int mId = 0;
        mNotificationManager.notify(mId, mBuilder.build());
    }
}
