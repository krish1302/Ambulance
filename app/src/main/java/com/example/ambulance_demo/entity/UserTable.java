package com.example.ambulance_demo.entity;

import com.google.gson.annotations.SerializedName;

public class UserTable {
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("user_email")
    private String user_email;
    @SerializedName("user_phone")
    private String user_phone;
    @SerializedName("user_password")
    private String user_password;
    @SerializedName("user_lat")
    private double user_lat;
    @SerializedName("user_lng")
    private double user_lng;
    @SerializedName("user_type")
    private String user_type;

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public UserTable(int user_id, String user_name, String user_email) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
    }

    public UserTable(double user_lat, double user_lng, String user_email) {
        this.user_lat = user_lat;
        this.user_lng = user_lng;
        this.user_email = user_email;
    }

    public UserTable(int user_id, double lat, double lng) {
        this.user_id = user_id;
        this.user_lat = lat;
        this.user_lng = lng;
    }

    public UserTable(double lat, double lng) {
        this.user_lat = lat;
        this.user_lng = lng;
    }

    public double getLat() {
        return user_lat;
    }

    public void setLat(double lat) {
        this.user_lat = lat;
    }

    public double getLng() {
        return user_lng;
    }

    public void setLng(double lng) {
        this.user_lng = lng;
    }

    public UserTable(String user_name, String user_email, String user_phone, String user_password, String user_type) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.user_password = user_password;
        this.user_type = user_type;
    }

    public UserTable(int user_id, String user_name, String user_email, String user_type) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_type = user_type;
    }

    public UserTable(Integer user_id, String user_name, String user_email, String user_phone, String user_password) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.user_password = user_password;
    }

    public UserTable(String user_email, double user_lat, double user_lng) {
        this.user_email = user_email;
        this.user_lat = user_lat;
        this.user_lng = user_lng;
    }

    public UserTable(String user_email, String user_password) {
        this.user_email = user_email;
        this.user_password = user_password;
    }

    public UserTable(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}
