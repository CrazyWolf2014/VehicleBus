package com.iflytek.ui;

import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayerListener;

/* renamed from: com.iflytek.ui.h */
final class C1091h implements SynthesizerPlayerListener {
    final /* synthetic */ C1090f f2094a;

    C1091h(C1090f c1090f) {
        this.f2094a = c1090f;
    }

    public void onBufferPercent(int i, int i2, int i3) {
        this.f2094a.f2088l.setSecondaryProgress(i);
    }

    public void onEnd(SpeechError speechError) {
        if (speechError != null) {
            this.f2094a.m2241a(speechError);
        } else {
            this.f2094a.m2255n();
        }
        if (this.f2094a.f2084h != null) {
            this.f2094a.f2084h.onEnd(speechError);
        }
    }

    public void onPlayBegin() {
        this.f2094a.f2088l.setProgress(0);
        this.f2094a.m2253l();
    }

    public void onPlayPaused() {
        this.f2094a.m2254m();
        this.f2094a.f2089m.setEnabled(false);
    }

    public void onPlayPercent(int i, int i2, int i3) {
        this.f2094a.f2088l.setProgress(i);
    }

    public void onPlayResumed() {
        this.f2094a.m2253l();
    }
}
