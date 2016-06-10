package com.iflytek.msc.p012e;

import android.content.Context;
import android.text.TextUtils;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.iflytek.msc.MSC;
import com.iflytek.msc.MSCSessionInfo;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.p006b.C0252a;
import com.iflytek.p006b.C0254c;
import com.iflytek.speech.SpeechError;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/* renamed from: com.iflytek.msc.e.b */
public class C0271b {
    private static boolean f1028a;
    private static Object f1029b;
    private char[] f1030c;
    private MSCSessionInfo f1031d;

    static {
        f1028a = false;
        f1029b = new Object();
    }

    public C0271b() {
        this.f1030c = null;
        this.f1031d = new MSCSessionInfo();
    }

    public static void m1194a(Context context, C0252a c0252a) throws SpeechError, IOException {
        synchronized (f1029b) {
            if (!f1028a) {
                int QTTSInit = MSC.QTTSInit(C0254c.m1134a(context, c0252a).getBytes("gb2312"));
                C0276e.m1220a("[initTts]ret:" + QTTSInit);
                f1028a = QTTSInit == 0;
                if (!f1028a) {
                    throw new SpeechError(SyncHttpTransportSE.DEFAULTTIMEOUT, QTTSInit);
                }
            }
        }
    }

    public static boolean m1195a() {
        return f1028a;
    }

    public static boolean m1196b() {
        boolean z = true;
        synchronized (f1029b) {
            if (f1028a) {
                int QTTSFini = MSC.QTTSFini();
                if (QTTSFini == 0) {
                    f1028a = false;
                }
                if (QTTSFini != 0) {
                    z = false;
                }
            }
        }
        return z;
    }

    public static int m1197d(String str) {
        int i = 0;
        try {
            Object e = C0271b.m1198e(str);
            if (!TextUtils.isEmpty(e)) {
                i = Integer.parseInt(e);
            }
        } catch (Exception e2) {
        }
        return i;
    }

    public static String m1198e(String str) {
        String str2 = null;
        try {
            MSCSessionInfo mSCSessionInfo = new MSCSessionInfo();
            if (MSC.QTTSGetParam(null, str.getBytes(), mSCSessionInfo) == 0) {
                str2 = new String(mSCSessionInfo.buffer).trim();
            }
        } catch (Exception e) {
        }
        return str2;
    }

    public synchronized void m1199a(String str) {
        if (this.f1030c != null) {
            C0276e.m1220a("QTTSSessionEnd enter");
            C0276e.m1220a("QTTSSessionEnd leavel:" + MSC.QTTSSessionEnd(this.f1030c, str.getBytes()));
            this.f1030c = null;
        }
    }

    public synchronized void m1200a(byte[] bArr) throws SpeechError {
        C0276e.m1220a("QTTSTextPut enter");
        int QTTSTextPut = MSC.QTTSTextPut(this.f1030c, bArr);
        C0276e.m1220a("QTTSTextPut leavel:" + QTTSTextPut);
        if (QTTSTextPut != 0) {
            throw new SpeechError(SyncHttpTransportSE.DEFAULTTIMEOUT, QTTSTextPut);
        }
    }

    public synchronized boolean m1201a(Context context, C0252a c0252a, String str) throws SpeechError, UnsupportedEncodingException {
        boolean z;
        this.f1030c = null;
        this.f1030c = MSC.QTTSSessionBegin(C0254c.m1135a(context, c0252a, str).getBytes("gb2312"), this.f1031d);
        C0276e.m1220a("QTTSSessionBegin leave:" + this.f1031d.errorcode + " ErrorCode:" + this.f1031d.errorcode);
        int i = this.f1031d.errorcode;
        if (i == 0) {
            z = true;
        } else if (i != 10129) {
            throw new SpeechError(SyncHttpTransportSE.DEFAULTTIMEOUT, this.f1031d.errorcode);
        } else {
            z = false;
        }
        return z;
    }

    public synchronized int m1202b(String str) {
        int i = 0;
        synchronized (this) {
            if (this.f1030c != null) {
                try {
                    Object c = m1203c(str);
                    if (!TextUtils.isEmpty(c)) {
                        i = Integer.parseInt(new String(c));
                    }
                } catch (Exception e) {
                }
            }
        }
        return i;
    }

    public synchronized String m1203c(String str) {
        String str2 = null;
        synchronized (this) {
            if (this.f1030c != null) {
                try {
                    if (MSC.QTTSGetParam(this.f1030c, str.getBytes(), this.f1031d) == 0) {
                        str2 = new String(this.f1031d.buffer);
                    }
                } catch (Exception e) {
                }
            }
        }
        return str2;
    }

    public synchronized byte[] m1204c() throws SpeechError {
        byte[] QTTSAudioGet;
        if (this.f1030c == null) {
            throw new SpeechError(6, SyncHttpTransportSE.DEFAULTTIMEOUT);
        }
        C0276e.m1220a("QTTSAudioGet enter");
        QTTSAudioGet = MSC.QTTSAudioGet(this.f1030c, this.f1031d);
        C0276e.m1220a("QTTSAudioGet leavel:" + this.f1031d.errorcode + "value len = " + (QTTSAudioGet == null ? 0 : QTTSAudioGet.length));
        int i = this.f1031d.errorcode;
        if (i != 0) {
            throw new SpeechError(SyncHttpTransportSE.DEFAULTTIMEOUT, i);
        }
        return QTTSAudioGet;
    }

    public int m1205d() {
        try {
            return new C0252a(new String(MSC.QTTSAudioInfo(this.f1030c)), (String[][]) null).m1117a("ced", 0);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public synchronized boolean m1206e() {
        return 2 == this.f1031d.sesstatus;
    }
}
