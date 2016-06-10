package com.iflytek.p005a;

import android.content.Context;
import com.iflytek.msc.p013f.C0277f;
import com.iflytek.msc.p013f.C0280h;
import com.iflytek.msc.p013f.C0280h.C0279a;
import org.apache.http.util.EncodingUtils;

/* renamed from: com.iflytek.a.h */
final class C1067h implements C0279a {
    final /* synthetic */ Context f1969a;
    final /* synthetic */ C0250g f1970b;

    C1067h(C0250g c0250g, Context context) {
        this.f1970b = c0250g;
        this.f1969a = context;
    }

    public void m2066a(C0280h c0280h, byte[] bArr) {
        if (bArr != null) {
            try {
                String string = EncodingUtils.getString(C0277f.m1228c(bArr), "utf-8");
                if (!C0280h.m1241a(string)) {
                    this.f1970b.f957c.m1114a(string);
                    this.f1970b.m1103c();
                }
                C0245a.m1052a(this.f1969a).m1057a("ad_last_update", System.currentTimeMillis());
            } catch (Exception e) {
            }
        }
        this.f1970b.f956b = null;
    }

    public void m2067a(Exception exception) {
        if (exception != null) {
            exception.printStackTrace();
        }
        this.f1970b.f956b = null;
    }
}
