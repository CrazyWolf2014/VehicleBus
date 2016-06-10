package com.googlecode.leptonica.android;

public class Convert {
    private static native int nativeConvertTo8(int i);

    static {
        System.loadLibrary("lept");
    }

    public static Pix convertTo8(Pix pixs) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        }
        int nativePix = nativeConvertTo8(pixs.mNativePix);
        if (nativePix != 0) {
            return new Pix(nativePix);
        }
        throw new RuntimeException("Failed to natively convert pix");
    }
}
