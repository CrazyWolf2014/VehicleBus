package org.codehaus.jackson.org.objectweb.asm;

import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.ksoap2.transport.ServiceConnection;
import org.xbill.DNS.KEYRecord.Flags;

final class FieldWriter implements FieldVisitor {
    FieldWriter f2285a;
    private final ClassWriter f2286b;
    private final int f2287c;
    private final int f2288d;
    private final int f2289e;
    private int f2290f;
    private int f2291g;
    private AnnotationWriter f2292h;
    private AnnotationWriter f2293i;
    private Attribute f2294j;

    FieldWriter(ClassWriter classWriter, int i, String str, String str2, String str3, Object obj) {
        if (classWriter.f2283y == null) {
            classWriter.f2283y = this;
        } else {
            classWriter.f2284z.f2285a = this;
        }
        classWriter.f2284z = this;
        this.f2286b = classWriter;
        this.f2287c = i;
        this.f2288d = classWriter.newUTF8(str);
        this.f2289e = classWriter.newUTF8(str2);
        if (str3 != null) {
            this.f2290f = classWriter.newUTF8(str3);
        }
        if (obj != null) {
            this.f2291g = classWriter.m2466a(obj).f1699a;
        }
    }

    int m2472a() {
        int a;
        int i = 8;
        if (this.f2291g != 0) {
            this.f2286b.newUTF8("ConstantValue");
            i = 16;
        }
        if ((this.f2287c & Flags.EXTEND) != 0 && ((this.f2286b.f2260b & InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) < 49 || (this.f2287c & ServiceConnection.DEFAULT_BUFFER_SIZE) != 0)) {
            this.f2286b.newUTF8("Synthetic");
            i += 6;
        }
        if ((this.f2287c & Opcodes.ACC_DEPRECATED) != 0) {
            this.f2286b.newUTF8("Deprecated");
            i += 6;
        }
        if (this.f2290f != 0) {
            this.f2286b.newUTF8("Signature");
            i += 8;
        }
        if (this.f2292h != null) {
            this.f2286b.newUTF8("RuntimeVisibleAnnotations");
            i += this.f2292h.m2453a() + 8;
        }
        if (this.f2293i != null) {
            this.f2286b.newUTF8("RuntimeInvisibleAnnotations");
            a = i + (this.f2293i.m2453a() + 8);
        } else {
            a = i;
        }
        return this.f2294j != null ? a + this.f2294j.m1713a(this.f2286b, null, 0, -1, -1) : a;
    }

    void m2473a(ByteVector byteVector) {
        byteVector.putShort(((393216 | ((this.f2287c & ServiceConnection.DEFAULT_BUFFER_SIZE) / 64)) ^ -1) & this.f2287c).putShort(this.f2288d).putShort(this.f2289e);
        int i = this.f2291g != 0 ? 1 : 0;
        if ((this.f2287c & Flags.EXTEND) != 0 && ((this.f2286b.f2260b & InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) < 49 || (this.f2287c & ServiceConnection.DEFAULT_BUFFER_SIZE) != 0)) {
            i++;
        }
        if ((this.f2287c & Opcodes.ACC_DEPRECATED) != 0) {
            i++;
        }
        if (this.f2290f != 0) {
            i++;
        }
        if (this.f2292h != null) {
            i++;
        }
        if (this.f2293i != null) {
            i++;
        }
        if (this.f2294j != null) {
            i += this.f2294j.m1712a();
        }
        byteVector.putShort(i);
        if (this.f2291g != 0) {
            byteVector.putShort(this.f2286b.newUTF8("ConstantValue"));
            byteVector.putInt(2).putShort(this.f2291g);
        }
        if ((this.f2287c & Flags.EXTEND) != 0 && ((this.f2286b.f2260b & InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) < 49 || (this.f2287c & ServiceConnection.DEFAULT_BUFFER_SIZE) != 0)) {
            byteVector.putShort(this.f2286b.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.f2287c & Opcodes.ACC_DEPRECATED) != 0) {
            byteVector.putShort(this.f2286b.newUTF8("Deprecated")).putInt(0);
        }
        if (this.f2290f != 0) {
            byteVector.putShort(this.f2286b.newUTF8("Signature"));
            byteVector.putInt(2).putShort(this.f2290f);
        }
        if (this.f2292h != null) {
            byteVector.putShort(this.f2286b.newUTF8("RuntimeVisibleAnnotations"));
            this.f2292h.m2454a(byteVector);
        }
        if (this.f2293i != null) {
            byteVector.putShort(this.f2286b.newUTF8("RuntimeInvisibleAnnotations"));
            this.f2293i.m2454a(byteVector);
        }
        if (this.f2294j != null) {
            this.f2294j.m1714a(this.f2286b, null, 0, -1, -1, byteVector);
        }
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.f2286b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.f2286b, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.f2248g = this.f2292h;
            this.f2292h = annotationWriter;
        } else {
            annotationWriter.f2248g = this.f2293i;
            this.f2293i = annotationWriter;
        }
        return annotationWriter;
    }

    public void visitAttribute(Attribute attribute) {
        attribute.f1673a = this.f2294j;
        this.f2294j = attribute;
    }

    public void visitEnd() {
    }
}
