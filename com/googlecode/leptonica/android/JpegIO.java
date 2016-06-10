package com.googlecode.leptonica.android;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JpegIO {
    public static final boolean DEFAULT_PROGRESSIVE = false;
    public static final int DEFAULT_QUALITY = 85;

    private static native byte[] nativeCompressToJpeg(int i, int i2, boolean z);

    static {
        System.loadLibrary("lept");
    }

    public static byte[] compressToJpeg(Pix pixs) {
        return compressToJpeg(pixs, DEFAULT_QUALITY, DEFAULT_PROGRESSIVE);
    }

    public static byte[] compressToJpeg(Pix pixs, int quality, boolean progressive) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        } else if (quality < 0 || quality > 100) {
            throw new IllegalArgumentException("Quality must be between 0 and 100 (inclusive)");
        } else {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            Bitmap bmp = WriteFile.writeBitmap(pixs);
            bmp.compress(CompressFormat.JPEG, quality, byteStream);
            bmp.recycle();
            byte[] encodedData = byteStream.toByteArray();
            try {
                byteStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return encodedData;
        }
    }
}
