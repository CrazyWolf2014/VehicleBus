package com.amap.mapapi.map;

import android.content.Context;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import com.amap.mapapi.map.ar.Tile;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.achartengine.renderer.DefaultRenderer;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.map.k */
class CachManager {
    private BitmapManager f689a;
    private String f690b;
    private final int f691c;

    public CachManager(Context context, boolean z, LayerPropertys layerPropertys) {
        this.f689a = null;
        this.f690b = "/sdcard/Amap/RMap";
        this.f691c = Flags.FLAG8;
        if (layerPropertys != null) {
            if (z) {
                this.f690b = context.getFilesDir().getPath();
            } else {
                boolean z2 = false;
                if (!layerPropertys.f1929l.equals(XmlPullParser.NO_NAMESPACE)) {
                    File file = new File(layerPropertys.f1929l);
                    z2 = file.exists();
                    if (!z2) {
                        z2 = file.mkdirs();
                    }
                }
                if (!z2) {
                    if (Environment.getExternalStorageState().equals("mounted")) {
                        this.f690b = Environment.getExternalStorageDirectory().getPath();
                    } else {
                        this.f690b = context.getFilesDir().getPath();
                    }
                }
            }
            this.f690b += "/Amap/" + layerPropertys.f1918a;
        }
    }

    private String[] m843a(int i, int i2, int i3, boolean z) {
        int i4 = i / Flags.FLAG8;
        int i5 = i2 / Flags.FLAG8;
        int i6 = i4 / 10;
        int i7 = i5 / 10;
        String[] strArr = new String[2];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.f690b);
        stringBuilder.append(FilePathGenerator.ANDROID_DIR_SEP);
        stringBuilder.append(i3);
        stringBuilder.append(FilePathGenerator.ANDROID_DIR_SEP);
        stringBuilder.append(i6);
        stringBuilder.append(FilePathGenerator.ANDROID_DIR_SEP);
        stringBuilder.append(i7);
        stringBuilder.append(FilePathGenerator.ANDROID_DIR_SEP);
        if (!z) {
            File file = new File(stringBuilder.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        stringBuilder.append(i3);
        stringBuilder.append("_");
        stringBuilder.append(i4);
        stringBuilder.append("_");
        stringBuilder.append(i5);
        strArr[0] = stringBuilder.toString() + ".idx";
        strArr[1] = stringBuilder.toString() + ".dat";
        return strArr;
    }

    public void m846a(BitmapManager bitmapManager) {
        this.f689a = bitmapManager;
    }

    private byte[] m842a(int i) {
        return new byte[]{(byte) (i & KEYRecord.PROTOCOL_ANY), (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) >> 8), (byte) ((16711680 & i) >> 16), (byte) ((DefaultRenderer.BACKGROUND_COLOR & i) >> 24)};
    }

    private void m841a(byte[] bArr) {
        if (bArr != null && bArr.length == 4) {
            byte b = bArr[0];
            bArr[0] = bArr[3];
            bArr[3] = b;
            b = bArr[1];
            bArr[1] = bArr[2];
            bArr[2] = b;
        }
    }

    private int m844b(byte[] bArr) {
        return (((bArr[0] & KEYRecord.PROTOCOL_ANY) | ((bArr[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK)) | ((bArr[2] << 16) & 16711680)) | ((bArr[3] << 24) & DefaultRenderer.BACKGROUND_COLOR);
    }

    private int m840a(int i, int i2) {
        return ((i % Flags.FLAG8) * Flags.FLAG8) + (i2 % Flags.FLAG8);
    }

    public int m845a(Tile tile) {
        Object a = m843a(tile.f630b, tile.f631c, tile.f632d, true);
        if (a == null || a.length != 2 || a[0].equals(XmlPullParser.NO_NAMESPACE) || a.equals(XmlPullParser.NO_NAMESPACE)) {
            return -1;
        }
        File file = new File(a[0]);
        if (!file.exists()) {
            return -1;
        }
        int a2 = m840a(tile.f630b, tile.f631c);
        if (a2 < 0) {
            return -1;
        }
        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            randomAccessFile = null;
        }
        if (randomAccessFile == null) {
            return -1;
        }
        try {
            randomAccessFile.seek((long) (a2 * 4));
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        byte[] bArr = new byte[4];
        try {
            randomAccessFile.read(bArr, 0, 4);
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        m841a(bArr);
        a2 = m844b(bArr);
        try {
            randomAccessFile.close();
        } catch (IOException e4) {
            e4.printStackTrace();
        }
        if (a2 < 0) {
            return -1;
        }
        File file2 = new File(a[1]);
        if (!file2.exists()) {
            return -1;
        }
        RandomAccessFile randomAccessFile2;
        try {
            randomAccessFile2 = new RandomAccessFile(file2, "r");
        } catch (FileNotFoundException e5) {
            e5.printStackTrace();
            randomAccessFile2 = null;
        }
        if (randomAccessFile2 == null) {
            return -1;
        }
        try {
            randomAccessFile2.seek((long) a2);
        } catch (IOException e42) {
            e42.printStackTrace();
        }
        try {
            randomAccessFile2.read(bArr, 0, 4);
        } catch (IOException e422) {
            e422.printStackTrace();
        }
        m841a(bArr);
        int b = m844b(bArr);
        if (b <= 0) {
            try {
                randomAccessFile2.close();
                return -1;
            } catch (IOException e4222) {
                e4222.printStackTrace();
                return -1;
            }
        }
        byte[] bArr2 = new byte[b];
        try {
            randomAccessFile2.read(bArr2, 0, b);
        } catch (IOException e22) {
            e22.printStackTrace();
        }
        try {
            randomAccessFile2.close();
        } catch (IOException e6) {
            e6.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tile.f630b);
        stringBuilder.append("-");
        stringBuilder.append(tile.f631c);
        stringBuilder.append("-");
        stringBuilder.append(tile.f632d);
        if (this.f689a == null) {
            return -1;
        }
        return this.f689a.m832a(bArr2, null, true, null, stringBuilder.toString());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean m847a(byte[] r12, int r13, int r14, int r15) {
        /*
        r11 = this;
        r3 = 0;
        r1 = 1;
        r4 = 0;
        r0 = 0;
        monitor-enter(r11);
        if (r12 != 0) goto L_0x000a;
    L_0x0008:
        monitor-exit(r11);
        return r0;
    L_0x000a:
        r6 = r12.length;	 Catch:{ all -> 0x00a2 }
        if (r6 <= 0) goto L_0x0008;
    L_0x000d:
        r2 = 0;
        r9 = r11.m843a(r13, r14, r15, r2);	 Catch:{ all -> 0x00a2 }
        if (r9 == 0) goto L_0x0008;
    L_0x0014:
        r2 = r9.length;	 Catch:{ all -> 0x00a2 }
        r7 = 2;
        if (r2 != r7) goto L_0x0008;
    L_0x0018:
        r2 = 0;
        r2 = r9[r2];	 Catch:{ all -> 0x00a2 }
        r7 = "";
        r2 = r2.equals(r7);	 Catch:{ all -> 0x00a2 }
        if (r2 != 0) goto L_0x0008;
    L_0x0023:
        r2 = "";
        r2 = r9.equals(r2);	 Catch:{ all -> 0x00a2 }
        if (r2 != 0) goto L_0x0008;
    L_0x002b:
        r7 = new java.io.File;	 Catch:{ all -> 0x00a2 }
        r2 = 1;
        r2 = r9[r2];	 Catch:{ all -> 0x00a2 }
        r7.<init>(r2);	 Catch:{ all -> 0x00a2 }
        r2 = r7.exists();	 Catch:{ all -> 0x00a2 }
        if (r2 != 0) goto L_0x003f;
    L_0x0039:
        r2 = r7.createNewFile();	 Catch:{ IOException -> 0x00a5 }
    L_0x003d:
        if (r2 == 0) goto L_0x0008;
    L_0x003f:
        r2 = new java.io.RandomAccessFile;	 Catch:{ FileNotFoundException -> 0x00ab }
        r8 = "rws";
        r2.<init>(r7, r8);	 Catch:{ FileNotFoundException -> 0x00ab }
    L_0x0046:
        if (r2 == 0) goto L_0x0008;
    L_0x0048:
        r10 = r11.m842a(r6);	 Catch:{ all -> 0x00a2 }
        r11.m841a(r10);	 Catch:{ all -> 0x00a2 }
        r6 = r2.length();	 Catch:{ IOException -> 0x00b1 }
        r7 = r6;
    L_0x0054:
        r2.seek(r7);	 Catch:{ IOException -> 0x00b7 }
    L_0x0057:
        r2.write(r10);	 Catch:{ IOException -> 0x00bc }
    L_0x005a:
        r2.write(r12);	 Catch:{ IOException -> 0x00c1 }
    L_0x005d:
        r2.close();	 Catch:{ IOException -> 0x00c6 }
    L_0x0060:
        r6 = new java.io.File;	 Catch:{ all -> 0x00a2 }
        r2 = 0;
        r2 = r9[r2];	 Catch:{ all -> 0x00a2 }
        r6.<init>(r2);	 Catch:{ all -> 0x00a2 }
        r2 = r6.exists();	 Catch:{ all -> 0x00a2 }
        if (r2 != 0) goto L_0x0074;
    L_0x006e:
        r2 = r6.createNewFile();	 Catch:{ IOException -> 0x00cb }
    L_0x0072:
        if (r2 == 0) goto L_0x0008;
    L_0x0074:
        r2 = new java.io.RandomAccessFile;	 Catch:{ FileNotFoundException -> 0x00d1 }
        r9 = "rws";
        r2.<init>(r6, r9);	 Catch:{ FileNotFoundException -> 0x00d1 }
        r6 = r2;
    L_0x007c:
        if (r6 == 0) goto L_0x0008;
    L_0x007e:
        r2 = r6.length();	 Catch:{ IOException -> 0x00d7 }
    L_0x0082:
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 != 0) goto L_0x0091;
    L_0x0086:
        r2 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r2 = new byte[r2];	 Catch:{ all -> 0x00a2 }
        r3 = -1;
        java.util.Arrays.fill(r2, r3);	 Catch:{ all -> 0x00a2 }
        r6.write(r2);	 Catch:{ IOException -> 0x00dd }
    L_0x0091:
        r2 = r11.m840a(r13, r14);	 Catch:{ all -> 0x00a2 }
        if (r2 >= 0) goto L_0x00e2;
    L_0x0097:
        r6.close();	 Catch:{ IOException -> 0x009c }
        goto L_0x0008;
    L_0x009c:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ all -> 0x00a2 }
        goto L_0x0008;
    L_0x00a2:
        r0 = move-exception;
        monitor-exit(r11);
        throw r0;
    L_0x00a5:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x00a2 }
        r2 = r0;
        goto L_0x003d;
    L_0x00ab:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x00a2 }
        r2 = r3;
        goto L_0x0046;
    L_0x00b1:
        r6 = move-exception;
        r6.printStackTrace();	 Catch:{ all -> 0x00a2 }
        r7 = r4;
        goto L_0x0054;
    L_0x00b7:
        r6 = move-exception;
        r6.printStackTrace();	 Catch:{ all -> 0x00a2 }
        goto L_0x0057;
    L_0x00bc:
        r6 = move-exception;
        r6.printStackTrace();	 Catch:{ all -> 0x00a2 }
        goto L_0x005a;
    L_0x00c1:
        r6 = move-exception;
        r6.printStackTrace();	 Catch:{ all -> 0x00a2 }
        goto L_0x005d;
    L_0x00c6:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x00a2 }
        goto L_0x0060;
    L_0x00cb:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x00a2 }
        r2 = r0;
        goto L_0x0072;
    L_0x00d1:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x00a2 }
        r6 = r3;
        goto L_0x007c;
    L_0x00d7:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x00a2 }
        r2 = r4;
        goto L_0x0082;
    L_0x00dd:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x00a2 }
        goto L_0x0091;
    L_0x00e2:
        r0 = r2 * 4;
        r2 = (long) r0;
        r6.seek(r2);	 Catch:{ IOException -> 0x00f9 }
    L_0x00e8:
        r0 = (int) r7;
        r0 = r11.m842a(r0);	 Catch:{ all -> 0x00a2 }
        r11.m841a(r0);	 Catch:{ all -> 0x00a2 }
        r6.write(r0);	 Catch:{ IOException -> 0x00fe }
    L_0x00f3:
        r6.close();	 Catch:{ IOException -> 0x0103 }
    L_0x00f6:
        r0 = r1;
        goto L_0x0008;
    L_0x00f9:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x00a2 }
        goto L_0x00e8;
    L_0x00fe:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x00a2 }
        goto L_0x00f3;
    L_0x0103:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x00a2 }
        goto L_0x00f6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.mapapi.map.k.a(byte[], int, int, int):boolean");
    }
}
