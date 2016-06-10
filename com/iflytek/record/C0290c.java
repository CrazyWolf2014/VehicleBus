package com.iflytek.record;

import android.media.AudioRecord;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.speech.SpeechConfig;
import com.iflytek.speech.SpeechError;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;

/* renamed from: com.iflytek.record.c */
public class C0290c extends Thread {
    private final short f1088a;
    private byte[] f1089b;
    private AudioRecord f1090c;
    private C0289a f1091d;
    private boolean f1092e;
    private int f1093f;
    private int f1094g;
    private int f1095h;

    /* renamed from: com.iflytek.record.c.a */
    public interface C0289a {
        void m1305a(SpeechError speechError);

        void m1306a(byte[] bArr, int i, int i2);
    }

    public C0290c(int i, int i2) {
        this.f1088a = (short) 16;
        this.f1089b = null;
        this.f1090c = null;
        this.f1091d = null;
        this.f1092e = false;
        this.f1093f = SpeechConfig.Rate16K;
        this.f1094g = 40;
        this.f1095h = 40;
        this.f1093f = i;
        this.f1094g = i2;
        if (this.f1094g < 40 || this.f1094g > 100) {
            this.f1094g = 40;
        }
        this.f1095h = 10;
    }

    private int m1307b() throws SpeechError {
        if (this.f1090c == null || this.f1091d == null) {
            return 0;
        }
        int read = this.f1090c.read(this.f1089b, 0, this.f1089b.length);
        if (read <= 0 || this.f1091d == null) {
            return read;
        }
        this.f1091d.m1306a(this.f1089b, 0, read);
        return read;
    }

    private void m1308c() {
        if (this.f1090c != null) {
            C0276e.m1220a("release record begin");
            this.f1090c.release();
            this.f1090c = null;
            C0276e.m1220a("release record over");
        }
    }

    public void m1309a() {
        this.f1092e = true;
        this.f1091d = null;
    }

    public void m1310a(C0289a c0289a) throws SpeechError {
        this.f1091d = c0289a;
        setPriority(10);
        start();
    }

    public void m1311a(short s, int i, int i2) throws SpeechError {
        if (this.f1090c != null) {
            m1308c();
        }
        int i3 = (i * i2) / 1000;
        int i4 = (((i3 * 4) * 16) * s) / 8;
        int i5 = s == (short) 1 ? 2 : 3;
        int minBufferSize = AudioRecord.getMinBufferSize(i, i5, 2);
        if (i4 < minBufferSize) {
            i4 = minBufferSize;
        }
        this.f1090c = new AudioRecord(1, i, i5, 2, i4);
        this.f1089b = new byte[(((i3 * s) * 16) / 8)];
        C0276e.m1220a("\nSampleRate:" + i + "\nChannel:" + i5 + "\nFormat:" + 2 + "\nFramePeriod:" + i3 + "\nBufferSize:" + i4 + "\nMinBufferSize:" + minBufferSize + "\nActualBufferSize:" + this.f1089b.length + SpecilApiUtil.LINE_SEP);
        if (this.f1090c.getState() != 1) {
            C0276e.m1220a("create AudioRecord error");
            throw new SpeechError(9, SyncHttpTransportSE.DEFAULTTIMEOUT);
        }
    }

    protected void finalize() throws Throwable {
        m1308c();
        super.finalize();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r9 = this;
        r8 = 4;
        r7 = 3;
        r0 = 0;
        r6 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r5 = 9;
        r1 = r0;
    L_0x0008:
        r2 = r9.f1092e;	 Catch:{ Exception -> 0x003c }
        if (r2 != 0) goto L_0x0014;
    L_0x000c:
        r2 = 1;
        r3 = r9.f1093f;	 Catch:{ Exception -> 0x003c }
        r4 = r9.f1094g;	 Catch:{ Exception -> 0x003c }
        r9.m1311a(r2, r3, r4);	 Catch:{ Exception -> 0x003c }
    L_0x0014:
        r1 = r9.f1090c;	 Catch:{ Exception -> 0x0026 }
        r1 = r1.getState();	 Catch:{ Exception -> 0x0026 }
        if (r1 != r7) goto L_0x0051;
    L_0x001c:
        r0 = new com.iflytek.speech.SpeechError;	 Catch:{ Exception -> 0x0026 }
        r1 = 9;
        r2 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x0026 }
        throw r0;	 Catch:{ Exception -> 0x0026 }
    L_0x0026:
        r0 = move-exception;
        r0.printStackTrace();
        r0 = r9.f1091d;
        if (r0 == 0) goto L_0x0038;
    L_0x002e:
        r0 = r9.f1091d;
        r1 = new com.iflytek.speech.SpeechError;
        r1.<init>(r5, r6);
        r0.m1305a(r1);
    L_0x0038:
        r9.m1308c();
        return;
    L_0x003c:
        r2 = move-exception;
        r1 = r1 + 1;
        if (r1 >= r8) goto L_0x0047;
    L_0x0041:
        r2 = 40;
        com.iflytek.record.C0290c.sleep(r2);	 Catch:{ Exception -> 0x0026 }
        goto L_0x0008;
    L_0x0047:
        r0 = new com.iflytek.speech.SpeechError;	 Catch:{ Exception -> 0x0026 }
        r1 = 9;
        r2 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x0026 }
        throw r0;	 Catch:{ Exception -> 0x0026 }
    L_0x0051:
        r1 = r9.f1092e;	 Catch:{ Exception -> 0x0026 }
        if (r1 != 0) goto L_0x005a;
    L_0x0055:
        r1 = r9.f1090c;	 Catch:{ Exception -> 0x0026 }
        r1.startRecording();	 Catch:{ Exception -> 0x0026 }
    L_0x005a:
        r1 = r9.f1092e;	 Catch:{ Exception -> 0x0026 }
        if (r1 != 0) goto L_0x0038;
    L_0x005e:
        r9.m1307b();	 Catch:{ Exception -> 0x0026 }
        r1 = r9.f1090c;	 Catch:{ Exception -> 0x0026 }
        r1 = r1.getRecordingState();	 Catch:{ Exception -> 0x0026 }
        if (r1 == r7) goto L_0x0077;
    L_0x0069:
        r0 = r0 + 1;
        if (r0 <= r8) goto L_0x0077;
    L_0x006d:
        r0 = new com.iflytek.speech.SpeechError;	 Catch:{ Exception -> 0x0026 }
        r1 = 9;
        r2 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x0026 }
        throw r0;	 Catch:{ Exception -> 0x0026 }
    L_0x0077:
        r1 = r9.f1095h;	 Catch:{ Exception -> 0x0026 }
        r1 = (long) r1;	 Catch:{ Exception -> 0x0026 }
        com.iflytek.record.C0290c.sleep(r1);	 Catch:{ Exception -> 0x0026 }
        goto L_0x005a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.iflytek.record.c.run():void");
    }
}
