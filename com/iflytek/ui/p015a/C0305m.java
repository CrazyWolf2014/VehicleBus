package com.iflytek.ui.p015a;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.iflytek.p007c.C0255a;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/* renamed from: com.iflytek.ui.a.m */
public class C0305m {
    public static int f1142a;
    public static int f1143b;
    public static int f1144c;
    public static int f1145d;
    private static HashMap<String, Drawable> f1146e;
    private static HashMap<String, Drawable> f1147f;

    static {
        f1146e = new HashMap();
        f1147f = new HashMap();
        f1142a = 3;
        f1143b = 4;
        f1144c = 7;
        f1145d = 8;
    }

    public static synchronized Drawable m1380a(Context context, String str) throws Exception {
        Drawable drawable;
        synchronized (C0305m.class) {
            drawable = (Drawable) f1146e.get(str);
            if (drawable == null) {
                drawable = C0305m.m1388e(context, str);
                f1146e.put(str, drawable);
            }
        }
        return drawable;
    }

    private static Drawable m1381a(Context context, String str, String str2, String str3, String str4) throws Exception {
        Drawable stateListDrawable = new StateListDrawable();
        if (str4 != null) {
            stateListDrawable.addState(new int[]{-16842910}, C0305m.m1380a(context, str4));
        }
        if (str2 != null) {
            stateListDrawable.addState(new int[]{16842919}, C0305m.m1380a(context, str2));
        }
        if (str3 != null) {
            stateListDrawable.addState(new int[]{16842908}, C0305m.m1380a(context, str3));
        }
        if (str != null) {
            stateListDrawable.addState(new int[0], C0305m.m1380a(context, str));
        }
        return stateListDrawable;
    }

    public static synchronized Drawable m1382a(Context context, String str, String[] strArr) throws Exception {
        Drawable drawable;
        synchronized (C0305m.class) {
            String[] strArr2 = new String[4];
            drawable = null;
            for (int i = 0; i < strArr.length; i++) {
                strArr2[i] = strArr[i];
            }
            if (null == null) {
                drawable = C0305m.m1381a(context, strArr2[0], strArr2[1], strArr2[2], strArr2[3]);
            }
        }
        return drawable;
    }

    public static View m1383a(Context context, String str, ViewGroup viewGroup) throws Exception {
        return ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(context.getAssets().openXmlResourceParser("assets/" + C0255a.f966a + File.separator + str + ".xml"), viewGroup);
    }

    public static synchronized Drawable[] m1384a(Context context, String[] strArr) throws Exception {
        Drawable[] drawableArr;
        synchronized (C0305m.class) {
            drawableArr = new Drawable[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                Drawable drawable = (Drawable) f1147f.get(strArr[i]);
                if (drawable == null) {
                    drawable = C0305m.m1380a(context, strArr[i]);
                    f1147f.put(strArr[i], drawable);
                }
                drawableArr[i] = drawable;
            }
        }
        return drawableArr;
    }

    public static synchronized Drawable m1385b(Context context, String str) throws Exception {
        Drawable f;
        synchronized (C0305m.class) {
            f = C0305m.m1389f(context, str);
        }
        return f;
    }

    private static InputStream m1386c(Context context, String str) throws IOException {
        return context.getAssets().open(C0255a.f966a + File.separator + str);
    }

    private static XmlResourceParser m1387d(Context context, String str) throws IOException {
        return context.getAssets().openXmlResourceParser("assets/" + C0255a.f966a + File.separator + str);
    }

    private static Drawable m1388e(Context context, String str) throws Exception {
        InputStream c = C0305m.m1386c(context, str + ".png");
        TypedValue typedValue = new TypedValue();
        typedValue.density = 240;
        Drawable a = VERSION.SDK_INT > f1142a ? C0297a.m1356a(context.getResources(), typedValue, c, str, null) : Drawable.createFromResourceStream(context.getResources(), typedValue, c, str);
        if (c != null) {
            c.close();
        }
        return a;
    }

    private static Drawable m1389f(Context context, String str) throws Exception {
        Object d = C0305m.m1387d(context, str);
        Drawable createFromXml = Drawable.createFromXml(context.getResources(), d);
        if (d != null) {
            d.close();
        }
        return createFromXml;
    }
}
