package com.ifoer.pro.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.ifoer.pro.expeditionphone.WelcomeActivity;

public class UpdateOverReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent arg1) {
        Log.e("zdy", "\u5b89\u88c5\u5b8c\u542f\u52a8");
        Intent intent1 = new Intent(context, WelcomeActivity.class);
        intent1.addFlags(268435456);
        context.startActivity(intent1);
    }
}
