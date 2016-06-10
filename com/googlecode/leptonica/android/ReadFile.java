package com.googlecode.leptonica.android;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import java.io.File;

public class ReadFile {
    private static native int nativeReadBitmap(Bitmap bitmap);

    private static native int nativeReadBytes8(byte[] bArr, int i, int i2);

    private static native int nativeReadFile(String str);

    private static native int nativeReadFiles(String str, String str2);

    private static native int nativeReadMem(byte[] bArr, int i);

    private static native boolean nativeReplaceBytes8(int i, byte[] bArr, int i2, int i3);

    static {
        System.loadLibrary("lept");
    }

    public static Pix readMem(byte[] encodedData) {
        if (encodedData == null) {
            throw new IllegalArgumentException("Image data byte array must be non-null");
        }
        Options opts = new Options();
        opts.inPreferredConfig = Config.ARGB_8888;
        Bitmap bmp = BitmapFactory.decodeByteArray(encodedData, 0, encodedData.length, opts);
        Pix pix = readBitmap(bmp);
        bmp.recycle();
        return pix;
    }

    public static Pix readBytes8(byte[] pixelData, int width, int height) {
        if (pixelData == null) {
            throw new IllegalArgumentException("Byte array must be non-null");
        } else if (width <= 0) {
            throw new IllegalArgumentException("Image width must be greater than 0");
        } else if (height <= 0) {
            throw new IllegalArgumentException("Image height must be greater than 0");
        } else if (pixelData.length < width * height) {
            throw new IllegalArgumentException("Array length does not match dimensions");
        } else {
            int nativePix = nativeReadBytes8(pixelData, width, height);
            if (nativePix != 0) {
                return new Pix(nativePix);
            }
            throw new RuntimeException("Failed to read pix from memory");
        }
    }

    public static boolean replaceBytes8(Pix pixs, byte[] pixelData, int width, int height) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        } else if (pixelData == null) {
            throw new IllegalArgumentException("Byte array must be non-null");
        } else if (width <= 0) {
            throw new IllegalArgumentException("Image width must be greater than 0");
        } else if (height <= 0) {
            throw new IllegalArgumentException("Image height must be greater than 0");
        } else if (pixelData.length < width * height) {
            throw new IllegalArgumentException("Array length does not match dimensions");
        } else if (pixs.getWidth() != width) {
            throw new IllegalArgumentException("Source pix width does not match image width");
        } else if (pixs.getHeight() == height) {
            return nativeReplaceBytes8(pixs.mNativePix, pixelData, width, height);
        } else {
            throw new IllegalArgumentException("Source pix width does not match image width");
        }
    }

    public static Pixa readFiles(File dir, String prefix) {
        if (dir == null) {
            throw new IllegalArgumentException("Directory must be non-null");
        } else if (!dir.exists()) {
            throw new IllegalArgumentException("Directory does not exist");
        } else if (dir.canRead()) {
            throw new RuntimeException("readFiles() is not current supported");
        } else {
            throw new IllegalArgumentException("Cannot read directory");
        }
    }

    public static Pix readFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File must be non-null");
        } else if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist");
        } else if (file.canRead()) {
            Options opts = new Options();
            opts.inPreferredConfig = Config.ARGB_8888;
            Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
            Pix pix = readBitmap(bmp);
            bmp.recycle();
            return pix;
        } else {
            throw new IllegalArgumentException("Cannot read file");
        }
    }

    public static Pix readBitmap(Bitmap bmp) {
        if (bmp == null) {
            throw new IllegalArgumentException("Bitmap must be non-null");
        } else if (bmp.getConfig() != Config.ARGB_8888) {
            throw new IllegalArgumentException("Bitmap config must be ARGB_8888");
        } else {
            int nativePix = nativeReadBitmap(bmp);
            if (nativePix != 0) {
                return new Pix(nativePix);
            }
            throw new RuntimeException("Failed to read pix from bitmap");
        }
    }
}
