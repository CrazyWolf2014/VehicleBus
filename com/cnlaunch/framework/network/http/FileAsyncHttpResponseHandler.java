package com.cnlaunch.framework.network.http;

import android.content.Context;
import android.util.Log;
import java.io.File;
import org.apache.http.Header;

public class FileAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String LOG_TAG = "FileAsyncHttpResponseHandler";
    private File mFile;

    static {
        $assertionsDisabled = !FileAsyncHttpResponseHandler.class.desiredAssertionStatus() ? true : $assertionsDisabled;
    }

    public FileAsyncHttpResponseHandler(File file) {
        if ($assertionsDisabled || file != null) {
            this.mFile = file;
            return;
        }
        throw new AssertionError();
    }

    public FileAsyncHttpResponseHandler(Context c) {
        if ($assertionsDisabled || c != null) {
            this.mFile = getTemporaryFile(c);
            return;
        }
        throw new AssertionError();
    }

    protected File getTemporaryFile(Context c) {
        try {
            return File.createTempFile("temp_", "_handled", c.getCacheDir());
        } catch (Throwable t) {
            Log.e(LOG_TAG, "Cannot create temporary file", t);
            return null;
        }
    }

    protected File getTargetFile() {
        if ($assertionsDisabled || this.mFile != null) {
            return this.mFile;
        }
        throw new AssertionError();
    }

    public void onSuccess(File file) {
    }

    public void onSuccess(int statusCode, File file) {
        onSuccess(file);
    }

    public void onSuccess(int statusCode, Header[] headers, File file) {
        onSuccess(statusCode, file);
    }

    public void onFailure(Throwable e, File response) {
        onFailure(e);
    }

    public void onFailure(int statusCode, Throwable e, File response) {
        onFailure(e, response);
    }

    public void onFailure(int statusCode, Header[] headers, Throwable e, File response) {
        onFailure(statusCode, e, response);
    }

    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        onFailure(statusCode, headers, error, getTargetFile());
    }

    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        onSuccess(statusCode, headers, getTargetFile());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    byte[] getResponseData(org.apache.http.HttpEntity r9) throws java.io.IOException {
        /*
        r8 = this;
        if (r9 == 0) goto L_0x0034;
    L_0x0002:
        r4 = r9.getContent();
        r1 = r9.getContentLength();
        r0 = new java.io.FileOutputStream;
        r7 = r8.getTargetFile();
        r0.<init>(r7);
        if (r4 == 0) goto L_0x0034;
    L_0x0015:
        r7 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r6 = new byte[r7];	 Catch:{ all -> 0x0040 }
        r3 = 0;
    L_0x001a:
        r5 = r4.read(r6);	 Catch:{ all -> 0x0040 }
        r7 = -1;
        if (r5 == r7) goto L_0x002b;
    L_0x0021:
        r7 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0040 }
        r7 = r7.isInterrupted();	 Catch:{ all -> 0x0040 }
        if (r7 == 0) goto L_0x0036;
    L_0x002b:
        r4.close();
        r0.flush();
        r0.close();
    L_0x0034:
        r7 = 0;
        return r7;
    L_0x0036:
        r3 = r3 + r5;
        r7 = 0;
        r0.write(r6, r7, r5);	 Catch:{ all -> 0x0040 }
        r7 = (int) r1;	 Catch:{ all -> 0x0040 }
        r8.sendProgressMessage(r3, r7);	 Catch:{ all -> 0x0040 }
        goto L_0x001a;
    L_0x0040:
        r7 = move-exception;
        r4.close();
        r0.flush();
        r0.close();
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cnlaunch.framework.network.http.FileAsyncHttpResponseHandler.getResponseData(org.apache.http.HttpEntity):byte[]");
    }
}
