package com.iflytek.ui.p015a;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;

/* renamed from: com.iflytek.ui.a.c */
public class C1081c implements C0301i {
    private static final int[] f2029a;
    private static final float[] f2030b;
    private static Path f2031d;
    private int[] f2032c;
    private Paint f2033e;
    private int f2034f;
    private float f2035g;
    private C0298a[] f2036h;

    /* renamed from: com.iflytek.ui.a.c.a */
    private class C0298a {
        final /* synthetic */ C1081c f1125a;
        private int f1126b;
        private float f1127c;
        private int f1128d;
        private int f1129e;
        private int f1130f;

        public C0298a(C1081c c1081c, int i, float f, int i2) {
            this.f1125a = c1081c;
            this.f1126b = i;
            this.f1127c = f;
            this.f1128d = i2;
            this.f1129e = 0;
            this.f1130f = (int) ((36.0d * ((double) this.f1127c)) * 4.0d);
        }

        public void m1357a() {
            this.f1129e += this.f1126b;
            if (this.f1129e >= this.f1130f) {
                this.f1129e = 0;
            }
        }

        public void m1358a(Canvas canvas, Rect rect) {
            canvas.save();
            canvas.translate((float) (-this.f1129e), (float) (rect.height() / 2));
            canvas.scale(this.f1127c, this.f1125a.f2035g * this.f1127c, 0.0f, 0.0f);
            this.f1125a.f2033e.setColor(this.f1128d);
            canvas.drawPath(C1081c.f2031d, this.f1125a.f2033e);
            canvas.restore();
        }
    }

    static {
        f2029a = new int[]{6, 4, 9};
        f2030b = new float[]{0.33333334f, 0.6666667f, 0.75f};
    }

    public C1081c(int[] iArr) {
        int i = 0;
        this.f2032c = new int[]{-16776961, -16711936, -65536};
        f2031d = new Path();
        f2031d.moveTo(0.0f, 0.0f);
        for (int i2 = 0; i2 < 50; i2++) {
            f2031d.lineTo((float) ((int) (((double) i2) * 36.0d)), (float) ((int) (Math.sin(((double) i2) * 1.5707963267948966d) * 24.0d)));
        }
        this.f2034f = 1;
        this.f2035g = 1.0f;
        this.f2036h = new C0298a[3];
        if (iArr != null && iArr.length == 3) {
            this.f2032c = iArr;
        }
        while (i < 3) {
            this.f2036h[i] = new C0298a(this, f2029a[i], f2030b[i], this.f2032c[i]);
            i++;
        }
        this.f2033e = new Paint();
        this.f2033e.setAntiAlias(true);
        this.f2033e.setPathEffect(new CornerPathEffect(15.0f));
        this.f2033e.setStrokeCap(Cap.ROUND);
        this.f2033e.setStyle(Style.STROKE);
    }

    public int m2169a() {
        return 60;
    }

    public void m2170a(int i) {
        if (i < 1) {
            i = 1;
        }
        this.f2034f = i;
    }

    public void m2171a(Canvas canvas, Rect rect) {
        for (int i = 0; i < 3; i++) {
            this.f2036h[i].m1358a(canvas, rect);
        }
    }

    public void m2172b() {
        for (int i = 0; i < 3; i++) {
            this.f2036h[i].m1357a();
        }
        this.f2035g = ((3.0f * this.f2035g) + ((float) this.f2034f)) / 4.0f;
    }

    public void m2173c() {
    }

    public void m2174d() {
    }

    protected void finalize() throws Throwable {
        this.f2033e = null;
        this.f2036h = null;
        super.finalize();
    }
}
