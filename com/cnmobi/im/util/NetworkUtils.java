package com.cnmobi.im.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

public class NetworkUtils {
    public static final int ALLACTIVE = 3;
    public static final int MOBILEACTIVE = 2;
    public static final int NONETWORK = 9;
    public static final int WIFIACTIVE = 1;

    public static int getNetworkInfo(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getApplicationContext().getSystemService("connectivity");
        State wifi = connectivity.getNetworkInfo(WIFIACTIVE).getState();
        State mobile = null;
        try {
            mobile = connectivity.getNetworkInfo(0).getState();
        } catch (Exception e) {
        }
        if (wifi == State.CONNECTED && State.CONNECTED == mobile) {
            return ALLACTIVE;
        }
        if (wifi == State.CONNECTED) {
            return WIFIACTIVE;
        }
        if (State.CONNECTED == mobile) {
            return MOBILEACTIVE;
        }
        return NONETWORK;
    }
}
