package cn.edu.hit.itsmobile;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import cn.edu.hit.itsmobile.model.MyLocation;
import cn.edu.hit.itsmobile.ui.BusLineActivity;
import cn.edu.hit.itsmobile.ui.SearchFragment;

public class UnitTest {
    private Context mContext;
    private MyLocation location;
    
    public UnitTest(Context context) {
        mContext = context;
        location = MyLocation.newInstance();
        location.setCity("哈尔滨");
    }
    
    public void run() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mContext != null) {
                    Intent intent = new Intent(mContext, BusLineActivity.class);
                    intent.putExtra("line", "11·");
                    mContext.startActivity(intent);
                }
            }
        }, 1000);
    }
}
