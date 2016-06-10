package cn.sharesdk.framework.utils;

import android.text.TextUtils;
import android.util.Xml;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/* renamed from: cn.sharesdk.framework.utils.m */
public class C0068m {

    /* renamed from: cn.sharesdk.framework.utils.m.a */
    private static class C0067a extends DefaultHandler {
        private HashMap<String, Object> f124a;
        private HashMap<String, Object> f125b;

        public C0067a() {
            this.f124a = new HashMap();
        }

        public HashMap<String, Object> m239a() {
            return this.f124a;
        }

        public void characters(char[] cArr, int i, int i2) {
            CharSequence trim = String.valueOf(cArr, i, i2).trim();
            if (!TextUtils.isEmpty(trim) && this.f125b != null) {
                this.f125b.put(SharedPref.VALUE, trim);
            }
        }

        public void endElement(String str, String str2, String str3) {
            this.f125b = null;
        }

        public void startElement(String str, String str2, String str3, Attributes attributes) {
            if (this.f125b != null) {
                HashMap hashMap = new HashMap();
                this.f125b.put(str2, hashMap);
                this.f125b = hashMap;
            } else {
                this.f125b = new HashMap();
                this.f124a.put(str2, this.f125b);
            }
            int length = attributes.getLength();
            for (int i = 0; i < length; i++) {
                this.f125b.put(attributes.getLocalName(i), attributes.getValue(i));
            }
        }
    }

    public HashMap<String, Object> m240a(String str) {
        Object c0067a = new C0067a();
        Xml.parse(str, c0067a);
        return c0067a.m239a();
    }
}
