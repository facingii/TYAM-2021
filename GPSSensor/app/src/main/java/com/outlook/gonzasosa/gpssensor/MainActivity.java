package com.outlook.gonzasosa.gpssensor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    LocationListener locationListener;
    LocationManager locationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout layout = new FrameLayout (getBaseContext ());
        setContentView (layout);

    }

    @Override
    protected void onResume () {
        super.onResume ();

        int permission = getBaseContext().checkSelfPermission (Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions (
                    new String [] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION },
                    1001
            );

            return;
        }

        beginRequestLocation ();
    }

    @SuppressLint("MissingPermission")
    private void beginRequestLocation () {
        locationListener = new MyLocationListener ();
        locationManager = (LocationManager) getSystemService (LOCATION_SERVICE);
        locationManager.requestLocationUpdates (LocationManager.GPS_PROVIDER,
                5000,
                10,
                locationListener);
    }

    @Override
    protected void onPause () {
        super.onPause ();
        locationManager.removeUpdates (locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) {
            if (grantResults [0] == PackageManager.PERMISSION_GRANTED) {
                beginRequestLocation ();
            }
        }
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            Toast.makeText (getBaseContext (),
                    "LocatinChanged: Lat " + location.getLatitude() + " Log: " + location.getLongitude (),
                    Toast.LENGTH_LONG).show ();

            Geocoder geocoder = new Geocoder (getBaseContext(), Locale.getDefault ());
            List<Address> addresses;
            String city = "";

            try {
                addresses = geocoder.getFromLocation (location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size () > 0) {
                    //Log.i ("GEO", addresses.get (0).getLocality ());
                    city = addresses.get (0).getLocality ();
                }
            } catch (IOException ex) {
                ex.printStackTrace ();
            }

            Log.i ("GEO", "City found: " + city);
        }

        @Override
        public void onLocationChanged(@NonNull List<Location> locations) {

        }

        @Override
        public void onFlushComplete(int requestCode) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }
}
