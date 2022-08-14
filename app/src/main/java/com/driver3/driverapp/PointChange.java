package com.driver3.driverapp;

public class PointChange {

    private String pointsAdded = "";
    private String reason = "";
    private String date = "";
    public PointChange(String pointsAdded, String reason, String date){
        this.pointsAdded = pointsAdded;
        this.reason = reason;
        this.date = date;
    }

    public String getPointsAdded(){
        return pointsAdded;
    }

    public String getReason(){
        return reason;
    }

    public String getDate(){
        return date;
    }
}
