package org.jivesoftware.smack.util;

import java.util.Map;

public class DNSUtil {
    private static Map<String, HostAddress> ccache;
    private static Map<String, HostAddress> scache;

    public static class HostAddress {
        private String host;
        private int port;

        private HostAddress(String str, int i) {
            this.host = str;
            this.port = i;
        }

        public String getHost() {
            return this.host;
        }

        public int getPort() {
            return this.port;
        }

        public String toString() {
            return this.host + ":" + this.port;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof HostAddress)) {
                return false;
            }
            HostAddress hostAddress = (HostAddress) obj;
            if (!this.host.equals(hostAddress.host)) {
                return false;
            }
            if (this.port != hostAddress.port) {
                return false;
            }
            return true;
        }
    }

    static {
        ccache = new Cache(100, 600000);
        scache = new Cache(100, 600000);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static org.jivesoftware.smack.util.DNSUtil.HostAddress resolveSRV(java.lang.String r14) {
        /*
        r5 = 0;
        r4 = -1;
        r3 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r2 = 0;
        r0 = new org.xbill.DNS.Lookup;	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r1 = 33;
        r0.<init>(r14, r1);	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r7 = r0.run();	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        if (r7 != 0) goto L_0x0015;
    L_0x0013:
        r0 = 0;
    L_0x0014:
        return r0;
    L_0x0015:
        r8 = r7.length;	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r0 = 0;
        r6 = r0;
    L_0x0018:
        if (r6 >= r8) goto L_0x0075;
    L_0x001a:
        r0 = r7[r6];	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r0 = (org.xbill.DNS.SRVRecord) r0;	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        if (r0 == 0) goto L_0x00a2;
    L_0x0020:
        r1 = r0.getTarget();	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        if (r1 == 0) goto L_0x00a2;
    L_0x0026:
        r1 = r0.getWeight();	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r9 = r0.getWeight();	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r1 = r1 * r9;
        r9 = (double) r1;	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r11 = java.lang.Math.random();	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r9 = r9 * r11;
        r1 = (int) r9;	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r9 = r0.getPriority();	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        if (r9 >= r3) goto L_0x0058;
    L_0x003c:
        r2 = r0.getPriority();	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r3 = r0.getTarget();	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r3 = r3.toString();	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r0 = r0.getPort();	 Catch:{ TextParseException -> 0x009f, NullPointerException -> 0x009c }
        r13 = r1;
        r1 = r2;
        r2 = r0;
        r0 = r13;
    L_0x0050:
        r4 = r6 + 1;
        r6 = r4;
        r5 = r3;
        r3 = r1;
        r4 = r2;
        r2 = r0;
        goto L_0x0018;
    L_0x0058:
        r9 = r0.getPriority();	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        if (r9 != r3) goto L_0x00a2;
    L_0x005e:
        if (r1 <= r2) goto L_0x00a2;
    L_0x0060:
        r2 = r0.getPriority();	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r3 = r0.getTarget();	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r3 = r3.toString();	 Catch:{ TextParseException -> 0x007a, NullPointerException -> 0x007d }
        r0 = r0.getPort();	 Catch:{ TextParseException -> 0x009f, NullPointerException -> 0x009c }
        r13 = r1;
        r1 = r2;
        r2 = r0;
        r0 = r13;
        goto L_0x0050;
    L_0x0075:
        r0 = r5;
    L_0x0076:
        if (r0 != 0) goto L_0x0080;
    L_0x0078:
        r0 = 0;
        goto L_0x0014;
    L_0x007a:
        r0 = move-exception;
    L_0x007b:
        r0 = r5;
        goto L_0x0076;
    L_0x007d:
        r0 = move-exception;
    L_0x007e:
        r0 = r5;
        goto L_0x0076;
    L_0x0080:
        r1 = ".";
        r1 = r0.endsWith(r1);
        if (r1 == 0) goto L_0x0093;
    L_0x0088:
        r1 = 0;
        r2 = r0.length();
        r2 = r2 + -1;
        r0 = r0.substring(r1, r2);
    L_0x0093:
        r1 = new org.jivesoftware.smack.util.DNSUtil$HostAddress;
        r2 = 0;
        r1.<init>(r4, r2);
        r0 = r1;
        goto L_0x0014;
    L_0x009c:
        r0 = move-exception;
        r5 = r3;
        goto L_0x007e;
    L_0x009f:
        r0 = move-exception;
        r5 = r3;
        goto L_0x007b;
    L_0x00a2:
        r0 = r2;
        r1 = r3;
        r2 = r4;
        r3 = r5;
        goto L_0x0050;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.util.DNSUtil.resolveSRV(java.lang.String):org.jivesoftware.smack.util.DNSUtil$HostAddress");
    }

    public static HostAddress resolveXMPPDomain(String str) {
        HostAddress hostAddress;
        synchronized (ccache) {
            if (ccache.containsKey(str)) {
                hostAddress = (HostAddress) ccache.get(str);
                if (hostAddress != null) {
                }
            }
            hostAddress = resolveSRV("_xmpp-client._tcp." + str);
            if (hostAddress == null) {
                hostAddress = resolveSRV("_jabber._tcp." + str);
            }
            if (hostAddress == null) {
                hostAddress = new HostAddress(5222, null);
            }
            synchronized (ccache) {
                ccache.put(str, hostAddress);
            }
        }
        return hostAddress;
    }

    public static HostAddress resolveXMPPServerDomain(String str) {
        HostAddress hostAddress;
        synchronized (scache) {
            if (scache.containsKey(str)) {
                hostAddress = (HostAddress) scache.get(str);
                if (hostAddress != null) {
                }
            }
            hostAddress = resolveSRV("_xmpp-server._tcp." + str);
            if (hostAddress == null) {
                hostAddress = resolveSRV("_jabber._tcp." + str);
            }
            if (hostAddress == null) {
                hostAddress = new HostAddress(5269, null);
            }
            synchronized (scache) {
                scache.put(str, hostAddress);
            }
        }
        return hostAddress;
    }
}
