package edu.unc.andrewck.spotmypot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;


public class MapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback

{
    private GoogleApiClient c = null;
    private LocationRequest req;
    //private Geocoder g = new Geocoder(this, Locale.getDefault());
    private Location currentlocation;
    private MapFragment mapFragment;
    private GoogleMap map;
    private Marker locationMarker;
    private Marker geoMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        createLocationRequest();
        buildGoogleApiClient();
        Button addBathroom = (Button) findViewById(R.id.add);
        addBathroom.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                addBathroom();
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
        String title = latlng.latitude + ", " + latlng.longitude;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latlng)
                .title(title);
        if (map != null) {
            // Remove the anterior marker
            if (locationMarker != null)
                locationMarker.remove();
            locationMarker = map.addMarker(markerOptions);
        }
        float zoom = 17f;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, zoom);
        map.animateCamera(cameraUpdate);
    }


    private void addGeoLocation(LatLng latlng) {
        String title = latlng.latitude + ", " + latlng.longitude;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latlng)
                .title(title);
        if (map != null) {
            if (geoMarker != null)
            {
                geoMarker.remove();
                geoMarker = null;
            }
            geoMarker = map.addMarker(markerOptions);
        }
    }

    private void addBathroom(){
        Intent i = new Intent(this, ReviewEntryActivity.class);
        i.putExtra("Location", currentlocation);
        startActivity(i);
    }
}
