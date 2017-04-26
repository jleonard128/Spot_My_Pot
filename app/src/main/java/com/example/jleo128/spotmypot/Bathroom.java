package com.example.jleo128.spotmypot;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jleo128 on 4/26/17.
 */

public class Bathroom {

    private String name;
    private LatLng location;
    private String gender;
    private int stars;
    private String review;

    public Bathroom()
    {

    }

    public Bathroom(String name, LatLng location, String gender, int stars, String review)
    {
        this.name = name;
        this.location = location;
        this.gender = gender;
        this.stars = stars;
        this.review = review;
    }

    public String getName(){
        return name;
    }

    public String getGender(){
        return gender;
    }

    public int getStars(){
        return stars;
    }

    public String getReview(){
        return review;
    }

    public LatLng getLocation(){
        return location;
    }
}
