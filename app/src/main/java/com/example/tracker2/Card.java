package com.example.tracker2;

public class Card {
    private String locationName;
    private String time;

    public Card(String locationName, String time) {
        this.locationName = locationName;
        this.time = time;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
