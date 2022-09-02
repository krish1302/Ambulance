package com.example.ambulance_demo.entity;

public class AlertTable {
    private int alert_id;
    private int alert_user_id;
    private double alert_user_lat;
    private double alert_user_lng;
    private boolean alert_status;
    private int alert_picker_id;
    private double alert_picker_lat;
    private double getAlert_picker_lng;

    public AlertTable(int alert_user_id, double alert_user_lat, double alert_user_lng) {
        this.alert_user_id = alert_user_id;
        this.alert_user_lat = alert_user_lat;
        this.alert_user_lng = alert_user_lng;
    }

    public AlertTable(int alert_id) {
        this.alert_id = alert_id;
    }

    public int getAlert_id() {
        return alert_id;
    }

    public void setAlert_id(int alert_id) {
        this.alert_id = alert_id;
    }

    public int getAlert_user_id() {
        return alert_user_id;
    }

    public void setAlert_user_id(int alert_user_id) {
        this.alert_user_id = alert_user_id;
    }

    public double getAlert_user_lat() {
        return alert_user_lat;
    }

    public void setAlert_user_lat(double alert_user_lat) {
        this.alert_user_lat = alert_user_lat;
    }

    public double getAlert_user_lng() {
        return alert_user_lng;
    }

    public void setAlert_user_lng(double alert_user_lng) {
        this.alert_user_lng = alert_user_lng;
    }

    public boolean isAlert_status() {
        return alert_status;
    }

    public void setAlert_status(boolean alert_status) {
        this.alert_status = alert_status;
    }

    public int getAlert_picker_id() {
        return alert_picker_id;
    }

    public void setAlert_picker_id(int alert_picker_id) {
        this.alert_picker_id = alert_picker_id;
    }

    public double getAlert_picker_lat() {
        return alert_picker_lat;
    }

    public void setAlert_picker_lat(double alert_picker_lat) {
        this.alert_picker_lat = alert_picker_lat;
    }

    public double getGetAlert_picker_lng() {
        return getAlert_picker_lng;
    }

    public void setGetAlert_picker_lng(double getAlert_picker_lng) {
        this.getAlert_picker_lng = getAlert_picker_lng;
    }
}
