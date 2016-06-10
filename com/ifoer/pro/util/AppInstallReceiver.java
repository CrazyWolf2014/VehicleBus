package com.ifoer.pro.util;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.ifoer.pro.expeditionphone.WelcomeActivity;

public class AppInstallReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getData().getSchemeSpecificPart().equals("com.ifoer.expedition.crp229")) {
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
                intent.getData().getSchemeSpecificPart();
            }
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
                intent.getData().getSchemeSpecificPart();
            }
            if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
                String packageName = intent.getData().getSchemeSpecificPart();
                Intent intent1 = new Intent(context, WelcomeActivity.class);
                intent1.addFlags(268435456);
                context.startActivity(intent1);
                ((ActivityManager) context.getSystemService("activity")).killBackgroundProcesses("com.android.packageinstaller");
            }
        }
    }
}
