package org.codehaus.jackson;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;

public enum JsonEncoding {
    UTF8(AsyncHttpResponseHandler.DEFAULT_CHARSET, false),
    UTF16_BE("UTF-16BE", true),
    UTF16_LE("UTF-16LE", false),
    UTF32_BE("UTF-32BE", true),
    UTF32_LE("UTF-32LE", false);
    
    final boolean mBigEndian;
    final String mJavaName;

    private JsonEncoding(String javaName, boolean bigEndian) {
        this.mJavaName = javaName;
        this.mBigEndian = bigEndian;
    }

    public String getJavaName() {
        return this.mJavaName;
    }

    public boolean isBigEndian() {
        return this.mBigEndian;
    }
}
