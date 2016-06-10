package org.xbill.DNS;

import com.ifoer.mine.Contact;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;

public class SimpleResolver implements Resolver {
    public static final int DEFAULT_EDNS_PAYLOADSIZE = 1280;
    public static final int DEFAULT_PORT = 53;
    private static final short DEFAULT_UDPSIZE = (short) 512;
    private static String defaultResolver;
    private static int uniqueID;
    private InetSocketAddress address;
    private boolean ignoreTruncation;
    private InetSocketAddress localAddress;
    private OPTRecord queryOPT;
    private long timeoutValue;
    private TSIG tsig;
    private boolean useTCP;

    static {
        defaultResolver = "localhost";
        uniqueID = 0;
    }

    public SimpleResolver(String str) throws UnknownHostException {
        InetAddress localHost;
        this.timeoutValue = 10000;
        if (str == null) {
            str = ResolverConfig.getCurrentConfig().server();
            if (str == null) {
                str = defaultResolver;
            }
        }
        if (str.equals(Contact.RELATION_ASK)) {
            localHost = InetAddress.getLocalHost();
        } else {
            localHost = InetAddress.getByName(str);
        }
        this.address = new InetSocketAddress(localHost, DEFAULT_PORT);
    }

    public SimpleResolver() throws UnknownHostException {
        this(null);
    }

    InetSocketAddress getAddress() {
        return this.address;
    }

    public static void setDefaultResolver(String str) {
        defaultResolver = str;
    }

    public void setPort(int i) {
        this.address = new InetSocketAddress(this.address.getAddress(), i);
    }

    public void setAddress(InetSocketAddress inetSocketAddress) {
        this.address = inetSocketAddress;
    }

    public void setAddress(InetAddress inetAddress) {
        this.address = new InetSocketAddress(inetAddress, this.address.getPort());
    }

    public void setLocalAddress(InetSocketAddress inetSocketAddress) {
        this.localAddress = inetSocketAddress;
    }

    public void setLocalAddress(InetAddress inetAddress) {
        this.localAddress = new InetSocketAddress(inetAddress, 0);
    }

    public void setTCP(boolean z) {
        this.useTCP = z;
    }

    public void setIgnoreTruncation(boolean z) {
        this.ignoreTruncation = z;
    }

    public void setEDNS(int i, int i2, int i3, List list) {
        if (i == 0 || i == -1) {
            int i4;
            if (i2 == 0) {
                i4 = DEFAULT_EDNS_PAYLOADSIZE;
            } else {
                i4 = i2;
            }
            this.queryOPT = new OPTRecord(i4, 0, i, i3, list);
            return;
        }
        throw new IllegalArgumentException("invalid EDNS level - must be 0 or -1");
    }

    public void setEDNS(int i) {
        setEDNS(i, 0, 0, null);
    }

    public void setTSIGKey(TSIG tsig) {
        this.tsig = tsig;
    }

    TSIG getTSIGKey() {
        return this.tsig;
    }

    public void setTimeout(int i, int i2) {
        this.timeoutValue = (((long) i) * 1000) + ((long) i2);
    }

    public void setTimeout(int i) {
        setTimeout(i, 0);
    }

    long getTimeout() {
        return this.timeoutValue;
    }

    private Message parseMessage(byte[] bArr) throws WireParseException {
        try {
            return new Message(bArr);
        } catch (IOException e) {
            IOException e2 = e;
            if (Options.check("verbose")) {
                e2.printStackTrace();
            }
            if (!(e2 instanceof WireParseException)) {
                e2 = new WireParseException("Error parsing message");
            }
            throw ((WireParseException) e2);
        }
    }

    private void verifyTSIG(Message message, Message message2, byte[] bArr, TSIG tsig) {
        if (tsig != null) {
            int verify = tsig.verify(message2, bArr, message.getTSIG());
            if (Options.check("verbose")) {
                System.err.println("TSIG verify: " + Rcode.TSIGstring(verify));
            }
        }
    }

    private void applyEDNS(Message message) {
        if (this.queryOPT != null && message.getOPT() == null) {
            message.addRecord(this.queryOPT, 3);
        }
    }

    private int maxUDPSize(Message message) {
        OPTRecord opt = message.getOPT();
        if (opt == null) {
            return KEYRecord.OWNER_HOST;
        }
        return opt.getPayloadSize();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.xbill.DNS.Message send(org.xbill.DNS.Message r13) throws java.io.IOException {
        /*
        r12 = this;
        r7 = 0;
        r9 = 1;
        r0 = "verbose";
        r0 = org.xbill.DNS.Options.check(r0);
        if (r0 == 0) goto L_0x003c;
    L_0x000a:
        r0 = java.lang.System.err;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Sending to ";
        r1 = r1.append(r2);
        r2 = r12.address;
        r2 = r2.getAddress();
        r2 = r2.getHostAddress();
        r1 = r1.append(r2);
        r2 = ":";
        r1 = r1.append(r2);
        r2 = r12.address;
        r2 = r2.getPort();
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.println(r1);
    L_0x003c:
        r0 = r13.getHeader();
        r0 = r0.getOpcode();
        if (r0 != 0) goto L_0x0059;
    L_0x0046:
        r0 = r13.getQuestion();
        if (r0 == 0) goto L_0x0059;
    L_0x004c:
        r0 = r0.getType();
        r1 = 252; // 0xfc float:3.53E-43 double:1.245E-321;
        if (r0 != r1) goto L_0x0059;
    L_0x0054:
        r0 = r12.sendAXFR(r13);
    L_0x0058:
        return r0;
    L_0x0059:
        r0 = r13.clone();
        r6 = r0;
        r6 = (org.xbill.DNS.Message) r6;
        r12.applyEDNS(r6);
        r0 = r12.tsig;
        if (r0 == 0) goto L_0x006d;
    L_0x0067:
        r0 = r12.tsig;
        r1 = 0;
        r0.apply(r6, r1);
    L_0x006d:
        r0 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r2 = r6.toWire(r0);
        r3 = r12.maxUDPSize(r6);
        r0 = java.lang.System.currentTimeMillis();
        r4 = r12.timeoutValue;
        r4 = r4 + r0;
        r0 = r7;
    L_0x0080:
        r1 = r12.useTCP;
        if (r1 != 0) goto L_0x0087;
    L_0x0084:
        r1 = r2.length;
        if (r1 <= r3) goto L_0x0114;
    L_0x0087:
        r8 = r9;
    L_0x0088:
        if (r8 == 0) goto L_0x009f;
    L_0x008a:
        r0 = r12.localAddress;
        r1 = r12.address;
        r0 = org.xbill.DNS.TCPClient.sendrecv(r0, r1, r2, r4);
    L_0x0092:
        r1 = r0.length;
        r10 = 12;
        if (r1 >= r10) goto L_0x00a8;
    L_0x0097:
        r0 = new org.xbill.DNS.WireParseException;
        r1 = "invalid DNS header - too short";
        r0.<init>(r1);
        throw r0;
    L_0x009f:
        r0 = r12.localAddress;
        r1 = r12.address;
        r0 = org.xbill.DNS.UDPClient.sendrecv(r0, r1, r2, r3, r4);
        goto L_0x0092;
    L_0x00a8:
        r1 = r0[r7];
        r1 = r1 & 255;
        r1 = r1 << 8;
        r10 = r0[r9];
        r10 = r10 & 255;
        r1 = r1 + r10;
        r10 = r6.getHeader();
        r10 = r10.getID();
        if (r1 == r10) goto L_0x00f1;
    L_0x00bd:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r11 = "invalid message id: expected ";
        r0 = r0.append(r11);
        r0 = r0.append(r10);
        r10 = "; got id ";
        r0 = r0.append(r10);
        r0 = r0.append(r1);
        r0 = r0.toString();
        if (r8 == 0) goto L_0x00e2;
    L_0x00dc:
        r1 = new org.xbill.DNS.WireParseException;
        r1.<init>(r0);
        throw r1;
    L_0x00e2:
        r1 = "verbose";
        r1 = org.xbill.DNS.Options.check(r1);
        if (r1 == 0) goto L_0x0111;
    L_0x00ea:
        r1 = java.lang.System.err;
        r1.println(r0);
        r0 = r8;
        goto L_0x0080;
    L_0x00f1:
        r1 = r12.parseMessage(r0);
        r10 = r12.tsig;
        r12.verifyTSIG(r6, r1, r0, r10);
        if (r8 != 0) goto L_0x010e;
    L_0x00fc:
        r0 = r12.ignoreTruncation;
        if (r0 != 0) goto L_0x010e;
    L_0x0100:
        r0 = r1.getHeader();
        r8 = 6;
        r0 = r0.getFlag(r8);
        if (r0 == 0) goto L_0x010e;
    L_0x010b:
        r0 = r9;
        goto L_0x0080;
    L_0x010e:
        r0 = r1;
        goto L_0x0058;
    L_0x0111:
        r0 = r8;
        goto L_0x0080;
    L_0x0114:
        r8 = r0;
        goto L_0x0088;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.SimpleResolver.send(org.xbill.DNS.Message):org.xbill.DNS.Message");
    }

    public Object sendAsync(Message message, ResolverListener resolverListener) {
        Integer num;
        String name;
        synchronized (this) {
            int i = uniqueID;
            uniqueID = i + 1;
            num = new Integer(i);
        }
        Record question = message.getQuestion();
        if (question != null) {
            name = question.getName().toString();
        } else {
            name = "(none)";
        }
        name = getClass() + ": " + name;
        Thread resolveThread = new ResolveThread(this, message, num, resolverListener);
        resolveThread.setName(name);
        resolveThread.setDaemon(true);
        resolveThread.start();
        return num;
    }

    private Message sendAXFR(Message message) throws IOException {
        ZoneTransferIn newAXFR = ZoneTransferIn.newAXFR(message.getQuestion().getName(), this.address, this.tsig);
        newAXFR.setTimeout((int) (getTimeout() / 1000));
        newAXFR.setLocalAddress(this.localAddress);
        try {
            newAXFR.run();
            List<Record> axfr = newAXFR.getAXFR();
            Message message2 = new Message(message.getHeader().getID());
            message2.getHeader().setFlag(5);
            message2.getHeader().setFlag(0);
            message2.addRecord(message.getQuestion(), 0);
            for (Record addRecord : axfr) {
                message2.addRecord(addRecord, 1);
            }
            return message2;
        } catch (ZoneTransferException e) {
            throw new WireParseException(e.getMessage());
        }
    }
}
