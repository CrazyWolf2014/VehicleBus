package com.cnmobi.im.cropImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public abstract class ImageViewTouchBase extends ImageView {
    static final float SCALE_RATE = 1.25f;
    public static final int STATE_DOODLE = 1;
    public static final int STATE_HIGHLIGHT = 0;
    public static final int STATE_NONE = 2;
    private static final String TAG = "ImageViewTouchBase";
    protected Matrix mBaseMatrix;
    public final RotateBitmap mBitmapDisplayed;
    private final Matrix mDisplayMatrix;
    protected Handler mHandler;
    protected int mLastXTouchPos;
    protected int mLastYTouchPos;
    private final float[] mMatrixValues;
    float mMaxZoom;
    private Runnable mOnLayoutRunnable;
    private Recycler mRecycler;
    protected int mState;
    protected Matrix mSuppMatrix;
    int mThisHeight;
    int mThisWidth;

    /* renamed from: com.cnmobi.im.cropImage.ImageViewTouchBase.1 */
    class C02051 implements Runnable {
        private final /* synthetic */ RotateBitmap val$bitmap;
        private final /* synthetic */ boolean val$resetSupp;

        C02051(RotateBitmap rotateBitmap, boolean z) {
            this.val$bitmap = rotateBitmap;
            this.val$resetSupp = z;
        }

        public void run() {
            ImageViewTouchBase.this.setImageRotateBitmapResetBase(this.val$bitmap, this.val$resetSupp);
        }
    }

    /* renamed from: com.cnmobi.im.cropImage.ImageViewTouchBase.2 */
    class C02062 implements Runnable {
        private final /* synthetic */ float val$centerX;
        private final /* synthetic */ float val$centerY;
        private final /* synthetic */ float val$durationMs;
        private final /* synthetic */ float val$incrementPerMs;
        private final /* synthetic */ float val$oldScale;
        private final /* synthetic */ long val$startTime;

        C02062(float f, long j, float f2, float f3, float f4, float f5) {
            this.val$durationMs = f;
            this.val$startTime = j;
            this.val$oldScale = f2;
            this.val$incrementPerMs = f3;
            this.val$centerX = f4;
            this.val$centerY = f5;
        }

        public void run() {
            float currentMs = Math.min(this.val$durationMs, (float) (System.currentTimeMillis() - this.val$startTime));
            ImageViewTouchBase.this.zoomTo(this.val$oldScale + (this.val$incrementPerMs * currentMs), this.val$centerX, this.val$centerY);
            if (currentMs < this.val$durationMs) {
                ImageViewTouchBase.this.mHandler.post(this);
            }
        }
    }

    public interface Recycler {
        void recycle(Bitmap bitmap);
    }

    public void setRecycler(Recycler r) {
        this.mRecycler = r;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mThisWidth = right - left;
        this.mThisHeight = bottom - top;
        Runnable r = this.mOnLayoutRunnable;
        if (r != null) {
            this.mOnLayoutRunnable = null;
            r.run();
        }
        if (this.mBitmapDisplayed.getBitmap() != null) {
            getProperBaseMatrix(this.mBitmapDisplayed, this.mBaseMatrix);
            setImageMatrix(getImageViewMatrix());
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || getScale() <= 1.0f) {
            return super.onKeyDown(keyCode, event);
        }
        zoomTo(1.0f);
        return true;
    }

    public void setImageBitmap(Bitmap bitmap) {
        setImageBitmap(bitmap, STATE_HIGHLIGHT);
    }

    private void setImageBitmap(Bitmap bitmap, int rotation) {
        super.setImageBitmap(bitmap);
        Drawable d = getDrawable();
        if (d != null) {
            d.setDither(true);
        }
        Bitmap old = this.mBitmapDisplayed.getBitmap();
        this.mBitmapDisplayed.setBitmap(bitmap);
        this.mBitmapDisplayed.setRotation(rotation);
        if (old != null && old != bitmap && this.mRecycler != null) {
            this.mRecycler.recycle(old);
        }
    }

    public void clear() {
        setImageBitmapResetBase(null, true);
    }

    public void setImageBitmapResetBase(Bitmap bitmap, boolean resetSupp) {
        setImageRotateBitmapResetBase(new RotateBitmap(bitmap), resetSupp);
    }

    public void setImageRotateBitmapResetBase(RotateBitmap bitmap, boolean resetSupp) {
        if (getWidth() <= 0) {
            this.mOnLayoutRunnable = new C02051(bitmap, resetSupp);
            return;
        }
        if (bitmap.getBitmap() != null) {
            getProperBaseMatrix(bitmap, this.mBaseMatrix);
            setImageBitmap(bitmap.getBitmap(), bitmap.getRotation());
        } else {
            this.mBaseMatrix.reset();
            setImageBitmap(null);
        }
        if (resetSupp) {
            this.mSuppMatrix.reset();
        }
        setImageMatrix(getImageViewMatrix());
        this.mMaxZoom = maxZoom();
    }

    public void center(boolean horizontal, boolean vertical) {
        if (this.mBitmapDisplayed.getBitmap() != null) {
            Matrix m = getImageViewMatrix();
            RectF rect = new RectF(0.0f, 0.0f, (float) this.mBitmapDisplayed.getBitmap().getWidth(), (float) this.mBitmapDisplayed.getBitmap().getHeight());
            m.mapRect(rect);
            float height = rect.height();
            float width = rect.width();
            float deltaX = 0.0f;
            float deltaY = 0.0f;
            if (vertical) {
                int viewHeight = getHeight();
                if (height < ((float) viewHeight)) {
                    deltaY = ((((float) viewHeight) - height) / 2.0f) - rect.top;
                } else if (rect.top > 0.0f) {
                    deltaY = -rect.top;
                } else if (rect.bottom < ((float) viewHeight)) {
                    deltaY = ((float) getHeight()) - rect.bottom;
                }
            }
            if (horizontal) {
                int viewWidth = getWidth();
                if (width < ((float) viewWidth)) {
                    deltaX = ((((float) viewWidth) - width) / 2.0f) - rect.left;
                } else if (rect.left > 0.0f) {
                    deltaX = -rect.left;
                } else if (rect.right < ((float) viewWidth)) {
                    deltaX = ((float) viewWidth) - rect.right;
                }
            }
            postTranslate(deltaX, deltaY);
            setImageMatrix(getImageViewMatrix());
        }
    }

    public ImageViewTouchBase(Context context) {
        super(context);
        this.mBaseMatrix = new Matrix();
        this.mSuppMatrix = new Matrix();
        this.mDisplayMatrix = new Matrix();
        this.mMatrixValues = new float[9];
        this.mBitmapDisplayed = new RotateBitmap(null);
        this.mThisWidth = -1;
        this.mThisHeight = -1;
        this.mState = STATE_HIGHLIGHT;
        this.mHandler = new Handler();
        this.mOnLayoutRunnable = null;
        init();
    }

    public ImageViewTouchBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mBaseMatrix = new Matrix();
        this.mSuppMatrix = new Matrix();
        this.mDisplayMatrix = new Matrix();
        this.mMatrixValues = new float[9];
        this.mBitmapDisplayed = new RotateBitmap(null);
        this.mThisWidth = -1;
        this.mThisHeight = -1;
        this.mState = STATE_HIGHLIGHT;
        this.mHandler = new Handler();
        this.mOnLayoutRunnable = null;
        init();
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
    }

    protected float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[whichValue];
    }

    protected float getScale(Matrix matrix) {
        return getValue(matrix, STATE_HIGHLIGHT);
    }

    public float getScale() {
        return getScale(this.mSuppMatrix);
    }

    private void getProperBaseMatrix(RotateBitmap bitmap, Matrix matrix) {
        float viewWidth = (float) getWidth();
        float viewHeight = (float) getHeight();
        float w = (float) bitmap.getWidth();
        float h = (float) bitmap.getHeight();
        matrix.reset();
        float scale = Math.min(Math.min(viewWidth / w, 2.0f), Math.min(viewHeight / h, 2.0f));
        matrix.postConcat(bitmap.getRotateMatrix());
        matrix.postScale(scale, scale);
        matrix.postTranslate((viewWidth - (w * scale)) / 2.0f, (viewHeight - (h * scale)) / 2.0f);
    }

    protected Matrix getImageViewMatrix() {
        this.mDisplayMatrix.set(this.mBaseMatrix);
        this.mDisplayMatrix.postConcat(this.mSuppMatrix);
        return this.mDisplayMatrix;
    }

    protected float maxZoom() {
        if (this.mBitmapDisplayed.getBitmap() == null) {
            return 1.0f;
        }
        float max = Math.max(((float) this.mBitmapDisplayed.getWidth()) / ((float) this.mThisWidth), ((float) this.mBitmapDisplayed.getHeight()) / ((float) this.mThisHeight)) * 4.0f;
        if (max < 1.0f) {
            max = 1.0f;
        }
        return max;
    }

    protected void zoomTo(float scale, float centerX, float centerY) {
        if (scale > this.mMaxZoom) {
            scale = this.mMaxZoom;
        }
        float deltaScale = scale / getScale();
        this.mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
        setImageMatrix(getImageViewMatrix());
        center(true, true);
    }

    protected void zoomTo(float scale, float centerX, float centerY, float durationMs) {
        float incrementPerMs = (scale - getScale()) / durationMs;
        float oldScale = getScale();
        this.mHandler.post(new C02062(durationMs, System.currentTimeMillis(), oldScale, incrementPerMs, centerX, centerY));
    }

    protected void zoomTo(float scale) {
        zoomTo(scale, ((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f);
    }

    protected void zoomIn() {
        zoomIn(SCALE_RATE);
    }

    protected void zoomOut() {
        zoomOut(SCALE_RATE);
    }

    protected void zoomIn(float rate) {
        if (getScale() < this.mMaxZoom && this.mBitmapDisplayed.getBitmap() != null) {
            this.mSuppMatrix.postScale(rate, rate, ((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f);
            setImageMatrix(getImageViewMatrix());
        }
    }

    protected void zoomOut(float rate) {
        if (this.mBitmapDisplayed.getBitmap() != null) {
            float cx = ((float) getWidth()) / 2.0f;
            float cy = ((float) getHeight()) / 2.0f;
            Matrix tmp = new Matrix(this.mSuppMatrix);
            tmp.postScale(1.0f / rate, 1.0f / rate, cx, cy);
            if (getScale(tmp) < 1.0f) {
                this.mSuppMatrix.setScale(1.0f, 1.0f, cx, cy);
            } else {
                this.mSuppMatrix.postScale(1.0f / rate, 1.0f / rate, cx, cy);
            }
            setImageMatrix(getImageViewMatrix());
            center(true, true);
        }
    }

    protected void postTranslate(float dx, float dy) {
        this.mSuppMatrix.postTranslate(dx, dy);
    }

    protected void panBy(float dx, float dy) {
        postTranslate(dx, dy);
        setImageMatrix(getImageViewMatrix());
    }
}
