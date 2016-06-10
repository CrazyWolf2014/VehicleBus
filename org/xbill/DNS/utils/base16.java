package org.xbill.DNS.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.xbill.DNS.KEYRecord;

public class base16 {
    private static final String Base16 = "0123456789ABCDEF";

    private base16() {
    }

    public static String toString(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (byte b : bArr) {
            short s = (short) (b & KEYRecord.PROTOCOL_ANY);
            byte b2 = (byte) (s >> 4);
            byte b3 = (byte) (s & 15);
            byteArrayOutputStream.write(Base16.charAt(b2));
            byteArrayOutputStream.write(Base16.charAt(b3));
        }
        return new String(byteArrayOutputStream.toByteArray());
    }

    public static byte[] fromString(String str) {
        int i = 0;
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = str.getBytes();
        for (int i2 = 0; i2 < bytes.length; i2++) {
            if (!Character.isWhitespace((char) bytes[i2])) {
                byteArrayOutputStream.write(bytes[i2]);
            }
        }
        byte[] toByteArray = byteArrayOutputStream.toByteArray();
        if (toByteArray.length % 2 != 0) {
            return null;
        }
        byteArrayOutputStream.reset();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        while (i < toByteArray.length) {
            try {
                dataOutputStream.writeByte((((byte) Base16.indexOf(Character.toUpperCase((char) toByteArray[i]))) << 4) + ((byte) Base16.indexOf(Character.toUpperCase((char) toByteArray[i + 1]))));
            } catch (IOException e) {
            }
            i += 2;
        }
        return byteArrayOutputStream.toByteArray();
    }
}
