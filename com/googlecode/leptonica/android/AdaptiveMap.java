package com.googlecode.leptonica.android;

public class AdaptiveMap {
    private static final int NORM_BG_VALUE = 200;
    private static final int NORM_REDUCTION = 16;
    private static final int NORM_SIZE = 3;

    private static native int nativeBackgroundNormMorph(int i, int i2, int i3, int i4);

    static {
        System.loadLibrary("lept");
    }

    public static Pix backgroundNormMorph(Pix pixs) {
        return backgroundNormMorph(pixs, NORM_REDUCTION, NORM_SIZE, NORM_BG_VALUE);
    }

    public static Pix backgroundNormMorph(Pix pixs, int normReduction, int normSize, int normBgValue) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        }
        int nativePix = nativeBackgroundNormMorph(pixs.mNativePix, normReduction, normSize, normBgValue);
        if (nativePix != 0) {
            return new Pix(nativePix);
        }
        throw new RuntimeException("Failed to normalize image background");
    }
}
