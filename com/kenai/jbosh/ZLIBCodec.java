package com.kenai.jbosh;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

final class ZLIBCodec {
    private static final int BUFFER_SIZE = 512;

    private ZLIBCodec() {
    }

    public static String getID() {
        return "deflate";
    }

    public static byte[] encode(byte[] bArr) throws IOException {
        DeflaterOutputStream deflaterOutputStream;
        Throwable th;
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream);
            try {
                deflaterOutputStream.write(bArr);
                deflaterOutputStream.close();
                byteArrayOutputStream.close();
                byte[] toByteArray = byteArrayOutputStream.toByteArray();
                deflaterOutputStream.close();
                byteArrayOutputStream.close();
                return toByteArray;
            } catch (Throwable th2) {
                th = th2;
                deflaterOutputStream.close();
                byteArrayOutputStream.close();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            deflaterOutputStream = null;
            deflaterOutputStream.close();
            byteArrayOutputStream.close();
            throw th;
        }
    }

    public static byte[] decode(byte[] bArr) throws IOException {
        InflaterInputStream inflaterInputStream;
        Throwable th;
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inflaterInputStream = new InflaterInputStream(byteArrayInputStream);
            try {
                byte[] bArr2 = new byte[BUFFER_SIZE];
                int read;
                do {
                    read = inflaterInputStream.read(bArr2);
                    if (read > 0) {
                        byteArrayOutputStream.write(bArr2, 0, read);
                        continue;
                    }
                } while (read >= 0);
                bArr2 = byteArrayOutputStream.toByteArray();
                inflaterInputStream.close();
                byteArrayOutputStream.close();
                return bArr2;
            } catch (Throwable th2) {
                th = th2;
                inflaterInputStream.close();
                byteArrayOutputStream.close();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            inflaterInputStream = null;
            inflaterInputStream.close();
            byteArrayOutputStream.close();
            throw th;
        }
    }
}
