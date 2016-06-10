package com.iflytek.msc.p009b;

import android.content.Context;
import com.iflytek.msc.p008a.C0260a.C0259a;
import com.iflytek.msc.p013f.C0283k;
import com.iflytek.speech.SpeechError;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/* renamed from: com.iflytek.msc.b.b */
public class C1275b extends C1069c {
    public C1275b(ConcurrentLinkedQueue<byte[]> concurrentLinkedQueue, Context context) {
        super(concurrentLinkedQueue, context);
    }

    void m2563k() throws SpeechError, IOException, InterruptedException {
        C0283k.m1266a("QISRSessionBegin", null);
        this.o.m1176a(this.d, this.s, m1160e(), this.t, this.n);
        m1155a(C0259a.stoprecord);
    }
}
