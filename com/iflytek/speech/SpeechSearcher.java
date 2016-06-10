package com.iflytek.speech;

import android.content.Context;
import com.iflytek.msc.p008a.C0262c;
import com.iflytek.msc.p008a.C0262c.C1068a;
import com.iflytek.msc.p011d.C1070b;
import com.iflytek.msc.p013f.C0276e;

public class SpeechSearcher extends C0262c {
    public SpeechSearcher() {
        super(null, null);
    }

    protected boolean m2106c() {
        C0276e.m1220a(m1169b() + " destory success");
        return true;
    }

    public void searchText(Context context, SpeechListener speechListener, String str, String str2) {
        this.d = new C1070b(context);
        ((C1070b) this.d).m2096a(new C1068a(this, speechListener), str, str2);
    }
}
