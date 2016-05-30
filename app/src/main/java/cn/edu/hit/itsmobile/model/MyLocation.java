package cn.edu.hit.itsmobile.model;

import com.baidu.mapapi.model.LatLng;

public class MyLocation {
    private static MyLocation instance;
    private double latitude;
    private double longitude;
    private String city;
    
    private MyLocation() {}
    
    public static MyLocation newInstance() {
        if(instance == null) {
            instance = new MyLocation();
        }
        return instance;
    }
    
    public MyLocation setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }
    
    public MyLocation setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
    
    public MyLocation setCity(String city) {
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
