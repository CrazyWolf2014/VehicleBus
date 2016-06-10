package com.googlecode.leptonica.android;

public class Enhance {
    private static native int nativeUnsharpMasking(int i, int i2, float f);

    static {
        System.loadLibrary("lept");
    }

    public static Pix unsharpMasking(Pix pixs, int halfwidth, float fraction) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        }
        int nativePix = nativeUnsharpMasking(pixs.mNativePix, halfwidth, fraction);
        if (nativePix != 0) {
            return new Pix(nativePix);
        }
        throw new OutOfMemoryError();
    }
}
