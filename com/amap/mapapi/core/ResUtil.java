package com.amap.mapapi.core;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.amap.mapapi.core.n */
public class ResUtil {
    public static byte[] m539a(Context context, String str) {
        try {
            InputStream open = context.getAssets().open(str);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[Flags.FLAG5];
            while (true) {
                int read = open.read(bArr);
                if (read > -1) {
                    byteArrayOutputStream.write(bArr, 0, read);
                } else {
                    bArr = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                    open.close();
                    return bArr;
                }
            }
        } catch (IOException e) {
            return null;
        }
    }
}
