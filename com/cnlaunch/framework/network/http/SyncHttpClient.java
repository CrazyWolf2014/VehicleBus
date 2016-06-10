package com.cnlaunch.framework.network.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.cnlaunch.framework.common.CacheManager;
import com.cnlaunch.framework.utils.NLog;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.mime.MIME;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class SyncHttpClient {
    private static final String ASSETS_PATH = "assets";
    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    private static final int DEFAULT_SOCKET_TIMEOUT = 60000;
    private static final String ENCODE_UTF8 = "UTF-8";
    private static final String ENCODING_GZIP = "gzip";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String LOG_TAG = "AsyncHttpClient";
    private static final String VERSION = "1.4.4";
    private static PersistentCookieStore cookieStore;
    private static SyncHttpClient instance;
    private final Map<String, String> clientHeaderMap;
    private final DefaultHttpClient httpClient;
    private final HttpContext httpContext;
    private boolean isUrlEncodingEnabled;
    private int maxConnections;
    private final Map<Context, List<WeakReference<Future<?>>>> requestMap;
    private final String tag;
    private int timeout;

    /* renamed from: com.cnlaunch.framework.network.http.SyncHttpClient.1 */
    class C01291 implements HttpRequestInterceptor {
        C01291() {
        }

        public void process(HttpRequest request, HttpContext context) {
            if (!request.containsHeader(SyncHttpClient.HEADER_ACCEPT_ENCODING)) {
                request.addHeader(SyncHttpClient.HEADER_ACCEPT_ENCODING, SyncHttpClient.ENCODING_GZIP);
            }
            for (String header : SyncHttpClient.this.clientHeaderMap.keySet()) {
                request.addHeader(header, (String) SyncHttpClient.this.clientHeaderMap.get(header));
            }
        }
    }

    /* renamed from: com.cnlaunch.framework.network.http.SyncHttpClient.2 */
    class C01302 implements HttpResponseInterceptor {
        C01302() {
        }

        public void process(HttpResponse response, HttpContext context) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                Header encoding = entity.getContentEncoding();
                if (encoding != null) {
                    for (HeaderElement element : encoding.getElements()) {
                        if (element.getName().equalsIgnoreCase(SyncHttpClient.ENCODING_GZIP)) {
                            response.setEntity(new InflatingEntity(entity));
                            return;
                        }
                    }
                }
            }
        }
    }

    /* renamed from: com.cnlaunch.framework.network.http.SyncHttpClient.3 */
    class C01313 extends DefaultRedirectHandler {
        private final /* synthetic */ boolean val$enableRedirects;

        C01313(boolean z) {
            this.val$enableRedirects = z;
        }

        public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
            return this.val$enableRedirects;
        }
    }

    private static class InflatingEntity extends HttpEntityWrapper {
        public InflatingEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        public InputStream getContent() throws IOException {
            return new GZIPInputStream(this.wrappedEntity.getContent());
        }

        public long getContentLength() {
            return -1;
        }
    }

    public static SyncHttpClient getInstance(Context context) {
        if (instance == null) {
            synchronized (SyncHttpClient.class) {
                if (instance == null) {
                    instance = new SyncHttpClient();
                }
            }
        }
        cookieStore = new PersistentCookieStore(context);
        instance.setCookieStore(cookieStore);
        return instance;
    }

    public SyncHttpClient() {
        this(false, 80, 443);
    }

    public SyncHttpClient(int httpPort) {
        this(false, httpPort, 443);
    }

    public SyncHttpClient(int httpPort, int httpsPort) {
        this(false, httpPort, httpsPort);
    }

    public SyncHttpClient(boolean fixNoHttpResponseException, int httpPort, int httpsPort) {
        this(getDefaultSchemeRegistry(fixNoHttpResponseException, httpPort, httpsPort));
    }

    private static SchemeRegistry getDefaultSchemeRegistry(boolean fixNoHttpResponseException, int httpPort, int httpsPort) {
        SSLSocketFactory sslSocketFactory;
        if (fixNoHttpResponseException) {
            Log.d(LOG_TAG, "Beware! Using the fix is insecure, as it doesn't verify SSL certificates.");
        }
        if (httpPort < 1) {
            httpPort = 80;
            Log.d(LOG_TAG, "Invalid HTTP port number specified, defaulting to 80");
        }
        if (httpsPort < 1) {
            httpsPort = 443;
            Log.d(LOG_TAG, "Invalid HTTPS port number specified, defaulting to 443");
        }
        if (fixNoHttpResponseException) {
            sslSocketFactory = MySSLSocketFactory.getFixedSocketFactory();
        } else {
            sslSocketFactory = SSLSocketFactory.getSocketFactory();
        }
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), httpPort));
        schemeRegistry.register(new Scheme("https", sslSocketFactory, httpsPort));
        return schemeRegistry;
    }

    public SyncHttpClient(SchemeRegistry schemeRegistry) {
        this.tag = SyncHttpClient.class.getSimpleName();
        this.maxConnections = DEFAULT_MAX_CONNECTIONS;
        this.timeout = DEFAULT_SOCKET_TIMEOUT;
        this.isUrlEncodingEnabled = true;
        BasicHttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setTimeout(httpParams, (long) this.timeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(this.maxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
        HttpConnectionParams.setSoTimeout(httpParams, this.timeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, this.timeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUserAgent(httpParams, String.format("android-async-http/%s", new Object[]{VERSION}));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        this.requestMap = new WeakHashMap();
        this.clientHeaderMap = new HashMap();
        this.httpContext = new SyncBasicHttpContext(new BasicHttpContext());
        this.httpClient = new DefaultHttpClient(cm, httpParams);
        this.httpClient.addRequestInterceptor(new C01291());
        this.httpClient.addResponseInterceptor(new C01302());
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public HttpContext getHttpContext() {
        return this.httpContext;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.httpContext.setAttribute("http.cookie-store", cookieStore);
    }

    public void setEnableRedirects(boolean enableRedirects) {
        this.httpClient.setRedirectHandler(new C01313(enableRedirects));
    }

    public void setUserAgent(String userAgent) {
        HttpProtocolParams.setUserAgent(this.httpClient.getParams(), userAgent);
    }

    public int getMaxConnections() {
        return this.maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        if (maxConnections < 1) {
            maxConnections = DEFAULT_MAX_CONNECTIONS;
        }
        this.maxConnections = maxConnections;
        ConnManagerParams.setMaxConnectionsPerRoute(this.httpClient.getParams(), new ConnPerRouteBean(this.maxConnections));
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        if (timeout < 1000) {
            timeout = DEFAULT_SOCKET_TIMEOUT;
        }
        this.timeout = timeout;
        HttpParams httpParams = this.httpClient.getParams();
        ConnManagerParams.setTimeout(httpParams, (long) this.timeout);
        HttpConnectionParams.setSoTimeout(httpParams, this.timeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, this.timeout);
    }

    public void setProxy(String hostname, int port) {
        this.httpClient.getParams().setParameter("http.route.default-proxy", new HttpHost(hostname, port));
    }

    public void setProxy(String hostname, int port, String username, String password) {
        this.httpClient.getCredentialsProvider().setCredentials(new AuthScope(hostname, port), new UsernamePasswordCredentials(username, password));
        this.httpClient.getParams().setParameter("http.route.default-proxy", new HttpHost(hostname, port));
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sslSocketFactory, 443));
    }

    public void setMaxRetriesAndTimeout(int retries, int timeout) {
        this.httpClient.setHttpRequestRetryHandler(new RetryHandler(retries, timeout));
    }

    public void addHeader(String header, String value) {
        this.clientHeaderMap.put(header, value);
    }

    public void removeHeader(String header) {
        this.clientHeaderMap.remove(header);
    }

    public void setBasicAuth(String username, String password) {
        setBasicAuth(username, password, AuthScope.ANY);
    }

    public void setBasicAuth(String username, String password, AuthScope scope) {
        this.httpClient.getCredentialsProvider().setCredentials(scope, new UsernamePasswordCredentials(username, password));
    }

    public void clearBasicAuth() {
        this.httpClient.getCredentialsProvider().clear();
    }

    public void cancelRequests(Context context, boolean mayInterruptIfRunning) {
        List<WeakReference<Future<?>>> requestList = (List) this.requestMap.get(context);
        if (requestList != null) {
            for (WeakReference<Future<?>> requestRef : requestList) {
                Future<?> request = (Future) requestRef.get();
                if (request != null) {
                    request.cancel(mayInterruptIfRunning);
                }
            }
        }
        this.requestMap.remove(context);
    }

    public String get(String url) throws HttpException {
        return get(null, url, null);
    }

    public String get(String url, RequestParams params) throws HttpException {
        return get(null, url, params);
    }

    public String get(Context context, String url) throws HttpException {
        return get(context, url, null);
    }

    public String get(Context context, String url, RequestParams params) throws HttpException {
        return sendRequest(this.httpClient, this.httpContext, new HttpGet(getUrlWithQueryString(this.isUrlEncodingEnabled, url, params)), null, context);
    }

    public String get(Context context, String url, Header[] headers, RequestParams params) throws HttpException {
        HttpUriRequest request = new HttpGet(getUrlWithQueryString(this.isUrlEncodingEnabled, url, params));
        if (headers != null) {
            request.setHeaders(headers);
        }
        return sendRequest(this.httpClient, this.httpContext, request, null, context);
    }

    public String post(String url) throws HttpException {
        return post(null, url, null);
    }

    public String post(String url, RequestParams params) throws HttpException {
        return post(null, url, params);
    }

    public String post(Context context, String url, RequestParams params) throws HttpException {
        return post(context, url, paramsToEntity(params), null);
    }

    public String post(Context context, String url, HttpEntity entity, String contentType) throws HttpException {
        return sendRequest(this.httpClient, this.httpContext, addEntityToRequestBase(new HttpPost(url), entity), contentType, context);
    }

    public String post(Context context, String url, Header[] headers, RequestParams params, String contentType) throws HttpException {
        HttpEntityEnclosingRequestBase request = new HttpPost(url);
        if (params != null) {
            request.setEntity(paramsToEntity(params));
        }
        if (headers != null) {
            request.setHeaders(headers);
        }
        return sendRequest(this.httpClient, this.httpContext, request, contentType, context);
    }

    public String post(Context context, String url, Header[] headers, HttpEntity entity, String contentType) throws HttpException {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPost(url), entity);
        if (headers != null) {
            request.setHeaders(headers);
        }
        return sendRequest(this.httpClient, this.httpContext, request, contentType, context);
    }

    protected String sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, Context context) throws HttpException {
        String responseBody = XmlPullParser.NO_NAMESPACE;
        if (contentType != null) {
            uriRequest.addHeader(MIME.CONTENT_TYPE, contentType);
        }
        List<Cookie> list = cookieStore.getCookies();
        if (list != null && list.size() > 0) {
            for (Cookie cookie : list) {
                uriRequest.setHeader("Cookie", cookie.getValue());
            }
        }
        try {
            String str;
            Object[] objArr;
            URI uri = uriRequest.getURI();
            NLog.m917e(this.tag, "url : " + uri.toString());
            String scheme = uri.getScheme();
            if (!TextUtils.isEmpty(scheme)) {
                if (ASSETS_PATH.equals(scheme)) {
                    String fileName = uri.getAuthority();
                    responseBody = inputSteamToString(context.getAssets().open(fileName));
                    str = this.tag;
                    objArr = new Object[1];
                    objArr[0] = "responseBody : " + responseBody;
                    NLog.m917e(str, objArr);
                    return responseBody;
                }
            }
            HttpResponse response = client.execute(uriRequest, httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseBody = EntityUtils.toString(new BufferedHttpEntity(entity), ENCODE_UTF8);
                if (NLog.isDebug) {
                    CacheManager.saveTestData(responseBody, uriRequest.getURI().toString().replace(FilePathGenerator.ANDROID_DIR_SEP, XmlPullParser.NO_NAMESPACE).replaceAll("[?.=&:]", "_"));
                }
                str = this.tag;
                objArr = new Object[1];
                objArr[0] = "responseBody : " + responseBody;
                NLog.m917e(str, objArr);
            }
            Header[] headers = response.getHeaders("Set-Cookie");
            if (headers != null && headers.length > 0) {
                int i = 0;
                while (true) {
                    int length = headers.length;
                    if (i >= r0) {
                        break;
                    }
                    String cookie2 = headers[i].getValue();
                    BasicClientCookie newCookie = new BasicClientCookie("cookie" + i, cookie2);
                    cookieStore.addCookie(newCookie);
                    i++;
                }
            }
            return responseBody;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new HttpException(e);
        }
    }

    public void setURLEncodingEnabled(boolean enabled) {
        this.isUrlEncodingEnabled = enabled;
    }

    public static String getUrlWithQueryString(boolean shouldEncodeUrl, String url, RequestParams params) {
        if (shouldEncodeUrl) {
            url = url.replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, "%20");
        }
        if (params == null) {
            return url;
        }
        String paramString = params.getParamString();
        if (url.contains("?")) {
            return new StringBuilder(String.valueOf(url)).append(AlixDefine.split).append(paramString).toString();
        }
        return new StringBuilder(String.valueOf(url)).append("?").append(paramString).toString();
    }

    private HttpEntity paramsToEntity(RequestParams params) {
        HttpEntity entity = null;
        if (params != null) {
            try {
                NLog.m917e(this.tag, "params : " + params.toString());
                entity = params.getEntity(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    public boolean isUrlEncodingEnabled() {
        return this.isUrlEncodingEnabled;
    }

    private HttpEntityEnclosingRequestBase addEntityToRequestBase(HttpEntityEnclosingRequestBase requestBase, HttpEntity entity) {
        if (entity != null) {
            requestBase.setEntity(entity);
        }
        return requestBase;
    }

    public static String inputSteamToString(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[Flags.FLAG5];
        while (true) {
            int count = in.read(data, 0, Flags.FLAG5);
            if (count == -1) {
                return new String(outStream.toByteArray(), ENCODE_UTF8);
            }
            outStream.write(data, 0, count);
        }
    }
}
