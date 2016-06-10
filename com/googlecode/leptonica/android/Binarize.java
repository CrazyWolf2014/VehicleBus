package com.googlecode.leptonica.android;

public class Binarize {
    public static final float OTSU_SCORE_FRACTION = 0.1f;
    public static final int OTSU_SIZE_X = 32;
    public static final int OTSU_SIZE_Y = 32;
    public static final int OTSU_SMOOTH_X = 2;
    public static final int OTSU_SMOOTH_Y = 2;

    private static native int nativeOtsuAdaptiveThreshold(int i, int i2, int i3, int i4, int i5, float f);

    private static native int nativeSauvolaBinarizeTiled(int i, int i2, float f, int i3, int i4);

    static {
        System.loadLibrary("lept");
    }

    public static Pix otsuAdaptiveThreshold(Pix pixs) {
        return otsuAdaptiveThreshold(pixs, OTSU_SIZE_Y, OTSU_SIZE_Y, OTSU_SMOOTH_Y, OTSU_SMOOTH_Y, OTSU_SCORE_FRACTION);
    }

    public static Pix otsuAdaptiveThreshold(Pix pixs, int sizeX, int sizeY, int smoothX, int smoothY, float scoreFraction) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        } else if (pixs.getDepth() != 8) {
            throw new IllegalArgumentException("Source pix depth must be 8bpp");
        } else {
            int nativePix = nativeOtsuAdaptiveThreshold(pixs.mNativePix, sizeX, sizeY, smoothX, smoothY, scoreFraction);
            if (nativePix != 0) {
                return new Pix(nativePix);
            }
            throw new RuntimeException("Failed to perform Otsu adaptive threshold on image");
        }
    }

    public static Pix sauvolaBinarizeTiled(Pix pixs, int whsize, float factor, int nx, int ny) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        } else if (pixs.getDepth() != 8) {
            throw new IllegalArgumentException("Source pix depth must be 8bpp");
        } else {
            int nativePix = nativeSauvolaBinarizeTiled(pixs.mNativePix, whsize, factor, nx, ny);
            if (nativePix != 0) {
                return new Pix(nativePix);
            }
            throw new RuntimeException("Failed to perform Otsu adaptive threshold on image");
        }
    }
}
