package com.autonavi.gelocator.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Proxy;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MIME;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.xmlpull.v1.XmlPullParser;

public class NetManagerApache {
    private static NetManagerApache f786b;
    private Context f787a;

    static {
        f786b = null;
    }

    private NetManagerApache() {
    }

    public static NetManagerApache getInstance(Context context) {
        if (f786b == null) {
            NetManagerApache netManagerApache = new NetManagerApache();
            f786b = netManagerApache;
            netManagerApache.f787a = context.getApplicationContext();
        }
        return f786b;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
        if (allNetworkInfo == null) {
            return false;
        }
        for (NetworkInfo state : allNetworkInfo) {
            if (state.getState() == State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public InputStream doGetAsInputstream(String str, Hashtable hashtable) {
        HttpClient httpClient;
        Throwable e;
        InputStream inputStream = null;
        try {
            String str2;
            httpClient = getHttpClient();
            if (hashtable != null) {
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    Enumeration keys = hashtable.keys();
                    while (keys.hasMoreElements()) {
                        str2 = (String) keys.nextElement();
                        stringBuffer.append(new StringBuilder(AlixDefine.split).append(str2).append("=").append(URLEncoder.encode((String) hashtable.get(str2), "utf-8")).toString());
                    }
                    str = str.contains("?") ? new StringBuilder(String.valueOf(str)).append(stringBuffer.toString()).toString() : new StringBuilder(String.valueOf(str)).append("?").append(stringBuffer.toString()).toString();
                } catch (Exception e2) {
                    e = e2;
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (httpClient != null) {
                        httpClient.getConnectionManager().shutdown();
                    }
                    throw new Exception("Net Exception", e);
                }
            }
            HttpResponse execute = httpClient.execute(new HttpGet(str));
            Header[] headers = execute.getHeaders("Content-Encoding");
            if (headers != null) {
                str2 = inputStream;
                for (Header header : headers) {
                    if (!(header.getValue() == null || header.getValue().equals(XmlPullParser.NO_NAMESPACE))) {
                        str2 = header.getValue();
                    }
                }
            } else {
                Object obj = inputStream;
            }
            if (execute.getStatusLine().getStatusCode() != AsyncTaskManager.REQUEST_SUCCESS_CODE) {
                return inputStream;
            }
            inputStream = execute.getEntity().getContent();
            return (str2 == null || !str2.equals("gzip")) ? inputStream : new GZIPInputStream(inputStream);
        } catch (Exception e3) {
            e = e3;
            Object obj2 = inputStream;
            if (inputStream != null) {
                inputStream.close();
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
            throw new Exception("Net Exception", e);
        }
    }

    public String doGetAsString(String str, Hashtable hashtable) {
        HttpClient httpClient;
        Throwable e;
        InputStream gZIPInputStream;
        StringBuffer stringBuffer = new StringBuffer();
        HttpClient httpClient2 = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            String str2;
            BufferedReader bufferedReader2;
            httpClient = getHttpClient();
            if (hashtable != null) {
                try {
                    StringBuffer stringBuffer2 = new StringBuffer();
                    Enumeration keys = hashtable.keys();
                    while (keys.hasMoreElements()) {
                        str2 = (String) keys.nextElement();
                        stringBuffer2.append(new StringBuilder(AlixDefine.split).append(str2).append("=").append(URLEncoder.encode((String) hashtable.get(str2), "utf-8")).toString());
                    }
                    str = str.contains("?") ? new StringBuilder(String.valueOf(str)).append(stringBuffer2.toString()).toString() : new StringBuilder(String.valueOf(str)).append("?").append(stringBuffer2.toString()).toString();
                } catch (Exception e2) {
                    e = e2;
                    httpClient2 = httpClient;
                } catch (Throwable th) {
                    e = th;
                }
            }
            HttpResponse execute = httpClient.execute(new HttpGet(str));
            str2 = "utf-8";
            Header[] headers = execute.getHeaders(MIME.CONTENT_TYPE);
            if (headers != null) {
                for (Header value : headers) {
                    String value2 = value.getValue();
                    if (!(value2 == null || value2.equals(XmlPullParser.NO_NAMESPACE))) {
                        String[] split = value2.split(";");
                        if (split != null) {
                            for (String str3 : split) {
                                if (str3.trim().toLowerCase().startsWith("charset=")) {
                                    str2 = str3.trim().toLowerCase().replace("charset=", XmlPullParser.NO_NAMESPACE);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            String str4 = str2;
            str2 = null;
            headers = execute.getHeaders("Content-Encoding");
            if (headers != null) {
                for (Header header : headers) {
                    if (!(header.getValue() == null || header.getValue().equals(XmlPullParser.NO_NAMESPACE))) {
                        str2 = header.getValue();
                    }
                }
            }
            if (execute.getStatusLine().getStatusCode() == AsyncTaskManager.REQUEST_SUCCESS_CODE) {
                inputStream = execute.getEntity().getContent();
                gZIPInputStream = (str2 == null || !str2.equals("gzip")) ? inputStream : new GZIPInputStream(inputStream);
                try {
                    bufferedReader2 = new BufferedReader(new InputStreamReader(gZIPInputStream, str4));
                    while (true) {
                        try {
                            str2 = bufferedReader2.readLine();
                            if (str2 == null) {
                                break;
                            }
                            stringBuffer.append(str2);
                        } catch (Exception e3) {
                            e = e3;
                            bufferedReader = bufferedReader2;
                            inputStream = gZIPInputStream;
                            httpClient2 = httpClient;
                        } catch (Throwable th2) {
                            e = th2;
                            bufferedReader = bufferedReader2;
                            inputStream = gZIPInputStream;
                        }
                    }
                } catch (Exception e4) {
                    e = e4;
                    inputStream = gZIPInputStream;
                    httpClient2 = httpClient;
                    try {
                        throw new Exception("Net Exception", e);
                    } catch (Throwable th3) {
                        e = th3;
                        httpClient = httpClient2;
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (httpClient != null) {
                            httpClient.getConnectionManager().shutdown();
                        }
                        throw e;
                    }
                } catch (Throwable th4) {
                    e = th4;
                    inputStream = gZIPInputStream;
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (httpClient != null) {
                        httpClient.getConnectionManager().shutdown();
                    }
                    throw e;
                }
            }
            gZIPInputStream = null;
            bufferedReader2 = null;
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            if (gZIPInputStream != null) {
                gZIPInputStream.close();
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
            return stringBuffer.toString();
        } catch (Exception e5) {
            e = e5;
            throw new Exception("Net Exception", e);
        } catch (Throwable th5) {
            e = th5;
            httpClient = null;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
            throw e;
        }
    }

    public InputStream doPostAsInputstream(String str, Hashtable hashtable) {
        Throwable e;
        HttpClient httpClient;
        InputStream inputStream = null;
        try {
            String str2;
            HttpClient httpClient2 = getHttpClient();
            HttpUriRequest httpPost = new HttpPost(str);
            List arrayList = new ArrayList();
            if (hashtable != null) {
                Enumeration keys = hashtable.keys();
                while (keys.hasMoreElements()) {
                    try {
                        String str3 = (String) keys.nextElement();
                        arrayList.add(new BasicNameValuePair(str3, (String) hashtable.get(str3)));
                    } catch (Exception e2) {
                        e = e2;
                        httpClient = httpClient2;
                    }
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "utf-8"));
            HttpResponse execute = httpClient2.execute(httpPost);
            Header[] headers = execute.getHeaders("Content-Encoding");
            if (headers != null) {
                str2 = null;
                for (Header header : headers) {
                    if (!(header.getValue() == null || header.getValue().equals(XmlPullParser.NO_NAMESPACE))) {
                        str2 = header.getValue();
                    }
                }
            } else {
                str2 = null;
            }
            if (execute.getStatusLine().getStatusCode() != AsyncTaskManager.REQUEST_SUCCESS_CODE) {
                return null;
            }
            InputStream content = execute.getEntity().getContent();
            if (str2 != null) {
                try {
                    if (str2.equals("gzip")) {
                        return new GZIPInputStream(content);
                    }
                } catch (Exception e3) {
                    e = e3;
                    inputStream = content;
                    httpClient = httpClient2;
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (httpClient != null) {
                        httpClient.getConnectionManager().shutdown();
                    }
                    throw new Exception("Net Exception", e);
                }
            }
            return content;
        } catch (Exception e4) {
            e = e4;
            httpClient = null;
            if (inputStream != null) {
                inputStream.close();
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
            throw new Exception("Net Exception", e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String doPostAsString(java.lang.String r18, java.util.Hashtable r19) {
        /*
        r17 = this;
        r7 = new java.lang.StringBuffer;
        r7.<init>();
        r2 = 0;
        r4 = 0;
        r3 = 0;
        r5 = r17.getHttpClient();	 Catch:{ Exception -> 0x0154, all -> 0x0145 }
        r6 = new org.apache.http.client.methods.HttpPost;	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r0 = r18;
        r6.<init>(r0);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r8 = new java.util.ArrayList;	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r8.<init>();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        if (r19 == 0) goto L_0x0024;
    L_0x001a:
        r9 = r19.keys();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
    L_0x001e:
        r1 = r9.hasMoreElements();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        if (r1 != 0) goto L_0x009a;
    L_0x0024:
        r1 = new org.apache.http.client.entity.UrlEncodedFormEntity;	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r2 = "utf-8";
        r1.<init>(r8, r2);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r6.setEntity(r1);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r8 = r5.execute(r6);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r1 = "utf-8";
        r2 = "Content-Type";
        r9 = r8.getHeaders(r2);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        if (r9 == 0) goto L_0x0041;
    L_0x003c:
        r10 = r9.length;	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r2 = 0;
        r6 = r2;
    L_0x003f:
        if (r6 < r10) goto L_0x00d8;
    L_0x0041:
        r6 = r1;
        r1 = 0;
        r2 = "Content-Encoding";
        r9 = r8.getHeaders(r2);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        if (r9 == 0) goto L_0x004f;
    L_0x004b:
        r10 = r9.length;	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r2 = 0;
    L_0x004d:
        if (r2 < r10) goto L_0x011f;
    L_0x004f:
        r2 = r8.getStatusLine();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r2 = r2.getStatusCode();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r9 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r2 != r9) goto L_0x016f;
    L_0x005b:
        r2 = r8.getEntity();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r2 = r2.getContent();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        if (r1 == 0) goto L_0x016c;
    L_0x0065:
        r4 = "gzip";
        r1 = r1.equals(r4);	 Catch:{ Exception -> 0x015d, all -> 0x014c }
        if (r1 == 0) goto L_0x016c;
    L_0x006d:
        r4 = new java.util.zip.GZIPInputStream;	 Catch:{ Exception -> 0x015d, all -> 0x014c }
        r4.<init>(r2);	 Catch:{ Exception -> 0x015d, all -> 0x014c }
    L_0x0072:
        r2 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0166, all -> 0x0149 }
        r1 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0166, all -> 0x0149 }
        r1.<init>(r4, r6);	 Catch:{ Exception -> 0x0166, all -> 0x0149 }
        r2.<init>(r1);	 Catch:{ Exception -> 0x0166, all -> 0x0149 }
    L_0x007c:
        r1 = r2.readLine();	 Catch:{ Exception -> 0x0140, all -> 0x0150 }
        if (r1 != 0) goto L_0x013b;
    L_0x0082:
        if (r2 == 0) goto L_0x0087;
    L_0x0084:
        r2.close();
    L_0x0087:
        if (r4 == 0) goto L_0x008c;
    L_0x0089:
        r4.close();
    L_0x008c:
        if (r5 == 0) goto L_0x0095;
    L_0x008e:
        r1 = r5.getConnectionManager();
        r1.shutdown();
    L_0x0095:
        r1 = r7.toString();
        return r1;
    L_0x009a:
        r1 = r9.nextElement();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r0 = r1;
        r0 = (java.lang.String) r0;	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r2 = r0;
        r10 = new org.apache.http.message.BasicNameValuePair;	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r0 = r19;
        r1 = r0.get(r2);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r1 = (java.lang.String) r1;	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r10.<init>(r2, r1);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r8.add(r10);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        goto L_0x001e;
    L_0x00b4:
        r1 = move-exception;
        r2 = r3;
        r3 = r4;
        r4 = r5;
    L_0x00b8:
        r5 = new java.lang.Exception;	 Catch:{ all -> 0x00c0 }
        r6 = "Net Exception";
        r5.<init>(r6, r1);	 Catch:{ all -> 0x00c0 }
        throw r5;	 Catch:{ all -> 0x00c0 }
    L_0x00c0:
        r1 = move-exception;
        r5 = r4;
        r4 = r3;
        r3 = r2;
    L_0x00c4:
        if (r3 == 0) goto L_0x00c9;
    L_0x00c6:
        r3.close();
    L_0x00c9:
        if (r4 == 0) goto L_0x00ce;
    L_0x00cb:
        r4.close();
    L_0x00ce:
        if (r5 == 0) goto L_0x00d7;
    L_0x00d0:
        r2 = r5.getConnectionManager();
        r2.shutdown();
    L_0x00d7:
        throw r1;
    L_0x00d8:
        r2 = r9[r6];	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r2 = r2.getValue();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        if (r2 == 0) goto L_0x00f4;
    L_0x00e0:
        r11 = "";
        r11 = r2.equals(r11);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        if (r11 != 0) goto L_0x00f4;
    L_0x00e8:
        r11 = ";";
        r11 = r2.split(r11);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        if (r11 == 0) goto L_0x00f4;
    L_0x00f0:
        r12 = r11.length;	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r2 = 0;
    L_0x00f2:
        if (r2 < r12) goto L_0x00f9;
    L_0x00f4:
        r2 = r6 + 1;
        r6 = r2;
        goto L_0x003f;
    L_0x00f9:
        r13 = r11[r2];	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r14 = r13.trim();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r14 = r14.toLowerCase();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r15 = "charset=";
        r14 = r14.startsWith(r15);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        if (r14 == 0) goto L_0x011c;
    L_0x010b:
        r1 = r13.trim();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r1 = r1.toLowerCase();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r2 = "charset=";
        r11 = "";
        r1 = r1.replace(r2, r11);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        goto L_0x00f4;
    L_0x011c:
        r2 = r2 + 1;
        goto L_0x00f2;
    L_0x011f:
        r11 = r9[r2];	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r12 = r11.getValue();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        if (r12 == 0) goto L_0x0137;
    L_0x0127:
        r12 = r11.getValue();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        r13 = "";
        r12 = r12.equals(r13);	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
        if (r12 != 0) goto L_0x0137;
    L_0x0133:
        r1 = r11.getValue();	 Catch:{ Exception -> 0x00b4, all -> 0x0149 }
    L_0x0137:
        r2 = r2 + 1;
        goto L_0x004d;
    L_0x013b:
        r7.append(r1);	 Catch:{ Exception -> 0x0140, all -> 0x0150 }
        goto L_0x007c;
    L_0x0140:
        r1 = move-exception;
        r3 = r4;
        r4 = r5;
        goto L_0x00b8;
    L_0x0145:
        r1 = move-exception;
        r5 = r2;
        goto L_0x00c4;
    L_0x0149:
        r1 = move-exception;
        goto L_0x00c4;
    L_0x014c:
        r1 = move-exception;
        r4 = r2;
        goto L_0x00c4;
    L_0x0150:
        r1 = move-exception;
        r3 = r2;
        goto L_0x00c4;
    L_0x0154:
        r1 = move-exception;
        r16 = r3;
        r3 = r4;
        r4 = r2;
        r2 = r16;
        goto L_0x00b8;
    L_0x015d:
        r1 = move-exception;
        r4 = r5;
        r16 = r2;
        r2 = r3;
        r3 = r16;
        goto L_0x00b8;
    L_0x0166:
        r1 = move-exception;
        r2 = r3;
        r3 = r4;
        r4 = r5;
        goto L_0x00b8;
    L_0x016c:
        r4 = r2;
        goto L_0x0072;
    L_0x016f:
        r2 = r3;
        goto L_0x0082;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.autonavi.gelocator.api.NetManagerApache.doPostAsString(java.lang.String, java.util.Hashtable):java.lang.String");
    }

    public String doPostXmlAsString(String str, String str2) {
        InputStream gZIPInputStream;
        BufferedReader bufferedReader;
        Throwable e;
        StringBuffer stringBuffer = new StringBuffer();
        HttpClient httpClient = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader2 = null;
        HttpClient httpClient2;
        try {
            httpClient2 = getHttpClient();
            try {
                HttpUriRequest httpPost = new HttpPost(str);
                HttpEntity stringEntity = new StringEntity(str2, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                stringEntity.setContentType("text/xml");
                httpPost.setHeader(MIME.CONTENT_TYPE, "application/soap+xml;charset=UTF-8");
                httpPost.setEntity(stringEntity);
                HttpResponse execute = httpClient2.execute(httpPost);
                String str3 = "utf-8";
                Header[] headers = execute.getHeaders(MIME.CONTENT_TYPE);
                if (headers != null) {
                    for (Header value : headers) {
                        String value2 = value.getValue();
                        if (!(value2 == null || value2.equals(XmlPullParser.NO_NAMESPACE))) {
                            String[] split = value2.split(";");
                            if (split != null) {
                                for (String str4 : split) {
                                    if (str4.trim().toLowerCase().startsWith("charset=")) {
                                        str3 = str4.trim().toLowerCase().replace("charset=", XmlPullParser.NO_NAMESPACE);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                String str5 = str3;
                str3 = null;
                headers = execute.getHeaders("Content-Encoding");
                if (headers != null) {
                    for (Header header : headers) {
                        if (!(header.getValue() == null || header.getValue().equals(XmlPullParser.NO_NAMESPACE))) {
                            str3 = header.getValue();
                        }
                    }
                }
                if (execute.getStatusLine().getStatusCode() == AsyncTaskManager.REQUEST_SUCCESS_CODE) {
                    inputStream = execute.getEntity().getContent();
                    gZIPInputStream = (str3 == null || !str3.equals("gzip")) ? inputStream : new GZIPInputStream(inputStream);
                    try {
                        bufferedReader = new BufferedReader(new InputStreamReader(gZIPInputStream, str5));
                        while (true) {
                            try {
                                str3 = bufferedReader.readLine();
                                if (str3 == null) {
                                    break;
                                }
                                stringBuffer.append(str3);
                            } catch (Exception e2) {
                                e = e2;
                                bufferedReader2 = bufferedReader;
                                inputStream = gZIPInputStream;
                                httpClient = httpClient2;
                            } catch (Throwable th) {
                                e = th;
                                bufferedReader2 = bufferedReader;
                                inputStream = gZIPInputStream;
                            }
                        }
                    } catch (Exception e3) {
                        e = e3;
                        inputStream = gZIPInputStream;
                        httpClient = httpClient2;
                        try {
                            throw new Exception("Net Exception", e);
                        } catch (Throwable th2) {
                            e = th2;
                            httpClient2 = httpClient;
                            if (bufferedReader2 != null) {
                                bufferedReader2.close();
                            }
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            if (httpClient2 != null) {
                                httpClient2.getConnectionManager().shutdown();
                            }
                            throw e;
                        }
                    } catch (Throwable th3) {
                        e = th3;
                        inputStream = gZIPInputStream;
                        if (bufferedReader2 != null) {
                            bufferedReader2.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (httpClient2 != null) {
                            httpClient2.getConnectionManager().shutdown();
                        }
                        throw e;
                    }
                }
                gZIPInputStream = null;
                bufferedReader = null;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (gZIPInputStream != null) {
                    gZIPInputStream.close();
                }
                if (httpClient2 != null) {
                    httpClient2.getConnectionManager().shutdown();
                }
                return stringBuffer.toString();
            } catch (Exception e4) {
                e = e4;
                httpClient = httpClient2;
            } catch (Throwable th4) {
                e = th4;
            }
        } catch (Exception e5) {
            e = e5;
            throw new Exception("Net Exception", e);
        } catch (Throwable th5) {
            e = th5;
            httpClient2 = null;
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (httpClient2 != null) {
                httpClient2.getConnectionManager().shutdown();
            }
            throw e;
        }
    }

    public HttpClient getHttpClient() {
        Object obj;
        String defaultHost;
        HttpClient defaultHttpClient = new DefaultHttpClient();
        ConnectivityManager connectivityManager = (ConnectivityManager) this.f787a.getSystemService("connectivity");
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.getTypeName().equals("WIFI")) {
                obj = 1;
                if (obj == null) {
                    defaultHost = Proxy.getDefaultHost();
                    if (!(defaultHost == null || defaultHost.equals(XmlPullParser.NO_NAMESPACE))) {
                        defaultHttpClient.getParams().setParameter("http.route.default-proxy", new HttpHost(defaultHost, Proxy.getDefaultPort()));
                    }
                }
                HttpConnectionParams.setConnectionTimeout(defaultHttpClient.getParams(), BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
                HttpConnectionParams.setSoTimeout(defaultHttpClient.getParams(), BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
                return defaultHttpClient;
            }
        }
        obj = null;
        if (obj == null) {
            defaultHost = Proxy.getDefaultHost();
            defaultHttpClient.getParams().setParameter("http.route.default-proxy", new HttpHost(defaultHost, Proxy.getDefaultPort()));
        }
        HttpConnectionParams.setConnectionTimeout(defaultHttpClient.getParams(), BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        HttpConnectionParams.setSoTimeout(defaultHttpClient.getParams(), BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        return defaultHttpClient;
    }
}
