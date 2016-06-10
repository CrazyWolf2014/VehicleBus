package com.cnlaunch.framework.network.http;

import java.io.IOException;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.ServiceConnectionSE;

public class SyncHttpTransportSE extends HttpTransportSE {
    public static final int DEFAULTTIMEOUT = 30000;
    public static int timeout;

    static {
        timeout = DEFAULTTIMEOUT;
    }

    public SyncHttpTransportSE(String url) {
        super(url);
    }

    public SyncHttpTransportSE(String url, int timeout) {
        super(url);
        timeout = timeout;
    }

    public ServiceConnection getServiceConnection() throws IOException {
        return new ServiceConnectionSE(this.url, timeout);
    }
}
