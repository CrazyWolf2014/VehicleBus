package com.amap.mapapi.map;

import com.amap.mapapi.core.Convert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import org.ksoap2.transport.ServiceConnection;
import org.xbill.DNS.KEYRecord.Flags;

/* compiled from: MapLoader */
class aa {
    MapView f520a;
    public ArrayList<String> f521b;
    boolean f522c;
    long f523d;
    int f524e;
    byte[] f525f;
    int f526g;
    int f527h;
    boolean f528i;
    boolean f529j;

    public String m665a() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.f521b.size(); i++) {
            stringBuffer.append(((String) this.f521b.get(i)) + ";");
        }
        if (stringBuffer.length() <= 0) {
            return null;
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return "&cp=1&mesh=" + stringBuffer.toString();
    }

    public aa(MapView mapView) {
        this.f521b = new ArrayList();
        this.f522c = false;
        this.f526g = 0;
        this.f527h = 0;
        this.f528i = false;
        this.f529j = false;
        this.f520a = mapView;
        this.f523d = System.currentTimeMillis();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void m670b() {
        /*
        r9 = this;
        r6 = 1;
        r4 = 0;
        r2 = 0;
        r9.f529j = r6;
        r0 = r9.m673c();
        if (r0 != 0) goto L_0x0016;
    L_0x000b:
        r0 = r9.f520a;
        r0 = r0.f492i;
        r0.m848a();
        r9.m671b(r9);
    L_0x0015:
        return;
    L_0x0016:
        r1 = r2;
        r3 = r2;
    L_0x0018:
        r0 = r9.f521b;
        r0 = r0.size();
        if (r1 >= r0) goto L_0x0054;
    L_0x0020:
        r0 = r9.f521b;
        r0 = r0.get(r1);
        r0 = (java.lang.String) r0;
        r5 = r9.f520a;
        r5 = r5.f489f;
        if (r5 == 0) goto L_0x0052;
    L_0x002e:
        r5 = r9.f520a;
        r5 = r5.f489f;
        r5 = r5.hasGridData(r0);
        if (r5 == 0) goto L_0x0052;
    L_0x0038:
        r5 = r6;
    L_0x0039:
        if (r5 == 0) goto L_0x004b;
    L_0x003b:
        r5 = r9.f521b;
        r5.remove(r1);
        r1 = r1 + -1;
        r3 = r3 + 1;
        r5 = r9.f520a;
        r5 = r5.tileDownloadCtrl;
        r5.m799a(r0);
    L_0x004b:
        r0 = r1;
        r1 = r3;
        r0 = r0 + 1;
        r3 = r1;
        r1 = r0;
        goto L_0x0018;
    L_0x0052:
        r5 = r2;
        goto L_0x0039;
    L_0x0054:
        r0 = r9.f521b;
        r0 = r0.size();
        if (r0 != 0) goto L_0x0060;
    L_0x005c:
        r9.m671b(r9);
        goto L_0x0015;
    L_0x0060:
        if (r3 <= 0) goto L_0x0067;
    L_0x0062:
        r0 = r9.f520a;
        r0.postInvalidate();
    L_0x0067:
        r3 = 0;
        r0 = 0;
        r1 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0128, all -> 0x00f8 }
        r1.<init>();	 Catch:{ IOException -> 0x0128, all -> 0x00f8 }
        r5 = "";
        r1 = r1.append(r5);	 Catch:{ IOException -> 0x0128, all -> 0x00f8 }
        r5 = r9.m665a();	 Catch:{ IOException -> 0x0128, all -> 0x00f8 }
        r1 = r1.append(r5);	 Catch:{ IOException -> 0x0128, all -> 0x00f8 }
        r1 = r1.toString();	 Catch:{ IOException -> 0x0128, all -> 0x00f8 }
        r5 = r9.f520a;	 Catch:{ IOException -> 0x0128, all -> 0x00f8 }
        r1 = r5.getConnection(r1);	 Catch:{ IOException -> 0x0128, all -> 0x00f8 }
        if (r1 != 0) goto L_0x009c;
    L_0x0088:
        r9.m671b(r9);
        if (r4 == 0) goto L_0x0090;
    L_0x008d:
        r3.close();	 Catch:{ IOException -> 0x010e }
    L_0x0090:
        if (r4 == 0) goto L_0x0095;
    L_0x0092:
        r0.close();	 Catch:{ IOException -> 0x0110 }
    L_0x0095:
        if (r1 == 0) goto L_0x0015;
    L_0x0097:
        r1.disconnect();
        goto L_0x0015;
    L_0x009c:
        r0 = 15000; // 0x3a98 float:2.102E-41 double:7.411E-320;
        r1.setConnectTimeout(r0);	 Catch:{ IOException -> 0x012c, all -> 0x011e }
        r0 = "GET";
        r1.setRequestMethod(r0);	 Catch:{ IOException -> 0x012c, all -> 0x011e }
        r0 = r1.getInputStream();	 Catch:{ IOException -> 0x012c, all -> 0x011e }
        r9.m666a(r9);	 Catch:{ IOException -> 0x00e3, all -> 0x0122 }
        r5 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r5 = new byte[r5];	 Catch:{ IOException -> 0x00e3, all -> 0x0122 }
    L_0x00b1:
        r6 = r0.read(r5);	 Catch:{ IOException -> 0x00e3, all -> 0x0122 }
        r7 = -1;
        if (r6 <= r7) goto L_0x00c9;
    L_0x00b8:
        r7 = r9.m673c();	 Catch:{ IOException -> 0x00e3, all -> 0x0122 }
        if (r7 == 0) goto L_0x00c2;
    L_0x00be:
        r7 = r9.f522c;	 Catch:{ IOException -> 0x00e3, all -> 0x0122 }
        if (r7 == 0) goto L_0x00dd;
    L_0x00c2:
        r2 = r9.f520a;	 Catch:{ IOException -> 0x00e3, all -> 0x0122 }
        r2 = r2.f492i;	 Catch:{ IOException -> 0x00e3, all -> 0x0122 }
        r2.m848a();	 Catch:{ IOException -> 0x00e3, all -> 0x0122 }
    L_0x00c9:
        r9.m671b(r9);
        if (r4 == 0) goto L_0x00d1;
    L_0x00ce:
        r3.close();	 Catch:{ IOException -> 0x0112 }
    L_0x00d1:
        if (r0 == 0) goto L_0x00d6;
    L_0x00d3:
        r0.close();	 Catch:{ IOException -> 0x0114 }
    L_0x00d6:
        if (r1 == 0) goto L_0x0015;
    L_0x00d8:
        r1.disconnect();
        goto L_0x0015;
    L_0x00dd:
        r2 = r2 + r6;
        r7 = 0;
        r9.m667a(r9, r7, r5, r6);	 Catch:{ IOException -> 0x00e3, all -> 0x0122 }
        goto L_0x00b1;
    L_0x00e3:
        r2 = move-exception;
    L_0x00e4:
        r9.m671b(r9);
        if (r4 == 0) goto L_0x00ec;
    L_0x00e9:
        r3.close();	 Catch:{ IOException -> 0x0116 }
    L_0x00ec:
        if (r0 == 0) goto L_0x00f1;
    L_0x00ee:
        r0.close();	 Catch:{ IOException -> 0x0118 }
    L_0x00f1:
        if (r1 == 0) goto L_0x0015;
    L_0x00f3:
        r1.disconnect();
        goto L_0x0015;
    L_0x00f8:
        r0 = move-exception;
        r1 = r4;
        r2 = r4;
    L_0x00fb:
        r9.m671b(r9);
        if (r4 == 0) goto L_0x0103;
    L_0x0100:
        r3.close();	 Catch:{ IOException -> 0x011a }
    L_0x0103:
        if (r1 == 0) goto L_0x0108;
    L_0x0105:
        r1.close();	 Catch:{ IOException -> 0x011c }
    L_0x0108:
        if (r2 == 0) goto L_0x010d;
    L_0x010a:
        r2.disconnect();
    L_0x010d:
        throw r0;
    L_0x010e:
        r2 = move-exception;
        goto L_0x0090;
    L_0x0110:
        r0 = move-exception;
        goto L_0x0095;
    L_0x0112:
        r2 = move-exception;
        goto L_0x00d1;
    L_0x0114:
        r0 = move-exception;
        goto L_0x00d6;
    L_0x0116:
        r2 = move-exception;
        goto L_0x00ec;
    L_0x0118:
        r0 = move-exception;
        goto L_0x00f1;
    L_0x011a:
        r3 = move-exception;
        goto L_0x0103;
    L_0x011c:
        r1 = move-exception;
        goto L_0x0108;
    L_0x011e:
        r0 = move-exception;
        r2 = r1;
        r1 = r4;
        goto L_0x00fb;
    L_0x0122:
        r2 = move-exception;
        r8 = r2;
        r2 = r1;
        r1 = r0;
        r0 = r8;
        goto L_0x00fb;
    L_0x0128:
        r0 = move-exception;
        r0 = r4;
        r1 = r4;
        goto L_0x00e4;
    L_0x012c:
        r0 = move-exception;
        r0 = r4;
        goto L_0x00e4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.mapapi.map.aa.b():void");
    }

    public boolean m673c() {
        if (this.f524e != this.f520a.mapLevel) {
            return false;
        }
        return this.f520a.isAGridsInScreen(this.f521b);
    }

    public void m668a(String str) {
    }

    public void m666a(aa aaVar) {
        this.f525f = new byte[ServiceConnection.DEFAULT_BUFFER_SIZE];
        this.f527h = 0;
        this.f526g = 0;
        this.f528i = false;
        m668a("\u8fde\u63a5\u6253\u5f00\u6210\u529f...");
    }

    public void m672b(String str) {
        this.f521b.add(str);
    }

    public void m671b(aa aaVar) {
        this.f525f = null;
        this.f527h = 0;
        this.f526g = 0;
        m668a(null);
        for (int i = 0; i < aaVar.f521b.size(); i++) {
            this.f520a.tileDownloadCtrl.m799a((String) aaVar.f521b.get(i));
        }
        if (this.f520a.f492i.f692a.m773b() == this) {
            this.f520a.f492i.f692a.m772a();
        }
        this.f520a.postInvalidate();
    }

    public void m667a(aa aaVar, int i, byte[] bArr, int i2) {
        System.arraycopy(bArr, 0, this.f525f, this.f526g, i2);
        this.f526g += i2;
        if (!this.f528i) {
            if (this.f526g <= 7) {
                return;
            }
            if (Convert.m478a(this.f525f, 0) != 0) {
                aaVar.f522c = true;
                return;
            }
            Convert.m478a(this.f525f, 4);
            Convert.m479a(this.f525f, 8, this.f525f, 0, i2 - 8);
            this.f526g -= 8;
            this.f527h = 0;
            this.f528i = true;
            m664d();
        }
        m664d();
    }

    private void m664d() {
        if (this.f527h == 0) {
            if (this.f526g >= 8) {
                this.f527h = Convert.m478a(this.f525f, 0) + 8;
                m664d();
            }
        } else if (this.f526g >= this.f527h) {
            int a = Convert.m478a(this.f525f, 0);
            int a2 = Convert.m478a(this.f525f, 4);
            if (a2 == 0) {
                m669a(this.f525f, 8, a);
            } else {
                try {
                    GZIPInputStream gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(this.f525f, 8, a));
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] bArr = new byte[Flags.FLAG8];
                    while (true) {
                        int read = gZIPInputStream.read(bArr);
                        if (read <= -1) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, read);
                    }
                    m669a(byteArrayOutputStream.toByteArray(), 0, a2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Convert.m479a(this.f525f, this.f527h, this.f525f, 0, this.f526g - this.f527h);
            this.f526g -= this.f527h;
            this.f527h = 0;
            m664d();
        }
    }

    void m669a(byte[] bArr, int i, int i2) {
        Convert.m480b(this.f525f, i);
        int i3 = i + 2;
        Convert.m480b(this.f525f, i3);
        i3 += 2;
        Convert.m478a(this.f525f, i3);
        i3 += 4;
        int i4 = i3 + 1;
        byte b = bArr[i3];
        String str = new String(bArr, i4, b);
        i3 = b + i4;
        if (this.f520a.f489f != null) {
            this.f520a.f489f.putGridData(bArr, i, i2 - i);
            this.f520a.f489f.removeBitmapData(str, this.f520a.getGridLevelOff(str.length()));
            if (this.f520a.isGridInScreen(str)) {
                this.f520a.postInvalidate();
            }
        }
    }
}
