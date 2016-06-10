package com.ifoer.pro.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.ifoer.pro.expeditionphone.WelcomeActivity;

public class BroadIntent extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent intent2 = new Intent(context, WelcomeActivity.class);
            intent2.setAction("android.intent.action.MAIN");
            intent2.addCategory("android.intent.category.LAUNCHER");
            intent2.setFlags(268435456);
            Log.i("BroadIntent", "\u5f00\u673a\u81ea\u542f\u52a8");
            context.startActivity(intent2);
            return;
        }
        Log.i("BroadIntent", "\u975e\u5f00\u673a\u81ea\u542f\u52a8");
    }
}
