package com.kenai.jbosh;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

final class GZIPCodec {
    private static final int BUFFER_SIZE = 512;

    private GZIPCodec() {
    }

    public static String getID() {
        return "gzip";
    }

    public static byte[] encode(byte[] bArr) throws IOException {
        Throwable th;
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream;
        try {
            gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            try {
                gZIPOutputStream.write(bArr);
                gZIPOutputStream.close();
                byteArrayOutputStream.close();
                byte[] toByteArray = byteArrayOutputStream.toByteArray();
                gZIPOutputStream.close();
                byteArrayOutputStream.close();
                return toByteArray;
            } catch (Throwable th2) {
                th = th2;
                gZIPOutputStream.close();
                byteArrayOutputStream.close();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            gZIPOutputStream = null;
            gZIPOutputStream.close();
            byteArrayOutputStream.close();
            throw th;
        }
    }

    public static byte[] decode(byte[] bArr) throws IOException {
        Throwable th;
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPInputStream gZIPInputStream;
        try {
            gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            try {
                byte[] bArr2 = new byte[BUFFER_SIZE];
                int read;
                do {
                    read = gZIPInputStream.read(bArr2);
                    if (read > 0) {
                        byteArrayOutputStream.write(bArr2, 0, read);
                        continue;
                    }
                } while (read >= 0);
                bArr2 = byteArrayOutputStream.toByteArray();
                gZIPInputStream.close();
                byteArrayOutputStream.close();
                return bArr2;
            } catch (Throwable th2) {
                th = th2;
                gZIPInputStream.close();
                byteArrayOutputStream.close();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            gZIPInputStream = null;
            gZIPInputStream.close();
            byteArrayOutputStream.close();
            throw th;
        }
    }
}
