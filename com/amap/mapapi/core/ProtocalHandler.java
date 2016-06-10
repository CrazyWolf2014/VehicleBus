package com.amap.mapapi.core;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.core.l */
public abstract class ProtocalHandler<T, V> {
    protected Proxy f356a;
    protected T f357b;
    protected int f358c;
    protected int f359d;
    protected int f360e;
    protected int f361f;
    protected String f362g;
    protected String f363h;

    protected abstract V m524b(InputStream inputStream) throws AMapException;

    protected abstract boolean m526e();

    protected abstract String[] m527f();

    protected abstract byte[] m528g();

    protected abstract String m529h();

    public ProtocalHandler(T t, Proxy proxy, String str, String str2) {
        this.f358c = 1;
        this.f359d = 20;
        this.f360e = 0;
        this.f361f = 0;
        this.f363h = XmlPullParser.NO_NAMESPACE;
        this.f356a = proxy;
        this.f357b = t;
        this.f358c = 1;
        this.f359d = 5;
        this.f360e = 2;
        this.f362g = str2;
    }

    protected byte[] m530i() {
        return m528g();
    }

    private String m521a() {
        String[] f = m527f();
        if (f == null) {
            return XmlPullParser.NO_NAMESPACE;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (f != null) {
            for (String append : f) {
                stringBuilder.append(append);
            }
        }
        return stringBuilder.toString();
    }

    public V m531j() throws AMapException {
        if (this.f357b != null) {
            return m522b();
        }
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private V m522b() throws com.amap.mapapi.core.AMapException {
        /*
        r10 = this;
        r4 = 0;
        r0 = 0;
        r1 = r4;
        r5 = r4;
        r2 = r4;
        r3 = r4;
    L_0x0006:
        r6 = r10.f358c;
        if (r0 >= r6) goto L_0x00e7;
    L_0x000a:
        r6 = r10.m526e();	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        if (r6 != 0) goto L_0x003f;
    L_0x0010:
        r6 = r10.m529h();	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r10.f363h = r6;	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r6 = r10.m530i();	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r7 = r10.f363h;	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r8 = r10.f356a;	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r3 = com.amap.mapapi.core.HttpTool.m500a(r7, r6, r8);	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
    L_0x0022:
        r6 = r10.m523a(r3);	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r1 = r10.m520a(r6);	 Catch:{ AMapException -> 0x00ea }
        r0 = r10.f358c;	 Catch:{ AMapException -> 0x00ea }
        if (r6 == 0) goto L_0x00ee;
    L_0x002e:
        r6.close();	 Catch:{ IOException -> 0x0063 }
        r2 = r4;
    L_0x0032:
        if (r4 == 0) goto L_0x0037;
    L_0x0034:
        r5.close();	 Catch:{ IOException -> 0x006c }
    L_0x0037:
        if (r3 == 0) goto L_0x003d;
    L_0x0039:
        r3.disconnect();
        r3 = r4;
    L_0x003d:
        r5 = r4;
        goto L_0x0006;
    L_0x003f:
        r6 = new java.lang.StringBuilder;	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r6.<init>();	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r7 = r10.m529h();	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r6 = r6.append(r7);	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r7 = r10.m521a();	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r6 = r6.append(r7);	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r6 = r6.toString();	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r10.f363h = r6;	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r6 = r10.f363h;	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r7 = r10.f356a;	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        r3 = com.amap.mapapi.core.HttpTool.m499a(r6, r7);	 Catch:{ AMapException -> 0x0075, all -> 0x00e8 }
        goto L_0x0022;
    L_0x0063:
        r0 = move-exception;
        r0 = new com.amap.mapapi.core.AMapException;
        r1 = "IO \u64cd\u4f5c\u5f02\u5e38 - IOException";
        r0.<init>(r1);
        throw r0;
    L_0x006c:
        r0 = move-exception;
        r0 = new com.amap.mapapi.core.AMapException;
        r1 = "IO \u64cd\u4f5c\u5f02\u5e38 - IOException";
        r0.<init>(r1);
        throw r0;
    L_0x0075:
        r6 = move-exception;
        r9 = r6;
        r6 = r2;
        r2 = r9;
    L_0x0079:
        r0 = r0 + 1;
        r7 = r10.f358c;	 Catch:{ all -> 0x00a4 }
        if (r0 >= r7) goto L_0x00b6;
    L_0x007f:
        r2 = r10.f360e;	 Catch:{ InterruptedException -> 0x0099 }
        r2 = r2 * 1000;
        r7 = (long) r2;	 Catch:{ InterruptedException -> 0x0099 }
        java.lang.Thread.sleep(r7);	 Catch:{ InterruptedException -> 0x0099 }
        if (r6 == 0) goto L_0x00ec;
    L_0x0089:
        r6.close();	 Catch:{ IOException -> 0x00c3 }
        r2 = r4;
    L_0x008d:
        if (r4 == 0) goto L_0x0092;
    L_0x008f:
        r5.close();	 Catch:{ IOException -> 0x00cc }
    L_0x0092:
        if (r3 == 0) goto L_0x003d;
    L_0x0094:
        r3.disconnect();
        r3 = r4;
        goto L_0x003d;
    L_0x0099:
        r0 = move-exception;
        r1 = new com.amap.mapapi.core.AMapException;	 Catch:{ all -> 0x00a4 }
        r0 = r0.getMessage();	 Catch:{ all -> 0x00a4 }
        r1.<init>(r0);	 Catch:{ all -> 0x00a4 }
        throw r1;	 Catch:{ all -> 0x00a4 }
    L_0x00a4:
        r0 = move-exception;
        r2 = r6;
    L_0x00a6:
        if (r2 == 0) goto L_0x00ab;
    L_0x00a8:
        r2.close();	 Catch:{ IOException -> 0x00d5 }
    L_0x00ab:
        if (r4 == 0) goto L_0x00b0;
    L_0x00ad:
        r5.close();	 Catch:{ IOException -> 0x00de }
    L_0x00b0:
        if (r3 == 0) goto L_0x00b5;
    L_0x00b2:
        r3.disconnect();
    L_0x00b5:
        throw r0;
    L_0x00b6:
        r10.m532k();	 Catch:{ all -> 0x00a4 }
        r0 = new com.amap.mapapi.core.AMapException;	 Catch:{ all -> 0x00a4 }
        r1 = r2.getErrorMessage();	 Catch:{ all -> 0x00a4 }
        r0.<init>(r1);	 Catch:{ all -> 0x00a4 }
        throw r0;	 Catch:{ all -> 0x00a4 }
    L_0x00c3:
        r0 = move-exception;
        r0 = new com.amap.mapapi.core.AMapException;
        r1 = "IO \u64cd\u4f5c\u5f02\u5e38 - IOException";
        r0.<init>(r1);
        throw r0;
    L_0x00cc:
        r0 = move-exception;
        r0 = new com.amap.mapapi.core.AMapException;
        r1 = "IO \u64cd\u4f5c\u5f02\u5e38 - IOException";
        r0.<init>(r1);
        throw r0;
    L_0x00d5:
        r0 = move-exception;
        r0 = new com.amap.mapapi.core.AMapException;
        r1 = "IO \u64cd\u4f5c\u5f02\u5e38 - IOException";
        r0.<init>(r1);
        throw r0;
    L_0x00de:
        r0 = move-exception;
        r0 = new com.amap.mapapi.core.AMapException;
        r1 = "IO \u64cd\u4f5c\u5f02\u5e38 - IOException";
        r0.<init>(r1);
        throw r0;
    L_0x00e7:
        return r1;
    L_0x00e8:
        r0 = move-exception;
        goto L_0x00a6;
    L_0x00ea:
        r2 = move-exception;
        goto L_0x0079;
    L_0x00ec:
        r2 = r6;
        goto L_0x008d;
    L_0x00ee:
        r2 = r6;
        goto L_0x0032;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.mapapi.core.l.b():V");
    }

    protected InputStream m523a(HttpURLConnection httpURLConnection) throws AMapException {
        try {
            return httpURLConnection.getInputStream();
        } catch (ProtocolException e) {
            throw new AMapException(AMapException.ERROR_PROTOCOL);
        } catch (UnknownHostException e2) {
            throw new AMapException(AMapException.ERROR_UNKNOW_HOST);
        } catch (UnknownServiceException e3) {
            throw new AMapException(AMapException.ERROR_UNKNOW_SERVICE);
        } catch (IOException e4) {
            throw new AMapException(AMapException.ERROR_IO);
        }
    }

    private V m520a(InputStream inputStream) throws AMapException {
        return m524b(inputStream);
    }

    protected V m532k() {
        return null;
    }

    protected String m525c(InputStream inputStream) throws AMapException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bArr = new byte[Flags.FLAG5];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
                byteArrayOutputStream.flush();
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    throw new AMapException(AMapException.ERROR_IO);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    throw new AMapException(AMapException.ERROR_IO);
                }
            }
            try {
                return new String(byteArrayOutputStream.toByteArray(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
            } catch (UnsupportedEncodingException e3) {
                e3.printStackTrace();
                return new String();
            }
        } catch (IOException e4) {
            throw new AMapException(AMapException.ERROR_IO);
        } catch (Throwable th) {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e5) {
                    throw new AMapException(AMapException.ERROR_IO);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e6) {
                    throw new AMapException(AMapException.ERROR_IO);
                }
            }
        }
    }
}
