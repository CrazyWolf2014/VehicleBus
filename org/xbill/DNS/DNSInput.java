package org.xbill.DNS;

public class DNSInput {
    private byte[] array;
    private int end;
    private int pos;
    private int saved_end;
    private int saved_pos;

    public DNSInput(byte[] bArr) {
        this.array = bArr;
        this.pos = 0;
        this.end = this.array.length;
        this.saved_pos = -1;
        this.saved_end = -1;
    }

    public int current() {
        return this.pos;
    }

    public int remaining() {
        return this.end - this.pos;
    }

    private void require(int i) throws WireParseException {
        if (i > remaining()) {
            throw new WireParseException("end of input");
        }
    }

    public void setActive(int i) {
        if (i > this.array.length - this.pos) {
            throw new IllegalArgumentException("cannot set active region past end of input");
        }
        this.end = this.pos + i;
    }

    public void clearActive() {
        this.end = this.array.length;
    }

    public int saveActive() {
        return this.end;
    }

    public void restoreActive(int i) {
        if (i > this.array.length) {
            throw new IllegalArgumentException("cannot set active region past end of input");
        }
        this.end = i;
    }

    public void jump(int i) {
        if (i >= this.array.length) {
            throw new IllegalArgumentException("cannot jump past end of input");
        }
        this.pos = i;
        this.end = this.array.length;
    }

    public void save() {
        this.saved_pos = this.pos;
        this.saved_end = this.end;
    }

    public void restore() {
        if (this.saved_pos < 0) {
            throw new IllegalStateException("no previous state");
        }
        this.pos = this.saved_pos;
        this.end = this.saved_end;
        this.saved_pos = -1;
        this.saved_end = -1;
    }

    public int readU8() throws WireParseException {
        require(1);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] & KEYRecord.PROTOCOL_ANY;
    }

    public int readU16() throws WireParseException {
        require(2);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        int i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
        byte[] bArr2 = this.array;
        int i3 = this.pos;
        this.pos = i3 + 1;
        return (i2 << 8) + (bArr2[i3] & KEYRecord.PROTOCOL_ANY);
    }

    public long readU32() throws WireParseException {
        require(4);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        int i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
        byte[] bArr2 = this.array;
        int i3 = this.pos;
        this.pos = i3 + 1;
        i = bArr2[i3] & KEYRecord.PROTOCOL_ANY;
        byte[] bArr3 = this.array;
        int i4 = this.pos;
        this.pos = i4 + 1;
        i3 = bArr3[i4] & KEYRecord.PROTOCOL_ANY;
        byte[] bArr4 = this.array;
        int i5 = this.pos;
        this.pos = i5 + 1;
        return ((((long) (i << 16)) + (((long) i2) << 24)) + ((long) (i3 << 8))) + ((long) (bArr4[i5] & KEYRecord.PROTOCOL_ANY));
    }

    public void readByteArray(byte[] bArr, int i, int i2) throws WireParseException {
        require(i2);
        System.arraycopy(this.array, this.pos, bArr, i, i2);
        this.pos += i2;
    }

    public byte[] readByteArray(int i) throws WireParseException {
        require(i);
        Object obj = new byte[i];
        System.arraycopy(this.array, this.pos, obj, 0, i);
        this.pos += i;
        return obj;
    }

    public byte[] readByteArray() {
        int remaining = remaining();
        Object obj = new byte[remaining];
        System.arraycopy(this.array, this.pos, obj, 0, remaining);
        this.pos = remaining + this.pos;
        return obj;
    }

    public byte[] readCountedString() throws WireParseException {
        require(1);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        return readByteArray(bArr[i] & KEYRecord.PROTOCOL_ANY);
    }
}
