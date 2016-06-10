package com.iflytek.ui.p015a;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

/* renamed from: com.iflytek.ui.a.j */
public class C0303j extends Dialog {
    protected C0304l f1136a;
    private C0302a f1137b;

    /* renamed from: com.iflytek.ui.a.j.a */
    public interface C0302a {
        void m1371a();
    }

    public C0303j(Context context) {
        super(context);
        this.f1136a = null;
        this.f1137b = new C1086k(this);
    }

    public boolean destory() {
        if (isShowing()) {
            return false;
        }
        boolean d = this.f1136a.m1377d();
        this.f1136a = null;
        return d;
    }

    public void dismiss() {
        this.f1136a.m1376c();
        super.dismiss();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(this.f1136a);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    public void show() {
        this.f1136a.m1373a(this.f1137b);
        this.f1136a.m1375b();
        super.show();
    }
}
