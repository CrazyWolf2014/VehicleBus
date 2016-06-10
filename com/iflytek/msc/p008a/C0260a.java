package com.iflytek.msc.p008a;

import android.content.Context;
import android.os.SystemClock;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.p006b.C0252a;
import com.iflytek.p006b.C0253b;
import com.iflytek.speech.SpeechError;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/* renamed from: com.iflytek.msc.a.a */
public class C0260a {
    protected int f990a;
    protected boolean f991b;
    protected String f992c;
    protected Context f993d;
    protected volatile boolean f994e;
    protected volatile C0259a f995f;
    protected long f996g;
    protected int f997h;
    protected SpeechError f998i;
    private C0252a f999j;
    private Runnable f1000k;

    /* renamed from: com.iflytek.msc.a.a.a */
    protected enum C0259a {
        idle,
        init,
        start,
        recording,
        stoprecord,
        waitresult,
        exiting,
        exited
    }

    public C0260a(Context context) {
        this.f990a = SyncHttpTransportSE.DEFAULTTIMEOUT;
        this.f991b = true;
        this.f992c = null;
        this.f993d = null;
        this.f999j = new C0252a();
        this.f994e = false;
        this.f995f = C0259a.idle;
        this.f996g = 0;
        this.f997h = BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
        this.f998i = null;
        this.f1000k = new C0261b(this);
        this.f993d = context;
        this.f994e = false;
    }

    public static void m1152a(long j, int i) throws SpeechError {
        if (SystemClock.elapsedRealtime() - j > ((long) i)) {
            throw new SpeechError(2, SyncHttpTransportSE.DEFAULTTIMEOUT);
        }
    }

    public void m1153a() {
        this.f994e = true;
        m1157b();
    }

    protected void m1154a(C0252a c0252a) {
        this.f999j = c0252a.m1126b();
    }

    protected synchronized void m1155a(C0259a c0259a) {
        if (this.f995f != C0259a.exited) {
            if (this.f995f != C0259a.exiting || c0259a == C0259a.exited) {
                C0276e.m1220a("setStatus success=" + c0259a);
                this.f995f = c0259a;
                this.f996g = SystemClock.elapsedRealtime();
            }
        }
    }

    protected void m1156a(String str) {
        this.f999j.m1123a(str, C0253b.f964a);
    }

    protected synchronized void m1157b() {
        if (this.f995f != C0259a.idle) {
            m1155a(C0259a.exiting);
        }
    }

    protected synchronized C0259a m1158c() {
        return this.f995f;
    }

    public String m1159d() {
        return this.f992c;
    }

    public C0252a m1160e() {
        return this.f999j;
    }

    protected void m1161f() {
        m1155a(C0259a.init);
        if (this.f999j.m1125a("crt", true)) {
            new Thread(this.f1000k).start();
        } else {
            this.f1000k.run();
        }
    }

    protected void m1162g() throws Exception {
    }

    protected void m1163h() {
        m1155a(C0259a.exited);
    }

    protected void m1164i() {
        this.f997h = this.f999j.m1117a("timeout", this.f997h);
        this.f991b = this.f999j.m1125a("plr", false);
    }

    protected String m1165j() {
        return getClass().toString();
    }
}
