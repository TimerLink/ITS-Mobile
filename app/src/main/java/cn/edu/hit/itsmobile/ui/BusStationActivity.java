package cn.edu.hit.itsmobile.ui;

import java.text.DecimalFormat;

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
import android.widget.TextView;
import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.manager.SearchManager;
import cn.edu.hit.itsmobile.manager.SearchManager.OnGetResultListener;
import cn.edu.hit.itsmobile.model.MyLocation;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.utils.DistanceUtil;

public class BusStationActivity extends Activity{
	private String name;
	private String lines;
	private double latitude;
	private double longitude;
	
	private ListView busLineList;
	
	private SearchManager mSearchManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_station);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		TextView tvTitle = (TextView)findViewById(R.id.title);
		final TextView tvSubTitle = (TextView)findViewById(R.id.subtitle);
		busLineList = (ListView)findViewById(R.id.bus_line_list);
		
		mSearchManager = new SearchManager();
		
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		lines = intent.getStringExtra("lines");
		latitude = intent.getDoubleExtra("latitude", 0);
		longitude = intent.getDoubleExtra("longitude", 0);
		LatLng location = new LatLng(latitude, longitude);
		
		tvTitle.setText(name);

		double distance = DistanceUtil.getDistance(MyLocation.newInstance().location(), location);
		DecimalFormat df = new DecimalFormat("#.0");
		tvSubTitle.setText(df.format(distance) + getString(R.string.meter));
		int minute = (int)(distance/5.5/60);
        int hour = 0;
        if (minute>60){
            hour = minute/60;
            minute = minute%60;
        }
        String str = "还需"+Integer.toString(hour)+"小时"+Integer.toString(minute)+"分";
        TextView tvTime = (TextView)findViewById(R.id.subtitle_time);
        tvTime.setText(str);

		
		if(lines != null  && !lines.equals("")){
			setBusStationList();
		}else{
			mSearchManager.searchInCity(name);
			mSearchManager.setOnGetResultListener(new OnGetResultListener() {
				@Override
				public void onGet(PoiResult result) {
					if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
						return;
					}
					for (PoiInfo poi : result.getAllPoi()) {
						if (poi.type == PoiInfo.POITYPE.BUS_STATION) {
							lines = poi.address;
							setBusStationList();
							break;
						}
					}
				}
			});
		}

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
			Bundle bundle = new Bundle();
			bundle.putString("name", name);
			bundle.putString("lines", lines);
			bundle.putDouble("latitude", latitude);
			bundle.putDouble("longitude", longitude);
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("action", MainActivity.ACTION_SHOW_BUS_STATION);
			intent.putExtra("bundle", bundle);
			startActivity(intent);
			break;
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setBusStationList() {
		if(lines == null  || lines.equals("")){
			return;
		}
		String[] busLines = lines.split(";");
		busLineList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item_bus_lines, busLines));
		busLineList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView tv = (TextView)view;
				Intent intent = new Intent();
				intent.putExtra("line", tv.getText());
				intent.setClass(BusStationActivity.this, BusLineActivity.class);
				startActivity(intent);
			}
		});
	}

}
