package edu.unc.andrewck.spotmypot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener

{
    private GoogleApiClient c = null;
    private LocationRequest req;
    //private Geocoder g = new Geocoder(this, Locale.getDefault());
    private Location currentlocation;
    private MapFragment mapFragment;
    private GoogleMap map;
    private Marker locationMarker;
    private Marker geoMarker = null;
    private boolean moved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        createLocationRequest();
        buildGoogleApiClient();
        Button zoom = (Button) findViewById(R.id.zoom);
        zoom.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                zoom();
            }
       });
        Button backToMain = (Button) findViewById(R.id.back);
        backToMain.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toMain(v);
            }
        });
        Button list = (Button) findViewById(R.id.list);
        list.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toList(v);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    protected synchronized void buildGoogleApiClient() {
        if (c == null) {
            c = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        c.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        c.disconnect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
        map.setOnMapLongClickListener(this);
        addBathrooms();
    }

    @Override
    public void onConnectionSuspended(int i) {
        stopLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (c.isConnected()) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                c, req, this);
    }

    private void createLocationRequest(){
        req = new LocationRequest();
        req.setInterval(5000);
        req.setFastestInterval(1000);
        req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentlocation = location;
        updateUI();
    }

    protected void updateUI(){
        if (currentlocation != null)
        {
            LatLng latlng = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
            markerLocation(latlng);
        }
    }
    protected void stopLocationUpdates() {
        if (c != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    c, this);
        }
    }

    // Create a Location Marker
    private void markerLocation(LatLng latlng) {
        if (!moved) {
            zoom();
            moved = true;
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latlng)
                .title("Current Location")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        if (map != null) {
            // Remove the anterior marker
            if (locationMarker != null)
                locationMarker.remove();
            locationMarker = map.addMarker(markerOptions);
        }
    }


    private void addBathrooms() {
        List<Bathroom> reviewList;
        map.setOnMarkerClickListener(this);
        try {
            try {
                    reviewList = (List<Bathroom>) InternalStorage.readObject(this, "list");
            } catch (ClassNotFoundException e) {
                reviewList = new ArrayList<Bathroom>();
            }
            for (Bathroom b: reviewList) {
                String title = b.getName();
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(b.getLocation())
                        .title(title)
                        .snippet(title);
                if (b.getGender().equals("Men's")) {
                    markerOptions.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                } else if (b.getGender().equals("Women's")){
                    markerOptions.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                }
                else
                {
                    markerOptions.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                }
                    if (map != null) {
                        geoMarker = map.addMarker(markerOptions);
                    }
            }
        } catch(IOException e) { }

    }

    private void addBathroom(Location loc){
        Intent i = new Intent(this, ReviewEntryActivity.class);
        i.putExtra("Location", loc);
        startActivity(i);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        List<Bathroom> reviewList = null;
        if (marker.getTitle().equals("Current Location")) {
            Intent i = new Intent(this, ReviewEntryActivity.class);
            i.putExtra("Location", currentlocation);
            startActivity(i);
        }
        else {
            try {
                try {
                    reviewList = (List<Bathroom>) InternalStorage.readObject(this, "list");
                    }
                catch (ClassNotFoundException e) {}
                } catch (IOException e) {}
            }

        Bathroom result = null;
        if (reviewList != null) {
            for (Bathroom b: reviewList) {
                if (b.getName().equals(marker.getTitle()))
                {
                    result = b;
                }
            }
        }
        if (result == null)
            return false;
        else
        {
            Intent i = new Intent(this, SingleReview.class);
            i.putExtra("review", result);
            startActivity(i);
        }
        return true;
    }

    protected void toMain(View v)
    {
        Intent i = new Intent(this, MainMenu.class);
        startActivity(i);
    }

    protected void toList(View v)
    {
        Intent i = new Intent(this, ListView.class);
        startActivity(i);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Location loc = new Location("");
        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);
        addBathroom(loc);
    }

    public void zoom(){
        float zoom = 17f;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude()), zoom);
        map.animateCamera(cameraUpdate);
    }
}
