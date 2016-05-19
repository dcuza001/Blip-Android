package com.example.horay.login;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
public class Blip_Map extends AppCompatActivity implements OnMapReadyCallback, LocationListener, OnItemSelectedListener {

    private static final int CONTENT_VIEW_ID = 10101010;
    private GoogleMap mMap;
    private Circle searchCircle;
    private int radiusValue = 600;

    FloatingActionButton pinButton;
    Location location;

    DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://blipster.firebaseio.com/");

    private Map<String,Marker> markers;

    DrawerLayout mDrawerLayout;

    //multi
    MultiSelectionSpinner spinner1;

    Bundle savedInstanceState;

    private void printPosition(LatLng loc){
        Toast.makeText(getApplicationContext(), "X: " + loc.latitude + " Y: " + loc.longitude, Toast.LENGTH_SHORT).show();
        Log.d("Location", "X: " + loc.latitude + " Y: " + loc.longitude );
    }

    private void setDrawer(){

    }

    private void decodeImage(){

    }

//    public void printLocation(Context context){
//        Toast.makeText(context, "X: " + x + " Y: " + y, Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pinButton = (FloatingActionButton) findViewById(R.id.addPin);
        markers = new HashMap<String, Marker>();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Spinner element
        //Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        //spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        //Add categories here
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Friends");
        categories.add("Personal");
        categories.add("Travel");
        categories.add("ryocsaito@gmail.com");

        // Creating adapter for spinner
        /*ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);*/


        //Multi Spinner
        spinner1 = (MultiSelectionSpinner) findViewById(R.id.mySpinner1);
        spinner1.setItems(categories);
        ImageButton bt = (ImageButton) findViewById(R.id.getSelected);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = spinner1.getSelectedItemsAsString();
                Log.e("getSelected", s);
                List<String> tags = spinner1.getSelectedStrings();
                if(tags.isEmpty()){
                    findMarkers();
                }
                else{
                    findMarkers(tags);
                }
            }
        });

    }


    public void addMarker(View view) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Intent intent = new Intent(getApplicationContext(), AddBlip.class);
        intent.putExtra("Lat", latitude);
        intent.putExtra("Long", longitude);
        startActivity(intent);


//        Firebase userRef = ref.child("blips");
//        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//        Blip newMarker = new Blip("ryocsaito@gmail.com", loc.latitude, loc.longitude , "hiii" );
//        userRef.push().setValue(newMarker);
    }

    private Boolean insideCircle(LatLng pos, Circle circle){
        float[] distance = new float[2];
        Location.distanceBetween(circle.getCenter().latitude, circle.getCenter().longitude,
                pos.latitude, pos.longitude, distance);

        if( distance[0] > circle.getRadius() ) {
            return false;
        }
        else{
            return true;
        }
    }

    //created a separate function for searching tags
    private void findMarkers(final List<String> tags) {


        mMap.clear();
        LatLng latLngCenter = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(latLngCenter , 16) );
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        searchCircle = this.mMap.addCircle(new CircleOptions().center(latLngCenter).radius(radiusValue));
        searchCircle.setCenter(latLngCenter);
        searchCircle.setFillColor(Color.argb(66, 255, 0, 255));
        searchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));

        ref.child("blips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usersSnapshot) {
                markers.clear();

                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    Blip b = userSnapshot.getValue(Blip.class);
                    LatLng pos = new LatLng(b.x, b.y);
                    if (insideCircle(pos, searchCircle)) {

                        //b.printLocation(getApplicationContext());
                        //the search function
                        if (tags.size() > 0) {
                            for (int i = 0; i < tags.size(); i++) {
                                String tag = tags.get(i);
                                String name = b.owner;
                                if (tag.equals(name)) {
                                    Marker m = mMap.addMarker(new MarkerOptions()
                                            .position(pos)
                                            .title(b.owner));
                                    markers.put(b.owner, m);
                                }
                            }

                        } else {
                            Marker m = mMap.addMarker(new MarkerOptions()
                                    .position(pos)
                                    .title(b.owner));
                            markers.put(b.owner, m);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


            //what is this?
            /*@Override
            public void onCancelled(FirebaseError firebaseError) {
            }*/
        });
    }

    private void findMarkers() {
        ref.child("blips_ryota").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usersSnapshot) {
                markers.clear();

                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    Blip b = userSnapshot.getValue(Blip.class);
                    LatLng pos = new LatLng(b.x, b.y);
                    if( insideCircle(pos, searchCircle)) {
                       // b.printLocation(getApplicationContext());
                        Marker m = mMap.addMarker(new MarkerOptions()
                                .position(pos)
                                .title(b.owner));
                        markers.put(b.owner, m);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            /*
            @Override
            public void onCancelled(FirebaseError firebaseError) { }
            */
        });
    }

    private void loadMarkers(){
        //markers = new HashMap<String, LatLng>();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng center = new LatLng(latitude, longitude);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        this.searchCircle.setCenter(center);
        this.searchCircle.setRadius(radiusValue);
        findMarkers();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
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


        loadMarkers();
    }
    public void loadButton(View view){
        loadMarkers();
    }

    public void centerCamera(View view){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        CameraUpdate center=  CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(16);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }

    public void openDrawer(View v){
        mDrawerLayout.openDrawer(Gravity.LEFT); //Edit Gravity.End need API 14

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

    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();


    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}


