package com.ifoer.util;

import android.app.Activity;
import android.app.Application;
import android.os.Process;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class MyApplication extends Application {
    private static String hexString;
    private static MyApplication instance;
    private boolean f1311D;
    private List<Activity> activityList;
    private HttpClient httpClient;

    private MyApplication() {
        this.activityList = new LinkedList();
        this.f1311D = false;
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        this.activityList.add(activity);
    }

    public void exit() {
        for (Activity activity : this.activityList) {
            activity.finish();
        }
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    public void exitActivity(String name) {
        for (Activity activity : this.activityList) {
            if (activity.getClass().getName().equalsIgnoreCase(name)) {
                activity.finish();
            }
        }
    }

    public static String stringToHexString(String strPart) {
        String hexString = XmlPullParser.NO_NAMESPACE;
        for (int i = 0; i < strPart.length(); i++) {
            hexString = new StringBuilder(String.valueOf(hexString)).append(Integer.toHexString(strPart.charAt(i))).toString();
        }
        return hexString;
    }

    static {
        hexString = "0123456789ABCDEF";
    }

    public static String encode(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i] & 240) >> 4));
            sb.append(hexString.charAt((bytes[i] & 15) >> 0));
        }
        return sb.toString();
    }

    public static byte[] decode(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        for (int i = 0; i < bytes.length(); i += 2) {
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4) | hexString.indexOf(bytes.charAt(i + 1)));
        }
        return baos.toByteArray();
    }

    public void onCreate() {
        super.onCreate();
        this.httpClient = createHttpClient();
    }

    public void onLowMemory() {
        super.onLowMemory();
        shutdownHttpClient();
    }

    public void onTerminate() {
        super.onTerminate();
        shutdownHttpClient();
    }

    private HttpClient createHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "ISO-8859-1");
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpConnectionParams.setConnectionTimeout(params, BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(params, Flags.FLAG2);
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        return new DefaultHttpClient(new ThreadSafeClientConnManager(params, schReg), params);
    }

    private void shutdownHttpClient() {
        if (this.httpClient != null && this.httpClient.getConnectionManager() != null) {
            this.httpClient.getConnectionManager().shutdown();
        }
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }
}
