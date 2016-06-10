package com.tencent.mm.platformtools;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.io.UnsupportedEncodingException;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.WKSRecord.Service;

public class SpellMap {
    private static int m1642a(char c) {
        if (c <= '\u0080') {
            return c;
        }
        try {
            byte[] bytes = String.valueOf(c).getBytes("GBK");
            return (bytes == null || bytes.length > 2 || bytes.length <= 0) ? 0 : bytes.length == 1 ? bytes[0] : bytes.length == 2 ? ((bytes[0] + KEYRecord.OWNER_ZONE) << 16) + (bytes[1] + KEYRecord.OWNER_ZONE) : 0;
        } catch (UnsupportedEncodingException e) {
            return 0;
        }
    }

    public static String getSpell(char c) {
        int a = m1642a(c);
        if (a < AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED) {
            return String.valueOf(c);
        }
        int i = a >> 16;
        a &= KEYRecord.PROTOCOL_ANY;
        String spellGetJni = (i < Service.PWDGEN || i > CERTRecord.URI) ? null : (a < 63 || a > CERTRecord.OID) ? null : spellGetJni(i - 129, a - 63);
        if (spellGetJni == null) {
            return null;
        }
        String[] split = spellGetJni.split(",");
        return (split == null || split.length < 2) ? spellGetJni : split[0];
    }

    public static native String spellGetJni(int i, int i2);
}
