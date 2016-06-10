package com.cnlaunch.framework.network.http;

import android.util.Log;
import org.apache.http.Header;

public class TextHttpResponseHandler extends AsyncHttpResponseHandler {
    private static final String LOG_TAG = "TextHttpResponseHandler";

    public TextHttpResponseHandler() {
        this(AsyncHttpResponseHandler.DEFAULT_CHARSET);
    }

    public TextHttpResponseHandler(String encoding) {
        setCharset(encoding);
    }

    public void onFailure(String responseBody, Throwable error) {
    }

    public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
        onFailure(responseBody, error);
    }

    public void onSuccess(int statusCode, Header[] headers, String responseBody) {
        onSuccess(statusCode, responseBody);
    }

    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            onSuccess(statusCode, headers, responseBody == null ? null : new String(responseBody, getCharset()));
        } catch (Throwable e) {
            Log.v(LOG_TAG, "String encoding failed, calling onFailure(int, Header[], String, Throwable)");
            onFailure(0, headers, null, e);
        }
    }

    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        try {
            onFailure(statusCode, headers, responseBody == null ? null : new String(responseBody, getCharset()), error);
        } catch (Throwable e) {
            Log.v(LOG_TAG, "String encoding failed, calling onFailure(int, Header[], String, Throwable)");
            onFailure(0, headers, null, e);
        }
    }
}
