package cn.sharesdk.framework.utils;

import android.util.Log;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: cn.sharesdk.framework.utils.e */
public class C0058e {
    protected static C0056a f107a;
    protected static C0057b f108b;

    /* renamed from: cn.sharesdk.framework.utils.e.a */
    public static class C0056a {
        protected int f104a;
        protected String f105b;
        protected String f106c;

        protected C0056a() {
            this.f104a = 7;
            this.f105b = XmlPullParser.NO_NAMESPACE;
            this.f106c = XmlPullParser.NO_NAMESPACE;
        }
    }

    /* renamed from: cn.sharesdk.framework.utils.e.b */
    public static class C0057b {
        protected static String m213a(int i) {
            if (C0058e.f107a.f104a <= 3) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                if (i >= 0 && i < stackTrace.length) {
                    StackTraceElement stackTraceElement = stackTrace[i];
                    String fileName = stackTraceElement.getFileName();
                    fileName = (fileName == null || fileName.length() <= 0) ? stackTraceElement.getClassName() : C0058e.f107a.f106c + FilePathGenerator.ANDROID_DIR_SEP + fileName;
                    int lineNumber = stackTraceElement.getLineNumber();
                    String valueOf = String.valueOf(lineNumber);
                    if (lineNumber < 0) {
                        valueOf = stackTraceElement.getMethodName();
                        if (valueOf == null || valueOf.length() <= 0) {
                            valueOf = "Unknown Source";
                        }
                    }
                    return fileName + "(" + valueOf + ")";
                }
            }
            return C0058e.f107a.f106c;
        }

        public int m214a(int i, String str) {
            return Log.println(i, C0057b.m213a(5), m215a(str));
        }

        protected String m215a(String str) {
            if (C0058e.f107a.f104a > 3) {
                return str;
            }
            return String.format("%s %s", new Object[]{Thread.currentThread().getName(), str});
        }
    }

    static {
        f107a = new C0056a();
        f108b = new C0057b();
    }

    public static int m216a(Object obj, Object... objArr) {
        if (f107a.f104a > 3) {
            return 0;
        }
        String obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return f108b.m214a(3, obj2);
    }

    public static int m217a(Throwable th) {
        return f107a.f104a <= 3 ? f108b.m214a(3, Log.getStackTraceString(th)) : 0;
    }

    public static int m218a(Throwable th, Object obj, Object... objArr) {
        if (f107a.f104a > 6) {
            return 0;
        }
        String obj2 = obj.toString();
        StringBuilder stringBuilder = new StringBuilder();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return f108b.m214a(6, stringBuilder.append(obj2).append('\n').append(Log.getStackTraceString(th)).toString());
    }

    public static int m219b(Object obj, Object... objArr) {
        if (f107a.f104a > 4) {
            return 0;
        }
        String obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return f108b.m214a(4, obj2);
    }

    public static int m220b(Throwable th) {
        return f107a.f104a <= 6 ? f108b.m214a(6, Log.getStackTraceString(th)) : 0;
    }

    public static int m221c(Object obj, Object... objArr) {
        if (f107a.f104a > 6) {
            return 0;
        }
        String obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return f108b.m214a(6, obj2);
    }
}
