package com.iflytek.speech;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.iflytek.msc.p008a.C0262c;
import com.iflytek.msc.p012e.C0271b;
import com.iflytek.msc.p012e.C1071a;
import com.iflytek.speech.SpeechConfig.RATE;
import java.util.ArrayList;

/* renamed from: com.iflytek.speech.c */
public class C1078c extends C0262c {
    private static C1078c f2023e;

    /* renamed from: com.iflytek.speech.c.a */
    public class C1077a implements C0296f {
        final /* synthetic */ C1078c f2019a;
        private C0296f f2020b;
        private int f2021c;
        private Handler f2022d;

        public C1077a(C1078c c1078c, C0296f c0296f) {
            this.f2019a = c1078c;
            this.f2020b = null;
            this.f2021c = 0;
            this.f2022d = new C0295d(this, Looper.getMainLooper());
            this.f2020b = c0296f;
        }

        public void m2141a() {
            Message.obtain(this.f2022d, 2, 0, 0).sendToTarget();
        }

        public void m2142a(SpeechError speechError) {
            Message.obtain(this.f2022d, 1, speechError).sendToTarget();
        }

        public void m2143a(ArrayList<byte[]> arrayList, int i, int i2, int i3) {
            this.f2021c = i;
            Message.obtain(this.f2022d, 3, i2, i3, arrayList).sendToTarget();
        }

        public void m2144b() {
            Message.obtain(this.f2022d, 4, null).sendToTarget();
        }
    }

    static {
        f2023e = null;
    }

    private C1078c(Context context, String str) {
        super(context, str);
    }

    public static C1078c m2145a(Context context, String str) {
        if (f2023e == null) {
            f2023e = new C1078c(context, str);
        }
        return f2023e;
    }

    public static C1078c m2146d() {
        return f2023e;
    }

    public int m2147a(boolean z) {
        return z ? C0271b.m1197d("downflow") : C1071a.f1998k;
    }

    protected void m2148a() throws Exception {
        C0271b.m1194a(this.b, this.a);
        super.m1167a();
    }

    public void m2149a(int i) {
        SpeechConfig.m1321c(i);
    }

    public void m2150a(RATE rate) {
        SpeechConfig.m1322c(rate);
    }

    public synchronized boolean m2151a(String str, String str2, C0296f c0296f) {
        C0296f c1077a = new C1077a(this, c0296f);
        this.d = new C1071a(this.b);
        ((C1071a) this.d).m2101a(str, str2, c1077a);
        return C0271b.m1195a();
    }

    public int m2152b(boolean z) {
        return z ? C0271b.m1197d("upflow") : C1071a.f1997j;
    }

    public void m2153b(int i) {
        SpeechConfig.m1318b(i);
    }

    public void m2154b(String str) {
        SpeechConfig.m1315a(str);
    }

    public void m2155c(int i) {
        SpeechConfig.m1324d(i);
    }

    public void m2156c(String str) {
        SpeechConfig.m1319b(str);
    }

    protected boolean m2157c() {
        boolean z = true;
        if (f2023e != null) {
            z = C0271b.m1196b();
            if (z) {
                f2023e = null;
            }
        }
        return z;
    }
}
