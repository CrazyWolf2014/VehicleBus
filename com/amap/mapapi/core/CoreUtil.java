package com.amap.mapapi.core;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.text.Html;
import android.text.Spanned;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.core.d */
public class CoreUtil {
    public static boolean f346a;
    static float[] f347b;
    private static String f348c;

    static {
        f348c = null;
        f346a = true;
        f347b = new float[9];
    }

    public static boolean m488a(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static long m483a() {
        return (System.nanoTime() * 1000) / 1000000000;
    }

    public static int m482a(int i) {
        return (int) ((1117 * ((long) i)) / 10000);
    }

    public static int m489b(int i) {
        return (int) ((((long) i) * 1000000) / 111700);
    }

    public static Document m487a(InputStream inputStream) {
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    public static Document m492b(String str) {
        return CoreUtil.m487a(new ByteArrayInputStream(str.getBytes()));
    }

    public static String m485a(Context context) {
        if (f348c == null) {
            char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            String str = XmlPullParser.NO_NAMESPACE;
            try {
                Signature[] signatureArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures;
                MessageDigest instance = MessageDigest.getInstance("MD5");
                instance.update(signatureArr[0].toByteArray());
                byte[] digest = instance.digest();
                String str2 = str;
                int i = 0;
                while (i < digest.length) {
                    int i2 = digest[i] < null ? digest[i] + KEYRecord.OWNER_ZONE : digest[i];
                    String str3 = (str2 + cArr[i2 / 16]) + cArr[i2 % 16];
                    if (i != digest.length - 1) {
                        str3 = str3 + ":";
                    }
                    i++;
                    str2 = str3;
                }
                f348c = str2;
            } catch (NoSuchAlgorithmException e) {
            } catch (NameNotFoundException e2) {
            }
        }
        return f348c;
    }

    public static Proxy m491b(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            String host;
            int port;
            if (activeNetworkInfo.getType() == 1) {
                host = android.net.Proxy.getHost(context);
                port = android.net.Proxy.getPort(context);
            } else {
                host = android.net.Proxy.getDefaultHost();
                port = android.net.Proxy.getDefaultPort();
            }
            if (host != null) {
                return new Proxy(Type.HTTP, new InetSocketAddress(host, port));
            }
        }
        return null;
    }

    public static long m484a(double d) {
        return (long) (1000000.0d * d);
    }

    public static double m481a(long j) {
        return ((double) j) / 1000000.0d;
    }

    public static Address m490b() {
        Address address = new Address(Locale.CHINA);
        address.setCountryCode("CN");
        address.setCountryName("\u4e2d\u56fd");
        return address;
    }

    public static String m495c(int i) {
        String str = "&nbsp;";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < i; i2++) {
            stringBuilder.append("&nbsp;");
        }
        return stringBuilder.toString();
    }

    public static String m494c() {
        return "<br />";
    }

    public static Spanned m493c(String str) {
        return str == null ? null : Html.fromHtml(str.replace(SpecilApiUtil.LINE_SEP, "<br />"));
    }

    public static String m486a(String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<font color=").append(str2).append(">").append(str).append("</font>");
        return stringBuffer.toString();
    }

    public static boolean m496c(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return false;
        }
        State state = activeNetworkInfo.getState();
        if (state == null || state == State.DISCONNECTED || state == State.DISCONNECTING) {
            return false;
        }
        return true;
    }
}
