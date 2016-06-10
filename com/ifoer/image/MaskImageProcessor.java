package com.ifoer.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

public class MaskImageProcessor implements ImageProcessor {
    private static final int CUSTOM = 1;
    private static final int RECTANGLE = 2;
    private final Paint mFillPaint;
    private Bitmap mMaskBitmap;
    private final Paint mMaskPaint;
    private final Path mPath;
    private float[] mRadiiArray;
    private float mRadius;
    private final RectF mRect;
    private int mShape;

    public MaskImageProcessor(float radius) {
        this.mMaskPaint = new Paint(CUSTOM);
        this.mFillPaint = new Paint(CUSTOM);
        this.mPath = new Path();
        this.mRect = new RectF();
        init();
        this.mShape = RECTANGLE;
        if (radius < 0.0f) {
            radius = 0.0f;
        }
        this.mRadius = radius;
    }

    public MaskImageProcessor(float[] radii) {
        this.mMaskPaint = new Paint(CUSTOM);
        this.mFillPaint = new Paint(CUSTOM);
        this.mPath = new Path();
        this.mRect = new RectF();
        init();
        this.mShape = RECTANGLE;
        this.mRadiiArray = radii;
        if (radii == null) {
            this.mRadius = 0.0f;
        }
    }

    public MaskImageProcessor(Bitmap maskBitmap) {
        this.mMaskPaint = new Paint(CUSTOM);
        this.mFillPaint = new Paint(CUSTOM);
        this.mPath = new Path();
        this.mRect = new RectF();
        init();
        this.mShape = CUSTOM;
        this.mMaskBitmap = maskBitmap;
    }

    private void init() {
        this.mFillPaint.setColor(-65536);
        this.mMaskPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    }

    public Bitmap processImage(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        this.mRect.set(0.0f, 0.0f, (float) width, (float) height);
        Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        switch (this.mShape) {
            case CUSTOM /*1*/:
                canvas.drawBitmap(this.mMaskBitmap, 0.0f, 0.0f, this.mFillPaint);
                break;
            default:
                if (this.mRadiiArray == null) {
                    float rad = this.mRadius;
                    float r = ((float) Math.min(width, height)) * 0.5f;
                    if (rad > r) {
                        rad = r;
                    }
                    canvas.drawRoundRect(this.mRect, rad, rad, this.mFillPaint);
                    break;
                }
                this.mPath.reset();
                this.mPath.addRoundRect(this.mRect, this.mRadiiArray, Direction.CW);
                canvas.drawPath(this.mPath, this.mFillPaint);
                break;
        }
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.mMaskPaint);
        return result;
    }
}
