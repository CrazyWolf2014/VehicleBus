package com.iflytek.ui;

import android.view.View;
import android.view.View.OnClickListener;
import com.iflytek.p005a.C0247c;

/* renamed from: com.iflytek.ui.c */
final class C0314c implements OnClickListener {
    final /* synthetic */ C1087a f1177a;

    C0314c(C1087a c1087a) {
        this.f1177a = c1087a;
    }

    public void onClick(View view) {
        this.f1177a.m1378i();
        this.f1177a.f2061g.setOnClickListener(this);
        this.f1177a.f2062h.performClick();
        C0247c.m1070a("asr").m1069b("netset");
    }
}
