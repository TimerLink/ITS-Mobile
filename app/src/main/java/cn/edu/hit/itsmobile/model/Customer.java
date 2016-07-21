package cn.edu.hit.itsmobile.model;

import java.util.Date;

public class Customer {
    private int id;
    private int sensorId;
    private double longitude;
    private double latitude;
    private Date gpstime;
    private Location station;
    private Location finalStation;
    private String locationMsg;
    private double finallongitude;
    private double finallatitude;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Date getGpstime() {
        return gpstime;
    }

    public void setGpstime(Date gpstime) {
        this.gpstime = gpstime;
    }

    public Location getStation() {
        return station;
    }

    public void setStation(Location station) {
        this.station = station;
    }

    public Location getFinalStation() {
        return finalStation;
    }

    public void setFinalStation(Location finalStation) {
        this.finalStation = finalStation;
    }

    public String getLocationMsg() {
        return locationMsg;
    }

    public void setLocationMsg(String locationMsg) {
        this.locationMsg = locationMsg;
    }

    public double getFinallongitude() {
        return finallongitude;
    }

    public void setFinallongitude(double finallongitude) {
        this.finallongitude = finallongitude;
    }

    public double getFinallatitude() {
        return finallatitude;
    }

    public void setFinallatitude(double finallatitude) {
        this.finallatitude = finallatitude;
    }
}
