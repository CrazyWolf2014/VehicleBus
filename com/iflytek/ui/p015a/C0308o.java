package com.iflytek.ui.p015a;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import com.iflytek.ui.p016b.C0310a;
import com.iflytek.ui.p016b.C0313b.C0311a;

/* renamed from: com.iflytek.ui.a.o */
public class C0308o extends View {
    private C0301i f1157a;
    private Rect f1158b;
    private Handler f1159c;
    private C1082d f1160d;
    private C1080b f1161e;
    private C1081c f1162f;
    private C0301i f1163g;

    public C0308o(Context context) {
        super(context);
        this.f1159c = new C0309p(this);
        this.f1158b = null;
    }

    public void m1399a() {
        try {
            if (this.f1160d == null) {
                this.f1160d = (C1082d) C0310a.m1403a().m1406a("loading").m1413b(getContext());
            }
            this.f1160d.m2179d();
            this.f1157a = this.f1160d;
        } catch (Exception e) {
            e.printStackTrace();
            this.f1157a = null;
        }
        setVisibility(0);
        this.f1159c.removeMessages(1);
        this.f1159c.sendEmptyMessage(1);
    }

    public void m1400a(int i) {
        if (this.f1162f != null && this.f1157a == this.f1162f) {
            this.f1162f.m2170a(i);
        }
        if (this.f1161e != null && this.f1157a == this.f1161e) {
            this.f1161e.m2161a(i);
        }
    }

    public void m1401b() {
        try {
            if (C0310a.m1403a().m1406a("recording").m1414b().equals(C0311a.curve.name())) {
                if (this.f1162f == null) {
                    this.f1162f = (C1081c) C0310a.m1403a().m1406a("recording").m1407a(getContext());
                }
                this.f1162f.m2170a(0);
                this.f1157a = this.f1162f;
            } else {
                if (this.f1161e == null) {
                    this.f1161e = (C1080b) C0310a.m1403a().m1406a("recording").m1407a(getContext());
                }
                this.f1161e.m2161a(0);
                this.f1157a = this.f1161e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.f1157a = null;
        }
        setVisibility(0);
        this.f1159c.removeMessages(1);
        this.f1159c.sendEmptyMessage(1);
    }

    public void m1402c() {
        try {
            if (this.f1163g == null) {
                this.f1163g = C0310a.m1403a().m1406a("connecting").m1417c(getContext());
            }
            this.f1163g.m1370d();
            this.f1157a = this.f1163g;
        } catch (Exception e) {
            e.printStackTrace();
            this.f1157a = null;
        }
        setVisibility(0);
        this.f1159c.removeMessages(1);
        this.f1159c.sendEmptyMessage(1);
    }

    public void draw(Canvas canvas) {
        if (!(this.f1158b != null && this.f1158b.width() == getWidth() && this.f1158b.height() == getHeight())) {
            this.f1158b = new Rect(0, 0, getWidth(), getHeight());
        }
        if (this.f1157a != null) {
            this.f1157a.m1367a(canvas, this.f1158b);
        }
        super.draw(canvas);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.f1157a != null) {
            this.f1157a.m1369c();
        }
        return super.onTouchEvent(motionEvent);
    }
}
