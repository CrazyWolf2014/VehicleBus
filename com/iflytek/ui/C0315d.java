package com.iflytek.ui;

import android.view.View;
import android.view.View.OnClickListener;
import com.iflytek.p005a.C0247c;

/* renamed from: com.iflytek.ui.d */
final class C0315d implements OnClickListener {
    final /* synthetic */ C1087a f1178a;

    C0315d(C1087a c1087a) {
        this.f1178a = c1087a;
    }

    public void onClick(View view) {
        C0247c.m1070a("asr").m1069b("asr.more");
        this.f1178a.m2204d(this.f1178a.f2063i.getVisibility() != 0);
    }
}
