package com.iflytek.ui;

import com.iflytek.msc.p013f.C0275d;
import com.iflytek.speech.C0294a;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import java.util.ArrayList;

/* renamed from: com.iflytek.ui.b */
final class C1088b implements C0294a {
    final /* synthetic */ C1087a f2078a;

    C1088b(C1087a c1087a) {
        this.f2078a = c1087a;
    }

    public void m2230a() {
        if (this.f2078a.f2075u == 1) {
            this.f2078a.m2209g();
        }
    }

    public void m2231a(int i) {
        int i2 = i / 3;
        if (i2 == 0 && C0275d.m1215a(0, 7) < 2) {
            i2 = 1;
        }
        this.f2078a.c.f1150a.m1400a(i2);
    }

    public void m2232a(SpeechError speechError) {
        if (speechError == null || !this.f2078a.d) {
            this.f2078a.m1379j();
        } else {
            this.f2078a.m2197a(speechError);
        }
        if (this.f2078a.f2071q != null) {
            this.f2078a.f2071q.onEnd(speechError);
        }
    }

    public void m2233a(ArrayList<RecognizerResult> arrayList, boolean z) {
        if (this.f2078a.f2071q != null) {
            this.f2078a.f2071q.onResults(arrayList, z);
        }
    }

    public void m2234b() {
        this.f2078a.m2212h();
    }

    public void m2235c() {
    }
}
