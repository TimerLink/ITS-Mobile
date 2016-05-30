package cn.edu.hit.itsmobile.manager;

import cn.edu.hit.itsmobile.model.MyLocation;
import cn.edu.hit.itsmobile.model.RuntimeParams;

import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

public class SearchManager {
    private MyLocation location;
	private PoiSearch mPoiSearch;
    private BusLineSearch mBusLineSearch;
	
	private OnGetResultListener mOnGetResultListener;
    private OnGetBusLineListener mOnGetBusLineListener;
	
	public SearchManager(){
	    location = MyLocation.newInstance();
		mPoiSearch = PoiSearch.newInstance();
        mBusLineSearch = BusLineSearch.newInstance();

		mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener(){

            public void onGetPoiResult(PoiResult result){
                if(mOnGetResultListener != null)
                    mOnGetResultListener.onGet(result);
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {
            }
        });
        mBusLineSearch.setOnGetBusLineSearchResultListener(new OnGetBusLineSearchResultListener() {
            
            @Override
            public void onGetBusLineResult(BusLineResult result) {
                if(mOnGetBusLineListener !=  null)
                    mOnGetBusLineListener.onGet(result);
            }
        });
	}
	
	public void setOnGetResultListener(OnGetResultListener listener) {
		this.mOnGetResultListener = listener;
	}
    
    public void setOnGetBusLineListener(OnGetBusLineListener listener){
        this.mOnGetBusLineListener = listener;
    }
	
	public void searchInCity(String keyword) {
	    if(location != null && location.city() != null)
	        mPoiSearch.searchInCity(new PoiCitySearchOption().city(location.city()).keyword(keyword));
	}
    
    public void searchNearbyBusStation() {
        if(location != null && location.location() != null)
            mPoiSearch.searchNearby(
                    new PoiNearbySearchOption()
                    .keyword("����")
                    .location(location.location())
                    .radius(RuntimeParams.newInstance().nearbySearchRadius())
                    .pageCapacity(20));
    }
	
    public void getBusLineInfo(String uid) {
        if(location != null && location.city() != null)
            mBusLineSearch.searchBusLine((new BusLineSearchOption().city(location.city()).uid(uid)));
    }
    
    public void getPoiDetail(String uid) {
        mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(uid));
    }

	public interface OnGetResultListener {
		public void onGet(PoiResult result);
	}

    public interface OnGetBusLineListener{
        public void onGet(BusLineResult result);
    }
}
