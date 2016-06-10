package com.iflytek.speech;

import android.content.Context;
import com.iflytek.msc.p008a.C0262c;
import com.iflytek.msc.p008a.C0262c.C1068a;
import com.iflytek.msc.p011d.C1070b;

public class DataDownloader extends C0262c {
    public DataDownloader() {
        super(null, null);
    }

    protected boolean m2104c() {
        return true;
    }

    public void downloadData(Context context, SpeechListener speechListener, String str) {
        this.d = new C1070b(context);
        ((C1070b) this.d).m2095a(new C1068a(this, speechListener), str);
    }
}
