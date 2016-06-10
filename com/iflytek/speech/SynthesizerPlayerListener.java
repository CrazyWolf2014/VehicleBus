package com.iflytek.speech;

public interface SynthesizerPlayerListener {
    void onBufferPercent(int i, int i2, int i3);

    void onEnd(SpeechError speechError);

    void onPlayBegin();

    void onPlayPaused();

    void onPlayPercent(int i, int i2, int i3);

    void onPlayResumed();
}
