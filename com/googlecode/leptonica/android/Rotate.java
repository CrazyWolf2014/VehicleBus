package com.googlecode.leptonica.android;

public class Rotate {
    public static final boolean ROTATE_QUALITY = true;

    private static native int nativeRotate(int i, float f, boolean z, boolean z2);

    static {
        System.loadLibrary("lept");
    }

    public static Pix rotate(Pix pixs, float degrees) {
        return rotate(pixs, degrees, false);
    }

    public static Pix rotate(Pix pixs, float degrees, boolean quality) {
        return rotate(pixs, degrees, quality, ROTATE_QUALITY);
    }

    public static Pix rotate(Pix pixs, float degrees, boolean quality, boolean resize) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        }
        int nativePix = nativeRotate(pixs.mNativePix, degrees, quality, resize);
        if (nativePix == 0) {
            return null;
        }
        return new Pix(nativePix);
    }
}
