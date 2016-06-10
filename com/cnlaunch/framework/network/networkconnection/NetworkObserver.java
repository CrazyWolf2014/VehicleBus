package com.cnlaunch.framework.network.networkconnection;

import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

public abstract class NetworkObserver {
    private Handler mHandler;

    /* renamed from: com.cnlaunch.framework.network.networkconnection.NetworkObserver.1 */
    class C01321 implements Runnable {
        private final /* synthetic */ NetworkInfo val$networkInfo;

        C01321(NetworkInfo networkInfo) {
            this.val$networkInfo = networkInfo;
        }

        public void run() {
            NetworkObserver.this.networkIsDisconnected(this.val$networkInfo);
        }
    }

    /* renamed from: com.cnlaunch.framework.network.networkconnection.NetworkObserver.2 */
    class C01332 implements Runnable {
        private final /* synthetic */ NetworkInfo val$networkInfo;

        C01332(NetworkInfo networkInfo) {
            this.val$networkInfo = networkInfo;
        }

        public void run() {
            NetworkObserver.this.networkIsConnected(this.val$networkInfo);
        }
    }

    public abstract void networkIsConnected(NetworkInfo networkInfo);

    public abstract void networkIsDisconnected(NetworkInfo networkInfo);

    public NetworkObserver(Looper looper) {
        this.mHandler = new Handler(looper);
    }

    protected final void dispatch_noneNetwork(NetworkInfo networkInfo) {
        this.mHandler.post(new C01321(networkInfo));
    }

    protected final void dispath_Connected(NetworkInfo networkInfo) {
        this.mHandler.post(new C01332(networkInfo));
    }
}
