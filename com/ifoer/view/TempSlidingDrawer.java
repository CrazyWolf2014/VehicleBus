package com.ifoer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.SlidingDrawer;

@SuppressLint({"WrongCall"})
public class TempSlidingDrawer extends SlidingDrawer {
    private static final boolean f1315D = true;
    public static int opendFlag;
    private View handle;
    private int height;
    private int heightMeasure;
    private int mTopOffset;
    private boolean mVertical;
    float oldX;
    float oldY;
    private int widthMeasure;

    static {
        opendFlag = 0;
    }

    public TempSlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
        boolean z = f1315D;
        super(context, attrs, defStyle);
        this.oldX = 0.0f;
        this.oldY = 0.0f;
        int orientation = attrs.getAttributeIntValue("android", "orientation", 1);
        this.mTopOffset = attrs.getAttributeIntValue("android", "topOffset", 0);
        if (orientation != 1) {
            z = false;
        }
        this.mVertical = z;
    }

    public TempSlidingDrawer(Context context, AttributeSet attrs) {
        boolean z = f1315D;
        super(context, attrs);
        this.oldX = 0.0f;
        this.oldY = 0.0f;
        int orientation = attrs.getAttributeIntValue("android", "orientation", 1);
        this.mTopOffset = attrs.getAttributeIntValue("android", "topOffset", 0);
        if (orientation != 1) {
            z = false;
        }
        this.mVertical = z;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasure = widthMeasureSpec;
        this.heightMeasure = heightMeasureSpec;
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        this.height = heightSpecSize;
        this.handle = getHandle();
        this.handle.setClickable(false);
        View content = getContent();
        if (this.mVertical) {
            content.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((heightSpecSize - this.handle.getMeasuredHeight()) - this.mTopOffset, heightSpecMode));
            heightSpecSize = (this.handle.getMeasuredHeight() + this.mTopOffset) + content.getMeasuredHeight();
            widthSpecSize = content.getMeasuredWidth();
            if (this.handle.getMeasuredWidth() > widthSpecSize) {
                widthSpecSize = this.handle.getMeasuredWidth();
            }
        } else {
            getContent().measure(MeasureSpec.makeMeasureSpec((widthSpecSize - this.handle.getMeasuredWidth()) - this.mTopOffset, widthSpecMode), heightMeasureSpec);
            widthSpecSize = (this.handle.getMeasuredWidth() + this.mTopOffset) + content.getMeasuredWidth();
            heightSpecSize = content.getMeasuredHeight();
            if (this.handle.getMeasuredHeight() > heightSpecSize) {
                heightSpecSize = this.handle.getMeasuredHeight();
            }
        }
        if (opendFlag == 0) {
            measureChild(this.handle, widthMeasureSpec, heightMeasureSpec / 3);
            setMeasuredDimension(widthSpecSize, heightSpecSize / 3);
        } else if (opendFlag == 1) {
            measureChild(this.handle, widthMeasureSpec, heightMeasureSpec / 2);
            setMeasuredDimension(widthSpecSize, heightSpecSize / 2);
        } else if (opendFlag == 2) {
            measureChild(this.handle, widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    protected void dispatchDraw(Canvas canvas) {
        long time = getDrawingTime();
        View handle = super.getHandle();
        View content = super.getContent();
        drawChild(canvas, handle, time);
        if (!isOpened() || isMoving()) {
            canvas.save();
            float f2 = (float) ((handle.getTop() - getTop()) / 2);
            canvas.clipRect(handle.getLeft(), handle.getBottom(), getRight(), getBottom());
            canvas.translate(0.0f, f2);
            drawChild(canvas, content, time);
            canvas.restore();
        } else {
            super.dispatchDraw(canvas);
        }
        super.dispatchDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float X = event.getX();
        float Y = event.getRawY();
        if (event.getAction() == 0) {
            this.oldY = Y;
            if (opendFlag == 0) {
                onMeasure(this.widthMeasure, this.heightMeasure / 3);
            } else if (opendFlag == 1) {
                onMeasure(this.widthMeasure, this.heightMeasure / 2);
            } else if (opendFlag == 2) {
                onMeasure(this.widthMeasure, this.heightMeasure);
            }
        } else if (event.getAction() == 1) {
            if (Y < ((float) (this.height / 2))) {
                opendFlag = 2;
                onMeasure(this.widthMeasure, this.heightMeasure);
            } else if (Y >= ((float) (this.height / 2)) && ((double) Y) < ((double) this.height) * 0.6666666666666666d) {
                opendFlag = 1;
                onMeasure(this.widthMeasure, this.heightMeasure);
            } else if (((double) Y) >= ((double) this.height) * 0.6666666666666666d && Y < ((float) (this.height - 100))) {
                opendFlag = 0;
                onMeasure(this.widthMeasure, this.heightMeasure);
            }
        } else if (event.getAction() == 2) {
            onMeasure(this.widthMeasure, this.heightMeasure);
        }
        this.oldX = X;
        this.oldY = Y;
        return super.onTouchEvent(event);
    }

    public void close() {
        super.close();
    }
}
