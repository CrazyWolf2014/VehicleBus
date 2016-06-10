package com.googlecode.leptonica.android;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import java.io.File;

public class WriteFile {
    public static final boolean DEFAULT_PROGRESSIVE = true;
    public static final int DEFAULT_QUALITY = 85;

    private static native boolean nativeWriteBitmap(int i, Bitmap bitmap);

    private static native int nativeWriteBytes8(int i, byte[] bArr);

    private static native boolean nativeWriteFiles(int i, String str, int i2);

    private static native boolean nativeWriteImpliedFormat(int i, String str, int i2, boolean z);

    private static native byte[] nativeWriteMem(int i, int i2);

    static {
        System.loadLibrary("lept");
    }

    public static byte[] writeBytes8(Pix pixs) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        }
        int size = pixs.getWidth() * pixs.getHeight();
        if (pixs.getDepth() != 8) {
            Pix pix8 = Convert.convertTo8(pixs);
            pixs.recycle();
            pixs = pix8;
        }
        byte[] data = new byte[size];
        writeBytes8(pixs, data);
        return data;
    }

    public static int writeBytes8(Pix pixs, byte[] data) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        }
        if (data.length >= pixs.getWidth() * pixs.getHeight()) {
            return nativeWriteBytes8(pixs.mNativePix, data);
        }
        throw new IllegalArgumentException("Data array must be large enough to hold image bytes");
    }

    public static boolean writeFiles(Pixa pixas, File path, String prefix, int format) {
        if (pixas == null) {
            throw new IllegalArgumentException("Source pixa must be non-null");
        } else if (path == null) {
            throw new IllegalArgumentException("Destination path non-null");
        } else if (prefix == null) {
            throw new IllegalArgumentException("Filename prefix must be non-null");
        } else {
            throw new RuntimeException("writeFiles() is not currently supported");
        }
    }

    public static byte[] writeMem(Pix pixs, int format) {
        if (pixs != null) {
            return nativeWriteMem(pixs.mNativePix, format);
        }
        throw new IllegalArgumentException("Source pix must be non-null");
    }

    public static boolean writeImpliedFormat(Pix pixs, File file) {
        return writeImpliedFormat(pixs, file, DEFAULT_QUALITY, DEFAULT_PROGRESSIVE);
    }

    public static boolean writeImpliedFormat(Pix pixs, File file, int quality, boolean progressive) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        } else if (file != null) {
            return nativeWriteImpliedFormat(pixs.mNativePix, file.getAbsolutePath(), quality, progressive);
        } else {
            throw new IllegalArgumentException("File must be non-null");
        }
    }

    public static Bitmap writeBitmap(Pix pixs) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        }
        int[] dimensions = pixs.getDimensions();
        Bitmap bitmap = Bitmap.createBitmap(dimensions[0], dimensions[1], Config.ARGB_8888);
        if (nativeWriteBitmap(pixs.mNativePix, bitmap)) {
            return bitmap;
        }
        bitmap.recycle();
        return null;
    }
}
