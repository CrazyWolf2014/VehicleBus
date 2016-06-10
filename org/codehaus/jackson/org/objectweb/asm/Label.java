package org.codehaus.jackson.org.objectweb.asm;

import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;

public class Label {
    int f1708a;
    int f1709b;
    int f1710c;
    private int f1711d;
    private int[] f1712e;
    int f1713f;
    int f1714g;
    Frame f1715h;
    Label f1716i;
    public Object info;
    Edge f1717j;
    Label f1718k;

    private void m1746a(int i, int i2) {
        if (this.f1712e == null) {
            this.f1712e = new int[6];
        }
        if (this.f1711d >= this.f1712e.length) {
            Object obj = new int[(this.f1712e.length + 6)];
            System.arraycopy(this.f1712e, 0, obj, 0, this.f1712e.length);
            this.f1712e = obj;
        }
        int[] iArr = this.f1712e;
        int i3 = this.f1711d;
        this.f1711d = i3 + 1;
        iArr[i3] = i;
        iArr = this.f1712e;
        i3 = this.f1711d;
        this.f1711d = i3 + 1;
        iArr[i3] = i2;
    }

    Label m1747a() {
        return this.f1715h == null ? this : this.f1715h.f1685b;
    }

    void m1748a(long j, int i) {
        if ((this.f1708a & Flags.FLAG5) == 0) {
            this.f1708a |= Flags.FLAG5;
            this.f1712e = new int[(((i - 1) / 32) + 1)];
        }
        int[] iArr = this.f1712e;
        int i2 = (int) (j >>> 32);
        iArr[i2] = iArr[i2] | ((int) j);
    }

    void m1749a(MethodWriter methodWriter, ByteVector byteVector, int i, boolean z) {
        if ((this.f1708a & 2) == 0) {
            if (z) {
                m1746a(-1 - i, byteVector.f1676b);
                byteVector.putInt(-1);
                return;
            }
            m1746a(i, byteVector.f1676b);
            byteVector.putShort(-1);
        } else if (z) {
            byteVector.putInt(this.f1710c - i);
        } else {
            byteVector.putShort(this.f1710c - i);
        }
    }

    boolean m1750a(long j) {
        return ((this.f1708a & Flags.FLAG5) == 0 || (this.f1712e[(int) (j >>> 32)] & ((int) j)) == 0) ? false : true;
    }

    boolean m1751a(Label label) {
        if ((this.f1708a & Flags.FLAG5) == 0 || (label.f1708a & Flags.FLAG5) == 0) {
            return false;
        }
        for (int i = 0; i < this.f1712e.length; i++) {
            if ((this.f1712e[i] & label.f1712e[i]) != 0) {
                return true;
            }
        }
        return false;
    }

    boolean m1752a(MethodWriter methodWriter, int i, byte[] bArr) {
        int i2 = 0;
        this.f1708a |= 2;
        this.f1710c = i;
        boolean z = false;
        while (i2 < this.f1711d) {
            int i3 = i2 + 1;
            int i4 = this.f1712e[i2];
            i2 = i3 + 1;
            i3 = this.f1712e[i3];
            int i5;
            if (i4 >= 0) {
                i4 = i - i4;
                if (i4 < -32768 || i4 > 32767) {
                    int i6 = bArr[i3 - 1] & KEYRecord.PROTOCOL_ANY;
                    if (i6 <= Opcodes.JSR) {
                        bArr[i3 - 1] = (byte) (i6 + 49);
                    } else {
                        bArr[i3 - 1] = (byte) (i6 + 20);
                    }
                    z = true;
                }
                i5 = i3 + 1;
                bArr[i3] = (byte) (i4 >>> 8);
                bArr[i5] = (byte) i4;
            } else {
                i4 = (i4 + i) + 1;
                i5 = i3 + 1;
                bArr[i3] = (byte) (i4 >>> 24);
                i3 = i5 + 1;
                bArr[i5] = (byte) (i4 >>> 16);
                i5 = i3 + 1;
                bArr[i3] = (byte) (i4 >>> 8);
                bArr[i5] = (byte) i4;
            }
        }
        return z;
    }

    void m1753b(Label label, long j, int i) {
        while (this != null) {
            Label label2 = this.f1718k;
            this.f1718k = null;
            if (label != null) {
                if ((this.f1708a & Flags.FLAG4) != 0) {
                    this = label2;
                } else {
                    this.f1708a |= Flags.FLAG4;
                    if (!((this.f1708a & KEYRecord.OWNER_ZONE) == 0 || m1751a(label))) {
                        Edge edge = new Edge();
                        edge.f1681a = this.f1713f;
                        edge.f1682b = label.f1717j.f1682b;
                        edge.f1683c = this.f1717j;
                        this.f1717j = edge;
                    }
                }
            } else if (m1750a(j)) {
                this = label2;
            } else {
                m1748a(j, i);
            }
            Label label3 = label2;
            Edge edge2 = this.f1717j;
            while (edge2 != null) {
                if (((this.f1708a & Flags.FLAG8) == 0 || edge2 != this.f1717j.f1683c) && edge2.f1682b.f1718k == null) {
                    edge2.f1682b.f1718k = label3;
                    label3 = edge2.f1682b;
                }
                edge2 = edge2.f1683c;
            }
            this = label3;
        }
    }

    public int getOffset() {
        if ((this.f1708a & 2) != 0) {
            return this.f1710c;
        }
        throw new IllegalStateException("Label offset position has not been resolved yet");
    }

    public String toString() {
        return new StringBuffer().append("L").append(System.identityHashCode(this)).toString();
    }
}
