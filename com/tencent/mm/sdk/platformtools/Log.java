package com.tencent.mm.sdk.platformtools;

import android.os.Build;
import android.os.Build.VERSION;
import android.widget.Toast;
import com.tencent.mm.algorithm.MD5;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

public class Log {
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_NONE = 6;
    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_WARNING = 3;
    private static PrintStream f1656W;
    private static byte[] f1657X;
    private static final String f1658Y;
    private static int level;

    /* renamed from: com.tencent.mm.sdk.platformtools.Log.1 */
    final class C08641 implements Runnable {
        final /* synthetic */ String f1655Z;

        C08641(String str) {
            this.f1655Z = str;
        }

        public final void run() {
            Toast.makeText(MMApplicationContext.getContext(), this.f1655Z, Log.LEVEL_DEBUG).show();
        }
    }

    static {
        level = LEVEL_NONE;
        f1657X = null;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("VERSION.RELEASE:[" + VERSION.RELEASE);
        stringBuilder.append("] VERSION.CODENAME:[" + VERSION.CODENAME);
        stringBuilder.append("] VERSION.INCREMENTAL:[" + VERSION.INCREMENTAL);
        stringBuilder.append("] BOARD:[" + Build.BOARD);
        stringBuilder.append("] DEVICE:[" + Build.DEVICE);
        stringBuilder.append("] DISPLAY:[" + Build.DISPLAY);
        stringBuilder.append("] FINGERPRINT:[" + Build.FINGERPRINT);
        stringBuilder.append("] HOST:[" + Build.HOST);
        stringBuilder.append("] MANUFACTURER:[" + Build.MANUFACTURER);
        stringBuilder.append("] MODEL:[" + Build.MODEL);
        stringBuilder.append("] PRODUCT:[" + Build.PRODUCT);
        stringBuilder.append("] TAGS:[" + Build.TAGS);
        stringBuilder.append("] TYPE:[" + Build.TYPE);
        stringBuilder.append("] USER:[" + Build.USER + "]");
        f1658Y = stringBuilder.toString();
    }

    protected Log() {
    }

    public static void m1655d(String str, String str2) {
        m1656d(str, str2, null);
    }

    public static void m1656d(String str, String str2, Object... objArr) {
        if (level <= LEVEL_DEBUG) {
            if (objArr != null) {
                str2 = String.format(str2, objArr);
            }
            android.util.Log.d(str, str2);
            LogHelper.writeToStream(f1656W, f1657X, "D/" + str, str2);
        }
    }

    public static void m1657e(String str, String str2) {
        m1658e(str, str2, null);
    }

    public static void m1658e(String str, String str2, Object... objArr) {
        if (level <= LEVEL_ERROR) {
            if (objArr != null) {
                str2 = String.format(str2, objArr);
            }
            android.util.Log.e(str, str2);
            LogHelper.writeToStream(f1656W, f1657X, "E/" + str, str2);
        }
    }

    public static void m1659f(String str, String str2) {
        m1660f(str, str2, null);
    }

    public static void m1660f(String str, String str2, Object... objArr) {
        if (level <= LEVEL_FATAL) {
            if (objArr != null) {
                str2 = String.format(str2, objArr);
            }
            android.util.Log.e(str, str2);
            LogHelper.writeToStream(f1656W, f1657X, "F/" + str, str2);
            MMHandlerThread.postToMainThread(new C08641(str2));
        }
    }

    public static int getLevel() {
        return level;
    }

    public static String getSysInfo() {
        return f1658Y;
    }

    public static void m1661i(String str, String str2) {
        m1662i(str, str2, null);
    }

    public static void m1662i(String str, String str2, Object... objArr) {
        if (level <= LEVEL_INFO) {
            if (objArr != null) {
                str2 = String.format(str2, objArr);
            }
            android.util.Log.i(str, str2);
            LogHelper.writeToStream(f1656W, f1657X, "I/" + str, str2);
        }
    }

    public static void reset() {
        f1656W = null;
        f1657X = null;
    }

    public static void setLevel(int i, boolean z) {
        level = i;
        android.util.Log.w("MicroMsg.SDK.Log", "new log level: " + i);
        if (z) {
            android.util.Log.e("MicroMsg.SDK.Log", "no jni log level support");
        }
    }

    public static void setOutputPath(String str, String str2, String str3, int i) {
        if (str != null && str.length() != 0 && str3 != null && str3.length() != 0) {
            try {
                File file = new File(str);
                if (file.exists()) {
                    InputStream fileInputStream = file.length() > 0 ? new FileInputStream(str) : null;
                    setOutputStream(fileInputStream, new FileOutputStream(str, true), str2, str3, i);
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setOutputStream(InputStream inputStream, OutputStream outputStream, String str, String str2, int i) {
        try {
            long j;
            f1656W = new PrintStream(new BufferedOutputStream(outputStream));
            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String trim = Util.nullAsNil(bufferedReader.readLine()).substring(LEVEL_INFO).trim();
                str2 = Util.nullAsNil(bufferedReader.readLine()).substring(LEVEL_INFO).trim();
                j = Util.getLong(Util.nullAsNil(bufferedReader.readLine()).trim().substring(LEVEL_INFO), 0);
                Object[] objArr = new Object[LEVEL_WARNING];
                objArr[LEVEL_VERBOSE] = trim;
                objArr[LEVEL_DEBUG] = str2;
                objArr[LEVEL_INFO] = Long.valueOf(j);
                m1656d("MicroMsg.SDK.Log", "using provided info, type=%s, user=%s, createtime=%d", objArr);
            } else {
                j = System.currentTimeMillis();
                LogHelper.initLogHeader(f1656W, str, str2, j, i);
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str2);
            stringBuffer.append(j);
            stringBuffer.append("dfdhgc");
            f1657X = MD5.getMessageDigest(stringBuffer.toString().getBytes()).substring(7, 21).getBytes();
            android.util.Log.d("MicroMsg.SDK.Log", "set up out put stream");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void m1663v(String str, String str2) {
        m1664v(str, str2, null);
    }

    public static void m1664v(String str, String str2, Object... objArr) {
        if (level <= 0) {
            if (objArr != null) {
                str2 = String.format(str2, objArr);
            }
            android.util.Log.v(str, str2);
            LogHelper.writeToStream(f1656W, f1657X, "V/" + str, str2);
        }
    }

    public static void m1665w(String str, String str2) {
        m1666w(str, str2, null);
    }

    public static void m1666w(String str, String str2, Object... objArr) {
        if (level <= LEVEL_WARNING) {
            if (objArr != null) {
                str2 = String.format(str2, objArr);
            }
            android.util.Log.w(str, str2);
            LogHelper.writeToStream(f1656W, f1657X, "W/" + str, str2);
        }
    }
}
