package cn.edu.hit.itsmobile.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.adapter.BusStationListAdapter;
import cn.edu.hit.itsmobile.manager.QueryManager;
import cn.edu.hit.itsmobile.manager.QueryManager.OnQueryCompleteListener;
import cn.edu.hit.itsmobile.model.JSONData;
import cn.edu.hit.itsmobile.model.RuntimeParams;
import cn.edu.hit.itsmobile.model.SensorData;
import cn.edu.hit.itsmobile.model.SensorData.Packet;
import cn.edu.hit.itsmobile.util.StringUtil;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineResult.BusStation;
import com.baidu.mapapi.utils.DistanceUtil;

public class SensorDataActivity extends Activity implements OnNavigationListener{
    private String line;
    private String name;
    private int nextStation;

    private LinearLayout rootView;
    private MenuItem mShowInMap;
    private MenuItem mStatistics;
    private TextView tvTitle;
    private TextView tvNextStation;
    private ListView busStationList;
    
    private BusLineResult mBusLineResult;
    private List<SensorData> mSensorDatas;
    private List<List<LatLng>> mSensorDataPoints;
    private List<List<LatLng>> mPassedRoutePoints;
    private List<List<LatLng>> mRemainingRoutePoints;
    private List<BusStation> mPassedBusStation;
    private List<Integer> mPassedBusStationPointIndex;
    private List<Integer> mPassedSensorIds;
    private List<String> mLicenseNumbers;
    private List<Double> mRemainingTime;
    
    private RuntimeParams mRuntimeParams;
    private SpinnerAdapter mSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data);
        
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        
        rootView = (LinearLayout)findViewById(R.id.root_view);
        tvTitle = (TextView)findViewById(R.id.title);
        tvNextStation = (TextView)findViewById(R.id.next_station);
        busStationList = (ListView)findViewById(R.id.bus_station_list);
        rootView.setVisibility(View.GONE);
        
        mRuntimeParams = RuntimeParams.newInstance();
        mBusLineResult = mRuntimeParams.busLineResult();
        
        nextStation = Integer.MIN_VALUE;

        Intent intent = getIntent();
        line = intent.getStringExtra("line");
        name = intent.getStringExtra("name");
        if(name != null)
            tvTitle.setText(name);
        
        if(line == null) {
            Toast.makeText(this, R.string.bus_line_not_found, Toast.LENGTH_SHORT).show();
            finish();
        }

        String[] loading = new String[]{getString(R.string.loading)};
        mSpinnerAdapter = new ArrayAdapter<String>(SensorDataActivity.this, android.R.layout.simple_spinner_dropdown_item, loading);
        getActionBar().setListNavigationCallbacks(mSpinnerAdapter, SensorDataActivity.this);
        
        QueryManager mQueryManager = new QueryManager(line);
        mQueryManager.setOnQueryCompleteListner(new OnQueryCompleteListener() {
            
            @Override
            public void onFinish(JSONData result) {
                if(result == null){
                    finish();
                    return;
                }
                mSensorDatas = new ArrayList<SensorData>();
                mSensorDataPoints = new ArrayList<List<LatLng>>();
                for(SensorData data : result.data) {
                    mSensorDatas.add(data);
                    mSensorDataPoints.add(data.getRoute());
                }

                calculateRoute();
            }
        });
        mQueryManager.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sensor_data, menu);
        mShowInMap = menu.findItem(R.id.menu_view_in_map);
        mStatistics = menu.findItem(R.id.menu_statistics);
        mShowInMap.setVisible(false);
        mStatistics.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        switch (id) {
        case android.R.id.home:
            finish();
            break;

        case R.id.menu_view_in_map:
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("action", MainActivity.ACTION_SHOW_SENSOR_ROUTE);
            startActivity(intent);
            break;

        case R.id.menu_statistics:
            
            intent = new Intent(this, StatisticsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            break;
            
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    // Get index of point in a route closest to the origin given point
    public int getClosestPointIndex(LatLng origin, List<LatLng> route) {
        int index = -1;
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < route.size(); i++) {
            double distance = DistanceUtil.getDistance(origin, route.get(i));
            if(distance < minDistance) {
                index = i;
                minDistance = distance;
            }
        }
        return index;
    }

    // Get route part that already run
    // end -> current point
    // start -> data start point
    public void calculateRoute() {
        mRemainingRoutePoints = new ArrayList<List<LatLng>>();
        mPassedRoutePoints = new ArrayList<List<LatLng>>();
        mPassedSensorIds = new ArrayList<Integer>();
        mLicenseNumbers = new ArrayList<String>();
        for (int j = 0; j < mSensorDataPoints.size(); j++) {
            List<LatLng> sensorData = mSensorDataPoints.get(j);
            if(sensorData == null || sensorData.size() == 0)
            	continue;
            int startIndex = 0, endIndex = 0;
            LatLng startDataP = sensorData.get(0),
                    endDataP = sensorData.get(sensorData.size() - 1);
            List<LatLng> route = mBusLineResult.getSteps().get(0).getWayPoints();
            startIndex = getClosestPointIndex(startDataP, route);
            endIndex = getClosestPointIndex(endDataP, route);
            if(startIndex <= endIndex) {
                List<LatLng> passedRoute = new ArrayList<LatLng>();
                List<LatLng> remainingRoute = new ArrayList<LatLng>();
                for (int i = 0; i < endIndex; i++) {
                    passedRoute.add(route.get(i));
                }
                for (int i = endIndex; i < route.size(); i++) {
                    remainingRoute.add(route.get(i));
                }
                mPassedRoutePoints.add(passedRoute);
                mRemainingRoutePoints.add(remainingRoute);
                mPassedSensorIds.add(j);
                mLicenseNumbers.add(mSensorDatas.get(j).lpNumber);
            }
        }
        
        // Set Spinner
        mSpinnerAdapter = new ArrayAdapter<String>(
                SensorDataActivity.this, 
                android.R.layout.simple_spinner_dropdown_item, 
                mLicenseNumbers);
        getActionBar().setListNavigationCallbacks(mSpinnerAdapter, SensorDataActivity.this);
        rootView.setVisibility(View.VISIBLE);
        
        mRuntimeParams.setPassedRoutePoints(mPassedRoutePoints);
    }
        
    // Get next station point
    public void nextStation(int itemPosition) {
        List<LatLng> route = mPassedRoutePoints.get(itemPosition);
        LatLng currentPoint = route.get(route.size() - 1);
        // Station 1, 2's distance to current point
        // Station 1, 2 are two closest stations to current point
        double[] stationMinDist = {Double.MAX_VALUE, Double.MAX_VALUE};
        int[] stationIndex = {0, 0};
        for (int i = 0; i < mBusLineResult.getStations().size(); i++) {
            double distance = DistanceUtil.getDistance(mBusLineResult.getStations().get(i).getLocation(), currentPoint);
            if(distance < stationMinDist[0]) {
                stationMinDist[0] = distance;
                stationIndex[0] = i;
            } else if (distance < stationMinDist[1]) {
                stationMinDist[1] = distance;
                stationIndex[1] = i;
            }
        }
        // Station 1, 2's minimum distance to passed route points
        double distance = Double.MAX_VALUE;
        int pointIndex = 0;
        LatLng stationPoint1 = mBusLineResult.getStations().get(stationIndex[0]).getLocation();
        for (int i = 0; i < route.size(); i++) {
            double distTmp = DistanceUtil.getDistance(route.get(i), stationPoint1);
            if(distTmp < distance) {
                distance = distTmp;
                pointIndex = i;
            }
        }
        if(pointIndex == route.size() - 1) {
            nextStation = stationIndex[0];
        } else {
            nextStation = stationIndex[1];
        }
        mRuntimeParams.setNextStationIndex(nextStation);
    }
    
    // Map bus stations to the points of bus line
    public void mapBusStations(int itemPosition) {
        mPassedBusStation = new ArrayList<BusStation>();
        mPassedBusStationPointIndex = new ArrayList<Integer>();
        for (int i = nextStation; i < mBusLineResult.getStations().size(); i++) {
            BusStation busStation = mBusLineResult.getStations().get(i);
            mPassedBusStation.add(busStation);
            mPassedBusStationPointIndex.add(getClosestPointIndex(busStation.getLocation(),
                    mRemainingRoutePoints.get(itemPosition)));
        }
    }
    
    // Calculate average speed of the vehicle
    // speed * timeInterval = distance; distance/timeTotal = average speed
    // unit: meter per second
    public double calculateSpeed(List<Packet> packets) {
        double distance = 0; // meters
        for (int i = 0; i < packets.size(); i++) {
            if(i < packets.size() - 1)
            distance += packets.get(i).speed * (packets.get(i + 1).timestamp - (packets.get(i).timestamp));
        }
        int timeTotal = Math.abs(packets.get(0).timestamp - packets.get(packets.size() - 1).timestamp);
        return distance / timeTotal;
    }
    
    // Calculate distance between destined station and current point
    public void getRemainingTime(int itemPosition) {
        int start = 0, end = 0;
        int sensorIndex = mPassedSensorIds.get(itemPosition);
        List<Packet> packets = mSensorDatas.get(sensorIndex).data;
        List<LatLng> remainingRoute = mRemainingRoutePoints.get(itemPosition);
        double distance = 0, averageSpeed = calculateSpeed(packets);
        mRemainingTime = new ArrayList<Double>();
        
        mRuntimeParams.setSensorPackets(packets);

        // TODO for debug only
//        long timeDelta = (int) (System.currentTimeMillis()/1000 - packets.get(packets.size() - 1).timestamp);
        for (int i = 0; i < mPassedBusStationPointIndex.size(); i++) {
            if(i > 0)
                start = mPassedBusStationPointIndex.get(i - 1);
            end = mPassedBusStationPointIndex.get(i);
            for (int j = start; j <= end; j++) {
                distance += DistanceUtil.getDistance(remainingRoute.get(j), remainingRoute.get(j+1));
            }
//            mRemainingTime.add(distance / averageSpeed - timeDelta);
            mRemainingTime.add(distance / averageSpeed);
        }
    }
    
    public void setViews(int index) {
        tvNextStation.setText(getString(R.string.next_station)
                + mBusLineResult.getStations().get(nextStation).getTitle());
        List<String> estTime = new ArrayList<String>();
        for (Double time : mRemainingTime) {
            estTime.add(StringUtil.formatTime(SensorDataActivity.this, time));
        }
        // Set List
        busStationList.setAdapter(
                new BusStationListAdapter(
                        SensorDataActivity.this, 
                        R.layout.list_item_bus_stations, 
                        mPassedBusStation,
                        estTime)
                );
        
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        if(mLicenseNumbers != null && itemPosition < mLicenseNumbers.size()){
            nextStation(itemPosition);
            mapBusStations(itemPosition);
            getRemainingTime(itemPosition);
            setViews(itemPosition);
            
            // Set actionbar menu
            mShowInMap.setVisible(true);
            mStatistics.setVisible(true);
        }
        return false;
    }
}
