package cn.edu.hit.itsmobile.model;

import com.alibaba.fastjson.JSON;

/**
 * Created by Newsoul on 2016/6/1.
 */
public class Person {
    public double busLongitude;
    public double busLatitude;
    public int personNumber;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
