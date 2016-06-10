package com.ifoer.util;

import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import java.io.IOException;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;

public class MyAndroidHttpTransport extends HttpTransportSE {
    private int timeout;

    public MyAndroidHttpTransport(String url) {
        super(url);
        this.timeout = SyncHttpTransportSE.DEFAULTTIMEOUT;
    }

    public MyAndroidHttpTransport(String url, int timeout) {
        super(url);
        this.timeout = SyncHttpTransportSE.DEFAULTTIMEOUT;
        if (timeout <= SyncHttpTransportSE.DEFAULTTIMEOUT) {
            this.timeout = SyncHttpTransportSE.DEFAULTTIMEOUT;
        } else {
            this.timeout = timeout;
        }
    }

    public ServiceConnection getServiceConnection() throws IOException {
        return new ServiceConnectionSE(this.url, this.timeout);
    }
}
