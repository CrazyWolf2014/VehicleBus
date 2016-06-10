package com.iflytek.ui.p015a;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.iflytek.p005a.C0245a;
import com.iflytek.p007c.C0255a;
import com.iflytek.ui.p015a.C0303j.C0302a;
import com.iflytek.ui.p016b.C0310a;

/* renamed from: com.iflytek.ui.a.l */
public class C0304l extends LinearLayout {
    private C0302a f1138a;
    protected C0307n f1139c;
    protected boolean f1140d;
    protected boolean f1141e;

    public C0304l(Context context) {
        super(context);
        this.f1138a = null;
        this.f1139c = null;
        this.f1140d = true;
        this.f1141e = true;
        C0245a.m1052a(context);
        C0310a.m1404a(context, C0255a.f966a);
    }

    protected static boolean m1372a(ViewGroup viewGroup) {
        try {
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                childAt.setOnClickListener(null);
                childAt.setBackgroundDrawable(null);
            }
            viewGroup.removeAllViews();
            viewGroup.setOnClickListener(null);
            viewGroup.setBackgroundDrawable(null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void m1373a(C0302a c0302a) {
        this.f1138a = c0302a;
    }

    public void m1374a(boolean z, boolean z2) {
        this.f1140d = z;
        this.f1141e = z2;
    }

    protected void m1375b() {
    }

    protected void m1376c() {
    }

    protected boolean m1377d() {
        try {
            C0304l.m1372a(this.f1139c);
            C0304l.m1372a((ViewGroup) this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void m1378i() {
        Intent intent = new Intent();
        if (VERSION.SDK_INT > 10) {
            intent.setAction("android.settings.SETTINGS");
        } else {
            intent.setAction("android.settings.WIRELESS_SETTINGS");
        }
        intent.addFlags(268435456);
        getContext().startActivity(intent);
    }

    public void m1379j() {
        if (this.f1138a != null) {
            this.f1138a.m1371a();
        }
    }
}
