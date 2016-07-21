package cn.edu.hit.itsmobile.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.adapter.BusStationListAdapter;
import cn.edu.hit.itsmobile.manager.SearchManager;
import cn.edu.hit.itsmobile.manager.SearchManager.OnGetBusLineListener;
import cn.edu.hit.itsmobile.manager.SearchManager.OnGetResultListener;
import cn.edu.hit.itsmobile.model.Customer;
import cn.edu.hit.itsmobile.model.Location;
import cn.edu.hit.itsmobile.model.RuntimeParams;
import cn.edu.hit.itsmobile.service.HttpService;
import cn.edu.hit.itsmobile.ui.widget.BusInfo_Details;

import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.google.gson.Gson;

public class BusLineActivity extends Activity implements OnNavigationListener{
    private String line;
    private String name;
	private ArrayList<String> lineName;
	private ArrayList<String> lineUid;

	public static double longi;
	public static double latit;
//	public static Location finalStation;
//	public static double finallongi;
//	public static double finallatit;
	
	private LinearLayout rootView;
    private MenuItem mShowInMap;
    private MenuItem mSensorData;
	private TextView tvTitle;
	private TextView tvStartTime;
	private TextView tvEndTime;
	private ListView busStationList;
	
	private RuntimeParams mRuntimeParams;
	private SearchManager mSearchManager;
	private SpinnerAdapter mSpinnerAdapter;
	private ImageButton button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_line);
		
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        /**
         * 新加的button展示详情
         */

        button = (ImageButton) findViewById(R.id.btnInformation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusLineActivity.this, BusInfo_Details.class);
                startActivity(intent);
            }
        });

		rootView = (LinearLayout)findViewById(R.id.root_view);
		tvTitle = (TextView)findViewById(R.id.title);
		tvStartTime = (TextView)findViewById(R.id.start_time);
		tvEndTime = (TextView)findViewById(R.id.end_time);
		busStationList = (ListView)findViewById(R.id.bus_station_list);
		
		rootView.setVisibility(View.GONE);
		
		mRuntimeParams = RuntimeParams.newInstance();
		
		Intent intent = getIntent();
		line = intent.getStringExtra("line");
		name = intent.getStringExtra("name");
		String uid = intent.getStringExtra("uid");

//		int busId = Integer.valueOf(line);
//		String busLine = line;
		String strings[] = name.split("路");
		int busId = Integer.valueOf(strings[0]);
		Customer customer = new Customer();
		customer.setLatitude(latit);
		customer.setLongitude(longi);
		customer.setId(busId);
		customer.setLocationMsg("name:"+name+" uid:"+uid);
//		if (finalStation!=null) {
//			customer.setFinallatitude(finallatit);
//			customer.setFinallongitude(finallongi);
//		}
		Gson gson = new Gson();
		String cusString = gson.toJson(customer);

		Intent intent1 = new Intent(this, HttpService.class);
		intent1.putExtra("customerMessage",cusString);
		startService(intent1);

//		intent1.setAction("bc.line");
//		String customer = "";
//		intent1.putExtra("customer",customer);
//		sendBroadcast(intent1);

		String[] loading;
		if(name == null)
			loading = new String[]{getString(R.string.loading)};
		else {
			loading = new String[]{name};
			tvTitle.setText(name);
		}
		mSpinnerAdapter = new ArrayAdapter<String>(BusLineActivity.this, android.R.layout.simple_spinner_dropdown_item, loading);
		getActionBar().setListNavigationCallbacks(mSpinnerAdapter, BusLineActivity.this);
		
		lineName = new ArrayList<String>();
		lineUid = new ArrayList<String>();
		
		mSearchManager = new SearchManager();
		mSearchManager.setOnGetResultListener(new OnGetResultListener() {
			
			@Override
			public void onGet(PoiResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					return;
				}
				for (PoiInfo poi : result.getAllPoi()) {
					if (poi.type == PoiInfo.POITYPE.BUS_LINE) {
						lineName.add(poi.name);
						lineUid.add(poi.uid);
					}
				}
				if(lineUid.size() > 0){
					//Set Spinner
					mSpinnerAdapter = new ArrayAdapter<String>(
							BusLineActivity.this, 
							android.R.layout.simple_spinner_dropdown_item, 
							lineName);
					getActionBar().setListNavigationCallbacks(mSpinnerAdapter, BusLineActivity.this);
					rootView.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(BusLineActivity.this, getString(R.string.bus_line_not_found), Toast.LENGTH_SHORT).show();
				}
			}
		});
		mSearchManager.setOnGetBusLineListener(new OnGetBusLineListener() {
			
			@SuppressLint("SimpleDateFormat")
            @Override
			public void onGet(BusLineResult result) {
				if (result == null || result.error != BusLineResult.ERRORNO.NO_ERROR) {
					return;
				}
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				try {
					if(result.getStartTime() != null)
						tvStartTime.setText(format.format(result.getStartTime()));
					if(result.getEndTime() != null)
						tvEndTime.setText(format.format(result.getEndTime()));
					busStationList.setAdapter(
							new BusStationListAdapter(
									BusLineActivity.this, 
									R.layout.list_item_bus_stations, 
									result.getStations()
									)
							);
					rootView.setVisibility(View.VISIBLE);
					mShowInMap.setVisible(true);
					mSensorData.setVisible(true);
					mRuntimeParams.setBusLineResult(result);
				} catch (Exception e) {
					Log.e("BusLineActivity", e.toString());
				}
			}
		});
		if(line != null)
		    mSearchManager.searchInCity(line);
		else {
		    if(uid != null)
	            mSearchManager.getBusLineInfo(uid);
		    if(name != null) {
	            line = name.substring(0, name.indexOf("·") + 1);
		    }
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_bus_line, menu);
        mShowInMap = menu.findItem(R.id.menu_view_in_map);
        mSensorData = menu.findItem(R.id.menu_sensor_data);
        mShowInMap.setVisible(false);
        mSensorData.setVisible(false);
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
            intent.putExtra("action", MainActivity.ACTION_SHOW_BUS_LINE);
            startActivity(intent);
            break;

        case R.id.menu_sensor_data:
            intent = new Intent(this, SensorDataActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("line", line);
            startActivity(intent);
            break;
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if(lineName != null && itemPosition < lineName.size()){
		    name = lineName.get(itemPosition);
			tvTitle.setText(name);
			mSearchManager.getBusLineInfo(lineUid.get(itemPosition));
			tvStartTime.setText(getString(R.string.default_time));
			tvEndTime.setText(getString(R.string.default_time));
			if(mShowInMap != null) {
	            mShowInMap.setVisible(false);
	            mSensorData.setVisible(false);
			}
		}
		return false;
	}
}
