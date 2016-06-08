package cn.edu.hit.itsmobile.ui.widget;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import java.util.List;

import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.listener.BusLocationListener;
import cn.edu.hit.itsmobile.listener.MyLocationListener;
import cn.edu.hit.itsmobile.model.MyLocation;
import cn.edu.hit.itsmobile.model.Transform;
//import cn.edu.hit.itsmobile.model.Location;
import cn.edu.hit.itsmobile.ui.MapFragment;
import cn.edu.hit.itsmobile.ui.SensorDataActivity;

public class BusInfo_Details extends Activity {
    private WebView webView;
    //    private Button button;
    private ImageButton button2;
//    private TextView textView;

//    private String provider;

//    private MyLocationListener myLocationListener;
//    private Context context = getApplicationContext();
//    private BaiduMap baiduMap;

    private LocationClient mLocationClient;
    private BDLocation bdLocation;

    public static double longi;
    public static double latit;

    public static double busLongi;
    public static double busLatit;
    public static int busNumber;

    private LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info__details);

        getActionBar().setDisplayShowTitleEnabled(false);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);
//        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        rootView = (LinearLayout)findViewById(R.id.root_view);

//        rootView.setVisibility(View.GONE);
        rootView.setVisibility(View.VISIBLE);

        webView = (WebView) findViewById(R.id.web_viewInformation);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://123.206.85.17:8080/HttpClient");//191.1/123.206.85.17/192.168.0.119
//        button = (Button)findViewById(R.id.nextWay);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(BusInfo_Details.this, MapFragment.class);
//                intent.putExtra("line", "11");
//                intent.putExtra("number", "下一站点是:");
//                startActivity(intent);
//            }
//        });
        button2 = (ImageButton) findViewById(R.id.btn_update);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });

        /**
         * 展示预计到达时间
         */

        TextView textView = (TextView) findViewById(R.id.textView_show);




//        SDKInitializer.initialize(getApplicationContext());
//
//        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        List<String> providerList = locationManager.getProviders(true);
//        if (providerList.contains(LocationManager.GPS_PROVIDER)){
//            provider = LocationManager.GPS_PROVIDER;
//        }
//        else if (providerList.contains(LocationManager.NETWORK_PROVIDER)){
//            provider = LocationManager.NETWORK_PROVIDER;
//        }else {
//            Toast.makeText(this,"定位功能未开启",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Location location = locationManager.getLastKnownLocation(provider);
//        double longitude  = location.getLongitude();
//
//        locationManager.requestLocationUpdates(provider,5000,1,locationListener);

        /**
         * 正确方法:利用LocationClient
         */
//        final LocationClientOption locOption = new LocationClientOption();
//        locOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        locOption.setCoorType("bd09ll");
//        locOption.setScanSpan(1000);
//        locOption.setIsNeedAddress(true);
//
//        mLocationClient = new LocationClient(getApplicationContext());
//        mLocationClient.setLocOption(locOption);
//        mLocationClient.start();
//        if (mLocationClient != null && mLocationClient.isStarted()) {
//            mLocationClient.requestLocation();//原始函数
//            BDLocation bdLocation = mLocationClient.getLastKnownLocation();
//            latit = bdLocation.getLatitude();
//            longi = bdLocation.getLongitude();

//            BDLocation bdLocation = mLocationClient.getLastKnownLocation();
//            double lat = bdLocation.getLatitude();
//            BusInfo_Details.longi = lat;
//            String lati = Double.toString(lat);
//            Intent intent = new Intent("lat", Uri.parse(lati))

//        }
//        else {
//            Log.d("LocSDK5", "locClient is null or not started");
//        }

//        locationClient.getLocOption();
//        bdLocation = locationClient.getLastKnownLocation();
//        double lat = bdLocation.getLatitude();
//        locationClient.start();
        String str;
        int hour = 0;
        if (busLatit!=0&&latit!=0){
            cn.edu.hit.itsmobile.model.Location mlocation = new cn.edu.hit.itsmobile.model.Location(latit,longi);
            cn.edu.hit.itsmobile.model.Location buslocation = new cn.edu.hit.itsmobile.model.Location(busLatit,busLongi);
            Transform transform = new Transform();
            int time = (int)(transform.timeCalculate(mlocation,buslocation)/60);
            if (time>60){
                hour = time/60;
                time = time%60;
            }
            str = "正常预计到站还需要:"+Integer.toString(hour)+"小时"+Integer.toString(time)+"分";
        }else {
            str = "请进行定位操作";
        }
//        String string = Double.toString(longi);
//        while (locationClient.isStarted()) {
//            BDLocation bdLocation = locationClient.getLastKnownLocation();
//            string = Double.toString(bdLocation.getLongitude());
//        }

        /**
         * 书中LocationManager方法
         */
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            return;
//        }
//        provider = LocationManager.NETWORK_PROVIDER;
//        Location location = locationManager.getLastKnownLocation(provider);
//        String string = Double.toString(location.getLongitude());

        /**
         * Mylocation方法
         */
//        MyLocation myLocation = MyLocation.newInstance();
//        String string = Double.toString(myLocation.latitude());

        textView.setText(str);



    }
//    LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    };

}
