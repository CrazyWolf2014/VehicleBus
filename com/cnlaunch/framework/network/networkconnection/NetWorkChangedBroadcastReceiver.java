package com.cnlaunch.framework.network.networkconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetWorkChangedBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "NetWorkChangedBroadcastReceiver";

    public void onReceive(Context context, Intent intent) {
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            NetworkObervable.getInstance().onChanged(((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo());
        }
    }
}
