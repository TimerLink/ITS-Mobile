package cn.edu.hit.itsmobile.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import cn.edu.hit.itsmobile.listener.OnMapFollowStatusChangeListener;
import cn.edu.hit.itsmobile.model.SensorData.Packet;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;


public class RuntimeParams {
    private static RuntimeParams instance;
    
    // Is first time locating
    private boolean isFirstLocating = true;
    // Is Map stick to current location
    private boolean isMapFollowing = false;
    // Nearby bus station search radius (meters)
    private int nearbySearchRadius = 1000;
    // Index of nearest bus station in nearby bus station list
    private int nearestStationIndex = -1;
    
    // Nearby bus station list
    private ArrayList<Bundle> nearbyBusStations = new ArrayList<Bundle>();
    // Bus line search result passing variable
    private BusLineResult busLineResult;
    
    // Parts of BusLineResult that the vehicle already passed
    private List<List<LatLng>> passedRoutePoints;
    // Parts of BusLineResult that the vehicle already passed
    private int nextStationIndex;
    // Packets that are going to be put to chart
    private List<Packet> sensorPackets;
    public Person sensorperson;
    
    // Map following status change listener
    private OnMapFollowStatusChangeListener mapFollowStatusChangeListener;

    private RuntimeParams() {}
    
    public static RuntimeParams newInstance() {
        if(instance == null) {
            instance = new RuntimeParams();
        }
        return instance;
    }
    
    public RuntimeParams setIsFirstLocating(boolean value) {
        isFirstLocating = value;
        return this;
    }
    
    public boolean isFirstLocating() {
        return isFirstLocating;
    }
    
    public RuntimeParams setIsMapFollowing(boolean value) {
        if(mapFollowStatusChangeListener != null)
            mapFollowStatusChangeListener.onChange(value);
        isMapFollowing = value;
        return this;
    }
    
    public boolean isMapFollowing() {
        return isMapFollowing;
    }
    
    public RuntimeParams setNearbySearchRadius(int value) {
        nearbySearchRadius = value;
        return this;
    }
    
    public int nearbySearchRadius() {
        return nearbySearchRadius;
    }
    
    public RuntimeParams setNearestStationIndex(int value) {
        nearestStationIndex = value;
        return this;
    }
    
    public int nearestStationIndex() {
        if(nearbyBusStations == null || nearbyBusStations.size() == 0)
            return -1;
        else
            return nearestStationIndex;
    }
    
    public ArrayList<Bundle> nearbyBusStations() {
        return nearbyBusStations;
    }
    
    public RuntimeParams setBusLineResult(BusLineResult value) {
        busLineResult = value;
        return this;
    }
    
    public BusLineResult busLineResult() {
        return busLineResult;
    }
    
    public RuntimeParams setPassedRoutePoints(List<List<LatLng>> list) {
        passedRoutePoints = list;
        return this;
    }
    
    public List<List<LatLng>> passedRoutePoints() {
        return passedRoutePoints;
    }
    
    public RuntimeParams setNextStationIndex(int value) {
        nextStationIndex = value;
        return this;
    }
    
    public int nextStationIndex() {
        return nextStationIndex;
    }
    
    public RuntimeParams setSensorPackets(List<Packet> packets) {
        sensorPackets = packets;
        return this;
    }
    
    public List<Packet> sensorPackets() {
        return sensorPackets;
    }
    
    public RuntimeParams setOnMapFollowStatusChangeListener(OnMapFollowStatusChangeListener listener){
        mapFollowStatusChangeListener = listener;
        return this;
    }

    /**
     * 新Person类方法
     * @param person
     * @return
     */
    public RuntimeParams setPerson(Person person){
        sensorperson = person;
        return this;
    }
    public Person getSensorperson(){
        return sensorperson;
    }
}
