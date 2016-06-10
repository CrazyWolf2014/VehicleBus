package org.xbill.DNS;

public class DNSOutput {
    private byte[] array;
    private int pos;
    private int saved_pos;

    public DNSOutput(int i) {
        this.array = new byte[i];
        this.pos = 0;
        this.saved_pos = -1;
    }

    public DNSOutput() {
        this(32);
    }

    public int current() {
        return this.pos;
    }

    private void check(long j, int i) {
        long j2 = 1 << i;
        if (j < 0 || j > j2) {
            throw new IllegalArgumentException(j + " out of range for " + i + " bit value");
        }
    }

    private void need(int i) {
        if (this.array.length - this.pos < i) {
            int length = this.array.length * 2;
            if (length < this.pos + i) {
                length = this.pos + i;
            }
            Object obj = new byte[length];
            System.arraycopy(this.array, 0, obj, 0, this.pos);
            this.array = obj;
        }
    }

    public void jump(int i) {
        if (i > this.pos) {
            throw new IllegalArgumentException("cannot jump past end of data");
        }
        this.pos = i;
    }

    public void save() {
        this.saved_pos = this.pos;
    }

    public void restore() {
        if (this.saved_pos < 0) {
            throw new IllegalStateException("no previous state");
        }
        this.pos = this.saved_pos;
        this.saved_pos = -1;
    }

    public void writeU8(int i) {
        check((long) i, 8);
        need(1);
        byte[] bArr = this.array;
        int i2 = this.pos;
        this.pos = i2 + 1;
        bArr[i2] = (byte) (i & KEYRecord.PROTOCOL_ANY);
    }

    public void writeU16(int i) {
        check((long) i, 16);
        need(2);
        byte[] bArr = this.array;
        int i2 = this.pos;
        this.pos = i2 + 1;
        bArr[i2] = (byte) ((i >>> 8) & KEYRecord.PROTOCOL_ANY);
        bArr = this.array;
        i2 = this.pos;
        this.pos = i2 + 1;
        bArr[i2] = (byte) (i & KEYRecord.PROTOCOL_ANY);
    }

    public void writeU16At(int i, int i2) {
        check((long) i, 16);
        if (i2 > this.pos - 2) {
            throw new IllegalArgumentException("cannot write past end of data");
        }
        int i3 = i2 + 1;
        this.array[i2] = (byte) ((i >>> 8) & KEYRecord.PROTOCOL_ANY);
        int i4 = i3 + 1;
        this.array[i3] = (byte) (i & KEYRecord.PROTOCOL_ANY);
    }

    public void writeU32(long j) {
        check(j, 32);
        need(4);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) ((j >>> 24) & 255));
        bArr = this.array;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) ((j >>> 16) & 255));
        bArr = this.array;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) ((j >>> 8) & 255));
        bArr = this.array;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (j & 255));
    }

    public void writeByteArray(byte[] bArr, int i, int i2) {
        need(i2);
        System.arraycopy(bArr, i, this.array, this.pos, i2);
        this.pos += i2;
    }

    public void writeByteArray(byte[] bArr) {
        writeByteArray(bArr, 0, bArr.length);
    }

    public void writeCountedString(byte[] bArr) {
        if (bArr.length > KEYRecord.PROTOCOL_ANY) {
            throw new IllegalArgumentException("Invalid counted string");
        }
        need(bArr.length + 1);
        byte[] bArr2 = this.array;
        int i = this.pos;
        this.pos = i + 1;
        bArr2[i] = (byte) (bArr.length & KEYRecord.PROTOCOL_ANY);
        writeByteArray(bArr, 0, bArr.length);
    }

    public byte[] toByteArray() {
        Object obj = new byte[this.pos];
        System.arraycopy(this.array, 0, obj, 0, this.pos);
        return obj;
    }
}
