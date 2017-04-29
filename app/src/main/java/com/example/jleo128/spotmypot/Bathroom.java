package edu.unc.andrewck.spotmypot;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by jleo128 on 4/26/17.
 */

public class Bathroom implements Serializable {

    private String name;
    private LatLng location;
    private String building;
    private int floor;
    private String gender;
    private int stars;
    private String review;

    public Bathroom()
    {

    }

    public Bathroom(String name, LatLng location, String building, int floor, String gender,
                        int stars, String review)
    {
        this.name = name;
        this.location = location;
        this.building = building;
        this.floor = floor;
        this.gender = gender;
        this.stars = stars;
        this.review = review;
    }

    public String getName(){
        return name;
    }

    public String getBuilding() { return building; }

    public int getFloor() { return floor; }

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
