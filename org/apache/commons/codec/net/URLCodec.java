package org.apache.commons.codec.net;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.xbill.DNS.KEYRecord;

public class URLCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder {
    protected static byte ESCAPE_CHAR;
    protected static final BitSet WWW_FORM_URL;
    protected String charset;

    static {
        int i;
        ESCAPE_CHAR = (byte) 37;
        WWW_FORM_URL = new BitSet(KEYRecord.OWNER_ZONE);
        for (i = 97; i <= Opcodes.ISHR; i++) {
            WWW_FORM_URL.set(i);
        }
        for (i = 65; i <= 90; i++) {
            WWW_FORM_URL.set(i);
        }
        for (i = 48; i <= 57; i++) {
            WWW_FORM_URL.set(i);
        }
        WWW_FORM_URL.set(45);
        WWW_FORM_URL.set(95);
        WWW_FORM_URL.set(46);
        WWW_FORM_URL.set(42);
        WWW_FORM_URL.set(32);
    }

    public URLCodec() {
        this.charset = AsyncHttpResponseHandler.DEFAULT_CHARSET;
    }

    public URLCodec(String charset) {
        this.charset = AsyncHttpResponseHandler.DEFAULT_CHARSET;
        this.charset = charset;
    }

    public static final byte[] encodeUrl(BitSet urlsafe, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (urlsafe == null) {
            urlsafe = WWW_FORM_URL;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int b : bytes) {
            int b2;
            if (b2 < 0) {
                b2 += KEYRecord.OWNER_ZONE;
            }
            if (urlsafe.get(b2)) {
                if (b2 == 32) {
                    b2 = 43;
                }
                buffer.write(b2);
            } else {
                buffer.write(37);
                char hex1 = Character.toUpperCase(Character.forDigit((b2 >> 4) & 15, 16));
                char hex2 = Character.toUpperCase(Character.forDigit(b2 & 15, 16));
                buffer.write(hex1);
                buffer.write(hex2);
            }
        }
        return buffer.toByteArray();
    }

    public static final byte[] decodeUrl(byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int i = 0;
        while (i < bytes.length) {
            int b = bytes[i];
            if (b == 43) {
                buffer.write(32);
            } else if (b == 37) {
                i++;
                try {
                    int u = Character.digit((char) bytes[i], 16);
                    i++;
                    int l = Character.digit((char) bytes[i], 16);
                    if (u == -1 || l == -1) {
                        throw new DecoderException("Invalid URL encoding");
                    }
                    buffer.write((char) ((u << 4) + l));
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new DecoderException("Invalid URL encoding");
                }
            } else {
                buffer.write(b);
            }
            i++;
        }
        return buffer.toByteArray();
    }

    public byte[] encode(byte[] bytes) {
        return encodeUrl(WWW_FORM_URL, bytes);
    }

    public byte[] decode(byte[] bytes) throws DecoderException {
        return decodeUrl(bytes);
    }

    public String encode(String pString, String charset) throws UnsupportedEncodingException {
        if (pString == null) {
            return null;
        }
        return new String(encode(pString.getBytes(charset)), StringEncodings.US_ASCII);
    }

    public String encode(String pString) throws EncoderException {
        if (pString == null) {
            return null;
        }
        try {
            return encode(pString, getDefaultCharset());
        } catch (UnsupportedEncodingException e) {
            throw new EncoderException(e.getMessage());
        }
    }

    public String decode(String pString, String charset) throws DecoderException, UnsupportedEncodingException {
        if (pString == null) {
            return null;
        }
        return new String(decode(pString.getBytes(StringEncodings.US_ASCII)), charset);
    }

    public String decode(String pString) throws DecoderException {
        if (pString == null) {
            return null;
        }
        try {
            return decode(pString, getDefaultCharset());
        } catch (UnsupportedEncodingException e) {
            throw new DecoderException(e.getMessage());
        }
    }

    public Object encode(Object pObject) throws EncoderException {
        if (pObject == null) {
            return null;
        }
        if (pObject instanceof byte[]) {
            return encode((byte[]) pObject);
        }
        if (pObject instanceof String) {
            return encode((String) pObject);
        }
        throw new EncoderException(new StringBuffer().append("Objects of type ").append(pObject.getClass().getName()).append(" cannot be URL encoded").toString());
    }

    public Object decode(Object pObject) throws DecoderException {
        if (pObject == null) {
            return null;
        }
        if (pObject instanceof byte[]) {
            return decode((byte[]) pObject);
        }
        if (pObject instanceof String) {
            return decode((String) pObject);
        }
        throw new DecoderException(new StringBuffer().append("Objects of type ").append(pObject.getClass().getName()).append(" cannot be URL decoded").toString());
    }

    public String getEncoding() {
        return this.charset;
    }

    public String getDefaultCharset() {
        return this.charset;
    }
}
