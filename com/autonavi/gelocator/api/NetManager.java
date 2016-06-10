package com.autonavi.gelocator.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Proxy;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import org.xmlpull.v1.XmlPullParser;

public class NetManager {
    private static String f783b;
    private static NetManager f784c;
    private Context f785a;

    static {
        f783b = null;
        f784c = null;
    }

    private NetManager() {
    }

    public static NetManager getInstance(Context context) {
        if (f784c == null) {
            NetManager netManager = new NetManager();
            f784c = netManager;
            netManager.f785a = context.getApplicationContext();
        }
        return f784c;
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

    public InputStream dogetAsStream(String str) {
        try {
            HttpURLConnection uRLConnection = getURLConnection(str);
            uRLConnection.setRequestMethod("GET");
            String headerField = uRLConnection.getHeaderField("Content-Encoding");
            return (headerField == null || !headerField.equals("gzip")) ? uRLConnection.getInputStream() : new GZIPInputStream(uRLConnection.getInputStream());
        } catch (Throwable e) {
            throw new Exception("Net Exception", e);
        }
    }

    public String dogetAsString(String str) {
        try {
            HttpURLConnection uRLConnection = getURLConnection(str);
            uRLConnection.setRequestMethod("GET");
            String headerField = uRLConnection.getHeaderField("Content-Encoding");
            InputStream inputStream = (headerField == null || !headerField.equals("gzip")) ? uRLConnection.getInputStream() : new GZIPInputStream(uRLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    inputStream.close();
                    return stringBuffer.toString();
                }
                stringBuffer.append(readLine);
            }
        } catch (Throwable e) {
            throw new Exception("Net Exception", e);
        }
    }

    public InputStream dopostAsInputStream(String str, String str2) {
        try {
            HttpURLConnection uRLConnection = getURLConnection(str);
            uRLConnection.setRequestMethod("POST");
            uRLConnection.setDoInput(true);
            uRLConnection.setDoOutput(true);
            OutputStream outputStream = uRLConnection.getOutputStream();
            outputStream.write(str2.getBytes());
            outputStream.flush();
            outputStream.close();
            String headerField = uRLConnection.getHeaderField("Content-Encoding");
            return (headerField == null || !headerField.equals("gzip")) ? uRLConnection.getInputStream() : new GZIPInputStream(uRLConnection.getInputStream());
        } catch (Throwable e) {
            throw new Exception("Net Exception", e);
        }
    }

    public String dopostAsString(String str, String str2) {
        try {
            HttpURLConnection uRLConnection = getURLConnection(str);
            uRLConnection.setRequestMethod("POST");
            uRLConnection.setDoInput(true);
            uRLConnection.setDoOutput(true);
            OutputStream outputStream = uRLConnection.getOutputStream();
            outputStream.write(str2.getBytes());
            outputStream.flush();
            outputStream.close();
            String headerField = uRLConnection.getHeaderField("Content-Encoding");
            InputStream inputStream = (headerField == null || !headerField.equals("gzip")) ? uRLConnection.getInputStream() : new GZIPInputStream(uRLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    inputStream.close();
                    return stringBuffer.toString();
                }
                stringBuffer.append(readLine);
            }
        } catch (Throwable e) {
            throw new Exception("Net Exception", e);
        }
    }

    public HttpURLConnection getURLConnection(String str) {
        Object obj;
        HttpURLConnection httpURLConnection;
        String defaultHost;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.f785a.getSystemService("connectivity");
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.getTypeName().equals("WIFI")) {
                obj = 1;
                if (obj == null) {
                    httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                } else {
                    defaultHost = Proxy.getDefaultHost();
                    if (defaultHost != null || defaultHost.equals(XmlPullParser.NO_NAMESPACE)) {
                        httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                    } else {
                        httpURLConnection = (HttpURLConnection) new URL(str).openConnection(new java.net.Proxy(Type.HTTP, new InetSocketAddress(Proxy.getDefaultHost(), Proxy.getDefaultPort())));
                    }
                }
                httpURLConnection.setConnectTimeout(4000);
                return httpURLConnection;
            }
        }
        obj = null;
        if (obj == null) {
            defaultHost = Proxy.getDefaultHost();
            if (defaultHost != null) {
            }
            httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        } else {
            httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        }
        httpURLConnection.setConnectTimeout(4000);
        return httpURLConnection;
    }
}
