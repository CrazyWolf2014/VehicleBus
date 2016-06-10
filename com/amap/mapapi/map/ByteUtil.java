package com.amap.mapapi.map;

import android.support.v4.view.MotionEventCompat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.achartengine.renderer.DefaultRenderer;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.amap.mapapi.map.j */
public class ByteUtil {
    public static int f688a;

    static {
        f688a = Flags.FLAG4;
    }

    public static void m836a(byte[] bArr) {
        if (bArr != null && bArr.length == 4) {
            byte b = bArr[0];
            bArr[0] = bArr[3];
            bArr[3] = b;
            b = bArr[1];
            bArr[1] = bArr[2];
            bArr[2] = b;
        }
    }

    public static int m839b(byte[] bArr) {
        return (((bArr[0] & KEYRecord.PROTOCOL_ANY) | ((bArr[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK)) | ((bArr[2] << 16) & 16711680)) | ((bArr[3] << 24) & DefaultRenderer.BACKGROUND_COLOR);
    }

    public static byte[] m837a(int i) {
        return new byte[]{(byte) (i & KEYRecord.PROTOCOL_ANY), (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) >> 8), (byte) ((16711680 & i) >> 16), (byte) ((DefaultRenderer.BACKGROUND_COLOR & i) >> 24)};
    }

    public static byte[] m838a(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[f688a];
        while (true) {
            int read = inputStream.read(bArr, 0, f688a);
            if (read == -1) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }
}
