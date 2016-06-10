package org.json;

import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class CDL {
    private static String getValue(JSONTokener x) throws JSONException {
        char c;
        do {
            c = x.next();
            if (c > ' ') {
                break;
            }
        } while (c != '\u0000');
        switch (c) {
            case KEYRecord.OWNER_USER /*0*/:
                return null;
            case Type.ATMA /*34*/:
            case Service.RLP /*39*/:
                return x.nextString(c);
            case Service.MPM_FLAGS /*44*/:
                x.back();
                return XmlPullParser.NO_NAMESPACE;
            default:
                x.back();
                return x.nextTo(',');
        }
    }

    public static JSONArray rowToJSONArray(JSONTokener x) throws JSONException {
        JSONArray ja = new JSONArray();
        loop0:
        while (true) {
            Object value = getValue(x);
            if (value == null) {
                return null;
            }
            ja.put(value);
            while (true) {
                char c = x.next();
                if (c != ',') {
                    if (c != ' ') {
                        break loop0;
                    }
                }
            }
        }
        if (c == '\n' || c == '\r' || c == '\u0000') {
            return ja;
        }
        throw x.syntaxError("Bad character '" + c + "' (" + c + ").");
    }

    public static JSONObject rowToJSONObject(JSONArray names, JSONTokener x) throws JSONException {
        JSONArray ja = rowToJSONArray(x);
        return ja != null ? ja.toJSONObject(names) : null;
    }

    public static JSONArray toJSONArray(String string) throws JSONException {
        return toJSONArray(new JSONTokener(string));
    }

    public static JSONArray toJSONArray(JSONTokener x) throws JSONException {
        return toJSONArray(rowToJSONArray(x), x);
    }

    public static JSONArray toJSONArray(JSONArray names, String string) throws JSONException {
        return toJSONArray(names, new JSONTokener(string));
    }

    public static JSONArray toJSONArray(JSONArray names, JSONTokener x) throws JSONException {
        if (names == null || names.length() == 0) {
            return null;
        }
        JSONArray ja = new JSONArray();
        while (true) {
            Object jo = rowToJSONObject(names, x);
            if (jo == null) {
                break;
            }
            ja.put(jo);
        }
        if (ja.length() == 0) {
            return null;
        }
        return ja;
    }

    public static String rowToString(JSONArray ja) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ja.length(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            Object o = ja.opt(i);
            if (o != null) {
                String s = o.toString();
                if (s.indexOf(44) < 0) {
                    sb.append(s);
                } else if (s.indexOf(34) >= 0) {
                    sb.append('\'');
                    sb.append(s);
                    sb.append('\'');
                } else {
                    sb.append('\"');
                    sb.append(s);
                    sb.append('\"');
                }
            }
        }
        sb.append('\n');
        return sb.toString();
    }

    public static String toString(JSONArray ja) throws JSONException {
        JSONObject jo = ja.optJSONObject(0);
        if (jo != null) {
            JSONArray names = jo.names();
            if (names != null) {
                return rowToString(names) + toString(names, ja);
            }
        }
        return null;
    }

    public static String toString(JSONArray names, JSONArray ja) throws JSONException {
        if (names == null || names.length() == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.optJSONObject(i);
            if (jo != null) {
                sb.append(rowToString(jo.toJSONArray(names)));
            }
        }
        return sb.toString();
    }
}
