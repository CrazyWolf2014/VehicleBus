package org.xbill.DNS;

public final class TTL {
    public static final long MAX_VALUE = 2147483647L;

    private TTL() {
    }

    static void check(long j) {
        if (j < 0 || j > MAX_VALUE) {
            throw new InvalidTTLException(j);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long parse(java.lang.String r10, boolean r11) {
        /*
        if (r10 == 0) goto L_0x0013;
    L_0x0002:
        r0 = r10.length();
        if (r0 == 0) goto L_0x0013;
    L_0x0008:
        r0 = 0;
        r0 = r10.charAt(r0);
        r0 = java.lang.Character.isDigit(r0);
        if (r0 != 0) goto L_0x0019;
    L_0x0013:
        r0 = new java.lang.NumberFormatException;
        r0.<init>();
        throw r0;
    L_0x0019:
        r3 = 0;
        r1 = 0;
        r0 = 0;
    L_0x001e:
        r5 = r10.length();
        if (r0 >= r5) goto L_0x0071;
    L_0x0024:
        r5 = r10.charAt(r0);
        r6 = java.lang.Character.isDigit(r5);
        if (r6 == 0) goto L_0x0042;
    L_0x002e:
        r6 = 10;
        r6 = r6 * r3;
        r5 = java.lang.Character.getNumericValue(r5);
        r8 = (long) r5;
        r5 = r6 + r8;
        r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1));
        if (r3 >= 0) goto L_0x006d;
    L_0x003c:
        r0 = new java.lang.NumberFormatException;
        r0.<init>();
        throw r0;
    L_0x0042:
        r5 = java.lang.Character.toUpperCase(r5);
        switch(r5) {
            case 68: goto L_0x0052;
            case 72: goto L_0x0055;
            case 77: goto L_0x0058;
            case 83: goto L_0x005b;
            case 87: goto L_0x004f;
            default: goto L_0x0049;
        };
    L_0x0049:
        r0 = new java.lang.NumberFormatException;
        r0.<init>();
        throw r0;
    L_0x004f:
        r5 = 7;
        r3 = r3 * r5;
    L_0x0052:
        r5 = 24;
        r3 = r3 * r5;
    L_0x0055:
        r5 = 60;
        r3 = r3 * r5;
    L_0x0058:
        r5 = 60;
        r3 = r3 * r5;
    L_0x005b:
        r1 = r1 + r3;
        r3 = 0;
        r5 = 4294967295; // 0xffffffff float:NaN double:2.1219957905E-314;
        r5 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1));
        if (r5 <= 0) goto L_0x006e;
    L_0x0067:
        r0 = new java.lang.NumberFormatException;
        r0.<init>();
        throw r0;
    L_0x006d:
        r3 = r5;
    L_0x006e:
        r0 = r0 + 1;
        goto L_0x001e;
    L_0x0071:
        r5 = 0;
        r0 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1));
        if (r0 != 0) goto L_0x0094;
    L_0x0077:
        r0 = r3;
    L_0x0078:
        r2 = 4294967295; // 0xffffffff float:NaN double:2.1219957905E-314;
        r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x0087;
    L_0x0081:
        r0 = new java.lang.NumberFormatException;
        r0.<init>();
        throw r0;
    L_0x0087:
        r2 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x0093;
    L_0x008e:
        if (r11 == 0) goto L_0x0093;
    L_0x0090:
        r0 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
    L_0x0093:
        return r0;
    L_0x0094:
        r0 = r1;
        goto L_0x0078;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.TTL.parse(java.lang.String, boolean):long");
    }

    public static long parseTTL(String str) {
        return parse(str, true);
    }

    public static String format(long j) {
        check(j);
        StringBuffer stringBuffer = new StringBuffer();
        long j2 = j % 60;
        long j3 = j / 60;
        long j4 = j3 % 60;
        j3 /= 60;
        long j5 = j3 % 24;
        j3 /= 24;
        long j6 = j3 % 7;
        j3 /= 7;
        if (j3 > 0) {
            stringBuffer.append(j3 + "W");
        }
        if (j6 > 0) {
            stringBuffer.append(j6 + "D");
        }
        if (j5 > 0) {
            stringBuffer.append(j5 + "H");
        }
        if (j4 > 0) {
            stringBuffer.append(j4 + "M");
        }
        if (j2 > 0 || (j3 == 0 && j6 == 0 && j5 == 0 && j4 == 0)) {
            stringBuffer.append(j2 + "S");
        }
        return stringBuffer.toString();
    }
}
