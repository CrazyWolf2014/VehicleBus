package com.iflytek.p005a;

import android.content.Context;
import com.cnlaunch.framework.common.Constants;
import com.iflytek.msc.p013f.C0277f;
import com.iflytek.msc.p013f.C0278g;
import com.iflytek.msc.p013f.C0280h;
import com.iflytek.msc.p013f.C0280h.C0279a;
import com.iflytek.speech.SpeechError;
import java.io.UnsupportedEncodingException;
import org.apache.http.util.EncodingUtils;

/* renamed from: com.iflytek.a.g */
public class C0250g {
    private static C0250g f955a;
    private C0280h f956b;
    private C0251i f957c;

    static {
        f955a = null;
    }

    private C0250g() {
        this.f956b = null;
        this.f957c = null;
        this.f957c = new C0251i();
        m1101b();
    }

    public static C0250g m1098a() {
        if (f955a == null) {
            f955a = new C0250g();
        }
        return f955a;
    }

    private void m1101b() {
        try {
            byte[] b = C0278g.m1235b(C0278g.m1229a(null) + "links.dat");
            if (b != null) {
                this.f957c.m1114a(EncodingUtils.getString(C0277f.m1228c(b), "utf-8"));
            }
        } catch (Exception e) {
        }
    }

    private synchronized void m1103c() {
        try {
            C0278g.m1233a(C0277f.m1227b(this.f957c.toString().getBytes("utf-8")), C0278g.m1229a(null) + "links.dat", false, 0);
        } catch (UnsupportedEncodingException e) {
        }
    }

    public C0249f m1104a(Context context, SpeechError speechError, boolean z) {
        return this.f957c.m1109a(context, speechError, z);
    }

    public synchronized void m1105a(Context context) {
        if (this.f957c.m1115a(context) && this.f956b == null) {
            try {
                this.f956b = new C0280h();
                this.f956b.m1247a(1);
                C0279a c1067h = new C1067h(this, context);
                C0248e c0248e = new C0248e(context);
                c0248e.m1085a(Constants.VER, this.f957c.m1110a());
                this.f956b.m1250a("http://service.voicecloud.cn/mscAd/ad_version.php", null, C0277f.m1227b(c0248e.m1084a(true).toString().getBytes("utf-8")));
                this.f956b.m1248a(c1067h);
            } catch (Exception e) {
                this.f956b = null;
            }
        }
    }

    public void m1106a(C0249f c0249f) {
        if (c0249f.m1089a()) {
            c0249f.m1096g();
            if (!c0249f.m1095f()) {
                this.f957c.m1113a(c0249f);
            }
            m1103c();
        }
    }

    public C0249f m1107b(Context context) {
        return this.f957c.m1116b(context);
    }
}
