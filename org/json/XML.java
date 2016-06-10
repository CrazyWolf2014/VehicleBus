package org.json;

import com.cnmobi.im.view.RecordButton;
import java.util.Iterator;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xmlpull.v1.XmlPullParser;

public class XML {
    public static final Character AMP;
    public static final Character APOS;
    public static final Character BANG;
    public static final Character EQ;
    public static final Character GT;
    public static final Character LT;
    public static final Character QUEST;
    public static final Character QUOT;
    public static final Character SLASH;

    static {
        AMP = new Character('&');
        APOS = new Character('\'');
        BANG = new Character('!');
        EQ = new Character(SignatureVisitor.INSTANCEOF);
        GT = new Character('>');
        LT = new Character('<');
        QUEST = new Character('?');
        QUOT = new Character('\"');
        SLASH = new Character('/');
    }

    public static String escape(String string) {
        StringBuffer sb = new StringBuffer();
        int len = string.length();
        for (int i = 0; i < len; i++) {
            char c = string.charAt(i);
            switch (c) {
                case Type.ATMA /*34*/:
                    sb.append("&quot;");
                    break;
                case Type.A6 /*38*/:
                    sb.append("&amp;");
                    break;
                case RecordButton.MAX_TIME /*60*/:
                    sb.append("&lt;");
                    break;
                case Protocol.CFTP /*62*/:
                    sb.append("&gt;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean parse(org.json.XMLTokener r11, org.json.JSONObject r12, java.lang.String r13) throws org.json.JSONException {
        /*
        r10 = 91;
        r9 = 45;
        r7 = 1;
        r6 = 0;
        r3 = 0;
        r5 = r11.nextToken();
        r8 = BANG;
        if (r5 != r8) goto L_0x006d;
    L_0x000f:
        r0 = r11.next();
        if (r0 != r9) goto L_0x0032;
    L_0x0015:
        r7 = r11.next();
        if (r7 != r9) goto L_0x0021;
    L_0x001b:
        r7 = "-->";
        r11.skipPast(r7);
    L_0x0020:
        return r6;
    L_0x0021:
        r11.back();
    L_0x0024:
        r1 = 1;
    L_0x0025:
        r5 = r11.nextMeta();
        if (r5 != 0) goto L_0x005d;
    L_0x002b:
        r6 = "Missing '>' after '<!'.";
        r6 = r11.syntaxError(r6);
        throw r6;
    L_0x0032:
        if (r0 != r10) goto L_0x0024;
    L_0x0034:
        r5 = r11.nextToken();
        r7 = "CDATA";
        r7 = r5.equals(r7);
        if (r7 == 0) goto L_0x0056;
    L_0x0040:
        r7 = r11.next();
        if (r7 != r10) goto L_0x0056;
    L_0x0046:
        r4 = r11.nextCDATA();
        r7 = r4.length();
        if (r7 <= 0) goto L_0x0020;
    L_0x0050:
        r7 = "content";
        r12.accumulate(r7, r4);
        goto L_0x0020;
    L_0x0056:
        r6 = "Expected 'CDATA['";
        r6 = r11.syntaxError(r6);
        throw r6;
    L_0x005d:
        r7 = LT;
        if (r5 != r7) goto L_0x0066;
    L_0x0061:
        r1 = r1 + 1;
    L_0x0063:
        if (r1 > 0) goto L_0x0025;
    L_0x0065:
        goto L_0x0020;
    L_0x0066:
        r7 = GT;
        if (r5 != r7) goto L_0x0063;
    L_0x006a:
        r1 = r1 + -1;
        goto L_0x0063;
    L_0x006d:
        r8 = QUEST;
        if (r5 != r8) goto L_0x0077;
    L_0x0071:
        r7 = "?>";
        r11.skipPast(r7);
        goto L_0x0020;
    L_0x0077:
        r8 = SLASH;
        if (r5 != r8) goto L_0x009f;
    L_0x007b:
        if (r13 == 0) goto L_0x0087;
    L_0x007d:
        r6 = r11.nextToken();
        r6 = r6.equals(r13);
        if (r6 != 0) goto L_0x008e;
    L_0x0087:
        r6 = "Mismatched close tag";
        r6 = r11.syntaxError(r6);
        throw r6;
    L_0x008e:
        r6 = r11.nextToken();
        r8 = GT;
        if (r6 == r8) goto L_0x009d;
    L_0x0096:
        r6 = "Misshaped close tag";
        r6 = r11.syntaxError(r6);
        throw r6;
    L_0x009d:
        r6 = r7;
        goto L_0x0020;
    L_0x009f:
        r8 = r5 instanceof java.lang.Character;
        if (r8 == 0) goto L_0x00aa;
    L_0x00a3:
        r6 = "Misshaped tag";
        r6 = r11.syntaxError(r6);
        throw r6;
    L_0x00aa:
        r2 = r5;
        r2 = (java.lang.String) r2;
        r5 = 0;
        r3 = new org.json.JSONObject;
        r3.<init>();
    L_0x00b3:
        if (r5 != 0) goto L_0x0169;
    L_0x00b5:
        r5 = r11.nextToken();
        r4 = r5;
    L_0x00ba:
        r8 = r4 instanceof java.lang.String;
        if (r8 == 0) goto L_0x00e2;
    L_0x00be:
        r4 = (java.lang.String) r4;
        r5 = r11.nextToken();
        r8 = EQ;
        if (r5 != r8) goto L_0x00dc;
    L_0x00c8:
        r5 = r11.nextToken();
        r8 = r5 instanceof java.lang.String;
        if (r8 != 0) goto L_0x00d7;
    L_0x00d0:
        r6 = "Missing value";
        r6 = r11.syntaxError(r6);
        throw r6;
    L_0x00d7:
        r3.accumulate(r4, r5);
        r5 = 0;
        goto L_0x00b3;
    L_0x00dc:
        r8 = "";
        r3.accumulate(r4, r8);
        goto L_0x00b3;
    L_0x00e2:
        r8 = SLASH;
        if (r4 != r8) goto L_0x00fa;
    L_0x00e6:
        r7 = r11.nextToken();
        r8 = GT;
        if (r7 == r8) goto L_0x00f5;
    L_0x00ee:
        r6 = "Misshaped tag";
        r6 = r11.syntaxError(r6);
        throw r6;
    L_0x00f5:
        r12.accumulate(r2, r3);
        goto L_0x0020;
    L_0x00fa:
        r8 = GT;
        if (r4 != r8) goto L_0x0162;
    L_0x00fe:
        r5 = r11.nextContent();
        if (r5 != 0) goto L_0x011a;
    L_0x0104:
        if (r13 == 0) goto L_0x0020;
    L_0x0106:
        r6 = new java.lang.StringBuilder;
        r7 = "Unclosed tag ";
        r6.<init>(r7);
        r6 = r6.append(r13);
        r6 = r6.toString();
        r6 = r11.syntaxError(r6);
        throw r6;
    L_0x011a:
        r8 = r5 instanceof java.lang.String;
        if (r8 == 0) goto L_0x012d;
    L_0x011e:
        r4 = r5;
        r4 = (java.lang.String) r4;
        r8 = r4.length();
        if (r8 <= 0) goto L_0x00fe;
    L_0x0127:
        r8 = "content";
        r3.accumulate(r8, r4);
        goto L_0x00fe;
    L_0x012d:
        r8 = LT;
        if (r5 != r8) goto L_0x00fe;
    L_0x0131:
        r8 = parse(r11, r3, r2);
        if (r8 == 0) goto L_0x00fe;
    L_0x0137:
        r8 = r3.length();
        if (r8 != 0) goto L_0x0144;
    L_0x013d:
        r7 = "";
        r12.accumulate(r2, r7);
        goto L_0x0020;
    L_0x0144:
        r8 = r3.length();
        if (r8 != r7) goto L_0x015d;
    L_0x014a:
        r7 = "content";
        r7 = r3.opt(r7);
        if (r7 == 0) goto L_0x015d;
    L_0x0152:
        r7 = "content";
        r7 = r3.opt(r7);
        r12.accumulate(r2, r7);
        goto L_0x0020;
    L_0x015d:
        r12.accumulate(r2, r3);
        goto L_0x0020;
    L_0x0162:
        r6 = "Misshaped tag";
        r6 = r11.syntaxError(r6);
        throw r6;
    L_0x0169:
        r4 = r5;
        goto L_0x00ba;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.json.XML.parse(org.json.XMLTokener, org.json.JSONObject, java.lang.String):boolean");
    }

    public static JSONObject toJSONObject(String string) throws JSONException {
        JSONObject o = new JSONObject();
        XMLTokener x = new XMLTokener(string);
        while (x.more()) {
            x.skipPast("<");
            parse(x, o, null);
        }
        return o;
    }

    public static String toString(Object o) throws JSONException {
        return toString(o, null);
    }

    public static String toString(Object o, String tagName) throws JSONException {
        StringBuffer b = new StringBuffer();
        String s;
        JSONArray ja;
        int len;
        int i;
        if (o instanceof JSONObject) {
            if (tagName != null) {
                b.append('<');
                b.append(tagName);
                b.append('>');
            }
            JSONObject jo = (JSONObject) o;
            Iterator keys = jo.keys();
            while (keys.hasNext()) {
                String k = keys.next().toString();
                String v = jo.get(k);
                if (v instanceof String) {
                    s = v;
                }
                if (k.equals("content")) {
                    if (v instanceof JSONArray) {
                        ja = (JSONArray) v;
                        len = ja.length();
                        for (i = 0; i < len; i++) {
                            if (i > 0) {
                                b.append('\n');
                            }
                            b.append(escape(ja.get(i).toString()));
                        }
                    } else {
                        b.append(escape(v.toString()));
                    }
                } else if (v instanceof JSONArray) {
                    ja = (JSONArray) v;
                    len = ja.length();
                    for (i = 0; i < len; i++) {
                        b.append(toString(ja.get(i), k));
                    }
                } else if (v.equals(XmlPullParser.NO_NAMESPACE)) {
                    b.append('<');
                    b.append(k);
                    b.append("/>");
                } else {
                    b.append(toString(v, k));
                }
            }
            if (tagName != null) {
                b.append("</");
                b.append(tagName);
                b.append('>');
            }
            return b.toString();
        } else if (o instanceof JSONArray) {
            ja = (JSONArray) o;
            len = ja.length();
            for (i = 0; i < len; i++) {
                String str;
                Object opt = ja.opt(i);
                if (tagName == null) {
                    str = "array";
                } else {
                    str = tagName;
                }
                b.append(toString(opt, str));
            }
            return b.toString();
        } else {
            s = o == null ? "null" : escape(o.toString());
            if (tagName == null) {
                return "\"" + s + "\"";
            }
            if (s.length() == 0) {
                return "<" + tagName + "/>";
            }
            return "<" + tagName + ">" + s + "</" + tagName + ">";
        }
    }
}
