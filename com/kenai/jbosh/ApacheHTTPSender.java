package com.kenai.jbosh;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

final class ApacheHTTPSender implements HTTPSender {
    private BOSHClientConfig cfg;
    private HttpClient httpClient;
    private final Lock lock;

    ApacheHTTPSender() {
        this.lock = new ReentrantLock();
        HttpClient.class.getName();
    }

    public void init(BOSHClientConfig bOSHClientConfig) {
        this.lock.lock();
        try {
            this.cfg = bOSHClientConfig;
            this.httpClient = initHttpClient(bOSHClientConfig);
        } finally {
            this.lock.unlock();
        }
    }

    public void destroy() {
        this.lock.lock();
        try {
            if (this.httpClient != null) {
                this.httpClient.getConnectionManager().shutdown();
            }
            this.cfg = null;
            this.httpClient = null;
            this.lock.unlock();
        } catch (Throwable th) {
            this.cfg = null;
            this.httpClient = null;
            this.lock.unlock();
        }
    }

    public HTTPResponse send(CMSessionParams cMSessionParams, AbstractBody abstractBody) {
        BOSHClientConfig bOSHClientConfig;
        this.lock.lock();
        try {
            if (this.httpClient == null) {
                this.httpClient = initHttpClient(this.cfg);
            }
            HttpClient httpClient = this.httpClient;
            bOSHClientConfig = this.cfg;
            return new ApacheHTTPResponse(httpClient, bOSHClientConfig, cMSessionParams, abstractBody);
        } finally {
            bOSHClientConfig = this.lock;
            bOSHClientConfig.unlock();
        }
    }

    private synchronized HttpClient initHttpClient(BOSHClientConfig bOSHClientConfig) {
        HttpParams basicHttpParams;
        SchemeRegistry schemeRegistry;
        basicHttpParams = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(basicHttpParams, 100);
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUseExpectContinue(basicHttpParams, false);
        if (!(bOSHClientConfig == null || bOSHClientConfig.getProxyHost() == null || bOSHClientConfig.getProxyPort() == 0)) {
            basicHttpParams.setParameter("http.route.default-proxy", new HttpHost(bOSHClientConfig.getProxyHost(), bOSHClientConfig.getProxyPort()));
        }
        schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        SocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        schemeRegistry.register(new Scheme("https", socketFactory, 443));
        return new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
    }
}
