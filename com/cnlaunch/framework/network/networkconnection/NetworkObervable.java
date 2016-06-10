package com.cnlaunch.framework.network.networkconnection;

import android.net.NetworkInfo;
import java.util.LinkedList;
import java.util.List;

public class NetworkObervable {
    private static NetworkObervable instance;
    private List<NetworkObserver> observers;

    public NetworkObervable() {
        this.observers = new LinkedList();
    }

    public static NetworkObervable getInstance() {
        if (instance == null) {
            instance = new NetworkObervable();
        }
        return instance;
    }

    public void addObserver(NetworkObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(NetworkObserver observer) {
        this.observers.remove(observer);
    }

    public void onChanged(NetworkInfo networkInfo) {
        for (NetworkObserver observer : this.observers) {
            if (observer != null) {
                dispatchChange(observer, networkInfo);
            }
        }
    }

    private void dispatchChange(NetworkObserver observer, NetworkInfo networkInfo) {
        if (networkInfo == null) {
            observer.dispatch_noneNetwork(networkInfo);
        } else if (networkInfo.isConnected() && networkInfo.isAvailable()) {
            observer.dispath_Connected(networkInfo);
        } else {
            observer.dispatch_noneNetwork(networkInfo);
        }
    }
}
