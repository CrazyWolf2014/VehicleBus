package com.iflytek.ui;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import java.util.ArrayList;

public interface RecognizerDialogListener {
    void onEnd(SpeechError speechError);

    void onResults(ArrayList<RecognizerResult> arrayList, boolean z);
}
