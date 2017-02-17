package com.example.deepak.hpphonelostproject;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created on 2/2/17.
 */

public class SmsBroadCastReceiver extends BroadcastReceiver {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1;
    String smsBody = "";
    String address = "";
    public static final String SMS_BUNDLE = "pdus";
    private Storage s;

    @Override
    public void onReceive(Context context, Intent intent) {
        s = new Storage(context);
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                smsBody = smsMessage.getMessageBody().toString();
                address = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
            }
            Toast.makeText(context, smsMessageStr + "In receiver", Toast.LENGTH_SHORT).show();

            onMessageReceived(address, smsBody, context);
        }
    }

    public void onMessageReceived(String address, String smsBody, Context context) {
//        ArrayList<String> str = s.fetchContacts();
//        str.add(0, "+91" + str.get(0));
//        str.add(1, "+91" + str.get(1));
//        if (address.equals(str.get(0)) || address.equals(str.get(1))) {
//            if (smsBody.toString().equals(R.string.msg1)) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            turnOnFlashLight(context);
//        }
        turnToGeneral(context);
        if (checkPlayServices())
            AppController.getInstance().getGoogleApiClient().connect();
//            } else if (smsBody.toString().equals(R.string.msg2)) {

//            }
//        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.instance());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity.instance(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(MainActivity.instance(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                MainActivity.instance().finish();
            }
            return false;
        }
        return true;
    }

    private void turnToGeneral(Context context) {
        AudioManager audMangr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audMangr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        playAlarm(context);
    }

    private void playAlarm(Context context) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void turnOnFlashLight(Context context) {
        try {
            if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                CameraManager camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                String cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    camManager.setTorchMode(cameraId, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Exception throws in turning on flashlight.", Toast.LENGTH_SHORT).show();
        }
    }
}
