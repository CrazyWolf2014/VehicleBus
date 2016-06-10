package cn.sharesdk.framework.utils;

import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.WKSRecord.Service;

/* renamed from: cn.sharesdk.framework.utils.g */
public class C1224g extends C1026j {
    private static final char[] f2350a;
    private static final char[] f2351b;
    private final boolean f2352c;
    private final boolean[] f2353d;

    static {
        f2350a = new char[]{SignatureVisitor.EXTENDS};
        f2351b = "0123456789ABCDEF".toCharArray();
    }

    public C1224g(String str, boolean z) {
        if (str.matches(".*[0-9A-Za-z].*")) {
            throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
        } else if (z && str.contains(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR)) {
            throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
        } else if (str.contains("%")) {
            throw new IllegalArgumentException("The '%' character cannot be specified as 'safe'");
        } else {
            this.f2352c = z;
            this.f2353d = C1224g.m2501a(str);
        }
    }

    private static boolean[] m2501a(String str) {
        int i;
        int i2 = 0;
        char[] toCharArray = str.toCharArray();
        int i3 = Opcodes.ISHR;
        for (char max : toCharArray) {
            i3 = Math.max(max, i3);
        }
        boolean[] zArr = new boolean[(i3 + 1)];
        for (i = 48; i <= 57; i++) {
            zArr[i] = true;
        }
        for (i = 65; i <= 90; i++) {
            zArr[i] = true;
        }
        for (i = 97; i <= Opcodes.ISHR; i++) {
            zArr[i] = true;
        }
        i = toCharArray.length;
        while (i2 < i) {
            zArr[toCharArray[i2]] = true;
            i2++;
        }
        return zArr;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected int m2502a(java.lang.CharSequence r3, int r4, int r5) {
        /*
        r2 = this;
    L_0x0000:
        if (r4 >= r5) goto L_0x0011;
    L_0x0002:
        r0 = r3.charAt(r4);
        r1 = r2.f2353d;
        r1 = r1.length;
        if (r0 >= r1) goto L_0x0011;
    L_0x000b:
        r1 = r2.f2353d;
        r0 = r1[r0];
        if (r0 != 0) goto L_0x0012;
    L_0x0011:
        return r4;
    L_0x0012:
        r4 = r4 + 1;
        goto L_0x0000;
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.sharesdk.framework.utils.g.a(java.lang.CharSequence, int, int):int");
    }

    protected char[] m2503a(int i) {
        if (i < this.f2353d.length && this.f2353d[i]) {
            return null;
        }
        if (i == 32 && this.f2352c) {
            return f2350a;
        }
        if (i <= Service.LOCUS_CON) {
            return new char[]{'%', f2351b[i & 15], f2351b[i >>> 4]};
        } else if (i <= 2047) {
            r0 = new char[6];
            r0[0] = '%';
            r0[3] = '%';
            r0[5] = f2351b[i & 15];
            r1 = i >>> 4;
            r0[4] = f2351b[(r1 & 3) | 8];
            r1 >>>= 2;
            r0[2] = f2351b[r1 & 15];
            r0[1] = f2351b[(r1 >>> 4) | 12];
            return r0;
        } else if (i <= InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) {
            r0 = new char[9];
            r1 = i >>> 4;
            r0[7] = f2351b[(r1 & 3) | 8];
            r1 >>>= 2;
            r0[5] = f2351b[r1 & 15];
            r1 >>>= 4;
            r0[4] = f2351b[(r1 & 3) | 8];
            r0[2] = f2351b[r1 >>> 2];
            return r0;
        } else if (i <= 1114111) {
            r0 = new char[12];
            r1 = i >>> 4;
            r0[10] = f2351b[(r1 & 3) | 8];
            r1 >>>= 2;
            r0[8] = f2351b[r1 & 15];
            r1 >>>= 4;
            r0[7] = f2351b[(r1 & 3) | 8];
            r1 >>>= 2;
            r0[5] = f2351b[r1 & 15];
            r1 >>>= 4;
            r0[4] = f2351b[(r1 & 3) | 8];
            r0[2] = f2351b[(r1 >>> 2) & 7];
            return r0;
        } else {
            throw new IllegalArgumentException("Invalid unicode character value " + i);
        }
    }

    public String escape(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt >= this.f2353d.length || !this.f2353d[charAt]) {
                return m1856a(str, i);
            }
        }
        return str;
    }
}
