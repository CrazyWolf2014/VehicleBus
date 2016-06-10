package com.iflytek.msc.p013f;

import android.text.TextUtils;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.iflytek.msc.f.h */
public class C0280h extends Thread {
    private int f1042a;
    private C0279a f1043b;
    private volatile boolean f1044c;
    private URL f1045d;
    private ArrayList<byte[]> f1046e;
    private Object f1047f;
    private int f1048g;

    /* renamed from: com.iflytek.msc.f.h.a */
    public interface C0279a {
        void m1237a(C0280h c0280h, byte[] bArr);

        void m1238a(Exception exception);
    }

    public C0280h() {
        this.f1042a = BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
        this.f1043b = null;
        this.f1044c = false;
        this.f1045d = null;
        this.f1046e = new ArrayList();
        this.f1047f = null;
        this.f1048g = 0;
    }

    public static URL m1239a(String str, String str2) throws MalformedURLException {
        if (!(TextUtils.isEmpty(str) || TextUtils.isEmpty(str2))) {
            if (!str.endsWith("?")) {
                str = str + "?";
            }
            str = str + str2;
        }
        return new URL(str);
    }

    private void m1240a(Exception exception) {
        if (this.f1043b != null && !this.f1044c) {
            this.f1043b.m1238a(exception);
        }
    }

    public static boolean m1241a(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("rsp") && jSONObject.getJSONObject("rsp").getInt("code") != 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private byte[] m1242a(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(Flags.FLAG5);
        byte[] bArr = new byte[Flags.FLAG5];
        while (!this.f1044c) {
            int read = bufferedInputStream.read(bArr, 0, Flags.FLAG5);
            if (read == -1) {
                break;
            }
            byteArrayBuffer.append(bArr, 0, read);
        }
        return byteArrayBuffer.toByteArray();
    }

    private void m1243b(byte[] bArr) {
        if (this.f1043b != null && !this.f1044c) {
            this.f1043b.m1237a(this, bArr);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m1244c() {
        /*
        r6 = this;
        r2 = 0;
        r0 = "Start connect server";
        com.iflytek.msc.p013f.C0276e.m1220a(r0);	 Catch:{ Exception -> 0x00ac, all -> 0x0093 }
        r0 = r6.f1045d;	 Catch:{ Exception -> 0x00ac, all -> 0x0093 }
        r0 = r0.openConnection();	 Catch:{ Exception -> 0x00ac, all -> 0x0093 }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x00ac, all -> 0x0093 }
        r1 = r6.f1042a;	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r0.setConnectTimeout(r1);	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r1 = r6.f1042a;	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r0.setReadTimeout(r1);	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r1 = "GET";
        r0.setRequestMethod(r1);	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r1 = r0.getResponseCode();	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r3.<init>();	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r4 = "responseCode = ";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r3 = r3.append(r1);	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        com.iflytek.msc.p013f.C0276e.m1220a(r3);	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r3 != r1) goto L_0x0051;
    L_0x003b:
        r2 = r0.getInputStream();	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r1 = r6.m1242a(r2);	 Catch:{ Exception -> 0x00af, all -> 0x00a2 }
        r6.m1243b(r1);	 Catch:{ Exception -> 0x00af, all -> 0x00a2 }
    L_0x0046:
        if (r2 == 0) goto L_0x004b;
    L_0x0048:
        r2.close();	 Catch:{ Exception -> 0x00b5 }
    L_0x004b:
        if (r0 == 0) goto L_0x0050;
    L_0x004d:
        r0.disconnect();	 Catch:{ Exception -> 0x00b5 }
    L_0x0050:
        return;
    L_0x0051:
        r1 = "MscHttpRequest connect error";
        com.iflytek.msc.p013f.C0276e.m1220a(r1);	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r1 = new java.lang.Exception;	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r3 = "HttpRequest Failed.";
        r1.<init>(r3);	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        r6.m1240a(r1);	 Catch:{ Exception -> 0x0061, all -> 0x00a2 }
        goto L_0x0046;
    L_0x0061:
        r1 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r0;
        r0 = r5;
    L_0x0066:
        r0.printStackTrace();	 Catch:{ all -> 0x00a7 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a7 }
        r3.<init>();	 Catch:{ all -> 0x00a7 }
        r4 = "MscHttpRequest error:";
        r3 = r3.append(r4);	 Catch:{ all -> 0x00a7 }
        r4 = r0.toString();	 Catch:{ all -> 0x00a7 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x00a7 }
        r3 = r3.toString();	 Catch:{ all -> 0x00a7 }
        com.iflytek.msc.p013f.C0276e.m1220a(r3);	 Catch:{ all -> 0x00a7 }
        r6.m1240a(r0);	 Catch:{ all -> 0x00a7 }
        if (r1 == 0) goto L_0x008b;
    L_0x0088:
        r1.close();	 Catch:{ Exception -> 0x0091 }
    L_0x008b:
        if (r2 == 0) goto L_0x0050;
    L_0x008d:
        r2.disconnect();	 Catch:{ Exception -> 0x0091 }
        goto L_0x0050;
    L_0x0091:
        r0 = move-exception;
        goto L_0x0050;
    L_0x0093:
        r0 = move-exception;
        r1 = r2;
    L_0x0095:
        if (r2 == 0) goto L_0x009a;
    L_0x0097:
        r2.close();	 Catch:{ Exception -> 0x00a0 }
    L_0x009a:
        if (r1 == 0) goto L_0x009f;
    L_0x009c:
        r1.disconnect();	 Catch:{ Exception -> 0x00a0 }
    L_0x009f:
        throw r0;
    L_0x00a0:
        r1 = move-exception;
        goto L_0x009f;
    L_0x00a2:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
        goto L_0x0095;
    L_0x00a7:
        r0 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r5;
        goto L_0x0095;
    L_0x00ac:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0066;
    L_0x00af:
        r1 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r0;
        r0 = r5;
        goto L_0x0066;
    L_0x00b5:
        r0 = move-exception;
        goto L_0x0050;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.iflytek.msc.f.h.c():void");
    }

    private int m1245d() {
        int i = 0;
        for (int i2 = 0; i2 < this.f1046e.size(); i2++) {
            i += ((byte[]) this.f1046e.get(i2)).length;
        }
        return i;
    }

    public Object m1246a() {
        return this.f1047f;
    }

    public void m1247a(int i) {
        this.f1048g = i;
    }

    public void m1248a(C0279a c0279a) {
        this.f1043b = c0279a;
        start();
    }

    public void m1249a(Object obj) {
        this.f1047f = obj;
    }

    public void m1250a(String str, String str2, byte[] bArr) {
        this.f1046e.clear();
        m1251a(bArr);
        try {
            this.f1045d = C0280h.m1239a(str, str2);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void m1251a(byte[] bArr) {
        if (bArr != null) {
            this.f1046e.add(bArr);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void m1252b() {
        /*
        r6 = this;
        r2 = 0;
        r0 = "MscHttpRequest start Post";
        com.iflytek.msc.p013f.C0276e.m1220a(r0);	 Catch:{ Exception -> 0x00f2, all -> 0x00e8 }
        r0 = r6.f1045d;	 Catch:{ Exception -> 0x00f2, all -> 0x00e8 }
        r0 = r0.openConnection();	 Catch:{ Exception -> 0x00f2, all -> 0x00e8 }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x00f2, all -> 0x00e8 }
        r1 = 1;
        r0.setDoOutput(r1);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = 1;
        r0.setDoInput(r1);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = 0;
        r0.setUseCaches(r1);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = "POST";
        r0.setRequestMethod(r1);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = r6.f1042a;	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r0.setConnectTimeout(r1);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = r6.f1042a;	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r0.setReadTimeout(r1);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = "Content-length";
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r3.<init>();	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r4 = "";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r4 = r6.m1245d();	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r0.setRequestProperty(r1, r3);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = "Connection";
        r3 = "Keep-Alive";
        r0.setRequestProperty(r1, r3);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = "Charset";
        r3 = "utf-8";
        r0.setRequestProperty(r1, r3);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = "Content-Type";
        r3 = "application/x-gzip";
        r0.setRequestProperty(r1, r3);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r3 = r0.getOutputStream();	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = r6.f1046e;	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r4 = r1.iterator();	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
    L_0x0064:
        r1 = r4.hasNext();	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        if (r1 == 0) goto L_0x00a1;
    L_0x006a:
        r1 = r4.next();	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = (byte[]) r1;	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r3.write(r1);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        goto L_0x0064;
    L_0x0074:
        r1 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r0;
        r0 = r5;
    L_0x0079:
        r6.m1240a(r0);	 Catch:{ all -> 0x00eb }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00eb }
        r3.<init>();	 Catch:{ all -> 0x00eb }
        r4 = "MscHttpRequest error:";
        r3 = r3.append(r4);	 Catch:{ all -> 0x00eb }
        r0 = r0.toString();	 Catch:{ all -> 0x00eb }
        r0 = r3.append(r0);	 Catch:{ all -> 0x00eb }
        r0 = r0.toString();	 Catch:{ all -> 0x00eb }
        com.iflytek.msc.p013f.C0276e.m1220a(r0);	 Catch:{ all -> 0x00eb }
        if (r1 == 0) goto L_0x009b;
    L_0x0098:
        r1.close();	 Catch:{ Exception -> 0x00f0 }
    L_0x009b:
        if (r2 == 0) goto L_0x00a0;
    L_0x009d:
        r2.disconnect();	 Catch:{ Exception -> 0x00f0 }
    L_0x00a0:
        return;
    L_0x00a1:
        r3.flush();	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r3.close();	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = r0.getResponseCode();	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r3 != r1) goto L_0x00c7;
    L_0x00af:
        r2 = r0.getInputStream();	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = r6.m1242a(r2);	 Catch:{ Exception -> 0x00f5, all -> 0x00d7 }
        r6.m1243b(r1);	 Catch:{ Exception -> 0x00f5, all -> 0x00d7 }
    L_0x00ba:
        if (r2 == 0) goto L_0x00bf;
    L_0x00bc:
        r2.close();	 Catch:{ Exception -> 0x00c5 }
    L_0x00bf:
        if (r0 == 0) goto L_0x00a0;
    L_0x00c1:
        r0.disconnect();	 Catch:{ Exception -> 0x00c5 }
        goto L_0x00a0;
    L_0x00c5:
        r0 = move-exception;
        goto L_0x00a0;
    L_0x00c7:
        r1 = "Http Request Failed.";
        com.iflytek.msc.p013f.C0276e.m1220a(r1);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r1 = new java.lang.Exception;	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r3 = "Http Request Failed.";
        r1.<init>(r3);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        r6.m1240a(r1);	 Catch:{ Exception -> 0x0074, all -> 0x00d7 }
        goto L_0x00ba;
    L_0x00d7:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
    L_0x00db:
        if (r2 == 0) goto L_0x00e0;
    L_0x00dd:
        r2.close();	 Catch:{ Exception -> 0x00e6 }
    L_0x00e0:
        if (r1 == 0) goto L_0x00e5;
    L_0x00e2:
        r1.disconnect();	 Catch:{ Exception -> 0x00e6 }
    L_0x00e5:
        throw r0;
    L_0x00e6:
        r1 = move-exception;
        goto L_0x00e5;
    L_0x00e8:
        r0 = move-exception;
        r1 = r2;
        goto L_0x00db;
    L_0x00eb:
        r0 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r5;
        goto L_0x00db;
    L_0x00f0:
        r0 = move-exception;
        goto L_0x00a0;
    L_0x00f2:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0079;
    L_0x00f5:
        r1 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r0;
        r0 = r5;
        goto L_0x0079;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.iflytek.msc.f.h.b():void");
    }

    public void run() {
        if (this.f1048g == 1) {
            m1252b();
        } else {
            m1244c();
        }
    }
}
