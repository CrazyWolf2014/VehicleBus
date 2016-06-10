package com.googlecode.leptonica.android;

import android.graphics.Rect;

public class Pix {
    public static final int INDEX_D = 2;
    public static final int INDEX_H = 1;
    public static final int INDEX_W = 0;
    final int mNativePix;
    private boolean mRecycled;

    private static native int nativeClone(int i);

    private static native int nativeCopy(int i);

    private static native int nativeCreateFromData(byte[] bArr, int i, int i2, int i3);

    private static native int nativeCreatePix(int i, int i2, int i3);

    private static native void nativeDestroy(int i);

    private static native boolean nativeGetData(int i, byte[] bArr);

    private static native int nativeGetDataSize(int i);

    private static native int nativeGetDepth(int i);

    private static native boolean nativeGetDimensions(int i, int[] iArr);

    private static native int nativeGetHeight(int i);

    private static native int nativeGetPixel(int i, int i2, int i3);

    private static native int nativeGetWidth(int i);

    private static native boolean nativeInvert(int i);

    private static native void nativeSetPixel(int i, int i2, int i3, int i4);

    static {
        System.loadLibrary("lept");
    }

    public Pix(int nativePix) {
        this.mNativePix = nativePix;
        this.mRecycled = false;
    }

    public Pix(int width, int height, int depth) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Pix width and height must be > 0");
        } else if (depth == INDEX_H || depth == INDEX_D || depth == 4 || depth == 8 || depth == 16 || depth == 24 || depth == 32) {
            this.mNativePix = nativeCreatePix(width, height, depth);
            this.mRecycled = false;
        } else {
            throw new IllegalArgumentException("Depth must be one of 1, 2, 4, 8, 16, or 32");
        }
    }

    public int getNativePix() {
        return this.mNativePix;
    }

    public byte[] getData() {
        byte[] buffer = new byte[nativeGetDataSize(this.mNativePix)];
        if (nativeGetData(this.mNativePix, buffer)) {
            return buffer;
        }
        throw new RuntimeException("native getData failed");
    }

    public int[] getDimensions() {
        int[] dimensions = new int[4];
        return getDimensions(dimensions) ? dimensions : null;
    }

    public boolean getDimensions(int[] dimensions) {
        return nativeGetDimensions(this.mNativePix, dimensions);
    }

    public Pix clone() {
        int nativePix = nativeClone(this.mNativePix);
        if (nativePix != 0) {
            return new Pix(nativePix);
        }
        throw new OutOfMemoryError();
    }

    public Pix copy() {
        int nativePix = nativeCopy(this.mNativePix);
        if (nativePix != 0) {
            return new Pix(nativePix);
        }
        throw new OutOfMemoryError();
    }

    public boolean invert() {
        return nativeInvert(this.mNativePix);
    }

    public void recycle() {
        if (!this.mRecycled) {
            nativeDestroy(this.mNativePix);
            this.mRecycled = true;
        }
    }

    protected void finalize() throws Throwable {
        recycle();
        super.finalize();
    }

    public static Pix createFromPix(byte[] pixData, int width, int height, int depth) {
        int nativePix = nativeCreateFromData(pixData, width, height, depth);
        if (nativePix != 0) {
            return new Pix(nativePix);
        }
        throw new OutOfMemoryError();
    }

    public Rect getRect() {
        return new Rect(0, 0, getWidth(), getHeight());
    }

    public int getWidth() {
        return nativeGetWidth(this.mNativePix);
    }

    public int getHeight() {
        return nativeGetHeight(this.mNativePix);
    }

    public int getDepth() {
        return nativeGetDepth(this.mNativePix);
    }

    public int getPixel(int x, int y) {
        if (x < 0 || x >= getWidth()) {
            throw new IllegalArgumentException("Supplied x coordinate exceeds image bounds");
        } else if (y >= 0 && y < getHeight()) {
            return nativeGetPixel(this.mNativePix, x, y);
        } else {
            throw new IllegalArgumentException("Supplied x coordinate exceeds image bounds");
        }
    }

    public void setPixel(int x, int y, int color) {
        if (x < 0 || x >= getWidth()) {
            throw new IllegalArgumentException("Supplied x coordinate exceeds image bounds");
        } else if (y < 0 || y >= getHeight()) {
            throw new IllegalArgumentException("Supplied x coordinate exceeds image bounds");
        } else {
            nativeSetPixel(this.mNativePix, x, y, color);
        }
    }
}
