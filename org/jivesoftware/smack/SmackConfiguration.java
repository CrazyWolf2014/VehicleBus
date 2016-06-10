package org.jivesoftware.smack;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import org.xmlpull.v1.XmlPullParser;

public final class SmackConfiguration {
    private static final String SMACK_VERSION = "3.2.2";
    private static boolean autoEnableEntityCaps;
    private static Vector<String> defaultMechs;
    private static int defaultPingInterval;
    private static int keepAliveInterval;
    private static boolean localSocks5ProxyEnabled;
    private static int localSocks5ProxyPort;
    private static int packetCollectorSize;
    private static int packetReplyTimeout;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static {
        /*
        r2 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r0 = 0;
        r8 = 1;
        packetReplyTimeout = r2;
        r1 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        keepAliveInterval = r1;
        r1 = new java.util.Vector;
        r1.<init>();
        defaultMechs = r1;
        localSocks5ProxyEnabled = r8;
        r1 = 7777; // 0x1e61 float:1.0898E-41 double:3.8423E-320;
        localSocks5ProxyPort = r1;
        packetCollectorSize = r2;
        r1 = 1800000; // 0x1b7740 float:2.522337E-39 double:8.89318E-318;
        defaultPingInterval = r1;
        autoEnableEntityCaps = r0;
        r3 = getClassLoaders();	 Catch:{ Exception -> 0x00af }
        r4 = r3.length;	 Catch:{ Exception -> 0x00af }
        r2 = r0;
    L_0x0026:
        if (r2 >= r4) goto L_0x00b3;
    L_0x0028:
        r0 = r3[r2];	 Catch:{ Exception -> 0x00af }
        r1 = "META-INF/smack-config.xml";
        r5 = r0.getResources(r1);	 Catch:{ Exception -> 0x00af }
    L_0x0030:
        r0 = r5.hasMoreElements();	 Catch:{ Exception -> 0x00af }
        if (r0 == 0) goto L_0x013b;
    L_0x0036:
        r0 = r5.nextElement();	 Catch:{ Exception -> 0x00af }
        r0 = (java.net.URL) r0;	 Catch:{ Exception -> 0x00af }
        r1 = 0;
        r1 = r0.openStream();	 Catch:{ Exception -> 0x008b }
        r0 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ Exception -> 0x008b }
        r6 = r0.newPullParser();	 Catch:{ Exception -> 0x008b }
        r0 = "http://xmlpull.org/v1/doc/features.html#process-namespaces";
        r7 = 1;
        r6.setFeature(r0, r7);	 Catch:{ Exception -> 0x008b }
        r0 = "UTF-8";
        r6.setInput(r1, r0);	 Catch:{ Exception -> 0x008b }
        r0 = r6.getEventType();	 Catch:{ Exception -> 0x008b }
    L_0x0058:
        r7 = 2;
        if (r0 != r7) goto L_0x006a;
    L_0x005b:
        r0 = r6.getName();	 Catch:{ Exception -> 0x008b }
        r7 = "className";
        r0 = r0.equals(r7);	 Catch:{ Exception -> 0x008b }
        if (r0 == 0) goto L_0x0076;
    L_0x0067:
        parseClassToLoad(r6);	 Catch:{ Exception -> 0x008b }
    L_0x006a:
        r0 = r6.next();	 Catch:{ Exception -> 0x008b }
        if (r0 != r8) goto L_0x0058;
    L_0x0070:
        r1.close();	 Catch:{ Exception -> 0x0074 }
        goto L_0x0030;
    L_0x0074:
        r0 = move-exception;
        goto L_0x0030;
    L_0x0076:
        r0 = r6.getName();	 Catch:{ Exception -> 0x008b }
        r7 = "packetReplyTimeout";
        r0 = r0.equals(r7);	 Catch:{ Exception -> 0x008b }
        if (r0 == 0) goto L_0x0095;
    L_0x0082:
        r0 = packetReplyTimeout;	 Catch:{ Exception -> 0x008b }
        r0 = parseIntProperty(r6, r0);	 Catch:{ Exception -> 0x008b }
        packetReplyTimeout = r0;	 Catch:{ Exception -> 0x008b }
        goto L_0x006a;
    L_0x008b:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x00aa }
        r1.close();	 Catch:{ Exception -> 0x0093 }
        goto L_0x0030;
    L_0x0093:
        r0 = move-exception;
        goto L_0x0030;
    L_0x0095:
        r0 = r6.getName();	 Catch:{ Exception -> 0x008b }
        r7 = "keepAliveInterval";
        r0 = r0.equals(r7);	 Catch:{ Exception -> 0x008b }
        if (r0 == 0) goto L_0x00b4;
    L_0x00a1:
        r0 = keepAliveInterval;	 Catch:{ Exception -> 0x008b }
        r0 = parseIntProperty(r6, r0);	 Catch:{ Exception -> 0x008b }
        keepAliveInterval = r0;	 Catch:{ Exception -> 0x008b }
        goto L_0x006a;
    L_0x00aa:
        r0 = move-exception;
        r1.close();	 Catch:{ Exception -> 0x0140 }
    L_0x00ae:
        throw r0;	 Catch:{ Exception -> 0x00af }
    L_0x00af:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x00b3:
        return;
    L_0x00b4:
        r0 = r6.getName();	 Catch:{ Exception -> 0x008b }
        r7 = "mechName";
        r0 = r0.equals(r7);	 Catch:{ Exception -> 0x008b }
        if (r0 == 0) goto L_0x00ca;
    L_0x00c0:
        r0 = defaultMechs;	 Catch:{ Exception -> 0x008b }
        r7 = r6.nextText();	 Catch:{ Exception -> 0x008b }
        r0.add(r7);	 Catch:{ Exception -> 0x008b }
        goto L_0x006a;
    L_0x00ca:
        r0 = r6.getName();	 Catch:{ Exception -> 0x008b }
        r7 = "localSocks5ProxyEnabled";
        r0 = r0.equals(r7);	 Catch:{ Exception -> 0x008b }
        if (r0 == 0) goto L_0x00e1;
    L_0x00d6:
        r0 = r6.nextText();	 Catch:{ Exception -> 0x008b }
        r0 = java.lang.Boolean.parseBoolean(r0);	 Catch:{ Exception -> 0x008b }
        localSocks5ProxyEnabled = r0;	 Catch:{ Exception -> 0x008b }
        goto L_0x006a;
    L_0x00e1:
        r0 = r6.getName();	 Catch:{ Exception -> 0x008b }
        r7 = "localSocks5ProxyPort";
        r0 = r0.equals(r7);	 Catch:{ Exception -> 0x008b }
        if (r0 == 0) goto L_0x00f7;
    L_0x00ed:
        r0 = localSocks5ProxyPort;	 Catch:{ Exception -> 0x008b }
        r0 = parseIntProperty(r6, r0);	 Catch:{ Exception -> 0x008b }
        localSocks5ProxyPort = r0;	 Catch:{ Exception -> 0x008b }
        goto L_0x006a;
    L_0x00f7:
        r0 = r6.getName();	 Catch:{ Exception -> 0x008b }
        r7 = "packetCollectorSize";
        r0 = r0.equals(r7);	 Catch:{ Exception -> 0x008b }
        if (r0 == 0) goto L_0x010d;
    L_0x0103:
        r0 = packetCollectorSize;	 Catch:{ Exception -> 0x008b }
        r0 = parseIntProperty(r6, r0);	 Catch:{ Exception -> 0x008b }
        packetCollectorSize = r0;	 Catch:{ Exception -> 0x008b }
        goto L_0x006a;
    L_0x010d:
        r0 = r6.getName();	 Catch:{ Exception -> 0x008b }
        r7 = "defaultPingInterval";
        r0 = r0.equals(r7);	 Catch:{ Exception -> 0x008b }
        if (r0 == 0) goto L_0x0123;
    L_0x0119:
        r0 = defaultPingInterval;	 Catch:{ Exception -> 0x008b }
        r0 = parseIntProperty(r6, r0);	 Catch:{ Exception -> 0x008b }
        defaultPingInterval = r0;	 Catch:{ Exception -> 0x008b }
        goto L_0x006a;
    L_0x0123:
        r0 = r6.getName();	 Catch:{ Exception -> 0x008b }
        r7 = "autoEnableEntityCaps";
        r0 = r0.equals(r7);	 Catch:{ Exception -> 0x008b }
        if (r0 == 0) goto L_0x006a;
    L_0x012f:
        r0 = r6.nextText();	 Catch:{ Exception -> 0x008b }
        r0 = java.lang.Boolean.parseBoolean(r0);	 Catch:{ Exception -> 0x008b }
        autoEnableEntityCaps = r0;	 Catch:{ Exception -> 0x008b }
        goto L_0x006a;
    L_0x013b:
        r0 = r2 + 1;
        r2 = r0;
        goto L_0x0026;
    L_0x0140:
        r1 = move-exception;
        goto L_0x00ae;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.SmackConfiguration.<clinit>():void");
    }

    private SmackConfiguration() {
    }

    public static String getVersion() {
        return SMACK_VERSION;
    }

    public static int getPacketReplyTimeout() {
        if (packetReplyTimeout <= 0) {
            packetReplyTimeout = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
        }
        return packetReplyTimeout;
    }

    public static void setPacketReplyTimeout(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException();
        }
        packetReplyTimeout = i;
    }

    public static int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public static void setKeepAliveInterval(int i) {
        keepAliveInterval = i;
    }

    public static int getPacketCollectorSize() {
        return packetCollectorSize;
    }

    public static void setPacketCollectorSize(int i) {
        packetCollectorSize = i;
    }

    public static void addSaslMech(String str) {
        if (!defaultMechs.contains(str)) {
            defaultMechs.add(str);
        }
    }

    public static void addSaslMechs(Collection<String> collection) {
        for (String addSaslMech : collection) {
            addSaslMech(addSaslMech);
        }
    }

    public static void removeSaslMech(String str) {
        if (defaultMechs.contains(str)) {
            defaultMechs.remove(str);
        }
    }

    public static void removeSaslMechs(Collection<String> collection) {
        for (String removeSaslMech : collection) {
            removeSaslMech(removeSaslMech);
        }
    }

    public static List<String> getSaslMechs() {
        return defaultMechs;
    }

    public static boolean isLocalSocks5ProxyEnabled() {
        return localSocks5ProxyEnabled;
    }

    public static void setLocalSocks5ProxyEnabled(boolean z) {
        localSocks5ProxyEnabled = z;
    }

    public static int getLocalSocks5ProxyPort() {
        return localSocks5ProxyPort;
    }

    public static void setLocalSocks5ProxyPort(int i) {
        localSocks5ProxyPort = i;
    }

    public static int getDefaultPingInterval() {
        return defaultPingInterval;
    }

    public static void setDefaultPingInterval(int i) {
        defaultPingInterval = i;
    }

    public static boolean autoEnableEntityCaps() {
        return autoEnableEntityCaps;
    }

    public static void setAutoEnableEntityCaps(boolean z) {
        autoEnableEntityCaps = z;
    }

    private static void parseClassToLoad(XmlPullParser xmlPullParser) throws Exception {
        String nextText = xmlPullParser.nextText();
        try {
            Class.forName(nextText);
        } catch (ClassNotFoundException e) {
            System.err.println("Error! A startup class specified in smack-config.xml could not be loaded: " + nextText);
        }
    }

    private static int parseIntProperty(XmlPullParser xmlPullParser, int i) throws Exception {
        try {
            i = Integer.parseInt(xmlPullParser.nextText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    private static ClassLoader[] getClassLoaders() {
        int i = 0;
        ClassLoader[] classLoaderArr = new ClassLoader[]{SmackConfiguration.class.getClassLoader(), Thread.currentThread().getContextClassLoader()};
        List arrayList = new ArrayList();
        int length = classLoaderArr.length;
        while (i < length) {
            Object obj = classLoaderArr[i];
            if (obj != null) {
                arrayList.add(obj);
            }
            i++;
        }
        return (ClassLoader[]) arrayList.toArray(new ClassLoader[arrayList.size()]);
    }
}
