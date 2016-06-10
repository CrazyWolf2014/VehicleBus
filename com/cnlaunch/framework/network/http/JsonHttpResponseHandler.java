package com.cnlaunch.framework.network.http;

import android.util.Log;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonHttpResponseHandler extends TextHttpResponseHandler {
    private static final String LOG_TAG = "JsonHttpResponseHandler";

    /* renamed from: com.cnlaunch.framework.network.http.JsonHttpResponseHandler.1 */
    class C01231 implements Runnable {
        private final /* synthetic */ Header[] val$headers;
        private final /* synthetic */ String val$responseBody;
        private final /* synthetic */ int val$statusCode;

        /* renamed from: com.cnlaunch.framework.network.http.JsonHttpResponseHandler.1.1 */
        class C01211 implements Runnable {
            private final /* synthetic */ Header[] val$headers;
            private final /* synthetic */ Object val$jsonResponse;
            private final /* synthetic */ int val$statusCode;

            C01211(Object obj, int i, Header[] headerArr) {
                this.val$jsonResponse = obj;
                this.val$statusCode = i;
                this.val$headers = headerArr;
            }

            public void run() {
                if (this.val$jsonResponse instanceof JSONObject) {
                    JsonHttpResponseHandler.this.onSuccess(this.val$statusCode, this.val$headers, (JSONObject) this.val$jsonResponse);
                } else if (this.val$jsonResponse instanceof JSONArray) {
                    JsonHttpResponseHandler.this.onSuccess(this.val$statusCode, this.val$headers, (JSONArray) this.val$jsonResponse);
                } else if (this.val$jsonResponse instanceof String) {
                    JsonHttpResponseHandler.this.onSuccess(this.val$statusCode, this.val$headers, (String) this.val$jsonResponse);
                } else {
                    JsonHttpResponseHandler.this.onFailure(new JSONException("Unexpected type " + this.val$jsonResponse.getClass().getName()), null);
                }
            }
        }

        /* renamed from: com.cnlaunch.framework.network.http.JsonHttpResponseHandler.1.2 */
        class C01222 implements Runnable {
            private final /* synthetic */ JSONException val$ex;

            C01222(JSONException jSONException) {
                this.val$ex = jSONException;
            }

            public void run() {
                JsonHttpResponseHandler.this.onFailure(this.val$ex, null);
            }
        }

        C01231(String str, int i, Header[] headerArr) {
            this.val$responseBody = str;
            this.val$statusCode = i;
            this.val$headers = headerArr;
        }

        public void run() {
            try {
                JsonHttpResponseHandler.this.postRunnable(new C01211(JsonHttpResponseHandler.this.parseResponse(this.val$responseBody), this.val$statusCode, this.val$headers));
            } catch (JSONException ex) {
                JsonHttpResponseHandler.this.postRunnable(new C01222(ex));
            }
        }
    }

    /* renamed from: com.cnlaunch.framework.network.http.JsonHttpResponseHandler.2 */
    class C01262 implements Runnable {
        private final /* synthetic */ Throwable val$e;
        private final /* synthetic */ Header[] val$headers;
        private final /* synthetic */ String val$responseBody;
        private final /* synthetic */ int val$statusCode;

        /* renamed from: com.cnlaunch.framework.network.http.JsonHttpResponseHandler.2.1 */
        class C01241 implements Runnable {
            private final /* synthetic */ Throwable val$e;
            private final /* synthetic */ Header[] val$headers;
            private final /* synthetic */ Object val$jsonResponse;
            private final /* synthetic */ int val$statusCode;

            C01241(Object obj, int i, Header[] headerArr, Throwable th) {
                this.val$jsonResponse = obj;
                this.val$statusCode = i;
                this.val$headers = headerArr;
                this.val$e = th;
            }

            public void run() {
                if (this.val$jsonResponse instanceof JSONObject) {
                    JsonHttpResponseHandler.this.onFailure(this.val$statusCode, this.val$headers, this.val$e, (JSONObject) this.val$jsonResponse);
                } else if (this.val$jsonResponse instanceof JSONArray) {
                    JsonHttpResponseHandler.this.onFailure(this.val$statusCode, this.val$headers, this.val$e, (JSONArray) this.val$jsonResponse);
                } else if (this.val$jsonResponse instanceof String) {
                    JsonHttpResponseHandler.this.onFailure(this.val$statusCode, this.val$headers, this.val$e, (String) this.val$jsonResponse);
                } else {
                    JsonHttpResponseHandler.this.onFailure(new JSONException("Unexpected type " + this.val$jsonResponse.getClass().getName()), null);
                }
            }
        }

        /* renamed from: com.cnlaunch.framework.network.http.JsonHttpResponseHandler.2.2 */
        class C01252 implements Runnable {
            private final /* synthetic */ JSONException val$ex;
            private final /* synthetic */ Header[] val$headers;
            private final /* synthetic */ int val$statusCode;

            C01252(int i, Header[] headerArr, JSONException jSONException) {
                this.val$statusCode = i;
                this.val$headers = headerArr;
                this.val$ex = jSONException;
            }

            public void run() {
                JsonHttpResponseHandler.this.onFailure(this.val$statusCode, this.val$headers, this.val$ex, null);
            }
        }

        C01262(String str, int i, Header[] headerArr, Throwable th) {
            this.val$responseBody = str;
            this.val$statusCode = i;
            this.val$headers = headerArr;
            this.val$e = th;
        }

        public void run() {
            try {
                JsonHttpResponseHandler.this.postRunnable(new C01241(JsonHttpResponseHandler.this.parseResponse(this.val$responseBody), this.val$statusCode, this.val$headers, this.val$e));
            } catch (JSONException ex) {
                JsonHttpResponseHandler.this.postRunnable(new C01252(this.val$statusCode, this.val$headers, ex));
            }
        }
    }

    public JsonHttpResponseHandler() {
        super(AsyncHttpResponseHandler.DEFAULT_CHARSET);
    }

    public JsonHttpResponseHandler(String encoding) {
        super(encoding);
    }

    public void onSuccess(JSONObject response) {
    }

    public void onSuccess(JSONArray response) {
    }

    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        onSuccess(statusCode, response);
    }

    public void onSuccess(int statusCode, JSONObject response) {
        onSuccess(response);
    }

    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        onSuccess(statusCode, response);
    }

    public void onSuccess(int statusCode, JSONArray response) {
        onSuccess(response);
    }

    public void onFailure(Throwable e, JSONObject errorResponse) {
        onFailure(e);
    }

    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
        onFailure(e, errorResponse);
    }

    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
        onFailure(statusCode, e, errorResponse);
    }

    public void onFailure(Throwable e, JSONArray errorResponse) {
        onFailure(e);
    }

    public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
        onFailure(e, errorResponse);
    }

    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray errorResponse) {
        onFailure(statusCode, e, errorResponse);
    }

    public void onSuccess(int statusCode, Header[] headers, String responseBody) {
        if (statusCode != 204) {
            new Thread(new C01231(responseBody, statusCode, headers)).start();
        } else {
            onSuccess(statusCode, headers, new JSONObject());
        }
    }

    public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable e) {
        if (responseBody != null) {
            new Thread(new C01262(responseBody, statusCode, headers, e)).start();
            return;
        }
        Log.v(LOG_TAG, "response body is null, calling onFailure(Throwable, JSONObject)");
        onFailure(statusCode, headers, e, null);
    }

    protected Object parseResponse(String responseBody) throws JSONException {
        if (responseBody == null) {
            return null;
        }
        Object result = null;
        String jsonString = responseBody.trim();
        if (jsonString.startsWith("{") || jsonString.startsWith("[")) {
            result = new JSONTokener(jsonString).nextValue();
        }
        if (result == null) {
            return jsonString;
        }
        return result;
    }
}
