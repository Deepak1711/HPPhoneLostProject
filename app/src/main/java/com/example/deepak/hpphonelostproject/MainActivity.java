package com.example.deepak.hpphonelostproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_ACCESS_LOCATION = 2;
    private static final int PERMISSION_RECEIVE_SMS = 1;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 3;
    EditText textContact1, textContact2;
    Button btnRegister;
    private final Storage s;
    Camera cam = null;
    private static MainActivity inst = null;
    private TextView longi;
    private TextView lat;
    private Location mLastLocation;
//    private Button btnClick;

    public MainActivity() {
        inst = this;
        s = new Storage(MainActivity.this);
    }

    public static MainActivity instance() {
        return inst;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        initViews();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textContact1.getText().equals("") && !textContact2.getText().equals("")) {
                    ArrayList<String> str = new ArrayList<>();
                    str.add(textContact1.getText().toString());
                    str.add(textContact2.getText().toString());
                    if (s.insert(str)) {
                        Toast.makeText(MainActivity.this, R.string.contactRegistered, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
//        btnClick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkPlayServices())
//                    AppController.getInstance().getGoogleApiClient().connect();
//            }
//        });
    }

//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.instance());
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity.instance(),
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Toast.makeText(MainActivity.instance(),
//                        "This device is not supported.", Toast.LENGTH_LONG)
//                        .show();
//                MainActivity.instance().finish();
//            }
//            return false;
//        }
//        return true;
//    }

    private void checkPermissions() {
        int permissionCheckReceive = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if (permissionCheckReceive != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.CAMERA}, PERMISSION_RECEIVE_SMS);
        }
    }

    private void initViews() {
        textContact1 = (EditText) findViewById(R.id.contact1);
        textContact2 = (EditText) findViewById(R.id.contact2);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        longi = (TextView) findViewById(R.id.longitude);
        lat = (TextView) findViewById(R.id.latitude);
//        btnClick = (Button) findViewById(R.id.btnClick);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressWarnings({"MissingPermission"})
    public void setCordinates() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(AppController.getInstance().getGoogleApiClient());
        if (mLastLocation != null) {
            this.lat.setText(String.valueOf(mLastLocation.getLatitude()));
            this.longi.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    protected void onStop() {
        AppController.getInstance().getGoogleApiClient().disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.instance(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ACCESS_LOCATION);
        } else {
            setCordinates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_RECEIVE_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // permission denied, boo! Ask for the permissions again
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.CAMERA}, PERMISSION_RECEIVE_SMS);
                }
                return;
            }
            case PERMISSION_ACCESS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    setCordinates();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.instance(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ACCESS_LOCATION);
                }
                return;
            }
        }
    }
}
