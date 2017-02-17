package com.example.deepak.hpphonelostproject;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created on 16/2/17.
 */


public class AppController extends Application {
    private GoogleApiClient mGoogleApiClient;
    private static AppController mAppController;

    public static AppController getInstance() {
        if (mAppController == null) {
            mAppController = new AppController();
        }
        return mAppController;
    }

    public GoogleApiClient getGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.instance())
                    .addConnectionCallbacks(MainActivity.instance())
                    .addOnConnectionFailedListener(MainActivity.instance())
                    .addApi(LocationServices.API)
                    .build();
        }
        return mGoogleApiClient;
    }
}
