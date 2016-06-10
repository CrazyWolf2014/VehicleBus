package com.ifoer.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ProgressbarForText extends ProgressBar {
    Paint mPaint;
    String text;

    public ProgressbarForText(Context context) {
        super(context);
        initText();
    }

    public ProgressbarForText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initText();
    }

    public ProgressbarForText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initText();
    }

    public synchronized void setProgress(int progress) {
        setText(progress);
        super.setProgress(progress);
    }

    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        canvas.drawText(this.text, (float) ((getWidth() / 2) - rect.centerX()), (float) ((getHeight() / 2) - rect.centerY()), this.mPaint);
    }

    private void initText() {
        this.mPaint = new Paint();
        this.mPaint.setColor(-1);
        this.mPaint.setTextSize(25.0f);
    }

    private void setText() {
        setText(getProgress());
    }

    private void setText(int progress) {
        this.text = new StringBuilder(String.valueOf(String.valueOf((progress * 100) / getMax()))).append("%").toString();
    }
}
