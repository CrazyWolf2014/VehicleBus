package org.jivesoftware.smack.packet;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jivesoftware.smack.util.StringUtils;

public abstract class Packet {
    protected static final String DEFAULT_LANGUAGE;
    private static String DEFAULT_XML_NS = null;
    public static final String ID_NOT_AVAILABLE = "ID_NOT_AVAILABLE";
    public static final DateFormat XEP_0082_UTC_FORMAT;
    private static long id;
    private static String prefix;
    private XMPPError error;
    private String from;
    private final List<PacketExtension> packetExtensions;
    private String packetID;
    private final Map<String, Object> properties;
    private String to;
    private String xmlns;

    public abstract String toXML();

    static {
        DEFAULT_LANGUAGE = Locale.getDefault().getLanguage().toLowerCase();
        DEFAULT_XML_NS = null;
        XEP_0082_UTC_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        XEP_0082_UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        prefix = StringUtils.randomString(5) + "-";
        id = 0;
    }

    public static synchronized String nextID() {
        String stringBuilder;
        synchronized (Packet.class) {
            StringBuilder append = new StringBuilder().append(prefix);
            long j = id;
            id = 1 + j;
            stringBuilder = append.append(Long.toString(j)).toString();
        }
        return stringBuilder;
    }

    public static void setDefaultXmlns(String str) {
        DEFAULT_XML_NS = str;
    }

    public Packet() {
        this.xmlns = DEFAULT_XML_NS;
        this.packetID = null;
        this.to = null;
        this.from = null;
        this.packetExtensions = new CopyOnWriteArrayList();
        this.properties = new HashMap();
        this.error = null;
    }

    public Packet(Packet packet) {
        this.xmlns = DEFAULT_XML_NS;
        this.packetID = null;
        this.to = null;
        this.from = null;
        this.packetExtensions = new CopyOnWriteArrayList();
        this.properties = new HashMap();
        this.error = null;
        this.packetID = packet.getPacketID();
        this.to = packet.getTo();
        this.from = packet.getFrom();
        this.xmlns = packet.xmlns;
        this.error = packet.error;
        for (PacketExtension addExtension : packet.getExtensions()) {
            addExtension(addExtension);
        }
    }

    public String getPacketID() {
        if (ID_NOT_AVAILABLE.equals(this.packetID)) {
            return null;
        }
        if (this.packetID == null) {
            this.packetID = nextID();
        }
        return this.packetID;
    }

    public void setPacketID(String str) {
        this.packetID = str;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String str) {
        this.to = str;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String str) {
        this.from = str;
    }

    public XMPPError getError() {
        return this.error;
    }

    public void setError(XMPPError xMPPError) {
        this.error = xMPPError;
    }

    public synchronized Collection<PacketExtension> getExtensions() {
        Collection<PacketExtension> emptyList;
        if (this.packetExtensions == null) {
            emptyList = Collections.emptyList();
        } else {
            emptyList = Collections.unmodifiableList(new ArrayList(this.packetExtensions));
        }
        return emptyList;
    }

    public PacketExtension getExtension(String str) {
        return getExtension(null, str);
    }

    public PacketExtension getExtension(String str, String str2) {
        if (str2 == null) {
            return null;
        }
        for (PacketExtension packetExtension : this.packetExtensions) {
            if ((str == null || str.equals(packetExtension.getElementName())) && str2.equals(packetExtension.getNamespace())) {
                return packetExtension;
            }
        }
        return null;
    }

    public void addExtension(PacketExtension packetExtension) {
        if (packetExtension != null) {
            this.packetExtensions.add(packetExtension);
        }
    }

    public void addExtensions(Collection<PacketExtension> collection) {
        if (collection != null) {
            this.packetExtensions.addAll(collection);
        }
    }

    public void removeExtension(PacketExtension packetExtension) {
        this.packetExtensions.remove(packetExtension);
    }

    public synchronized Object getProperty(String str) {
        Object obj;
        if (this.properties == null) {
            obj = null;
        } else {
            obj = this.properties.get(str);
        }
        return obj;
    }

    public synchronized void setProperty(String str, Object obj) {
        if (obj instanceof Serializable) {
            this.properties.put(str, obj);
        } else {
            throw new IllegalArgumentException("Value must be serialiazble");
        }
    }

    public synchronized void deleteProperty(String str) {
        if (this.properties != null) {
            this.properties.remove(str);
        }
    }

    public synchronized Collection<String> getPropertyNames() {
        Collection<String> emptySet;
        if (this.properties == null) {
            emptySet = Collections.emptySet();
        } else {
            emptySet = Collections.unmodifiableSet(new HashSet(this.properties.keySet()));
        }
        return emptySet;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected synchronized java.lang.String getExtensionsXML() {
        /*
        r8 = this;
        r4 = 0;
        monitor-enter(r8);
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0023 }
        r6.<init>();	 Catch:{ all -> 0x0023 }
        r1 = r8.getExtensions();	 Catch:{ all -> 0x0023 }
        r2 = r1.iterator();	 Catch:{ all -> 0x0023 }
    L_0x000f:
        r1 = r2.hasNext();	 Catch:{ all -> 0x0023 }
        if (r1 == 0) goto L_0x0026;
    L_0x0015:
        r1 = r2.next();	 Catch:{ all -> 0x0023 }
        r1 = (org.jivesoftware.smack.packet.PacketExtension) r1;	 Catch:{ all -> 0x0023 }
        r1 = r1.toXML();	 Catch:{ all -> 0x0023 }
        r6.append(r1);	 Catch:{ all -> 0x0023 }
        goto L_0x000f;
    L_0x0023:
        r1 = move-exception;
        monitor-exit(r8);
        throw r1;
    L_0x0026:
        r1 = r8.properties;	 Catch:{ all -> 0x0023 }
        if (r1 == 0) goto L_0x0149;
    L_0x002a:
        r1 = r8.properties;	 Catch:{ all -> 0x0023 }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x0023 }
        if (r1 != 0) goto L_0x0149;
    L_0x0032:
        r1 = "<properties xmlns=\"http://www.jivesoftware.com/xmlns/xmpp/properties\">";
        r6.append(r1);	 Catch:{ all -> 0x0023 }
        r1 = r8.getPropertyNames();	 Catch:{ all -> 0x0023 }
        r7 = r1.iterator();	 Catch:{ all -> 0x0023 }
    L_0x003f:
        r1 = r7.hasNext();	 Catch:{ all -> 0x0023 }
        if (r1 == 0) goto L_0x0144;
    L_0x0045:
        r1 = r7.next();	 Catch:{ all -> 0x0023 }
        r1 = (java.lang.String) r1;	 Catch:{ all -> 0x0023 }
        r2 = r8.getProperty(r1);	 Catch:{ all -> 0x0023 }
        r3 = "<property>";
        r6.append(r3);	 Catch:{ all -> 0x0023 }
        r3 = "<name>";
        r3 = r6.append(r3);	 Catch:{ all -> 0x0023 }
        r1 = org.jivesoftware.smack.util.StringUtils.escapeForXML(r1);	 Catch:{ all -> 0x0023 }
        r1 = r3.append(r1);	 Catch:{ all -> 0x0023 }
        r3 = "</name>";
        r1.append(r3);	 Catch:{ all -> 0x0023 }
        r1 = "<value type=\"";
        r6.append(r1);	 Catch:{ all -> 0x0023 }
        r1 = r2 instanceof java.lang.Integer;	 Catch:{ all -> 0x0023 }
        if (r1 == 0) goto L_0x0085;
    L_0x0070:
        r1 = "integer\">";
        r1 = r6.append(r1);	 Catch:{ all -> 0x0023 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0023 }
        r2 = "</value>";
        r1.append(r2);	 Catch:{ all -> 0x0023 }
    L_0x007f:
        r1 = "</property>";
        r6.append(r1);	 Catch:{ all -> 0x0023 }
        goto L_0x003f;
    L_0x0085:
        r1 = r2 instanceof java.lang.Long;	 Catch:{ all -> 0x0023 }
        if (r1 == 0) goto L_0x0099;
    L_0x0089:
        r1 = "long\">";
        r1 = r6.append(r1);	 Catch:{ all -> 0x0023 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0023 }
        r2 = "</value>";
        r1.append(r2);	 Catch:{ all -> 0x0023 }
        goto L_0x007f;
    L_0x0099:
        r1 = r2 instanceof java.lang.Float;	 Catch:{ all -> 0x0023 }
        if (r1 == 0) goto L_0x00ad;
    L_0x009d:
        r1 = "float\">";
        r1 = r6.append(r1);	 Catch:{ all -> 0x0023 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0023 }
        r2 = "</value>";
        r1.append(r2);	 Catch:{ all -> 0x0023 }
        goto L_0x007f;
    L_0x00ad:
        r1 = r2 instanceof java.lang.Double;	 Catch:{ all -> 0x0023 }
        if (r1 == 0) goto L_0x00c1;
    L_0x00b1:
        r1 = "double\">";
        r1 = r6.append(r1);	 Catch:{ all -> 0x0023 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0023 }
        r2 = "</value>";
        r1.append(r2);	 Catch:{ all -> 0x0023 }
        goto L_0x007f;
    L_0x00c1:
        r1 = r2 instanceof java.lang.Boolean;	 Catch:{ all -> 0x0023 }
        if (r1 == 0) goto L_0x00d5;
    L_0x00c5:
        r1 = "boolean\">";
        r1 = r6.append(r1);	 Catch:{ all -> 0x0023 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0023 }
        r2 = "</value>";
        r1.append(r2);	 Catch:{ all -> 0x0023 }
        goto L_0x007f;
    L_0x00d5:
        r1 = r2 instanceof java.lang.String;	 Catch:{ all -> 0x0023 }
        if (r1 == 0) goto L_0x00ef;
    L_0x00d9:
        r1 = "string\">";
        r6.append(r1);	 Catch:{ all -> 0x0023 }
        r0 = r2;
        r0 = (java.lang.String) r0;	 Catch:{ all -> 0x0023 }
        r1 = r0;
        r1 = org.jivesoftware.smack.util.StringUtils.escapeForXML(r1);	 Catch:{ all -> 0x0023 }
        r6.append(r1);	 Catch:{ all -> 0x0023 }
        r1 = "</value>";
        r6.append(r1);	 Catch:{ all -> 0x0023 }
        goto L_0x007f;
    L_0x00ef:
        r5 = new java.io.ByteArrayOutputStream;	 Catch:{ Exception -> 0x0121, all -> 0x0136 }
        r5.<init>();	 Catch:{ Exception -> 0x0121, all -> 0x0136 }
        r3 = new java.io.ObjectOutputStream;	 Catch:{ Exception -> 0x0160, all -> 0x0157 }
        r3.<init>(r5);	 Catch:{ Exception -> 0x0160, all -> 0x0157 }
        r3.writeObject(r2);	 Catch:{ Exception -> 0x0164, all -> 0x015a }
        r1 = "java-object\">";
        r6.append(r1);	 Catch:{ Exception -> 0x0164, all -> 0x015a }
        r1 = r5.toByteArray();	 Catch:{ Exception -> 0x0164, all -> 0x015a }
        r1 = org.jivesoftware.smack.util.StringUtils.encodeBase64(r1);	 Catch:{ Exception -> 0x0164, all -> 0x015a }
        r1 = r6.append(r1);	 Catch:{ Exception -> 0x0164, all -> 0x015a }
        r2 = "</value>";
        r1.append(r2);	 Catch:{ Exception -> 0x0164, all -> 0x015a }
        if (r3 == 0) goto L_0x0117;
    L_0x0114:
        r3.close();	 Catch:{ Exception -> 0x014f }
    L_0x0117:
        if (r5 == 0) goto L_0x007f;
    L_0x0119:
        r5.close();	 Catch:{ Exception -> 0x011e }
        goto L_0x007f;
    L_0x011e:
        r1 = move-exception;
        goto L_0x007f;
    L_0x0121:
        r1 = move-exception;
        r2 = r4;
        r3 = r4;
    L_0x0124:
        r1.printStackTrace();	 Catch:{ all -> 0x015c }
        if (r2 == 0) goto L_0x012c;
    L_0x0129:
        r2.close();	 Catch:{ Exception -> 0x0151 }
    L_0x012c:
        if (r3 == 0) goto L_0x007f;
    L_0x012e:
        r3.close();	 Catch:{ Exception -> 0x0133 }
        goto L_0x007f;
    L_0x0133:
        r1 = move-exception;
        goto L_0x007f;
    L_0x0136:
        r1 = move-exception;
        r3 = r4;
        r5 = r4;
    L_0x0139:
        if (r3 == 0) goto L_0x013e;
    L_0x013b:
        r3.close();	 Catch:{ Exception -> 0x0153 }
    L_0x013e:
        if (r5 == 0) goto L_0x0143;
    L_0x0140:
        r5.close();	 Catch:{ Exception -> 0x0155 }
    L_0x0143:
        throw r1;	 Catch:{ all -> 0x0023 }
    L_0x0144:
        r1 = "</properties>";
        r6.append(r1);	 Catch:{ all -> 0x0023 }
    L_0x0149:
        r1 = r6.toString();	 Catch:{ all -> 0x0023 }
        monitor-exit(r8);
        return r1;
    L_0x014f:
        r1 = move-exception;
        goto L_0x0117;
    L_0x0151:
        r1 = move-exception;
        goto L_0x012c;
    L_0x0153:
        r2 = move-exception;
        goto L_0x013e;
    L_0x0155:
        r2 = move-exception;
        goto L_0x0143;
    L_0x0157:
        r1 = move-exception;
        r3 = r4;
        goto L_0x0139;
    L_0x015a:
        r1 = move-exception;
        goto L_0x0139;
    L_0x015c:
        r1 = move-exception;
        r5 = r3;
        r3 = r2;
        goto L_0x0139;
    L_0x0160:
        r1 = move-exception;
        r2 = r4;
        r3 = r5;
        goto L_0x0124;
    L_0x0164:
        r1 = move-exception;
        r2 = r3;
        r3 = r5;
        goto L_0x0124;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.packet.Packet.getExtensionsXML():java.lang.String");
    }

    public String getXmlns() {
        return this.xmlns;
    }

    public static String getDefaultLanguage() {
        return DEFAULT_LANGUAGE;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
        r4 = this;
        r0 = 1;
        r1 = 0;
        if (r4 != r5) goto L_0x0006;
    L_0x0004:
        r1 = r0;
    L_0x0005:
        return r1;
    L_0x0006:
        if (r5 == 0) goto L_0x0005;
    L_0x0008:
        r2 = r4.getClass();
        r3 = r5.getClass();
        if (r2 != r3) goto L_0x0005;
    L_0x0012:
        r5 = (org.jivesoftware.smack.packet.Packet) r5;
        r2 = r4.error;
        if (r2 == 0) goto L_0x0075;
    L_0x0018:
        r2 = r4.error;
        r3 = r5.error;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x0022:
        r2 = r4.from;
        if (r2 == 0) goto L_0x007a;
    L_0x0026:
        r2 = r4.from;
        r3 = r5.from;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x0030:
        r2 = r4.packetExtensions;
        r3 = r5.packetExtensions;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x003a:
        r2 = r4.packetID;
        if (r2 == 0) goto L_0x007f;
    L_0x003e:
        r2 = r4.packetID;
        r3 = r5.packetID;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x0048:
        r2 = r4.properties;
        if (r2 == 0) goto L_0x0084;
    L_0x004c:
        r2 = r4.properties;
        r3 = r5.properties;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x0056:
        r2 = r4.to;
        if (r2 == 0) goto L_0x008a;
    L_0x005a:
        r2 = r4.to;
        r3 = r5.to;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x0064:
        r2 = r4.xmlns;
        if (r2 == 0) goto L_0x0090;
    L_0x0068:
        r2 = r4.xmlns;
        r3 = r5.xmlns;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x0073;
    L_0x0072:
        r0 = r1;
    L_0x0073:
        r1 = r0;
        goto L_0x0005;
    L_0x0075:
        r2 = r5.error;
        if (r2 == 0) goto L_0x0022;
    L_0x0079:
        goto L_0x0005;
    L_0x007a:
        r2 = r5.from;
        if (r2 == 0) goto L_0x0030;
    L_0x007e:
        goto L_0x0005;
    L_0x007f:
        r2 = r5.packetID;
        if (r2 == 0) goto L_0x0048;
    L_0x0083:
        goto L_0x0005;
    L_0x0084:
        r2 = r5.properties;
        if (r2 == 0) goto L_0x0056;
    L_0x0088:
        goto L_0x0005;
    L_0x008a:
        r2 = r5.to;
        if (r2 == 0) goto L_0x0064;
    L_0x008e:
        goto L_0x0005;
    L_0x0090:
        r2 = r5.xmlns;
        if (r2 != 0) goto L_0x0072;
    L_0x0094:
        goto L_0x0073;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.packet.Packet.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        int hashCode2 = (this.xmlns != null ? this.xmlns.hashCode() : 0) * 31;
        if (this.packetID != null) {
            hashCode = this.packetID.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode + hashCode2) * 31;
        if (this.to != null) {
            hashCode = this.to.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode + hashCode2) * 31;
        if (this.from != null) {
            hashCode = this.from.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (((((hashCode + hashCode2) * 31) + this.packetExtensions.hashCode()) * 31) + this.properties.hashCode()) * 31;
        if (this.error != null) {
            i = this.error.hashCode();
        }
        return hashCode + i;
    }
}
