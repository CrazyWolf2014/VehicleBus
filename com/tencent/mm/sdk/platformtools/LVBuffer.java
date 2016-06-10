package com.tencent.mm.sdk.platformtools;

import java.nio.ByteBuffer;
import org.xmlpull.v1.XmlPullParser;

public class LVBuffer {
    public static final int LENGTH_ALLOC_PER_NEW = 4096;
    public static final int MAX_STRING_LENGTH = 2048;
    private ByteBuffer f1653U;
    private boolean f1654V;

    private int m1653b(int i) {
        if (this.f1653U.limit() - this.f1653U.position() <= i) {
            ByteBuffer allocate = ByteBuffer.allocate(this.f1653U.limit() + LENGTH_ALLOC_PER_NEW);
            allocate.put(this.f1653U.array(), 0, this.f1653U.position());
            this.f1653U = allocate;
        }
        return 0;
    }

    public byte[] buildFinish() {
        if (this.f1654V) {
            m1653b(1);
            this.f1653U.put((byte) 125);
            Object obj = new byte[this.f1653U.position()];
            System.arraycopy(this.f1653U.array(), 0, obj, 0, obj.length);
            return obj;
        }
        throw new Exception("Buffer For Parse");
    }

    public boolean checkGetFinish() {
        return this.f1653U.limit() - this.f1653U.position() <= 1;
    }

    public int getInt() {
        if (!this.f1654V) {
            return this.f1653U.getInt();
        }
        throw new Exception("Buffer For Build");
    }

    public long getLong() {
        if (!this.f1654V) {
            return this.f1653U.getLong();
        }
        throw new Exception("Buffer For Build");
    }

    public String getString() {
        if (this.f1654V) {
            throw new Exception("Buffer For Build");
        }
        short s = this.f1653U.getShort();
        if (s > (short) 2048) {
            this.f1653U = null;
            throw new Exception("Buffer String Length Error");
        } else if (s == (short) 0) {
            return XmlPullParser.NO_NAMESPACE;
        } else {
            byte[] bArr = new byte[s];
            this.f1653U.get(bArr, 0, s);
            return new String(bArr);
        }
    }

    public int initBuild() {
        this.f1653U = ByteBuffer.allocate(LENGTH_ALLOC_PER_NEW);
        this.f1653U.put((byte) 123);
        this.f1654V = true;
        return 0;
    }

    public int initParse(byte[] bArr) {
        boolean z = (bArr == null || bArr.length == 0) ? true : bArr[0] != 123 ? true : bArr[bArr.length + -1] != 125 ? true : false;
        if (z) {
            this.f1653U = null;
            return -1;
        }
        this.f1653U = ByteBuffer.wrap(bArr);
        this.f1653U.position(1);
        this.f1654V = false;
        return 0;
    }

    public int putInt(int i) {
        if (this.f1654V) {
            m1653b(4);
            this.f1653U.putInt(i);
            return 0;
        }
        throw new Exception("Buffer For Parse");
    }

    public int putLong(long j) {
        if (this.f1654V) {
            m1653b(8);
            this.f1653U.putLong(j);
            return 0;
        }
        throw new Exception("Buffer For Parse");
    }

    public int putString(String str) {
        if (this.f1654V) {
            byte[] bArr = null;
            if (str != null) {
                bArr = str.getBytes();
            }
            if (bArr == null) {
                bArr = new byte[0];
            }
            if (bArr.length > MAX_STRING_LENGTH) {
                throw new Exception("Buffer String Length Error");
            }
            m1653b(bArr.length + 2);
            this.f1653U.putShort((short) bArr.length);
            if (bArr.length > 0) {
                this.f1653U.put(bArr);
            }
            return 0;
        }
        throw new Exception("Buffer For Parse");
    }
}
