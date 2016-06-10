package com.iflytek.util.p017a;

import android.content.Context;
import com.iflytek.util.p017a.p018a.C0322a;
import com.iflytek.util.p017a.p019c.C0326a;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

/* renamed from: com.iflytek.util.a.a */
public class C0324a {
    protected Context f1205a;
    private C0326a f1206b;
    private String[] f1207c;
    private String[] f1208d;
    private HashMap<String, String> f1209e;
    private HashMap<String, String> f1210f;
    private List<C0322a> f1211g;
    private HashMap<String, String> f1212h;
    private HashMap<String, String> f1213i;

    public C0324a(Context context, C0326a c0326a) {
        this.f1207c = null;
        this.f1208d = null;
        this.f1209e = new HashMap();
        this.f1210f = new HashMap();
        this.f1211g = new ArrayList();
        this.f1212h = new HashMap();
        this.f1213i = new HashMap();
        this.f1206b = c0326a;
        this.f1205a = context;
    }

    private void m1451b() {
        if (this.f1210f.size() > 0) {
            this.f1210f = null;
            this.f1210f = new HashMap();
        }
        if (this.f1213i.size() > 0) {
            this.f1213i = null;
            this.f1213i = new HashMap();
        }
        if (this.f1209e.size() > 0) {
            this.f1209e = null;
            this.f1209e = new HashMap();
        }
        if (this.f1207c != null && this.f1207c.length > 0) {
            this.f1207c = null;
        }
    }

    public void m1452a(int i) {
        if (this.f1212h != null && this.f1212h.size() > 0) {
            this.f1212h.clear();
        }
        if (this.f1206b != null) {
            this.f1212h = this.f1206b.m1457a(i);
        }
    }

    public String[] m1453a() {
        m1451b();
        Collection arrayList = new ArrayList();
        HashMap d = this.f1206b.m1461d();
        List<C0322a> e = this.f1206b.m1462e();
        for (String str : d.keySet()) {
            String str2 = (String) d.get(str);
            this.f1209e.put(str + "p", str2);
            arrayList.add(str2);
            if (str2.contains("\u0000")) {
                this.f1213i.put(str2.replace("\u0000", MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR), str2);
            }
        }
        for (C0322a c0322a : e) {
            String a = c0322a.m1446a();
            String c = c0322a.m1448c();
            String b = c0322a.m1447b();
            this.f1210f.put(a + "s", c);
            this.f1209e.put(a + "s", b);
            arrayList.add(b);
            if (b.contains("\u0000")) {
                this.f1213i.put(b.replace("\u0000", MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR), b);
            }
            this.f1211g.add(c0322a);
        }
        Set hashSet = new HashSet(arrayList);
        this.f1207c = (String[]) hashSet.toArray(new String[hashSet.size()]);
        return this.f1207c;
    }
}
