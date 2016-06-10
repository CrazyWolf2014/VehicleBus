package com.cnlaunch.framework.network.http;

import android.util.Log;
import org.apache.http.Header;

public abstract class BaseJsonHttpResponseHandler<JSON_TYPE> extends TextHttpResponseHandler {
    private static final String LOG_TAG = "BaseJsonHttpResponseHandler";

    /* renamed from: com.cnlaunch.framework.network.http.BaseJsonHttpResponseHandler.1 */
    class C01171 implements Runnable {
        private final /* synthetic */ Header[] val$headers;
        private final /* synthetic */ String val$responseBody;
        private final /* synthetic */ int val$statusCode;

        /* renamed from: com.cnlaunch.framework.network.http.BaseJsonHttpResponseHandler.1.1 */
        class C01151 implements Runnable {
            private final /* synthetic */ Header[] val$headers;
            private final /* synthetic */ Object val$jsonResponse;
            private final /* synthetic */ String val$responseBody;
            private final /* synthetic */ int val$statusCode;

            C01151(int i, Header[] headerArr, String str, Object obj) {
                this.val$statusCode = i;
                this.val$headers = headerArr;
                this.val$responseBody = str;
                this.val$jsonResponse = obj;
            }

            public void run() {
                BaseJsonHttpResponseHandler.this.onSuccess(this.val$statusCode, this.val$headers, this.val$responseBody, this.val$jsonResponse);
            }
        }

        /* renamed from: com.cnlaunch.framework.network.http.BaseJsonHttpResponseHandler.1.2 */
        class C01162 implements Runnable {
            private final /* synthetic */ Header[] val$headers;
            private final /* synthetic */ String val$responseBody;
            private final /* synthetic */ int val$statusCode;
            private final /* synthetic */ Throwable val$t;

            C01162(int i, Header[] headerArr, Throwable th, String str) {
                this.val$statusCode = i;
                this.val$headers = headerArr;
                this.val$t = th;
                this.val$responseBody = str;
            }

            public void run() {
                BaseJsonHttpResponseHandler.this.onFailure(this.val$statusCode, this.val$headers, this.val$t, this.val$responseBody, null);
            }
        }

        C01171(String str, int i, Header[] headerArr) {
            this.val$responseBody = str;
            this.val$statusCode = i;
            this.val$headers = headerArr;
        }

        public void run() {
            try {
                BaseJsonHttpResponseHandler.this.postRunnable(new C01151(this.val$statusCode, this.val$headers, this.val$responseBody, BaseJsonHttpResponseHandler.this.parseResponse(this.val$responseBody)));
            } catch (Throwable t) {
                Log.d(BaseJsonHttpResponseHandler.LOG_TAG, "parseResponse thrown an problem", t);
                BaseJsonHttpResponseHandler.this.postRunnable(new C01162(this.val$statusCode, this.val$headers, t, this.val$responseBody));
            }
        }
    }

    /* renamed from: com.cnlaunch.framework.network.http.BaseJsonHttpResponseHandler.2 */
    class C01202 implements Runnable {
        private final /* synthetic */ Throwable val$e;
        private final /* synthetic */ Header[] val$headers;
        private final /* synthetic */ String val$responseBody;
        private final /* synthetic */ int val$statusCode;

        /* renamed from: com.cnlaunch.framework.network.http.BaseJsonHttpResponseHandler.2.1 */
        class C01181 implements Runnable {
            private final /* synthetic */ Throwable val$e;
            private final /* synthetic */ Header[] val$headers;
            private final /* synthetic */ Object val$jsonResponse;
            private final /* synthetic */ String val$responseBody;
            private final /* synthetic */ int val$statusCode;

            C01181(int i, Header[] headerArr, Throwable th, String str, Object obj) {
                this.val$statusCode = i;
                this.val$headers = headerArr;
                this.val$e = th;
                this.val$responseBody = str;
                this.val$jsonResponse = obj;
            }

            public void run() {
                BaseJsonHttpResponseHandler.this.onFailure(this.val$statusCode, this.val$headers, this.val$e, this.val$responseBody, this.val$jsonResponse);
            }
        }

        /* renamed from: com.cnlaunch.framework.network.http.BaseJsonHttpResponseHandler.2.2 */
        class C01192 implements Runnable {
            private final /* synthetic */ Throwable val$e;
            private final /* synthetic */ Header[] val$headers;
            private final /* synthetic */ String val$responseBody;
            private final /* synthetic */ int val$statusCode;

            C01192(int i, Header[] headerArr, Throwable th, String str) {
                this.val$statusCode = i;
                this.val$headers = headerArr;
                this.val$e = th;
                this.val$responseBody = str;
            }

            public void run() {
                BaseJsonHttpResponseHandler.this.onFailure(this.val$statusCode, this.val$headers, this.val$e, this.val$responseBody, null);
            }
        }

        C01202(String str, int i, Header[] headerArr, Throwable th) {
            this.val$responseBody = str;
            this.val$statusCode = i;
            this.val$headers = headerArr;
            this.val$e = th;
        }

        public void run() {
            try {
                BaseJsonHttpResponseHandler.this.postRunnable(new C01181(this.val$statusCode, this.val$headers, this.val$e, this.val$responseBody, BaseJsonHttpResponseHandler.this.parseResponse(this.val$responseBody)));
            } catch (Throwable t) {
                Log.d(BaseJsonHttpResponseHandler.LOG_TAG, "parseResponse thrown an problem", t);
                BaseJsonHttpResponseHandler.this.postRunnable(new C01192(this.val$statusCode, this.val$headers, this.val$e, this.val$responseBody));
            }
        }
    }

    public abstract void onFailure(int i, Header[] headerArr, Throwable th, String str, JSON_TYPE json_type);

    public abstract void onSuccess(int i, Header[] headerArr, String str, JSON_TYPE json_type);

    protected abstract JSON_TYPE parseResponse(String str) throws Throwable;

    public BaseJsonHttpResponseHandler() {
        super(AsyncHttpResponseHandler.DEFAULT_CHARSET);
    }

    public BaseJsonHttpResponseHandler(String encoding) {
        super(encoding);
    }

    public final void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        super.onSuccess(statusCode, headers, responseBody);
    }

    public final void onSuccess(String content) {
        super.onSuccess(content);
    }

    public final void onSuccess(int statusCode, String content) {
        super.onSuccess(statusCode, content);
    }

    public final void onFailure(String responseBody, Throwable error) {
        super.onFailure(responseBody, error);
    }

    public final void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        super.onFailure(statusCode, headers, responseBody, error);
    }

    public final void onFailure(Throwable error) {
        super.onFailure(error);
    }

    public final void onFailure(Throwable error, String content) {
        super.onFailure(error, content);
    }

    public final void onFailure(int statusCode, Throwable error, String content) {
        super.onFailure(statusCode, error, content);
    }

    public final void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
        super.onFailure(statusCode, headers, error, content);
    }

    public void onSuccess(int statusCode, Header[] headers, String responseBody) {
        if (statusCode != 204) {
            new Thread(new C01171(responseBody, statusCode, headers)).start();
        } else {
            onSuccess(statusCode, headers, null, null);
        }
    }

    public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable e) {
        if (responseBody != null) {
            new Thread(new C01202(responseBody, statusCode, headers, e)).start();
        } else {
            onFailure(statusCode, headers, e, null, null);
        }
    }
}
