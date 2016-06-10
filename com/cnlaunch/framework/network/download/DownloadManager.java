package com.cnlaunch.framework.network.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.cnlaunch.framework.common.Constants;
import com.cnlaunch.framework.common.parse.JsonMananger;
import com.cnlaunch.framework.network.http.AsyncHttpClient;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.framework.network.http.BreakpointHttpResponseHandler;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.framework.network.http.SyncHttpClient;
import com.cnlaunch.framework.utils.MD5Utils;
import com.cnlaunch.framework.utils.NLog;
import com.ifoer.util.MySharedPreferences;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xmlpull.v1.XmlPullParser;

public class DownloadManager extends Thread {
    private static final int DEFAULT_MAX_CONNECTIONS = 3;
    private static DownloadManager instance;
    private AsyncHttpClient asyncHttpClient;
    private Boolean isRunning;
    private DownLoadCallback mDownLoadCallback;
    private List<AsyncHttpResponseHandler> mDownloadinghandlers;
    private List<AsyncHttpResponseHandler> mPausinghandlers;
    private HandlerQueue mhandlerQueue;
    private SyncHttpClient syncHttpClient;
    private final String tag;
    private ThreadPoolExecutor threadPool;

    private class HandlerQueue {
        private Queue<AsyncHttpResponseHandler> handlerQueue;

        public HandlerQueue() {
            this.handlerQueue = new LinkedList();
        }

        public void offer(AsyncHttpResponseHandler handler) {
            this.handlerQueue.offer(handler);
        }

        public AsyncHttpResponseHandler get(int position) {
            if (position >= size()) {
                return null;
            }
            return (AsyncHttpResponseHandler) ((LinkedList) this.handlerQueue).get(position);
        }

        public AsyncHttpResponseHandler poll() {
            return (AsyncHttpResponseHandler) this.handlerQueue.poll();
        }

        public boolean remove(int position) {
            return this.handlerQueue.remove(get(position));
        }

        public boolean remove(AsyncHttpResponseHandler handler) {
            return this.handlerQueue.remove(handler);
        }

        public int size() {
            return this.handlerQueue.size();
        }
    }

    /* renamed from: com.cnlaunch.framework.network.download.DownloadManager.1 */
    class C12981 extends BreakpointHttpResponseHandler {
        C12981(DownloadParam $anonymous0) {
            super($anonymous0);
        }

        public void onProgress(int bytesWritten, int totalSize) {
            super.onProgress(bytesWritten, totalSize);
            if (DownloadManager.this.mDownLoadCallback != null) {
                DownloadManager.this.mDownLoadCallback.sendLoadMessage(getFileName(), bytesWritten, totalSize);
            }
        }

        public void onSuccess(File file) {
            if (DownloadManager.this.mDownLoadCallback != null) {
                NLog.m916d(DownloadManager.this.tag, "onSuccess: " + getFileName());
                DownloadManager.this.mDownLoadCallback.sendSuccessMessage(getFileName(), file.getPath());
            }
        }

        public void onFinish() {
            NLog.m916d(DownloadManager.this.tag, "onFinish: " + getFileName());
            DownloadManager.this.completehandler(this);
        }

        public void onStart() {
            if (DownloadManager.this.mDownLoadCallback != null) {
                DownloadManager.this.mDownLoadCallback.onStart();
            }
            NLog.m916d(DownloadManager.this.tag, "onStart: " + getFileName());
        }

        public void onFailure(Throwable error) {
            String message = XmlPullParser.NO_NAMESPACE;
            if (error != null) {
                message = error.getMessage();
            }
            if (DownloadManager.this.mDownLoadCallback != null) {
                NLog.m916d(DownloadManager.this.tag, "onFailure: " + getFileName());
                DownloadManager.this.mDownLoadCallback.sendFailureMessage(getFileName(), message);
            }
            DownloadManager.this.pausehandler(this);
        }
    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    private DownloadManager() {
        this.tag = DownloadManager.class.getSimpleName();
        this.isRunning = Boolean.valueOf(false);
        this.mhandlerQueue = new HandlerQueue();
        this.mDownloadinghandlers = new ArrayList();
        this.mPausinghandlers = new ArrayList();
        this.threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(DEFAULT_MAX_CONNECTIONS);
    }

    public void setDownLoadCallback(DownLoadCallback downLoadCallback) {
        this.mDownLoadCallback = downLoadCallback;
    }

    public void startManage() {
        if (!isAlive()) {
            this.isRunning = Boolean.valueOf(true);
            start();
            if (this.mDownLoadCallback != null) {
                this.mDownLoadCallback.sendStartMessage();
            }
        }
    }

    public void close() {
        this.isRunning = Boolean.valueOf(false);
        pauseAllHandler();
        if (this.mDownLoadCallback != null) {
            this.mDownLoadCallback.sendStopMessage();
        }
        interrupt();
        instance = null;
    }

    public boolean isRunning() {
        return this.isRunning.booleanValue();
    }

    public void run() {
        while (this.isRunning.booleanValue()) {
            BreakpointHttpResponseHandler handler = (BreakpointHttpResponseHandler) this.mhandlerQueue.poll();
            if (handler != null) {
                this.mDownloadinghandlers.add(handler);
                handler.setInterrupt(false);
                this.syncHttpClient = new SyncHttpClient();
                this.asyncHttpClient = new AsyncHttpClient();
                this.asyncHttpClient.setThreadPool(this.threadPool);
                if (!(handler.getParams() == null || handler.getContext() == null)) {
                    String user_id = MySharedPreferences.getStringValue(handler.getContext(), MySharedPreferences.CCKey);
                    String sign = new StringBuilder(String.valueOf(handler.getParams().getParamValueString())).append(MySharedPreferences.getStringValue(handler.getContext(), MySharedPreferences.TokenKey)).toString();
                    if (handler.getEnableBreakpoint()) {
                        this.syncHttpClient.addHeader(Constants.SIGN, MD5Utils.encrypt(sign));
                        this.syncHttpClient.addHeader(MultipleAddresses.CC, user_id);
                    } else {
                        this.asyncHttpClient.addHeader(Constants.SIGN, MD5Utils.encrypt(sign));
                        this.asyncHttpClient.addHeader(MultipleAddresses.CC, user_id);
                    }
                }
                if (handler.getEnableBreakpoint()) {
                    try {
                        String response = this.syncHttpClient.get(handler.getContext(), handler.getUrl(), handler.getParams());
                        Log.d(this.tag, "Response: " + response);
                        BreakpointURL breakpointURL = (BreakpointURL) JsonMananger.getInstance().jsonToBean(response, BreakpointURL.class);
                        if (breakpointURL.getCode() == 0) {
                            handler.setUrl(breakpointURL.getData().getDownUrl());
                            this.asyncHttpClient.addHeader(Constants.SIGN, breakpointURL.getData().getMd5SignStr());
                            this.asyncHttpClient.get(handler.getContext(), handler.getUrl(), null, handler);
                        } else {
                            if (this.mDownLoadCallback != null) {
                                NLog.m916d(this.tag, "onFailure: " + handler.getFileName());
                                this.mDownLoadCallback.sendFailureMessage(handler.getFileName(), "getBreakpointURL error! code: " + breakpointURL.getCode() + ", message: " + breakpointURL.getMessage());
                            }
                            completehandler(handler);
                        }
                    } catch (HttpException e) {
                        e.printStackTrace();
                        if (this.mDownLoadCallback != null) {
                            NLog.m916d(this.tag, "onFailure: " + handler.getFileName());
                            this.mDownLoadCallback.sendFailureMessage(handler.getFileName(), "getBreakpointURL network error!");
                        }
                        completehandler(handler);
                    }
                } else {
                    this.asyncHttpClient.get(handler.getContext(), handler.getUrl(), handler.getParams(), handler);
                }
            }
        }
    }

    public void addHandler(Context context, String uriString) {
        if (TextUtils.isEmpty(uriString)) {
            throw new IllegalArgumentException("addHandler uriString is not null.");
        }
        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(uriString)));
    }

    public void addHandler(DownloadParam downloadParam) {
        if (downloadParam == null) {
            NLog.m917e(this.tag, "addHandler downloadParam is not null.");
            return;
        }
        String fileName = downloadParam.getFileName();
        if (TextUtils.isEmpty(downloadParam.getUrl()) || TextUtils.isEmpty(fileName)) {
            NLog.m917e(this.tag, "addHandler url or fileName is not null.");
        } else if (TextUtils.isEmpty(downloadParam.getDownPath())) {
            NLog.m917e(this.tag, "addHandler downPath is not null.");
        } else if (hasHandler(fileName)) {
            NLog.m917e(this.tag, "addHandler fileName: " + fileName + " is exist.");
        } else {
            broadcastAddHandler(fileName);
            this.mhandlerQueue.offer(newAsyncHttpResponseHandler(downloadParam));
            if (!isAlive()) {
                startManage();
            }
        }
    }

    private void broadcastAddHandler(String fileName) {
        broadcastAddHandler(fileName, false);
    }

    private void broadcastAddHandler(String fileName, boolean isInterrupt) {
        if (this.mDownLoadCallback != null) {
            this.mDownLoadCallback.sendAddMessage(fileName, Boolean.valueOf(isInterrupt));
        }
    }

    public void reBroadcastAddAllhandler() {
        int i;
        for (i = 0; i < this.mDownloadinghandlers.size(); i++) {
            BreakpointHttpResponseHandler handler = (BreakpointHttpResponseHandler) this.mDownloadinghandlers.get(i);
            broadcastAddHandler(handler.getFileName(), handler.isInterrupt());
        }
        for (i = 0; i < this.mhandlerQueue.size(); i++) {
            broadcastAddHandler(((BreakpointHttpResponseHandler) this.mhandlerQueue.get(i)).getFileName());
        }
        for (i = 0; i < this.mPausinghandlers.size(); i++) {
            broadcastAddHandler(((BreakpointHttpResponseHandler) this.mPausinghandlers.get(i)).getFileName());
        }
    }

    public boolean hasHandler(String fileName) {
        int i;
        for (i = 0; i < this.mDownloadinghandlers.size(); i++) {
            if (fileName.equals(((BreakpointHttpResponseHandler) this.mDownloadinghandlers.get(i)).getFileName())) {
                return true;
            }
        }
        i = 0;
        while (i < this.mhandlerQueue.size()) {
            try {
                BreakpointHttpResponseHandler handler = (BreakpointHttpResponseHandler) this.mhandlerQueue.get(i);
                if (handler == null) {
                    return false;
                }
                if (fileName.equals(handler.getFileName())) {
                    return true;
                }
                i++;
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public int getTotalhandlerCount() {
        return (this.mhandlerQueue.size() + this.mDownloadinghandlers.size()) + this.mPausinghandlers.size();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void deleteHandler(java.lang.String r7) {
        /*
        r6 = this;
        monitor-enter(r6);
        r2 = 0;
        r3 = 0;
    L_0x0003:
        r5 = r6.mDownloadinghandlers;	 Catch:{ all -> 0x0057 }
        r5 = r5.size();	 Catch:{ all -> 0x0057 }
        if (r3 < r5) goto L_0x001f;
    L_0x000b:
        r3 = 0;
    L_0x000c:
        r5 = r6.mhandlerQueue;	 Catch:{ all -> 0x0057 }
        r5 = r5.size();	 Catch:{ all -> 0x0057 }
        if (r3 < r5) goto L_0x005d;
    L_0x0014:
        r3 = 0;
    L_0x0015:
        r5 = r6.mPausinghandlers;	 Catch:{ all -> 0x0057 }
        r5 = r5.size();	 Catch:{ all -> 0x0057 }
        if (r3 < r5) goto L_0x007b;
    L_0x001d:
        monitor-exit(r6);
        return;
    L_0x001f:
        r5 = r6.mDownloadinghandlers;	 Catch:{ all -> 0x0057 }
        r5 = r5.get(r3);	 Catch:{ all -> 0x0057 }
        r0 = r5;
        r0 = (com.cnlaunch.framework.network.http.BreakpointHttpResponseHandler) r0;	 Catch:{ all -> 0x0057 }
        r2 = r0;
        if (r2 == 0) goto L_0x005a;
    L_0x002b:
        r5 = r2.getFileName();	 Catch:{ all -> 0x0057 }
        r5 = r5.equals(r7);	 Catch:{ all -> 0x0057 }
        if (r5 == 0) goto L_0x005a;
    L_0x0035:
        r1 = r2.getTargetFile();	 Catch:{ all -> 0x0057 }
        r5 = r1.exists();	 Catch:{ all -> 0x0057 }
        if (r5 == 0) goto L_0x0042;
    L_0x003f:
        r1.delete();	 Catch:{ all -> 0x0057 }
    L_0x0042:
        r4 = r2.getTempFile();	 Catch:{ all -> 0x0057 }
        r5 = r4.exists();	 Catch:{ all -> 0x0057 }
        if (r5 == 0) goto L_0x004f;
    L_0x004c:
        r4.delete();	 Catch:{ all -> 0x0057 }
    L_0x004f:
        r5 = 1;
        r2.setInterrupt(r5);	 Catch:{ all -> 0x0057 }
        r6.completehandler(r2);	 Catch:{ all -> 0x0057 }
        goto L_0x001d;
    L_0x0057:
        r5 = move-exception;
        monitor-exit(r6);
        throw r5;
    L_0x005a:
        r3 = r3 + 1;
        goto L_0x0003;
    L_0x005d:
        r5 = r6.mhandlerQueue;	 Catch:{ all -> 0x0057 }
        r5 = r5.get(r3);	 Catch:{ all -> 0x0057 }
        r0 = r5;
        r0 = (com.cnlaunch.framework.network.http.BreakpointHttpResponseHandler) r0;	 Catch:{ all -> 0x0057 }
        r2 = r0;
        if (r2 == 0) goto L_0x0078;
    L_0x0069:
        r5 = r2.getFileName();	 Catch:{ all -> 0x0057 }
        r5 = r5.equals(r7);	 Catch:{ all -> 0x0057 }
        if (r5 == 0) goto L_0x0078;
    L_0x0073:
        r5 = r6.mhandlerQueue;	 Catch:{ all -> 0x0057 }
        r5.remove(r2);	 Catch:{ all -> 0x0057 }
    L_0x0078:
        r3 = r3 + 1;
        goto L_0x000c;
    L_0x007b:
        r5 = r6.mPausinghandlers;	 Catch:{ all -> 0x0057 }
        r5 = r5.get(r3);	 Catch:{ all -> 0x0057 }
        r0 = r5;
        r0 = (com.cnlaunch.framework.network.http.BreakpointHttpResponseHandler) r0;	 Catch:{ all -> 0x0057 }
        r2 = r0;
        if (r2 == 0) goto L_0x0096;
    L_0x0087:
        r5 = r2.getFileName();	 Catch:{ all -> 0x0057 }
        r5 = r5.equals(r7);	 Catch:{ all -> 0x0057 }
        if (r5 == 0) goto L_0x0096;
    L_0x0091:
        r5 = r6.mPausinghandlers;	 Catch:{ all -> 0x0057 }
        r5.remove(r2);	 Catch:{ all -> 0x0057 }
    L_0x0096:
        r3 = r3 + 1;
        goto L_0x0015;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cnlaunch.framework.network.download.DownloadManager.deleteHandler(java.lang.String):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void continueHandler(java.lang.String r5) {
        /*
        r4 = this;
        monitor-enter(r4);
        r1 = 0;
        r2 = 0;
    L_0x0003:
        r3 = r4.mPausinghandlers;	 Catch:{ all -> 0x0030 }
        r3 = r3.size();	 Catch:{ all -> 0x0030 }
        if (r2 < r3) goto L_0x000d;
    L_0x000b:
        monitor-exit(r4);
        return;
    L_0x000d:
        r3 = r4.mPausinghandlers;	 Catch:{ all -> 0x0030 }
        r3 = r3.get(r2);	 Catch:{ all -> 0x0030 }
        r0 = r3;
        r0 = (com.cnlaunch.framework.network.http.BreakpointHttpResponseHandler) r0;	 Catch:{ all -> 0x0030 }
        r1 = r0;
        if (r1 == 0) goto L_0x002d;
    L_0x0019:
        r3 = r1.getFileName();	 Catch:{ all -> 0x0030 }
        r3 = r3.equals(r5);	 Catch:{ all -> 0x0030 }
        if (r3 == 0) goto L_0x002d;
    L_0x0023:
        r3 = r4.mPausinghandlers;	 Catch:{ all -> 0x0030 }
        r3.remove(r1);	 Catch:{ all -> 0x0030 }
        r3 = r4.mhandlerQueue;	 Catch:{ all -> 0x0030 }
        r3.offer(r1);	 Catch:{ all -> 0x0030 }
    L_0x002d:
        r2 = r2 + 1;
        goto L_0x0003;
    L_0x0030:
        r3 = move-exception;
        monitor-exit(r4);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cnlaunch.framework.network.download.DownloadManager.continueHandler(java.lang.String):void");
    }

    public synchronized void continueAllHandler() {
        this.threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(DEFAULT_MAX_CONNECTIONS);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void pauseHandler(java.lang.String r4) {
        /*
        r3 = this;
        monitor-enter(r3);
        r1 = 0;
    L_0x0002:
        r2 = r3.mDownloadinghandlers;	 Catch:{ all -> 0x0026 }
        r2 = r2.size();	 Catch:{ all -> 0x0026 }
        if (r1 < r2) goto L_0x000c;
    L_0x000a:
        monitor-exit(r3);
        return;
    L_0x000c:
        r2 = r3.mDownloadinghandlers;	 Catch:{ all -> 0x0026 }
        r0 = r2.get(r1);	 Catch:{ all -> 0x0026 }
        r0 = (com.cnlaunch.framework.network.http.BreakpointHttpResponseHandler) r0;	 Catch:{ all -> 0x0026 }
        if (r0 == 0) goto L_0x0023;
    L_0x0016:
        r2 = r0.getFileName();	 Catch:{ all -> 0x0026 }
        r2 = r2.equals(r4);	 Catch:{ all -> 0x0026 }
        if (r2 == 0) goto L_0x0023;
    L_0x0020:
        r3.pausehandler(r0);	 Catch:{ all -> 0x0026 }
    L_0x0023:
        r1 = r1 + 1;
        goto L_0x0002;
    L_0x0026:
        r2 = move-exception;
        monitor-exit(r3);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cnlaunch.framework.network.download.DownloadManager.pauseHandler(java.lang.String):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void pauseAllHandler() {
        /*
        r3 = this;
        monitor-enter(r3);
        r0 = 0;
    L_0x0002:
        r1 = r3.mhandlerQueue;	 Catch:{ all -> 0x0029 }
        r1 = r1.size();	 Catch:{ all -> 0x0029 }
        r2 = 1;
        if (r1 >= r2) goto L_0x001c;
    L_0x000b:
        r1 = r3.mDownloadinghandlers;	 Catch:{ all -> 0x0029 }
        r1.clear();	 Catch:{ all -> 0x0029 }
        r1 = r3.mPausinghandlers;	 Catch:{ all -> 0x0029 }
        r1.clear();	 Catch:{ all -> 0x0029 }
        r1 = r3.threadPool;	 Catch:{ all -> 0x0029 }
        r1.shutdownNow();	 Catch:{ all -> 0x0029 }
        monitor-exit(r3);
        return;
    L_0x001c:
        r1 = r3.mhandlerQueue;	 Catch:{ all -> 0x0029 }
        r2 = 0;
        r0 = r1.get(r2);	 Catch:{ all -> 0x0029 }
        r1 = r3.mhandlerQueue;	 Catch:{ all -> 0x0029 }
        r1.remove(r0);	 Catch:{ all -> 0x0029 }
        goto L_0x0002;
    L_0x0029:
        r1 = move-exception;
        monitor-exit(r3);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cnlaunch.framework.network.download.DownloadManager.pauseAllHandler():void");
    }

    private synchronized void pausehandler(AsyncHttpResponseHandler handler) {
        BreakpointHttpResponseHandler fileHttpResponseHandler = (BreakpointHttpResponseHandler) handler;
        if (handler != null) {
            fileHttpResponseHandler.setInterrupt(true);
            this.mDownloadinghandlers.remove(handler);
            this.mPausinghandlers.add(handler);
        }
    }

    private synchronized void completehandler(AsyncHttpResponseHandler handler) {
        if (this.mDownloadinghandlers.contains(handler)) {
            this.mDownloadinghandlers.remove(handler);
            if (this.mDownLoadCallback != null) {
                this.mDownLoadCallback.sendFinishMessage(((BreakpointHttpResponseHandler) handler).getFileName());
            }
        }
    }

    private AsyncHttpResponseHandler newAsyncHttpResponseHandler(DownloadParam downloadParam) {
        return new C12981(downloadParam);
    }
}
