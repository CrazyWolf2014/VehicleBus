package org.codehaus.jackson.org.objectweb.asm;

public class Attribute {
    Attribute f1673a;
    byte[] f1674b;
    public final String type;

    protected Attribute(String str) {
        this.type = str;
    }

    final int m1712a() {
        int i = 0;
        while (this != null) {
            i++;
            this = this.f1673a;
        }
        return i;
    }

    final int m1713a(ClassWriter classWriter, byte[] bArr, int i, int i2, int i3) {
        int i4 = 0;
        Attribute attribute = this;
        while (attribute != null) {
            classWriter.newUTF8(attribute.type);
            int i5 = (attribute.write(classWriter, bArr, i, i2, i3).f1676b + 6) + i4;
            attribute = attribute.f1673a;
            i4 = i5;
        }
        return i4;
    }

    final void m1714a(ClassWriter classWriter, byte[] bArr, int i, int i2, int i3, ByteVector byteVector) {
        for (Attribute attribute = this; attribute != null; attribute = attribute.f1673a) {
            ByteVector write = attribute.write(classWriter, bArr, i, i2, i3);
            byteVector.putShort(classWriter.newUTF8(attribute.type)).putInt(write.f1676b);
            byteVector.putByteArray(write.f1675a, 0, write.f1676b);
        }
    }

    protected Label[] getLabels() {
        return null;
    }

    public boolean isCodeAttribute() {
        return false;
    }

    public boolean isUnknown() {
        return true;
    }

    protected Attribute read(ClassReader classReader, int i, int i2, char[] cArr, int i3, Label[] labelArr) {
        Attribute attribute = new Attribute(this.type);
        attribute.f1674b = new byte[i2];
        System.arraycopy(classReader.f1678b, i, attribute.f1674b, 0, i2);
        return attribute;
    }

    protected ByteVector write(ClassWriter classWriter, byte[] bArr, int i, int i2, int i3) {
        ByteVector byteVector = new ByteVector();
        byteVector.f1675a = this.f1674b;
        byteVector.f1676b = this.f1674b.length;
        return byteVector;
    }
}
