package com.vivek.tripanalyzer.models;

import android.database.sqlite.SQLiteOpenHelper;

public class Trips {

    public Trips() {
    }

    public Trips(String trip_key) {
        this.trip_key = trip_key;
    }


    private String trip_key;
    private String trip_name;
    private int id;
    private String memberName;
    private int memberId;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrip_key() {
        return trip_key;
    }

    public void setTrip_key(String trip_key) {
        this.trip_key = trip_key;
    }

    public String getTrip_name() {
        return trip_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
}
