package com.iflytek.msc.p011d;

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

/* renamed from: com.iflytek.msc.d.a */
public class C0269a {
    public static Object f1021a;
    private MSCSessionInfo f1022b;

    static {
        f1021a = new Object();
    }

    public C0269a() {
        this.f1022b = new MSCSessionInfo();
    }

    public static void m1189a(Context context, String str, String str2, C0252a c0252a) throws SpeechError, IOException {
        byte[] bArr = null;
        synchronized (f1021a) {
            byte[] bytes;
            String a = C0254c.m1134a(context, c0252a);
            if (!TextUtils.isEmpty(str)) {
                bytes = str.getBytes("utf-8");
            } else if (context != null) {
                Object a2 = C0254c.m1133a(context);
                bytes = TextUtils.isEmpty(a2) ? null : a2.getBytes("utf-8");
            } else {
                bytes = null;
            }
            if (!TextUtils.isEmpty(str2)) {
                bArr = str2.getBytes("utf-8");
            }
            int QMSPLogin = MSC.QMSPLogin(bytes, bArr, a.getBytes("utf-8"));
            C0276e.m1220a("[MSPLogin]ret:" + QMSPLogin);
            if (QMSPLogin != 0) {
                throw new SpeechError(SyncHttpTransportSE.DEFAULTTIMEOUT, QMSPLogin);
            }
        }
    }

    public static boolean m1190a() {
        boolean z;
        synchronized (f1021a) {
            z = MSC.QMSPLogOut() == 0;
        }
        return z;
    }

    public synchronized byte[] m1191a(Context context, C0252a c0252a) throws SpeechError {
        byte[] QMSPDownloadData;
        synchronized (f1021a) {
            String c = C0254c.m1142c(context, c0252a);
            C0276e.m1220a("[MSPSession downloadData]enter time:" + System.currentTimeMillis());
            QMSPDownloadData = MSC.QMSPDownloadData(c.getBytes(), this.f1022b);
            C0276e.m1220a("[MSPSession downloadData]leavel:" + this.f1022b.errorcode + ",data len = " + (QMSPDownloadData == null ? 0 : QMSPDownloadData.length));
            int i = this.f1022b.errorcode;
            if (i != 0 || QMSPDownloadData == null) {
                throw new SpeechError(SyncHttpTransportSE.DEFAULTTIMEOUT, i);
            }
        }
        return QMSPDownloadData;
    }

    public synchronized byte[] m1192a(Context context, C0252a c0252a, String str) throws SpeechError, UnsupportedEncodingException {
        byte[] QMSPSearch;
        synchronized (f1021a) {
            String c = C0254c.m1142c(context, c0252a);
            C0276e.m1220a("[MSPSession searchResult]enter time:" + System.currentTimeMillis());
            QMSPSearch = MSC.QMSPSearch(c.getBytes(), str.getBytes("utf-8"), this.f1022b);
            C0276e.m1220a("[QMSPSearch searchResult]leavel:" + this.f1022b.errorcode + ",data len = " + (QMSPSearch == null ? 0 : QMSPSearch.length));
            int i = this.f1022b.errorcode;
            if (i != 0 || QMSPSearch == null) {
                throw new SpeechError(SyncHttpTransportSE.DEFAULTTIMEOUT, i);
            }
        }
        return QMSPSearch;
    }

    public synchronized byte[] m1193a(Context context, String str, byte[] bArr, C0252a c0252a) throws SpeechError, UnsupportedEncodingException {
        byte[] QMSPUploadData;
        synchronized (f1021a) {
            String c = C0254c.m1142c(context, c0252a);
            C0276e.m1220a("[MSPSession uploadData]enter time:" + System.currentTimeMillis());
            QMSPUploadData = MSC.QMSPUploadData(str.getBytes("utf-8"), bArr, bArr.length, c.getBytes("utf-8"), this.f1022b);
            C0276e.m1220a("[MSPSession uploaddData]leavel:" + this.f1022b.errorcode + ",data len = " + (QMSPUploadData == null ? 0 : QMSPUploadData.length));
            int i = this.f1022b.errorcode;
            if (i != 0 || QMSPUploadData == null) {
                throw new SpeechError(SyncHttpTransportSE.DEFAULTTIMEOUT, i);
            }
        }
        return QMSPUploadData;
    }
}
