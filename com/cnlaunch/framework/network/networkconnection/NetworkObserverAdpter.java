package com.cnlaunch.framework.network.networkconnection;

import android.net.NetworkInfo;
import android.os.Looper;

public class NetworkObserverAdpter extends NetworkObserver {
    public NetworkObserverAdpter(Looper looper) {
        super(looper);
    }

    public void networkIsConnected(NetworkInfo networkInfo) {
    }

    public void networkIsDisconnected(NetworkInfo networkInfo) {
    }
}
