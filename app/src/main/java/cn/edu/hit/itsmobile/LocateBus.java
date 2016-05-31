package cn.edu.hit.itsmobile;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.baidu.mapapi.map.*;

import cn.edu.hit.itsmobile.model.MyLocation;
import cn.edu.hit.itsmobile.ui.BusLineActivity;

/**
 * Created by Newsoul on 2016/5/31.
 */
public class LocateBus {
    private Context mContext;
    private MyLocation location;



    public LocateBus(Context context) {
        mContext = context;
        location = MyLocation.newInstance();
        location.setCity("哈尔滨");
    }

    public void run() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mContext != null) {
                    Intent intent = new Intent(mContext, BusLineActivity.class);
                    intent.putExtra("line", "63·");
                    mContext.startActivity(intent);
                }
            }
        }, 1000);
    }
    public void setLocate(BaiduMap baiduMap){

        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(45.77316);
        locationBuilder.longitude(126.619159);
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);

    }
}
