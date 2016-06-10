package com.iflytek.p006b;

import android.text.TextUtils;
import com.iflytek.msc.p013f.C0276e;
import com.ifoer.mine.Contact;
import java.util.HashMap;
import java.util.Map.Entry;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* renamed from: com.iflytek.b.a */
public class C0252a {
    HashMap<String, String> f963a;

    public C0252a() {
        this.f963a = new HashMap();
    }

    public C0252a(String str, String[][] strArr) {
        this.f963a = new HashMap();
        m1123a(str, strArr);
    }

    public int m1117a(String str, int i) {
        if (C0253b.m1130a(str) && this.f963a.containsKey(str)) {
            try {
                i = Integer.parseInt((String) this.f963a.get(str));
            } catch (Exception e) {
            }
        }
        return i;
    }

    public String m1118a(String str) {
        return (C0253b.m1130a(str) && this.f963a.containsKey(str)) ? (String) this.f963a.get(str) : null;
    }

    public void m1119a() {
        this.f963a.clear();
    }

    public void m1120a(C0252a c0252a, String str) {
        if (c0252a != null) {
            m1121a(str, c0252a.m1118a(str));
        }
    }

    public void m1121a(String str, String str2) {
        m1122a(str, str2, true);
    }

    public void m1122a(String str, String str2, boolean z) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            if (z || !this.f963a.containsKey(str)) {
                this.f963a.put(str, str2);
            }
        }
    }

    public void m1123a(String str, String[][] strArr) {
        this.f963a.clear();
        if (!TextUtils.isEmpty(str)) {
            for (String str2 : str.split(",")) {
                int indexOf = str2.indexOf("=");
                if (indexOf > 0 && indexOf < str2.length()) {
                    this.f963a.put(str2.substring(0, indexOf), str2.substring(indexOf + 1));
                }
            }
        }
        m1124a(strArr);
    }

    public void m1124a(String[][] strArr) {
        if (strArr != null) {
            for (Object[] objArr : strArr) {
                if (this.f963a.containsKey(objArr[0])) {
                    String str = (String) this.f963a.get(objArr[0]);
                    this.f963a.remove(objArr[0]);
                    for (int i = 1; i < objArr.length; i++) {
                        this.f963a.put(objArr[i], str);
                    }
                }
            }
        }
    }

    public boolean m1125a(String str, boolean z) {
        if (!C0253b.m1130a(str) || !this.f963a.containsKey(str)) {
            return z;
        }
        String str2 = (String) this.f963a.get(str);
        return (str2.equals("true") || str2.equals(Contact.RELATION_FRIEND)) ? true : (str2.equals("false") || str2.equals(Contact.RELATION_ASK)) ? false : z;
    }

    public C0252a m1126b() {
        C0252a c0252a = new C0252a();
        for (Entry entry : this.f963a.entrySet()) {
            c0252a.m1121a((String) entry.getKey(), (String) entry.getValue());
        }
        return c0252a;
    }

    public boolean m1127b(String str) {
        return this.f963a.containsKey(str);
    }

    public HashMap<String, String> m1128c() {
        return this.f963a;
    }

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        return m1126b();
    }

    public void m1129d() {
        for (Entry entry : this.f963a.entrySet()) {
            Object replaceAll = ((String) entry.getValue()).replaceAll("[,\n ]", "|");
            if (replaceAll.length() > Opcodes.IUSHR) {
                replaceAll = replaceAll.substring(0, Opcodes.IUSHR);
            }
            entry.setValue(replaceAll);
        }
    }

    public String toString() {
        m1129d();
        StringBuffer stringBuffer = new StringBuffer();
        for (Entry entry : this.f963a.entrySet()) {
            stringBuffer.append((String) entry.getKey());
            stringBuffer.append("=");
            stringBuffer.append((String) entry.getValue());
            stringBuffer.append(",");
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        String stringBuffer2 = stringBuffer.toString();
        C0276e.m1224c(stringBuffer2);
        return stringBuffer2;
    }
}
