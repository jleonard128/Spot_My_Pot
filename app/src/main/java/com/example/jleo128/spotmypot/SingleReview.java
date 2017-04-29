package edu.unc.andrewck.spotmypot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SingleReview extends AppCompatActivity implements OnMapReadyCallback {

    private Bathroom b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_review);

        b = (Bathroom) getIntent().getSerializableExtra("review");

        ((TextView) findViewById(R.id.srName)).setText(b.getName() + " (" + b.getGender() + ")");
        ((TextView) findViewById(R.id.srBuilding)).setText(b.getBuilding() + ", Floor " +
                b.getFloor());
        ((TextView) findViewById(R.id.srStars)).setText(b.getStars() + " Stars");
        ((TextView) findViewById(R.id.srReview)).setText(b.getReview());

        ((MapView) findViewById(R.id.srMap)).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(b.getLocation()));
        googleMap.addMarker(new MarkerOptions().position(b.getLocation()));
    }

    protected void srReturn(View v) {
        finish();
    }

    protected void deleteReview(View v) {
        //Put it into the array that's stored in data
        List<Bathroom> reviewList;
        try {
            try {
                reviewList = (List<Bathroom>) InternalStorage.readObject(this, "list");
            } catch (ClassNotFoundException e) {
                reviewList = new ArrayList<Bathroom>();
            }

            reviewList.remove(b);

            InternalStorage.writeObject(this, "list", reviewList);
        } catch(IOException e) { }

        Intent i = new Intent(this, MainMenu.class);
        startActivity(i);
    }
}
