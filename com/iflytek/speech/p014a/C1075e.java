package com.iflytek.speech.p014a;

import com.iflytek.p005a.C0247c;
import com.iflytek.record.PcmPlayer.C0284a;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.p014a.C1073c.C0293a;

/* renamed from: com.iflytek.speech.a.e */
final class C1075e implements C0284a {
    final /* synthetic */ C0293a f2018a;

    C1075e(C0293a c0293a) {
        this.f2018a = c0293a;
    }

    public void m2124a() {
        C0247c.m1070a("tts").m1066a("play.onpause");
        if (this.f2018a.f1118d != null) {
            this.f2018a.f1118d.onPlayPaused();
        }
    }

    public void m2125a(int i, int i2, int i3) {
        if (this.f2018a.f1118d != null) {
            this.f2018a.f1118d.onPlayPercent(i, i2, i3);
        }
    }

    public void m2126a(SpeechError speechError) {
        C0247c.m1070a("tts").m1063a(speechError.getErrorCode());
        C0247c.m1071a().m1081a("tts", true);
        if (this.f2018a.f1118d != null) {
            this.f2018a.f1118d.onEnd(speechError);
            if (this.f2018a.f1116b != null) {
                this.f2018a.f1116b.m1289d();
            }
            if (this.f2018a.f1115a.f2016d != null) {
                this.f2018a.f1115a.f2016d.cancel();
            }
        }
    }

    public void m2127b() {
        C0247c.m1070a("tts").m1066a("tts.onresume");
        if (this.f2018a.f1118d != null) {
            this.f2018a.f1118d.onPlayResumed();
        }
    }

    public void m2128c() {
        C0247c.m1070a("tts").m1066a("tts.onstop");
        if (this.f2018a.f1118d != null) {
            this.f2018a.f1118d.onEnd(null);
        }
        C0247c.m1071a().m1079a(this.f2018a.f1115a.f2014a, true);
    }
}
