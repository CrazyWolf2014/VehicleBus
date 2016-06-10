package com.ifoer.expedition.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.ifoer.entity.Constant;

public class ConnectivityReceiver extends BroadcastReceiver {
    private static final String LOGTAG = "ConnectivityReceiver";
    private NotificationService notificationService;

    public ConnectivityReceiver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void onReceive(Context context, Intent intent) {
        Log.d(LOGTAG, "ConnectivityReceiver.onReceive()...");
        Log.d(LOGTAG, new StringBuilder(Constant.ACTION_REGEX).append(intent.getAction()).toString());
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo != null) {
            Log.d(LOGTAG, "Network Type  = " + networkInfo.getTypeName());
            Log.d(LOGTAG, "Network State = " + networkInfo.getState());
            if (networkInfo.isConnected()) {
                Log.i(LOGTAG, "Network connected");
                this.notificationService.connect();
                return;
            }
            return;
        }
        Log.e(LOGTAG, "Network unavailable");
        this.notificationService.disconnect();
    }
}
