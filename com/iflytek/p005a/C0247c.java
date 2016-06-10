package com.iflytek.p005a;

import android.content.Context;
import android.text.TextUtils;
import com.iflytek.msc.p013f.C0277f;
import com.iflytek.msc.p013f.C0278g;
import com.iflytek.msc.p013f.C0280h;
import com.iflytek.msc.p013f.C0280h.C0279a;
import com.iflytek.speech.C1076b;
import com.iflytek.speech.SynthesizerPlayer;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.File;

/* renamed from: com.iflytek.a.c */
public class C0247c {
    public static long f939a;
    private static C0247c f940b;
    private C0246b f941c;
    private C0246b f942d;
    private C0246b f943e;
    private C0280h f944f;

    static {
        f940b = null;
        f939a = 0;
    }

    private C0247c() {
        this.f941c = null;
        this.f942d = null;
        this.f943e = null;
        this.f944f = null;
        f939a = System.currentTimeMillis();
        this.f941c = new C0246b();
        this.f941c.m1062a();
        m1081a(null, true);
    }

    public static C0246b m1070a(String str) {
        return C0247c.m1071a().m1075b(str);
    }

    public static synchronized C0247c m1071a() {
        C0247c c0247c;
        synchronized (C0247c.class) {
            if (f940b == null) {
                f940b = new C0247c();
            }
            c0247c = f940b;
        }
        return c0247c;
    }

    private synchronized void m1074a(Context context) {
        if (this.f944f == null) {
            try {
                this.f944f = new C0280h();
                this.f944f.m1247a(1);
                C0279a c1066d = new C1066d(this);
                String str = C0278g.m1229a(null) + "events.dat";
                byte[] b = C0278g.m1235b(str);
                this.f944f.m1249a((Object) b);
                StringBuffer stringBuffer = new StringBuffer(new String(C0277f.m1226a(b), "utf-8"));
                stringBuffer.append(SpecilApiUtil.LINE_SEP);
                stringBuffer.append(new C0248e(context).m1083a());
                this.f944f.m1250a("http://service.voicecloud.cn/mscAd/upload_data.php", "cmd=ctrllog&ver=1", C0277f.m1227b(stringBuffer.toString().getBytes("utf-8")));
                this.f944f.m1248a(c1066d);
                C0278g.m1230a(str);
            } catch (Exception e) {
                this.f944f = null;
            }
        }
    }

    private C0246b m1075b(String str) {
        return (this.f942d == null || !"asr".equals(str)) ? (this.f943e == null || !"tts".equals(str)) ? this.f942d != null ? this.f942d : this.f943e != null ? this.f943e : this.f941c : this.f943e : this.f942d;
    }

    private void m1076b() {
    }

    private boolean m1077c() {
        String str = C0278g.m1229a(null) + "events.dat";
        File file = new File(str);
        if (file.length() <= 10240) {
            return file.length() > 1024;
        } else {
            C0278g.m1230a(str);
            return false;
        }
    }

    private void m1078d() {
        if (this.f941c.f938a.length() > 0) {
            try {
                C0278g.m1233a(C0277f.m1226a(this.f941c.f938a.toString().getBytes("utf-8")), C0278g.m1229a(null) + "events.dat", true, -1);
                this.f941c.m1068b();
            } catch (Exception e) {
            }
            m1076b();
        }
    }

    public void m1079a(Context context, boolean z) {
        if (C1076b.m2130d() != null && !C1076b.m2130d().isAvaible()) {
            return;
        }
        if (SynthesizerPlayer.getSynthesizerPlayer() == null || SynthesizerPlayer.getSynthesizerPlayer().isAvaible()) {
            if (z) {
                C0250g.m1098a().m1105a(context);
            }
            m1081a(null, true);
            if (m1077c() && z && this.f944f == null) {
                m1074a(context);
            }
        }
    }

    public void m1080a(String str, String str2) {
        m1081a(str, false);
        if ("asr".equals(str)) {
            this.f942d = new C0246b();
            this.f942d.m1066a("asr.start");
        } else if ("tts".equals(str)) {
            this.f943e = new C0246b();
            this.f943e.m1066a("tts.start");
        }
    }

    public void m1081a(String str, boolean z) {
        if (this.f942d != null && (TextUtils.isEmpty(str) || "asr".equals(str))) {
            this.f941c.m1064a(this.f942d);
            this.f942d = null;
        }
        if (this.f943e != null && (TextUtils.isEmpty(str) || "tts".equals(str))) {
            this.f941c.m1064a(this.f943e);
            this.f943e = null;
        }
        if (z && this.f943e == null && this.f942d == null) {
            m1078d();
        }
    }
}
