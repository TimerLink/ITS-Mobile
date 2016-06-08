package cn.edu.hit.itsmobile.model;

/**
 * Created by Newsoul on 2016/6/7.
 */
public class Location {
    public double longitude;
    public double latitude;
    public double speed = 5.56;
    public Location(double latitude,double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public double getLatitude(){
        return latitude;
    }
}
