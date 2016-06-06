package cn.edu.hit.itsmobile.ui.widget;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.ui.MapFragment;
import cn.edu.hit.itsmobile.ui.SensorDataActivity;

public class BusInfo_Details extends Activity {
    private WebView webView;
//    private Button button;
    private Button button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info__details);

        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        webView = (WebView)findViewById(R.id.web_viewInformation);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://192.168.0.119:8080/HttpClient");//191.1
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
        button2 = (Button)findViewById(R.id.btn_update);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });


    }

}
