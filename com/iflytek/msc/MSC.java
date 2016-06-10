package com.iflytek.msc;

public class MSC {
    static {
        System.loadLibrary("msc");
    }

    public static native int DebugLog(boolean z);

    public static native int QISRAudioWrite(char[] cArr, byte[] bArr, int i, int i2, MSCSessionInfo mSCSessionInfo);

    public static native int QISRFini();

    public static native int QISRGetParam(char[] cArr, byte[] bArr, MSCSessionInfo mSCSessionInfo);

    public static final native byte[] QISRGetResult(char[] cArr, MSCSessionInfo mSCSessionInfo);

    public static native int QISRInit(byte[] bArr);

    public static final native char[] QISRSessionBegin(byte[] bArr, byte[] bArr2, MSCSessionInfo mSCSessionInfo);

    public static native int QISRSessionEnd(char[] cArr, byte[] bArr);

    public static native int QISRSetParam(char[] cArr, byte[] bArr, byte[] bArr2);

    public static native int QISVFini();

    public static native byte[] QMSPDownloadData(byte[] bArr, MSCSessionInfo mSCSessionInfo);

    public static native int QMSPLogOut();

    public static native int QMSPLogin(byte[] bArr, byte[] bArr2, byte[] bArr3);

    public static native byte[] QMSPSearch(byte[] bArr, byte[] bArr2, MSCSessionInfo mSCSessionInfo);

    public static native byte[] QMSPUploadData(byte[] bArr, byte[] bArr2, int i, byte[] bArr3, MSCSessionInfo mSCSessionInfo);

    public static native byte[] QTTSAudioGet(char[] cArr, MSCSessionInfo mSCSessionInfo);

    public static native char[] QTTSAudioInfo(char[] cArr);

    public static native int QTTSFini();

    public static native int QTTSGetParam(char[] cArr, byte[] bArr, MSCSessionInfo mSCSessionInfo);

    public static native int QTTSInit(byte[] bArr);

    public static final native char[] QTTSSessionBegin(byte[] bArr, MSCSessionInfo mSCSessionInfo);

    public static native int QTTSSessionEnd(char[] cArr, byte[] bArr);

    public static native int QTTSTextPut(char[] cArr, byte[] bArr);
}
