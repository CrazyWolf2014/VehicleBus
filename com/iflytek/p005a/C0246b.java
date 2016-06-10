package com.iflytek.p005a;

import android.text.TextUtils;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.a.b */
public class C0246b {
    public StringBuffer f938a;

    public C0246b() {
        this.f938a = null;
        this.f938a = new StringBuffer();
    }

    public long m1061a(boolean z) {
        long currentTimeMillis = System.currentTimeMillis();
        return !z ? currentTimeMillis - C0247c.f939a : currentTimeMillis;
    }

    public void m1062a() {
        this.f938a.append(SpecilApiUtil.LINE_SEP);
        m1067a("msc.init", XmlPullParser.NO_NAMESPACE + m1061a(true));
    }

    public void m1063a(int i) {
        m1066a("error.tm");
        m1067a("error.code", XmlPullParser.NO_NAMESPACE + i);
    }

    public void m1064a(C0246b c0246b) {
        if (c0246b != null) {
            this.f938a.append(SpecilApiUtil.LINE_SEP);
            this.f938a.append(c0246b.f938a);
        }
    }

    public void m1065a(C0249f c0249f) {
        if (c0249f != null) {
            m1066a("link.click");
            m1067a("link.id", c0249f.m1093d());
            m1067a("link.url", c0249f.m1092c());
        }
    }

    public void m1066a(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.f938a.append(str);
            this.f938a.append("=");
            this.f938a.append(XmlPullParser.NO_NAMESPACE + m1061a(false));
            this.f938a.append(",");
        }
    }

    public void m1067a(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            this.f938a.append(str);
            this.f938a.append("=");
            this.f938a.append(str2.replace("=", "~").replace(",", "^"));
            this.f938a.append(",");
        }
    }

    public void m1068b() {
        this.f938a.delete(0, this.f938a.length());
    }

    public void m1069b(String str) {
        if (!TextUtils.isEmpty(str)) {
            m1066a("btn.click");
            m1067a("btn.id", str);
        }
    }

    public String toString() {
        return this.f938a.toString();
    }
}
