package com.swiss4ward.swissapp.models;

import android.graphics.Point;

import org.json.JSONObject;

public class Address {

    private String street, suite, city, zipCode,lat,lng;

    public Address(JSONObject ob) {

        this.street = ob.getString("street");
        this.suite = ob.getString("suite");
        this.city = ob.getString("city");
        this.zipcode = ob.getString("zipcode");
        this.lat = ob.getJSONObject("geo").getString("lat");
        this.lng = ob.getJSONObject("geo").getString("lng");

    }

    public Address(String street, String suite, String city, String zipCode, String lat, String lng) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipCode = zipCode;
        this.lat = lat;
        this.lng = lng;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}