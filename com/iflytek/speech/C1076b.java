package com.iflytek.speech;

import android.content.Context;
import com.iflytek.msc.p008a.C0262c;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.p014a.C1277a;
import java.util.concurrent.ConcurrentLinkedQueue;

/* renamed from: com.iflytek.speech.b */
public abstract class C1076b extends C0262c {
    protected C1076b(Context context, String str) {
        super(context, str);
    }

    public static C1076b m2129a(Context context, String str) {
        return C1277a.m2566b(context, str);
    }

    public static C1076b m2130d() {
        return C1277a.m2570g();
    }

    public int m2131a(boolean z) {
        return 0;
    }

    public void m2132a(int i) {
    }

    public void m2133a(RATE rate) {
    }

    public abstract void m2134a(C0294a c0294a, String str, String str2, String str3);

    public abstract void m2135a(C0294a c0294a, ConcurrentLinkedQueue<byte[]> concurrentLinkedQueue, String str, String str2, String str3);

    public int m2136b(boolean z) {
        return 0;
    }

    public abstract void m2137e();

    public abstract int m2138f();
}
