package com.cnlaunch.framework.network.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.cnlaunch.mycar.jni.JniX431File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URI;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.util.ByteArrayBuffer;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.TTL;

public class AsyncHttpResponseHandler implements ResponseHandlerInterface {
    protected static final int BUFFER_SIZE = 4096;
    public static final String DEFAULT_CHARSET = "UTF-8";
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int FINISH_MESSAGE = 3;
    private static final String LOG_TAG = "AsyncHttpResponseHandler";
    protected static final int PROGRESS_MESSAGE = 4;
    protected static final int RETRY_MESSAGE = 5;
    protected static final int START_MESSAGE = 2;
    protected static final int SUCCESS_MESSAGE = 0;
    private Handler handler;
    private Header[] requestHeaders;
    private URI requestURI;
    private String responseCharset;
    private Boolean useSynchronousMode;

    static class ResponderHandler extends Handler {
        private final WeakReference<AsyncHttpResponseHandler> mResponder;

        ResponderHandler(AsyncHttpResponseHandler service) {
            this.mResponder = new WeakReference(service);
        }

        public void handleMessage(Message msg) {
            AsyncHttpResponseHandler service = (AsyncHttpResponseHandler) this.mResponder.get();
            if (service != null) {
                service.handleMessage(msg);
            }
        }
    }

    public URI getRequestURI() {
        return this.requestURI;
    }

    public Header[] getRequestHeaders() {
        return this.requestHeaders;
    }

    public void setRequestURI(URI requestURI) {
        this.requestURI = requestURI;
    }

    public void setRequestHeaders(Header[] requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public boolean getUseSynchronousMode() {
        return this.useSynchronousMode.booleanValue();
    }

    public void setUseSynchronousMode(boolean value) {
        this.useSynchronousMode = Boolean.valueOf(value);
    }

    public void setCharset(String charset) {
        this.responseCharset = charset;
    }

    public String getCharset() {
        return this.responseCharset == null ? DEFAULT_CHARSET : this.responseCharset;
    }

    public AsyncHttpResponseHandler() {
        this.responseCharset = DEFAULT_CHARSET;
        this.useSynchronousMode = Boolean.valueOf(false);
        this.requestURI = null;
        this.requestHeaders = null;
        if (Looper.myLooper() != null) {
            this.handler = new ResponderHandler(this);
        }
    }

    public void onProgress(int bytesWritten, int totalSize) {
    }

    public void onStart() {
    }

    public void onFinish() {
    }

    @Deprecated
    public void onSuccess(String content) {
    }

    @Deprecated
    public void onSuccess(int statusCode, Header[] headers, String content) {
        onSuccess(statusCode, content);
    }

    @Deprecated
    public void onSuccess(int statusCode, String content) {
        onSuccess(content);
    }

    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            onSuccess(statusCode, headers, responseBody == null ? null : new String(responseBody, getCharset()));
        } catch (Throwable e) {
            Log.e(LOG_TAG, e.toString());
            onFailure(statusCode, headers, e, null);
        }
    }

    @Deprecated
    public void onFailure(Throwable error) {
    }

    @Deprecated
    public void onFailure(Throwable error, String content) {
        onFailure(error);
    }

    @Deprecated
    public void onFailure(int statusCode, Throwable error, String content) {
        onFailure(error, content);
    }

    @Deprecated
    public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
        onFailure(statusCode, error, content);
    }

    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        try {
            onFailure(statusCode, headers, error, responseBody == null ? null : new String(responseBody, getCharset()));
        } catch (Throwable e) {
            Log.e(LOG_TAG, e.toString());
            onFailure(statusCode, headers, e, null);
        }
    }

    public void onRetry() {
    }

    public final void sendProgressMessage(int bytesWritten, int bytesTotal) {
        Object obj = new Object[START_MESSAGE];
        obj[0] = Integer.valueOf(bytesWritten);
        obj[FAILURE_MESSAGE] = Integer.valueOf(bytesTotal);
        sendMessage(obtainMessage(PROGRESS_MESSAGE, obj));
    }

    public final void sendSuccessMessage(int statusCode, Header[] headers, byte[] responseBody) {
        Object obj = new Object[FINISH_MESSAGE];
        obj[0] = Integer.valueOf(statusCode);
        obj[FAILURE_MESSAGE] = headers;
        obj[START_MESSAGE] = responseBody;
        sendMessage(obtainMessage(0, obj));
    }

    public final void sendFailureMessage(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Object obj = new Object[PROGRESS_MESSAGE];
        obj[0] = Integer.valueOf(statusCode);
        obj[FAILURE_MESSAGE] = headers;
        obj[START_MESSAGE] = responseBody;
        obj[FINISH_MESSAGE] = error;
        sendMessage(obtainMessage(FAILURE_MESSAGE, obj));
    }

    public final void sendStartMessage() {
        sendMessage(obtainMessage(START_MESSAGE, null));
    }

    public final void sendFinishMessage() {
        sendMessage(obtainMessage(FINISH_MESSAGE, null));
    }

    public final void sendRetryMessage() {
        sendMessage(obtainMessage(RETRY_MESSAGE, null));
    }

    protected void handleMessage(Message msg) {
        Object[] response;
        switch (msg.what) {
            case KEYRecord.OWNER_USER /*0*/:
                response = msg.obj;
                if (response == null || response.length < FINISH_MESSAGE) {
                    Log.e(LOG_TAG, "SUCCESS_MESSAGE didn't got enough params");
                } else {
                    onSuccess(((Integer) response[0]).intValue(), (Header[]) response[FAILURE_MESSAGE], (byte[]) response[START_MESSAGE]);
                }
            case FAILURE_MESSAGE /*1*/:
                response = (Object[]) msg.obj;
                if (response == null || response.length < PROGRESS_MESSAGE) {
                    Log.e(LOG_TAG, "FAILURE_MESSAGE didn't got enough params");
                } else {
                    onFailure(((Integer) response[0]).intValue(), (Header[]) response[FAILURE_MESSAGE], (byte[]) response[START_MESSAGE], (Throwable) response[FINISH_MESSAGE]);
                }
            case START_MESSAGE /*2*/:
                onStart();
            case FINISH_MESSAGE /*3*/:
                onFinish();
            case PROGRESS_MESSAGE /*4*/:
                response = (Object[]) msg.obj;
                if (response == null || response.length < START_MESSAGE) {
                    Log.e(LOG_TAG, "PROGRESS_MESSAGE didn't got enough params");
                    return;
                }
                try {
                    onProgress(((Integer) response[0]).intValue(), ((Integer) response[FAILURE_MESSAGE]).intValue());
                } catch (Throwable t) {
                    Log.e(LOG_TAG, "custom onProgress contains an error", t);
                }
            case RETRY_MESSAGE /*5*/:
                onRetry();
            default:
        }
    }

    protected void sendMessage(Message msg) {
        if (getUseSynchronousMode() || this.handler == null) {
            handleMessage(msg);
        } else if (!Thread.currentThread().isInterrupted()) {
            this.handler.sendMessage(msg);
        }
    }

    protected void postRunnable(Runnable r) {
        if (r != null) {
            this.handler.post(r);
        }
    }

    protected Message obtainMessage(int responseMessage, Object response) {
        if (this.handler != null) {
            return this.handler.obtainMessage(responseMessage, response);
        }
        Message msg = Message.obtain();
        if (msg == null) {
            return msg;
        }
        msg.what = responseMessage;
        msg.obj = response;
        return msg;
    }

    public void sendResponseMessage(HttpResponse response) throws IOException {
        if (!Thread.currentThread().isInterrupted()) {
            StatusLine status = response.getStatusLine();
            byte[] responseBody = getResponseData(response.getEntity());
            if (!Thread.currentThread().isInterrupted()) {
                if (status.getStatusCode() >= JniX431File.MAX_DS_COLNUMBER) {
                    sendFailureMessage(status.getStatusCode(), response.getAllHeaders(), responseBody, new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()));
                } else {
                    sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(), responseBody);
                }
            }
        }
    }

    byte[] getResponseData(HttpEntity entity) throws IOException {
        byte[] responseBody = null;
        if (entity != null) {
            InputStream instream = entity.getContent();
            if (instream != null) {
                long contentLength = entity.getContentLength();
                if (contentLength > TTL.MAX_VALUE) {
                    throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
                }
                if (contentLength < 0) {
                    contentLength = 4096;
                }
                try {
                    ByteArrayBuffer buffer = new ByteArrayBuffer((int) contentLength);
                    byte[] tmp = new byte[BUFFER_SIZE];
                    int count = 0;
                    while (true) {
                        int l = instream.read(tmp);
                        if (l == -1 || Thread.currentThread().isInterrupted()) {
                            break;
                        }
                        count += l;
                        buffer.append(tmp, 0, l);
                        sendProgressMessage(count, (int) contentLength);
                    }
                    instream.close();
                    responseBody = buffer.toByteArray();
                } catch (OutOfMemoryError e) {
                    System.gc();
                    throw new IOException("File too large to fit into available memory");
                } catch (Throwable th) {
                    instream.close();
                }
            }
        }
        return responseBody;
    }
}
