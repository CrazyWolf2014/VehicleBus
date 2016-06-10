package com.iflytek.speech;

import android.content.Context;
import com.iflytek.record.PcmPlayer.PLAY_STATE;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.p014a.C1073c;

public abstract class SynthesizerPlayer {
    public static SynthesizerPlayer createSynthesizerPlayer(Context context, String str) {
        return C1073c.m2117a(context, str);
    }

    public static SynthesizerPlayer getSynthesizerPlayer() {
        return C1073c.m2118a();
    }

    public abstract void cancel();

    public abstract boolean destory();

    public abstract boolean destory(int i);

    public int getDownflowBytes(boolean z) {
        return 0;
    }

    public abstract PLAY_STATE getState();

    public int getUpflowBytes(boolean z) {
        return 0;
    }

    public abstract boolean isAvaible();

    public abstract void pause();

    public abstract void playText(String str, String str2, SynthesizerPlayerListener synthesizerPlayerListener);

    public abstract void replay();

    public abstract void resume();

    public void setBackgroundSound(String str) {
    }

    public void setPitch(int i) {
    }

    public void setSampleRate(RATE rate) {
    }

    public void setSpeed(int i) {
    }

    public void setVoiceName(String str) {
    }

    public void setVolume(int i) {
    }
}
