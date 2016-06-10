package com.iflytek.msc.p008a;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.iflytek.msc.p008a.C0260a.C0259a;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.p005a.C0245a;
import com.iflytek.p006b.C0252a;
import com.iflytek.p006b.C0253b;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SpeechListener;

/* renamed from: com.iflytek.msc.a.c */
public abstract class C0262c {
    protected C0252a f1002a;
    protected Context f1003b;
    protected Object f1004c;
    protected volatile C0260a f1005d;

    /* renamed from: com.iflytek.msc.a.c.a */
    protected class C1068a implements SpeechListener {
        final /* synthetic */ C0262c f1971a;
        private SpeechListener f1972b;
        private Handler f1973c;

        public C1068a(C0262c c0262c, SpeechListener speechListener) {
            this.f1971a = c0262c;
            this.f1972b = null;
            this.f1973c = new C0264e(this, Looper.getMainLooper());
            this.f1972b = speechListener;
        }

        public void onData(byte[] bArr) {
            this.f1973c.sendMessage(this.f1973c.obtainMessage(1, bArr));
        }

        public void onEnd(SpeechError speechError) {
            this.f1973c.sendMessage(this.f1973c.obtainMessage(2, speechError));
        }

        public void onEvent(int i, Bundle bundle) {
            this.f1973c.sendMessage(this.f1973c.obtainMessage(0, i, 0, bundle));
        }
    }

    protected C0262c(Context context, String str) {
        this.f1002a = new C0252a();
        this.f1003b = null;
        this.f1004c = new Object();
        this.f1005d = null;
        synchronized (this.f1004c) {
            C0245a.m1052a(context);
            this.f1002a.m1123a(str, C0253b.f964a);
            if (context != null) {
                this.f1003b = context.getApplicationContext();
                m1166d();
            } else {
                this.f1003b = null;
            }
        }
    }

    private synchronized void m1166d() {
        new C0263d(this).start();
    }

    protected void m1167a() throws Exception {
    }

    protected void m1168a(String str) {
        this.f1002a.m1123a(str, C0253b.f964a);
    }

    protected String m1169b() {
        return getClass().toString();
    }

    protected boolean m1170c() {
        return true;
    }

    public void cancel() {
        if (this.f1005d != null) {
            this.f1005d.m1153a();
        }
    }

    public boolean destory() {
        boolean c;
        synchronized (this.f1004c) {
            C0276e.m1224c(m1169b() + "destory called mscer = " + this.f1005d);
            if (isAvaible()) {
                c = m1170c();
                C0276e.m1224c(m1169b() + "destory =" + c);
            } else {
                this.f1005d.m1153a();
                C0276e.m1224c(m1169b() + "destory false");
                c = false;
            }
        }
        return c;
    }

    public boolean destory(int i) {
        try {
            if (destory()) {
                return true;
            }
            long currentTimeMillis = System.currentTimeMillis();
            while (System.currentTimeMillis() - currentTimeMillis <= ((long) i)) {
                if (destory()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void finalize() throws Throwable {
        C0276e.m1220a(m1169b() + " finalize called");
        super.finalize();
    }

    public C0252a getInitParam() {
        return this.f1002a;
    }

    public String getSessionID() {
        return this.f1005d != null ? this.f1005d.m1159d() : null;
    }

    public boolean isAvaible() {
        return this.f1005d == null || this.f1005d.m1158c() == C0259a.idle || this.f1005d.m1158c() == C0259a.exited;
    }
}
