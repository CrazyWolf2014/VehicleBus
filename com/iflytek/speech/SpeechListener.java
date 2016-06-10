package com.iflytek.speech;

import android.os.Bundle;

public interface SpeechListener {
    void onData(byte[] bArr);

    void onEnd(SpeechError speechError);

    void onEvent(int i, Bundle bundle);
}
