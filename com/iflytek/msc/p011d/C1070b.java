package com.iflytek.msc.p011d;

import android.content.Context;
import android.text.TextUtils;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.iflytek.msc.p008a.C0260a;
import com.iflytek.msc.p008a.C0260a.C0259a;
import com.iflytek.msc.p013f.C0283k;
import com.iflytek.p006b.C0252a;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SpeechListener;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.msc.d.b */
public class C1070b extends C0260a {
    private String f1989j;
    private C0270a f1990k;
    private byte[] f1991l;
    private String f1992m;
    private String f1993n;
    private String f1994o;
    private C0269a f1995p;
    private SpeechListener f1996q;

    /* renamed from: com.iflytek.msc.d.b.a */
    enum C0270a {
        login,
        upload,
        download,
        search
    }

    public C1070b(Context context) {
        super(context);
        this.f1989j = XmlPullParser.NO_NAMESPACE;
        this.f1990k = C0270a.upload;
        this.f1991l = null;
        this.f1992m = XmlPullParser.NO_NAMESPACE;
        this.f1993n = XmlPullParser.NO_NAMESPACE;
        this.f1994o = XmlPullParser.NO_NAMESPACE;
        this.f1995p = new C0269a();
        this.f1996q = null;
    }

    public void m2095a(SpeechListener speechListener, String str) {
        this.f1996q = speechListener;
        m1156a(str);
        this.f1990k = C0270a.download;
        m1161f();
    }

    public void m2096a(SpeechListener speechListener, String str, String str2) {
        this.f1996q = speechListener;
        m1156a(str);
        this.f1990k = C0270a.search;
        this.f1989j = str2;
        m1161f();
    }

    public void m2097a(SpeechListener speechListener, String str, String str2, C0252a c0252a) {
        this.f1993n = str;
        this.f1994o = str2;
        this.f1996q = speechListener;
        this.f1990k = C0270a.login;
        m1154a(c0252a);
        m1161f();
    }

    public void m2098a(SpeechListener speechListener, String str, String str2, byte[] bArr) {
        this.f1996q = speechListener;
        m1156a(str2);
        this.f1992m = str;
        this.f1991l = bArr;
        this.f1990k = C0270a.upload;
        m1161f();
    }

    protected void m2099g() throws Exception {
        byte[] bArr = null;
        if (this.f == C0259a.init) {
            m1155a(C0259a.start);
        } else if (this.f == C0259a.start) {
            if (this.f1990k == C0270a.login) {
                C0283k.m1266a("QMSPLogin", null);
                C0269a.m1189a(this.d, this.f1993n, this.f1994o, m1160e());
            } else if (this.f1990k == C0270a.upload) {
                if (this.f1991l == null || this.f1991l.length <= 0) {
                    throw new SpeechError(13, SyncHttpTransportSE.DEFAULTTIMEOUT);
                }
                C0283k.m1266a("QMSPUploadData", null);
                bArr = this.f1995p.m1193a(this.d, this.f1992m, this.f1991l, m1160e());
            } else if (this.f1990k == C0270a.download) {
                C0283k.m1266a("QMSPDownloadData", null);
                bArr = this.f1995p.m1191a(this.d, m1160e());
            } else if (this.f1990k == C0270a.search) {
                if (TextUtils.isEmpty(this.f1989j)) {
                    throw new SpeechError(13, SyncHttpTransportSE.DEFAULTTIMEOUT);
                }
                C0283k.m1266a("QMSPSearch", null);
                bArr = this.f1995p.m1192a(this.d, m1160e(), this.f1989j);
            }
            if (this.f1990k != C0270a.login) {
                if (bArr == null) {
                    throw new SpeechError(5, SyncHttpTransportSE.DEFAULTTIMEOUT);
                } else if (this.f1996q != null) {
                    this.f1996q.onData(bArr);
                }
            }
            m1157b();
        }
        super.m1162g();
    }

    protected void m2100h() {
        super.m1163h();
        if (this.f1996q != null && !this.e) {
            this.f1996q.onEnd(this.i);
        }
    }
}
