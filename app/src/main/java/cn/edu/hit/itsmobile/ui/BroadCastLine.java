package cn.edu.hit.itsmobile.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadCastLine extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String line = intent.getStringExtra("line");
        String name = intent.getStringExtra("name");
        int lineNumber = Integer.valueOf(line);

    }
}
