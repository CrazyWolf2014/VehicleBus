package com.amap.mapapi.core;

import org.xbill.DNS.KEYRecord;

/* renamed from: com.amap.mapapi.core.c */
public class Convert {
    public static int m478a(byte[] bArr, int i) {
        return ((((bArr[i + 3] & KEYRecord.PROTOCOL_ANY) << 24) + ((bArr[i + 2] & KEYRecord.PROTOCOL_ANY) << 16)) + ((bArr[i + 1] & KEYRecord.PROTOCOL_ANY) << 8)) + ((bArr[i + 0] & KEYRecord.PROTOCOL_ANY) << 0);
    }

    public static short m480b(byte[] bArr, int i) {
        return (short) (((bArr[i + 1] & KEYRecord.PROTOCOL_ANY) << 8) + ((bArr[i + 0] & KEYRecord.PROTOCOL_ANY) << 0));
    }

    public static void m479a(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        Object obj = new byte[i3];
        System.arraycopy(bArr, i, obj, 0, i3);
        System.arraycopy(obj, 0, bArr2, i2, i3);
    }
}
