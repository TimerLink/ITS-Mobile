package cn.edu.hit.itsmobile.listener;

import cn.edu.hit.itsmobile.model.MyLocation;
import cn.edu.hit.itsmobile.model.RuntimeParams;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;

public class MyLocationListener implements BDLocationListener {
	private BaiduMap mBaiduMap;
	private MyLocation location;
	private RuntimeParams params;
	
	public MyLocationListener(BaiduMap map){
		mBaiduMap = map;
		location = MyLocation.newInstance();
		params = RuntimeParams.newInstance();
	}
	
	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null || mBaiduMap == null)
			return;

		if(params.isFirstLocating() || params.isMapFollowing()){
		    this.location.setLatitude(location.getLatitude())//126.619159
		        .setLongitude(location.getLongitude())//45.77316
		         .setCity(location.getCity());
	        try {
	        	if(params.isFirstLocating())
	        		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(mBaiduMap.getMaxZoomLevel()));
		        mBaiduMap.setMyLocationData(
		                new MyLocationData.Builder()
		                .accuracy(location.getRadius())
		                .latitude(location.getLatitude())
		                .longitude(location.getLongitude())
		                .build());

		        params.setIsFirstLocating(false);
			} catch (NullPointerException e) {}
		}
	}
}