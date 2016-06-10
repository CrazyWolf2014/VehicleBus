package cn.sharesdk.framework.p000a;

import cn.sharesdk.framework.utils.C0052a;
import cn.sharesdk.framework.utils.C0058e;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MIME;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.json.HTTP;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: cn.sharesdk.framework.a.g */
public class C0025g {
    public static int f13a;
    public static int f14b;

    private String m32a(ArrayList<C0023d<String>> arrayList) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            C0023d c0023d = (C0023d) it.next();
            String c = C0052a.m172c(c0023d.f9a, "utf-8");
            String c2 = c0023d.f10b != null ? C0052a.m172c((String) c0023d.f10b, "utf-8") : XmlPullParser.NO_NAMESPACE;
            if (stringBuilder.length() > 0) {
                stringBuilder.append('&');
            }
            stringBuilder.append(c).append(SignatureVisitor.INSTANCEOF).append(c2);
        }
        return stringBuilder.toString();
    }

    private HttpClient m33a() {
        KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
        instance.load(null, null);
        SocketFactory c0026i = new C0026i(instance);
        c0026i.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(basicHttpParams, AsyncHttpResponseHandler.DEFAULT_CHARSET);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", c0026i, 443));
        return new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
    }

    private HttpPost m34a(String str, ArrayList<C0023d<String>> arrayList) {
        HttpPost httpPost = new HttpPost(str);
        if (arrayList != null) {
            C1013k c1013k = new C1013k();
            c1013k.m1779a(m32a(arrayList));
            HttpEntity c = c1013k.m29c();
            c.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(c);
        }
        return httpPost;
    }

    private HttpPost m35a(String str, ArrayList<C0023d<String>> arrayList, C0023d<String> c0023d) {
        HttpPost httpPost = new HttpPost(str);
        String uuid = UUID.randomUUID().toString();
        C1011e c1011e = new C1011e();
        C0022c c1013k = new C1013k();
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                C0023d c0023d2 = (C0023d) it.next();
                c1013k.m1779a("--").m1779a(uuid).m1779a(HTTP.CRLF);
                c1013k.m1779a("content-disposition: form-data; name=\"").m1779a(c0023d2.f9a).m1779a("\"\r\n\r\n");
                c1013k.m1779a((String) c0023d2.f10b).m1779a(HTTP.CRLF);
            }
        }
        httpPost.setHeader(MIME.CONTENT_TYPE, "multipart/form-data; boundary=" + uuid);
        c1013k.m1779a("--").m1779a(uuid).m1779a(HTTP.CRLF);
        c1013k.m1779a("Content-Disposition: form-data; name=\"").m1779a(c0023d.f9a).m1779a("\"; filename=\"").m1779a(new File((String) c0023d.f10b).getName()).m1779a("\"\r\n");
        String contentTypeFor = URLConnection.getFileNameMap().getContentTypeFor((String) c0023d.f10b);
        if (contentTypeFor == null || contentTypeFor.length() <= 0) {
            if (((String) c0023d.f10b).toLowerCase().endsWith("jpg") || ((String) c0023d.f10b).toLowerCase().endsWith("jepg")) {
                contentTypeFor = "image/jpeg";
            } else if (((String) c0023d.f10b).toLowerCase().endsWith("png")) {
                contentTypeFor = "image/png";
            } else if (((String) c0023d.f10b).toLowerCase().endsWith("gif")) {
                contentTypeFor = "image/gif";
            } else {
                InputStream fileInputStream = new FileInputStream((String) c0023d.f10b);
                contentTypeFor = URLConnection.guessContentTypeFromStream(fileInputStream);
                fileInputStream.close();
                if (contentTypeFor == null || contentTypeFor.length() <= 0) {
                    contentTypeFor = "application/octet-stream";
                }
            }
        }
        c1013k.m1779a("Content-Type: ").m1779a(contentTypeFor).m1779a("\r\n\r\n");
        c1011e.m1765a(c1013k);
        c1013k = new C1010b();
        c1013k.m1763a((String) c0023d.f10b);
        c1011e.m1765a(c1013k);
        C0022c c1013k2 = new C1013k();
        c1013k2.m1779a("\r\n--").m1779a(uuid).m1779a("--\r\n");
        c1011e.m1765a(c1013k2);
        httpPost.setEntity(c1011e.m29c());
        return httpPost;
    }

    public InputStream m36a(String str, ArrayList<C0023d<String>> arrayList, C0022c c0022c) {
        HttpUriRequest httpPost = new HttpPost(str);
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                C0023d c0023d = (C0023d) it.next();
                httpPost.setHeader(c0023d.f9a, (String) c0023d.f10b);
            }
        }
        httpPost.setEntity(c0022c.m29c());
        HttpResponse execute = (str.startsWith("https://") ? m33a() : new DefaultHttpClient()).execute(httpPost);
        if (execute.getStatusLine().getStatusCode() == AsyncTaskManager.REQUEST_SUCCESS_CODE) {
            return execute.getEntity().getContent();
        }
        throw new Throwable(EntityUtils.toString(execute.getEntity(), "utf-8"));
    }

    public String m37a(String str, ArrayList<C0023d<String>> arrayList, C0023d<String> c0023d, ArrayList<C0023d<String>> arrayList2, ArrayList<C0023d<?>> arrayList3) {
        HttpUriRequest a;
        C0058e.m219b("httpPost: " + str, new Object[0]);
        if (c0023d == null || c0023d.f10b == null || !new File((String) c0023d.f10b).exists()) {
            Object a2 = m34a(str, (ArrayList) arrayList);
        } else {
            a = m35a(str, (ArrayList) arrayList, (C0023d) c0023d);
        }
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                C0023d c0023d2 = (C0023d) it.next();
                a.setHeader(c0023d2.f9a, (String) c0023d2.f10b);
            }
        }
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, f13a);
        HttpConnectionParams.setSoTimeout(basicHttpParams, f14b);
        a.setParams(basicHttpParams);
        HttpResponse execute = (str.startsWith("https://") ? m33a() : new DefaultHttpClient()).execute(a);
        int statusCode = execute.getStatusLine().getStatusCode();
        if (statusCode == AsyncTaskManager.REQUEST_SUCCESS_CODE || statusCode == 201) {
            return EntityUtils.toString(execute.getEntity(), "utf-8");
        }
        throw new Throwable(EntityUtils.toString(execute.getEntity(), "utf-8"));
    }

    public String m38a(String str, ArrayList<C0023d<String>> arrayList, ArrayList<C0023d<String>> arrayList2, ArrayList<C0023d<?>> arrayList3) {
        C0058e.m219b("httpGet: " + str, new Object[0]);
        if (arrayList != null) {
            String a = m32a(arrayList);
            if (a.length() > 0) {
                str = str + "?" + a;
            }
        }
        HttpUriRequest httpGet = new HttpGet(str);
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                C0023d c0023d = (C0023d) it.next();
                httpGet.setHeader(c0023d.f9a, (String) c0023d.f10b);
            }
        }
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, f13a);
        HttpConnectionParams.setSoTimeout(basicHttpParams, f14b);
        httpGet.setParams(basicHttpParams);
        HttpClient a2 = str.startsWith("https://") ? m33a() : new DefaultHttpClient();
        HttpResponse execute = a2.execute(httpGet);
        if (execute.getStatusLine().getStatusCode() == AsyncTaskManager.REQUEST_SUCCESS_CODE) {
            String entityUtils = EntityUtils.toString(execute.getEntity(), "utf-8");
            a2.getConnectionManager().shutdown();
            return entityUtils;
        }
        throw new Throwable(EntityUtils.toString(execute.getEntity(), "utf-8"));
    }

    public void m39a(String str, File file) {
        HttpUriRequest httpGet = new HttpGet(str);
        HttpClient a = str.startsWith("https://") ? m33a() : new DefaultHttpClient();
        HttpResponse execute = a.execute(httpGet);
        if (execute.getStatusLine().getStatusCode() == AsyncTaskManager.REQUEST_SUCCESS_CODE) {
            InputStream content = execute.getEntity().getContent();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[Flags.FLAG5];
            for (int read = content.read(bArr); read > 0; read = content.read(bArr)) {
                fileOutputStream.write(bArr, 0, read);
            }
            fileOutputStream.flush();
            content.close();
            fileOutputStream.close();
            a.getConnectionManager().shutdown();
            return;
        }
        throw new Throwable(EntityUtils.toString(execute.getEntity(), "utf-8"));
    }

    public String m40b(String str, ArrayList<C0023d<String>> arrayList, C0023d<String> c0023d, ArrayList<C0023d<String>> arrayList2, ArrayList<C0023d<?>> arrayList3) {
        C0058e.m219b("httpPut: " + str, new Object[0]);
        if (arrayList != null) {
            String a = m32a(arrayList);
            if (a.length() > 0) {
                str = str + "?" + a;
            }
        }
        HttpUriRequest httpPut = new HttpPut(str);
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                C0023d c0023d2 = (C0023d) it.next();
                httpPut.setHeader(c0023d2.f9a, (String) c0023d2.f10b);
            }
        }
        C1010b c1010b = new C1010b();
        c1010b.m1763a((String) c0023d.f10b);
        HttpEntity c = c1010b.m29c();
        c.setContentEncoding("application/octet-stream");
        httpPut.setEntity(c);
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, f13a);
        HttpConnectionParams.setSoTimeout(basicHttpParams, f14b);
        httpPut.setParams(basicHttpParams);
        HttpResponse execute = (str.startsWith("https://") ? m33a() : new DefaultHttpClient()).execute(httpPut);
        int statusCode = execute.getStatusLine().getStatusCode();
        if (statusCode == AsyncTaskManager.REQUEST_SUCCESS_CODE || statusCode == 201) {
            return EntityUtils.toString(execute.getEntity(), "utf-8");
        }
        throw new Throwable(EntityUtils.toString(execute.getEntity(), "utf-8"));
    }
}
