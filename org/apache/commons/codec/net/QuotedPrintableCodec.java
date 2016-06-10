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
import org.codehaus.jackson.smile.SmileConstants;
import org.xbill.DNS.KEYRecord;

public class QuotedPrintableCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder {
    private static byte ESCAPE_CHAR;
    private static final BitSet PRINTABLE_CHARS;
    private static byte SPACE;
    private static byte TAB;
    private String charset;

    static {
        int i;
        PRINTABLE_CHARS = new BitSet(KEYRecord.OWNER_ZONE);
        ESCAPE_CHAR = (byte) 61;
        TAB = (byte) 9;
        SPACE = SmileConstants.TOKEN_LITERAL_EMPTY_STRING;
        for (i = 33; i <= 60; i++) {
            PRINTABLE_CHARS.set(i);
        }
        for (i = 62; i <= Opcodes.IAND; i++) {
            PRINTABLE_CHARS.set(i);
        }
        PRINTABLE_CHARS.set(TAB);
        PRINTABLE_CHARS.set(SPACE);
    }

    public QuotedPrintableCodec() {
        this.charset = AsyncHttpResponseHandler.DEFAULT_CHARSET;
    }

    public QuotedPrintableCodec(String charset) {
        this.charset = AsyncHttpResponseHandler.DEFAULT_CHARSET;
        this.charset = charset;
    }

    private static final void encodeQuotedPrintable(int b, ByteArrayOutputStream buffer) {
        buffer.write(ESCAPE_CHAR);
        char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 15, 16));
        char hex2 = Character.toUpperCase(Character.forDigit(b & 15, 16));
        buffer.write(hex1);
        buffer.write(hex2);
    }

    public static final byte[] encodeQuotedPrintable(BitSet printable, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (printable == null) {
            printable = PRINTABLE_CHARS;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int b : bytes) {
            int b2;
            if (b2 < 0) {
                b2 += KEYRecord.OWNER_ZONE;
            }
            if (printable.get(b2)) {
                buffer.write(b2);
            } else {
                encodeQuotedPrintable(b2, buffer);
            }
        }
        return buffer.toByteArray();
    }

    public static final byte[] decodeQuotedPrintable(byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int i = 0;
        while (i < bytes.length) {
            byte b = bytes[i];
            if (b == ESCAPE_CHAR) {
                i++;
                int u = Character.digit((char) bytes[i], 16);
                i++;
                int l = Character.digit((char) bytes[i], 16);
                if (u == -1 || l == -1) {
                    throw new DecoderException("Invalid quoted-printable encoding");
                }
                try {
                    buffer.write((char) ((u << 4) + l));
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new DecoderException("Invalid quoted-printable encoding");
                }
            }
            buffer.write(b);
            i++;
        }
        return buffer.toByteArray();
    }

    public byte[] encode(byte[] bytes) {
        return encodeQuotedPrintable(PRINTABLE_CHARS, bytes);
    }

    public byte[] decode(byte[] bytes) throws DecoderException {
        return decodeQuotedPrintable(bytes);
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
        throw new EncoderException(new StringBuffer().append("Objects of type ").append(pObject.getClass().getName()).append(" cannot be quoted-printable encoded").toString());
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
        throw new DecoderException(new StringBuffer().append("Objects of type ").append(pObject.getClass().getName()).append(" cannot be quoted-printable decoded").toString());
    }

    public String getDefaultCharset() {
        return this.charset;
    }

    public String encode(String pString, String charset) throws UnsupportedEncodingException {
        if (pString == null) {
            return null;
        }
        return new String(encode(pString.getBytes(charset)), StringEncodings.US_ASCII);
    }
}
