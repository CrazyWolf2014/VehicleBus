package com.paypal.android.p022a;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnmobi.im.util.XmppConnection;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.mine.Contact;
import com.paypal.android.MEP.C0811a.C0801b;
import com.paypal.android.MEP.MEPAddress;
import com.paypal.android.MEP.MEPAmounts;
import com.paypal.android.MEP.MEPReceiverAmounts;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalAdvancedPayment;
import com.paypal.android.MEP.PayPalReceiverDetails;
import com.paypal.android.MEP.p020a.C1099a;
import com.paypal.android.MEP.p020a.C1102d.C08051;
import com.paypal.android.MEP.p020a.C1103e;
import com.paypal.android.MEP.p020a.C1106h;
import com.paypal.android.p022a.p023a.C0828h;
import com.paypal.android.p025c.C0860a;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.codehaus.jackson.smile.SmileConstants;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xbill.DNS.Flags;
import org.xbill.DNS.KEYRecord;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.paypal.android.a.b */
public final class C1107b implements C0801b {
    protected static C0860a f2215a;
    protected static C0860a f2216b;
    private static List<String> f2217k;
    private static C0833a f2218l;
    private static byte[] f2219m;
    private static byte[] f2220n;
    private static byte[] f2221o;
    private static String[] f2222p;
    private static String f2223q;
    private HttpPost f2224c;
    private DefaultHttpClient f2225d;
    private int f2226e;
    private int f2227f;
    private int f2228g;
    private Hashtable<String, Object> f2229h;
    private boolean f2230i;
    private final Thread f2231j;

    /* renamed from: com.paypal.android.a.b.a */
    private static class C0833a extends Thread {
        private boolean f1564a;

        public C0833a() {
            this.f1564a = false;
            start();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void run() {
            /*
            r4 = this;
        L_0x0000:
            r0 = 0;
            r1 = com.paypal.android.p022a.C1107b.f2217k;
            monitor-enter(r1);
            r2 = com.paypal.android.p022a.C1107b.f2217k;	 Catch:{ all -> 0x0069 }
            r2 = r2.size();	 Catch:{ all -> 0x0069 }
            if (r2 != 0) goto L_0x005d;
        L_0x0010:
            r2 = com.paypal.android.p022a.C1107b.f2217k;	 Catch:{ InterruptedException -> 0x008b }
            r2.wait();	 Catch:{ InterruptedException -> 0x008b }
        L_0x0017:
            monitor-exit(r1);	 Catch:{ all -> 0x0069 }
            if (r0 == 0) goto L_0x0000;
        L_0x001a:
            r1 = new org.apache.http.client.methods.HttpGet;
            r1.<init>(r0);
            r2 = com.paypal.android.p022a.C1107b.m2391h();
            r2.execute(r1);	 Catch:{ ClientProtocolException -> 0x003f, IOException -> 0x006c }
            r1 = "NetworkHandler";
            r2 = new java.lang.StringBuilder;	 Catch:{ ClientProtocolException -> 0x003f, IOException -> 0x006c }
            r2.<init>();	 Catch:{ ClientProtocolException -> 0x003f, IOException -> 0x006c }
            r3 = "TrackingPostThread (), posted tracking ";
            r2 = r2.append(r3);	 Catch:{ ClientProtocolException -> 0x003f, IOException -> 0x006c }
            r0 = r2.append(r0);	 Catch:{ ClientProtocolException -> 0x003f, IOException -> 0x006c }
            r0 = r0.toString();	 Catch:{ ClientProtocolException -> 0x003f, IOException -> 0x006c }
            com.paypal.android.MEP.PayPal.logd(r1, r0);	 Catch:{ ClientProtocolException -> 0x003f, IOException -> 0x006c }
            goto L_0x0000;
        L_0x003f:
            r0 = move-exception;
            r1 = "NetworkHandler";
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "TrackingPostThread (), exception ";
            r2 = r2.append(r3);
            r0 = r0.getMessage();
            r0 = r2.append(r0);
            r0 = r0.toString();
            com.paypal.android.MEP.PayPal.loge(r1, r0);
            goto L_0x0000;
        L_0x005d:
            r0 = com.paypal.android.p022a.C1107b.f2217k;	 Catch:{ all -> 0x0069 }
            r2 = 0;
            r0 = r0.remove(r2);	 Catch:{ all -> 0x0069 }
            r0 = (java.lang.String) r0;	 Catch:{ all -> 0x0069 }
            goto L_0x0017;
        L_0x0069:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0069 }
            throw r0;
        L_0x006c:
            r0 = move-exception;
            r1 = "NetworkHandler";
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "TrackingPostThread (), exception ";
            r2 = r2.append(r3);
            r0 = r0.getMessage();
            r0 = r2.append(r0);
            r0 = r0.toString();
            com.paypal.android.MEP.PayPal.loge(r1, r0);
            goto L_0x0000;
        L_0x008b:
            r2 = move-exception;
            goto L_0x0017;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.paypal.android.a.b.a.run():void");
        }
    }

    static {
        f2215a = new C1108i();
        f2216b = new C1109j();
        f2217k = new ArrayList();
        f2218l = null;
        f2219m = new byte[]{(byte) 48, (byte) -126, (byte) 3, (byte) 115, (byte) 48, (byte) -126, (byte) 2, (byte) 91, (byte) 2, (byte) 4, (byte) 77, (byte) 122, (byte) -66, (byte) 90, (byte) 48, (byte) 13, (byte) 6, (byte) 9, (byte) 42, (byte) -122, (byte) 72, (byte) -122, (byte) -9, (byte) 13, (byte) 1, (byte) 1, (byte) 5, (byte) 5, (byte) 0, (byte) 48, (byte) 126, (byte) 49, Flags.CD, (byte) 48, (byte) 9, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 6, (byte) 19, (byte) 2, (byte) 85, (byte) 83, (byte) 49, Flags.CD, (byte) 48, (byte) 9, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 8, (byte) 19, (byte) 2, (byte) 67, (byte) 65, (byte) 49, (byte) 17, (byte) 48, (byte) 15, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 7, (byte) 19, (byte) 8, (byte) 83, (byte) 97, (byte) 110, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 74, (byte) 111, (byte) 115, (byte) 101, (byte) 49, (byte) 21, (byte) 48, (byte) 19, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 10, (byte) 19, (byte) 12, (byte) 80, (byte) 97, (byte) 121, (byte) 80, (byte) 97, (byte) 108, (byte) 44, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 73, (byte) 110, (byte) 99, (byte) 46, (byte) 49, (byte) 20, (byte) 48, (byte) 18, (byte) 6, (byte) 3, (byte) 85, (byte) 4, Flags.CD, (byte) 12, Flags.CD, (byte) 115, (byte) 116, (byte) 97, (byte) 103, (byte) 101, (byte) 95, (byte) 99, (byte) 101, (byte) 114, (byte) 116, (byte) 115, (byte) 49, SmileConstants.TOKEN_LITERAL_FALSE, (byte) 48, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 3, (byte) 12, (byte) 25, (byte) 115, (byte) 116, (byte) 97, (byte) 103, (byte) 101, (byte) 95, (byte) 109, (byte) 112, (byte) 108, (byte) 95, (byte) 101, (byte) 110, (byte) 99, (byte) 114, (byte) 121, (byte) 112, (byte) 116, (byte) 105, (byte) 111, (byte) 110, (byte) 95, (byte) 99, (byte) 101, (byte) 114, (byte) 116, (byte) 48, (byte) 30, (byte) 23, (byte) 13, (byte) 49, (byte) 49, (byte) 48, (byte) 51, (byte) 49, (byte) 50, (byte) 48, (byte) 48, (byte) 50, (byte) 57, (byte) 49, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 90, (byte) 23, (byte) 13, (byte) 51, (byte) 54, (byte) 48, (byte) 51, (byte) 48, (byte) 53, (byte) 48, (byte) 48, (byte) 50, (byte) 57, (byte) 49, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 90, (byte) 48, (byte) 126, (byte) 49, Flags.CD, (byte) 48, (byte) 9, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 6, (byte) 19, (byte) 2, (byte) 85, (byte) 83, (byte) 49, Flags.CD, (byte) 48, (byte) 9, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 8, (byte) 19, (byte) 2, (byte) 67, (byte) 65, (byte) 49, (byte) 17, (byte) 48, (byte) 15, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 7, (byte) 19, (byte) 8, (byte) 83, (byte) 97, (byte) 110, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 74, (byte) 111, (byte) 115, (byte) 101, (byte) 49, (byte) 21, (byte) 48, (byte) 19, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 10, (byte) 19, (byte) 12, (byte) 80, (byte) 97, (byte) 121, (byte) 80, (byte) 97, (byte) 108, (byte) 44, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 73, (byte) 110, (byte) 99, (byte) 46, (byte) 49, (byte) 20, (byte) 48, (byte) 18, (byte) 6, (byte) 3, (byte) 85, (byte) 4, Flags.CD, (byte) 12, Flags.CD, (byte) 115, (byte) 116, (byte) 97, (byte) 103, (byte) 101, (byte) 95, (byte) 99, (byte) 101, (byte) 114, (byte) 116, (byte) 115, (byte) 49, SmileConstants.TOKEN_LITERAL_FALSE, (byte) 48, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 3, (byte) 12, (byte) 25, (byte) 115, (byte) 116, (byte) 97, (byte) 103, (byte) 101, (byte) 95, (byte) 109, (byte) 112, (byte) 108, (byte) 95, (byte) 101, (byte) 110, (byte) 99, (byte) 114, (byte) 121, (byte) 112, (byte) 116, (byte) 105, (byte) 111, (byte) 110, (byte) 95, (byte) 99, (byte) 101, (byte) 114, (byte) 116, (byte) 48, (byte) -126, (byte) 1, SmileConstants.TOKEN_LITERAL_FALSE, (byte) 48, (byte) 13, (byte) 6, (byte) 9, (byte) 42, (byte) -122, (byte) 72, (byte) -122, (byte) -9, (byte) 13, (byte) 1, (byte) 1, (byte) 1, (byte) 5, (byte) 0, (byte) 3, (byte) -126, (byte) 1, (byte) 15, (byte) 0, (byte) 48, (byte) -126, (byte) 1, (byte) 10, (byte) 2, (byte) -126, (byte) 1, (byte) 1, (byte) 0, (byte) -103, (byte) -59, (byte) -112, (byte) -109, SmileConstants.TOKEN_LITERAL_FALSE, (byte) 95, (byte) -44, (byte) 88, (byte) -8, (byte) 47, (byte) -24, (byte) 97, (byte) -96, (byte) 24, (byte) -27, (byte) -16, (byte) 96, (byte) 78, (byte) -96, (byte) 101, (byte) 96, (byte) -55, (byte) 117, (byte) -103, (byte) 29, (byte) 116, (byte) 85, (byte) 25, (byte) -52, (byte) 5, (byte) 71, (byte) -21, (byte) 1, (byte) -114, (byte) -57, (byte) -50, (byte) 36, (byte) -31, (byte) 103, (byte) 39, (byte) -58, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 21, (byte) -82, (byte) -3, (byte) 118, (byte) 42, (byte) 1, (byte) -93, (byte) -111, (byte) 114, (byte) -11, (byte) 73, (byte) 63, (byte) 99, (byte) 97, (byte) -26, (byte) 103, (byte) 97, (byte) 49, (byte) 39, SmileConstants.TOKEN_LITERAL_NULL, (byte) -12, (byte) 31, (byte) -40, (byte) 14, (byte) -100, (byte) -99, (byte) -80, (byte) -53, (byte) -105, (byte) -48, (byte) 28, (byte) 72, (byte) 53, (byte) 0, (byte) 89, (byte) -44, (byte) -97, (byte) -45, (byte) 39, (byte) -96, (byte) -9, (byte) 1, (byte) 73, (byte) -23, SmileConstants.HEADER_BYTE_1, (byte) 3, (byte) 126, (byte) -61, (byte) -86, (byte) 5, (byte) -64, (byte) -10, (byte) -107, (byte) -64, (byte) 50, (byte) 79, (byte) 64, (byte) 42, (byte) 114, SmileConstants.TOKEN_LITERAL_NULL, (byte) 121, (byte) 59, (byte) 121, (byte) -122, (byte) -105, (byte) 6, (byte) -70, (byte) 19, (byte) 83, (byte) -91, (byte) 48, (byte) -9, (byte) 78, (byte) 112, (byte) 91, (byte) 71, (byte) -30, (byte) -61, (byte) 3, (byte) 113, (byte) 84, (byte) -47, (byte) -9, (byte) 103, (byte) -4, (byte) 40, (byte) 20, (byte) -29, (byte) -95, (byte) -124, (byte) -125, Byte.MIN_VALUE, (byte) 42, (byte) -24, (byte) -37, (byte) 40, (byte) 74, (byte) -121, (byte) -45, (byte) -87, (byte) -10, (byte) -42, (byte) -63, (byte) 45, (byte) 111, (byte) -30, (byte) -3, (byte) 68, (byte) 8, (byte) -60, (byte) -22, (byte) -8, (byte) -25, (byte) -99, (byte) 12, (byte) -89, (byte) -117, (byte) 1, (byte) -95, (byte) -110, (byte) -1, (byte) -58, (byte) 12, (byte) -7, (byte) 59, (byte) -22, (byte) -61, (byte) 9, (byte) 122, (byte) -27, (byte) -52, (byte) -114, (byte) -92, (byte) 17, (byte) 59, (byte) -16, (byte) 95, (byte) -111, (byte) -124, (byte) -110, (byte) -31, (byte) -5, (byte) 116, (byte) -112, (byte) -121, (byte) -123, (byte) -11, (byte) -69, (byte) -24, (byte) 46, (byte) 117, (byte) -8, (byte) 120, (byte) -40, (byte) 101, (byte) -124, (byte) 70, (byte) 113, (byte) -79, (byte) -25, (byte) 120, (byte) -14, (byte) -49, (byte) -32, (byte) -53, (byte) -17, (byte) -4, (byte) -85, (byte) -102, (byte) 85, (byte) -85, (byte) 44, (byte) 95, (byte) 69, (byte) 92, (byte) -49, (byte) 3, (byte) -13, (byte) 14, (byte) 86, (byte) -74, (byte) 72, (byte) 93, (byte) -45, (byte) -114, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 43, (byte) -4, (byte) -95, (byte) -26, (byte) 118, (byte) -78, (byte) 122, (byte) -73, (byte) 25, (byte) 118, (byte) -82, (byte) -108, (byte) -66, (byte) 107, (byte) 98, (byte) -102, (byte) 91, (byte) 46, (byte) 123, (byte) 81, (byte) -113, (byte) 30, (byte) -54, (byte) 120, (byte) -44, (byte) -26, (byte) -84, (byte) -97, (byte) 2, (byte) 3, (byte) 1, (byte) 0, (byte) 1, (byte) 48, (byte) 13, (byte) 6, (byte) 9, (byte) 42, (byte) -122, (byte) 72, (byte) -122, (byte) -9, (byte) 13, (byte) 1, (byte) 1, (byte) 5, (byte) 5, (byte) 0, (byte) 3, (byte) -126, (byte) 1, (byte) 1, (byte) 0, (byte) 123, (byte) -27, (byte) -3, (byte) -116, (byte) -46, (byte) -77, (byte) 0, (byte) -88, (byte) -121, (byte) -22, (byte) -28, (byte) -105, (byte) -75, (byte) 59, (byte) -109, Byte.MIN_VALUE, (byte) 87, (byte) 99, (byte) 8, (byte) 92, (byte) -122, (byte) 44, (byte) -88, (byte) -116, (byte) 0, (byte) -92, (byte) 69, (byte) 89, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 2, (byte) -41, (byte) 9, (byte) -90, (byte) -20, (byte) 73, (byte) 5, (byte) -40, (byte) 123, (byte) 73, SmileConstants.TOKEN_LITERAL_NULL, (byte) 40, (byte) -45, (byte) -124, (byte) -45, (byte) -88, (byte) 50, (byte) 66, (byte) -102, (byte) -103, (byte) -38, (byte) 18, (byte) 5, (byte) -17, (byte) 28, (byte) 47, (byte) 82, (byte) -28, (byte) 87, (byte) 7, (byte) -50, (byte) 108, (byte) 88, (byte) 120, (byte) 119, (byte) 105, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, Byte.MIN_VALUE, (byte) -48, (byte) -30, (byte) -50, (byte) -2, (byte) -58, (byte) -69, (byte) -126, (byte) -80, (byte) 104, (byte) -101, (byte) -11, (byte) 27, (byte) 13, (byte) -36, (byte) 112, (byte) -36, (byte) 77, (byte) 16, (byte) 78, (byte) -76, (byte) -91, (byte) -119, (byte) -56, (byte) -32, (byte) 98, (byte) 68, (byte) -44, (byte) 86, (byte) 91, (byte) 57, (byte) 85, (byte) 29, (byte) 60, SmileConstants.TOKEN_LITERAL_FALSE, (byte) 7, (byte) -96, (byte) 42, (byte) 60, (byte) -101, (byte) -40, (byte) -62, (byte) -41, (byte) -109, (byte) 17, (byte) -50, (byte) -86, SmileConstants.HEADER_BYTE_1, (byte) 82, (byte) 64, (byte) -72, (byte) 73, (byte) -95, (byte) -91, (byte) -13, (byte) -57, (byte) -33, (byte) -76, (byte) 9, (byte) -15, (byte) 85, (byte) 125, (byte) -17, (byte) 83, (byte) -61, (byte) -52, (byte) 92, (byte) -30, (byte) -76, (byte) -118, (byte) 0, (byte) -101, (byte) 12, (byte) -109, (byte) 20, (byte) 74, (byte) -114, (byte) -21, (byte) -32, (byte) -15, (byte) 68, (byte) -58, (byte) -126, (byte) -102, (byte) 86, (byte) -87, (byte) 68, (byte) -34, (byte) -101, (byte) -108, (byte) 43, (byte) 70, (byte) -75, (byte) -88, (byte) 84, (byte) 101, (byte) -30, (byte) -57, (byte) -59, (byte) 90, (byte) -102, (byte) 49, (byte) 121, SmileConstants.TOKEN_LITERAL_FALSE, (byte) -24, (byte) 30, (byte) -65, (byte) -59, (byte) -12, (byte) 21, (byte) 8, (byte) -115, (byte) 102, (byte) -104, (byte) -81, (byte) 7, (byte) 126, (byte) -8, (byte) -8, (byte) -124, (byte) 12, (byte) 123, (byte) 42, (byte) 54, (byte) 9, (byte) 63, (byte) 22, Flags.CD, (byte) -15, (byte) 124, (byte) 51, (byte) -76, (byte) 68, (byte) -87, (byte) -113, (byte) -127, (byte) -93, (byte) -32, SmileConstants.HEADER_BYTE_1, (byte) 30, (byte) 2, (byte) -90, (byte) -15, (byte) -83, (byte) -81, (byte) 117, (byte) -61, (byte) 24, (byte) -25, (byte) -120, (byte) 1, (byte) -109, (byte) 69, (byte) 92, (byte) 92, (byte) -8, (byte) 103, (byte) 7, (byte) -63, (byte) -107, (byte) 54, (byte) 55, (byte) 89, (byte) 50, (byte) -13, (byte) 44, SmileConstants.TOKEN_LITERAL_FALSE, (byte) 87, (byte) -88, (byte) -98, (byte) 60, (byte) 103, (byte) 4, (byte) 93, (byte) -89, (byte) 0, (byte) 37, (byte) 118, (byte) -113, (byte) 59, (byte) -114, (byte) -9, (byte) 106, (byte) -92, (byte) -110, (byte) 81, (byte) 105, (byte) 109, (byte) -87, (byte) 6};
        f2220n = new byte[]{(byte) 48, (byte) -126, (byte) 2, (byte) 106, (byte) 48, (byte) -126, (byte) 1, (byte) -45, (byte) 2, (byte) 4, (byte) 77, (byte) -90, (byte) -88, (byte) -61, (byte) 48, (byte) 13, (byte) 6, (byte) 9, (byte) 42, (byte) -122, (byte) 72, (byte) -122, (byte) -9, (byte) 13, (byte) 1, (byte) 1, (byte) 5, (byte) 5, (byte) 0, (byte) 48, (byte) 124, (byte) 49, Flags.CD, (byte) 48, (byte) 9, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 6, (byte) 19, (byte) 2, (byte) 85, (byte) 83, (byte) 49, Flags.CD, (byte) 48, (byte) 9, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 8, (byte) 19, (byte) 2, (byte) 67, (byte) 65, (byte) 49, (byte) 17, (byte) 48, (byte) 15, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 7, (byte) 19, (byte) 8, (byte) 83, (byte) 97, (byte) 110, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 74, (byte) 111, (byte) 115, (byte) 101, (byte) 49, (byte) 21, (byte) 48, (byte) 19, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 10, (byte) 19, (byte) 12, (byte) 80, (byte) 97, (byte) 121, (byte) 80, (byte) 97, (byte) 108, (byte) 44, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 73, (byte) 110, (byte) 99, (byte) 46, (byte) 49, (byte) 19, (byte) 48, (byte) 17, (byte) 6, (byte) 3, (byte) 85, (byte) 4, Flags.CD, (byte) 12, (byte) 10, (byte) 108, (byte) 105, (byte) 118, (byte) 101, (byte) 95, (byte) 99, (byte) 101, (byte) 114, (byte) 116, (byte) 115, (byte) 49, SmileConstants.TOKEN_LITERAL_NULL, (byte) 48, (byte) 31, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 3, (byte) 12, (byte) 24, (byte) 108, (byte) 105, (byte) 118, (byte) 101, (byte) 95, (byte) 109, (byte) 112, (byte) 108, (byte) 95, (byte) 101, (byte) 110, (byte) 99, (byte) 114, (byte) 121, (byte) 112, (byte) 116, (byte) 105, (byte) 111, (byte) 110, (byte) 95, (byte) 99, (byte) 101, (byte) 114, (byte) 116, (byte) 48, (byte) 30, (byte) 23, (byte) 13, (byte) 49, (byte) 49, (byte) 48, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 49, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 48, (byte) 55, (byte) 53, (byte) 54, (byte) 53, (byte) 49, (byte) 90, (byte) 23, (byte) 13, (byte) 49, (byte) 51, (byte) 48, (byte) 54, (byte) 50, (byte) 50, (byte) 48, (byte) 55, (byte) 53, (byte) 54, (byte) 53, (byte) 49, (byte) 90, (byte) 48, (byte) 124, (byte) 49, Flags.CD, (byte) 48, (byte) 9, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 6, (byte) 19, (byte) 2, (byte) 85, (byte) 83, (byte) 49, Flags.CD, (byte) 48, (byte) 9, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 8, (byte) 19, (byte) 2, (byte) 67, (byte) 65, (byte) 49, (byte) 17, (byte) 48, (byte) 15, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 7, (byte) 19, (byte) 8, (byte) 83, (byte) 97, (byte) 110, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 74, (byte) 111, (byte) 115, (byte) 101, (byte) 49, (byte) 21, (byte) 48, (byte) 19, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 10, (byte) 19, (byte) 12, (byte) 80, (byte) 97, (byte) 121, (byte) 80, (byte) 97, (byte) 108, (byte) 44, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 73, (byte) 110, (byte) 99, (byte) 46, (byte) 49, (byte) 19, (byte) 48, (byte) 17, (byte) 6, (byte) 3, (byte) 85, (byte) 4, Flags.CD, (byte) 12, (byte) 10, (byte) 108, (byte) 105, (byte) 118, (byte) 101, (byte) 95, (byte) 99, (byte) 101, (byte) 114, (byte) 116, (byte) 115, (byte) 49, SmileConstants.TOKEN_LITERAL_NULL, (byte) 48, (byte) 31, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 3, (byte) 12, (byte) 24, (byte) 108, (byte) 105, (byte) 118, (byte) 101, (byte) 95, (byte) 109, (byte) 112, (byte) 108, (byte) 95, (byte) 101, (byte) 110, (byte) 99, (byte) 114, (byte) 121, (byte) 112, (byte) 116, (byte) 105, (byte) 111, (byte) 110, (byte) 95, (byte) 99, (byte) 101, (byte) 114, (byte) 116, (byte) 48, (byte) -127, (byte) -97, (byte) 48, (byte) 13, (byte) 6, (byte) 9, (byte) 42, (byte) -122, (byte) 72, (byte) -122, (byte) -9, (byte) 13, (byte) 1, (byte) 1, (byte) 1, (byte) 5, (byte) 0, (byte) 3, (byte) -127, (byte) -115, (byte) 0, (byte) 48, (byte) -127, (byte) -119, (byte) 2, (byte) -127, (byte) -127, (byte) 0, (byte) -82, Byte.MIN_VALUE, (byte) -69, (byte) 76, (byte) -124, (byte) -63, (byte) 108, (byte) -113, (byte) -33, (byte) 17, (byte) 7, (byte) -86, (byte) -95, (byte) -7, (byte) 114, (byte) -125, (byte) -63, (byte) -125, (byte) 14, (byte) -67, (byte) 18, (byte) 68, (byte) -95, (byte) -61, (byte) 17, (byte) -55, (byte) -17, (byte) -28, (byte) -18, (byte) -28, (byte) -117, (byte) -37, (byte) -9, (byte) 59, (byte) -39, (byte) 46, (byte) 54, (byte) 1, (byte) -64, (byte) 14, (byte) -78, (byte) 6, (byte) -29, (byte) -41, (byte) 90, (byte) -20, (byte) 108, SmileConstants.HEADER_BYTE_1, (byte) -60, (byte) 22, (byte) -68, (byte) -125, (byte) 74, (byte) -14, (byte) 18, (byte) -123, (byte) -36, (byte) -41, (byte) 77, (byte) -106, (byte) -25, (byte) -74, (byte) -5, (byte) 112, (byte) 126, (byte) 108, (byte) 86, (byte) 44, (byte) 0, (byte) -61, (byte) 121, (byte) -21, (byte) -43, (byte) -3, (byte) 74, (byte) -125, (byte) -122, (byte) 114, (byte) 42, (byte) 109, (byte) -48, (byte) 61, (byte) 77, (byte) -52, (byte) 121, (byte) -59, (byte) 51, (byte) -108, (byte) -52, (byte) -44, (byte) 64, SmileConstants.HEADER_BYTE_2, (byte) 84, (byte) -92, (byte) -43, (byte) 97, (byte) -77, (byte) 81, (byte) -88, (byte) -111, (byte) 124, (byte) 16, (byte) -110, (byte) -85, (byte) -11, (byte) 2, (byte) -76, (byte) 103, (byte) 50, (byte) -56, (byte) -93, (byte) 1, (byte) -115, (byte) 82, (byte) 43, (byte) 74, (byte) 79, (byte) 110, (byte) -16, (byte) -68, (byte) 12, (byte) 23, (byte) -56, (byte) 31, (byte) -108, (byte) -113, (byte) 82, (byte) 47, (byte) 2, (byte) 3, (byte) 1, (byte) 0, (byte) 1, (byte) 48, (byte) 13, (byte) 6, (byte) 9, (byte) 42, (byte) -122, (byte) 72, (byte) -122, (byte) -9, (byte) 13, (byte) 1, (byte) 1, (byte) 5, (byte) 5, (byte) 0, (byte) 3, (byte) -127, (byte) -127, (byte) 0, (byte) 118, (byte) -105, (byte) -7, (byte) 69, (byte) -119, (byte) -75, (byte) 31, (byte) 110, (byte) 91, (byte) 10, (byte) 124, (byte) -36, (byte) -41, (byte) -111, (byte) 100, (byte) -55, (byte) -103, (byte) -71, (byte) 118, (byte) -18, (byte) -31, (byte) 55, (byte) -84, (byte) -22, (byte) 10, (byte) -15, (byte) 114, (byte) 39, (byte) -118, (byte) -4, SmileConstants.TOKEN_LITERAL_NULL, (byte) -72, (byte) -112, (byte) 5, (byte) 106, (byte) 100, (byte) -127, SmileConstants.HEADER_BYTE_1, (byte) 71, (byte) 26, (byte) -2, (byte) 86, (byte) -111, (byte) 31, (byte) -15, (byte) 89, (byte) 23, (byte) 91, (byte) 66, (byte) 65, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 89, (byte) 115, (byte) -103, (byte) -4, (byte) -94, (byte) 29, (byte) 90, (byte) -15, (byte) 8, SmileConstants.HEADER_BYTE_2, (byte) 61, (byte) 29, (byte) 42, (byte) -111, (byte) 25, (byte) -94, (byte) 29, (byte) 23, (byte) 70, (byte) -48, (byte) 59, (byte) -37, (byte) -32, (byte) 102, (byte) 111, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_NULL, (byte) -34, (byte) -106, (byte) -60, (byte) 121, (byte) -127, (byte) -115, (byte) -79, (byte) -29, (byte) -9, (byte) 82, (byte) -97, (byte) -11, (byte) -51, (byte) 15, (byte) -125, (byte) -20, (byte) 30, (byte) -11, (byte) 75, (byte) -20, (byte) -19, (byte) 97, (byte) 105, (byte) 56, (byte) -9, (byte) -118, SmileConstants.TOKEN_LITERAL_FALSE, (byte) 4, (byte) 67, (byte) -49, (byte) -42, (byte) 83, (byte) 96, (byte) -14, (byte) -59, (byte) 39, (byte) -9, (byte) -100, (byte) -68, (byte) -13, (byte) -76, SmileConstants.HEADER_BYTE_2, Flags.CD, (byte) -59, (byte) -34, (byte) -79, (byte) 8, (byte) -40, (byte) -94, (byte) -75};
        f2221o = new byte[]{(byte) 48, (byte) -126, (byte) 2, (byte) 120, (byte) 48, (byte) -126, (byte) 1, (byte) -31, (byte) 2, (byte) 4, (byte) 77, (byte) -90, (byte) -94, (byte) 69, (byte) 48, (byte) 13, (byte) 6, (byte) 9, (byte) 42, (byte) -122, (byte) 72, (byte) -122, (byte) -9, (byte) 13, (byte) 1, (byte) 1, (byte) 5, (byte) 5, (byte) 0, (byte) 48, (byte) -127, (byte) -126, (byte) 49, Flags.CD, (byte) 48, (byte) 9, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 6, (byte) 19, (byte) 2, (byte) 85, (byte) 83, (byte) 49, Flags.CD, (byte) 48, (byte) 9, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 8, (byte) 19, (byte) 2, (byte) 67, (byte) 65, (byte) 49, (byte) 17, (byte) 48, (byte) 15, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 7, (byte) 19, (byte) 8, (byte) 83, (byte) 97, (byte) 110, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 74, (byte) 111, (byte) 115, (byte) 101, (byte) 49, (byte) 21, (byte) 48, (byte) 19, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 10, (byte) 19, (byte) 12, (byte) 80, (byte) 97, (byte) 121, (byte) 80, (byte) 97, (byte) 108, (byte) 44, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 73, (byte) 110, (byte) 99, (byte) 46, (byte) 49, (byte) 22, (byte) 48, (byte) 20, (byte) 6, (byte) 3, (byte) 85, (byte) 4, Flags.CD, (byte) 12, (byte) 13, (byte) 115, (byte) 97, (byte) 110, (byte) 100, (byte) 98, (byte) 111, (byte) 120, (byte) 95, (byte) 99, (byte) 101, (byte) 114, (byte) 116, (byte) 115, (byte) 49, (byte) 36, (byte) 48, SmileConstants.TOKEN_LITERAL_FALSE, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 3, (byte) 12, (byte) 27, (byte) 115, (byte) 97, (byte) 110, (byte) 100, (byte) 98, (byte) 111, (byte) 120, (byte) 95, (byte) 109, (byte) 112, (byte) 108, (byte) 95, (byte) 101, (byte) 110, (byte) 99, (byte) 114, (byte) 121, (byte) 112, (byte) 116, (byte) 105, (byte) 111, (byte) 110, (byte) 95, (byte) 99, (byte) 101, (byte) 114, (byte) 116, (byte) 48, (byte) 30, (byte) 23, (byte) 13, (byte) 49, (byte) 49, (byte) 48, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 49, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 48, (byte) 55, (byte) 50, (byte) 57, (byte) 48, (byte) 57, (byte) 90, (byte) 23, (byte) 13, (byte) 51, (byte) 54, (byte) 48, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 48, (byte) 55, (byte) 48, (byte) 55, (byte) 50, (byte) 57, (byte) 48, (byte) 57, (byte) 90, (byte) 48, (byte) -127, (byte) -126, (byte) 49, Flags.CD, (byte) 48, (byte) 9, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 6, (byte) 19, (byte) 2, (byte) 85, (byte) 83, (byte) 49, Flags.CD, (byte) 48, (byte) 9, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 8, (byte) 19, (byte) 2, (byte) 67, (byte) 65, (byte) 49, (byte) 17, (byte) 48, (byte) 15, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 7, (byte) 19, (byte) 8, (byte) 83, (byte) 97, (byte) 110, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 74, (byte) 111, (byte) 115, (byte) 101, (byte) 49, (byte) 21, (byte) 48, (byte) 19, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 10, (byte) 19, (byte) 12, (byte) 80, (byte) 97, (byte) 121, (byte) 80, (byte) 97, (byte) 108, (byte) 44, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, (byte) 73, (byte) 110, (byte) 99, (byte) 46, (byte) 49, (byte) 22, (byte) 48, (byte) 20, (byte) 6, (byte) 3, (byte) 85, (byte) 4, Flags.CD, (byte) 12, (byte) 13, (byte) 115, (byte) 97, (byte) 110, (byte) 100, (byte) 98, (byte) 111, (byte) 120, (byte) 95, (byte) 99, (byte) 101, (byte) 114, (byte) 116, (byte) 115, (byte) 49, (byte) 36, (byte) 48, SmileConstants.TOKEN_LITERAL_FALSE, (byte) 6, (byte) 3, (byte) 85, (byte) 4, (byte) 3, (byte) 12, (byte) 27, (byte) 115, (byte) 97, (byte) 110, (byte) 100, (byte) 98, (byte) 111, (byte) 120, (byte) 95, (byte) 109, (byte) 112, (byte) 108, (byte) 95, (byte) 101, (byte) 110, (byte) 99, (byte) 114, (byte) 121, (byte) 112, (byte) 116, (byte) 105, (byte) 111, (byte) 110, (byte) 95, (byte) 99, (byte) 101, (byte) 114, (byte) 116, (byte) 48, (byte) -127, (byte) -97, (byte) 48, (byte) 13, (byte) 6, (byte) 9, (byte) 42, (byte) -122, (byte) 72, (byte) -122, (byte) -9, (byte) 13, (byte) 1, (byte) 1, (byte) 1, (byte) 5, (byte) 0, (byte) 3, (byte) -127, (byte) -115, (byte) 0, (byte) 48, (byte) -127, (byte) -119, (byte) 2, (byte) -127, (byte) -127, (byte) 0, (byte) -122, (byte) 60, (byte) -17, (byte) -98, (byte) 126, (byte) 97, (byte) -124, (byte) -43, (byte) 10, (byte) -104, (byte) 124, (byte) 117, (byte) -41, (byte) -15, (byte) -106, (byte) 43, (byte) -52, (byte) -80, (byte) -89, (byte) -13, (byte) -9, (byte) 71, (byte) -122, (byte) 28, (byte) 97, (byte) 30, Byte.MIN_VALUE, (byte) 25, SmileConstants.TOKEN_LITERAL_NULL, (byte) 59, (byte) -66, Flags.CD, (byte) 75, (byte) -79, (byte) 91, (byte) -67, (byte) 124, (byte) -16, (byte) -54, (byte) 22, (byte) 71, (byte) 83, (byte) -89, (byte) 5, (byte) -104, (byte) 96, (byte) -82, (byte) 124, (byte) -55, (byte) -64, (byte) 118, (byte) 120, (byte) -122, (byte) 116, (byte) -99, (byte) 106, (byte) -29, (byte) -85, (byte) -54, (byte) 16, SmileConstants.TOKEN_LITERAL_FALSE, (byte) -22, (byte) -82, (byte) -30, (byte) -11, (byte) 62, (byte) 39, (byte) 123, (byte) 18, (byte) -40, (byte) 6, (byte) -106, (byte) 29, (byte) -25, (byte) 47, (byte) 55, (byte) 28, (byte) 28, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 13, (byte) 63, (byte) -2, (byte) 76, (byte) 94, (byte) 74, (byte) 55, (byte) -97, (byte) -19, (byte) 27, (byte) -83, (byte) 93, (byte) 57, (byte) 64, (byte) -63, (byte) -57, (byte) -56, (byte) -57, (byte) -88, (byte) 19, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) -124, (byte) -52, (byte) 37, (byte) -28, (byte) -82, (byte) -58, (byte) 100, (byte) 121, (byte) 79, (byte) 22, (byte) 15, (byte) 78, (byte) -33, (byte) -1, (byte) -30, (byte) 50, (byte) -5, (byte) -32, (byte) 122, (byte) -6, (byte) -116, (byte) -116, (byte) 84, (byte) -105, (byte) 42, (byte) 86, (byte) -30, (byte) 99, (byte) 2, (byte) 3, (byte) 1, (byte) 0, (byte) 1, (byte) 48, (byte) 13, (byte) 6, (byte) 9, (byte) 42, (byte) -122, (byte) 72, (byte) -122, (byte) -9, (byte) 13, (byte) 1, (byte) 1, (byte) 5, (byte) 5, (byte) 0, (byte) 3, (byte) -127, (byte) -127, (byte) 0, (byte) 46, (byte) 29, Flags.CD, (byte) -21, (byte) -95, (byte) -37, (byte) -60, (byte) 16, (byte) 5, (byte) 46, (byte) 112, (byte) -28, (byte) -72, (byte) 2, (byte) 44, (byte) -41, (byte) 38, (byte) 93, (byte) -73, (byte) 15, (byte) 112, (byte) -42, (byte) -15, (byte) -15, (byte) -80, (byte) -120, (byte) 55, (byte) -35, (byte) -36, (byte) 7, (byte) 111, (byte) -74, (byte) -52, (byte) -81, (byte) 60, (byte) -125, (byte) 69, (byte) 117, (byte) -103, (byte) 79, (byte) 113, (byte) 25, (byte) 36, (byte) 44, (byte) 29, (byte) 97, (byte) 97, (byte) -66, (byte) -8, (byte) 94, (byte) -85, (byte) 28, (byte) 96, (byte) -106, (byte) 24, (byte) 64, (byte) -1, (byte) -55, (byte) -112, (byte) -52, (byte) -57, SmileConstants.TOKEN_LITERAL_TRUE, (byte) -36, (byte) 122, (byte) -21, (byte) 122, (byte) 37, (byte) 54, (byte) -68, (byte) -48, (byte) 36, Flags.CD, Flags.CD, (byte) 91, SmileConstants.HEADER_BYTE_1, (byte) -100, (byte) 126, (byte) 28, (byte) -109, (byte) 19, (byte) 95, (byte) 49, (byte) -46, (byte) 25, (byte) 77, (byte) -110, (byte) 80, (byte) 122, (byte) -22, (byte) -41, (byte) -98, (byte) 60, (byte) -118, (byte) 30, (byte) -111, (byte) 62, (byte) -89, (byte) 9, (byte) -20, (byte) -104, (byte) -100, (byte) -120, (byte) 69, (byte) -110, (byte) -4, (byte) 18, (byte) 110, (byte) -50, (byte) 83, (byte) -17, (byte) 115, (byte) -76, (byte) 111, (byte) 95, (byte) -106, (byte) -5, (byte) 8, (byte) -93, (byte) -31, (byte) 13, (byte) 54, (byte) -122, (byte) -97, (byte) -41, (byte) 9, (byte) -63, (byte) 96, SmileConstants.TOKEN_LITERAL_EMPTY_STRING};
        f2222p = new String[]{Contact.RELATION_FRIEND, "27", "30", "31", "32", "33", "34", "36", "39", "41", "43", "44", "45", "46", "47", "48", "49", "52", "54", "55", "56", "58", "60", "61", "64", "65", "66", "81", "82", "86", "90", "91", "351", "352", "353", "354", "356", "357", "358", "370", "371", "372", "377", "386", "420", "421", "506", "593", "598", "852", "886", "972"};
        f2223q = XmlPullParser.NO_NAMESPACE;
    }

    public C1107b() {
        this.f2225d = null;
        this.f2226e = -1;
        this.f2227f = -1;
        this.f2228g = -1;
        this.f2230i = false;
        this.f2231j = new C0840k(this);
    }

    private static String m2360A() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            PayPal.loge("Exception", e.toString());
        }
        return null;
    }

    private boolean m2361B() {
        String a = C0842m.m1580a();
        String str = C1107b.m2376b() + C1107b.m2407q() + "/DeviceInterrogation";
        PayPal.logd("MPL", "start makeDeviceInterrogationRequest Post");
        String a2 = m2365a(a, str, true);
        PayPal.logd("MPL", "end makeDeviceInterrogationRequest Post");
        PayPal instance = PayPal.getInstance();
        try {
            Document parse = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(a2.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET)));
            if (m2373a(parse, a2)) {
                C1107b.m2368a("makeDeviceInterrogationRequest", str, a, a2);
                try {
                    C0842m.m1591a(parse, this.f2229h);
                } catch (Throwable th) {
                    PayPal.loge("NetworkHandler", "makeDeviceInterrogationRequest caught exception " + th.getMessage());
                }
                instance.resetAccount();
                return false;
            } else if (C0842m.m1591a(parse, this.f2229h)) {
                return true;
            } else {
                instance.resetAccount();
                this.f2227f = -1;
                PayPal.loge("NetworkHandler", "makeDeviceInterrogationRequest something failed");
                return false;
            }
        } catch (ParserConfigurationException e) {
            C1107b.m2368a("makeDeviceInterrogationRequest", str, a, "exception " + e.getMessage());
            instance.resetAccount();
            return false;
        } catch (UnsupportedEncodingException e2) {
            C1107b.m2368a("makeDeviceInterrogationRequest", str, a, "exception " + e2.getMessage());
            instance.resetAccount();
            return false;
        } catch (SAXException e3) {
            C1107b.m2368a("makeDeviceInterrogationRequest", str, a, "exception " + e3.getMessage());
            instance.resetAccount();
            return false;
        } catch (NullPointerException e4) {
            C1107b.m2368a("makeDeviceInterrogationRequest", str, a, "exception " + e4.getMessage());
            instance.resetAccount();
            return false;
        } catch (IOException e5) {
            C1107b.m2368a("makeDeviceInterrogationRequest", str, a, "exception " + e5.getMessage());
            instance.resetAccount();
            return false;
        }
    }

    private boolean m2362C() {
        try {
            String a = C0842m.m1582a(this.f2229h, C1107b.m2395j((String) this.f2229h.get("NewPin")));
            String str = C1107b.m2376b() + C1107b.m2407q() + "/DeviceCreatePin";
            String a2 = m2365a(a, str, true);
            Document parse = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(a2.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET)));
            if (m2373a(parse, a2)) {
                C1107b.m2368a("createPIN", str, a, a2);
                this.f2227f = -1;
                return false;
            }
            C0842m.m1598b(parse);
            return true;
        } catch (C0837f e) {
            PayPal.loge("NetworkHandler", "createPIN caught BadXMLException " + e.getMessage());
        } catch (C0843n e2) {
            PayPal.loge("NetworkHandler", "createPIN caught BadPhoneNumberException " + e2.getMessage());
        } catch (ParserConfigurationException e3) {
            PayPal.loge("NetworkHandler", "Exception " + e3.getMessage());
        } catch (UnsupportedEncodingException e4) {
            PayPal.loge("NetworkHandler", "Exception " + e4.getMessage());
        } catch (SAXException e5) {
            PayPal.loge("NetworkHandler", "Exception " + e5.getMessage());
        } catch (IOException e6) {
            PayPal.loge("NetworkHandler", "Exception " + e6.getMessage());
        } catch (CertificateExpiredException e7) {
            PayPal.loge("NetworkHandler", "Exception " + e7.getMessage());
        } catch (CertificateNotYetValidException e8) {
            PayPal.loge("NetworkHandler", "Exception " + e8.getMessage());
        }
    }

    private boolean m2363D() {
        String a = m2365a(C0842m.m1600c(), C1107b.m2376b() + C1107b.m2407q() + "/RemoveDeviceAuthorization", true);
        if (m2398k(a)) {
            try {
                C0842m.m1614f(a, this.f2229h);
                return false;
            } catch (Throwable th) {
                PayPal.loge("NetworkHandler", "readRemoveDeviceAuthorization caught exception " + th.getMessage());
                return false;
            }
        } else if (C0842m.m1614f(a, this.f2229h)) {
            return true;
        } else {
            this.f2227f = -1;
            return false;
        }
    }

    private String m2365a(String str, String str2, boolean z) {
        Throwable th;
        DataInputStream dataInputStream;
        InputStream content;
        try {
            String str3;
            int i;
            if (this.f2225d == null) {
                this.f2225d = C1107b.m2391h();
            }
            this.f2224c = new HttpPost(str2);
            HttpEntity stringEntity = new StringEntity(str, Charset.forName(AsyncHttpResponseHandler.DEFAULT_CHARSET).name());
            if (z) {
                this.f2224c.setHeader("CLIENT-AUTH", "No cert");
                this.f2224c.setHeader("X-PAYPAL-MESSAGE-PROTOCOL", "SOAP11");
                this.f2224c.setHeader("X-PAYPAL-APPLICATION-ID", PayPalActivity._paypal.getAppID());
                this.f2224c.setHeader("X-PAYPAL-REQUEST-SOURCE", PayPal.getVersionWithoutBuild());
                this.f2224c.setHeader("X-PAYPAL-REQUEST-DATA-FORMAT", "XML");
                this.f2224c.setHeader("X-PAYPAL-RESPONSE-DATA-FORMAT", "XML");
                this.f2224c.setHeader("X-PAYPAL-RESPONSE-DATA-FORMAT", "XML");
                this.f2224c.setHeader("X-PAYPAL-RESPONSE-DATA-FORMAT", "XML");
                this.f2224c.setHeader("X-PAYPAL-RESPONSE-DATA-FORMAT", "XML");
                this.f2224c.setHeader("x-paypal-service-version", "1.0.0");
                this.f2224c.setHeader("x-paypal-element-ordering-preserve", "false");
                this.f2224c.setEntity(stringEntity);
            } else if (str2.contains(C1107b.m2406p())) {
                if (PayPal.getInstance().getServer() == 3) {
                    this.f2224c.setHeader("CLIENT-AUTH", "No cert");
                } else {
                    this.f2224c.setHeader("X-PAYPAL-SECURITY-PASSWORD", "MPL");
                    this.f2224c.setHeader("X-PAYPAL-SECURITY-USERID", "MPL");
                    this.f2224c.setHeader("X-PAYPAL-SECURITY-SIGNATURE", "MPL");
                }
                this.f2224c.setHeader("X-PAYPAL-MESSAGE-PROTOCOL", "SOAP11");
                this.f2224c.setHeader("X-PAYPAL-DEVICE-IPADDRESS", C1107b.m2360A());
                this.f2224c.setHeader("X-PAYPAL-APPLICATION-ID", PayPalActivity._paypal.getAppID());
                this.f2224c.setHeader("X-PAYPAL-DEVICE-AUTH-TOKEN", f2223q);
                this.f2224c.setHeader("X-PAYPAL-REQUEST-SOURCE", PayPal.getVersionWithoutBuild());
                this.f2224c.setHeader("X-PAYPAL-REQUEST-DATA-FORMAT", "XML");
                this.f2224c.setHeader("X-PAYPAL-RESPONSE-DATA-FORMAT", "XML");
                this.f2224c.setHeader("x-paypal-service-version", "1.0.0");
                this.f2224c.setHeader("x-paypal-element-ordering-preserve", "false");
                this.f2224c.setEntity(stringEntity);
            } else {
                str3 = "ErrorId=-1";
                this.f2224c = null;
                this.f2225d = null;
                return str3;
            }
            PayPal.logd("NetworkHandler", "postXML do execute");
            HttpResponse execute = this.f2225d.execute(this.f2224c);
            HttpEntity entity = execute.getEntity();
            Header[] allHeaders = execute.getAllHeaders();
            if (allHeaders != null) {
                i = 0;
                for (Header header : allHeaders) {
                    if (header.getName().compareTo("Content-Length") == 0) {
                        i = Integer.parseInt(header.getValue());
                    }
                }
            } else {
                i = 0;
            }
            PayPal.logd("NetworkHandler", "postXML setup to read reponse");
            content = entity.getContent();
            try {
                InputStream dataInputStream2 = new DataInputStream(content);
                try {
                    if (content.available() > i) {
                        i = content.available();
                    }
                    if (dataInputStream2.available() > i) {
                        i = dataInputStream2.available();
                    }
                    PayPal.logd("NetworkHandler", "postXML do read response");
                    if (i != 0) {
                        byte[] bArr = new byte[i];
                        dataInputStream2.readFully(bArr);
                        str3 = new String(bArr, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                    } else {
                        str3 = new String(C1107b.m2374a(dataInputStream2), AsyncHttpResponseHandler.DEFAULT_CHARSET);
                    }
                    if (entity != null) {
                        entity.consumeContent();
                    }
                    dataInputStream2.close();
                    content.close();
                    this.f2224c = null;
                    this.f2225d = null;
                    try {
                        dataInputStream2.close();
                    } catch (Exception e) {
                    }
                    if (content != null) {
                        try {
                            content.close();
                        } catch (Exception e2) {
                            PayPal.loge("NetworkHandler", "postXML caught exception closing streams" + e2.getMessage());
                        }
                    }
                    this.f2224c = null;
                    this.f2225d = null;
                    return str3;
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        PayPal.loge("NetworkHandler", "postXML caught exception doing I/O, " + th.getMessage());
                        if (dataInputStream != null) {
                            try {
                                dataInputStream.close();
                            } catch (Exception e3) {
                            }
                        }
                        if (content != null) {
                            try {
                                content.close();
                            } catch (Exception e4) {
                                PayPal.loge("NetworkHandler", "postXML caught exception closing streams" + e4.getMessage());
                            }
                        }
                        this.f2224c = null;
                        this.f2225d = null;
                        this.f2227f = 0;
                        return null;
                    } catch (Throwable th3) {
                        th = th3;
                        if (dataInputStream != null) {
                            try {
                                dataInputStream.close();
                            } catch (Exception e5) {
                            }
                        }
                        if (content != null) {
                            try {
                                content.close();
                            } catch (Exception e22) {
                                PayPal.loge("NetworkHandler", "postXML caught exception closing streams" + e22.getMessage());
                            }
                        }
                        this.f2224c = null;
                        this.f2225d = null;
                        throw th;
                    }
                }
            } catch (Throwable th4) {
                th = th4;
                dataInputStream = null;
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
                if (content != null) {
                    content.close();
                }
                this.f2224c = null;
                this.f2225d = null;
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            dataInputStream = null;
            content = null;
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (content != null) {
                content.close();
            }
            this.f2224c = null;
            this.f2225d = null;
            throw th;
        }
    }

    static /* synthetic */ void m2366a(C1107b c1107b, int i, C0860a c0860a) {
        String f = c1107b.m2423f();
        c0860a.m1640b(i, f);
        PayPal.logd("MPL", "end " + i + " fail, " + f);
    }

    static /* synthetic */ void m2367a(C1107b c1107b, String str, C0801b c0801b) {
        String f = c1107b.m2423f();
        c0801b.m1494d(f);
        PayPal.logd("MPL", "end " + str + " fail, " + f);
    }

    private static void m2368a(String str, String str2, String str3, String str4) {
        PayPal.logd("NetworkHandler", str + " error endpoint - " + str2);
        PayPal.logd("NetworkHandler", str + " error request - " + str3);
        if (str4 != null) {
            PayPal.logd("NetworkHandler", str + " error reply - " + str4);
        }
    }

    public static final boolean m2369a() {
        try {
            if (((String) PayPalActivity._networkHandler.f2229h.get("payButtonEnable")).compareToIgnoreCase("true") == 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    static /* synthetic */ boolean m2371a(C1107b c1107b, String str, String str2) throws C0843n, C0837f, CertificateExpiredException, CertificateNotYetValidException {
        boolean z = false;
        if (!C1107b.m2414x()) {
            return false;
        }
        f2223q = XmlPullParser.NO_NAMESPACE;
        PayPal instance = PayPal.getInstance();
        StringBuilder stringBuilder = new StringBuilder();
        if (str.indexOf(XmppConnection.JID_SEPARATOR) > 0) {
            z = true;
        }
        if (z) {
            C0842m.m1584a(stringBuilder, "authorizationType", "Email");
            C0842m.m1584a(stringBuilder, "email", C1107b.m2377b(str));
            C0842m.m1584a(stringBuilder, "password", str2);
            C0842m.m1585a(stringBuilder, "bypassEncryption", true);
        } else {
            C0842m.m1584a(stringBuilder, "authorizationType", "Phone");
            stringBuilder.append("<phone>");
            C0842m.m1584a(stringBuilder, "countryCode", instance.getAccountCountryDialingCode());
            C0842m.m1584a(stringBuilder, "phoneNumber", C1107b.m2384e(str));
            stringBuilder.append("</phone>");
            C0842m.m1584a(stringBuilder, "password", str2);
            C0842m.m1585a(stringBuilder, "bypassEncryption", true);
        }
        C0842m.m1585a(stringBuilder, "authorizeDevice", instance.getIsRememberMe());
        String str3 = C1107b.m2376b() + C1107b.m2408r() + "/DeviceAuthenticateUser";
        String c = C0842m.m1601c(stringBuilder.toString());
        return c1107b.m2372a(str3, c, c1107b.m2365a(c, str3, true));
    }

    private boolean m2372a(String str, String str2, String str3) throws C0837f {
        PayPal.getInstance();
        try {
            Document parse = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(str3.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET)));
            if (m2373a(parse, str3)) {
                C1107b.m2368a("createQuickPayment", str, str2, str3);
                return false;
            }
            NodeList elementsByTagName = parse.getElementsByTagName("pinEstablished");
            if (elementsByTagName.getLength() != 1) {
                throw new C0837f("Not exactly one pinEstablished tag");
            }
            PayPal.getInstance().setPINCreated(Boolean.parseBoolean(elementsByTagName.item(0).getChildNodes().item(0).getNodeValue()));
            elementsByTagName = parse.getElementsByTagName("sessionToken");
            if (elementsByTagName.getLength() != 1) {
                throw new C0837f("Not exactly one sessionToken tag");
            }
            f2223q = elementsByTagName.item(0).getChildNodes().item(0).getNodeValue();
            elementsByTagName = parse.getElementsByTagName("deviceAuthorized");
            if (elementsByTagName.getLength() != 1) {
                throw new C0837f("Not exactly one deviceAuthorized tag");
            }
            this.f2229h.put("AuthorizedDevice", elementsByTagName.item(0).getChildNodes().item(0).getNodeValue());
            C0842m.m1588a(parse);
            return f2223q.length() > 0;
        } catch (ParserConfigurationException e) {
            C1107b.m2368a("parseDeviceAuthenticateUser", str, str2, "Exception " + e.getMessage());
            return false;
        } catch (UnsupportedEncodingException e2) {
            C1107b.m2368a("parseDeviceAuthenticateUser", str, str2, "Exception " + e2.getMessage());
            return false;
        } catch (SAXException e3) {
            C1107b.m2368a("parseDeviceAuthenticateUser", str, str2, "Exception " + e3.getMessage());
            return false;
        } catch (NullPointerException e4) {
            if (str3 == null) {
                C1107b.m2368a("parseDeviceAuthenticateUser", str, str2, "null response from server");
                return false;
            }
            C1107b.m2368a("parseDeviceAuthenticateUser", str, str2, "Exception " + e4.getMessage());
            return false;
        } catch (IOException e5) {
            C1107b.m2368a("parseDeviceAuthenticateUser", str, str2, "Exception " + e5.getMessage());
            return false;
        }
    }

    private boolean m2373a(Document document, String str) {
        this.f2227f = -1;
        if (str == null || str.length() <= 0) {
            this.f2227f = 408;
            return true;
        }
        int c = C0842m.m1599c(document);
        if (str == null) {
            this.f2227f = 408;
            return true;
        }
        if (str.contains("ErrorId=")) {
            this.f2227f = Integer.parseInt(str.substring(str.indexOf("ErrorId=") + "ErrorId=".length()));
        } else if (c != AsyncTaskManager.REQUEST_SUCCESS_CODE) {
            this.f2227f = c;
        } else if (str.contains("<SOAP-ENV:Body") && !str.contains("</SOAP-ENV:Body")) {
            this.f2227f = 0;
            return true;
        }
        if (this.f2227f == -1) {
            return false;
        }
        Intent putExtra;
        String str2 = XmlPullParser.NO_NAMESPACE + this.f2227f;
        String a = C0839h.m1568a("ANDROID_" + this.f2227f);
        if (C1107b.m2389g(XmlPullParser.NO_NAMESPACE + this.f2227f)) {
            String[] a2 = C0842m.m1592a(str);
            if (a2 != null) {
                String str3 = a;
                for (c = 0; c < a2.length; c++) {
                    str3 = str3 + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + a2[c] + (c + 1 == a2.length ? "." : ",");
                }
                a = str3;
            }
        }
        if (C1107b.m2394i(str2)) {
            String a3;
            Intent putExtra2 = new Intent(PayPalActivity.FATAL_ERROR).putExtra("FATAL_ERROR_ID", str2);
            if (str2.equals("569060")) {
                a3 = C0839h.m1568a("ANDROID_no_personal_payments");
            } else if (str2.equals("500000")) {
                a3 = C0839h.m1568a("ANDROID_10001");
            } else if (str2.equals("580001")) {
                try {
                    if (document.getElementsByTagName("parameter").item(0).getChildNodes().item(0).getNodeValue().length() == 3) {
                        a = C0839h.m1568a("ANDROID_580001_4");
                    }
                    a3 = a;
                } catch (Throwable th) {
                    a3 = a;
                }
            } else {
                if (str2.equals("520009")) {
                    try {
                        if (document.getElementsByTagName("parameter").item(0).getChildNodes().item(0).getNodeValue().length() > 0) {
                            a = C0839h.m1568a("ANDROID_520009_2");
                        }
                        a3 = a;
                    } catch (Throwable th2) {
                    }
                }
                a3 = a;
            }
            if (!(this.f2226e == 11 || PayPalActivity.getInstance() == null)) {
                PayPalActivity.getInstance().paymentFailed((String) PayPalActivity._networkHandler.m2421c("CorrelationId"), (String) PayPalActivity._networkHandler.m2421c("PayKey"), str2, a3, false, true);
            }
            putExtra = putExtra2.putExtra("FATAL_ERROR_MESSAGE", a3);
        } else {
            putExtra = (!C1107b.m2392h(str2) || this.f2226e == 11) ? (C1107b.m2392h(str2) && this.f2226e == 11) ? new Intent(PayPalActivity.FATAL_ERROR).putExtra("FATAL_ERROR_ID", str2).putExtra("FATAL_ERROR_MESSAGE", C0839h.m1568a("ANDROID_pin_creation_timeout")).putExtra("ERROR_TIMEOUT", a) : new Intent(PayPalActivity.CREATE_PAYMENT_FAIL) : new Intent(PayPalActivity.LOGIN_FAIL).putExtra("FATAL_ERROR_ID", str2).putExtra("ERROR_TIMEOUT", a);
        }
        try {
            PayPalActivity.getInstance().sendBroadcast(putExtra);
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    private static byte[] m2374a(InputStream inputStream) {
        int i = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i2 = 0;
        byte[] bArr = null;
        while (true) {
            int read = inputStream.read();
            if (read == -1) {
                break;
            }
            if (bArr == null) {
                bArr = new byte[KEYRecord.Flags.FLAG5];
            }
            if (i2 == KEYRecord.Flags.FLAG5) {
                byteArrayOutputStream.write(bArr);
                try {
                    bArr = new byte[KEYRecord.Flags.FLAG5];
                    i2 = 0;
                } catch (IOException e) {
                    return null;
                }
            }
            try {
                bArr[i2] = (byte) read;
                i2++;
            } catch (IOException e2) {
                return bArr;
            }
        }
        if (i2 != 0) {
            while (i < i2) {
                byteArrayOutputStream.write(bArr[i]);
                i++;
            }
        }
        bArr = new byte[byteArrayOutputStream.size()];
        bArr = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return bArr;
    }

    public static String m2376b() {
        switch (PayPal.getInstance().getServer()) {
            case KEYRecord.OWNER_USER /*0*/:
                return "https://mobileclient.sandbox.paypal.com/";
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return XmlPullParser.NO_NAMESPACE;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return "https://www.stage2mb101.paypal.com:10521/";
            default:
                return "https://mobileclient.paypal.com/";
        }
    }

    public static String m2377b(String str) {
        return str != null ? str.replace(AlixDefine.split, "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;") : str;
    }

    public static void m2380c() {
        if (f2218l == null) {
            f2218l = new C0833a();
        }
        if (PayPalActivity._networkHandler == null) {
            PayPalActivity._networkHandler = new C1107b();
        }
        if (PayPalActivity._networkHandler.f2229h == null) {
            PayPalActivity._networkHandler.f2229h = new Hashtable();
        }
        if (PayPal.getInstance().getServer() == 2) {
            PayPalActivity._networkHandler.f2229h.put("payButtonEnable", "true");
            PayPal.getInstance().setLibraryInitialized(true);
            return;
        }
        if (!PayPalActivity._networkHandler.f2231j.isAlive()) {
            PayPalActivity._networkHandler.f2231j.start();
        }
        PayPalActivity._networkHandler.m2417a(8);
    }

    public static void m2381d() {
        if (PayPalActivity._networkHandler != null) {
            C1107b c1107b = PayPalActivity._networkHandler;
            c1107b.f2230i = true;
            while (c1107b.f2231j.isAlive()) {
                PayPal.logd("NetworkHandler", "waiting for thread to stop");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    PayPal.logd("NetworkHandler", "waiting for thread to stop");
                }
            }
            PayPal.logd("NetworkHandler", "thread has stopped");
            PayPalActivity._networkHandler = null;
        }
    }

    public static C1107b m2383e() {
        return PayPalActivity._networkHandler;
    }

    public static String m2384e(String str) {
        if (str == null || str.length() == 0) {
            return XmlPullParser.NO_NAMESPACE;
        }
        if (!str.startsWith("+")) {
            return str;
        }
        str = str.substring(1);
        for (String str2 : f2222p) {
            if (str.startsWith(str2)) {
                return str.substring(str2.length());
            }
        }
        return str;
    }

    static /* synthetic */ boolean m2385e(C1107b c1107b) throws C0837f {
        if (!C1107b.m2414x()) {
            return false;
        }
        f2223q = XmlPullParser.NO_NAMESPACE;
        StringBuilder stringBuilder = new StringBuilder();
        C0842m.m1584a(stringBuilder, "authorizationType", "Device");
        C0842m.m1584a(stringBuilder, "authorizeDevice", "true");
        String str = C1107b.m2376b() + C1107b.m2408r() + "/DeviceAuthenticateUser";
        String c = C0842m.m1601c(stringBuilder.toString());
        return c1107b.m2372a(str, c, c1107b.m2365a(c, str, true));
    }

    public static String m2386f(String str) {
        if (str != null && str.length() > 0 && str.charAt(0) == SignatureVisitor.EXTENDS) {
            String substring = str.substring(1);
            for (String str2 : f2222p) {
                if (substring.startsWith(str2)) {
                    return str2;
                }
            }
        }
        return C1107b.m2400m();
    }

    private static boolean m2389g(String str) {
        String[] strArr = new String[]{"560027", "580022", "580023"};
        for (String equals : strArr) {
            if (equals.equals(str)) {
                return true;
            }
        }
        return false;
    }

    static /* synthetic */ String m2390h(C1107b c1107b) {
        String a = C0842m.m1581a(c1107b.f2229h);
        String str = C1107b.m2406p() + "ExecutePayment/";
        String a2 = c1107b.m2365a(a, str, false);
        if (c1107b.m2398k(a2)) {
            C1107b.m2368a("sendPayment", str, a, a2);
            return null;
        }
        a = C0842m.m1594b(a2);
        return a == null ? "-1" : a;
    }

    public static final DefaultHttpClient m2391h() {
        if (PayPal.getInstance().getServer() != 3) {
            return new DefaultHttpClient();
        }
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        try {
            schemeRegistry.register(new Scheme("https", new C0834c(), 443));
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        } catch (KeyStoreException e3) {
            e3.printStackTrace();
        } catch (UnrecoverableKeyException e4) {
            e4.printStackTrace();
        }
        HttpParams basicHttpParams = new BasicHttpParams();
        basicHttpParams.setParameter("http.conn-manager.max-total", Integer.valueOf(30));
        basicHttpParams.setParameter("http.conn-manager.max-per-route", new ConnPerRouteBean(30));
        basicHttpParams.setParameter("http.protocol.expect-continue", Boolean.valueOf(false));
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        return new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
    }

    private static boolean m2392h(String str) {
        String[] strArr = new String[]{"10818", "10897", "10898", "10899", "520003"};
        for (String equals : strArr) {
            if (equals.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private static boolean m2394i(String str) {
        String[] strArr = new String[]{"10001", "10004", "10800", "10801", "10802", "10804", "10805", "10806", "10808", "10809", "10810", "10811", "10812", "10813", "10815", "10819", "10820", "10821", "10822", "10823", "10824", "10825", "10849", "10850", "10858", "10859", "10860", "10861", "10862", "10863", "10864", "10867", "99999", "520002", "520009", "539041", "540031", "550001", "550006", "559044", "560027", "569000", "569042", "569056", "569060", "579007", "579017", "579033", "579040", "579045", "579047", "579048", "580001", "580022", "580023", "580028", "580030", "580031", "580032", "580033", "580034", "589009", "589019", "500000"};
        for (String equals : strArr) {
            if (equals.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private static String m2395j(String str) throws CertificateExpiredException, CertificateNotYetValidException {
        try {
            InputStream byteArrayInputStream;
            switch (PayPal.getInstance().getServer()) {
                case KEYRecord.OWNER_USER /*0*/:
                    byteArrayInputStream = new ByteArrayInputStream(f2221o);
                    break;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    byteArrayInputStream = new ByteArrayInputStream(f2219m);
                    break;
                default:
                    byteArrayInputStream = new ByteArrayInputStream(f2220n);
                    break;
            }
            X509Certificate x509Certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(byteArrayInputStream);
            byteArrayInputStream.close();
            x509Certificate.checkValidity(new Date());
            RSAPublicKey rSAPublicKey = (RSAPublicKey) x509Certificate.getPublicKey();
            Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            instance.init(1, rSAPublicKey);
            return C1107b.m2377b(C0832a.m1546a(instance.doFinal(str.getBytes()), 8));
        } catch (FileNotFoundException e) {
            PayPal.loge("NetworkHandler", "encryptPassword faulted " + e.getMessage());
            return XmlPullParser.NO_NAMESPACE;
        } catch (CertificateException e2) {
            PayPal.loge("NetworkHandler", "encryptPassword faulted " + e2.getMessage());
            return XmlPullParser.NO_NAMESPACE;
        } catch (IOException e3) {
            PayPal.loge("NetworkHandler", "encryptPassword faulted " + e3.getMessage());
            return XmlPullParser.NO_NAMESPACE;
        } catch (NoSuchAlgorithmException e4) {
            PayPal.loge("NetworkHandler", "encryptPassword faulted " + e4.getMessage());
            return XmlPullParser.NO_NAMESPACE;
        } catch (NoSuchPaddingException e5) {
            PayPal.loge("NetworkHandler", "encryptPassword faulted " + e5.getMessage());
            return XmlPullParser.NO_NAMESPACE;
        } catch (InvalidKeyException e6) {
            PayPal.loge("NetworkHandler", "encryptPassword faulted " + e6.getMessage());
            return XmlPullParser.NO_NAMESPACE;
        } catch (IllegalBlockSizeException e7) {
            PayPal.loge("NetworkHandler", "encryptPassword faulted " + e7.getMessage());
            return XmlPullParser.NO_NAMESPACE;
        } catch (BadPaddingException e8) {
            PayPal.loge("NetworkHandler", "encryptPassword faulted " + e8.getMessage());
            return XmlPullParser.NO_NAMESPACE;
        }
    }

    private boolean m2398k(String str) {
        ParserConfigurationException parserConfigurationException = null;
        try {
            if (!m2373a(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET))), str)) {
                return false;
            }
        } catch (SAXException e) {
            parserConfigurationException = e;
        } catch (IOException e2) {
            parserConfigurationException = e2;
        } catch (ParserConfigurationException e3) {
            parserConfigurationException = e3;
        }
        if (parserConfigurationException == null) {
            PayPal.loge("NetworkHandler", "Response contains an error, " + str);
        } else {
            PayPal.loge("NetworkHandler", "Exception checking for error in response, " + str);
        }
        return true;
    }

    public static String m2400m() {
        String toUpperCase = Locale.getDefault().getCountry().toUpperCase();
        return toUpperCase.compareTo("US") == 0 ? Contact.RELATION_FRIEND : toUpperCase.compareTo("CA") == 0 ? Contact.RELATION_FRIEND : toUpperCase.compareTo("GB") == 0 ? "44" : toUpperCase.compareTo("AU") == 0 ? "61" : toUpperCase.compareTo("FR") == 0 ? "33" : toUpperCase.compareTo("ES") == 0 ? "34" : toUpperCase.compareTo("IT") == 0 ? "39" : Contact.RELATION_FRIEND;
    }

    public static String m2402n() {
        return f2223q;
    }

    private static String m2406p() {
        switch (PayPal.getInstance().getServer()) {
            case KEYRecord.OWNER_USER /*0*/:
                return "https://svcs.sandbox.paypal.com/AdaptivePayments/";
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return XmlPullParser.NO_NAMESPACE;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return "https://www.stage2mb101.paypal.com:10279/AdaptivePayments/";
            default:
                return "https://svcs.paypal.com/AdaptivePayments/";
        }
    }

    private static String m2407q() {
        switch (PayPal.getInstance().getServer()) {
            case KEYRecord.OWNER_USER /*0*/:
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return "GMAdapter";
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return XmlPullParser.NO_NAMESPACE;
            default:
                return "GMAdapter";
        }
    }

    private static String m2408r() {
        switch (PayPal.getInstance().getServer()) {
            case KEYRecord.OWNER_USER /*0*/:
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return "GMAdapter";
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return XmlPullParser.NO_NAMESPACE;
            default:
                return "GMAdapter";
        }
    }

    private boolean m2409s() {
        try {
            String g = C0842m.m1615g(this.f2229h);
            String str = C1107b.m2406p() + "SetPaymentOptions/";
            String a = m2365a(g, str, false);
            if (m2398k(a)) {
                C1107b.m2368a("setPaymentOptions", str, g, a);
                try {
                    C0842m.m1590a(a, this.f2229h);
                    return false;
                } catch (Throwable th) {
                    return false;
                }
            }
            C0842m.m1590a(a, this.f2229h);
            return true;
        } catch (C0843n e) {
            PayPal.loge("NetworkHandler", "Exception " + e.getMessage());
            return false;
        }
    }

    private boolean m2410t() {
        String h = C0842m.m1617h(this.f2229h);
        String str = C1107b.m2406p() + "GetAvailableShippingAddresses/";
        String a = m2365a(h, str, false);
        if (m2398k(a)) {
            C1107b.m2368a("getAddresses", str, h, a);
            try {
                C0842m.m1616g(a, this.f2229h);
                return false;
            } catch (Throwable th) {
                return false;
            }
        }
        C0842m.m1616g(a, this.f2229h);
        return true;
    }

    private boolean m2411u() {
        String b = C0842m.m1595b(this.f2229h);
        String str = C1107b.m2406p() + "Preapproval/";
        String a = m2365a(b, str, false);
        if (m2398k(a)) {
            C1107b.m2368a("createPreapprovalRequest", str, b, a);
            try {
                C0842m.m1597b(a, this.f2229h);
                return false;
            } catch (Throwable th) {
                return false;
            }
        }
        C0842m.m1597b(a, this.f2229h);
        return true;
    }

    private boolean m2412v() {
        String a = m2365a(C0842m.m1606d(this.f2229h), C1107b.m2406p() + "PreapprovalDetails/", false);
        if (m2398k(a)) {
            try {
                C0842m.m1603c(a, this.f2229h);
                return false;
            } catch (Throwable th) {
                return false;
            }
        }
        C0842m.m1603c(a, this.f2229h);
        if (!this.f2229h.get("Approved").equals("true")) {
            return true;
        }
        Intent putExtra = new Intent(PayPalActivity.FATAL_ERROR).putExtra("FATAL_ERROR_ID", "-1").putExtra("FATAL_ERROR_MESSAGE", C0839h.m1568a("ANDROID_preapproval_already_approved"));
        PayPalActivity.getInstance().paymentFailed((String) PayPalActivity._networkHandler.m2421c("CorrelationId"), (String) PayPalActivity._networkHandler.m2421c("PayKey"), "-1", C0839h.m1568a("ANDROID_preapproval_already_approved"), false, true);
        PayPalActivity.getInstance().sendBroadcast(putExtra);
        return false;
    }

    private boolean m2413w() {
        String c = C0842m.m1602c(this.f2229h);
        String str = C1107b.m2406p() + "ConfirmPreapproval/";
        String a = m2365a(c, str, false);
        if (m2398k(a)) {
            C1107b.m2368a("confirmPreapprovalRequest", str, c, a);
            try {
                C0842m.m1607d(a, this.f2229h);
                return false;
            } catch (Throwable th) {
                return false;
            }
        }
        C0842m.m1607d(a, this.f2229h);
        return true;
    }

    private static boolean m2414x() {
        String deviceId = ((TelephonyManager) PayPal.getInstance().getParentContext().getSystemService("phone")).getDeviceId();
        if (deviceId == null) {
            deviceId = ((WifiManager) PayPal.getInstance().getParentContext().getSystemService("wifi")).getConnectionInfo().getMacAddress();
        }
        if (PayPal.getInstance().getServer() != 1 || !r0.equals("000000000000000")) {
            return true;
        }
        Intent putExtra = new Intent(PayPalActivity.FATAL_ERROR).putExtra("FATAL_ERROR_ID", "-1").putExtra("FATAL_ERROR_MESSAGE", C0839h.m1568a("ANDROID_simulator_payment_block"));
        PayPalActivity.getInstance().paymentFailed((String) PayPalActivity._networkHandler.m2421c("CorrelationId"), (String) PayPalActivity._networkHandler.m2421c("PayKey"), "-1", C0839h.m1568a("ANDROID_simulator_payment_block"), false, true);
        PayPalActivity.getInstance().sendBroadcast(putExtra);
        return false;
    }

    private Hashtable<String, Object> m2415y() {
        try {
            String f = C0842m.m1613f(this.f2229h);
            if (f == null) {
                return null;
            }
            String str = C1107b.m2406p() + "Pay/";
            String a = m2365a(f, str, false);
            Intent putExtra;
            if (m2398k(a)) {
                C1107b.m2368a("createPayment", str, f, a);
                if (!a.contains("<errorId>580022</errorId>")) {
                    return null;
                }
                String a2 = C0839h.m1568a("ANDROID_580022");
                putExtra = new Intent(PayPalActivity.FATAL_ERROR).putExtra("FATAL_ERROR_ID", "580022").putExtra("FATAL_ERROR_MESSAGE", a2);
                PayPalActivity.getInstance().paymentFailed((String) PayPalActivity._networkHandler.m2421c("CorrelationId"), (String) PayPalActivity._networkHandler.m2421c("PayKey"), "580022", a2, false, true);
                try {
                    PayPalActivity.getInstance().sendBroadcast(putExtra);
                } catch (Exception e) {
                }
                return null;
            } else if (!C0842m.m1590a(a, this.f2229h)) {
                this.f2227f = -1;
                return null;
            } else if (a.contains("defaultFundingPlan") || !((String) this.f2229h.get("ActionType")).equals("CREATE")) {
                return this.f2229h;
            } else {
                putExtra = new Intent(PayPalActivity.FATAL_ERROR).putExtra("FATAL_ERROR_ID", "589009").putExtra("FATAL_ERROR_MESSAGE", C0839h.m1568a("ANDROID_589009"));
                PayPalActivity.getInstance().paymentFailed((String) PayPalActivity._networkHandler.m2421c("CorrelationId"), (String) PayPalActivity._networkHandler.m2421c("PayKey"), "589009", C0839h.m1568a("ANDROID_589009"), false, true);
                try {
                    PayPalActivity.getInstance().sendBroadcast(putExtra);
                } catch (Exception e2) {
                }
                return null;
            }
        } catch (C0843n e3) {
            PayPal.loge("NetworkHandler", "Exception " + e3.getMessage());
            return null;
        }
    }

    private Hashtable<String, Object> m2416z() {
        String e = C0842m.m1610e(this.f2229h);
        String str = C1107b.m2406p() + "GetFundingPlans/";
        String a = m2365a(e, str, false);
        if (m2398k(a)) {
            C1107b.m2368a("createFundingRequest", str, e, a);
            try {
                C0842m.m1611e(a, this.f2229h);
            } catch (Throwable th) {
            }
            return null;
        } else if (!C0842m.m1611e(a, this.f2229h)) {
            this.f2227f = -1;
            return null;
        } else if (((Vector) this.f2229h.get("FundingPlans")).size() != 0) {
            return this.f2229h;
        } else {
            try {
                PayPalActivity.getInstance().sendBroadcast(new Intent(PayPalActivity.FATAL_ERROR).putExtra("FATAL_ERROR_ID", "589009").putExtra("FATAL_ERROR_MESSAGE", C0839h.m1568a("ANDROID_589009")));
            } catch (Exception e2) {
            }
            return null;
        }
    }

    public final void m2417a(int i) {
        this.f2228g = i;
        this.f2226e = i;
    }

    public final void m2418a(int i, Object obj) {
        PayPal instance = PayPal.getInstance();
        PayPalActivity instance2 = PayPalActivity.getInstance();
        C1107b c1107b = PayPalActivity._networkHandler;
        switch (i) {
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                if (((String) this.f2229h.get("quickPay")).equals("true")) {
                    m2417a(4);
                } else if (instance.getShippingEnabled()) {
                    m2417a(7);
                } else {
                    instance2.sendBroadcast(new Intent(PayPalActivity.CREATE_PAYMENT_SUCCESS));
                }
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                String str = (String) c1107b.m2421c("PayKey");
                String str2 = (String) c1107b.m2421c("PaymentExecStatus");
                if (!instance.isHeavyCountry() || instance.hasCreatedPIN()) {
                    instance2.paymentSucceeded(str, str2, true);
                    return;
                }
                instance2.setTransactionSuccessful(true);
                instance2.paymentSucceeded(str, str2, false);
                C1103e.f2180a = (String) obj;
                C08051.m1499b(7);
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                m2420a("FundingPlanId", Contact.RELATION_ASK);
                if (instance.getShippingEnabled()) {
                    m2417a(7);
                } else {
                    instance2.sendBroadcast(new Intent(PayPalActivity.CREATE_PAYMENT_SUCCESS));
                }
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                Hashtable hashtable;
                int k;
                Intent putExtra;
                if (instance.getServer() == 2) {
                    hashtable = C1099a.f2141a;
                } else {
                    C1099a.f2141a = (Hashtable) obj;
                    hashtable = c1107b.f2229h;
                }
                Vector vector = (Vector) hashtable.get("AvailableAddresses");
                if (vector != null && vector.size() > 0) {
                    C0828h c0828h = (C0828h) vector.get(0);
                    if (((String) m2421c("ShippingAddressId")) == null) {
                        m2420a("ShippingAddressId", c0828h.m1543h());
                        k = m2427k();
                        if (k == 0) {
                            instance2.sendBroadcast(new Intent(PayPalActivity.CREATE_PAYMENT_SUCCESS));
                        }
                        if (k == -1) {
                            putExtra = new Intent(PayPalActivity.FATAL_ERROR).putExtra("FATAL_ERROR_ID", "-1").putExtra("FATAL_ERROR_MESSAGE", instance.getAdjustPaymentError());
                            instance2.paymentFailed((String) c1107b.m2421c("CorrelationId"), (String) c1107b.m2421c("PayKey"), "-1", instance.getAdjustPaymentError(), false, true);
                            instance2.sendBroadcast(putExtra);
                        }
                    }
                }
                k = 0;
                if (k == 0) {
                    instance2.sendBroadcast(new Intent(PayPalActivity.CREATE_PAYMENT_SUCCESS));
                }
                if (k == -1) {
                    putExtra = new Intent(PayPalActivity.FATAL_ERROR).putExtra("FATAL_ERROR_ID", "-1").putExtra("FATAL_ERROR_MESSAGE", instance.getAdjustPaymentError());
                    instance2.paymentFailed((String) c1107b.m2421c("CorrelationId"), (String) c1107b.m2421c("PayKey"), "-1", instance.getAdjustPaymentError(), false, true);
                    instance2.sendBroadcast(putExtra);
                }
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                C1106h.f2212a = (String) obj;
                C08051.m1497a(4);
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                instance2.sendBroadcast(new Intent(PayPalActivity.CREATE_PAYMENT_SUCCESS));
            default:
        }
    }

    public final void m2419a(String str) {
        PayPal instance = PayPal.getInstance();
        if (instance.getServer() == 1) {
            PayPal.logd("MPL Tracking", "Post: " + str);
            String str2 = "Device";
            if (instance.getAuthMethod() == 0 || instance.getAuthMethod() == 3) {
                str2 = "Password";
            } else if (instance.getAuthMethod() == 1) {
                str2 = "PIN";
            }
            String str3 = "Simple";
            if (instance.getPayType() == 3) {
                str3 = "Preapproval";
            } else if (instance.getPayType() == 2) {
                str3 = "Chained";
            } else if (instance.getPayType() == 1) {
                str3 = "Parallel";
            }
            StringBuilder stringBuilder = new StringBuilder("https://sstats.paypal-metrics.com/b/ss/paypalwireless/5/H.5--WAP/12345?pageName=android/");
            stringBuilder.append("mpl-").append(str);
            stringBuilder.append("&c1=").append(Locale.getDefault().getCountry().toUpperCase());
            stringBuilder.append("&c4=ver").append(PayPal.getVersion());
            stringBuilder.append("&c5=").append("Android");
            stringBuilder.append("&c6=").append(instance.getParentContext().getPackageName());
            stringBuilder.append("&c7=").append(str2);
            stringBuilder.append("&c9=").append(str3);
            stringBuilder.append("&c10=").append(instance.getShippingEnabled() ? "Enabled" : "Disabled");
            str2 = stringBuilder.toString();
            PayPal.logd("NetworkHandler", "queueTrackingPost (), queue tracking " + str2);
            synchronized (f2217k) {
                f2217k.add(str2);
                f2217k.notifyAll();
            }
        }
    }

    public final void m2420a(String str, Object obj) {
        if (obj != null) {
            this.f2229h.put(str, obj);
        } else {
            this.f2229h.remove(str);
        }
    }

    public final Object m2421c(String str) {
        return this.f2229h.get(str);
    }

    public final void m2422d(String str) {
        Intent putExtra = new Intent(PayPalActivity.FATAL_ERROR).putExtra("FATAL_ERROR_ID", "-1").putExtra("FATAL_ERROR_MESSAGE", str);
        PayPalActivity.getInstance().paymentFailed((String) PayPalActivity._networkHandler.m2421c("CorrelationId"), (String) PayPalActivity._networkHandler.m2421c("PayKey"), "-1", str, false, true);
        PayPalActivity.getInstance().sendBroadcast(putExtra);
    }

    public final String m2423f() {
        if (this.f2227f == 0) {
            this.f2227f = 408;
        }
        return C0839h.m1568a("ANDROID_" + this.f2227f);
    }

    public final Hashtable<String, Object> m2424g() {
        return this.f2229h;
    }

    public final void m2425i() {
        Object obj;
        PayPal instance = PayPal.getInstance();
        PayPalAdvancedPayment payment = instance.getPayment();
        this.f2229h.remove("ShippingAddressId");
        m2420a("PaymentCurrencyID", payment.getCurrencyType());
        m2420a("CancelUrl", instance.getCancelUrl());
        m2420a("ReturnUrl", instance.getReturnUrl());
        switch (instance.getFeesPayer()) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                obj = "SENDER";
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                obj = "PRIMARYRECEIVER";
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                obj = "SECONDARYONLY";
                break;
            default:
                obj = "EACHRECEIVER";
                break;
        }
        m2420a("FeesPayer", obj);
        m2420a("ActionType", (Object) "CREATE");
        m2420a("Receivers", payment.getReceivers());
        if (!(payment.getIpnUrl() == null || payment.getIpnUrl().equals(XmlPullParser.NO_NAMESPACE))) {
            m2420a("IpnNotificationUrl", payment.getIpnUrl());
        }
        if (!(payment.getMemo() == null || payment.getMemo().equals(XmlPullParser.NO_NAMESPACE))) {
            m2420a("Memo", payment.getMemo());
        }
        m2420a("delegate", (Object) this);
        PayPalActivity._networkHandler.m2417a(3);
    }

    public final void m2426j() {
        m2420a("PreapprovalKey", PayPal.getInstance().getPreapprovalKey());
        m2420a("delegate", (Object) this);
        PayPalActivity._networkHandler.m2417a(13);
    }

    public final int m2427k() {
        int i = 0;
        PayPal instance = PayPal.getInstance();
        if (instance.getDynamicAmountCalculationEnabled() && instance.getShippingEnabled()) {
            C0828h c0828h;
            PayPalReceiverDetails payPalReceiverDetails;
            String str = (String) this.f2229h.get("ShippingAddressId");
            Vector vector = (Vector) (PayPal.getInstance().getServer() == 2 ? C1099a.f2141a : this.f2229h).get("AvailableAddresses");
            for (int i2 = 0; i2 < vector.size(); i2++) {
                c0828h = (C0828h) vector.elementAt(i2);
                if (c0828h.m1543h().equals(str)) {
                    break;
                }
            }
            c0828h = null;
            MEPAddress mEPAddress = new MEPAddress();
            str = c0828h.m1535d();
            String e = c0828h.m1537e();
            String b = c0828h.m1531b();
            String g = c0828h.m1541g();
            String f = c0828h.m1539f();
            String c = c0828h.m1533c();
            mEPAddress.setStreet1(str);
            mEPAddress.setStreet2(e);
            mEPAddress.setCity(b);
            mEPAddress.setState(g);
            mEPAddress.setPostalcode(f);
            mEPAddress.setCountrycode(c);
            PayPalAdvancedPayment payment = PayPal.getInstance().getPayment();
            ArrayList receivers = payment.getReceivers();
            Vector vector2 = new Vector();
            for (int i3 = 0; i3 < payment.getReceivers().size(); i3++) {
                payPalReceiverDetails = (PayPalReceiverDetails) payment.getReceivers().get(i3);
                MEPReceiverAmounts mEPReceiverAmounts = new MEPReceiverAmounts();
                mEPReceiverAmounts.amounts = new MEPAmounts();
                mEPReceiverAmounts.receiver = payPalReceiverDetails.getRecipient();
                if (payPalReceiverDetails.getInvoiceData().getTax() != null) {
                    mEPReceiverAmounts.amounts.setTax(payPalReceiverDetails.getInvoiceData().getTax());
                }
                if (payPalReceiverDetails.getInvoiceData().getShipping() != null) {
                    mEPReceiverAmounts.amounts.setShipping(payPalReceiverDetails.getInvoiceData().getShipping());
                }
                if (payment.getCurrencyType() != null) {
                    mEPReceiverAmounts.amounts.setCurrency(payment.getCurrencyType());
                }
                if (payPalReceiverDetails.getSubtotal() != null) {
                    mEPReceiverAmounts.amounts.setPaymentAmount(payPalReceiverDetails.getSubtotal());
                }
                vector2.add(mEPReceiverAmounts);
            }
            Vector adjustAmountsAdvanced = PayPalActivity.getInstance().adjustAmountsAdvanced(mEPAddress, payment.getCurrencyType(), vector2);
            if (adjustAmountsAdvanced == null) {
                return -1;
            }
            if (adjustAmountsAdvanced.size() == receivers.size()) {
                MEPReceiverAmounts mEPReceiverAmounts2;
                int i4 = 0;
                int i5 = 0;
                while (i4 < adjustAmountsAdvanced.size()) {
                    payPalReceiverDetails = (PayPalReceiverDetails) receivers.get(i4);
                    mEPReceiverAmounts2 = (MEPReceiverAmounts) adjustAmountsAdvanced.elementAt(i4);
                    BigDecimal tax = payPalReceiverDetails.getInvoiceData().getTax() != null ? payPalReceiverDetails.getInvoiceData().getTax() : new BigDecimal("0.0");
                    BigDecimal shipping = payPalReceiverDetails.getInvoiceData().getShipping() != null ? payPalReceiverDetails.getInvoiceData().getShipping() : new BigDecimal("0.0");
                    BigDecimal subtotal = payPalReceiverDetails.getSubtotal() != null ? payPalReceiverDetails.getSubtotal() : new BigDecimal("0.0");
                    str = payPalReceiverDetails.getRecipient();
                    BigDecimal tax2 = mEPReceiverAmounts2.amounts.getTax();
                    BigDecimal shipping2 = mEPReceiverAmounts2.amounts.getShipping();
                    BigDecimal paymentAmount = mEPReceiverAmounts2.amounts.getPaymentAmount();
                    e = mEPReceiverAmounts2.receiver;
                    if (paymentAmount == subtotal && str.equals(e)) {
                        int i6 = (tax2 == tax && shipping2 == shipping) ? i5 : 1;
                        i4++;
                        i5 = i6;
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
                if (i5 != 0) {
                    while (i < adjustAmountsAdvanced.size()) {
                        payPalReceiverDetails = (PayPalReceiverDetails) receivers.get(i);
                        mEPReceiverAmounts2 = (MEPReceiverAmounts) adjustAmountsAdvanced.elementAt(i);
                        payPalReceiverDetails.getInvoiceData().setShipping(mEPReceiverAmounts2.amounts.getShipping());
                        payPalReceiverDetails.getInvoiceData().setTax(mEPReceiverAmounts2.amounts.getTax());
                        i++;
                    }
                    m2420a("PaymentCurrencyID", payment.getCurrencyType());
                    m2420a("Receivers", payment.getReceivers());
                    if (!(payment.getIpnUrl() == null || payment.getIpnUrl().equals(XmlPullParser.NO_NAMESPACE))) {
                        m2420a("IpnNotificationUrl", payment.getIpnUrl());
                    }
                    if (!(payment.getMemo() == null || payment.getMemo().equals(XmlPullParser.NO_NAMESPACE))) {
                        m2420a("Memo", payment.getMemo());
                    }
                    m2420a("ActionType", (Object) "CREATE");
                    m2420a("delegate", (Object) this);
                    m2417a(3);
                    return 1;
                }
            }
            throw new IllegalArgumentException();
        }
        return 0;
    }

    public final void m2428l() {
    }
}
