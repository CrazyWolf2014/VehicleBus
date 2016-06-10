package com.googlecode.tesseract.android;

public class PageIterator {
    private final int mNativePageIterator;

    private static native void nativeBegin(int i);

    private static native boolean nativeNext(int i, int i2);

    static {
        System.loadLibrary("lept");
        System.loadLibrary("tess");
    }

    PageIterator(int nativePageIterator) {
        this.mNativePageIterator = nativePageIterator;
    }

    public void begin() {
        nativeBegin(this.mNativePageIterator);
    }

    public boolean next(int level) {
        return nativeNext(this.mNativePageIterator, level);
    }
}
