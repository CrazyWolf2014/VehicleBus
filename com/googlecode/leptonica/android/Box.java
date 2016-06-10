package com.googlecode.leptonica.android;

public class Box {
    public static final int INDEX_H = 3;
    public static final int INDEX_W = 2;
    public static final int INDEX_X = 0;
    public static final int INDEX_Y = 1;
    final int mNativeBox;
    private boolean mRecycled;

    private static native int nativeCreate(int i, int i2, int i3, int i4);

    private static native void nativeDestroy(int i);

    private static native boolean nativeGetGeometry(int i, int[] iArr);

    private static native int nativeGetHeight(int i);

    private static native int nativeGetWidth(int i);

    private static native int nativeGetX(int i);

    private static native int nativeGetY(int i);

    static {
        System.loadLibrary("lept");
    }

    Box(int nativeBox) {
        this.mRecycled = false;
        this.mNativeBox = nativeBox;
        this.mRecycled = false;
    }

    public Box(int x, int y, int w, int h) {
        this.mRecycled = false;
        if (x < 0 || y < 0 || w < 0 || h < 0) {
            throw new IllegalArgumentException("All box dimensions must be non-negative");
        }
        int nativeBox = nativeCreate(x, y, w, h);
        if (nativeBox == 0) {
            throw new OutOfMemoryError();
        }
        this.mNativeBox = nativeBox;
        this.mRecycled = false;
    }

    public int getX() {
        return nativeGetX(this.mNativeBox);
    }

    public int getY() {
        return nativeGetY(this.mNativeBox);
    }

    public int getWidth() {
        return nativeGetWidth(this.mNativeBox);
    }

    public int getHeight() {
        return nativeGetHeight(this.mNativeBox);
    }

    public int[] getGeometry() {
        int[] geometry = new int[4];
        return getGeometry(geometry) ? geometry : null;
    }

    public boolean getGeometry(int[] geometry) {
        if (geometry.length >= 4) {
            return nativeGetGeometry(this.mNativeBox, geometry);
        }
        throw new IllegalArgumentException("Geometry array must be at least 4 elements long");
    }

    public void recycle() {
        if (!this.mRecycled) {
            nativeDestroy(this.mNativeBox);
            this.mRecycled = true;
        }
    }

    protected void finalize() throws Throwable {
        recycle();
        super.finalize();
    }
}
