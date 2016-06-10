package com.thoughtworks.xstream.io.xml;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.ifoer.util.MyHttpException;
import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.KEYRecord.Flags;

public class XmlFriendlyNameCoder implements NameCoder, Cloneable {
    private static final IntPair[] XML_NAME_CHAR_EXTRA_BOUNDS;
    private static final IntPair[] XML_NAME_START_CHAR_BOUNDS;
    private final String dollarReplacement;
    private transient Map escapeCache;
    private final String escapeCharReplacement;
    private final String hexPrefix;
    private transient Map unescapeCache;

    private static class IntPair {
        int max;
        int min;

        public IntPair(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    static {
        AnonymousClass1IntPairList list = new ArrayList() {
            void add(int min, int max) {
                super.add(new IntPair(min, max));
            }

            void add(char cp) {
                super.add(new IntPair(cp, cp));
            }
        };
        list.add(':');
        list.add(65, 90);
        list.add(97, Opcodes.ISHR);
        list.add('_');
        list.add(Wbxml.EXT_0, 214);
        list.add(216, 246);
        list.add(248, MyHttpException.ERROR_TOTAL_PRICE_ZERO);
        list.add(880, 893);
        list.add(895, 8191);
        list.add(8204, 8205);
        list.add(8304, 8591);
        list.add(11264, 12271);
        list.add(12289, 55295);
        list.add(63744, 64975);
        list.add(65008, 65533);
        list.add(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED, 983039);
        XML_NAME_START_CHAR_BOUNDS = (IntPair[]) list.toArray(new IntPair[list.size()]);
        list.clear();
        list.add(SignatureVisitor.SUPER);
        list.add('.');
        list.add(48, 57);
        list.add('\u00b7');
        list.add(Flags.OWNER_MASK, 879);
        list.add(8255, 8256);
        XML_NAME_CHAR_EXTRA_BOUNDS = (IntPair[]) list.toArray(new IntPair[list.size()]);
    }

    public XmlFriendlyNameCoder() {
        this("_-", "__");
    }

    public XmlFriendlyNameCoder(String dollarReplacement, String escapeCharReplacement) {
        this(dollarReplacement, escapeCharReplacement, "_.");
    }

    public XmlFriendlyNameCoder(String dollarReplacement, String escapeCharReplacement, String hexPrefix) {
        this.dollarReplacement = dollarReplacement;
        this.escapeCharReplacement = escapeCharReplacement;
        this.hexPrefix = hexPrefix;
        readResolve();
    }

    public String decodeAttribute(String attributeName) {
        return decodeName(attributeName);
    }

    public String decodeNode(String elementName) {
        return decodeName(elementName);
    }

    public String encodeAttribute(String name) {
        return encodeName(name);
    }

    public String encodeNode(String name) {
        return encodeName(name);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String encodeName(java.lang.String r9) {
        /*
        r8 = this;
        r7 = 95;
        r6 = 36;
        r5 = r8.escapeCache;
        r4 = r5.get(r9);
        r4 = (java.lang.String) r4;
        if (r4 != 0) goto L_0x009b;
    L_0x000e:
        r2 = r9.length();
        r1 = 0;
    L_0x0013:
        if (r1 >= r2) goto L_0x0025;
    L_0x0015:
        r0 = r9.charAt(r1);
        if (r0 == r6) goto L_0x0025;
    L_0x001b:
        if (r0 == r7) goto L_0x0025;
    L_0x001d:
        r5 = 27;
        if (r0 <= r5) goto L_0x0025;
    L_0x0021:
        r5 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        if (r0 < r5) goto L_0x0028;
    L_0x0025:
        if (r1 != r2) goto L_0x002b;
    L_0x0027:
        return r9;
    L_0x0028:
        r1 = r1 + 1;
        goto L_0x0013;
    L_0x002b:
        r3 = new java.lang.StringBuffer;
        r5 = r2 + 8;
        r3.<init>(r5);
        if (r1 <= 0) goto L_0x003c;
    L_0x0034:
        r5 = 0;
        r5 = r9.substring(r5, r1);
        r3.append(r5);
    L_0x003c:
        if (r1 >= r2) goto L_0x0092;
    L_0x003e:
        r0 = r9.charAt(r1);
        if (r0 != r6) goto L_0x004c;
    L_0x0044:
        r5 = r8.dollarReplacement;
        r3.append(r5);
    L_0x0049:
        r1 = r1 + 1;
        goto L_0x003c;
    L_0x004c:
        if (r0 != r7) goto L_0x0054;
    L_0x004e:
        r5 = r8.escapeCharReplacement;
        r3.append(r5);
        goto L_0x0049;
    L_0x0054:
        if (r1 != 0) goto L_0x005c;
    L_0x0056:
        r5 = isXmlNameStartChar(r0);
        if (r5 == 0) goto L_0x0064;
    L_0x005c:
        if (r1 <= 0) goto L_0x008e;
    L_0x005e:
        r5 = isXmlNameChar(r0);
        if (r5 != 0) goto L_0x008e;
    L_0x0064:
        r5 = r8.hexPrefix;
        r3.append(r5);
        r5 = 16;
        if (r0 >= r5) goto L_0x007a;
    L_0x006d:
        r5 = "000";
        r3.append(r5);
    L_0x0072:
        r5 = java.lang.Integer.toHexString(r0);
        r3.append(r5);
        goto L_0x0049;
    L_0x007a:
        r5 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        if (r0 >= r5) goto L_0x0084;
    L_0x007e:
        r5 = "00";
        r3.append(r5);
        goto L_0x0072;
    L_0x0084:
        r5 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        if (r0 >= r5) goto L_0x0072;
    L_0x0088:
        r5 = "0";
        r3.append(r5);
        goto L_0x0072;
    L_0x008e:
        r3.append(r0);
        goto L_0x0049;
    L_0x0092:
        r4 = r3.toString();
        r5 = r8.escapeCache;
        r5.put(r9, r4);
    L_0x009b:
        r9 = r4;
        goto L_0x0027;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder.encodeName(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String decodeName(java.lang.String r11) {
        /*
        r10 = this;
        r9 = 0;
        r8 = r10.unescapeCache;
        r7 = r8.get(r11);
        r7 = (java.lang.String) r7;
        if (r7 != 0) goto L_0x00af;
    L_0x000b:
        r8 = r10.dollarReplacement;
        r1 = r8.charAt(r9);
        r8 = r10.escapeCharReplacement;
        r2 = r8.charAt(r9);
        r8 = r10.hexPrefix;
        r3 = r8.charAt(r9);
        r5 = r11.length();
        r4 = 0;
    L_0x0022:
        if (r4 >= r5) goto L_0x002e;
    L_0x0024:
        r0 = r11.charAt(r4);
        if (r0 == r1) goto L_0x002e;
    L_0x002a:
        if (r0 == r2) goto L_0x002e;
    L_0x002c:
        if (r0 != r3) goto L_0x0031;
    L_0x002e:
        if (r4 != r5) goto L_0x0034;
    L_0x0030:
        return r11;
    L_0x0031:
        r4 = r4 + 1;
        goto L_0x0022;
    L_0x0034:
        r6 = new java.lang.StringBuffer;
        r8 = r5 + 8;
        r6.<init>(r8);
        if (r4 <= 0) goto L_0x0044;
    L_0x003d:
        r8 = r11.substring(r9, r4);
        r6.append(r8);
    L_0x0044:
        if (r4 >= r5) goto L_0x00a6;
    L_0x0046:
        r0 = r11.charAt(r4);
        if (r0 != r1) goto L_0x0065;
    L_0x004c:
        r8 = r10.dollarReplacement;
        r8 = r11.startsWith(r8, r4);
        if (r8 == 0) goto L_0x0065;
    L_0x0054:
        r8 = r10.dollarReplacement;
        r8 = r8.length();
        r8 = r8 + -1;
        r4 = r4 + r8;
        r8 = 36;
        r6.append(r8);
    L_0x0062:
        r4 = r4 + 1;
        goto L_0x0044;
    L_0x0065:
        if (r0 != r3) goto L_0x0089;
    L_0x0067:
        r8 = r10.hexPrefix;
        r8 = r11.startsWith(r8, r4);
        if (r8 == 0) goto L_0x0089;
    L_0x006f:
        r8 = r10.hexPrefix;
        r8 = r8.length();
        r4 = r4 + r8;
        r8 = r4 + 4;
        r8 = r11.substring(r4, r8);
        r9 = 16;
        r8 = java.lang.Integer.parseInt(r8, r9);
        r0 = (char) r8;
        r4 = r4 + 3;
        r6.append(r0);
        goto L_0x0062;
    L_0x0089:
        if (r0 != r2) goto L_0x00a2;
    L_0x008b:
        r8 = r10.escapeCharReplacement;
        r8 = r11.startsWith(r8, r4);
        if (r8 == 0) goto L_0x00a2;
    L_0x0093:
        r8 = r10.escapeCharReplacement;
        r8 = r8.length();
        r8 = r8 + -1;
        r4 = r4 + r8;
        r8 = 95;
        r6.append(r8);
        goto L_0x0062;
    L_0x00a2:
        r6.append(r0);
        goto L_0x0062;
    L_0x00a6:
        r7 = r6.toString();
        r8 = r10.unescapeCache;
        r8.put(r11, r7);
    L_0x00af:
        r11 = r7;
        goto L_0x0030;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder.decodeName(java.lang.String):java.lang.String");
    }

    public Object clone() {
        try {
            XmlFriendlyNameCoder coder = (XmlFriendlyNameCoder) super.clone();
            coder.readResolve();
            return coder;
        } catch (CloneNotSupportedException e) {
            throw new ObjectAccessException("Cannot clone XmlFriendlyNameCoder", e);
        }
    }

    private Object readResolve() {
        this.escapeCache = createCacheMap();
        this.unescapeCache = createCacheMap();
        return this;
    }

    protected Map createCacheMap() {
        return new HashMap();
    }

    private static boolean isXmlNameStartChar(int cp) {
        return isInNameCharBounds(cp, XML_NAME_START_CHAR_BOUNDS);
    }

    private static boolean isXmlNameChar(int cp) {
        if (isXmlNameStartChar(cp)) {
            return true;
        }
        return isInNameCharBounds(cp, XML_NAME_CHAR_EXTRA_BOUNDS);
    }

    private static boolean isInNameCharBounds(int cp, IntPair[] nameCharBounds) {
        for (IntPair p : nameCharBounds) {
            if (cp >= p.min && cp <= p.max) {
                return true;
            }
        }
        return false;
    }
}
