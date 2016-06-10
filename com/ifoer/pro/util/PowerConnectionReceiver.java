package com.ifoer.pro.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class PowerConnectionReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.i("PowerConnectionReceiver", "\u7535\u6c60\u76d1\u542c");
        int level = intent.getIntExtra("level", -1);
        int scale = intent.getIntExtra("scale", -1);
        Toast.makeText(context, "\u7535\u6c60\u76d1\u542c", 1).show();
        float batteryPct = ((float) level) / ((float) scale);
    }
}
