package com.cnlaunch.framework.network.http;

import com.cnlaunch.framework.utils.NLog;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

class AsyncHttpRequest implements Runnable {
    private final AbstractHttpClient client;
    private final HttpContext context;
    private int executionCount;
    private final HttpUriRequest request;
    private final ResponseHandlerInterface responseHandler;
    private final String tag;

    public AsyncHttpRequest(AbstractHttpClient client, HttpContext context, HttpUriRequest request, ResponseHandlerInterface responseHandler) {
        this.tag = AsyncHttpRequest.class.getSimpleName();
        this.client = client;
        this.context = context;
        this.request = request;
        this.responseHandler = responseHandler;
        if (this.responseHandler instanceof BreakpointHttpResponseHandler) {
            BreakpointHttpResponseHandler breakpointHandler = this.responseHandler;
            File tempFile = breakpointHandler.getTempFile();
            if (breakpointHandler.getEnableBreakpoint() && tempFile.exists()) {
                long previousFileSize = tempFile.length();
                NLog.m917e(this.tag, "previousFileSized: " + previousFileSize);
                this.request.setHeader("RANGE", "bytes=" + previousFileSize + "-");
            }
        }
    }

    public void run() {
        if (this.responseHandler != null) {
            this.responseHandler.sendStartMessage();
        }
        try {
            makeRequestWithRetries();
        } catch (IOException e) {
            if (this.responseHandler != null) {
                this.responseHandler.sendFailureMessage(0, null, null, e);
            }
        }
        if (this.responseHandler != null) {
            this.responseHandler.sendFinishMessage();
        }
    }

    private void makeRequest() throws IOException {
        if (!Thread.currentThread().isInterrupted()) {
            if (this.request.getURI().getScheme() == null) {
                throw new MalformedURLException("No valid URI scheme was provided");
            }
            HttpResponse response = this.client.execute(this.request, this.context);
            if (!Thread.currentThread().isInterrupted() && this.responseHandler != null) {
                this.responseHandler.sendResponseMessage(response);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void makeRequestWithRetries() throws java.io.IOException {
        /*
        r7 = this;
        r3 = 1;
        r0 = 0;
        r5 = r7.client;
        r4 = r5.getHttpRequestRetryHandler();
        r1 = r0;
    L_0x0009:
        if (r3 != 0) goto L_0x000d;
    L_0x000b:
        r0 = r1;
    L_0x000c:
        throw r0;
    L_0x000d:
        r7.makeRequest();	 Catch:{ UnknownHostException -> 0x0011, NullPointerException -> 0x004c, IOException -> 0x0072 }
        return;
    L_0x0011:
        r2 = move-exception;
        r0 = new java.io.IOException;	 Catch:{ Exception -> 0x0081 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0081 }
        r6 = "UnknownHostException exception: ";
        r5.<init>(r6);	 Catch:{ Exception -> 0x0081 }
        r6 = r2.getMessage();	 Catch:{ Exception -> 0x0081 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0081 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0081 }
        r0.<init>(r5);	 Catch:{ Exception -> 0x0081 }
        r5 = r7.executionCount;	 Catch:{ Exception -> 0x00a4 }
        if (r5 <= 0) goto L_0x004a;
    L_0x002e:
        r5 = r7.executionCount;	 Catch:{ Exception -> 0x00a4 }
        r5 = r5 + 1;
        r7.executionCount = r5;	 Catch:{ Exception -> 0x00a4 }
        r6 = r7.context;	 Catch:{ Exception -> 0x00a4 }
        r5 = r4.retryRequest(r0, r5, r6);	 Catch:{ Exception -> 0x00a4 }
        if (r5 == 0) goto L_0x004a;
    L_0x003c:
        r3 = 1;
    L_0x003d:
        if (r3 == 0) goto L_0x0048;
    L_0x003f:
        r5 = r7.responseHandler;	 Catch:{ Exception -> 0x00a4 }
        if (r5 == 0) goto L_0x0048;
    L_0x0043:
        r5 = r7.responseHandler;	 Catch:{ Exception -> 0x00a4 }
        r5.sendRetryMessage();	 Catch:{ Exception -> 0x00a4 }
    L_0x0048:
        r1 = r0;
        goto L_0x0009;
    L_0x004a:
        r3 = 0;
        goto L_0x003d;
    L_0x004c:
        r2 = move-exception;
        r0 = new java.io.IOException;	 Catch:{ Exception -> 0x0081 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0081 }
        r6 = "NPE in HttpClient: ";
        r5.<init>(r6);	 Catch:{ Exception -> 0x0081 }
        r6 = r2.getMessage();	 Catch:{ Exception -> 0x0081 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0081 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0081 }
        r0.<init>(r5);	 Catch:{ Exception -> 0x0081 }
        r5 = r7.executionCount;	 Catch:{ Exception -> 0x00a4 }
        r5 = r5 + 1;
        r7.executionCount = r5;	 Catch:{ Exception -> 0x00a4 }
        r6 = r7.context;	 Catch:{ Exception -> 0x00a4 }
        r3 = r4.retryRequest(r0, r5, r6);	 Catch:{ Exception -> 0x00a4 }
        goto L_0x003d;
    L_0x0072:
        r2 = move-exception;
        r0 = r2;
        r5 = r7.executionCount;	 Catch:{ Exception -> 0x00a4 }
        r5 = r5 + 1;
        r7.executionCount = r5;	 Catch:{ Exception -> 0x00a4 }
        r6 = r7.context;	 Catch:{ Exception -> 0x00a4 }
        r3 = r4.retryRequest(r0, r5, r6);	 Catch:{ Exception -> 0x00a4 }
        goto L_0x003d;
    L_0x0081:
        r2 = move-exception;
        r0 = r1;
    L_0x0083:
        r5 = "AsyncHttpRequest";
        r6 = "Unhandled exception origin cause";
        android.util.Log.e(r5, r6, r2);
        r0 = new java.io.IOException;
        r5 = new java.lang.StringBuilder;
        r6 = "Unhandled exception: ";
        r5.<init>(r6);
        r6 = r2.getMessage();
        r5 = r5.append(r6);
        r5 = r5.toString();
        r0.<init>(r5);
        goto L_0x000c;
    L_0x00a4:
        r2 = move-exception;
        goto L_0x0083;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cnlaunch.framework.network.http.AsyncHttpRequest.makeRequestWithRetries():void");
    }
}
