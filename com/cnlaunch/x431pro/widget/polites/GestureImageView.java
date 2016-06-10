package com.cnlaunch.x431pro.widget.polites;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.googlecode.leptonica.android.Skew;
import java.io.InputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.xbill.DNS.KEYRecord;

public class GestureImageView extends ImageView {
    private static /* synthetic */ int[] $SWITCH_TABLE$android$widget$ImageView$ScaleType = null;
    public static final String GLOBAL_NS = "http://schemas.android.com/apk/res/android";
    public static final String LOCAL_NS = "http://schemas.polites.com/android";
    private int alpha;
    private Animator animator;
    private float centerX;
    private float centerY;
    private ColorFilter colorFilter;
    private OnTouchListener customOnTouchListener;
    private int deviceOrientation;
    private int displayHeight;
    private int displayWidth;
    private final Semaphore drawLock;
    private Drawable drawable;
    private float fitScaleHorizontal;
    private float fitScaleVertical;
    private GestureImageViewListener gestureImageViewListener;
    private GestureImageViewTouchListener gestureImageViewTouchListener;
    private int hHeight;
    private int hWidth;
    private int imageOrientation;
    private boolean layout;
    private float maxScale;
    private float minScale;
    private OnClickListener onClickListener;
    private boolean recycle;
    private int resId;
    private float rotation;
    private float scale;
    private float scaleAdjust;
    private Float startX;
    private Float startY;
    private float startingScale;
    private boolean strict;
    private float f831x;
    private float f832y;

    /* renamed from: com.cnlaunch.x431pro.widget.polites.GestureImageView.1 */
    class C01381 implements OnTouchListener {
        C01381() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (GestureImageView.this.customOnTouchListener != null) {
                GestureImageView.this.customOnTouchListener.onTouch(v, event);
            }
            return GestureImageView.this.gestureImageViewTouchListener.onTouch(v, event);
        }
    }

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

    public GestureImageView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public GestureImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.drawLock = new Semaphore(0);
        this.f831x = 0.0f;
        this.f832y = 0.0f;
        this.layout = false;
        this.scaleAdjust = 1.0f;
        this.startingScale = -1.0f;
        this.scale = 1.0f;
        this.maxScale = Skew.SWEEP_DELTA;
        this.minScale = 0.75f;
        this.fitScaleHorizontal = 1.0f;
        this.fitScaleVertical = 1.0f;
        this.rotation = 0.0f;
        this.resId = -1;
        this.recycle = false;
        this.strict = false;
        this.alpha = KEYRecord.PROTOCOL_ANY;
        this.deviceOrientation = -1;
        String scaleType = attrs.getAttributeValue(GLOBAL_NS, "scaleType");
        if (scaleType == null || scaleType.trim().length() == 0) {
            setScaleType(ScaleType.CENTER_INSIDE);
        }
        String strStartX = attrs.getAttributeValue(LOCAL_NS, "start-x");
        String strStartY = attrs.getAttributeValue(LOCAL_NS, "start-y");
        if (strStartX != null && strStartX.trim().length() > 0) {
            this.startX = Float.valueOf(Float.parseFloat(strStartX));
        }
        if (strStartY != null && strStartY.trim().length() > 0) {
            this.startY = Float.valueOf(Float.parseFloat(strStartY));
        }
        setStartingScale(attrs.getAttributeFloatValue(LOCAL_NS, "start-scale", this.startingScale));
        setMinScale(attrs.getAttributeFloatValue(LOCAL_NS, "min-scale", this.minScale));
        setMaxScale(attrs.getAttributeFloatValue(LOCAL_NS, "max-scale", this.maxScale));
        setStrict(attrs.getAttributeBooleanValue(LOCAL_NS, "strict", this.strict));
        setRecycle(attrs.getAttributeBooleanValue(LOCAL_NS, "recycle", this.recycle));
        initImage();
    }

    public GestureImageView(Context context) {
        super(context);
        this.drawLock = new Semaphore(0);
        this.f831x = 0.0f;
        this.f832y = 0.0f;
        this.layout = false;
        this.scaleAdjust = 1.0f;
        this.startingScale = -1.0f;
        this.scale = 1.0f;
        this.maxScale = Skew.SWEEP_DELTA;
        this.minScale = 0.75f;
        this.fitScaleHorizontal = 1.0f;
        this.fitScaleVertical = 1.0f;
        this.rotation = 0.0f;
        this.resId = -1;
        this.recycle = false;
        this.strict = false;
        this.alpha = KEYRecord.PROTOCOL_ANY;
        this.deviceOrientation = -1;
        setScaleType(ScaleType.CENTER_INSIDE);
        initImage();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.drawable == null) {
            this.displayHeight = MeasureSpec.getSize(heightMeasureSpec);
            this.displayWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else if (getResources().getConfiguration().orientation == 2) {
            this.displayHeight = MeasureSpec.getSize(heightMeasureSpec);
            if (getLayoutParams().width == -2) {
                this.displayWidth = Math.round(((float) this.displayHeight) * (((float) getImageWidth()) / ((float) getImageHeight())));
            } else {
                this.displayWidth = MeasureSpec.getSize(widthMeasureSpec);
            }
        } else {
            this.displayWidth = MeasureSpec.getSize(widthMeasureSpec);
            if (getLayoutParams().height == -2) {
                this.displayHeight = Math.round(((float) this.displayWidth) * (((float) getImageHeight()) / ((float) getImageWidth())));
            } else {
                this.displayHeight = MeasureSpec.getSize(heightMeasureSpec);
            }
        }
        setMeasuredDimension(this.displayWidth, this.displayHeight);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed || !this.layout) {
            setupCanvas(this.displayWidth, this.displayHeight, getResources().getConfiguration().orientation);
        }
    }

    protected void setupCanvas(int measuredWidth, int measuredHeight, int orientation) {
        if (this.deviceOrientation != orientation) {
            this.layout = false;
            this.deviceOrientation = orientation;
        }
        if (this.drawable != null && !this.layout) {
            int imageWidth = getImageWidth();
            int imageHeight = getImageHeight();
            this.hWidth = Math.round(((float) imageWidth) / 2.0f);
            this.hHeight = Math.round(((float) imageHeight) / 2.0f);
            measuredWidth -= getPaddingLeft() + getPaddingRight();
            measuredHeight -= getPaddingTop() + getPaddingBottom();
            computeCropScale(imageWidth, imageHeight, measuredWidth, measuredHeight);
            if (this.startingScale <= 0.0f) {
                computeStartingScale(imageWidth, imageHeight, measuredWidth, measuredHeight);
            }
            this.scaleAdjust = this.startingScale;
            this.centerX = ((float) measuredWidth) / 2.0f;
            this.centerY = ((float) measuredHeight) / 2.0f;
            if (this.startX == null) {
                this.f831x = this.centerX;
            } else {
                this.f831x = this.startX.floatValue();
            }
            if (this.startY == null) {
                this.f832y = this.centerY;
            } else {
                this.f832y = this.startY.floatValue();
            }
            this.gestureImageViewTouchListener = new GestureImageViewTouchListener(this, measuredWidth, measuredHeight);
            if (isLandscape()) {
                this.gestureImageViewTouchListener.setMinScale(this.minScale * this.fitScaleHorizontal);
            } else {
                this.gestureImageViewTouchListener.setMinScale(this.minScale * this.fitScaleVertical);
            }
            this.gestureImageViewTouchListener.setMaxScale(this.maxScale * this.startingScale);
            this.gestureImageViewTouchListener.setFitScaleHorizontal(this.fitScaleHorizontal);
            this.gestureImageViewTouchListener.setFitScaleVertical(this.fitScaleVertical);
            this.gestureImageViewTouchListener.setCanvasWidth(measuredWidth);
            this.gestureImageViewTouchListener.setCanvasHeight(measuredHeight);
            this.gestureImageViewTouchListener.setOnClickListener(this.onClickListener);
            this.drawable.setBounds(-this.hWidth, -this.hHeight, this.hWidth, this.hHeight);
            super.setOnTouchListener(new C01381());
            this.layout = true;
        }
    }

    protected void computeCropScale(int imageWidth, int imageHeight, int measuredWidth, int measuredHeight) {
        this.fitScaleHorizontal = ((float) measuredWidth) / ((float) imageWidth);
        this.fitScaleVertical = ((float) measuredHeight) / ((float) imageHeight);
    }

    protected void computeStartingScale(int imageWidth, int imageHeight, int measuredWidth, int measuredHeight) {
        switch ($SWITCH_TABLE$android$widget$ImageView$ScaleType()[getScaleType().ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.startingScale = 1.0f;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                this.startingScale = Math.max(((float) measuredHeight) / ((float) imageHeight), ((float) measuredWidth) / ((float) imageWidth));
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                if (((float) imageWidth) / ((float) measuredWidth) > ((float) imageHeight) / ((float) measuredHeight)) {
                    this.startingScale = this.fitScaleHorizontal;
                } else {
                    this.startingScale = this.fitScaleVertical;
                }
            default:
        }
    }

    protected boolean isRecycled() {
        if (this.drawable != null && (this.drawable instanceof BitmapDrawable)) {
            Bitmap bitmap = ((BitmapDrawable) this.drawable).getBitmap();
            if (bitmap != null) {
                return bitmap.isRecycled();
            }
        }
        return false;
    }

    protected void recycle() {
        if (this.recycle && this.drawable != null && (this.drawable instanceof BitmapDrawable)) {
            Bitmap bitmap = ((BitmapDrawable) this.drawable).getBitmap();
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        if (this.layout) {
            if (!(this.drawable == null || isRecycled())) {
                canvas.save();
                float adjustedScale = this.scale * this.scaleAdjust;
                canvas.translate(this.f831x, this.f832y);
                if (this.rotation != 0.0f) {
                    canvas.rotate(this.rotation);
                }
                if (adjustedScale != 1.0f) {
                    canvas.scale(adjustedScale, adjustedScale);
                }
                this.drawable.draw(canvas);
                canvas.restore();
            }
            if (this.drawLock.availablePermits() <= 0) {
                this.drawLock.release();
            }
        }
    }

    public boolean waitForDraw(long timeout) throws InterruptedException {
        return this.drawLock.tryAcquire(timeout, TimeUnit.MILLISECONDS);
    }

    protected void onAttachedToWindow() {
        this.animator = new Animator(this, "GestureImageViewAnimator");
        this.animator.start();
        if (this.resId >= 0 && this.drawable == null) {
            setImageResource(this.resId);
        }
        super.onAttachedToWindow();
    }

    public void animationStart(Animation animation) {
        if (this.animator != null) {
            this.animator.play(animation);
        }
    }

    public void animationStop() {
        if (this.animator != null) {
            this.animator.cancel();
        }
    }

    protected void onDetachedFromWindow() {
        if (this.animator != null) {
            this.animator.finish();
        }
        if (!(!this.recycle || this.drawable == null || isRecycled())) {
            recycle();
            this.drawable = null;
        }
        super.onDetachedFromWindow();
    }

    protected void initImage() {
        if (this.drawable != null) {
            this.drawable.setAlpha(this.alpha);
            this.drawable.setFilterBitmap(true);
            if (this.colorFilter != null) {
                this.drawable.setColorFilter(this.colorFilter);
            }
        }
        if (!this.layout) {
            requestLayout();
            redraw();
        }
    }

    public void setImageBitmap(Bitmap image) {
        this.drawable = new BitmapDrawable(getResources(), image);
        initImage();
    }

    public void setImageDrawable(Drawable drawable) {
        this.drawable = drawable;
        initImage();
    }

    public void setImageResource(int id) {
        if (this.drawable != null) {
            recycle();
        }
        if (id >= 0) {
            this.resId = id;
            setImageDrawable(getContext().getResources().getDrawable(id));
        }
    }

    public int getScaledWidth() {
        return Math.round(((float) getImageWidth()) * getScale());
    }

    public int getScaledHeight() {
        return Math.round(((float) getImageHeight()) * getScale());
    }

    public int getImageWidth() {
        if (this.drawable != null) {
            return this.drawable.getIntrinsicWidth();
        }
        return 0;
    }

    public int getImageHeight() {
        if (this.drawable != null) {
            return this.drawable.getIntrinsicHeight();
        }
        return 0;
    }

    public void moveBy(float x, float y) {
        this.f831x += x;
        this.f832y += y;
    }

    public void setPosition(float x, float y) {
        this.f831x = x;
        this.f832y = y;
    }

    public void redraw() {
        postInvalidate();
    }

    public void setMinScale(float min) {
        this.minScale = min;
        if (this.gestureImageViewTouchListener != null) {
            this.gestureImageViewTouchListener.setMinScale(this.fitScaleHorizontal * min);
        }
    }

    public void setMaxScale(float max) {
        this.maxScale = max;
        if (this.gestureImageViewTouchListener != null) {
            this.gestureImageViewTouchListener.setMaxScale(this.startingScale * max);
        }
    }

    public void setScale(float scale) {
        this.scaleAdjust = scale;
    }

    public float getScale() {
        return this.scaleAdjust;
    }

    public float getImageX() {
        return this.f831x;
    }

    public float getImageY() {
        return this.f832y;
    }

    public boolean isStrict() {
        return this.strict;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public boolean isRecycle() {
        return this.recycle;
    }

    public void setRecycle(boolean recycle) {
        this.recycle = recycle;
    }

    public void reset() {
        this.f831x = this.centerX;
        this.f832y = this.centerY;
        this.scaleAdjust = this.startingScale;
        if (this.gestureImageViewTouchListener != null) {
            this.gestureImageViewTouchListener.reset();
        }
        redraw();
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setGestureImageViewListener(GestureImageViewListener pinchImageViewListener) {
        this.gestureImageViewListener = pinchImageViewListener;
    }

    public GestureImageViewListener getGestureImageViewListener() {
        return this.gestureImageViewListener;
    }

    public Drawable getDrawable() {
        return this.drawable;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
        if (this.drawable != null) {
            this.drawable.setAlpha(alpha);
        }
    }

    public void setColorFilter(ColorFilter cf) {
        this.colorFilter = cf;
        if (this.drawable != null) {
            this.drawable.setColorFilter(cf);
        }
    }

    public void setImageURI(Uri mUri) {
        if ("content".equals(mUri.getScheme())) {
            Cursor cur;
            InputStream in;
            try {
                String[] orientationColumn = new String[]{"orientation"};
                cur = getContext().getContentResolver().query(mUri, orientationColumn, null, null, null);
                if (cur != null && cur.moveToFirst()) {
                    this.imageOrientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
                }
                in = null;
                in = getContext().getContentResolver().openInputStream(mUri);
                Bitmap bmp = BitmapFactory.decodeStream(in);
                if (this.imageOrientation != 0) {
                    Matrix m = new Matrix();
                    m.postRotate((float) this.imageOrientation);
                    Bitmap rotated = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, true);
                    bmp.recycle();
                    setImageDrawable(new BitmapDrawable(getResources(), rotated));
                } else {
                    setImageDrawable(new BitmapDrawable(getResources(), bmp));
                }
                if (in != null) {
                    in.close();
                }
                if (cur != null) {
                    cur.close();
                }
            } catch (Exception e) {
                Log.w("GestureImageView", "Unable to open content: " + mUri, e);
            } catch (Throwable th) {
                if (in != null) {
                    in.close();
                }
                if (cur != null) {
                    cur.close();
                }
            }
        } else {
            setImageDrawable(Drawable.createFromPath(mUri.toString()));
        }
        if (this.drawable == null) {
            Log.e("GestureImageView", "resolveUri failed on bad bitmap uri: " + mUri);
        }
    }

    public Matrix getImageMatrix() {
        if (!this.strict) {
            return super.getImageMatrix();
        }
        throw new UnsupportedOperationException("Not supported");
    }

    public void setScaleType(ScaleType scaleType) {
        if (scaleType == ScaleType.CENTER || scaleType == ScaleType.CENTER_CROP || scaleType == ScaleType.CENTER_INSIDE) {
            super.setScaleType(scaleType);
        } else if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
    }

    public void invalidateDrawable(Drawable dr) {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
        super.invalidateDrawable(dr);
    }

    public int[] onCreateDrawableState(int extraSpace) {
        if (!this.strict) {
            return super.onCreateDrawableState(extraSpace);
        }
        throw new UnsupportedOperationException("Not supported");
    }

    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
        super.setAdjustViewBounds(adjustViewBounds);
    }

    public void setImageLevel(int level) {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
        super.setImageLevel(level);
    }

    public void setImageMatrix(Matrix matrix) {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
    }

    public void setImageState(int[] state, boolean merge) {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
    }

    public void setSelected(boolean selected) {
        if (this.strict) {
            throw new UnsupportedOperationException("Not supported");
        }
        super.setSelected(selected);
    }

    public void setOnTouchListener(OnTouchListener l) {
        this.customOnTouchListener = l;
    }

    public float getCenterX() {
        return this.centerX;
    }

    public float getCenterY() {
        return this.centerY;
    }

    public boolean isLandscape() {
        return getImageWidth() >= getImageHeight();
    }

    public boolean isPortrait() {
        return getImageWidth() <= getImageHeight();
    }

    public void setStartingScale(float startingScale) {
        this.startingScale = startingScale;
    }

    public void setStartingPosition(float x, float y) {
        this.startX = Float.valueOf(x);
        this.startY = Float.valueOf(y);
    }

    public void setOnClickListener(OnClickListener l) {
        this.onClickListener = l;
        if (this.gestureImageViewTouchListener != null) {
            this.gestureImageViewTouchListener.setOnClickListener(l);
        }
    }

    public boolean isOrientationAligned() {
        if (this.deviceOrientation == 2) {
            return isLandscape();
        }
        if (this.deviceOrientation == 1) {
            return isPortrait();
        }
        return true;
    }

    public int getDeviceOrientation() {
        return this.deviceOrientation;
    }
}
