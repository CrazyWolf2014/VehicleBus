package com.googlecode.leptonica.android;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;

public class Scale {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$googlecode$leptonica$android$Scale$ScaleType;

    public enum ScaleType {
        FILL,
        FIT,
        FIT_SHRINK
    }

    private static native int nativeScale(int i, float f, float f2);

    static /* synthetic */ int[] $SWITCH_TABLE$com$googlecode$leptonica$android$Scale$ScaleType() {
        int[] iArr = $SWITCH_TABLE$com$googlecode$leptonica$android$Scale$ScaleType;
        if (iArr == null) {
            iArr = new int[ScaleType.values().length];
            try {
                iArr[ScaleType.FILL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ScaleType.FIT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ScaleType.FIT_SHRINK.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$googlecode$leptonica$android$Scale$ScaleType = iArr;
        }
        return iArr;
    }

    static {
        System.loadLibrary("lept");
    }

    public static Pix scaleToSize(Pix pixs, int width, int height, ScaleType type) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        }
        float scaleX = ((float) width) / ((float) pixs.getWidth());
        float scaleY = ((float) height) / ((float) pixs.getHeight());
        switch ($SWITCH_TABLE$com$googlecode$leptonica$android$Scale$ScaleType()[type.ordinal()]) {
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                scaleX = Math.min(scaleX, scaleY);
                scaleY = scaleX;
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                scaleX = Math.min(1.0f, Math.min(scaleX, scaleY));
                scaleY = scaleX;
                break;
        }
        return scale(pixs, scaleX, scaleY);
    }

    public static Pix scale(Pix pixs, float scale) {
        return scale(pixs, scale, scale);
    }

    public static Pix scale(Pix pixs, float scaleX, float scaleY) {
        if (pixs == null) {
            throw new IllegalArgumentException("Source pix must be non-null");
        } else if (scaleX <= 0.0f) {
            throw new IllegalArgumentException("X scaling factor must be positive");
        } else if (scaleY <= 0.0f) {
            throw new IllegalArgumentException("Y scaling factor must be positive");
        } else {
            int nativePix = nativeScale(pixs.mNativePix, scaleX, scaleY);
            if (nativePix != 0) {
                return new Pix(nativePix);
            }
            throw new RuntimeException("Failed to natively scale pix");
        }
    }
}
