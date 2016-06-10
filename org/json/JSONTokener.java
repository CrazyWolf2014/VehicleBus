package org.json;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FileOptions;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.ksoap2.SoapEnvelope;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class JSONTokener {
    private int myIndex;
    private String mySource;

    public JSONTokener(String s) {
        this.myIndex = 0;
        this.mySource = s;
    }

    public void back() {
        if (this.myIndex > 0) {
            this.myIndex--;
        }
    }

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 55;
        }
        if (c < 'a' || c > 'f') {
            return -1;
        }
        return c - 87;
    }

    public boolean more() {
        return this.myIndex < this.mySource.length();
    }

    public char next() {
        if (!more()) {
            return '\u0000';
        }
        char c = this.mySource.charAt(this.myIndex);
        this.myIndex++;
        return c;
    }

    public char next(char c) throws JSONException {
        char n = next();
        if (n == c) {
            return n;
        }
        throw syntaxError("Expected '" + c + "' and instead saw '" + n + "'.");
    }

    public String next(int n) throws JSONException {
        int i = this.myIndex;
        int j = i + n;
        if (j >= this.mySource.length()) {
            throw syntaxError("Substring bounds error");
        }
        this.myIndex += n;
        return this.mySource.substring(i, j);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public char nextClean() throws org.json.JSONException {
        /*
        r5 = this;
        r4 = 13;
        r3 = 10;
        r1 = 47;
    L_0x0006:
        r0 = r5.next();
        if (r0 != r1) goto L_0x003e;
    L_0x000c:
        r2 = r5.next();
        switch(r2) {
            case 42: goto L_0x0026;
            case 47: goto L_0x0018;
            default: goto L_0x0013;
        };
    L_0x0013:
        r5.back();
        r0 = r1;
    L_0x0017:
        return r0;
    L_0x0018:
        r0 = r5.next();
        if (r0 == r3) goto L_0x0006;
    L_0x001e:
        if (r0 == r4) goto L_0x0006;
    L_0x0020:
        if (r0 != 0) goto L_0x0018;
    L_0x0022:
        goto L_0x0006;
    L_0x0023:
        r5.back();
    L_0x0026:
        r0 = r5.next();
        if (r0 != 0) goto L_0x0033;
    L_0x002c:
        r1 = "Unclosed comment.";
        r1 = r5.syntaxError(r1);
        throw r1;
    L_0x0033:
        r2 = 42;
        if (r0 != r2) goto L_0x0026;
    L_0x0037:
        r2 = r5.next();
        if (r2 != r1) goto L_0x0023;
    L_0x003d:
        goto L_0x0006;
    L_0x003e:
        r2 = 35;
        if (r0 != r2) goto L_0x004d;
    L_0x0042:
        r0 = r5.next();
        if (r0 == r3) goto L_0x0006;
    L_0x0048:
        if (r0 == r4) goto L_0x0006;
    L_0x004a:
        if (r0 != 0) goto L_0x0042;
    L_0x004c:
        goto L_0x0006;
    L_0x004d:
        if (r0 == 0) goto L_0x0017;
    L_0x004f:
        r2 = 32;
        if (r0 <= r2) goto L_0x0006;
    L_0x0053:
        goto L_0x0017;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.json.JSONTokener.nextClean():char");
    }

    public String nextString(char quote) throws JSONException {
        StringBuffer sb = new StringBuffer();
        while (true) {
            char c = next();
            switch (c) {
                case KEYRecord.OWNER_USER /*0*/:
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                    throw syntaxError("Unterminated string");
                case Opcodes.DUP2 /*92*/:
                    c = next();
                    switch (c) {
                        case Service.TACNEWS /*98*/:
                            sb.append('\b');
                            break;
                        case Service.ISO_TSAP /*102*/:
                            sb.append('\f');
                            break;
                        case SoapEnvelope.VER11 /*110*/:
                            sb.append('\n');
                            break;
                        case Opcodes.FREM /*114*/:
                            sb.append('\r');
                            break;
                        case Opcodes.INEG /*116*/:
                            sb.append('\t');
                            break;
                        case Service.UUCP_PATH /*117*/:
                            sb.append((char) Integer.parseInt(next(4), 16));
                            break;
                        case SoapEnvelope.VER12 /*120*/:
                            sb.append((char) Integer.parseInt(next(2), 16));
                            break;
                        default:
                            sb.append(c);
                            break;
                    }
                default:
                    if (c != quote) {
                        sb.append(c);
                        break;
                    }
                    return sb.toString();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String nextTo(char r4) {
        /*
        r3 = this;
        r1 = new java.lang.StringBuffer;
        r1.<init>();
    L_0x0005:
        r0 = r3.next();
        if (r0 == r4) goto L_0x0015;
    L_0x000b:
        if (r0 == 0) goto L_0x0015;
    L_0x000d:
        r2 = 10;
        if (r0 == r2) goto L_0x0015;
    L_0x0011:
        r2 = 13;
        if (r0 != r2) goto L_0x0023;
    L_0x0015:
        if (r0 == 0) goto L_0x001a;
    L_0x0017:
        r3.back();
    L_0x001a:
        r2 = r1.toString();
        r2 = r2.trim();
        return r2;
    L_0x0023:
        r1.append(r0);
        goto L_0x0005;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.json.JSONTokener.nextTo(char):java.lang.String");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String nextTo(java.lang.String r4) {
        /*
        r3 = this;
        r1 = new java.lang.StringBuffer;
        r1.<init>();
    L_0x0005:
        r0 = r3.next();
        r2 = r4.indexOf(r0);
        if (r2 >= 0) goto L_0x0019;
    L_0x000f:
        if (r0 == 0) goto L_0x0019;
    L_0x0011:
        r2 = 10;
        if (r0 == r2) goto L_0x0019;
    L_0x0015:
        r2 = 13;
        if (r0 != r2) goto L_0x0027;
    L_0x0019:
        if (r0 == 0) goto L_0x001e;
    L_0x001b:
        r3.back();
    L_0x001e:
        r2 = r1.toString();
        r2 = r2.trim();
        return r2;
    L_0x0027:
        r1.append(r0);
        goto L_0x0005;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.json.JSONTokener.nextTo(java.lang.String):java.lang.String");
    }

    public Object nextValue() throws JSONException {
        char c = nextClean();
        switch (c) {
            case Type.ATMA /*34*/:
            case Service.RLP /*39*/:
                return nextString(c);
            case Service.MIT_DOV /*91*/:
                back();
                return new JSONArray(this);
            case Service.NTP /*123*/:
                back();
                return new JSONObject(this);
            default:
                StringBuffer sb = new StringBuffer();
                char b = c;
                while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
                    sb.append(c);
                    c = next();
                }
                back();
                Object s = sb.toString().trim();
                if (s.equals(XmlPullParser.NO_NAMESPACE)) {
                    throw syntaxError("Missing value.");
                } else if (s.equalsIgnoreCase("true")) {
                    return Boolean.TRUE;
                } else {
                    if (s.equalsIgnoreCase("false")) {
                        return Boolean.FALSE;
                    }
                    if (s.equalsIgnoreCase("null")) {
                        return JSONObject.NULL;
                    }
                    if ((b < '0' || b > '9') && b != '.' && b != SignatureVisitor.SUPER && b != SignatureVisitor.EXTENDS) {
                        return s;
                    }
                    if (b == '0') {
                        if (s.length() <= 2 || !(s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
                            try {
                                return new Integer(Integer.parseInt(s, 8));
                            } catch (Exception e) {
                            }
                        } else {
                            try {
                                return new Integer(Integer.parseInt(s.substring(2), 16));
                            } catch (Exception e2) {
                            }
                        }
                    }
                    try {
                        return new Integer(s);
                    } catch (Exception e3) {
                        try {
                            return new Long(s);
                        } catch (Exception e4) {
                            try {
                                return new Double(s);
                            } catch (Exception e5) {
                                return s;
                            }
                        }
                    }
                }
        }
    }

    public char skipTo(char to) {
        char c;
        int index = this.myIndex;
        do {
            c = next();
            if (c == '\u0000') {
                this.myIndex = index;
                break;
            }
        } while (c != to);
        back();
        return c;
    }

    public void skipPast(String to) {
        this.myIndex = this.mySource.indexOf(to, this.myIndex);
        if (this.myIndex < 0) {
            this.myIndex = this.mySource.length();
        } else {
            this.myIndex += to.length();
        }
    }

    public JSONException syntaxError(String message) {
        return new JSONException(new StringBuilder(String.valueOf(message)).append(toString()).toString());
    }

    public String toString() {
        return " at character " + this.myIndex + " of " + this.mySource;
    }
}
