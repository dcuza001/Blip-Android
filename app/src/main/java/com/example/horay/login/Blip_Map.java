package com.example.horay.login;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


class Blip {

    public String owner;
    public String comment;
    public double latitude;
    public double longitude;
    public String date;
    public String group = "default";
    public String type = "default";

    public Blip(String username, double latitude, double longitude, String comment){
        this.owner = "ryocsaito@gmail.com";
        this.latitude = latitude;
        this.longitude = longitude;

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        this.date = (dateFormat.format(date));
        this.comment = comment;
    }
}

public class Blip_Map extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Circle searchCircle;
    private int radiusValue = 100;

    FloatingActionButton pinButton;
    Location location;

    Firebase ref = new Firebase("https://blipster.firebaseio.com/");

    private Map<String,Marker> markers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pinButton = (FloatingActionButton) findViewById(R.id.addPin);
        Firebase.setAndroidContext(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //options to update quicker
        Criteria criteria = new Criteria();
        //update speed
        String bestProvider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(bestProvider);


        LatLng latLngCenter = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(latLngCenter , 16) );
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        searchCircle = this.mMap.addCircle(new CircleOptions().center(latLngCenter).radius(radiusValue));
        searchCircle.setCenter(latLngCenter);
        searchCircle.setFillColor(Color.argb(66, 255, 0, 255));
        searchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,1, (LocationListener) this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, (LocationListener) this);
        this.markers = new HashMap<String, Marker>();
    }

    public void addMarker(View view) {
        onLocationChanged(location);
        Firebase userRef = ref.child("blips");
        Blip newMarker = new Blip("ryocsaito@gmail.com",location.getLatitude(), location.getLongitude(), "hiii" );
        userRef.push().setValue(newMarker);
    }

    private void findMarkers(){

    }
    public void loadMarkers(View view){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng center = new LatLng(latitude, longitude);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        this.searchCircle.setCenter(center);
        this.searchCircle.setRadius(radiusValue);
    }


//
//    @Override
//    public void onLocationChanged(Location location) {
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//        LatLng center = new LatLng(latitude, longitude);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
//        this.searchCircle.setCenter(center);
//        this.searchCircle.setRadius(radiusValue);
//    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}


