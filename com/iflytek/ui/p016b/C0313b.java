package com.iflytek.ui.p016b;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import com.iflytek.msc.p013f.C0275d;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.ui.p015a.C0300h;
import com.iflytek.ui.p015a.C0301i;
import com.iflytek.ui.p015a.C0305m;
import com.iflytek.ui.p015a.C1080b;
import com.iflytek.ui.p015a.C1081c;
import com.iflytek.ui.p015a.C1082d;
import com.iflytek.ui.p015a.C1083e;
import com.iflytek.ui.p015a.C1084f;
import com.iflytek.ui.p015a.C1085g;
import java.util.HashMap;
import java.util.Map.Entry;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.ui.b.b */
public class C0313b {
    private int[][] f1174a;
    private HashMap<String, String> f1175b;
    private String f1176c;

    /* renamed from: com.iflytek.ui.b.b.a */
    public enum C0311a {
        image,
        curve
    }

    /* renamed from: com.iflytek.ui.b.b.b */
    public enum C0312b {
        drawer,
        drawable,
        animation
    }

    public C0313b() {
        int[][] iArr = new int[4][];
        iArr[0] = new int[]{16842919};
        iArr[1] = new int[]{16842908};
        iArr[2] = new int[]{-16842910};
        iArr[3] = new int[0];
        this.f1174a = iArr;
        this.f1175b = new HashMap();
        this.f1176c = XmlPullParser.NO_NAMESPACE;
    }

    public C0301i m1407a(Context context) {
        try {
            return ((String) this.f1175b.get("style")).equals(C0311a.image.name()) ? new C1080b(m1423e(context)) : new C1081c(m1419c());
        } catch (Exception e) {
            C0276e.m1223b("SkinTag", "view:" + m1408a() + " setRecordingView() is error ! ");
            return null;
        }
    }

    public String m1408a() {
        return this.f1176c;
    }

    public void m1409a(View view) {
        for (Entry key : this.f1175b.entrySet()) {
            m1410a(view, (String) key.getKey());
        }
    }

    public void m1410a(View view, String str) {
        if (str.equals("height")) {
            m1416b(view);
        } else if (str.equals("width")) {
            m1418c(view);
        } else if (str.equals("backgrounddrawable")) {
            m1421d(view);
        } else if (str.equals("textcolor")) {
            m1422e(view);
        } else if (str.equals("textsize")) {
            m1424f(view);
        } else if (str.equals("linktextcolor")) {
            m1425g(view);
        } else if (str.equals("margin")) {
            m1426h(view);
        }
    }

    public void m1411a(String str) {
        this.f1176c = str;
    }

    public void m1412a(String str, String str2) {
        if (this.f1175b.containsKey(str)) {
            C0276e.m1221a("SkinTag", "the key has exist in defaultConfig");
        } else {
            this.f1175b.put(str, str2);
        }
    }

    public C0301i m1413b(Context context) {
        try {
            return new C1082d(m1423e(context));
        } catch (Exception e) {
            C0276e.m1223b("SkinTag", "view:" + m1408a() + " getLoadingView() is error ! ");
            return null;
        }
    }

    public String m1414b() {
        return m1415b("style").trim();
    }

    public String m1415b(String str) {
        return (String) this.f1175b.get(str);
    }

    public void m1416b(View view) {
        try {
            int parseInt = Integer.parseInt(m1415b("height").trim());
            LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = C0275d.m1216a(view.getContext(), (float) parseInt);
            view.setLayoutParams(layoutParams);
        } catch (Exception e) {
            C0276e.m1223b("SkinTag", "view:" + m1408a() + " setHeight() is error ! ");
        }
    }

    public C0301i m1417c(Context context) {
        String trim = ((String) this.f1175b.get("style")).trim();
        try {
            if (trim.equals(C0312b.drawer.name())) {
                trim = (String) this.f1175b.get("drawable");
                return new C1085g(C0305m.m1380a(context, trim), C0275d.m1217a((String) this.f1175b.get("color")));
            } else if (trim.equals(C0312b.drawable.name())) {
                return new C1084f(C0305m.m1380a(context, (String) this.f1175b.get("drawable")));
            } else {
                return new C1083e(m1423e(context), Integer.parseInt((String) this.f1175b.get("speed")));
            }
        } catch (Exception e) {
            C0276e.m1223b("SkinTag", "view:" + m1408a() + " getConnectingView() is error ! ");
            return null;
        }
    }

    public void m1418c(View view) {
        try {
            int parseInt = Integer.parseInt(m1415b("width").trim());
            LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = C0275d.m1216a(view.getContext(), (float) parseInt);
            view.setLayoutParams(layoutParams);
        } catch (Exception e) {
            C0276e.m1223b("SkinTag", "view:" + m1408a() + " setWidth() is error ! ");
        }
    }

    public int[] m1419c() {
        try {
            String[] split = ((String) this.f1175b.get("color")).split(",");
            int[] iArr = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                iArr[i] = C0275d.m1217a(split[i].trim());
            }
            return iArr;
        } catch (Exception e) {
            C0276e.m1223b("SkinTag", "view:" + m1408a() + " getColorsList() is error ! ");
            return null;
        }
    }

    public C0300h m1420d(Context context) {
        C0300h c0300h;
        String str = (String) this.f1175b.get("textcolor");
        String str2 = (String) this.f1175b.get("linktextcolor");
        String str3 = (String) this.f1175b.get("drawable");
        try {
            int b = C0275d.m1219b(context, (float) Integer.parseInt((String) this.f1175b.get("textsize")));
            int a = C0275d.m1217a(str);
            int a2 = str2 != null ? C0275d.m1217a(str2) : -1;
            c0300h = new C0300h(context, C0305m.m1380a(context, str3));
            try {
                m1421d((View) c0300h);
                c0300h.m1362a(a);
                c0300h.m1365c(b);
                c0300h.m1364b(a2);
            } catch (Exception e) {
                C0276e.m1223b("SkinTag", "view:" + m1408a() + " getErrorView() is error ! ");
                return c0300h;
            }
        } catch (Exception e2) {
            c0300h = null;
            C0276e.m1223b("SkinTag", "view:" + m1408a() + " getErrorView() is error ! ");
            return c0300h;
        }
        return c0300h;
    }

    public void m1421d(View view) {
        String trim = m1415b("backgrounddrawable").trim();
        try {
            if (trim.indexOf(",") != -1) {
                String[] split = trim.split(",");
                for (int i = 0; i < split.length; i++) {
                    split[i] = split[i].trim();
                }
                view.setBackgroundDrawable(C0305m.m1382a(view.getContext(), this.f1176c, split));
                return;
            }
            view.setBackgroundDrawable(C0305m.m1380a(view.getContext(), trim));
        } catch (Exception e) {
            C0276e.m1223b("SkinTag", "view:" + m1408a() + " setBackgrounDrawable() is error ! ");
        }
    }

    public void m1422e(View view) {
        String trim = m1415b("textcolor").trim();
        try {
            if (trim.indexOf(",") != -1) {
                int[] iArr = new int[]{0, 0, 0, 0};
                String[] split = trim.split(",");
                for (int i = 0; i < split.length; i++) {
                    iArr[i] = C0275d.m1217a(split[i].trim());
                }
                ((TextView) view).setTextColor(new ColorStateList(this.f1174a, iArr));
                return;
            }
            ((TextView) view).setTextColor(C0275d.m1217a(trim));
        } catch (Exception e) {
            C0276e.m1223b("SkinTag", "view:" + m1408a() + " setFontColor() is error ! ");
        }
    }

    public Drawable[] m1423e(Context context) {
        Drawable[] drawableArr = null;
        try {
            String[] split = m1415b("drawable").trim().split(",");
            int i = 0;
            while (i < split.length) {
                split[i] = split[i].trim();
                i++;
                drawableArr = C0305m.m1384a(context, split);
            }
        } catch (Exception e) {
            C0276e.m1223b("SkinTag", "view:" + m1408a() + " getDrawableList() is error ! ");
        }
        return drawableArr;
    }

    public void m1424f(View view) {
        try {
            TextView textView = (TextView) view;
            r0.setTextSize((float) C0275d.m1219b(view.getContext(), (float) Integer.parseInt(m1415b("textsize").trim())));
        } catch (Exception e) {
            C0276e.m1223b("SkinTag", "view:" + m1408a() + " setFontSize() is error ! ");
        }
    }

    public void m1425g(View view) {
        try {
            ((TextView) view).setLinkTextColor(C0275d.m1217a(m1415b("linktextcolor").trim()));
        } catch (Exception e) {
            C0276e.m1223b("SkinTag", "view:" + m1408a() + " setFontSize() is error ! ");
        }
    }

    public void m1426h(View view) {
        try {
            String[] split = m1415b("margin").trim().split(",");
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
            marginLayoutParams.setMargins(C0275d.m1216a(view.getContext(), (float) Integer.parseInt(split[0].trim())), C0275d.m1216a(view.getContext(), (float) Integer.parseInt(split[1].trim())), C0275d.m1216a(view.getContext(), (float) Integer.parseInt(split[2].trim())), C0275d.m1216a(view.getContext(), (float) Integer.parseInt(split[3].trim())));
            view.setLayoutParams(marginLayoutParams);
        } catch (Exception e) {
            C0276e.m1223b("SkinTag", "view:" + m1408a() + " setMagin() is error ! ");
        }
    }
}
