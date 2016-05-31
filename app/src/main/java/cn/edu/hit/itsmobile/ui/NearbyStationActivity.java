package cn.edu.hit.itsmobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;

import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.listener.BusLocationListener;
import cn.edu.hit.itsmobile.model.RuntimeParams;

public class NearbyStationActivity extends Activity{
	private ListView busStationList;
	
	private RuntimeParams mRuntimeParams;
	private LocationClient mLocationClient;
	private BaiduMap mBaiduMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mRuntimeParams = RuntimeParams.newInstance();
		
		if(mRuntimeParams.nearbyBusStations() == null || mRuntimeParams.nearbyBusStations().size() == 0)
			finish();
		setContentView(R.layout.activity_nearby_station);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		String[] stations = new String[mRuntimeParams.nearbyBusStations().size()];
		for(int i = 0; i < mRuntimeParams.nearbyBusStations().size(); i++){
			stations[i] = mRuntimeParams.nearbyBusStations().get(i).getString("name")
							+ (i == mRuntimeParams.nearestStationIndex()?"(" + getString(R.string.nearest_station) + ")":"");
		}

		/**
		 * 模仿MyLocation
		 */
		mLocationClient.registerLocationListener(new BusLocationListener(mBaiduMap));


		busStationList = (ListView)findViewById(R.id.bus_station_list);
		busStationList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item_bus_lines, stations));
		busStationList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("name", mRuntimeParams.nearbyBusStations().get(position).getString("name"));
				intent.putExtra("latitude", mRuntimeParams.nearbyBusStations().get(position).getDouble("latitude"));
				intent.putExtra("longitude", mRuntimeParams.nearbyBusStations().get(position).getDouble("longitude"));
				intent.setClass(NearbyStationActivity.this, BusStationActivity.class);
				startActivity(intent);
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_bus_station, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			break;

		case R.id.menu_view_in_map:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("action", MainActivity.ACTION_SHOW_NEARBY_STATION);
			startActivity(intent);
			break;
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
