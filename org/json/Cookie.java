package org.json;

import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;

public class Cookie {
    public static String escape(String string) {
        String s = string.trim();
        StringBuffer sb = new StringBuffer();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c < ' ' || c == SignatureVisitor.EXTENDS || c == '%' || c == SignatureVisitor.INSTANCEOF || c == ';') {
                sb.append('%');
                sb.append(Character.forDigit((char) ((c >>> 4) & 15), 16));
                sb.append(Character.forDigit((char) (c & 15), 16));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static JSONObject toJSONObject(String string) throws JSONException {
        JSONObject o = new JSONObject();
        JSONTokener x = new JSONTokener(string);
        o.put("name", x.nextTo((char) SignatureVisitor.INSTANCEOF));
        x.next((char) SignatureVisitor.INSTANCEOF);
        o.put(SharedPref.VALUE, x.nextTo(';'));
        x.next();
        while (x.more()) {
            Object v;
            String n = unescape(x.nextTo("=;"));
            if (x.next() == SignatureVisitor.INSTANCEOF) {
                v = unescape(x.nextTo(';'));
                x.next();
            } else if (n.equals("secure")) {
                v = Boolean.TRUE;
            } else {
                throw x.syntaxError("Missing '=' in cookie parameter.");
            }
            o.put(n, v);
        }
        return o;
    }

    public static String toString(JSONObject o) throws JSONException {
        StringBuffer sb = new StringBuffer();
        sb.append(escape(o.getString("name")));
        sb.append("=");
        sb.append(escape(o.getString(SharedPref.VALUE)));
        if (o.has("expires")) {
            sb.append(";expires=");
            sb.append(o.getString("expires"));
        }
        if (o.has("domain")) {
            sb.append(";domain=");
            sb.append(escape(o.getString("domain")));
        }
        if (o.has("path")) {
            sb.append(";path=");
            sb.append(escape(o.getString("path")));
        }
        if (o.optBoolean("secure")) {
            sb.append(";secure");
        }
        return sb.toString();
    }

    public static String unescape(String s) {
        int len = s.length();
        StringBuffer b = new StringBuffer();
        int i = 0;
        while (i < len) {
            char c = s.charAt(i);
            if (c == SignatureVisitor.EXTENDS) {
                c = ' ';
            } else if (c == '%' && i + 2 < len) {
                int d = JSONTokener.dehexchar(s.charAt(i + 1));
                int e = JSONTokener.dehexchar(s.charAt(i + 2));
                if (d >= 0 && e >= 0) {
                    c = (char) ((d * 16) + e);
                    i += 2;
                }
            }
            b.append(c);
            i++;
        }
        return b.toString();
    }
}
