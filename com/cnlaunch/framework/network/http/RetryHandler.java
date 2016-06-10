package com.cnlaunch.framework.network.http;

import android.os.SystemClock;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import javax.net.ssl.SSLException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

class RetryHandler implements HttpRequestRetryHandler {
    private static HashSet<Class<?>> exceptionBlacklist;
    private static HashSet<Class<?>> exceptionWhitelist;
    private final int maxRetries;
    private final int retrySleepTimeMS;

    static {
        exceptionWhitelist = new HashSet();
        exceptionBlacklist = new HashSet();
        exceptionWhitelist.add(NoHttpResponseException.class);
        exceptionWhitelist.add(UnknownHostException.class);
        exceptionWhitelist.add(SocketException.class);
        exceptionBlacklist.add(InterruptedIOException.class);
        exceptionBlacklist.add(SSLException.class);
    }

    public RetryHandler(int maxRetries, int retrySleepTimeMS) {
        this.maxRetries = maxRetries;
        this.retrySleepTimeMS = retrySleepTimeMS;
    }

    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        boolean sent;
        boolean retry = true;
        Boolean b = (Boolean) context.getAttribute("http.request_sent");
        if (b == null || !b.booleanValue()) {
            sent = false;
        } else {
            sent = true;
        }
        if (executionCount > this.maxRetries) {
            retry = false;
        } else if (isInList(exceptionBlacklist, exception)) {
            retry = false;
        } else if (isInList(exceptionWhitelist, exception)) {
            retry = true;
        } else if (!sent) {
            retry = true;
        }
        if (retry) {
            HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute("http.request");
            if (currentReq == null) {
                return false;
            }
            if (currentReq.getMethod().equals("POST")) {
                retry = false;
            } else {
                retry = true;
            }
        }
        if (retry) {
            SystemClock.sleep((long) this.retrySleepTimeMS);
        } else {
            exception.printStackTrace();
        }
        return retry;
    }

    protected boolean isInList(HashSet<Class<?>> list, Throwable error) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (((Class) it.next()).isInstance(error)) {
                return true;
            }
        }
        return false;
    }
}
