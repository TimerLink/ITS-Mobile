package cn.edu.hit.itsmobile.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.model.RuntimeParams;

import com.alibaba.fastjson.JSON;

public class StatisticsActivity extends Activity{

    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        RuntimeParams mRuntimeParams = RuntimeParams.newInstance();
        
        WebView webView = (WebView) findViewById(R.id.webView);
        
        webView.setHorizontalScrollBarEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
 
        WebSettings settings = webView.getSettings();
        settings.setPluginState(PluginState.ON);

        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.addJavascriptInterface(new JSinterface(JSON.toJSONString(mRuntimeParams.sensorPackets())), "androidObject");

        String url = "file:///android_asset/chart/index.html";
        webView.loadUrl(url);
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
