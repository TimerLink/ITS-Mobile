package cn.edu.hit.itsmobile;

import com.baidu.mapapi.SDKInitializer;

public class Application extends android.app.Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
        SDKInitializer.initialize(this);
	}

}
