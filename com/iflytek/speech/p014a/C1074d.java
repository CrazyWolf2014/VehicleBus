package com.iflytek.speech.p014a;

import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.iflytek.p005a.C0247c;
import com.iflytek.record.PcmPlayer.PLAY_STATE;
import com.iflytek.speech.C0296f;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.p014a.C1073c.C0293a;
import java.io.IOException;
import java.util.ArrayList;

/* renamed from: com.iflytek.speech.a.d */
final class C1074d implements C0296f {
    final /* synthetic */ C0293a f2017a;

    C1074d(C0293a c0293a) {
        this.f2017a = c0293a;
    }

    public void m2120a() {
    }

    public void m2121a(SpeechError speechError) {
        if (this.f2017a.f1118d != null && speechError != null) {
            this.f2017a.f1118d.onEnd(speechError);
            this.f2017a.f1116b.m1289d();
        }
    }

    public void m2122a(ArrayList<byte[]> arrayList, int i, int i2, int i3) {
        if (this.f2017a.f1118d != null) {
            this.f2017a.f1118d.onBufferPercent(i, i2, i3);
        }
        C0247c.m1070a("tts").m1066a("rsp");
        try {
            this.f2017a.f1117c.m1295a(arrayList, i, i2, i3);
            if (!this.f2017a.f1120f && this.f2017a.f1116b.m1285a() == PLAY_STATE.INIT && this.f2017a.f1117c.m1297a(this.f2017a.f1119e)) {
                this.f2017a.f1120f = true;
                this.f2017a.f1116b.m1286a(this.f2017a.f1117c, this.f2017a.f1122h);
                if (this.f2017a.f1118d != null) {
                    this.f2017a.f1118d.onPlayBegin();
                }
                C0247c.m1070a("tts").m1066a("tts.onplay");
            }
        } catch (IOException e) {
            e.printStackTrace();
            m2121a(new SpeechError(14, SyncHttpTransportSE.DEFAULTTIMEOUT));
            this.f2017a.m1343f();
        }
    }

    public void m2123b() {
    }
}
