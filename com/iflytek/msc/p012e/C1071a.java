package com.iflytek.msc.p012e;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.iflytek.msc.p008a.C0260a;
import com.iflytek.msc.p008a.C0260a.C0259a;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.msc.p013f.C0282j;
import com.iflytek.msc.p013f.C0283k;
import com.iflytek.p005a.C0247c;
import com.iflytek.p006b.C0254c;
import com.iflytek.speech.C0296f;
import com.iflytek.speech.C1078c;
import com.iflytek.speech.SpeechError;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.msc.e.a */
public class C1071a extends C0260a {
    public static int f1997j;
    public static int f1998k;
    private String f1999l;
    private C0271b f2000m;
    private C0296f f2001n;
    private ArrayList<byte[]> f2002o;
    private int f2003p;
    private int f2004q;
    private long f2005r;

    static {
        f1997j = 0;
        f1998k = 0;
    }

    public C1071a(Context context) {
        super(context);
        this.f1999l = XmlPullParser.NO_NAMESPACE;
        this.f2000m = null;
        this.f2001n = null;
        this.f2002o = null;
        this.f2003p = 0;
        this.f2004q = 0;
        this.f2005r = 0;
        this.f2000m = new C0271b();
        this.f2002o = new ArrayList();
    }

    public void m2101a(String str, String str2, C0296f c0296f) {
        this.f1999l = str;
        m1156a(str2);
        this.f2001n = c0296f;
        if (str == null || TextUtils.isEmpty(str)) {
            this.i = new SpeechError(13, SyncHttpTransportSE.DEFAULTTIMEOUT);
            this.f2001n.m1351a(this.i);
            C0247c.m1070a("tts").m1063a(this.i.getErrorCode());
            C0247c.m1070a("tts").m1066a("end");
            C0247c.m1071a().m1079a(this.d, false);
            return;
        }
        m1161f();
    }

    protected void m2102g() throws Exception {
        if (this.f == C0259a.init) {
            if (!C0254c.m1137a(m1160e())) {
                C0282j.m1263a(this.d);
            }
            C0283k.m1266a("QTTSInit", null);
            C0271b.m1194a(this.d, C1078c.m2146d().getInitParam());
            this.f2005r = SystemClock.elapsedRealtime();
            m1155a(C0259a.start);
        } else if (this.f == C0259a.start) {
            C0283k.m1266a("QTTSSessionBegin", null);
            if (this.f2000m.m1201a(this.d, m1160e(), "gb2312")) {
                C0283k.m1266a("QTTSTextPut", null);
                this.f2000m.m1200a(this.f1999l.getBytes("gb2312"));
                m1155a(C0259a.waitresult);
            } else {
                Thread.sleep(50);
                C0260a.m1152a(this.f2005r, this.h);
            }
        } else if (this.f == C0259a.waitresult) {
            if (!this.f2000m.m1206e()) {
                Object c = this.f2000m.m1204c();
                if (!(c == null || this.f2001n == null)) {
                    C0283k.m1266a("QTTSAudioGet", XmlPullParser.NO_NAMESPACE + c.length);
                    int d = (this.f2000m.m1205d() / 2) - 1;
                    if (!(this.f2003p == 0 || d == this.f2003p || this.f2002o.size() <= 0)) {
                        this.f2001n.m1352a(this.f2002o, (this.f2003p * 100) / this.f1999l.length(), this.f2004q, this.f2003p);
                        this.f2002o = new ArrayList();
                        this.f2004q = this.f2003p;
                    }
                    this.f2005r = SystemClock.elapsedRealtime();
                    this.f2003p = d;
                    this.f2002o.add(c);
                }
                C0260a.m1152a(this.f2005r, this.h);
            } else if (this.f2001n != null) {
                this.f2001n.m1352a(this.f2002o, 100, this.f2004q, this.f1999l.length());
                m1157b();
            }
        }
        super.m1162g();
    }

    protected void m2103h() {
        f1997j = this.f2000m.m1202b("upflow");
        f1998k = this.f2000m.m1202b("downflow");
        C0247c.m1070a("tts").m1067a("lgid", this.f2000m.m1203c("loginid"));
        this.c = this.f2000m.m1203c(AlixDefine.SID);
        C0247c.m1070a("tts").m1067a(AlixDefine.SID, this.c);
        C0283k.m1266a("QTTSSessionEnd", null);
        if (this.f2001n == null) {
            this.f2000m.m1199a("user abort");
        } else if (this.i != null) {
            this.f2000m.m1199a("error" + this.i.getErrorCode());
            C0276e.m1220a("QTts Error Code = " + this.i.getErrorCode());
        } else {
            this.f2000m.m1199a("success");
        }
        if (this.i != null) {
            C0247c.m1070a("tts").m1063a(this.i.getErrorCode());
        }
        super.m1163h();
        C0247c.m1070a("tts").m1066a("end");
        if (this.f2001n != null) {
            if (this.e) {
                C0276e.m1220a("SynthesizerPlayer#onCancel");
                this.f2001n.m1353b();
            } else {
                C0276e.m1220a("SynthesizerPlayer#onEnd");
                this.f2001n.m1351a(this.i);
            }
        }
        if (this.i != null || this.e) {
            C0247c.m1071a().m1079a(this.d, false);
        }
    }
}
