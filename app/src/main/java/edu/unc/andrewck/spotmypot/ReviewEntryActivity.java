package edu.unc.andrewck.spotmypot;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReviewEntryActivity extends AppCompatActivity {

    private Location l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_entry);

        //Get location from Intent
        l = (Location) getIntent().getParcelableExtra("Location");

        //Set up bathroom type choice spinner
        Spinner s = (Spinner) findViewById(R.id.typeSpin);
        ArrayAdapter<CharSequence> a = ArrayAdapter.createFromResource(this,
                R.array.types_array, android.R.layout.simple_spinner_item);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(a);

        s = (Spinner) findViewById(R.id.starSpin);
        a = ArrayAdapter.createFromResource(this, R.array.stars_array,
                android.R.layout.simple_spinner_item);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(a);
    }

    protected void submitReview(View v) {
        //Make bathroom object
        Bathroom b;
        try {
            b = new Bathroom(((EditText) findViewById(R.id.name)).getText().toString(),
                    l.getLatitude(),
                    l.getLongitude(),
                    ((EditText) findViewById(R.id.building)).getText().toString(),
                    Integer.parseInt(((EditText) findViewById(R.id.floor)).getText().toString()),
                    ((Spinner) findViewById(R.id.typeSpin)).getSelectedItem().toString(),
                    ((Spinner) findViewById(R.id.starSpin)).getSelectedItemPosition() - 1,
                    ((EditText) findViewById(R.id.review)).getText().toString());
        } catch(NumberFormatException e) {
            Toast.makeText(this, "Invalid input.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Put it into the array that's stored in data
        List<Bathroom> reviewList = null;
        try {
            try {
                reviewList = (List<Bathroom>) InternalStorage.readObject(this, "list");
            } catch (ClassNotFoundException e) {
            }
        } catch(IOException e) {
            reviewList = new ArrayList<Bathroom>();
        }
        if (reviewList != null) {
            reviewList.add(b);
        }

        try {
            InternalStorage.writeObject(this, "list", reviewList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        returnToMain(v);
    }

    protected void returnToMain(View v) {
        Intent i = new Intent(this, MainMenu.class);
        startActivity(i);
    }
}
