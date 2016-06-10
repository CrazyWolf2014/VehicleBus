package com.iflytek.ui;

import android.content.Context;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.ui.p015a.C0303j;

public class RecognizerDialog extends C0303j {
    public RecognizerDialog(Context context, String str) {
        super(context);
        this.a = new C1087a(context, str);
    }

    public int getDownflowBytes(boolean z) {
        return ((C1087a) this.a).m2225b(z);
    }

    public int getUpflowBytes(boolean z) {
        return ((C1087a) this.a).m2227c(z);
    }

    public void setEngine(String str, String str2, String str3) {
        ((C1087a) this.a).m2223a(str, str2, str3);
    }

    public void setListener(RecognizerDialogListener recognizerDialogListener) {
        ((C1087a) this.a).m2222a(recognizerDialogListener);
    }

    public void setRecordInterval(int i) {
        ((C1087a) this.a).m2220a(i);
    }

    public void setSampleRate(RATE rate) {
        ((C1087a) this.a).m2221a(rate);
    }

    public void showErrorView(boolean z, boolean z2) {
        this.a.m1374a(z, z2);
    }

    public void showMoreButton(boolean z) {
        ((C1087a) this.a).m2224a(z);
    }
}
