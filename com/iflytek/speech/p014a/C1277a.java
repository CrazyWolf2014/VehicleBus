package com.iflytek.speech.p014a;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.iflytek.msc.p009b.C0266a;
import com.iflytek.msc.p009b.C1069c;
import com.iflytek.msc.p009b.C1275b;
import com.iflytek.msc.p013f.C0273b;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.msc.p013f.C0278g;
import com.iflytek.p005a.C0247c;
import com.iflytek.p006b.C0252a;
import com.iflytek.p006b.C0253b;
import com.iflytek.speech.C0294a;
import com.iflytek.speech.C1076b;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/* renamed from: com.iflytek.speech.a.a */
public class C1277a extends C1076b {
    private static C1277a f2384e;
    private boolean f2385f;

    /* renamed from: com.iflytek.speech.a.a.a */
    private final class C1072a implements C0294a {
        final /* synthetic */ C1277a f2010a;
        private C0294a f2011b;
        private Handler f2012c;

        public C1072a(C1277a c1277a, C0294a c0294a) {
            this.f2010a = c1277a;
            this.f2011b = null;
            this.f2012c = new C0292b(this, Looper.getMainLooper());
            this.f2011b = c0294a;
        }

        public void m2110a() {
            C0276e.m1220a("onBeginOfSpeech");
            this.f2012c.sendMessage(this.f2012c.obtainMessage(2, 0, 0, null));
        }

        public void m2111a(int i) {
            this.f2012c.sendMessage(this.f2012c.obtainMessage(1, i, 0, null));
        }

        public void m2112a(SpeechError speechError) {
            String a = this.f2010a.d.m1160e().m1118a("aap");
            if (!TextUtils.isEmpty(a)) {
                C0278g.m1231a(((C1069c) this.f2010a.d).m2087l(), a);
            }
            C0273b.m1212b(this.f2010a.b, Boolean.valueOf(this.f2010a.f2385f));
            this.f2012c.sendMessage(this.f2012c.obtainMessage(0, speechError));
        }

        public void m2113a(ArrayList<RecognizerResult> arrayList, boolean z) {
            int i = 1;
            Handler handler = this.f2012c;
            if (!z) {
                i = 0;
            }
            this.f2012c.sendMessage(handler.obtainMessage(4, i, 0, arrayList));
        }

        public void m2114b() {
            this.f2012c.sendMessage(this.f2012c.obtainMessage(3, 0, 0, null));
        }

        public void m2115c() {
            C0273b.m1212b(this.f2010a.b, Boolean.valueOf(this.f2010a.f2385f));
            this.f2012c.sendMessage(this.f2012c.obtainMessage(5));
        }
    }

    static {
        f2384e = null;
    }

    protected C1277a(Context context, String str) {
        super(context, str);
        this.f2385f = false;
    }

    public static C1076b m2566b(Context context, String str) {
        if (f2384e == null) {
            f2384e = new C1277a(context, str);
        }
        return f2384e;
    }

    public static C1277a m2570g() {
        return f2384e;
    }

    public int m2571a(boolean z) {
        int d;
        synchronized (this.c) {
            if (z) {
                d = C0266a.m1174d("upflow");
            } else {
                d = C1069c.f1974q;
            }
        }
        return d;
    }

    protected void m2572a() throws Exception {
        C0266a.m1171a(this.b, this.a);
        super.m1167a();
    }

    public void m2573a(int i) {
        SpeechConfig.m1313a(i);
    }

    public void m2574a(RATE rate) {
        SpeechConfig.m1314a(rate);
    }

    public void m2575a(C0294a c0294a, String str, String str2, String str3) {
        synchronized (this.c) {
            this.f2385f = new C0252a(str2, C0253b.f964a).m1125a("request_audio_focus", true);
            this.d = new C1069c(this.b);
            C0273b.m1211a(this.b, Boolean.valueOf(this.f2385f));
            ((C1069c) this.d).m2075a(str, str2, str3, new C1072a(this, c0294a));
        }
    }

    public void m2576a(C0294a c0294a, ConcurrentLinkedQueue<byte[]> concurrentLinkedQueue, String str, String str2, String str3) {
        synchronized (this.c) {
            if (isAvaible()) {
                this.d = new C1275b(concurrentLinkedQueue, this.b);
                ((C1069c) this.d).m2075a(str, str2, str3, new C1072a(this, c0294a));
                return;
            }
            new C1072a(this, c0294a).m2112a(new SpeechError(19, SyncHttpTransportSE.DEFAULTTIMEOUT));
        }
    }

    public int m2577b(boolean z) {
        int d;
        synchronized (this.c) {
            if (z) {
                d = C0266a.m1174d("downflow");
            } else {
                d = C1069c.f1975r;
            }
        }
        return d;
    }

    protected boolean m2578c() {
        boolean z = true;
        if (f2384e != null) {
            z = C0266a.m1173a();
            if (z) {
                f2384e = null;
            }
        }
        return z;
    }

    public void cancel() {
        super.cancel();
    }

    public void m2579e() {
        synchronized (this.c) {
            if (this.d != null) {
                ((C1069c) this.d).m2080a(true);
                C0247c.m1070a("asr").m1069b("asr.stop");
            }
        }
    }

    public int m2580f() {
        return SpeechConfig.m1312a();
    }

    public ConcurrentLinkedQueue<byte[]> m2581h() {
        ConcurrentLinkedQueue<byte[]> l;
        synchronized (this.c) {
            if (this.d != null) {
                l = ((C1069c) this.d).m2087l();
            } else {
                l = null;
            }
        }
        return l;
    }
}
