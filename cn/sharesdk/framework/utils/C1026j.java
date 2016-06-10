package cn.sharesdk.framework.utils;

/* renamed from: cn.sharesdk.framework.utils.j */
public abstract class C1026j implements Escaper {
    private static final ThreadLocal<char[]> f1782a;

    static {
        f1782a = new C0066l();
    }

    private static final char[] m1853a(char[] cArr, int i, int i2) {
        Object obj = new char[i2];
        if (i > 0) {
            System.arraycopy(cArr, 0, obj, 0, i);
        }
        return obj;
    }

    protected static final int m1854b(CharSequence charSequence, int i, int i2) {
        if (i < i2) {
            int i3 = i + 1;
            char charAt = charSequence.charAt(i);
            if (charAt < '\ud800' || charAt > '\udfff') {
                return charAt;
            }
            if (charAt > '\udbff') {
                throw new IllegalArgumentException("Unexpected low surrogate character '" + charAt + "' with value " + charAt + " at index " + (i3 - 1));
            } else if (i3 == i2) {
                return -charAt;
            } else {
                char charAt2 = charSequence.charAt(i3);
                if (Character.isLowSurrogate(charAt2)) {
                    return Character.toCodePoint(charAt, charAt2);
                }
                throw new IllegalArgumentException("Expected low surrogate but got char '" + charAt2 + "' with value " + charAt2 + " at index " + i3);
            }
        }
        throw new IndexOutOfBoundsException("Index exceeds specified range");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected int m1855a(java.lang.CharSequence r3, int r4, int r5) {
        /*
        r2 = this;
    L_0x0000:
        if (r4 >= r5) goto L_0x000e;
    L_0x0002:
        r0 = cn.sharesdk.framework.utils.C1026j.m1854b(r3, r4, r5);
        if (r0 < 0) goto L_0x000e;
    L_0x0008:
        r1 = r2.m1857a(r0);
        if (r1 == 0) goto L_0x000f;
    L_0x000e:
        return r4;
    L_0x000f:
        r0 = java.lang.Character.isSupplementaryCodePoint(r0);
        if (r0 == 0) goto L_0x0018;
    L_0x0015:
        r0 = 2;
    L_0x0016:
        r4 = r4 + r0;
        goto L_0x0000;
    L_0x0018:
        r0 = 1;
        goto L_0x0016;
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.sharesdk.framework.utils.j.a(java.lang.CharSequence, int, int):int");
    }

    protected final String m1856a(String str, int i) {
        int b;
        int length = str.length();
        int i2 = 0;
        char[] cArr = (char[]) f1782a.get();
        int i3 = 0;
        while (i < length) {
            b = C1026j.m1854b(str, i, length);
            if (b < 0) {
                throw new IllegalArgumentException("Trailing high surrogate at end of input");
            }
            Object a = m1857a(b);
            if (a != null) {
                int i4 = i - i2;
                int length2 = (i3 + i4) + a.length;
                if (cArr.length < length2) {
                    cArr = C1026j.m1853a(cArr, i3, (length2 + (length - i)) + 32);
                }
                if (i4 > 0) {
                    str.getChars(i2, i, cArr, i3);
                    i3 += i4;
                }
                if (a.length > 0) {
                    System.arraycopy(a, 0, cArr, i3, a.length);
                    i3 += a.length;
                }
            }
            b = (Character.isSupplementaryCodePoint(b) ? 2 : 1) + i;
            i = m1855a((CharSequence) str, b, length);
            i2 = b;
        }
        b = length - i2;
        if (b > 0) {
            b += i3;
            if (cArr.length < b) {
                cArr = C1026j.m1853a(cArr, i3, b);
            }
            str.getChars(i2, length, cArr, i3);
            i3 = b;
        }
        return new String(cArr, 0, i3);
    }

    protected abstract char[] m1857a(int i);

    public Appendable escape(Appendable appendable) {
        C0063h.m236a(appendable);
        return new C0065k(this, appendable);
    }

    public String escape(String str) {
        int length = str.length();
        int a = m1855a((CharSequence) str, 0, length);
        return a == length ? str : m1856a(str, a);
    }
}
