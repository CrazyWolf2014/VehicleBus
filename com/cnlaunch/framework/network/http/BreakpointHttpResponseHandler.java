package com.cnlaunch.framework.network.http;

import android.content.Context;
import android.text.TextUtils;
import com.cnlaunch.framework.network.download.DownloadParam;
import com.cnlaunch.framework.utils.MD5Utils;
import java.io.File;
import java.io.RandomAccessFile;
import org.apache.http.Header;

public class BreakpointHttpResponseHandler extends AsyncHttpResponseHandler {
    private static final String TEMP_SUFFIX = ".download";
    private Context context;
    private long downloadSize;
    private boolean enable_breakpoint;
    private String fileName;
    private long fileSize;
    private boolean interrupt;
    private RequestParams params;
    private long previousFileSize;
    private RandomAccessFile randomAccessFile;
    private final String tag;
    private File targetFile;
    private File tempFile;
    private long totalSize;
    private String url;

    public BreakpointHttpResponseHandler(DownloadParam downloadParam) {
        this.tag = BreakpointHttpResponseHandler.class.getSimpleName();
        this.interrupt = false;
        this.enable_breakpoint = false;
        this.context = downloadParam.getContext();
        this.url = downloadParam.getUrl();
        this.params = downloadParam.getParams();
        this.fileSize = downloadParam.getFileSize();
        this.enable_breakpoint = downloadParam.getEnableBreakpoint();
        if (TextUtils.isEmpty(downloadParam.getFileName())) {
            this.fileName = getFileName(this.url);
        } else {
            this.fileName = downloadParam.getFileName();
        }
        File file = new File(downloadParam.getDownPath());
        if (!file.exists()) {
            file.mkdirs();
        }
        this.targetFile = new File(downloadParam.getDownPath(), this.fileName);
        this.tempFile = new File(downloadParam.getDownPath(), this.fileName + TEMP_SUFFIX);
    }

    public String getFileName(String url) {
        StringBuilder fileName = new StringBuilder(MD5Utils.encrypt(url));
        if (!TextUtils.isEmpty(url) && url.indexOf(".") > 0) {
            fileName.append(url.substring(url.lastIndexOf("."), url.length()));
        }
        return fileName.toString();
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
    public void sendResponseMessage(org.apache.http.HttpResponse r34) {
        /*
        r33 = this;
        r25 = java.lang.Thread.currentThread();
        r25 = r25.isInterrupted();
        if (r25 != 0) goto L_0x00b5;
    L_0x000a:
        r0 = r33;
        r0 = r0.interrupt;
        r25 = r0;
        if (r25 != 0) goto L_0x00b5;
    L_0x0012:
        r12 = 0;
        r15 = 0;
        r19 = r34.getStatusLine();
        r11 = r34.getEntity();
        r14 = r34.getAllHeaders();
        r0 = r14.length;
        r26 = r0;
        r25 = 0;
    L_0x0025:
        r0 = r25;
        r1 = r26;
        if (r0 < r1) goto L_0x00b6;
    L_0x002b:
        r0 = r33;
        r0 = r0.tag;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r26 = 1;
        r0 = r26;
        r0 = new java.lang.Object[r0];	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = r0;
        r27 = 0;
        r28 = new java.lang.StringBuilder;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = "download fileName: ";
        r28.<init>(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r33;
        r0 = r0.fileName;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = r0;
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r28 = r28.toString();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26[r27] = r28;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        com.cnlaunch.framework.utils.NLog.m916d(r25, r26);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        if (r11 != 0) goto L_0x0169;
    L_0x0057:
        r25 = new java.io.IOException;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = "Fail download. entity is null.";
        r25.<init>(r26);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        throw r25;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
    L_0x005f:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ all -> 0x03a0 }
        r12 = r10;
        if (r15 == 0) goto L_0x0069;
    L_0x0066:
        r15.close();	 Catch:{ IOException -> 0x04a5 }
    L_0x0069:
        r0 = r33;
        r0 = r0.randomAccessFile;	 Catch:{ IOException -> 0x04a5 }
        r25 = r0;
        if (r25 == 0) goto L_0x007a;
    L_0x0071:
        r0 = r33;
        r0 = r0.randomAccessFile;	 Catch:{ IOException -> 0x04a5 }
        r25 = r0;
        r25.close();	 Catch:{ IOException -> 0x04a5 }
    L_0x007a:
        r25 = java.lang.Thread.currentThread();
        r25 = r25.isInterrupted();
        if (r25 != 0) goto L_0x00b5;
    L_0x0084:
        r0 = r33;
        r0 = r0.interrupt;
        r25 = r0;
        if (r25 != 0) goto L_0x00b5;
    L_0x008c:
        r25 = r19.getStatusCode();
        r26 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        r0 = r25;
        r1 = r26;
        if (r0 >= r1) goto L_0x009a;
    L_0x0098:
        if (r12 == 0) goto L_0x04d2;
    L_0x009a:
        r25 = r19.getStatusCode();
        r26 = r34.getAllHeaders();
        r27 = r12.getMessage();
        r27 = r27.getBytes();
        r0 = r33;
        r1 = r25;
        r2 = r26;
        r3 = r27;
        r0.sendFailureMessage(r1, r2, r3, r12);
    L_0x00b5:
        return;
    L_0x00b6:
        r13 = r14[r25];
        r27 = r13.getName();
        r28 = "code";
        r27 = r27.equalsIgnoreCase(r28);
        if (r27 == 0) goto L_0x010c;
    L_0x00c4:
        r27 = r13.getValue();
        r28 = "-1";
        r27 = r27.equals(r28);
        if (r27 == 0) goto L_0x010c;
    L_0x00d0:
        r25 = r19.getStatusCode();
        r26 = r34.getAllHeaders();
        r27 = "Token is invalid!";
        r27 = r27.getBytes();
        r28 = new org.apache.http.client.HttpResponseException;
        r29 = -1;
        r30 = "Token is invalid!";
        r28.<init>(r29, r30);
        r0 = r33;
        r1 = r25;
        r2 = r26;
        r3 = r27;
        r4 = r28;
        r0.sendFailureMessage(r1, r2, r3, r4);
        r0 = r33;
        r0 = r0.tag;
        r25 = r0;
        r26 = 1;
        r0 = r26;
        r0 = new java.lang.Object[r0];
        r26 = r0;
        r27 = 0;
        r28 = "Token is invalid!";
        r26[r27] = r28;
        com.cnlaunch.framework.utils.NLog.m916d(r25, r26);
        goto L_0x00b5;
    L_0x010c:
        r27 = r13.getName();
        r28 = "Content-Type";
        r27 = r27.equalsIgnoreCase(r28);
        if (r27 == 0) goto L_0x0165;
    L_0x0118:
        r24 = r13.getValue();
        r27 = "text/html";
        r0 = r24;
        r1 = r27;
        r27 = r0.contains(r1);
        if (r27 == 0) goto L_0x0165;
    L_0x0128:
        r25 = r19.getStatusCode();
        r26 = r34.getAllHeaders();
        r27 = "Content-Type: text/html";
        r27 = r27.getBytes();
        r28 = new org.apache.http.client.HttpResponseException;
        r29 = -2;
        r30 = "Content-Type is not right! Content-Type: text/html";
        r28.<init>(r29, r30);
        r0 = r33;
        r1 = r25;
        r2 = r26;
        r3 = r27;
        r4 = r28;
        r0.sendFailureMessage(r1, r2, r3, r4);
        r0 = r33;
        r0 = r0.tag;
        r25 = r0;
        r26 = 1;
        r0 = r26;
        r0 = new java.lang.Object[r0];
        r26 = r0;
        r27 = 0;
        r28 = "Content-Type is not right! Content-Type: text/html";
        r26[r27] = r28;
        com.cnlaunch.framework.utils.NLog.m916d(r25, r26);
        goto L_0x00b5;
    L_0x0165:
        r25 = r25 + 1;
        goto L_0x0025;
    L_0x0169:
        r6 = r11.getContentLength();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = 0;
        r25 = (r6 > r25 ? 1 : (r6 == r25 ? 0 : -1));
        if (r25 > 0) goto L_0x0183;
    L_0x0173:
        r0 = r33;
        r0 = r0.fileSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r27 = 0;
        r25 = (r25 > r27 ? 1 : (r25 == r27 ? 0 : -1));
        if (r25 <= 0) goto L_0x01b5;
    L_0x017f:
        r0 = r33;
        r6 = r0.fileSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
    L_0x0183:
        r15 = r11.getContent();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        if (r15 != 0) goto L_0x01f0;
    L_0x0189:
        r25 = new java.io.IOException;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = "Fail download. instream is null.";
        r25.<init>(r26);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        throw r25;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
    L_0x0191:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ all -> 0x03a0 }
        r12 = r10;
        if (r15 == 0) goto L_0x019b;
    L_0x0198:
        r15.close();	 Catch:{ IOException -> 0x01ae }
    L_0x019b:
        r0 = r33;
        r0 = r0.randomAccessFile;	 Catch:{ IOException -> 0x01ae }
        r25 = r0;
        if (r25 == 0) goto L_0x007a;
    L_0x01a3:
        r0 = r33;
        r0 = r0.randomAccessFile;	 Catch:{ IOException -> 0x01ae }
        r25 = r0;
        r25.close();	 Catch:{ IOException -> 0x01ae }
        goto L_0x007a;
    L_0x01ae:
        r10 = move-exception;
        r10.printStackTrace();
        r12 = r10;
        goto L_0x007a;
    L_0x01b5:
        r25 = new java.io.IOException;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = new java.lang.StringBuilder;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r27 = "Fail download. contentLength = ";
        r26.<init>(r27);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r26;
        r26 = r0.append(r6);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = r26.toString();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25.<init>(r26);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        throw r25;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
    L_0x01cc:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ all -> 0x03a0 }
        r12 = r10;
        if (r15 == 0) goto L_0x01d6;
    L_0x01d3:
        r15.close();	 Catch:{ IOException -> 0x01e9 }
    L_0x01d6:
        r0 = r33;
        r0 = r0.randomAccessFile;	 Catch:{ IOException -> 0x01e9 }
        r25 = r0;
        if (r25 == 0) goto L_0x007a;
    L_0x01de:
        r0 = r33;
        r0 = r0.randomAccessFile;	 Catch:{ IOException -> 0x01e9 }
        r25 = r0;
        r25.close();	 Catch:{ IOException -> 0x01e9 }
        goto L_0x007a;
    L_0x01e9:
        r10 = move-exception;
        r10.printStackTrace();
        r12 = r10;
        goto L_0x007a;
    L_0x01f0:
        r25 = new java.io.RandomAccessFile;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r33;
        r0 = r0.tempFile;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = r0;
        r27 = "rw";
        r25.<init>(r26, r27);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r25;
        r1 = r33;
        r1.randomAccessFile = r0;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r33;
        r0 = r0.enable_breakpoint;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        if (r25 == 0) goto L_0x03b8;
    L_0x020b:
        r0 = r33;
        r0 = r0.tempFile;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r25 = r25.exists();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        if (r25 == 0) goto L_0x0227;
    L_0x0217:
        r0 = r33;
        r0 = r0.tempFile;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r25 = r25.length();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r25;
        r2 = r33;
        r2.previousFileSize = r0;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
    L_0x0227:
        r0 = r33;
        r0 = r0.previousFileSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r25 = r25 + r6;
        r0 = r25;
        r2 = r33;
        r2.totalSize = r0;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r33;
        r0 = r0.tag;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r26 = 1;
        r0 = r26;
        r0 = new java.lang.Object[r0];	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = r0;
        r27 = 0;
        r28 = new java.lang.StringBuilder;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r33;
        r0 = r0.fileName;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = r0;
        r29 = java.lang.String.valueOf(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r28.<init>(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = ", previousFileSize: ";
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r33;
        r0 = r0.previousFileSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = r0;
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = ", contentLength: ";
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r28;
        r28 = r0.append(r6);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = ", ";
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r33;
        r0 = r0.randomAccessFile;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = r0;
        r29 = r29.length();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r28 = r28.toString();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26[r27] = r28;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        com.cnlaunch.framework.utils.NLog.m916d(r25, r26);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r16 = 0;
        r8 = 0;
        r0 = r33;
        r0 = r0.previousFileSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r0 = r25;
        r2 = r33;
        r2.downloadSize = r0;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r33;
        r0 = r0.downloadSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r0 = r25;
        r0 = (int) r0;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r0 = r33;
        r0 = r0.totalSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = r0;
        r0 = r26;
        r0 = (int) r0;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = r0;
        r0 = r33;
        r1 = r25;
        r2 = r26;
        r0.sendProgressMessage(r1, r2);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = r25;
        r5 = new byte[r0];	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r20 = java.lang.System.currentTimeMillis();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r22 = 0;
        r17 = 0;
    L_0x02ca:
        r16 = r15.read(r5);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = -1;
        r0 = r16;
        r1 = r25;
        if (r0 == r1) goto L_0x02e8;
    L_0x02d6:
        r25 = java.lang.Thread.currentThread();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r25.isInterrupted();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        if (r25 != 0) goto L_0x02e8;
    L_0x02e0:
        r0 = r33;
        r0 = r0.interrupt;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        if (r25 == 0) goto L_0x03c2;
    L_0x02e8:
        r0 = r33;
        r0 = r0.tag;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r26 = 1;
        r0 = r26;
        r0 = new java.lang.Object[r0];	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = r0;
        r27 = 0;
        r28 = new java.lang.StringBuilder;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r33;
        r0 = r0.fileName;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = r0;
        r29 = java.lang.String.valueOf(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r28.<init>(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = ", Finished. Rate: ";
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r28;
        r1 = r17;
        r28 = r0.append(r1);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = "KB/S";
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = ". Elapsed time:";
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r29 = r22 / r29;
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = "S";
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r28 = r28.toString();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26[r27] = r28;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        com.cnlaunch.framework.utils.NLog.m916d(r25, r26);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r33;
        r0 = r0.tag;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r26 = 1;
        r0 = r26;
        r0 = new java.lang.Object[r0];	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = r0;
        r27 = 0;
        r28 = new java.lang.StringBuilder;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = "contentLength: ";
        r28.<init>(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r28;
        r28 = r0.append(r6);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = ", count: ";
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r28;
        r28 = r0.append(r8);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r28 = r28.toString();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26[r27] = r28;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        com.cnlaunch.framework.utils.NLog.m916d(r25, r26);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = java.lang.Thread.currentThread();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r25.isInterrupted();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        if (r25 != 0) goto L_0x04b3;
    L_0x0374:
        r0 = r33;
        r0 = r0.interrupt;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        if (r25 != 0) goto L_0x04b3;
    L_0x037c:
        r0 = r33;
        r0 = r0.downloadSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r0 = r33;
        r0 = r0.totalSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r27 = r0;
        r25 = (r25 > r27 ? 1 : (r25 == r27 ? 0 : -1));
        if (r25 == 0) goto L_0x04b3;
    L_0x038c:
        r0 = r33;
        r0 = r0.totalSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r27 = -1;
        r25 = (r25 > r27 ? 1 : (r25 == r27 ? 0 : -1));
        if (r25 == 0) goto L_0x04b3;
    L_0x0398:
        r25 = new java.io.IOException;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = "Fail download. totalSize not eq downloadSize.";
        r25.<init>(r26);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        throw r25;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
    L_0x03a0:
        r25 = move-exception;
        if (r15 == 0) goto L_0x03a6;
    L_0x03a3:
        r15.close();	 Catch:{ IOException -> 0x04ac }
    L_0x03a6:
        r0 = r33;
        r0 = r0.randomAccessFile;	 Catch:{ IOException -> 0x04ac }
        r26 = r0;
        if (r26 == 0) goto L_0x03b7;
    L_0x03ae:
        r0 = r33;
        r0 = r0.randomAccessFile;	 Catch:{ IOException -> 0x04ac }
        r26 = r0;
        r26.close();	 Catch:{ IOException -> 0x04ac }
    L_0x03b7:
        throw r25;
    L_0x03b8:
        r25 = 0;
        r0 = r25;
        r2 = r33;
        r2.previousFileSize = r0;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        goto L_0x0227;
    L_0x03c2:
        r0 = r16;
        r0 = (long) r0;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r8 = r8 + r25;
        r0 = r33;
        r0 = r0.enable_breakpoint;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        if (r25 == 0) goto L_0x03e0;
    L_0x03d1:
        r0 = r33;
        r0 = r0.randomAccessFile;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r0 = r33;
        r0 = r0.downloadSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = r0;
        r25.seek(r26);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
    L_0x03e0:
        r0 = r33;
        r0 = r0.randomAccessFile;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r26 = 0;
        r0 = r25;
        r1 = r26;
        r2 = r16;
        r0.write(r5, r1, r2);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r33;
        r0 = r0.downloadSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r0 = r16;
        r0 = (long) r0;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r27 = r0;
        r25 = r25 + r27;
        r0 = r25;
        r2 = r33;
        r2.downloadSize = r0;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = java.lang.System.currentTimeMillis();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r22 = r25 - r20;
        r25 = 0;
        r25 = (r22 > r25 ? 1 : (r22 == r25 ? 0 : -1));
        if (r25 <= 0) goto L_0x041a;
    L_0x0410:
        r25 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r25 = r25 * r8;
        r27 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r25 = r25 / r27;
        r17 = r25 / r22;
    L_0x041a:
        r0 = r33;
        r0 = r0.tag;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r26 = 1;
        r0 = r26;
        r0 = new java.lang.Object[r0];	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = r0;
        r27 = 0;
        r28 = new java.lang.StringBuilder;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r33;
        r0 = r0.fileName;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = r0;
        r29 = java.lang.String.valueOf(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r28.<init>(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = ", Rate: ";
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r28;
        r1 = r17;
        r28 = r0.append(r1);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = "KB/S, downloaded: ";
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r29 = r8 / r29;
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = "KB, totalLength: ";
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r29 = r6 / r29;
        r31 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r29 = r29 / r31;
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = "MB, elapsed time: ";
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r29 = r22 / r29;
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r29 = "S.";
        r28 = r28.append(r29);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r28 = r28.toString();	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26[r27] = r28;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        com.cnlaunch.framework.utils.NLog.m916d(r25, r26);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r0 = r33;
        r0 = r0.downloadSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r0 = r25;
        r0 = (int) r0;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r25 = r0;
        r0 = r33;
        r0 = r0.totalSize;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = r0;
        r0 = r26;
        r0 = (int) r0;	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        r26 = r0;
        r0 = r33;
        r1 = r25;
        r2 = r26;
        r0.sendProgressMessage(r1, r2);	 Catch:{ IllegalStateException -> 0x005f, FileNotFoundException -> 0x0191, IOException -> 0x01cc }
        goto L_0x02ca;
    L_0x04a5:
        r10 = move-exception;
        r10.printStackTrace();
        r12 = r10;
        goto L_0x007a;
    L_0x04ac:
        r10 = move-exception;
        r10.printStackTrace();
        r12 = r10;
        goto L_0x03b7;
    L_0x04b3:
        if (r15 == 0) goto L_0x04b8;
    L_0x04b5:
        r15.close();	 Catch:{ IOException -> 0x04cb }
    L_0x04b8:
        r0 = r33;
        r0 = r0.randomAccessFile;	 Catch:{ IOException -> 0x04cb }
        r25 = r0;
        if (r25 == 0) goto L_0x007a;
    L_0x04c0:
        r0 = r33;
        r0 = r0.randomAccessFile;	 Catch:{ IOException -> 0x04cb }
        r25 = r0;
        r25.close();	 Catch:{ IOException -> 0x04cb }
        goto L_0x007a;
    L_0x04cb:
        r10 = move-exception;
        r10.printStackTrace();
        r12 = r10;
        goto L_0x007a;
    L_0x04d2:
        r0 = r33;
        r0 = r0.targetFile;
        r25 = r0;
        r25 = r25.exists();
        if (r25 == 0) goto L_0x055a;
    L_0x04de:
        r0 = r33;
        r0 = r0.totalSize;
        r25 = r0;
        r0 = r33;
        r0 = r0.targetFile;
        r27 = r0;
        r27 = r27.length();
        r25 = (r25 > r27 ? 1 : (r25 == r27 ? 0 : -1));
        if (r25 != 0) goto L_0x055a;
    L_0x04f2:
        r0 = r33;
        r0 = r0.tag;
        r25 = r0;
        r26 = 1;
        r0 = r26;
        r0 = new java.lang.Object[r0];
        r26 = r0;
        r27 = 0;
        r28 = new java.lang.StringBuilder;
        r0 = r33;
        r0 = r0.fileName;
        r29 = r0;
        r29 = java.lang.String.valueOf(r29);
        r28.<init>(r29);
        r29 = ", Output file already exists. Skipping download.";
        r28 = r28.append(r29);
        r28 = r28.toString();
        r26[r27] = r28;
        com.cnlaunch.framework.utils.NLog.m917e(r25, r26);
        r0 = r33;
        r0 = r0.totalSize;
        r25 = r0;
        r0 = r25;
        r0 = (int) r0;
        r25 = r0;
        r0 = r33;
        r0 = r0.totalSize;
        r26 = r0;
        r0 = r26;
        r0 = (int) r0;
        r26 = r0;
        r0 = r33;
        r1 = r25;
        r2 = r26;
        r0.sendProgressMessage(r1, r2);
    L_0x053f:
        r25 = r19.getStatusCode();
        r26 = r34.getAllHeaders();
        r27 = "success";
        r27 = r27.getBytes();
        r0 = r33;
        r1 = r25;
        r2 = r26;
        r3 = r27;
        r0.sendSuccessMessage(r1, r2, r3);
        goto L_0x00b5;
    L_0x055a:
        r0 = r33;
        r0 = r0.tempFile;
        r25 = r0;
        r0 = r33;
        r0 = r0.targetFile;
        r26 = r0;
        r25.renameTo(r26);
        goto L_0x053f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cnlaunch.framework.network.http.BreakpointHttpResponseHandler.sendResponseMessage(org.apache.http.HttpResponse):void");
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context mContext) {
        this.context = mContext;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }

    public File getTempFile() {
        return this.tempFile;
    }

    public File getTargetFile() {
        return this.targetFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    public long getPreviousFileSize() {
        return this.previousFileSize;
    }

    public void setPreviousFileSize(int previousFileSize) {
        this.previousFileSize = (long) previousFileSize;
    }

    public boolean isInterrupt() {
        return this.interrupt;
    }

    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    public RequestParams getParams() {
        return this.params;
    }

    public void setParams(RequestParams params) {
        this.params = params;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean getEnableBreakpoint() {
        return this.enable_breakpoint;
    }
}
