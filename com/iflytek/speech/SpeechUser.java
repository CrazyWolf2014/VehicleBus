package com.iflytek.speech;

import android.content.Context;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.iflytek.msc.p008a.C0262c;
import com.iflytek.msc.p008a.C0262c.C1068a;
import com.iflytek.msc.p011d.C0269a;
import com.iflytek.msc.p011d.C1070b;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.msc.p013f.C0283k;
import org.xmlpull.v1.XmlPullParser;

public class SpeechUser extends C0262c {
    private static SpeechUser f2006e;
    private String f2007f;
    private String f2008g;
    private Login_State f2009h;

    public enum Login_State {
        Logined,
        Unlogin
    }

    /* renamed from: com.iflytek.speech.SpeechUser.a */
    private class C1276a extends C1068a {
        final /* synthetic */ SpeechUser f2383b;

        public C1276a(SpeechUser speechUser, SpeechListener speechListener) {
            this.f2383b = speechUser;
            super(speechUser, speechListener);
        }

        public void onEnd(SpeechError speechError) {
            if (speechError == null) {
                this.f2383b.f2009h = Login_State.Logined;
            }
            super.onEnd(speechError);
        }
    }

    static {
        f2006e = new SpeechUser();
    }

    private SpeechUser() {
        super(null, null);
        this.f2007f = XmlPullParser.NO_NAMESPACE;
        this.f2008g = XmlPullParser.NO_NAMESPACE;
        this.f2009h = Login_State.Unlogin;
    }

    public static SpeechUser getUser() {
        if (f2006e == null) {
            f2006e = new SpeechUser();
        }
        return f2006e;
    }

    protected boolean m2108c() {
        boolean z = true;
        if (f2006e != null) {
            z = logout();
        }
        if (z) {
            f2006e = null;
            C0276e.m1220a(m1169b() + " destory mInstance=null");
        }
        return z;
    }

    public Login_State getLoginState() {
        return this.f2009h;
    }

    public boolean login(Context context, String str, String str2, String str3, SpeechListener speechListener) {
        C0276e.m1220a("SpeechUser Login isLogined=" + this.f2009h);
        if (this.f2009h == Login_State.Logined) {
            return false;
        }
        if (isAvaible()) {
            this.f2007f = str;
            this.f2008g = str2;
            this.b = context;
            m1168a(str3);
            this.d = new C1070b(this.b);
            ((C1070b) this.d).m2097a(new C1276a(this, speechListener), this.f2007f, this.f2008g, getInitParam());
            return true;
        }
        new C1276a(this, speechListener).onEnd(new SpeechError(19, SyncHttpTransportSE.DEFAULTTIMEOUT));
        return true;
    }

    public boolean logout() {
        if (this.f2009h != Login_State.Logined) {
            return true;
        }
        C0283k.m1266a("QMSPLogOut", null);
        boolean a = C0269a.m1190a();
        if (!a) {
            return a;
        }
        this.f2009h = Login_State.Unlogin;
        return a;
    }
}
