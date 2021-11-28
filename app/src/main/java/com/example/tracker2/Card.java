package com.example.tracker2;

public class Card {
    private String locationName;
    private double latitude;
    private double longitude;
    private String time;


    public Card(String locationName, double latitude, double longitude, String time) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
