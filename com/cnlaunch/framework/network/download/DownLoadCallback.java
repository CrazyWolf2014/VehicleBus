package com.cnlaunch.framework.network.download;

import android.os.Handler;
import android.os.Message;

public class DownLoadCallback extends Handler {
    protected static final int ADD_MESSAGE = 1;
    protected static final int FAILURE_MESSAGE = 4;
    protected static final int FINISH_MESSAGE = 5;
    protected static final int PROGRESS_MESSAGE = 2;
    protected static final int START_MESSAGE = 0;
    protected static final int STOP_MESSAGE = 6;
    protected static final int SUCCESS_MESSAGE = 3;

    public void onStart() {
    }

    public void onAdd(String fileName, Boolean isInterrupt) {
    }

    public void onLoading(String fileName, int bytesWritten, int totalSize) {
    }

    public void onSuccess(String fileName, String filePath) {
    }

    public void onFailure(String fileName, String strMsg) {
    }

    public void onFinish(String fileName) {
    }

    public void onStop() {
    }

    public void handleMessage(Message msg) {
        Object[] response;
        switch (msg.what) {
            case START_MESSAGE /*0*/:
                onStart();
            case ADD_MESSAGE /*1*/:
                response = msg.obj;
                onAdd((String) response[START_MESSAGE], (Boolean) response[ADD_MESSAGE]);
            case PROGRESS_MESSAGE /*2*/:
                response = msg.obj;
                onLoading((String) response[START_MESSAGE], ((Integer) response[ADD_MESSAGE]).intValue(), ((Integer) response[PROGRESS_MESSAGE]).intValue());
            case SUCCESS_MESSAGE /*3*/:
                response = msg.obj;
                onSuccess((String) response[START_MESSAGE], (String) response[ADD_MESSAGE]);
            case FAILURE_MESSAGE /*4*/:
                response = msg.obj;
                onFailure((String) response[START_MESSAGE], (String) response[ADD_MESSAGE]);
            case FINISH_MESSAGE /*5*/:
                onFinish((String) msg.obj[START_MESSAGE]);
            case STOP_MESSAGE /*6*/:
                onStop();
            default:
        }
    }

    protected void sendSuccessMessage(String url, String path) {
        Object obj = new Object[PROGRESS_MESSAGE];
        obj[START_MESSAGE] = url;
        obj[ADD_MESSAGE] = path;
        sendMessage(obtainMessage(SUCCESS_MESSAGE, obj));
    }

    protected void sendLoadMessage(String url, int bytesWritten, int totalSize) {
        Object obj = new Object[SUCCESS_MESSAGE];
        obj[START_MESSAGE] = url;
        obj[ADD_MESSAGE] = Integer.valueOf(bytesWritten);
        obj[PROGRESS_MESSAGE] = Integer.valueOf(totalSize);
        sendMessage(obtainMessage(PROGRESS_MESSAGE, obj));
    }

    protected void sendAddMessage(String url, Boolean isInterrupt) {
        Object obj = new Object[PROGRESS_MESSAGE];
        obj[START_MESSAGE] = url;
        obj[ADD_MESSAGE] = isInterrupt;
        sendMessage(obtainMessage(ADD_MESSAGE, obj));
    }

    protected void sendFailureMessage(String url, String strMsg) {
        Object obj = new Object[PROGRESS_MESSAGE];
        obj[START_MESSAGE] = url;
        obj[ADD_MESSAGE] = strMsg;
        sendMessage(obtainMessage(FAILURE_MESSAGE, obj));
    }

    protected void sendStartMessage() {
        sendMessage(obtainMessage(START_MESSAGE, null));
    }

    protected void sendStopMessage() {
        sendMessage(obtainMessage(STOP_MESSAGE, null));
    }

    protected void sendFinishMessage(String url) {
        Object obj = new Object[ADD_MESSAGE];
        obj[START_MESSAGE] = url;
        sendMessage(obtainMessage(FINISH_MESSAGE, obj));
    }
}
