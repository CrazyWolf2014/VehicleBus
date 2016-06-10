package com.cnlaunch.framework.network.http;

import android.util.Log;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.mime.MIME;

public class BinaryHttpResponseHandler extends AsyncHttpResponseHandler {
    private String[] mAllowedContentTypes;

    public String[] getAllowedContentTypes() {
        return this.mAllowedContentTypes;
    }

    public BinaryHttpResponseHandler() {
        this.mAllowedContentTypes = new String[]{"image/jpeg", "image/png"};
    }

    public BinaryHttpResponseHandler(String[] allowedContentTypes) {
        this();
        this.mAllowedContentTypes = allowedContentTypes;
    }

    public void onSuccess(byte[] binaryData) {
    }

    public void onSuccess(int statusCode, byte[] binaryData) {
        onSuccess(binaryData);
    }

    public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
        onSuccess(statusCode, binaryData);
    }

    public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
        onFailure(statusCode, error, null);
    }

    public final void sendResponseMessage(HttpResponse response) throws IOException {
        int i = 0;
        StatusLine status = response.getStatusLine();
        Header[] contentTypeHeaders = response.getHeaders(MIME.CONTENT_TYPE);
        if (contentTypeHeaders.length != 1) {
            sendFailureMessage(status.getStatusCode(), response.getAllHeaders(), null, new HttpResponseException(status.getStatusCode(), "None, or more than one, Content-Type Header found!"));
            return;
        }
        Header contentTypeHeader = contentTypeHeaders[0];
        boolean foundAllowedContentType = false;
        String[] allowedContentTypes = getAllowedContentTypes();
        int length = allowedContentTypes.length;
        while (i < length) {
            String anAllowedContentType = allowedContentTypes[i];
            try {
                if (Pattern.matches(anAllowedContentType, contentTypeHeader.getValue())) {
                    foundAllowedContentType = true;
                }
            } catch (PatternSyntaxException e) {
                Log.e("BinaryHttpResponseHandler", "Given pattern is not valid: " + anAllowedContentType, e);
            }
            i++;
        }
        if (foundAllowedContentType) {
            super.sendResponseMessage(response);
        } else {
            sendFailureMessage(status.getStatusCode(), response.getAllHeaders(), null, new HttpResponseException(status.getStatusCode(), "Content-Type not allowed!"));
        }
    }
}
