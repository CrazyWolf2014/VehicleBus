package com.ifoer.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ksoap2.HeaderProperty;
import org.ksoap2.transport.ServiceConnection;

public class ServiceConnectionSE implements ServiceConnection {
    private HttpURLConnection connection;

    public ServiceConnectionSE(String url, int timeout) throws IOException {
        this.connection = (HttpURLConnection) new URL(url).openConnection();
        this.connection.setUseCaches(false);
        this.connection.setDoOutput(true);
        this.connection.setDoInput(true);
        this.connection.setConnectTimeout(timeout);
        this.connection.setReadTimeout(timeout);
    }

    public void connect() throws IOException {
        this.connection.connect();
    }

    public void disconnect() {
        this.connection.disconnect();
    }

    public void setRequestProperty(String string, String soapAction) {
        this.connection.setRequestProperty(string, soapAction);
    }

    public void setRequestMethod(String requestMethod) throws IOException {
        this.connection.setRequestMethod(requestMethod);
    }

    public OutputStream openOutputStream() throws IOException {
        return this.connection.getOutputStream();
    }

    public InputStream openInputStream() throws IOException {
        return this.connection.getInputStream();
    }

    public InputStream getErrorStream() {
        return this.connection.getErrorStream();
    }

    public void setConnectionTimeOut(int timeout) {
        this.connection.setConnectTimeout(timeout);
    }

    public String getHost() {
        return this.connection.getURL().getHost();
    }

    public int getPort() {
        return this.connection.getURL().getPort();
    }

    public String getPath() {
        return this.connection.getURL().getPath();
    }

    public List getResponseProperties() {
        Map properties = this.connection.getHeaderFields();
        Set<String> keys = properties.keySet();
        List retList = new LinkedList();
        for (String key : keys) {
            List values = (List) properties.get(key);
            for (int j = 0; j < values.size(); j++) {
                retList.add(new HeaderProperty(key, (String) values.get(j)));
            }
        }
        return retList;
    }

    public void setFixedLengthStreamingMode(int contentLength) {
        this.connection.setFixedLengthStreamingMode(contentLength);
    }
}
