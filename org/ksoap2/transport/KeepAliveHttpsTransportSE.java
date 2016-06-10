package org.ksoap2.transport;

import java.io.IOException;

public class KeepAliveHttpsTransportSE extends HttpsTransportSE {
    private ServiceConnection serviceConnection;

    public KeepAliveHttpsTransportSE(String host, int port, String file, int timeout) {
        super(host, port, file, timeout);
        this.timeout = timeout;
    }

    public ServiceConnection getServiceConnection() throws IOException {
        if (this.serviceConnection == null) {
            this.serviceConnection = new HttpsServiceConnectionSEIgnoringConnectionClose(this.host, this.port, this.file, this.timeout);
            this.serviceConnection.setRequestProperty("Connection", "keep-alive");
        }
        return this.serviceConnection;
    }
}
