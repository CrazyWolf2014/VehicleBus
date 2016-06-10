package com.iflytek.ui.p015a;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.cnlaunch.framework.network.async.AsyncTaskManager;

/* renamed from: com.iflytek.ui.a.d */
public class C1082d implements C0301i {
    private Drawable f2037a;
    private Drawable f2038b;
    private Rect f2039c;
    private int f2040d;
    private int f2041e;

    public C1082d(Drawable[] drawableArr) {
        this.f2041e = 0;
        if (drawableArr == null || drawableArr.length != 2) {
            throw new IllegalArgumentException("Arguments must be not null");
        }
        this.f2037a = drawableArr[0];
        this.f2038b = drawableArr[1];
        this.f2039c = new Rect(((-this.f2038b.getIntrinsicWidth()) * 9) / 2, (-this.f2038b.getIntrinsicHeight()) / 2, ((-this.f2038b.getIntrinsicWidth()) * 7) / 2, this.f2038b.getIntrinsicHeight() / 2);
        this.f2040d = this.f2038b.getIntrinsicWidth() * 2;
        Log.d("StateConnectingDrawer", "Intrinsic\t:" + this.f2039c.toString());
    }

    public int m2175a() {
        return AsyncTaskManager.REQUEST_SUCCESS_CODE;
    }

    public void m2176a(Canvas canvas, Rect rect) {
        canvas.save();
        canvas.translate((float) (rect.width() / 2), (float) (rect.height() / 2));
        for (int i = 0; i < 5; i++) {
            if (i == this.f2041e) {
                this.f2038b.setBounds(this.f2039c);
                this.f2038b.draw(canvas);
            } else {
                this.f2037a.setBounds(this.f2039c);
                this.f2037a.draw(canvas);
            }
            this.f2039c.offset(this.f2040d, 0);
        }
        canvas.restore();
        this.f2039c.offset(this.f2040d * -5, 0);
    }

    public void m2177b() {
        this.f2041e = (this.f2041e + 1) % 5;
    }

    public void m2178c() {
    }

    public void m2179d() {
        this.f2041e = 0;
    }

    protected void finalize() throws Throwable {
        this.f2037a = null;
        this.f2038b = null;
        this.f2039c = null;
        super.finalize();
    }
}
