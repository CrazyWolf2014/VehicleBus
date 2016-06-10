package com.iflytek.p005a;

import com.iflytek.msc.p013f.C0277f;
import com.iflytek.msc.p013f.C0278g;
import com.iflytek.msc.p013f.C0280h;
import com.iflytek.msc.p013f.C0280h.C0279a;
import org.apache.http.util.EncodingUtils;

/* renamed from: com.iflytek.a.d */
final class C1066d implements C0279a {
    final /* synthetic */ C0247c f1968a;

    C1066d(C0247c c0247c) {
        this.f1968a = c0247c;
    }

    public void m2064a(C0280h c0280h, byte[] bArr) {
        try {
            if (C0280h.m1241a(EncodingUtils.getString(C0277f.m1228c(bArr), "utf-8"))) {
                m2065a(new Exception());
            }
            this.f1968a.f944f = null;
        } catch (Exception e) {
        }
    }

    public void m2065a(Exception exception) {
        byte[] bArr = (byte[]) this.f1968a.f944f.m1246a();
        C0278g.m1233a(C0277f.m1226a(bArr), C0278g.m1229a(null) + "events.dat", true, 0);
        this.f1968a.f944f = null;
    }
}
