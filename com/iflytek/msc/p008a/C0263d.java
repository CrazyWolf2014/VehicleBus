package com.iflytek.msc.p008a;

import com.iflytek.speech.SpeechError;
import java.io.IOException;

/* renamed from: com.iflytek.msc.a.d */
final class C0263d extends Thread {
    final /* synthetic */ C0262c f1006a;

    C0263d(C0262c c0262c) {
        this.f1006a = c0262c;
    }

    public void run() {
        try {
            this.f1006a.m1167a();
        } catch (SpeechError e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }
}
