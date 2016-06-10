package com.amap.mapapi.map;

/* renamed from: com.amap.mapapi.map.f */
class BitUtil {
    BitUtil() {
    }

    public static int m819a(byte[] bArr, int i, int i2, boolean z) {
        int i3;
        int i4 = 0;
        while (i > 7) {
            i3 = i / 8;
            i -= i3 * 8;
            i4 = i3;
        }
        if (i2 + i < 8) {
            i3 = 1;
        } else if ((i + i2) % 8 == 0) {
            i3 = (i + i2) / 8;
        } else {
            i3 = ((i + i2) / 8) + 1;
        }
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArr, i4, bArr2, 0, i3);
        i3 = Integer.parseInt(BitUtil.m821a(bArr2).substring(i, i2 + i), 2);
        if (z) {
            return i3;
        }
        i4 = (int) Math.pow(2.0d, (double) i2);
        if (i3 > (i4 / 2) - 1) {
            return i3 - i4;
        }
        return i3;
    }

    public static StringBuilder m820a(byte b) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            stringBuilder.append((b >> (8 - (i + 1))) & 1);
        }
        stringBuilder.trimToSize();
        return stringBuilder;
    }

    public static StringBuilder m821a(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte a : bArr) {
            stringBuilder.append(BitUtil.m820a(a));
        }
        stringBuilder.trimToSize();
        return stringBuilder;
    }
}
