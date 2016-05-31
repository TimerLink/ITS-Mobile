package cn.edu.hit.itsmobile.listener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;

import cn.edu.hit.itsmobile.model.BusLocation;
import cn.edu.hit.itsmobile.model.RuntimeParams;

public class BusLocationListener implements BDLocationListener {
    private BaiduMap mBaiduMap;
    private BusLocation location;
    private RuntimeParams params;

    public BusLocationListener(BaiduMap map){
        mBaiduMap = map;
        location = BusLocation.newInstance();
        params = RuntimeParams.newInstance();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null || mBaiduMap == null)
            return;

        if(params.isFirstLocating() || params.isMapFollowing()){
            this.location.setLatitude(45.795692)//126.715664,45.795692
                    .setLongitude(126.715664)//哈东站
                    .setCity(location.getCity());
            try {
                if(params.isFirstLocating())
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(mBaiduMap.getMaxZoomLevel()));
                mBaiduMap.setMyLocationData(
                        new MyLocationData.Builder()
                                .accuracy(location.getRadius())
                                .latitude(45.795692)
                                .longitude(126.715664)
                                .build());

                params.setIsFirstLocating(false);
            } catch (NullPointerException e) {}
        }
    }
}
