package com.example.horay.login;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Blip_Map extends FragmentActivity implements OnMapReadyCallback, LocationListener, OnItemSelectedListener
, GoogleMap.OnMarkerClickListener, NavigationView.OnNavigationItemSelectedListener{

    public static Activity blipMapActivity;

    private static final int CONTENT_VIEW_ID = 10101010;
    private GoogleMap mMap;
    private Circle searchCircle;
    private int radiusValue = 600;

    FloatingActionButton pinButton;
    Location location;

    DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://blipster.firebaseio.com/");

    public Map<String,Blip> markerMap;
    List<String> categories;

    DrawerLayout mDrawerLayout;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    TextView emailNavHeader;

    //multi
    MultiSelectionSpinner spinner1;

    public Blip blipToSend;

    public Blip getBlipObject(){
        return blipToSend;
    }

    private void makeList(){
        // Spinner Drop down elements
        categories = new ArrayList<String>();
        //Add categories here
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Friends");
        categories.add("Personal");
        categories.add("Travel");
        categories.add("ryocsaito@gmail.com");
    }

    private void makeMarker(LatLng pos, Blip b){
        Marker m = mMap.addMarker(new MarkerOptions().position(pos));
        markerMap.put(m.getId(), b);
    }

    private void makeMultiSpinner(){
        //Multi Spinner
        spinner1 = (MultiSelectionSpinner) findViewById(R.id.mySpinner1);
        assert spinner1 != null;
        spinner1.setItems(categories);
        ImageButton bt = (ImageButton) findViewById(R.id.getSelected);
        assert bt != null;
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = spinner1.getSelectedItemsAsString();
                Log.e("getSelected", s);
                List<String> tags = spinner1.getSelectedStrings();
                findMarkers(tags);

            }
        });
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        markerMap = new HashMap<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //pinButton = (FloatingActionButton) findViewById(R.id.addPin);

        // Find our drawer view
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        makeList();
        makeMultiSpinner();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        blipMapActivity = this;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            // Name, email address, and profile photo Url
            //String name = user.getDisplayName();
            /*String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();

            emailNavHeader = (TextView) findViewById(R.id.emailNavHeader);
            emailNavHeader.setText(email);*/
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //created a separate function for searching tags
    private void findMarkers(final List<String> tags) {


        mMap.clear();
        markerMap.clear();

        LatLng latLngCenter = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, 16));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        searchCircle = this.mMap.addCircle(new CircleOptions().center(latLngCenter).radius(radiusValue));
        searchCircle.setCenter(latLngCenter);

        searchCircle.setStrokeColor(Color.argb(100, 1, 83, 47));
        searchCircle.setFillColor(Color.argb(100, 105, 190, 40));
        Toast.makeText(getApplicationContext(), "Making markers", Toast.LENGTH_SHORT).show();

        ref.child("blips_ryota").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usersSnapshot) {

                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    Blip b = userSnapshot.getValue(Blip.class);
                    LatLng pos = new LatLng(b.x, b.y);
                    if (insideCircle(pos, searchCircle)) {

                        if (tags.size() > 0) {
                            for (int i = 0; i < tags.size(); i++) {
                                String tag = tags.get(i);
                                String name = b.tag;
                                if (tag.equals(name)) {
                                    makeMarker(pos, b);
                                }
                            }
                        }
                        else {
                            //Toast.makeText(getApplicationContext(), "Making markers", Toast.LENGTH_SHORT).show();
                            makeMarker(pos,b);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

//    private void findMarkersDefault() {
//        ref.child("blips_ryota").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot usersSnapshot) {
//                markers.clear();
//
//                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
//                    Blip b = userSnapshot.getValue(Blip.class);
//                    LatLng pos = new LatLng(b.x, b.y);
//                    if( insideCircle(pos, searchCircle)) {
//                        Marker m = mMap.addMarker(new MarkerOptions()
//                                .position(pos)
//                                .title(b.owner));
//                        //markers.put(b.owner, m);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//            /*
//            @Override
//            public void onCancelled(FirebaseError firebaseError) { }
//            */
//        });
//    }

    private void loadMarkers(){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng center = new LatLng(latitude, longitude);
        this.searchCircle.setCenter(center);
        this.searchCircle.setRadius(radiusValue);
        findMarkers(spinner1.getSelectedStrings());
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
        searchCircle.setStrokeColor(Color.argb(100, 1, 83, 47));
        searchCircle.setFillColor(Color.argb(100, 105, 190, 40));

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,1, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, this);

        mMap.setOnMarkerClickListener( this);

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

    public void addMarker(View view) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();


        Intent intent = new Intent(getApplicationContext(), AddBlip.class);
        intent.putExtra("Lat", latitude);
        intent.putExtra("Long", longitude);
        startActivity(intent);
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

    @Override
    public boolean onMarkerClick(Marker marker) {

        blipToSend = markerMap.get(marker.getId());
        Toast.makeText(this, blipToSend.owner, Toast.LENGTH_SHORT ).show();
        Bundle bundle = new Bundle();
        bundle.putSerializable("BlipObj", blipToSend);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        ViewBlipDialog fragment = new ViewBlipDialog();
        fragment.setArguments(bundle);
        fragment.show(fm, "fragment_edit_name");
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_groups) {
            // Handle the groups action
            /*fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new GroupsFragment())
                    .commit();*/
        } else if (id == R.id.nav_settings) {
            /*fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new SettingsFragment())
                    .commit();*/

        } else if (id == R.id.nav_logout) {
            /*fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new LogoutFragment())
                    .commit();*/
            FirebaseAuth.getInstance().signOut();
            finish();


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
        //test comment
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(Gravity.LEFT);

        } else {
            super.onBackPressed();
        }
    }

}


