package com.cnmobi.im.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.cnmobi.im.util.NetworkUtils;

public class NetworkStateReceiver extends BroadcastReceiver {
    private NetworkStateListener networkStateListener;

    public NetworkStateReceiver(NetworkStateListener networkStateListener) {
        this.networkStateListener = networkStateListener;
    }

    public void onReceive(Context context, Intent intent) {
        this.networkStateListener.sendMessage(NetworkUtils.getNetworkInfo(context));
    }
}
