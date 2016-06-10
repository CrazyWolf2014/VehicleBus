package cn.sharesdk.framework.p001b.p002a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.util.ArrayList;
import org.achartengine.chart.TimeChart;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: cn.sharesdk.framework.b.a.e */
public class C0040e {
    public static int f38a;
    public static int f39b;
    public static int f40c;

    static {
        f38a = 0;
        f39b = 1;
        f40c = 2;
    }

    public static synchronized long m64a(Context context, String str, long j) {
        long a;
        synchronized (C0040e.class) {
            if (str != null) {
                if (str.trim() != XmlPullParser.NO_NAMESPACE) {
                    C0037b a2 = C0037b.m46a(context);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("post_time", Long.valueOf(j));
                    contentValues.put("message_data", str.toString());
                    a = a2.m49a(BundleBuilder.AskFromMessage, contentValues);
                }
            }
            a = -1;
        }
        return a;
    }

    public static synchronized long m65a(Context context, ArrayList<String> arrayList) {
        long a;
        synchronized (C0040e.class) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < arrayList.size(); i++) {
                stringBuilder.append("'");
                stringBuilder.append((String) arrayList.get(i));
                stringBuilder.append("'");
                stringBuilder.append(",");
            }
            a = (long) C0037b.m46a(context).m48a(BundleBuilder.AskFromMessage, "_id in ( " + stringBuilder.toString().substring(0, stringBuilder.length() - 1) + " )", null);
        }
        return a;
    }

    public static synchronized ArrayList<C0039d> m66a(Context context) {
        ArrayList<C0039d> a;
        synchronized (C0040e.class) {
            if (C0037b.m46a(context).m47a(BundleBuilder.AskFromMessage) < 10) {
                a = C0040e.m67a(context, "post_time < ?", new String[]{String.valueOf((System.currentTimeMillis() - C0038c.m51a(context).m52a()) - TimeChart.DAY)});
            } else {
                a = C0040e.m67a(context, null, null);
            }
        }
        return a;
    }

    private static synchronized ArrayList<C0039d> m67a(Context context, String str, String[] strArr) {
        ArrayList<C0039d> arrayList;
        synchronized (C0040e.class) {
            arrayList = new ArrayList();
            C0039d c0039d = new C0039d();
            StringBuilder stringBuilder = new StringBuilder();
            Cursor a = C0037b.m46a(context).m50a(BundleBuilder.AskFromMessage, new String[]{"_id", "post_time", "message_data"}, str, strArr, null);
            StringBuilder stringBuilder2 = stringBuilder;
            C0039d c0039d2 = c0039d;
            while (a != null && a.moveToNext()) {
                c0039d2.f37b.add(a.getString(0));
                if (c0039d2.f37b.size() == 10) {
                    stringBuilder2.append(a.getString(2));
                    c0039d2.f36a = stringBuilder2.toString();
                    arrayList.add(c0039d2);
                    c0039d2 = new C0039d();
                    stringBuilder2 = new StringBuilder();
                } else {
                    stringBuilder2.append(a.getString(2) + SpecilApiUtil.LINE_SEP);
                }
            }
            a.close();
            if (c0039d2.f37b.size() != 0) {
                c0039d2.f36a = stringBuilder2.toString().substring(0, stringBuilder2.length() - 1);
                arrayList.add(c0039d2);
            }
        }
        return arrayList;
    }

    public static synchronized ArrayList<C0039d> m68b(Context context) {
        ArrayList<C0039d> arrayList;
        synchronized (C0040e.class) {
            arrayList = C0037b.m46a(context).m47a(BundleBuilder.AskFromMessage) < 2 ? new ArrayList() : C0040e.m67a(context, null, null);
        }
        return arrayList;
    }
}
