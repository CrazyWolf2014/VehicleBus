package com.ifoer.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.widget.ImageView.ScaleType;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;

public class ScaleImageProcessor implements ImageProcessor {
    private static /* synthetic */ int[] $SWITCH_TABLE$android$widget$ImageView$ScaleType;
    private int mHeight;
    private final Matrix mMatrix;
    private ScaleType mScaleType;
    private final RectF mTempDst;
    private final RectF mTempSrc;
    private int mWidth;

    static /* synthetic */ int[] $SWITCH_TABLE$android$widget$ImageView$ScaleType() {
        int[] iArr = $SWITCH_TABLE$android$widget$ImageView$ScaleType;
        if (iArr == null) {
            iArr = new int[ScaleType.values().length];
            try {
                iArr[ScaleType.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ScaleType.CENTER_CROP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ScaleType.CENTER_INSIDE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ScaleType.FIT_CENTER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[ScaleType.FIT_END.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[ScaleType.FIT_START.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[ScaleType.FIT_XY.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[ScaleType.MATRIX.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            $SWITCH_TABLE$android$widget$ImageView$ScaleType = iArr;
        }
        return iArr;
    }

    public ScaleImageProcessor(int width, int height, ScaleType scaleType) {
        this.mMatrix = new Matrix();
        this.mTempSrc = new RectF();
        this.mTempDst = new RectF();
        this.mWidth = width;
        this.mHeight = height;
        this.mScaleType = scaleType;
    }

    public Bitmap processImage(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        this.mMatrix.reset();
        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();
        float dx;
        float dy;
        float scale;
        switch ($SWITCH_TABLE$android$widget$ImageView$ScaleType()[this.mScaleType.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.mMatrix.setTranslate((float) ((int) ((((float) (this.mWidth - bWidth)) * 0.5f) + 0.5f)), (float) ((int) ((((float) (this.mHeight - bHeight)) * 0.5f) + 0.5f)));
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                dx = 0.0f;
                dy = 0.0f;
                if (this.mHeight * bWidth > this.mWidth * bHeight) {
                    scale = ((float) this.mHeight) / ((float) bHeight);
                    dx = (((float) this.mWidth) - (((float) bWidth) * scale)) * 0.5f;
                } else {
                    scale = ((float) this.mWidth) / ((float) bWidth);
                    dy = (((float) this.mHeight) - (((float) bHeight) * scale)) * 0.5f;
                }
                this.mMatrix.setScale(scale, scale);
                this.mMatrix.postTranslate((float) ((int) (dx + 0.5f)), (float) ((int) (dy + 0.5f)));
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                if (bWidth > this.mWidth || bHeight > this.mHeight) {
                    scale = Math.min(((float) this.mWidth) / ((float) bWidth), ((float) this.mHeight) / ((float) bHeight));
                } else {
                    scale = 1.0f;
                }
                dx = (float) ((int) (((((float) this.mWidth) - (((float) bWidth) * scale)) * 0.5f) + 0.5f));
                dy = (float) ((int) (((((float) this.mHeight) - (((float) bHeight) * scale)) * 0.5f) + 0.5f));
                this.mMatrix.setScale(scale, scale);
                this.mMatrix.postTranslate(dx, dy);
                break;
            default:
                this.mTempSrc.set(0.0f, 0.0f, (float) bWidth, (float) bHeight);
                this.mTempDst.set(0.0f, 0.0f, (float) this.mWidth, (float) this.mHeight);
                this.mMatrix.setRectToRect(this.mTempSrc, this.mTempDst, ScaleToFit.FILL);
                break;
        }
        Bitmap result = Bitmap.createBitmap(this.mWidth, this.mHeight, Config.ARGB_8888);
        new Canvas(result).drawBitmap(bitmap, this.mMatrix, null);
        return result;
    }
}
