package com.googlecode.tesseract.android;

public class ResultIterator extends PageIterator {
    private final int mNativeResultIterator;

    private static native float nativeConfidence(int i, int i2);

    private static native String nativeGetUTF8Text(int i, int i2);

    static {
        System.loadLibrary("lept");
        System.loadLibrary("tess");
    }

    ResultIterator(int nativeResultIterator) {
        super(nativeResultIterator);
        this.mNativeResultIterator = nativeResultIterator;
    }

    public String getUTF8Text(int level) {
        return nativeGetUTF8Text(this.mNativeResultIterator, level);
    }

    public float confidence(int level) {
        return nativeConfidence(this.mNativeResultIterator, level);
    }
}
