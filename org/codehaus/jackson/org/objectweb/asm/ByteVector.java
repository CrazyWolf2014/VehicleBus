package org.codehaus.jackson.org.objectweb.asm;

import org.codehaus.jackson.smile.SmileConstants;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.KEYRecord.Flags;

public class ByteVector {
    byte[] f1675a;
    int f1676b;

    public ByteVector() {
        this.f1675a = new byte[64];
    }

    public ByteVector(int i) {
        this.f1675a = new byte[i];
    }

    private void m1715a(int i) {
        int length = this.f1675a.length * 2;
        int i2 = this.f1676b + i;
        if (length <= i2) {
            length = i2;
        }
        Object obj = new byte[length];
        System.arraycopy(this.f1675a, 0, obj, 0, this.f1676b);
        this.f1675a = obj;
    }

    ByteVector m1716a(int i, int i2) {
        int i3 = this.f1676b;
        if (i3 + 2 > this.f1675a.length) {
            m1715a(2);
        }
        byte[] bArr = this.f1675a;
        int i4 = i3 + 1;
        bArr[i3] = (byte) i;
        i3 = i4 + 1;
        bArr[i4] = (byte) i2;
        this.f1676b = i3;
        return this;
    }

    ByteVector m1717b(int i, int i2) {
        int i3 = this.f1676b;
        if (i3 + 3 > this.f1675a.length) {
            m1715a(3);
        }
        byte[] bArr = this.f1675a;
        int i4 = i3 + 1;
        bArr[i3] = (byte) i;
        i3 = i4 + 1;
        bArr[i4] = (byte) (i2 >>> 8);
        i4 = i3 + 1;
        bArr[i3] = (byte) i2;
        this.f1676b = i4;
        return this;
    }

    public ByteVector putByte(int i) {
        int i2 = this.f1676b;
        if (i2 + 1 > this.f1675a.length) {
            m1715a(1);
        }
        int i3 = i2 + 1;
        this.f1675a[i2] = (byte) i;
        this.f1676b = i3;
        return this;
    }

    public ByteVector putByteArray(byte[] bArr, int i, int i2) {
        if (this.f1676b + i2 > this.f1675a.length) {
            m1715a(i2);
        }
        if (bArr != null) {
            System.arraycopy(bArr, i, this.f1675a, this.f1676b, i2);
        }
        this.f1676b += i2;
        return this;
    }

    public ByteVector putInt(int i) {
        int i2 = this.f1676b;
        if (i2 + 4 > this.f1675a.length) {
            m1715a(4);
        }
        byte[] bArr = this.f1675a;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 24);
        i2 = i3 + 1;
        bArr[i3] = (byte) (i >>> 16);
        i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) i;
        this.f1676b = i2;
        return this;
    }

    public ByteVector putLong(long j) {
        int i = this.f1676b;
        if (i + 8 > this.f1675a.length) {
            m1715a(8);
        }
        byte[] bArr = this.f1675a;
        int i2 = (int) (j >>> 32);
        int i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 24);
        i = i3 + 1;
        bArr[i3] = (byte) (i2 >>> 16);
        i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 8);
        i = i3 + 1;
        bArr[i3] = (byte) i2;
        i2 = (int) j;
        i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 24);
        i = i3 + 1;
        bArr[i3] = (byte) (i2 >>> 16);
        i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 8);
        i = i3 + 1;
        bArr[i3] = (byte) i2;
        this.f1676b = i;
        return this;
    }

    public ByteVector putShort(int i) {
        int i2 = this.f1676b;
        if (i2 + 2 > this.f1675a.length) {
            m1715a(2);
        }
        byte[] bArr = this.f1675a;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) i;
        this.f1676b = i2;
        return this;
    }

    public ByteVector putUTF8(String str) {
        int length = str.length();
        int i = this.f1676b;
        if ((i + 2) + length > this.f1675a.length) {
            m1715a(length + 2);
        }
        byte[] bArr = this.f1675a;
        int i2 = i + 1;
        bArr[i] = (byte) (length >>> 8);
        int i3 = i2 + 1;
        bArr[i2] = (byte) length;
        i2 = 0;
        while (i2 < length) {
            char charAt = str.charAt(i2);
            if (charAt < '\u0001' || charAt > '\u007f') {
                byte[] bArr2;
                i = i2;
                for (int i4 = i2; i4 < length; i4++) {
                    char charAt2 = str.charAt(i4);
                    i = (charAt2 < '\u0001' || charAt2 > '\u007f') ? charAt2 > '\u07ff' ? i + 3 : i + 2 : i + 1;
                }
                bArr[this.f1676b] = (byte) (i >>> 8);
                bArr[this.f1676b + 1] = (byte) i;
                if ((this.f1676b + 2) + i > bArr.length) {
                    this.f1676b = i3;
                    m1715a(i + 2);
                    bArr2 = this.f1675a;
                } else {
                    bArr2 = bArr;
                }
                while (i2 < length) {
                    int i5;
                    charAt = str.charAt(i2);
                    if (charAt >= '\u0001' && charAt <= '\u007f') {
                        i5 = i3 + 1;
                        bArr2[i3] = (byte) charAt;
                    } else if (charAt > '\u07ff') {
                        i5 = i3 + 1;
                        bArr2[i3] = (byte) (((charAt >> 12) & 15) | SmileConstants.TOKEN_PREFIX_MISC_OTHER);
                        i3 = i5 + 1;
                        bArr2[i5] = (byte) (((charAt >> 6) & 63) | Flags.FLAG8);
                        i5 = i3 + 1;
                        bArr2[i3] = (byte) ((charAt & 63) | Flags.FLAG8);
                    } else {
                        int i6 = i3 + 1;
                        bArr2[i3] = (byte) (((charAt >> 6) & 31) | Wbxml.EXT_0);
                        i5 = i6 + 1;
                        bArr2[i6] = (byte) ((charAt & 63) | Flags.FLAG8);
                    }
                    i2++;
                    i3 = i5;
                }
                i = i3;
                this.f1676b = i;
                return this;
            }
            i = i3 + 1;
            bArr[i3] = (byte) charAt;
            i2++;
            i3 = i;
        }
        i = i3;
        this.f1676b = i;
        return this;
    }
}
