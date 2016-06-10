package com.ifoer.mine;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

class HttpClientManager {
    public static int contectTimeout;
    public static HttpClient httpClient;
    public static int socketTimeout;

    HttpClientManager() {
    }

    static {
        contectTimeout = SyncHttpTransportSE.DEFAULTTIMEOUT;
        socketTimeout = 60000;
    }

    public static void createHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, AsyncHttpResponseHandler.DEFAULT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpConnectionParams.setConnectionTimeout(params, contectTimeout);
        HttpConnectionParams.setSoTimeout(params, socketTimeout);
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, schReg), params);
    }

    public static void shutdownHttpClient() {
        if (httpClient != null && httpClient.getConnectionManager() != null) {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public static HttpClient getHttpClient() {
        if (httpClient == null) {
            createHttpClient();
        }
        return httpClient;
    }
}
