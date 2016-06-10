package com.iflytek.record;

import android.content.Context;
import android.media.AudioTrack;
import android.os.MemoryFile;
import android.text.TextUtils;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.msc.p013f.C0278g;
import com.iflytek.speech.SpeechConfig;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.record.a */
public class C0287a {
    private int f1075a;
    private ArrayList<C0286a> f1076b;
    private Context f1077c;
    private boolean f1078d;
    private int f1079e;
    private volatile int f1080f;
    private MemoryFile f1081g;
    private volatile long f1082h;
    private volatile int f1083i;
    private C0286a f1084j;
    private String f1085k;
    private String f1086l;

    /* renamed from: com.iflytek.record.a.a */
    public class C0286a {
        long f1070a;
        long f1071b;
        int f1072c;
        int f1073d;
        final /* synthetic */ C0287a f1074e;

        public C0286a(C0287a c0287a, long j, long j2, int i, int i2) {
            this.f1074e = c0287a;
            this.f1070a = j;
            this.f1071b = j2;
            this.f1072c = i;
            this.f1073d = i2;
        }
    }

    public C0287a(Context context, int i, boolean z, String str) {
        this.f1075a = 3145728;
        this.f1076b = null;
        this.f1077c = null;
        this.f1078d = false;
        this.f1079e = SpeechConfig.Rate16K;
        this.f1080f = 0;
        this.f1081g = null;
        this.f1082h = 0;
        this.f1083i = 0;
        this.f1084j = null;
        this.f1085k = XmlPullParser.NO_NAMESPACE;
        this.f1086l = null;
        this.f1077c = context;
        this.f1078d = z;
        this.f1080f = 0;
        this.f1076b = new ArrayList();
        this.f1082h = 0;
        this.f1079e = i;
        this.f1086l = str;
    }

    private void m1290a(byte[] bArr) throws IOException {
        if (bArr != null && bArr.length != 0) {
            if (this.f1081g == null) {
                this.f1085k = m1291h();
                this.f1081g = new MemoryFile(this.f1085k, this.f1075a);
                this.f1081g.allowPurging(false);
            }
            this.f1081g.writeBytes(bArr, 0, (int) this.f1082h, bArr.length);
            this.f1082h += (long) bArr.length;
        }
    }

    private String m1291h() {
        String a = C0278g.m1229a(this.f1077c);
        return this.f1078d ? a + "record.pcm" : a + "tts.pcm";
    }

    public int m1292a() {
        return this.f1079e;
    }

    public void m1293a(AudioTrack audioTrack, int i) throws IOException {
        int i2 = this.f1082h - ((long) this.f1083i) < ((long) i) ? (int) (this.f1082h - ((long) this.f1083i)) : i;
        byte[] bArr = new byte[i2];
        this.f1081g.readBytes(bArr, this.f1083i, 0, i2);
        this.f1083i += i2;
        audioTrack.write(bArr, 0, i2);
        if (i2 < i) {
            m1299b(audioTrack, i);
        }
    }

    public void m1294a(String str) {
        int i = 307200;
        if (!TextUtils.isEmpty(str)) {
            this.f1075a = (((str.length() / 5) * 4) * 32) * Flags.FLAG5;
            if (this.f1075a > 307200) {
                i = this.f1075a;
            }
            this.f1075a = i;
        }
    }

    public void m1295a(ArrayList<byte[]> arrayList, int i, int i2, int i3) throws IOException {
        C0276e.m1220a("buffer percent = " + i + " beg=" + i2 + " end=" + i3);
        C0286a c0286a = new C0286a(this, this.f1082h, this.f1082h, i2, i3);
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            m1290a((byte[]) arrayList.get(i4));
        }
        c0286a.f1071b = this.f1082h;
        this.f1080f = i;
        this.f1076b.add(c0286a);
        if (i == 100 && !this.f1078d) {
            C0278g.m1232a(this.f1081g, this.f1082h, this.f1086l);
        }
        C0276e.m1220a("allSize = " + this.f1082h + " maxSize=" + this.f1075a);
    }

    public void m1296a(ConcurrentLinkedQueue<byte[]> concurrentLinkedQueue) throws IOException {
        if (concurrentLinkedQueue != null) {
            Iterator it = concurrentLinkedQueue.iterator();
            while (it.hasNext()) {
                m1290a((byte[]) it.next());
            }
        }
    }

    public boolean m1297a(int i) {
        return this.f1080f > 95 || ((int) (this.f1082h / 32768)) >= i / 1000;
    }

    public void m1298b() throws IOException {
        this.f1083i = 0;
        this.f1084j = null;
        if (this.f1076b.size() > 0) {
            this.f1084j = (C0286a) this.f1076b.get(0);
        }
    }

    public void m1299b(AudioTrack audioTrack, int i) {
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 < i; i2++) {
            bArr[i2] = (byte) 0;
        }
        audioTrack.write(bArr, 0, i);
    }

    public int m1300c() {
        return this.f1082h <= 0 ? 0 : (int) (((long) (this.f1083i * this.f1080f)) / this.f1082h);
    }

    public C0286a m1301d() {
        if (this.f1084j != null) {
            if (((long) this.f1083i) >= this.f1084j.f1070a && ((long) this.f1083i) <= this.f1084j.f1071b) {
                return this.f1084j;
            }
            Iterator it = this.f1076b.iterator();
            while (it.hasNext()) {
                this.f1084j = (C0286a) it.next();
                if (((long) this.f1083i) >= this.f1084j.f1070a && ((long) this.f1083i) <= this.f1084j.f1071b) {
                    return this.f1084j;
                }
            }
        }
        return null;
    }

    public boolean m1302e() {
        return this.f1078d ? true : 100 == this.f1080f && ((long) this.f1083i) >= this.f1082h;
    }

    public boolean m1303f() throws IOException {
        return ((long) this.f1083i) < this.f1082h;
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void m1304g() {
        try {
            if (this.f1081g != null) {
                this.f1081g.close();
                this.f1081g = null;
            }
            File file = new File(this.f1085k);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
