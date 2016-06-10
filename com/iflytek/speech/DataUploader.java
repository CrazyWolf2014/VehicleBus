package com.iflytek.speech;

import android.content.Context;
import com.iflytek.msc.p008a.C0262c;
import com.iflytek.msc.p008a.C0262c.C1068a;
import com.iflytek.msc.p011d.C1070b;

public class DataUploader extends C0262c {
    public DataUploader() {
        super(null, null);
    }

    protected boolean m2105c() {
        return true;
    }

    public void uploadData(Context context, SpeechListener speechListener, String str, String str2, byte[] bArr) {
        this.d = new C1070b(context);
        ((C1070b) this.d).m2098a(new C1068a(this, speechListener), str, str2, bArr);
    }
}
