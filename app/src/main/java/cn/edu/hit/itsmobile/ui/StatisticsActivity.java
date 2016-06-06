package cn.edu.hit.itsmobile.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.model.RuntimeParams;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatisticsActivity extends Activity{

    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);
//
//        RuntimeParams mRuntimeParams = RuntimeParams.newInstance();
//
//        WebView webView = (WebView) findViewById(R.id.webView);
//
//        webView.setHorizontalScrollBarEnabled(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
//
//        WebSettings settings = webView.getSettings();
//        settings.setPluginState(PluginState.ON);
//
//        settings.setJavaScriptEnabled(true);
//        settings.setSupportZoom(false);
//        settings.setBuiltInZoomControls(false);
//        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        webView.addJavascriptInterface(new JSinterface(JSON.toJSONString(mRuntimeParams.sensorPackets())), "androidObject");
//
//        String url = "http://www.baidu.com";//http://192.168.0.119:8080/HttpClient?l=120&b=21
//        webView.loadUrl(url);
//        String url = "file:///android_asset/chart/index.html";
        /**
         * debug
         */
//        WebView webView = (WebView) findViewById(R.id.webView);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("http://www.baidu.com");
//        try {
//            // 得到Json解析成功之后数据
////            persons = JsonParse.getListPerson(urlPath);
////            List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
////            for (int i = 0; i < persons.size(); i++) {
////                HashMap<String, Object> map = new HashMap<String, Object>();
////                map.put("id", persons.get(i).getName());
////                map.put("number", persons.get(i).getAddress());
////                data.add(map);
////            }
//            //初始化适配器，并且绑定数据
//            SimpleAdapter _Adapter = new SimpleAdapter(MainActivity.this,
//                    data, R.layout.activity_statistics, new String[] { "number",
//                    "id" }, new int[] { R.id.number,
//                    R.id.id });
//            mListView.setAdapter(_Adapter);
//        } catch (Exception e) {
//            Log.i(TAG, e.toString());
//
//        }
        Intent intent = getIntent();
        String dataNum = intent.getStringExtra("extraData");
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", 1);
        map.put("number",dataNum);
        data.add(map);
        ListView mListView = (ListView)findViewById(R.id.statisticslist_view);
        SimpleAdapter _Adapter = new SimpleAdapter(StatisticsActivity.this,data
                    , R.layout.activity_statistics, new String[] { "number",
                    "id" }, new int[] { R.id.number,
                    R.id.id });
            mListView.setAdapter(_Adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case android.R.id.home:
            finish();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class JSinterface {
        private String mData;
     
        public JSinterface(String data) {
            mData = data;
        }
        
        @JavascriptInterface
        public String getData() {
            return mData;
        }
    }

}
