package com.iflytek.msc.p009b;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.iflytek.msc.p008a.C0260a;
import com.iflytek.msc.p008a.C0260a.C0259a;
import com.iflytek.msc.p009b.C0266a.C0265a;
import com.iflytek.msc.p013f.C0275d;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.msc.p013f.C0281i;
import com.iflytek.msc.p013f.C0282j;
import com.iflytek.msc.p013f.C0283k;
import com.iflytek.p005a.C0247c;
import com.iflytek.p005a.C0250g;
import com.iflytek.p006b.C0254c;
import com.iflytek.record.C0290c;
import com.iflytek.record.C0290c.C0289a;
import com.iflytek.speech.C0294a;
import com.iflytek.speech.C1076b;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig;
import com.iflytek.speech.SpeechError;
import com.ifoer.mine.Contact;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.msc.b.c */
public class C1069c extends C0260a implements C0289a {
    public static int f1974q;
    public static int f1975r;
    protected volatile C0294a f1976j;
    protected long f1977k;
    protected long f1978l;
    protected boolean f1979m;
    protected boolean f1980n;
    protected C0266a f1981o;
    protected C0290c f1982p;
    protected String f1983s;
    protected String f1984t;
    protected ConcurrentLinkedQueue<byte[]> f1985u;
    protected ConcurrentLinkedQueue<byte[]> f1986v;
    protected ArrayList<String> f1987w;
    private long f1988x;

    /* renamed from: com.iflytek.msc.b.c.1 */
    static /* synthetic */ class C02671 {
        static final /* synthetic */ int[] f1018a;

        static {
            f1018a = new int[C0265a.values().length];
            try {
                f1018a[C0265a.noResult.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1018a[C0265a.hasResult.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1018a[C0265a.resultOver.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    static {
        f1974q = 0;
        f1975r = 0;
    }

    public C1069c(Context context) {
        super(context);
        this.f1976j = null;
        this.f1977k = 0;
        this.f1978l = 0;
        this.f1979m = false;
        this.f1988x = 0;
        this.f1980n = true;
        this.f1981o = new C0266a();
        this.f1982p = null;
        this.f1983s = null;
        this.f1984t = null;
        this.f1985u = null;
        this.f1986v = null;
        this.f1987w = null;
        this.f1985u = new ConcurrentLinkedQueue();
        this.f1986v = new ConcurrentLinkedQueue();
        this.f1987w = new ArrayList();
        this.f1979m = false;
    }

    public C1069c(ConcurrentLinkedQueue<byte[]> concurrentLinkedQueue, Context context) {
        super(context);
        this.f1976j = null;
        this.f1977k = 0;
        this.f1978l = 0;
        this.f1979m = false;
        this.f1988x = 0;
        this.f1980n = true;
        this.f1981o = new C0266a();
        this.f1982p = null;
        this.f1983s = null;
        this.f1984t = null;
        this.f1985u = null;
        this.f1986v = null;
        this.f1987w = null;
        this.f1986v = new ConcurrentLinkedQueue();
        this.f1985u = concurrentLinkedQueue;
        this.f1987w = new ArrayList();
        this.f1979m = false;
    }

    private void m2069d(boolean z) throws SpeechError, UnsupportedEncodingException {
        C0283k.m1266a("QISRGetResult", null);
        C0247c.m1070a("asr").m1066a("rsp");
        this.g = SystemClock.elapsedRealtime();
        if (this.f1981o.m1186f() != null && this.f1981o.m1186f().length > 0) {
            this.f1987w.add(new String(this.f1981o.m1186f(), "utf-8"));
        }
        if (m2071u()) {
            m2090o();
        } else {
            m2081b(z);
        }
    }

    private void m2070t() throws SpeechError, UnsupportedEncodingException {
        switch (C02671.f1018a[this.f1981o.m1187g().ordinal()]) {
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                m2069d(false);
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                m2069d(true);
            default:
        }
    }

    private boolean m2071u() {
        return !TextUtils.isEmpty(this.f1984t) || m1160e().m1127b("grt");
    }

    private void m2072v() {
        if (this.f1982p != null) {
            this.f1982p.m1309a();
            this.f1982p = null;
        }
    }

    public void m2073a() {
        if (!(m1158c() == C0259a.exited || m1158c() == C0259a.exiting || m1158c() == C0259a.idle)) {
            C0247c.m1070a("asr").m1069b("asr.cancel");
        }
        if (this.f1982p != null) {
            this.f1982p.m1309a();
        }
        if (m1158c() == C0259a.recording) {
            this.f1979m = true;
        }
        super.m1153a();
    }

    public void m2074a(SpeechError speechError) {
        this.i = speechError;
        m1157b();
    }

    public synchronized void m2075a(String str, String str2, String str3, C0294a c0294a) {
        C0247c.m1071a().m1080a("asr", str2);
        this.f1976j = c0294a;
        if (TextUtils.isEmpty(str) && TextUtils.isEmpty(str3)) {
            this.f1983s = "sms";
        } else {
            this.f1983s = str;
        }
        m1156a(str2);
        this.f1984t = str3;
        C0276e.m1220a("startListening called");
        m1161f();
    }

    public void m2076a(byte[] bArr) {
        if (bArr != null && bArr.length != 0) {
            this.f1985u.add(bArr);
        }
    }

    public void m2077a(byte[] bArr, int i) {
        if (this.f1976j != null && this.f != C0259a.exiting) {
            this.f1976j.m1345a(i);
        }
    }

    public void m2078a(byte[] bArr, int i, int i2) {
        if (i2 > 0 && this.f != C0259a.exiting) {
            byte[] bArr2 = new byte[i2];
            System.arraycopy(bArr, i, bArr2, 0, i2);
            m2076a(bArr2);
        }
    }

    protected void m2079a(byte[] bArr, boolean z) throws SpeechError {
        C0283k.m1266a("QISRAudioWrite", XmlPullParser.NO_NAMESPACE + bArr.length);
        this.f1981o.m1178a(bArr, bArr.length);
        if (z) {
            int c = this.f1981o.m1182c();
            if (c == 1) {
                C0247c.m1070a("asr").m1066a("asr.bos");
            }
            if (c == 3) {
                C0247c.m1070a("asr").m1066a("asr.eos");
                m2088m();
                return;
            }
            c = this.f1981o.m1184d();
            C0276e.m1220a("QISRAudioWrite volume:" + c);
            m2077a(bArr, c);
        }
    }

    public synchronized boolean m2080a(boolean z) {
        boolean z2;
        if (this.f != C0259a.recording) {
            C0276e.m1220a("stopRecognize fail  status is :" + this.f);
            z2 = false;
        } else {
            if (this.f1982p != null) {
                this.f1982p.m1309a();
            }
            this.f1979m = z;
            m1155a(C0259a.stoprecord);
            z2 = true;
        }
        return z2;
    }

    public void m2081b(boolean z) throws SpeechError, UnsupportedEncodingException {
        C0276e.m1220a("msc result time:" + System.currentTimeMillis());
        RecognizerResult recognizerResult = new RecognizerResult();
        recognizerResult.text = C0281i.m1261c(this.f1981o.m1186f(), "utf-8");
        C0281i.m1256a(recognizerResult, this.f1981o.m1186f(), "utf-8");
        boolean z2 = false;
        if (z || C0281i.f1049a || recognizerResult.semanteme.size() > 0) {
            z2 = true;
        }
        if (!(this.f1976j == null || this.f == C0259a.exiting)) {
            if (this.b) {
                this.f1976j.m1347a(m2089n(), z2);
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.add(recognizerResult);
                this.f1976j.m1347a(arrayList, z2);
            }
        }
        if (z2) {
            m1157b();
        }
    }

    public boolean m2082c(boolean z) throws SpeechError, InterruptedException {
        if (this.f1985u.size() == 0) {
            return false;
        }
        byte[] bArr = (byte[]) this.f1985u.poll();
        this.f1986v.add(bArr);
        m2079a(bArr, z);
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.f1988x < 10 && this.f1985u.size() > 0) {
            Thread.sleep(10);
        }
        this.f1988x = currentTimeMillis;
        return true;
    }

    protected void m2083g() throws Exception {
        if (this.f == C0259a.init) {
            m2091p();
        } else if (this.f == C0259a.start) {
            m2086k();
        } else if (this.f == C0259a.recording) {
            m2092q();
        } else if (this.f == C0259a.stoprecord) {
            m2093r();
        } else if (this.f == C0259a.waitresult) {
            m2094s();
        }
        super.m1162g();
    }

    protected void m2084h() {
        C0276e.m1220a("onSessionEnd");
        m2072v();
        f1974q = this.f1981o.m1180b("upflow");
        f1975r = this.f1981o.m1180b("downflow");
        C0247c.m1070a("asr").m1067a("lgid", this.f1981o.m1183c("loginid"));
        this.c = this.f1981o.m1183c(AlixDefine.SID);
        C0247c.m1070a("asr").m1067a(AlixDefine.SID, this.c);
        if (this.f1987w.size() <= 0 && this.i == null && m1160e().m1125a("asr_nme", false)) {
            this.i = new SpeechError(11, 10118);
        }
        C0283k.m1266a("QISRSessionEnd", null);
        this.f1981o.m1179a("rec_start", C0275d.m1218a(this.f1978l));
        this.f1981o.m1179a("rec_ustop", this.f1979m ? Contact.RELATION_FRIEND : Contact.RELATION_ASK);
        if (this.e) {
            this.f1981o.m1177a("user abort");
        } else if (this.i != null) {
            this.f1981o.m1177a("error" + this.i.getErrorCode());
        } else {
            this.f1981o.m1177a("success");
        }
        if (this.i != null) {
            C0247c.m1070a("asr").m1063a(this.i.getErrorCode());
        }
        super.m1163h();
        C0247c.m1070a("asr").m1066a("end");
        if (this.f1976j != null) {
            if (this.e) {
                C0276e.m1220a("RecognizerListener#onCancel");
                this.f1976j.m1349c();
            } else {
                C0276e.m1220a("RecognizerListener#onEnd");
                this.f1976j.m1346a(this.i);
            }
        }
        C0247c.m1071a().m1079a(this.d, this.i == null);
        C0250g.m1098a().m1105a(this.d);
    }

    protected void m2085i() {
        this.f1980n = C0254c.m1138a(this.f1983s);
        if (this.f1980n) {
            this.a = SyncHttpTransportSE.DEFAULTTIMEOUT;
        } else {
            this.a = BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
        }
        this.a = m1160e().m1117a("speech_timeout", this.a);
        C0276e.m1220a("mSpeechTimeOut=" + this.a);
        super.m1164i();
    }

    void m2086k() throws SpeechError, IOException, InterruptedException {
        C0276e.m1220a("start  record");
        this.f1982p = new C0290c(SpeechConfig.m1312a(), SpeechConfig.m1316b());
        if (this.f != C0259a.exiting) {
            this.f1982p.m1310a(this);
        }
        this.f1977k = SystemClock.elapsedRealtime();
        this.f1978l = System.currentTimeMillis();
        C0283k.m1266a("QISRSessionBegin", null);
        int a = this.f1981o.m1176a(this.d, this.f1983s, m1160e(), this.f1984t, this.f1980n);
        while (a == 10129 && this.f == C0259a.start) {
            if (System.currentTimeMillis() - this.f1978l < 800) {
                Thread.sleep(10);
                a = this.f1981o.m1176a(this.d, this.f1983s, m1160e(), this.f1984t, this.f1980n);
            } else {
                throw new SpeechError(SyncHttpTransportSE.DEFAULTTIMEOUT, 10129);
            }
        }
        m1155a(C0259a.recording);
        if (this.f != C0259a.exiting && this.f1976j != null) {
            this.f1976j.m1344a();
        }
    }

    public ConcurrentLinkedQueue<byte[]> m2087l() {
        while (true) {
            byte[] bArr = (byte[]) this.f1985u.poll();
            if (bArr == null) {
                return this.f1986v;
            }
            this.f1986v.add(bArr);
        }
    }

    public void m2088m() {
        if (C0259a.recording == this.f) {
            m2080a(false);
            if (this.f1976j != null) {
                this.f1976j.m1348b();
            }
        }
    }

    public ArrayList<RecognizerResult> m2089n() {
        ArrayList<RecognizerResult> arrayList = new ArrayList();
        try {
            RecognizerResult recognizerResult = new RecognizerResult();
            if (this.f1981o.m1186f() != null) {
                recognizerResult.text = new String(this.f1981o.m1186f(), "utf-8");
                arrayList.add(recognizerResult);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public void m2090o() throws SpeechError, UnsupportedEncodingException {
        if (!(this.f1976j == null || this.f == C0259a.exiting)) {
            if (this.b) {
                this.f1976j.m1347a(m2089n(), true);
            } else {
                this.f1976j.m1347a(!C0254c.m1137a(m1160e()) ? C0281i.m1255a(this.f1981o.m1186f(), "utf-8") : C0281i.m1258b(this.f1981o.m1186f(), "utf-8"), true);
            }
        }
        m1157b();
    }

    void m2091p() throws SpeechError, IOException {
        C0276e.m1220a("start connecting");
        if (!C0254c.m1137a(m1160e())) {
            C0282j.m1263a(this.d);
        }
        C0266a.m1171a(this.d, C1076b.m2130d().getInitParam());
        m1155a(C0259a.start);
    }

    void m2092q() throws SpeechError, IOException, InterruptedException {
        if (!m2082c(true)) {
            Thread.sleep(20);
        } else if (this.f1981o.m1185e()) {
            m2070t();
        }
        if (SystemClock.elapsedRealtime() - this.f1977k > ((long) this.a)) {
            m2088m();
        }
    }

    void m2093r() throws SpeechError, IOException, InterruptedException {
        m2072v();
        if (!m2082c(true)) {
            this.f1981o.m1181b();
            m1155a(C0259a.waitresult);
        }
    }

    void m2094s() throws SpeechError, InterruptedException, UnsupportedEncodingException {
        m2072v();
        m2070t();
        if (this.f == C0259a.waitresult) {
            Thread.sleep(10);
        }
        C0260a.m1152a(this.g, this.h);
    }
}
