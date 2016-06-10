package com.google.protobuf;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

public final class ByteString {
    public static final ByteString EMPTY;
    private final byte[] bytes;
    private volatile int hash;

    public static final class Output extends FilterOutputStream {
        private final ByteArrayOutputStream bout;

        private Output(ByteArrayOutputStream byteArrayOutputStream) {
            super(byteArrayOutputStream);
            this.bout = byteArrayOutputStream;
        }

        public ByteString toByteString() {
            return new ByteString(null);
        }
    }

    /* renamed from: com.google.protobuf.ByteString.a */
    static final class C0213a {
        private final CodedOutputStream f835a;
        private final byte[] f836b;

        private C0213a(int i) {
            this.f836b = new byte[i];
            this.f835a = CodedOutputStream.newInstance(this.f836b);
        }

        public ByteString m922a() {
            this.f835a.checkNoSpaceLeft();
            return new ByteString(null);
        }

        public CodedOutputStream m923b() {
            return this.f835a;
        }
    }

    private ByteString(byte[] bArr) {
        this.hash = 0;
        this.bytes = bArr;
    }

    public byte byteAt(int i) {
        return this.bytes[i];
    }

    public int size() {
        return this.bytes.length;
    }

    public boolean isEmpty() {
        return this.bytes.length == 0;
    }

    static {
        EMPTY = new ByteString(new byte[0]);
    }

    public static ByteString copyFrom(byte[] bArr, int i, int i2) {
        Object obj = new byte[i2];
        System.arraycopy(bArr, i, obj, 0, i2);
        return new ByteString(obj);
    }

    public static ByteString copyFrom(byte[] bArr) {
        return copyFrom(bArr, 0, bArr.length);
    }

    public static ByteString copyFrom(ByteBuffer byteBuffer, int i) {
        byte[] bArr = new byte[i];
        byteBuffer.get(bArr);
        return new ByteString(bArr);
    }

    public static ByteString copyFrom(ByteBuffer byteBuffer) {
        return copyFrom(byteBuffer, byteBuffer.remaining());
    }

    public static ByteString copyFrom(String str, String str2) throws UnsupportedEncodingException {
        return new ByteString(str.getBytes(str2));
    }

    public static ByteString copyFromUtf8(String str) {
        try {
            return new ByteString(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
        } catch (Throwable e) {
            throw new RuntimeException("UTF-8 not supported?", e);
        }
    }

    public static ByteString copyFrom(List<ByteString> list) {
        if (list.size() == 0) {
            return EMPTY;
        }
        if (list.size() == 1) {
            return (ByteString) list.get(0);
        }
        int i = 0;
        for (ByteString size : list) {
            i = size.size() + i;
        }
        Object obj = new byte[i];
        i = 0;
        for (ByteString size2 : list) {
            System.arraycopy(size2.bytes, 0, obj, i, size2.size());
            i = size2.size() + i;
        }
        return new ByteString(obj);
    }

    public void copyTo(byte[] bArr, int i) {
        System.arraycopy(this.bytes, 0, bArr, i, this.bytes.length);
    }

    public void copyTo(byte[] bArr, int i, int i2, int i3) {
        System.arraycopy(this.bytes, i, bArr, i2, i3);
    }

    public void copyTo(ByteBuffer byteBuffer) {
        byteBuffer.put(this.bytes, 0, this.bytes.length);
    }

    public byte[] toByteArray() {
        int length = this.bytes.length;
        Object obj = new byte[length];
        System.arraycopy(this.bytes, 0, obj, 0, length);
        return obj;
    }

    public ByteBuffer asReadOnlyByteBuffer() {
        return ByteBuffer.wrap(this.bytes).asReadOnlyBuffer();
    }

    public String toString(String str) throws UnsupportedEncodingException {
        return new String(this.bytes, str);
    }

    public String toStringUtf8() {
        try {
            return new String(this.bytes, AsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Throwable e) {
            throw new RuntimeException("UTF-8 not supported?", e);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ByteString)) {
            return false;
        }
        ByteString byteString = (ByteString) obj;
        int length = this.bytes.length;
        if (length != byteString.bytes.length) {
            return false;
        }
        byte[] bArr = this.bytes;
        byte[] bArr2 = byteString.bytes;
        for (int i = 0; i < length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = this.hash;
        if (i == 0) {
            byte[] bArr = this.bytes;
            int length = this.bytes.length;
            int i2 = 0;
            i = length;
            while (i2 < length) {
                int i3 = bArr[i2] + (i * 31);
                i2++;
                i = i3;
            }
            if (i == 0) {
                i = 1;
            }
            this.hash = i;
        }
        return i;
    }

    public InputStream newInput() {
        return new ByteArrayInputStream(this.bytes);
    }

    public CodedInputStream newCodedInput() {
        return CodedInputStream.newInstance(this.bytes);
    }

    public static Output newOutput(int i) {
        return new Output(null);
    }

    public static Output newOutput() {
        return newOutput(32);
    }

    static C0213a newCodedBuilder(int i) {
        return new C0213a(null);
    }
}
