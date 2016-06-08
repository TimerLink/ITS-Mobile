package cn.edu.hit.itsmobile.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.listener.MyLocationListener;
import cn.edu.hit.itsmobile.listener.BusLocationListener;
import cn.edu.hit.itsmobile.listener.OnMapFollowStatusChangeListener;
import cn.edu.hit.itsmobile.manager.MapManager;
import cn.edu.hit.itsmobile.manager.QueryManager;
import cn.edu.hit.itsmobile.manager.QueryPersonManager;
import cn.edu.hit.itsmobile.model.ColorSet;
import cn.edu.hit.itsmobile.model.ColorSet.ColorType;
import cn.edu.hit.itsmobile.model.Person;
import cn.edu.hit.itsmobile.model.RuntimeParams;
import cn.edu.hit.itsmobile.ui.widget.BusInfo_Details;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineResult.BusStation;
import com.baidu.mapapi.search.busline.BusLineResult.BusStep;

import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;

public class MapFragment extends Fragment implements OnMarkerClickListener{
	private View mView;
	private MapView mMapView;
	private BaiduMap mBaiduMap;

    private BaiduMap sBaiduMap;

	private LinearLayout bottomLayout;
	private ImageButton btnMyLocation;
	private ImageButton btnNearByStation;
	
	private MapManager mMapManager;
	private RuntimeParams mRuntimeParams;
    private LocationClient mLocationClient;

//    private LocationClient sLocationClient;

    private final static String HTTP_HOST = "http://123.206.85.17:8080/HttpServer";//104.236.182.43//123.206.85.17
    private HttpURLConnection con = null;

	@SuppressLint("InflateParams")
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_map, null);
        mMapView = (MapView) mView.findViewById(R.id.bmapView);
        btnMyLocation = (ImageButton) mView.findViewById(R.id.btnMyLocation);
        btnNearByStation = (ImageButton) mView.findViewById(R.id.btnNearByStation);
        bottomLayout = (LinearLayout) mView.findViewById(R.id.bottom_layout);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        mRuntimeParams = RuntimeParams.newInstance();

        mBaiduMap = mMapView.getMap();
//        sBaiduMap = mMapView.getMap();

        // location options
        final LocationClientOption locOption = new LocationClientOption();
        locOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locOption.setCoorType("bd09ll");
        locOption.setScanSpan(1000);
        locOption.setIsNeedAddress(true);

        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener(mBaiduMap));

        //sBaiduMap
//		mLocationClient.registerLocationListener(new BusLocationListener(sBaiduMap));
//        BusInfo_Details.longi = 100;
        mLocationClient.setLocOption(locOption);
        mLocationClient.start();
//        if (mLocationClient.isStarted()){
//            BusInfo_Details.longi = 1;
//        }
//        if (mLocationClient!=null){
//            BusInfo_Details.longi = 2;
//        }
//        if (!mLocationClient.isStarted()){
//            BusInfo_Details.longi = 3;
//        }
//
//        if (mLocationClient != null && mLocationClient.isStarted()) {
//            BDLocation bdLocation = mLocationClient.getLastKnownLocation();
//            double lat = bdLocation.getLatitude();
//            BusInfo_Details.longi = lat;
//        }
//        BDLocation bdLocation = mLocationClient.getLastKnownLocation();
//        if (bdLocation!=null){
//            BusInfo_Details.longi = bdLocation.getLongitude();
//            BusInfo_Details.latit = bdLocation.getLatitude();
//        }
//        else {
//            BusInfo_Details.latit = 1;
//            BusInfo_Details.longi = 2;
//        }

//        if (mLocationClient != null && mLocationClient.isStarted()) {
//            mLocationClient.requestLocation();//原始函数
//            BusInfo_Details.latit = 100;
////            BDLocation bdLocation = mLocationClient.getLastKnownLocation();
////            double lat = bdLocation.getLatitude();
////            BusInfo_Details.longi = lat;
////            String lati = Double.toString(lat);
////            Intent intent = new Intent("lat", Uri.parse(lati))
//        }
//        else {
//            Log.d("LocSDK5", "locClient is null or not started");
//        }

        
        for (int i = 0; i < mMapView.getChildCount(); i++) {
            if(i > 0)
            	mMapView.getChildAt(i).setVisibility(View.GONE);
		}
        
        mBaiduMap.setMyLocationEnabled(true);

//        sBaiduMap.setMyLocationEnabled(true);

        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location);
        MyLocationConfiguration config = new MyLocationConfiguration(LocationMode.FOLLOWING, true, mCurrentMarker);  
        mBaiduMap.setMyLocationConfigeration(config);
        mBaiduMap.setOnMarkerClickListener(this);

        mRuntimeParams.setOnMapFollowStatusChangeListener(new OnMapFollowStatusChangeListener() {

            @Override
            public void onChange(boolean isFollowing) {
                setLocationButton(isFollowing);
            }
        });
        
        btnMyLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                BDLocation bdLocation = mLocationClient.getLastKnownLocation();
                if (bdLocation!=null){
                    BusInfo_Details.longi = bdLocation.getLongitude();
                    BusInfo_Details.latit = bdLocation.getLatitude();
                }
                else {
                    BusInfo_Details.latit = 45.750684;//主楼
                    BusInfo_Details.longi = 126.634287;
                }
				if(!mRuntimeParams.isMapFollowing()){
					try {
						mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(12.5f));//mBaiduMap.getMaxZoomLevel()
					} catch (NullPointerException e) {}
					btnMyLocation.setImageResource(R.drawable.ic_my_location_white);
				}else{
					btnMyLocation.setImageResource(R.drawable.ic_my_location);
				}
				mRuntimeParams.setIsMapFollowing(!mRuntimeParams.isMapFollowing());
				btnMyLocation.setSelected(mRuntimeParams.isMapFollowing());
			}
		});
        
        mMapManager = new MapManager(getActivity(), getView(), mMapView).setBottomLayout(bottomLayout);
        btnNearByStation.setOnClickListener(new OnClickListener() {
            @Override
			public void onClick(View v) {
                QueryPersonManager mQueryPersonManager = new QueryPersonManager("11");
                mQueryPersonManager.setOnQueryCompleteListner(new QueryPersonManager.OnQueryCompleteListener() {

                    @Override
                    public void onFinish(Person person) {
                        if (person == null) {
//                            finish();
                            return;
                        }
                        /**
                         * 进行数据操作
                         */
                        double longitude = person.busLongitude;
                        double latitude = person.busLatitude;

                        BusInfo_Details.busLongi = longitude;
                        BusInfo_Details.busLatit = latitude;
                        BusInfo_Details.busNumber = person.personNumber;

//                        String str = Double.toString(longitude);
//                        Log.e("longitu:", str);
//                        LatLng latLng = new LatLng(latitude, longitude);
//                        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
//                        mBaiduMap.animateMapStatus(update);
//
//                        MyLocationData.Builder builder = new MyLocationData.Builder();
//                        builder.latitude(latitude);
//                        builder.longitude(longitude);
//                        MyLocationData loc = builder.build();
//                        mBaiduMap.setMyLocationData(loc);

                        /**
                         * 尝试标记
                         */

                        Bundle bundle = new Bundle();
                        bundle.putString("name", "最近公交");
                        bundle.putDouble("longitude", longitude);
                        bundle.putDouble("latitude", latitude);
                        mMapManager.addMarker(bundle, R.drawable.ic_marker_highlight);
                    }
                });
                mQueryPersonManager.execute();
            }
		});
//        btnNearByStation.setOnClickListener(new OnClickListener() {
//            @Override
//			public void onClick(View v) {
//                boolean isFirstLocated = true;
//
//                Person person = doIn();
////                double longitude = person.busLongitude;
////                double latitude = person.busLatitude;
//
////                double longitude = 126.631026;
////                double latitude = 45.783751;
//
//                /**
//                 * 测试Json
//                 */
////                QueryPersonManager qp = new QueryPersonManager("11");
////                Person person = qp.doInBackground();
//
//				double longitude = person.busLongitude;
//                double latitude = person.busLatitude;
//                String str = Double.toString(longitude);
//                Log.e("longitu:",str);
//                LatLng latLng = new LatLng(latitude, longitude);
//                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
//                mBaiduMap.animateMapStatus(update);
//
//                MyLocationData.Builder builder = new MyLocationData.Builder();
//                builder.latitude(latitude);
//                builder.longitude(longitude);
//                MyLocationData loc = builder.build();
//                mBaiduMap.setMyLocationData(loc);
//			}
//
//		});
        /**
         *
         * 原始代码:寻找附近车站
         */
//		btnNearByStation.setOnClickListener(new OnClickListener() {
//
////			@Override
////			public void onClick(View v) {
////				mMapManager.searchNearByBusStation();
////			}
//            @Override
//			public void onClick(View v) {
//				if(!mRuntimeParams.isMapFollowing()){
//					try {
//						sBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(sBaiduMap.getMaxZoomLevel()));
//					} catch (NullPointerException e) {}
//					btnNearByStation.setImageResource(R.drawable.ic_bus);
//				}else{
//					btnNearByStation.setImageResource(R.drawable.ic_bus);
//				}
//				mRuntimeParams.setIsMapFollowing(!mRuntimeParams.isMapFollowing());
//				btnNearByStation.setSelected(mRuntimeParams.isMapFollowing());
//			}
//
//		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
		setLocationButton(mRuntimeParams.isMapFollowing());
	}
	
	public void setLocationButton(boolean isFollowing) {
        if(isFollowing){
            btnMyLocation.setImageResource(R.drawable.ic_my_location_white);
        }else{
            btnMyLocation.setImageResource(R.drawable.ic_my_location);
        }
        btnMyLocation.setSelected(isFollowing);
    }
	
	public void searchNearByBusStation() {
		if(mMapManager != null)
			mMapManager.searchNearByBusStation();
	}
	
	public Marker addMarker(Bundle bundle){
	    mRuntimeParams.setIsMapFollowing(false);
		final Marker marker = mMapManager.addMarker(bundle);
		double latitude = bundle.getDouble("latitude", 0);
		double longitude = bundle.getDouble("longitude", 0);
		LatLng location = new LatLng(latitude, longitude);
		// Scroll to Marker
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(location));
		LinearLayout tooltip = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.tooltip, (ViewGroup)mView, false);
		TextView text = (TextView) tooltip.findViewById(R.id.text);
		text.setText(marker.getExtraInfo().getString("name"));
		tooltip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toolTipClick(marker);
			}
		});
		InfoWindow mInfoWindow = new InfoWindow(tooltip, marker.getPosition(), -48);
		mBaiduMap.showInfoWindow(mInfoWindow);
		return marker;
	}

    // Add Route Lines
    public void addRoute(List<LatLng> routePoints, int colorPos) {
        ColorSet colors = new ColorSet(getActivity());
        PolylineOptions route = new PolylineOptions();
        route.points(routePoints);
        route.color(colors.getColor(colorPos % ColorType.values().length));
        route.width(10);
        mBaiduMap.addOverlay(route);
    }

    // Add Route Lines
    public void addRoute(List<LatLng> routePoints) {
        addRoute(routePoints, 0);
    }

    // Add Bus Station Markers
    public void addBusStations(BusLineResult busLine, boolean isMarkNext) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker);
        final List<OverlayOptions> markers = new ArrayList<OverlayOptions>();
        for(BusStation station : busLine.getStations()){
            markers.add(new MarkerOptions().position(station.getLocation()).icon(bitmap));
        }
        OverlayManager overlayManager = new OverlayManager(mBaiduMap) {
            
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
            
            @Override
            public List<OverlayOptions> getOverlayOptions() {
                return markers;
            }
        };
        overlayManager.addToMap();
        overlayManager.zoomToSpan();
        overlayManager.removeFromMap();
        for(int i = 0; i < busLine.getStations().size(); i++){
            BusStation station = busLine.getStations().get(i);
            Bundle bundle = new Bundle();
            bundle.putString("name", station.getTitle());
            bundle.putDouble("longitude", station.getLocation().longitude);
            bundle.putDouble("latitude", station.getLocation().latitude);
            if(isMarkNext && i == mRuntimeParams.nextStationIndex())
                mMapManager.addMarker(bundle, R.drawable.ic_marker_highlight);
            else
                mMapManager.addMarker(bundle);
        }
    }
    
    public void addBusStations(BusLineResult busLine) {
        addBusStations(busLine, false);
    }
	
	public void showBusLine(){
		BusLineResult busLine = mRuntimeParams.busLineResult();
		if(busLine  == null)
			return;

		mMapManager.clearMap();
		
		//Add Route Line
		List<LatLng> routePoints = new ArrayList<LatLng>();
		for(BusStep step : busLine.getSteps()){
			for(LatLng point : step.getWayPoints()){
				routePoints.add(point);
			}
		}
		addRoute(routePoints);
		addBusStations(busLine);
	}
	
	public void showSensorData() {
        mMapManager.clearMap();
        
//        List<List<LatLng>> busLines = mRuntimeParams.sensorDataPoints();
//        if(busLines  == null || busLines.size() == 0)
//            return;
//        
//        for (int i = 0; i < busLines.size(); i++) {
//            List<LatLng> routePoints = busLines.get(i);
//            addRoute(routePoints, i);
//        }

        List<List<LatLng>> busLines_ = mRuntimeParams.passedRoutePoints();
        if(busLines_  == null || busLines_.size() == 0)
            return;
        for (int i = 0; i < busLines_.size(); i++) {
            List<LatLng> routePoints = busLines_.get(i);
            addRoute(routePoints, i);
        }

        BusLineResult busLine = mRuntimeParams.busLineResult();
        if(busLine  == null)
            return;
        addBusStations(busLine, true);
    }
	
	public void showNearbyBusStations() {
		mMapManager.clearMap();
		for(Bundle bundle : mRuntimeParams.nearbyBusStations()){
			addMarker(bundle);
		}
		mBaiduMap.hideInfoWindow();
	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		LinearLayout tooltip = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.tooltip, (ViewGroup)mView, false);
		TextView text = (TextView) tooltip.findViewById(R.id.text);
		text.setText(marker.getExtraInfo().getString("name"));
		tooltip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toolTipClick(marker);
			}
		});
		InfoWindow mInfoWindow = new InfoWindow(tooltip, marker.getPosition(), -32);
		mBaiduMap.showInfoWindow(mInfoWindow);
		Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_enter);
		tooltip.setAnimation(anim);
		tooltip.startAnimation(anim);
		return false;
	}
	
	/**
	 * Marker's tooltip click event
	 * @param marker
	 */
	public void toolTipClick(Marker marker){
		Intent intent = new Intent();
		intent.setClass(getActivity(), BusStationActivity.class);
		intent.putExtra("name", marker.getExtraInfo().getString("name"));
		intent.putExtra("lines", marker.getExtraInfo().getString("lines"));
		intent.putExtra("latitude", marker.getExtraInfo().getDouble("latitude"));
		intent.putExtra("longitude", marker.getExtraInfo().getDouble("longitude"));
		getActivity().startActivity(intent);
		mBaiduMap.hideInfoWindow();
	}
}
