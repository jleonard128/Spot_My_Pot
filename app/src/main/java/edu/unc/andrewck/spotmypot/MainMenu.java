package edu.unc.andrewck.spotmypot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    protected void toList(View v) {

        Intent i = new Intent(this, ListView.class);
        startActivity(i);

    }

    protected void toMap(View v) {
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);

    }

    protected void toEntry(View v) {
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);
    }
}
