package org.xbill.DNS.utils;

import org.xbill.DNS.KEYRecord;

public class hexdump {
    private static final char[] hex;

    static {
        hex = "0123456789ABCDEF".toCharArray();
    }

    public static String dump(String str, byte[] bArr, int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(i2 + "b");
        if (str != null) {
            stringBuffer.append(" (" + str + ")");
        }
        stringBuffer.append(':');
        int length = (stringBuffer.toString().length() + 8) & -8;
        stringBuffer.append('\t');
        int i3 = (80 - length) / 3;
        int i4 = 0;
        while (i4 < i2) {
            int i5;
            if (i4 != 0 && i4 % i3 == 0) {
                stringBuffer.append('\n');
                for (i5 = 0; i5 < length / 8; i5++) {
                    stringBuffer.append('\t');
                }
            }
            i5 = bArr[i4 + i] & KEYRecord.PROTOCOL_ANY;
            stringBuffer.append(hex[i5 >> 4]);
            stringBuffer.append(hex[i5 & 15]);
            stringBuffer.append(' ');
            i4++;
        }
        stringBuffer.append('\n');
        return stringBuffer.toString();
    }

    public static String dump(String str, byte[] bArr) {
        return dump(str, bArr, 0, bArr.length);
    }
}
