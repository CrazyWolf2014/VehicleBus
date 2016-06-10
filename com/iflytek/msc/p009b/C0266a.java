package com.iflytek.msc.p009b;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.iflytek.msc.MSC;
import com.iflytek.msc.MSCSessionInfo;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.p006b.C0252a;
import com.iflytek.p006b.C0254c;
import com.iflytek.speech.SpeechError;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import org.xbill.DNS.KEYRecord;

/* renamed from: com.iflytek.msc.b.a */
public class C0266a {
    private static volatile boolean f1012a;
    private static Object f1013b;
    private char[] f1014c;
    private MSCSessionInfo f1015d;
    private MSCSessionInfo f1016e;
    private byte[] f1017f;

    /* renamed from: com.iflytek.msc.b.a.a */
    public enum C0265a {
        noResult,
        hasResult,
        resultOver
    }

    static {
        f1012a = false;
        f1013b = new Object();
    }

    public C0266a() {
        this.f1014c = null;
        this.f1015d = new MSCSessionInfo();
        this.f1016e = new MSCSessionInfo();
        this.f1017f = null;
    }

    public static void m1171a(Context context, C0252a c0252a) throws SpeechError, IOException {
        synchronized (f1013b) {
            if (!f1012a) {
                int QISRInit = MSC.QISRInit(C0254c.m1134a(context, c0252a).getBytes("gb2312"));
                C0276e.m1220a("[initIsr]ret:" + QISRInit);
                f1012a = QISRInit == 0;
                if (!f1012a) {
                    throw new SpeechError(SyncHttpTransportSE.DEFAULTTIMEOUT, QISRInit);
                }
            }
        }
    }

    private synchronized void m1172a(byte[] bArr, int i, int i2) throws SpeechError {
        int QISRAudioWrite = MSC.QISRAudioWrite(this.f1014c, bArr, i, i2, this.f1016e);
        this.f1015d.sesstatus = this.f1016e.sesstatus;
        C0276e.m1220a("QISRAudioWrite length:" + i);
        if (QISRAudioWrite != 0) {
            throw new SpeechError(SyncHttpTransportSE.DEFAULTTIMEOUT, this.f1016e.errorcode);
        }
    }

    public static boolean m1173a() {
        boolean z = true;
        synchronized (f1013b) {
            if (f1012a) {
                int QISRFini = MSC.QISRFini();
                if (QISRFini == 0) {
                    f1012a = false;
                }
                if (QISRFini != 0) {
                    z = false;
                }
            }
        }
        return z;
    }

    public static int m1174d(String str) {
        int i = 0;
        try {
            Object e = C0266a.m1175e(str);
            if (!TextUtils.isEmpty(e)) {
                i = Integer.parseInt(e);
            }
        } catch (Exception e2) {
        }
        return i;
    }

    public static String m1175e(String str) {
        String str2 = null;
        try {
            MSCSessionInfo mSCSessionInfo = new MSCSessionInfo();
            if (MSC.QISRGetParam(null, str.getBytes(), mSCSessionInfo) == 0) {
                str2 = new String(mSCSessionInfo.buffer).trim();
            }
        } catch (Exception e) {
        }
        return str2;
    }

    public synchronized int m1176a(Context context, String str, C0252a c0252a, String str2, boolean z) throws SpeechError, UnsupportedEncodingException {
        int i;
        String a = C0254c.m1136a(context, str, c0252a, z);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (TextUtils.isEmpty(str2)) {
            this.f1014c = MSC.QISRSessionBegin(null, a.getBytes("gb2312"), this.f1015d);
        } else {
            this.f1014c = MSC.QISRSessionBegin(str2.getBytes("gb2312"), a.getBytes("gb2312"), this.f1015d);
            C0276e.m1220a("sessionBegin grammarId:" + str2);
        }
        C0276e.m1220a("sessionBegin ErrCode:" + this.f1015d.errorcode + " time:" + (SystemClock.elapsedRealtime() - elapsedRealtime));
        i = this.f1015d.errorcode;
        if (i != 0 && i != 10129) {
            throw new SpeechError(SyncHttpTransportSE.DEFAULTTIMEOUT, i);
        }
        return i;
    }

    public synchronized void m1177a(String str) {
        if (this.f1014c != null) {
            C0276e.m1220a("sessionEnd enter ");
            C0276e.m1220a("sessionEnd leavel:" + (MSC.QISRSessionEnd(this.f1014c, str.getBytes()) == 0) + " time:" + (System.currentTimeMillis() - System.currentTimeMillis()));
            this.f1014c = null;
        }
    }

    public synchronized void m1178a(byte[] bArr, int i) throws SpeechError {
        m1172a(bArr, i, 2);
    }

    public synchronized boolean m1179a(String str, String str2) {
        boolean z = false;
        synchronized (this) {
            if (!(TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || this.f1014c == null)) {
                int QISRSetParam;
                C0276e.m1220a("setParam:" + str + "," + str2);
                try {
                    QISRSetParam = MSC.QISRSetParam(this.f1014c, str.getBytes("utf-8"), str2.getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    QISRSetParam = -1;
                }
                if (QISRSetParam == 0) {
                    z = true;
                }
            }
        }
        return z;
    }

    public synchronized int m1180b(String str) {
        int i = 0;
        synchronized (this) {
            if (this.f1014c != null) {
                try {
                    Object c = m1183c(str);
                    if (!TextUtils.isEmpty(c)) {
                        i = Integer.parseInt(new String(c));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return i;
    }

    public synchronized void m1181b() throws SpeechError {
        m1172a(new byte[0], 0, 4);
    }

    public synchronized int m1182c() {
        return this.f1016e.epstatues;
    }

    public synchronized String m1183c(String str) {
        String str2 = null;
        synchronized (this) {
            if (this.f1014c != null) {
                try {
                    if (MSC.QISRGetParam(this.f1014c, str.getBytes(), this.f1015d) == 0) {
                        str2 = new String(this.f1015d.buffer);
                    }
                } catch (Exception e) {
                }
            }
        }
        return str2;
    }

    public synchronized int m1184d() {
        int i = 0;
        synchronized (this) {
            int QISRGetParam;
            try {
                QISRGetParam = MSC.QISRGetParam(this.f1014c, "volume".getBytes(), this.f1016e);
                if (QISRGetParam == 0) {
                    try {
                        i = Integer.parseInt(new String(new String(this.f1016e.buffer)));
                    } catch (Exception e) {
                        C0276e.m1220a("getAudioVolume Exception vadret = " + QISRGetParam);
                        return i;
                    }
                } else {
                    C0276e.m1220a("VAD CHECK FALSE");
                }
            } catch (Exception e2) {
                QISRGetParam = i;
                C0276e.m1220a("getAudioVolume Exception vadret = " + QISRGetParam);
                return i;
            }
        }
        return i;
    }

    public synchronized boolean m1185e() {
        return this.f1015d.sesstatus == 0;
    }

    public byte[] m1186f() {
        return this.f1017f;
    }

    public C0265a m1187g() throws SpeechError {
        Date date = new Date();
        this.f1017f = MSC.QISRGetResult(this.f1014c, this.f1015d);
        C0276e.m1220a("QISRGetResult leavel:" + (this.f1017f != null) + " time:" + (new Date().getTime() - date.getTime()));
        int i = this.f1015d.errorcode;
        if (i == 0) {
            i = this.f1015d.rsltstatus;
            switch (i) {
                case KEYRecord.OWNER_USER /*0*/:
                    C0276e.m1220a("ResultStatus: hasResult" + i);
                    return C0265a.hasResult;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    C0276e.m1220a("ResultStatus: noResult" + i);
                    return C0265a.noResult;
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    C0276e.m1220a("ResultStatus: resultOver" + i);
                    return C0265a.resultOver;
                default:
                    return C0265a.noResult;
            }
        }
        C0276e.m1220a("Result: error errorcode is " + i);
        throw new SpeechError(SyncHttpTransportSE.DEFAULTTIMEOUT, i);
    }
}
