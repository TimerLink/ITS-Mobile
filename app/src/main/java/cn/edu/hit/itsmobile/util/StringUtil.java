package cn.edu.hit.itsmobile.util;

import android.content.Context;
import cn.edu.hit.itsmobile.R;

public class StringUtil {
    
    public static String formatTime(Context context, double time_) {
        if(time_ <= 0) return context.getResources().getString(R.string.already_arrived);
        long time = Math.round(time_), hour = 0;
        int minute = 0, second = 0;
        hour = time / 3600;
        minute = (int) ((time - hour * 3600) / 60);
        second = (int) (time - hour * 3600 - minute * 60);

        String hourStr = context.getResources().getString(R.string.hour);
        String minuteStr = context.getResources().getString(R.string.minute);
        String secondStr = context.getResources().getString(R.string.second);

        return (hour > 0?hour + hourStr:"")
                + (minute > 0?minute + minuteStr:"")
                + (second > 0?second + secondStr:"")
                + context.getResources().getString(R.string.estimate_arrived);
    }
}
