package cn.edu.hit.itsmobile.model;

import com.alibaba.fastjson.JSON;

/**
 * Created by Newsoul on 2016/6/1.
 */
public class Person {
    public double busLongitude;
    public double busLatitude;
    public int personNumber;
    public Person(double busLatitude,double busLongitude,int personNumber){
        this.busLatitude = busLatitude;
        this.busLongitude = busLongitude;
        this.personNumber = personNumber;
    }
    public Person(){}
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
