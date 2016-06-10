package cn.sharesdk.framework.p000a;

import android.content.Context;
import android.text.TextUtils;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.C0051R;
import cn.sharesdk.framework.utils.C0052a;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

/* renamed from: cn.sharesdk.framework.a.h */
public class C1012h extends C0025g {
    private static C1012h f1733c;

    static {
        f1733c = null;
    }

    private C1012h() {
    }

    public static C1012h m1768a() {
        if (f1733c == null) {
            f1733c = new C1012h();
        }
        return f1733c;
    }

    private void m1769a(String str, int i) {
        if (!TextUtils.isEmpty(str) && i > 0) {
            ShareSDK.logApiEvent(str, i);
        }
    }

    public InputStream m1770a(String str, ArrayList<C0023d<String>> arrayList, C0022c c0022c, String str2, int i) {
        m1769a(str2, i);
        return super.m36a(str, (ArrayList) arrayList, c0022c);
    }

    public String m1771a(Context context, String str) {
        String b = C0052a.m170b(str);
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf > 0) {
            b = b + str.substring(lastIndexOf);
        }
        File file = new File(C0051R.getCachePath(context, null), b);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        m39a(str, file);
        return file.getAbsolutePath();
    }

    public String m1772a(String str, ArrayList<C0023d<String>> arrayList, C0023d<String> c0023d, String str2, int i) {
        return m1773a(str, (ArrayList) arrayList, (C0023d) c0023d, null, str2, i);
    }

    public String m1773a(String str, ArrayList<C0023d<String>> arrayList, C0023d<String> c0023d, ArrayList<C0023d<String>> arrayList2, String str2, int i) {
        return m1774a(str, arrayList, c0023d, arrayList2, null, str2, i);
    }

    public String m1774a(String str, ArrayList<C0023d<String>> arrayList, C0023d<String> c0023d, ArrayList<C0023d<String>> arrayList2, ArrayList<C0023d<?>> arrayList3, String str2, int i) {
        m1769a(str2, i);
        return super.m37a(str, arrayList, c0023d, arrayList2, arrayList3);
    }

    public String m1775a(String str, ArrayList<C0023d<String>> arrayList, String str2, int i) {
        return m1776a(str, (ArrayList) arrayList, null, null, str2, i);
    }

    public String m1776a(String str, ArrayList<C0023d<String>> arrayList, ArrayList<C0023d<String>> arrayList2, ArrayList<C0023d<?>> arrayList3, String str2, int i) {
        m1769a(str2, i);
        return super.m38a(str, arrayList, arrayList2, arrayList3);
    }

    public String m1777b(String str, ArrayList<C0023d<String>> arrayList, C0023d<String> c0023d, ArrayList<C0023d<String>> arrayList2, ArrayList<C0023d<?>> arrayList3, String str2, int i) {
        m1769a(str2, i);
        return super.m40b(str, arrayList, c0023d, arrayList2, arrayList3);
    }

    public String m1778b(String str, ArrayList<C0023d<String>> arrayList, String str2, int i) {
        return m1772a(str, (ArrayList) arrayList, null, str2, i);
    }
}
