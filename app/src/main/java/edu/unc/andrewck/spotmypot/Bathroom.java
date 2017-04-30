package edu.unc.andrewck.spotmypot;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by jleo128 on 4/26/17.
 */

public class Bathroom implements Serializable {

    private String name;
    private double longitude, latitude;
    private String building;
    private int floor;
    private String gender;
    private int stars;
    private String review;

    public Bathroom()
    {

    }

    public Bathroom(String name, double latitude, double longitude, String building, int floor, String gender,
                        int stars, String review)
    {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
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
        return new LatLng(latitude,longitude);
    }

    public boolean equals(Bathroom b){
        if (this.name.equals(b.getName()) && this.longitude == b.getLocation().longitude &&
                this.latitude == b.getLocation().latitude && this.building.equals(b.getBuilding()) &&
                this.floor == b.getFloor() && this.gender.equals(b.getGender()) &&
                this.stars == b.getStars() && this.review.equals(b.getReview()))
            return true;
        else
            return false;
    }
}
