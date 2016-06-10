package com.iflytek.ui;

import android.content.Context;
import com.iflytek.record.PcmPlayer.PLAY_STATE;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.ui.p015a.C0303j;

public class SynthesizerDialog extends C0303j {
    public SynthesizerDialog(Context context, String str) {
        super(context);
        this.a = new C1090f(context, str);
    }

    public int getDownflowBytes(boolean z) {
        return ((C1090f) this.a).m2266b(z);
    }

    public PLAY_STATE getState() {
        return ((C1090f) this.a).m2259a();
    }

    public int getUpflowBytes(boolean z) {
        return ((C1090f) this.a).m2270c(z);
    }

    public void pause() {
        ((C1090f) this.a).m2274e();
    }

    public void resume() {
        ((C1090f) this.a).m2275f();
    }

    public void setBackgroundSound(String str) {
        ((C1090f) this.a).m2269b(str);
    }

    public void setListener(SynthesizerDialogListener synthesizerDialogListener) {
        ((C1090f) this.a).m2262a(synthesizerDialogListener);
    }

    public void setPitch(int i) {
        ((C1090f) this.a).m2260a(i);
    }

    public void setSampleRate(RATE rate) {
        ((C1090f) this.a).m2261a(rate);
    }

    public void setSpeed(int i) {
        ((C1090f) this.a).m2268b(i);
    }

    public void setText(String str, String str2) {
        ((C1090f) this.a).m2264a(str, str2);
    }

    public void setVoiceName(String str) {
        ((C1090f) this.a).m2263a(str);
    }

    public void setVolume(int i) {
        ((C1090f) this.a).m2272c(i);
    }
}
