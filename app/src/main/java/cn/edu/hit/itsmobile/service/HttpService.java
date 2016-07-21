package cn.edu.hit.itsmobile.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Newsoul on 2016-07-19.
 */
public class HttpService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        FileUploadManager fileUploadManager = new FileUploadManager();
        String message = intent.getStringExtra("customerMessage");
        fileUploadManager.pushData(message);
//        fileUploadManager.
        return super.onStartCommand(intent, flags, startId);
    }
}
