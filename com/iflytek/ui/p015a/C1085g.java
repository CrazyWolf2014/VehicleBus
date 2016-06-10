package com.iflytek.ui.p015a;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import java.util.Random;

/* renamed from: com.iflytek.ui.a.g */
public class C1085g implements C0301i {
    private Paint f2050a;
    private Path f2051b;
    private Point f2052c;
    private Drawable f2053d;
    private Rect f2054e;
    private Random f2055f;
    private int f2056g;

    public C1085g(Drawable drawable, int i) {
        this.f2056g = -8274176;
        if (drawable == null) {
            throw new IllegalArgumentException("Arguments must be not null");
        }
        this.f2056g = i;
        this.f2053d = drawable;
        this.f2054e = new Rect(0, 0, this.f2053d.getIntrinsicWidth(), this.f2053d.getIntrinsicHeight());
        this.f2050a = new Paint();
        this.f2050a.setAntiAlias(true);
        this.f2050a.setColor(this.f2056g);
        this.f2050a.setStrokeWidth(4.0f);
        this.f2050a.setStyle(Style.STROKE);
        this.f2050a.setStrokeCap(Cap.ROUND);
        this.f2050a.setPathEffect(new CornerPathEffect(10.0f));
        this.f2051b = new Path();
        this.f2051b.moveTo(0.0f, 0.0f);
        this.f2052c = new Point(0, 0);
        this.f2055f = new Random();
    }

    public int m2190a() {
        return 100;
    }

    public void m2191a(Canvas canvas, Rect rect) {
        int width = (((int) (((float) rect.width()) * 0.6f)) - this.f2054e.width()) / 2;
        int height = ((rect.height() - 20) + this.f2054e.height()) / 2;
        this.f2054e.offset(this.f2052c.x - this.f2054e.left, this.f2052c.y - this.f2054e.bottom);
        this.f2053d.setBounds(this.f2054e);
        canvas.save();
        canvas.translate((float) width, (float) height);
        canvas.drawPath(this.f2051b, this.f2050a);
        this.f2053d.draw(canvas);
        canvas.restore();
        if (((float) this.f2052c.x) >= ((float) rect.width()) * 0.4f) {
            this.f2052c.x = 0;
            this.f2051b.reset();
            this.f2051b.moveTo(0.0f, 0.0f);
        }
    }

    public void m2192b() {
        this.f2052c.x = (this.f2052c.x + this.f2055f.nextInt(3)) + 5;
        this.f2052c.y = this.f2055f.nextInt(20);
        this.f2051b.lineTo((float) this.f2052c.x, (float) this.f2052c.y);
    }

    public void m2193c() {
    }

    public void m2194d() {
        this.f2051b.reset();
        this.f2051b.moveTo(0.0f, 0.0f);
        this.f2052c = new Point(0, 0);
    }

    protected void finalize() throws Throwable {
        this.f2050a = null;
        this.f2051b = null;
        this.f2052c = null;
        this.f2053d = null;
        this.f2054e = null;
        this.f2055f = null;
        super.finalize();
    }
}
