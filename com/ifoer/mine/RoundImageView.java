package com.ifoer.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageView;
import org.xbill.DNS.KEYRecord;

public class RoundImageView extends ImageView {
    private float f1296f;
    private float f1297g;
    private float f1298h;
    DisplayMetrics metrics;
    Paint paint;
    Path path;
    Paint patint1;
    RectF rect;

    public RoundImageView(Context paramContext) {
        super(paramContext);
        this.f1296f = 4.0f;
        this.f1297g = 3.33f;
        this.f1298h = 4.0f;
        m1468b();
        m1466a();
    }

    public RoundImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.f1296f = 4.0f;
        this.f1297g = 3.33f;
        this.f1298h = 4.0f;
        m1468b();
        m1466a();
    }

    private void m1466a() {
        Drawable localDrawable = getBackground();
        if (localDrawable != null) {
            setBackgroundDrawable(null);
            if (getDrawable() == null) {
                setImageDrawable(localDrawable);
            }
        }
    }

    private void m1467a(int paramInt1, int paramInt2) {
        this.path = new Path();
        this.path.addCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) (getHeight() / 2), Direction.CCW);
        this.path.setFillType(FillType.INVERSE_WINDING);
        float f1 = (this.f1298h / 2.0f) - 0.5f;
        this.rect = new RectF(f1, f1, ((float) paramInt1) - f1, ((float) paramInt2) - f1);
    }

    @SuppressLint({"NewApi"})
    private void m1468b() {
        this.metrics = getContext().getResources().getDisplayMetrics();
        this.f1296f = TypedValue.applyDimension(1, this.f1296f, this.metrics);
        this.f1297g = TypedValue.applyDimension(1, this.f1297g, this.metrics);
        this.paint = new Paint(1);
        this.paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        this.patint1 = new Paint(1);
        this.patint1.setStyle(Style.STROKE);
        this.patint1.setColor(Color.parseColor("#ffffff"));
    }

    protected void onDraw(Canvas paramCanvas) {
        int i = paramCanvas.saveLayerAlpha(0.0f, 0.0f, (float) paramCanvas.getWidth(), (float) paramCanvas.getHeight(), KEYRecord.PROTOCOL_ANY, 4);
        super.onDraw(paramCanvas);
        if (this.path != null) {
            paramCanvas.drawPath(this.path, this.paint);
            paramCanvas.drawArc(this.rect, 0.0f, 360.0f, false, this.patint1);
        }
        paramCanvas.restoreToCount(i);
    }

    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        if (!(width == oldWidth && height == oldHeight)) {
            this.patint1.setStrokeWidth(this.f1298h);
        }
        m1467a(width, height);
    }
}
