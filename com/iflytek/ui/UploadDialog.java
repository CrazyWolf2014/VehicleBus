package com.iflytek.ui;

import android.content.Context;
import com.iflytek.speech.SpeechListener;
import com.iflytek.ui.p015a.C0303j;

public class UploadDialog extends C0303j {
    public UploadDialog(Context context) {
        super(context);
        this.a = new C1092i(context);
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void setContent(String str, byte[] bArr, String str2) {
        ((C1092i) this.a).m2286a(str, bArr, str2);
    }

    public void setListener(SpeechListener speechListener) {
        ((C1092i) this.a).m2285a(speechListener);
    }

    public void showErrorView(boolean z, boolean z2) {
        this.a.m1374a(z, z2);
    }
}
