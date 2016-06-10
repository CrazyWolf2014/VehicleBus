package org.ksoap2.transport;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

public class HttpsTransportSE extends HttpTransportSE {
    static final String PROTOCOL = "https";
    private static final String PROTOCOL_FULL = "https://";
    protected final String file;
    protected final String host;
    protected final int port;
    private ServiceConnection serviceConnection;

    public HttpsTransportSE(String host, int port, String file, int timeout) {
        super(new StringBuilder(PROTOCOL_FULL).append(host).append(":").append(port).append(file).toString());
        this.serviceConnection = null;
        this.host = host;
        this.port = port;
        this.file = file;
        this.timeout = timeout;
    }

    public HttpsTransportSE(Proxy proxy, String host, int port, String file, int timeout) {
        super(proxy, new StringBuilder(PROTOCOL_FULL).append(host).append(":").append(port).append(file).toString());
        this.serviceConnection = null;
        this.host = host;
        this.port = port;
        this.file = file;
        this.timeout = timeout;
    }

    public ServiceConnection getServiceConnection() throws IOException {
        if (this.serviceConnection == null) {
            this.serviceConnection = new HttpsServiceConnectionSE(this.proxy, this.host, this.port, this.file, this.timeout);
        }
        return this.serviceConnection;
    }

    public String getHost() {
        String retVal = null;
        try {
            retVal = new URL(this.url).getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public int getPort() {
        int retVal = -1;
        try {
            retVal = new URL(this.url).getPort();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public String getPath() {
        String retVal = null;
        try {
            retVal = new URL(this.url).getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retVal;
    }
}
