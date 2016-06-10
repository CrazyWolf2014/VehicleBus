package org.xbill.DNS;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class ReverseMap {
    private static Name inaddr4;
    private static Name inaddr6;

    static {
        inaddr4 = Name.fromConstantString("in-addr.arpa.");
        inaddr6 = Name.fromConstantString("ip6.arpa.");
    }

    private ReverseMap() {
    }

    public static Name fromAddress(byte[] bArr) {
        if (bArr.length == 4 || bArr.length == 16) {
            StringBuffer stringBuffer = new StringBuffer();
            int length;
            if (bArr.length == 4) {
                for (length = bArr.length - 1; length >= 0; length--) {
                    stringBuffer.append(bArr[length] & KEYRecord.PROTOCOL_ANY);
                    if (length > 0) {
                        stringBuffer.append(".");
                    }
                }
            } else {
                int[] iArr = new int[2];
                for (int length2 = bArr.length - 1; length2 >= 0; length2--) {
                    iArr[0] = (bArr[length2] & KEYRecord.PROTOCOL_ANY) >> 4;
                    iArr[1] = (bArr[length2] & KEYRecord.PROTOCOL_ANY) & 15;
                    length = iArr.length - 1;
                    while (length >= 0) {
                        stringBuffer.append(Integer.toHexString(iArr[length]));
                        if (length2 > 0 || length > 0) {
                            stringBuffer.append(".");
                        }
                        length--;
                    }
                }
            }
            try {
                if (bArr.length == 4) {
                    return Name.fromString(stringBuffer.toString(), inaddr4);
                }
                return Name.fromString(stringBuffer.toString(), inaddr6);
            } catch (TextParseException e) {
                throw new IllegalStateException("name cannot be invalid");
            }
        }
        throw new IllegalArgumentException("array must contain 4 or 16 elements");
    }

    public static Name fromAddress(int[] iArr) {
        byte[] bArr = new byte[iArr.length];
        int i = 0;
        while (i < iArr.length) {
            if (iArr[i] < 0 || iArr[i] > 255) {
                throw new IllegalArgumentException("array must contain values between 0 and 255");
            }
            bArr[i] = (byte) iArr[i];
            i++;
        }
        return fromAddress(bArr);
    }

    public static Name fromAddress(InetAddress inetAddress) {
        return fromAddress(inetAddress.getAddress());
    }

    public static Name fromAddress(String str, int i) throws UnknownHostException {
        byte[] toByteArray = Address.toByteArray(str, i);
        if (toByteArray != null) {
            return fromAddress(toByteArray);
        }
        throw new UnknownHostException("Invalid IP address");
    }

    public static Name fromAddress(String str) throws UnknownHostException {
        byte[] toByteArray = Address.toByteArray(str, 1);
        if (toByteArray == null) {
            toByteArray = Address.toByteArray(str, 2);
        }
        if (toByteArray != null) {
            return fromAddress(toByteArray);
        }
        throw new UnknownHostException("Invalid IP address");
    }
}
