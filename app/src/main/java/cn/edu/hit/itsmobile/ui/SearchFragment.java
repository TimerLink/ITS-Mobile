package cn.edu.hit.itsmobile.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.manager.SearchManager;
import cn.edu.hit.itsmobile.manager.SearchManager.OnGetResultListener;
import cn.edu.hit.itsmobile.ui.MainActivity.onSearchQueryListener;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiResult;

public class SearchFragment extends Fragment{
	private SearchManager mSearchManager;
	private ListView searchList;
	private ArrayList<PoiInfo> searchResult;

	@SuppressLint("InflateParams")
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_search, null);
		searchList = (ListView)mView.findViewById(R.id.search_list);
		return mView;
	}


	/**
	 * 搜索公交
	 * @param savedInstanceState
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSearchManager = new SearchManager();
		((MainActivity)getActivity()).setOnSearchQueryListener(new onSearchQueryListener() {
			
			@Override
			public void onQuery(String query) {
				if(query == null || query.equals(""))
					return;
				mSearchManager.searchInCity(query);
			}
		});
		mSearchManager.setOnGetResultListener(new OnGetResultListener() {
			
			@Override
			public void onGet(PoiResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					return;
				}
				searchResult = new ArrayList<PoiInfo>();
				for (PoiInfo poi : result.getAllPoi()) {
					if (poi.type == PoiInfo.POITYPE.BUS_STATION
						|| poi.type == PoiInfo.POITYPE.BUS_LINE) {
						searchResult.add(poi);
					}
				}
				searchList.setAdapter(new SearchResultAdapter());
			}
		});
	}
	
	private class SearchResultAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return (searchResult!=null?searchResult.size():0);
		}

		@Override
		public Object getItem(int position) {
			return (searchResult!=null?searchResult.get(position):null);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			final PoiInfo poi = (PoiInfo)getItem(position);
			if(convertView == null){
				convertView = (ViewGroup)LayoutInflater.from(getActivity()).inflate(R.layout.list_item_search_result, parent, false);
				holder.icon = (ImageView)convertView.findViewById(R.id.icon);
				holder.tvTitle = (TextView)convertView.findViewById(R.id.title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			holder.tvTitle.setText(poi.name);
			if(poi.type == PoiInfo.POITYPE.BUS_LINE) {
				holder.icon.setImageResource(R.drawable.ic_directions);;
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("name", poi.name);
						intent.putExtra("uid", poi.uid);
						intent.setClass(getActivity(), BusLineActivity.class);
						startActivity(intent);
					}
				});
			} else if(poi.type == PoiInfo.POITYPE.BUS_STATION) {
				holder.icon.setImageResource(R.drawable.ic_bus);;
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(getActivity(), BusStationActivity.class);
						intent.putExtra("name", poi.name);
						intent.putExtra("lines", poi.address);
						intent.putExtra("latitude", poi.location.latitude);
						intent.putExtra("longitude", poi.location.longitude);
						getActivity().startActivity(intent);
					}
				});
			}
			return convertView;
		}
		
		public class ViewHolder{
			ImageView icon;
			TextView tvTitle;
		}
		
	}

}
