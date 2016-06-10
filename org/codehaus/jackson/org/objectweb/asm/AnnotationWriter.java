package org.codehaus.jackson.org.objectweb.asm;

import org.xbill.DNS.WKSRecord.Service;

final class AnnotationWriter implements AnnotationVisitor {
    private final ClassWriter f2242a;
    private int f2243b;
    private final boolean f2244c;
    private final ByteVector f2245d;
    private final ByteVector f2246e;
    private final int f2247f;
    AnnotationWriter f2248g;
    AnnotationWriter f2249h;

    AnnotationWriter(ClassWriter classWriter, boolean z, ByteVector byteVector, ByteVector byteVector2, int i) {
        this.f2242a = classWriter;
        this.f2244c = z;
        this.f2245d = byteVector;
        this.f2246e = byteVector2;
        this.f2247f = i;
    }

    static void m2452a(AnnotationWriter[] annotationWriterArr, int i, ByteVector byteVector) {
        int length = ((annotationWriterArr.length - i) * 2) + 1;
        for (int i2 = i; i2 < annotationWriterArr.length; i2++) {
            length += annotationWriterArr[i2] == null ? 0 : annotationWriterArr[i2].m2453a();
        }
        byteVector.putInt(length).putByte(annotationWriterArr.length - i);
        while (i < annotationWriterArr.length) {
            AnnotationWriter annotationWriter = annotationWriterArr[i];
            AnnotationWriter annotationWriter2 = null;
            length = 0;
            while (annotationWriter != null) {
                length++;
                annotationWriter.visitEnd();
                annotationWriter.f2249h = annotationWriter2;
                AnnotationWriter annotationWriter3 = annotationWriter;
                annotationWriter = annotationWriter.f2248g;
                annotationWriter2 = annotationWriter3;
            }
            byteVector.putShort(length);
            while (annotationWriter2 != null) {
                byteVector.putByteArray(annotationWriter2.f2245d.f1675a, 0, annotationWriter2.f2245d.f1676b);
                annotationWriter2 = annotationWriter2.f2249h;
            }
            i++;
        }
    }

    int m2453a() {
        int i = 0;
        while (this != null) {
            i += this.f2245d.f1676b;
            this = this.f2248g;
        }
        return i;
    }

    void m2454a(ByteVector byteVector) {
        AnnotationWriter annotationWriter = null;
        int i = 2;
        int i2 = 0;
        for (AnnotationWriter annotationWriter2 = this; annotationWriter2 != null; annotationWriter2 = annotationWriter2.f2248g) {
            i2++;
            i += annotationWriter2.f2245d.f1676b;
            annotationWriter2.visitEnd();
            annotationWriter2.f2249h = annotationWriter;
            annotationWriter = annotationWriter2;
        }
        byteVector.putInt(i);
        byteVector.putShort(i2);
        while (annotationWriter != null) {
            byteVector.putByteArray(annotationWriter.f2245d.f1675a, 0, annotationWriter.f2245d.f1676b);
            annotationWriter = annotationWriter.f2249h;
        }
    }

    public void visit(String str, Object obj) {
        int i = 1;
        int i2 = 0;
        this.f2243b++;
        if (this.f2244c) {
            this.f2245d.putShort(this.f2242a.newUTF8(str));
        }
        if (obj instanceof String) {
            this.f2245d.m1717b(Service.SFTP, this.f2242a.newUTF8((String) obj));
        } else if (obj instanceof Byte) {
            this.f2245d.m1717b(66, this.f2242a.m2464a(((Byte) obj).byteValue()).f1699a);
        } else if (obj instanceof Boolean) {
            if (!((Boolean) obj).booleanValue()) {
                i = 0;
            }
            this.f2245d.m1717b(90, this.f2242a.m2464a(i).f1699a);
        } else if (obj instanceof Character) {
            this.f2245d.m1717b(67, this.f2242a.m2464a(((Character) obj).charValue()).f1699a);
        } else if (obj instanceof Short) {
            this.f2245d.m1717b(83, this.f2242a.m2464a(((Short) obj).shortValue()).f1699a);
        } else if (obj instanceof Type) {
            this.f2245d.m1717b(99, this.f2242a.newUTF8(((Type) obj).getDescriptor()));
        } else if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            this.f2245d.m1717b(91, bArr.length);
            while (i2 < bArr.length) {
                this.f2245d.m1717b(66, this.f2242a.m2464a(bArr[i2]).f1699a);
                i2++;
            }
        } else if (obj instanceof boolean[]) {
            boolean[] zArr = (boolean[]) obj;
            this.f2245d.m1717b(91, zArr.length);
            for (boolean z : zArr) {
                this.f2245d.m1717b(90, this.f2242a.m2464a(z ? 1 : 0).f1699a);
            }
        } else if (obj instanceof short[]) {
            short[] sArr = (short[]) obj;
            this.f2245d.m1717b(91, sArr.length);
            while (i2 < sArr.length) {
                this.f2245d.m1717b(83, this.f2242a.m2464a(sArr[i2]).f1699a);
                i2++;
            }
        } else if (obj instanceof char[]) {
            char[] cArr = (char[]) obj;
            this.f2245d.m1717b(91, cArr.length);
            while (i2 < cArr.length) {
                this.f2245d.m1717b(67, this.f2242a.m2464a(cArr[i2]).f1699a);
                i2++;
            }
        } else if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            this.f2245d.m1717b(91, iArr.length);
            while (i2 < iArr.length) {
                this.f2245d.m1717b(73, this.f2242a.m2464a(iArr[i2]).f1699a);
                i2++;
            }
        } else if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            this.f2245d.m1717b(91, jArr.length);
            while (i2 < jArr.length) {
                this.f2245d.m1717b(74, this.f2242a.m2465a(jArr[i2]).f1699a);
                i2++;
            }
        } else if (obj instanceof float[]) {
            float[] fArr = (float[]) obj;
            this.f2245d.m1717b(91, fArr.length);
            while (i2 < fArr.length) {
                this.f2245d.m1717b(70, this.f2242a.m2463a(fArr[i2]).f1699a);
                i2++;
            }
        } else if (obj instanceof double[]) {
            double[] dArr = (double[]) obj;
            this.f2245d.m1717b(91, dArr.length);
            while (i2 < dArr.length) {
                this.f2245d.m1717b(68, this.f2242a.m2462a(dArr[i2]).f1699a);
                i2++;
            }
        } else {
            Item a = this.f2242a.m2466a(obj);
            this.f2245d.m1717b(".s.IFJDCS".charAt(a.f1700b), a.f1699a);
        }
    }

    public AnnotationVisitor visitAnnotation(String str, String str2) {
        this.f2243b++;
        if (this.f2244c) {
            this.f2245d.putShort(this.f2242a.newUTF8(str));
        }
        this.f2245d.m1717b(64, this.f2242a.newUTF8(str2)).putShort(0);
        return new AnnotationWriter(this.f2242a, true, this.f2245d, this.f2245d, this.f2245d.f1676b - 2);
    }

    public AnnotationVisitor visitArray(String str) {
        this.f2243b++;
        if (this.f2244c) {
            this.f2245d.putShort(this.f2242a.newUTF8(str));
        }
        this.f2245d.m1717b(91, 0);
        return new AnnotationWriter(this.f2242a, false, this.f2245d, this.f2245d, this.f2245d.f1676b - 2);
    }

    public void visitEnd() {
        if (this.f2246e != null) {
            byte[] bArr = this.f2246e.f1675a;
            bArr[this.f2247f] = (byte) (this.f2243b >>> 8);
            bArr[this.f2247f + 1] = (byte) this.f2243b;
        }
    }

    public void visitEnum(String str, String str2, String str3) {
        this.f2243b++;
        if (this.f2244c) {
            this.f2245d.putShort(this.f2242a.newUTF8(str));
        }
        this.f2245d.m1717b(Service.HOSTNAME, this.f2242a.newUTF8(str2)).putShort(this.f2242a.newUTF8(str3));
    }
}
