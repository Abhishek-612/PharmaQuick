package com.example.pharmaquick;

public class PharmacyStore {
    String name;
    double latitude;
    double longitude;
    double distance;
    String address;

    public PharmacyStore(String name, double latitude, double longitude, double distance, String address) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.address = address;
    }

    public PharmacyStore(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public PharmacyStore() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
