package com.tencent.mm.sdk.platformtools;

public class CharSequences {

    /* renamed from: com.tencent.mm.sdk.platformtools.CharSequences.1 */
    final class C08621 implements CharSequence {
        final /* synthetic */ byte[] f1626u;

        C08621(byte[] bArr) {
            this.f1626u = bArr;
        }

        public final char charAt(int i) {
            return (char) this.f1626u[i];
        }

        public final int length() {
            return this.f1626u.length;
        }

        public final CharSequence subSequence(int i, int i2) {
            return CharSequences.forAsciiBytes(this.f1626u, i, i2);
        }

        public final String toString() {
            return new String(this.f1626u);
        }
    }

    /* renamed from: com.tencent.mm.sdk.platformtools.CharSequences.2 */
    final class C08632 implements CharSequence {
        final /* synthetic */ byte[] f1627u;
        final /* synthetic */ int f1628v;
        final /* synthetic */ int f1629w;

        C08632(byte[] bArr, int i, int i2) {
            this.f1627u = bArr;
            this.f1628v = i;
            this.f1629w = i2;
        }

        public final char charAt(int i) {
            return (char) this.f1627u[this.f1628v + i];
        }

        public final int length() {
            return this.f1629w - this.f1628v;
        }

        public final CharSequence subSequence(int i, int i2) {
            int i3 = i - this.f1628v;
            int i4 = i2 - this.f1628v;
            CharSequences.m1644a(i3, i4, length());
            return CharSequences.forAsciiBytes(this.f1627u, i3, i4);
        }

        public final String toString() {
            return new String(this.f1627u, this.f1628v, length());
        }
    }

    static void m1644a(int i, int i2, int i3) {
        if (i < 0) {
            throw new IndexOutOfBoundsException();
        } else if (i2 < 0) {
            throw new IndexOutOfBoundsException();
        } else if (i2 > i3) {
            throw new IndexOutOfBoundsException();
        } else if (i > i2) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static int compareToIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        int i = 0;
        int length = charSequence.length();
        int length2 = charSequence2.length();
        int i2 = length < length2 ? length : length2;
        int i3 = 0;
        while (i3 < i2) {
            int i4 = i3 + 1;
            char toLowerCase = Character.toLowerCase(charSequence.charAt(i3));
            i3 = i + 1;
            i = toLowerCase - Character.toLowerCase(charSequence2.charAt(i));
            if (i != 0) {
                return i;
            }
            i = i3;
            i3 = i4;
        }
        return length - length2;
    }

    public static boolean equals(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence.length() != charSequence2.length()) {
            return false;
        }
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (charSequence.charAt(i) != charSequence2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static CharSequence forAsciiBytes(byte[] bArr) {
        return new C08621(bArr);
    }

    public static CharSequence forAsciiBytes(byte[] bArr, int i, int i2) {
        m1644a(i, i2, bArr.length);
        return new C08632(bArr, i, i2);
    }
}
