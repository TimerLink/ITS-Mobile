package cn.edu.hit.itsmobile.manager;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow.OnDismissListener;
import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.manager.SearchManager.OnGetResultListener;
import cn.edu.hit.itsmobile.model.MyLocation;
import cn.edu.hit.itsmobile.model.RuntimeParams;
import cn.edu.hit.itsmobile.ui.NearbyStationActivity;
import cn.edu.hit.itsmobile.ui.widget.BottomWindow;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.utils.DistanceUtil;


/**
 * 公交管理
 */
public class MapManager {
	private Context mContext;
	
	private View rootView;
	private ViewGroup bottomLayout;
	private BaiduMap mBaiduMap;
	
	private RuntimeParams mRuntimeParams;
	private SearchManager mSearchManager;
	private ArrayList<Marker> mStationMarkers;
	
	public MapManager(Context context, final View view, MapView mapView) {
		mContext = context;
		this.rootView = view;
		mBaiduMap = mapView.getMap();
		mRuntimeParams = RuntimeParams.newInstance();
		mSearchManager = new SearchManager();
		mStationMarkers = new ArrayList<Marker>();
		
		mSearchManager.setOnGetResultListener(new OnGetResultListener() {
			
			@Override
			public void onGet(PoiResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR)
					return;
				
				mRuntimeParams.nearbyBusStations().clear();
				
				MyLocation location = MyLocation.newInstance();
				int index = -1;
				double distance2, maxDistance2 = Double.MAX_VALUE;
				for (PoiInfo poi : result.getAllPoi()) {
					if (poi.type == PoiInfo.POITYPE.BUS_STATION) {
						index++;
						Bundle bundle = new Bundle();
						bundle.putString("name", poi.name);
						bundle.putString("lines", poi.address);
						bundle.putDouble("longitude", poi.location.longitude);
						bundle.putDouble("latitude", poi.location.latitude);

						distance2 = DistanceUtil.getDistance(poi.location, location.location());
						if(distance2 < maxDistance2) {
							maxDistance2 = distance2;
							mRuntimeParams.setNearestStationIndex(index);
						}

						mRuntimeParams.nearbyBusStations().add(bundle);
					}
				}
				
				Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_popup_exit);
				anim.setFillAfter(true);
				bottomLayout.startAnimation(anim);
				
				final BottomWindow window = new BottomWindow(mContext);
				window.setText(mContext.getString(R.string.find_station_tip_1)
						+ result.getAllPoi().size() + mContext.getString(R.string.find_station_tip_2));
				window.setChevron(true);
				window.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, NearbyStationActivity.class);
						mContext.startActivity(intent);
						window.dismiss();
					}
				});
				window.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss() {
						Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_popup_enter);
						anim.setFillAfter(true);
						bottomLayout.startAnimation(anim);
					}
				});
				window.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
			}
		});
	}
	
	public MapManager setBottomLayout(ViewGroup bottomLayout) {
		this.bottomLayout = bottomLayout;
		return this;
	}
	
	public void searchNearByBusStation() {
		if(mSearchManager != null)
		    mSearchManager.searchNearbyBusStation();
	}
	
	/**
	 * Clear All Station Markers form Map
	 */
	public void cleanStationArray(){
		for(Marker marker : mStationMarkers){
			marker.remove();
		}
		mStationMarkers.clear();
	}
	
	public void clearMap() {
		if(mBaiduMap != null){
			cleanStationArray();
			mBaiduMap.clear();
		}
	}

    /**
     * Add Marker to Map
     * @param bundle
     * @return
     */
    public Marker addMarker(Bundle bundle){
        return addMarker(bundle, R.drawable.ic_marker);
    }

    /**
     * Add Marker to Map
     * @param bundle
     * @param icon
     * @return
     */
    public Marker addMarker(Bundle bundle, int icon){
        double latitude = bundle.getDouble("latitude", 0);
        double longitude = bundle.getDouble("longitude", 0);
        LatLng location = new LatLng(latitude, longitude);
        
        //Whether target marker already exist
        for(Marker marker : mStationMarkers){
            if(marker.getPosition().latitude == latitude
                    && marker.getPosition().longitude == longitude)
                return marker;
        }
        
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);
        OverlayOptions option = new MarkerOptions().position(location).icon(bitmap);
        Marker marker = (Marker)mBaiduMap.addOverlay(option);
        marker.setExtraInfo(bundle);
        
        mStationMarkers.add(marker);
        return marker;
    }
}
