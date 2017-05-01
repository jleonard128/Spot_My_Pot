package edu.unc.andrewck.spotmypot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListView extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Bathroom> rList;
    private LinearLayout l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Spinner s = (Spinner) findViewById(R.id.lvSort);
        ArrayAdapter<CharSequence> a = ArrayAdapter.createFromResource(this,
                R.array.sort_array, android.R.layout.simple_spinner_item);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(a);

        l = (LinearLayout) findViewById(R.id.lvList);

        try {
            rList = (ArrayList) InternalStorage.readObject(this, "list");
        } catch(IOException e) { }
        catch(ClassNotFoundException e) { }


        //l.removeAllViews();
        if(rList != null) for(Bathroom b : rList) addToList(l, b);
    }




    protected void sortList(View v) {
        if(rList ==  null) return;

        Spinner s = (Spinner) findViewById(R.id.lvSort);

        String sortBy = s.getSelectedItem().toString();

        switch(sortBy) {
            case "Name":
                Collections.sort(rList, new Comparator<Bathroom>() {
                    @Override
                    public int compare(Bathroom b2, Bathroom b1)
                    {
                        return  b2.getName().compareToIgnoreCase(b1.getName());
                    }
                });
                break;
            case "Building":
                Collections.sort(rList, new Comparator<Bathroom>() {
                    @Override
                    public int compare(Bathroom b2, Bathroom b1)
                    {
                        return  b2.getBuilding().compareToIgnoreCase(b1.getBuilding());
                    }
                });
                break;
            case "Gender":
                Collections.sort(rList, new Comparator<Bathroom>() {
                    @Override
                    public int compare(Bathroom b2, Bathroom b1)
                    {
                        return  b2.getGender().compareToIgnoreCase(b1.getGender());
                    }
                });
                break;
            case "Rating":
                Collections.sort(rList, new Comparator<Bathroom>() {
                    @Override
                    public int compare(Bathroom b2, Bathroom b1)
                    {
                        return  b1.getStars() - b2.getStars();
                    }
                });
                break;
            default:
        }

        l.removeAllViews();
        if(rList != null) for(Bathroom b : rList) addToList(l, b);
    }


    private void addToList(LinearLayout l, Bathroom b) {
        View r = new SmallReview(this, b);
        r.setOnClickListener(this);
        r.postInvalidate();
        l.addView(r, l.getWidth(), 200);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, SingleReview.class);
        i.putExtra("review", ((SmallReview) v).getReview());
        startActivity(i);
    }

    protected void toMap(View v) {
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);
    }

    protected void toMain(View v) {
        Intent i = new Intent(this, MainMenu.class);
        startActivity(i);
    }

    protected void loadReviews(View v) {
        l.removeAllViews();
        if(rList != null) for(Bathroom b : rList) addToList(l, b);
    }
}
