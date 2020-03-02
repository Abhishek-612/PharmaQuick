package com.example.pharmaquick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.android.SphericalUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng current;
    private double latitude,longitude;

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    private Circle mCircle;
    private Marker mMarker;
    Timer timer;
    final Handler handler = new Handler();
    double radiusInMeters;
    CircleOptions circleOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        Places.initialize(getApplicationContext(), "AIzaSyDe9CUiOhE5rFrlcZNvd34Iu8T7dLa1diE");
//        PlacesClient placesClient = Places.createClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(current).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
    }


    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }


        @SuppressLint("MissingPermission")
        private void getLastLocation() {
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    mFusedLocationClient.getLastLocation().addOnCompleteListener(
                            new OnCompleteListener<Location>() {
                                @Override
                                public void onComplete(@NonNull Task<Location> task) {
                                    Location location = task.getResult();
                                    if (location == null) {
                                        requestNewLocationData();
                                    } else {
                                        current = new LatLng(location.getLatitude(), location.getLongitude());

                                        updateMap(current);
                                    }
                                }
                            }
                    );
                } else {
                    Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            } else {
                requestPermissions();
            }
        }

            @SuppressLint("MissingPermission")
            private void requestNewLocationData(){

                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(0);
                mLocationRequest.setFastestInterval(0);
                mLocationRequest.setNumUpdates(1);

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationClient.requestLocationUpdates(
                        mLocationRequest, mLocationCallback,
                        Looper.myLooper()
                );

            }

            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location mLastLocation = locationResult.getLastLocation();
                    current = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                    updateMap(current);
                }
            };

    private void updateMap(LatLng current){
        mMap.addMarker(new MarkerOptions().position(current).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 16.0f));
        drawMarkerWithCircle(current);
        Double dis = SphericalUtil.computeDistanceBetween(current,new LatLng(0,0));
//        Toast.makeText(MapsActivity.this, dis.toString(), Toast.LENGTH_SHORT).show();

    }

    private void drawMarkerWithCircle(LatLng position){
        radiusInMeters = 500.0;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = mMap.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(position);
        mMarker = mMap.addMarker(markerOptions);


        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                radiusInMeters+=250.0;
                circleOptions.radius(radiusInMeters);
                Log.i("time","reach");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMap.clear();
                        mCircle = mMap.addCircle(circleOptions);
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 10000);


    }


}


