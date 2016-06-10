package org.kobjects.base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.json.HTTP;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.WKSRecord.Service;

public class Base64 {
    static final char[] charTab;

    static {
        charTab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    }

    public static String encode(byte[] data) {
        return encode(data, 0, data.length, null).toString();
    }

    public static StringBuffer encode(byte[] data, int start, int len, StringBuffer buf) {
        if (buf == null) {
            buf = new StringBuffer((data.length * 3) / 2);
        }
        int end = len - 3;
        int i = start;
        int n = 0;
        while (i <= end) {
            int d = (((data[i] & KEYRecord.PROTOCOL_ANY) << 16) | ((data[i + 1] & KEYRecord.PROTOCOL_ANY) << 8)) | (data[i + 2] & KEYRecord.PROTOCOL_ANY);
            buf.append(charTab[(d >> 18) & 63]);
            buf.append(charTab[(d >> 12) & 63]);
            buf.append(charTab[(d >> 6) & 63]);
            buf.append(charTab[d & 63]);
            i += 3;
            int n2 = n + 1;
            if (n >= 14) {
                n2 = 0;
                buf.append(HTTP.CRLF);
            }
            n = n2;
        }
        if (i == (start + len) - 2) {
            d = ((data[i] & KEYRecord.PROTOCOL_ANY) << 16) | ((data[i + 1] & KEYRecord.PROTOCOL_ANY) << 8);
            buf.append(charTab[(d >> 18) & 63]);
            buf.append(charTab[(d >> 12) & 63]);
            buf.append(charTab[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == (start + len) - 1) {
            d = (data[i] & KEYRecord.PROTOCOL_ANY) << 16;
            buf.append(charTab[(d >> 18) & 63]);
            buf.append(charTab[(d >> 12) & 63]);
            buf.append("==");
        }
        return buf;
    }

    static int decode(char c) {
        if (c >= 'A' && c <= 'Z') {
            return c - 65;
        }
        if (c >= 'a' && c <= 'z') {
            return (c - 97) + 26;
        }
        if (c >= '0' && c <= '9') {
            return ((c - 48) + 26) + 26;
        }
        switch (c) {
            case Service.NICNAME /*43*/:
                return 62;
            case Service.NI_FTP /*47*/:
                return 63;
            case Service.NI_MAIL /*61*/:
                return 0;
            default:
                throw new RuntimeException("unexpected code: " + c);
        }
    }

    public static byte[] decode(String s) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            decode(s, bos);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static void decode(String s, OutputStream os) throws IOException {
        int i = 0;
        int len = s.length();
        while (true) {
            if (i < len && s.charAt(i) <= ' ') {
                i++;
            } else if (i != len) {
                int tri = (((decode(s.charAt(i)) << 18) + (decode(s.charAt(i + 1)) << 12)) + (decode(s.charAt(i + 2)) << 6)) + decode(s.charAt(i + 3));
                os.write((tri >> 16) & KEYRecord.PROTOCOL_ANY);
                if (s.charAt(i + 2) != SignatureVisitor.INSTANCEOF) {
                    os.write((tri >> 8) & KEYRecord.PROTOCOL_ANY);
                    if (s.charAt(i + 3) != SignatureVisitor.INSTANCEOF) {
                        os.write(tri & KEYRecord.PROTOCOL_ANY);
                        i += 4;
                    } else {
                        return;
                    }
                }
                return;
            } else {
                return;
            }
        }
    }
}
