package cn.edu.hit.itsmobile.model;

import com.baidu.mapapi.model.LatLng;

public class BusLocation {
    private static BusLocation instance;
    private double latitude;
    private double longitude;
    private String city;

    private BusLocation() {}

    public static BusLocation newInstance() {
        if(instance == null) {
            instance = new BusLocation();
        }
        return instance;
    }

    public BusLocation setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public BusLocation setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public BusLocation setCity(String city) {
        this.city = city;
        return this;
    }

    public double latitude() {
        return latitude;
    }

    public double longitude() {
        return longitude;
    }

    public LatLng location() {
        return new LatLng(latitude, longitude);
    }

    public String city() {
        return city;
    }
}

