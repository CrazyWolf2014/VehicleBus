package com.iflytek.ui;

import com.iflytek.speech.SpeechError;

public interface SynthesizerDialogListener {
    void onEnd(SpeechError speechError);
}
