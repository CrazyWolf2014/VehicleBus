package com.iflytek.speech.p014a;

import android.content.Context;
import android.text.TextUtils;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.p005a.C0247c;
import com.iflytek.p006b.C0252a;
import com.iflytek.p006b.C0253b;
import com.iflytek.record.C0287a;
import com.iflytek.record.PcmPlayer;
import com.iflytek.record.PcmPlayer.C0284a;
import com.iflytek.record.PcmPlayer.PLAY_STATE;
import com.iflytek.speech.C0296f;
import com.iflytek.speech.C1078c;
import com.iflytek.speech.SpeechConfig;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.speech.a.c */
public class C1073c extends SynthesizerPlayer {
    private static C1073c f2013b;
    private Context f2014a;
    private C0293a f2015c;
    private C1078c f2016d;

    /* renamed from: com.iflytek.speech.a.c.a */
    public class C0293a {
        final /* synthetic */ C1073c f1115a;
        private PcmPlayer f1116b;
        private C0287a f1117c;
        private SynthesizerPlayerListener f1118d;
        private int f1119e;
        private boolean f1120f;
        private C0296f f1121g;
        private C0284a f1122h;

        public C0293a(C1073c c1073c) {
            this.f1115a = c1073c;
            this.f1116b = null;
            this.f1117c = null;
            this.f1118d = null;
            this.f1119e = 0;
            this.f1120f = false;
            this.f1121g = new C1074d(this);
            this.f1122h = new C1075e(this);
        }

        public PLAY_STATE m1336a() {
            return (this.f1117c == null || this.f1116b == null) ? PLAY_STATE.STOPED : this.f1116b.m1285a();
        }

        public void m1337a(String str, String str2, SynthesizerPlayerListener synthesizerPlayerListener) {
            C0247c.m1071a().m1080a("tts", str2);
            if (!TextUtils.isEmpty(str)) {
                C0247c.m1070a("tts").m1067a("tts.len", XmlPullParser.NO_NAMESPACE + str.length());
            }
            this.f1118d = synthesizerPlayerListener;
            C0252a c0252a = new C0252a(str2, C0253b.f964a);
            this.f1116b = new PcmPlayer(this.f1115a.f2014a, c0252a.m1117a("stream_type", 3), c0252a.m1125a("request_audio_focus", true));
            this.f1117c = new C0287a(this.f1115a.f2014a, SpeechConfig.m1320c(), false, c0252a.m1118a("tap"));
            this.f1117c.m1294a(str);
            this.f1119e = c0252a.m1117a("tbt", 0);
            this.f1120f = false;
            this.f1115a.f2016d.m2151a(str, str2, this.f1121g);
        }

        public void m1338a(boolean z) {
            if (m1341d()) {
                if (this.f1118d != null && z) {
                    this.f1118d.onEnd(new SpeechError(27, SyncHttpTransportSE.DEFAULTTIMEOUT));
                }
                m1343f();
            }
        }

        public void m1339b() {
            if (this.f1117c != null && this.f1116b != null) {
                C0247c.m1070a("tts").m1069b("tts.pause");
                this.f1116b.m1287b();
            }
        }

        public void m1340c() {
            C0276e.m1220a("Session replay");
            if (this.f1117c != null && this.f1116b != null) {
                C0247c.m1070a("tts").m1069b("tts.replay");
                this.f1116b.m1286a(this.f1117c, this.f1122h);
            }
        }

        public boolean m1341d() {
            return ((m1336a() == PLAY_STATE.STOPED || m1336a() == PLAY_STATE.INIT) && this.f1115a.f2016d.isAvaible()) ? false : true;
        }

        public void m1342e() {
            if (this.f1117c != null && this.f1116b != null) {
                C0247c.m1070a("tts").m1069b("tts.resume");
                this.f1116b.m1288c();
            }
        }

        public void m1343f() {
            this.f1118d = null;
            if (!(this.f1116b == null || this.f1115a.f2016d == null)) {
                C0247c.m1070a("tts").m1069b("tts.cancel");
                if (this.f1115a.f2016d.isAvaible()) {
                    C0247c.m1071a().m1081a("tts", true);
                }
            }
            if (this.f1115a.f2016d != null) {
                this.f1115a.f2016d.cancel();
            }
            if (this.f1116b != null) {
                this.f1116b.m1289d();
            }
            if (this.f1117c != null) {
                this.f1117c.m1304g();
            }
        }
    }

    static {
        f2013b = null;
    }

    private C1073c(Context context, String str) {
        this.f2014a = null;
        this.f2015c = null;
        this.f2016d = null;
        this.f2014a = context.getApplicationContext();
        this.f2016d = C1078c.m2145a(this.f2014a, str);
    }

    public static SynthesizerPlayer m2117a(Context context, String str) {
        if (f2013b == null) {
            f2013b = new C1073c(context, str);
        }
        return f2013b;
    }

    public static C1073c m2118a() {
        return f2013b;
    }

    public void cancel() {
        if (this.f2015c != null) {
            this.f2015c.m1343f();
        }
    }

    public boolean destory() {
        cancel();
        boolean destory = this.f2016d.destory();
        if (destory) {
            f2013b = null;
        }
        return destory;
    }

    public boolean destory(int i) {
        cancel();
        boolean destory = this.f2016d.destory(i);
        if (destory) {
            f2013b = null;
        }
        return destory;
    }

    public int getDownflowBytes(boolean z) {
        return this.f2016d.m2147a(z);
    }

    public PLAY_STATE getState() {
        return this.f2015c != null ? this.f2015c.m1336a() : PLAY_STATE.STOPED;
    }

    public int getUpflowBytes(boolean z) {
        return this.f2016d.m2152b(z);
    }

    public boolean isAvaible() {
        return this.f2015c == null || !this.f2015c.m1341d();
    }

    public void pause() {
        if (this.f2015c != null) {
            this.f2015c.m1339b();
        }
    }

    public void playText(String str, String str2, SynthesizerPlayerListener synthesizerPlayerListener) {
        if (this.f2015c != null && this.f2015c.m1341d()) {
            this.f2015c.m1338a(new C0252a(str2, (String[][]) null).m1125a("tts_interrupt_error", false));
        }
        this.f2015c = new C0293a(this);
        this.f2015c.m1337a(str, str2, synthesizerPlayerListener);
    }

    public void replay() {
        C0276e.m1220a("Player replay");
        if (this.f2015c != null) {
            this.f2015c.m1340c();
        }
    }

    public void resume() {
        if (this.f2015c != null) {
            this.f2015c.m1342e();
        }
    }

    public void setBackgroundSound(String str) {
        this.f2016d.m2156c(str);
    }

    public void setPitch(int i) {
        this.f2016d.m2149a(i);
    }

    public void setSampleRate(RATE rate) {
        this.f2016d.m2150a(rate);
    }

    public void setSpeed(int i) {
        this.f2016d.m2153b(i);
    }

    public void setVoiceName(String str) {
        this.f2016d.m2154b(str);
    }

    public void setVolume(int i) {
        this.f2016d.m2155c(i);
    }
}
