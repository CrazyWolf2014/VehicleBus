package org.xbill.DNS;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.TSIG.StreamVerifier;

public class ZoneTransferIn {
    private static final int AXFR = 6;
    private static final int END = 7;
    private static final int FIRSTDATA = 1;
    private static final int INITIALSOA = 0;
    private static final int IXFR_ADD = 5;
    private static final int IXFR_ADDSOA = 4;
    private static final int IXFR_DEL = 3;
    private static final int IXFR_DELSOA = 2;
    private SocketAddress address;
    private TCPClient client;
    private long current_serial;
    private int dclass;
    private long end_serial;
    private ZoneTransferHandler handler;
    private Record initialsoa;
    private long ixfr_serial;
    private SocketAddress localAddress;
    private int qtype;
    private int rtype;
    private int state;
    private long timeout;
    private TSIG tsig;
    private StreamVerifier verifier;
    private boolean want_fallback;
    private Name zname;

    public static class Delta {
        public List adds;
        public List deletes;
        public long end;
        public long start;

        private Delta() {
            this.adds = new ArrayList();
            this.deletes = new ArrayList();
        }
    }

    public interface ZoneTransferHandler {
        void handleRecord(Record record) throws ZoneTransferException;

        void startAXFR() throws ZoneTransferException;

        void startIXFR() throws ZoneTransferException;

        void startIXFRAdds(Record record) throws ZoneTransferException;

        void startIXFRDeletes(Record record) throws ZoneTransferException;
    }

    private static class BasicHandler implements ZoneTransferHandler {
        private List axfr;
        private List ixfr;

        private BasicHandler() {
        }

        public void startAXFR() {
            this.axfr = new ArrayList();
        }

        public void startIXFR() {
            this.ixfr = new ArrayList();
        }

        public void startIXFRDeletes(Record record) {
            Delta delta = new Delta();
            delta.deletes.add(record);
            delta.start = ZoneTransferIn.getSOASerial(record);
            this.ixfr.add(delta);
        }

        public void startIXFRAdds(Record record) {
            Delta delta = (Delta) this.ixfr.get(this.ixfr.size() - 1);
            delta.adds.add(record);
            delta.end = ZoneTransferIn.getSOASerial(record);
        }

        public void handleRecord(Record record) {
            List list;
            if (this.ixfr != null) {
                Delta delta = (Delta) this.ixfr.get(this.ixfr.size() - 1);
                if (delta.adds.size() > 0) {
                    list = delta.adds;
                } else {
                    list = delta.deletes;
                }
            } else {
                list = this.axfr;
            }
            list.add(record);
        }
    }

    private ZoneTransferIn() {
        this.timeout = 900000;
    }

    private ZoneTransferIn(Name name, int i, long j, boolean z, SocketAddress socketAddress, TSIG tsig) {
        this.timeout = 900000;
        this.address = socketAddress;
        this.tsig = tsig;
        if (name.isAbsolute()) {
            this.zname = name;
        } else {
            try {
                this.zname = Name.concatenate(name, Name.root);
            } catch (NameTooLongException e) {
                throw new IllegalArgumentException("ZoneTransferIn: name too long");
            }
        }
        this.qtype = i;
        this.dclass = FIRSTDATA;
        this.ixfr_serial = j;
        this.want_fallback = z;
        this.state = INITIALSOA;
    }

    public static ZoneTransferIn newAXFR(Name name, SocketAddress socketAddress, TSIG tsig) {
        return new ZoneTransferIn(name, Type.AXFR, 0, false, socketAddress, tsig);
    }

    public static ZoneTransferIn newAXFR(Name name, String str, int i, TSIG tsig) throws UnknownHostException {
        if (i == 0) {
            i = 53;
        }
        return newAXFR(name, new InetSocketAddress(str, i), tsig);
    }

    public static ZoneTransferIn newAXFR(Name name, String str, TSIG tsig) throws UnknownHostException {
        return newAXFR(name, str, INITIALSOA, tsig);
    }

    public static ZoneTransferIn newIXFR(Name name, long j, boolean z, SocketAddress socketAddress, TSIG tsig) {
        return new ZoneTransferIn(name, Type.IXFR, j, z, socketAddress, tsig);
    }

    public static ZoneTransferIn newIXFR(Name name, long j, boolean z, String str, int i, TSIG tsig) throws UnknownHostException {
        if (i == 0) {
            i = 53;
        }
        return newIXFR(name, j, z, new InetSocketAddress(str, i), tsig);
    }

    public static ZoneTransferIn newIXFR(Name name, long j, boolean z, String str, TSIG tsig) throws UnknownHostException {
        return newIXFR(name, j, z, str, INITIALSOA, tsig);
    }

    public Name getName() {
        return this.zname;
    }

    public int getType() {
        return this.qtype;
    }

    public void setTimeout(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("timeout cannot be negative");
        }
        this.timeout = 1000 * ((long) i);
    }

    public void setDClass(int i) {
        DClass.check(i);
        this.dclass = i;
    }

    public void setLocalAddress(SocketAddress socketAddress) {
        this.localAddress = socketAddress;
    }

    private void openConnection() throws IOException {
        this.client = new TCPClient(System.currentTimeMillis() + this.timeout);
        if (this.localAddress != null) {
            this.client.bind(this.localAddress);
        }
        this.client.connect(this.address);
    }

    private void sendQuery() throws IOException {
        Record newRecord = Record.newRecord(this.zname, this.qtype, this.dclass);
        Message message = new Message();
        message.getHeader().setOpcode(INITIALSOA);
        message.addRecord(newRecord, INITIALSOA);
        if (this.qtype == Type.IXFR) {
            message.addRecord(new SOARecord(this.zname, this.dclass, 0, Name.root, Name.root, this.ixfr_serial, 0, 0, 0, 0), IXFR_DELSOA);
        }
        if (this.tsig != null) {
            this.tsig.apply(message, null);
            this.verifier = new StreamVerifier(this.tsig, message.getTSIG());
        }
        this.client.send(message.toWire((int) InBandBytestreamManager.MAXIMUM_BLOCK_SIZE));
    }

    private static long getSOASerial(Record record) {
        return ((SOARecord) record).getSerial();
    }

    private void logxfr(String str) {
        if (Options.check("verbose")) {
            System.out.println(this.zname + ": " + str);
        }
    }

    private void fail(String str) throws ZoneTransferException {
        throw new ZoneTransferException(str);
    }

    private void fallback() throws ZoneTransferException {
        if (!this.want_fallback) {
            fail("server doesn't support IXFR");
        }
        logxfr("falling back to AXFR");
        this.qtype = Type.AXFR;
        this.state = INITIALSOA;
    }

    private void parseRR(Record record) throws ZoneTransferException {
        int type = record.getType();
        switch (this.state) {
            case INITIALSOA /*0*/:
                if (type != AXFR) {
                    fail("missing initial SOA");
                }
                this.initialsoa = record;
                this.end_serial = getSOASerial(record);
                if (this.qtype != Type.IXFR || Serial.compare(this.end_serial, this.ixfr_serial) > 0) {
                    this.state = FIRSTDATA;
                    return;
                }
                logxfr("up to date");
                this.state = END;
            case FIRSTDATA /*1*/:
                if (this.qtype == Type.IXFR && type == AXFR && getSOASerial(record) == this.ixfr_serial) {
                    this.rtype = Type.IXFR;
                    this.handler.startIXFR();
                    logxfr("got incremental response");
                    this.state = IXFR_DELSOA;
                } else {
                    this.rtype = Type.AXFR;
                    this.handler.startAXFR();
                    this.handler.handleRecord(this.initialsoa);
                    logxfr("got nonincremental response");
                    this.state = AXFR;
                }
                parseRR(record);
            case IXFR_DELSOA /*2*/:
                this.handler.startIXFRDeletes(record);
                this.state = IXFR_DEL;
            case IXFR_DEL /*3*/:
                if (type == AXFR) {
                    this.current_serial = getSOASerial(record);
                    this.state = IXFR_ADDSOA;
                    parseRR(record);
                    return;
                }
                this.handler.handleRecord(record);
            case IXFR_ADDSOA /*4*/:
                this.handler.startIXFRAdds(record);
                this.state = IXFR_ADD;
            case IXFR_ADD /*5*/:
                if (type == AXFR) {
                    long sOASerial = getSOASerial(record);
                    if (sOASerial == this.end_serial) {
                        this.state = END;
                        return;
                    } else if (sOASerial != this.current_serial) {
                        fail("IXFR out of sync: expected serial " + this.current_serial + " , got " + sOASerial);
                    } else {
                        this.state = IXFR_DELSOA;
                        parseRR(record);
                        return;
                    }
                }
                this.handler.handleRecord(record);
            case AXFR /*6*/:
                if (type != FIRSTDATA || record.getDClass() == this.dclass) {
                    this.handler.handleRecord(record);
                    if (type == AXFR) {
                        this.state = END;
                    }
                }
            case END /*7*/:
                fail("extra data");
            default:
                fail("invalid state");
        }
    }

    private void closeConnection() {
        try {
            if (this.client != null) {
                this.client.cleanup();
            }
        } catch (IOException e) {
        }
    }

    private Message parseMessage(byte[] bArr) throws WireParseException {
        try {
            return new Message(bArr);
        } catch (IOException e) {
            if (e instanceof WireParseException) {
                throw ((WireParseException) e);
            }
            throw new WireParseException("Error parsing message");
        }
    }

    private void doxfr() throws IOException, ZoneTransferException {
        sendQuery();
        while (this.state != END) {
            int rcode;
            byte[] recv = this.client.recv();
            Message parseMessage = parseMessage(recv);
            if (parseMessage.getHeader().getRcode() == 0 && this.verifier != null) {
                parseMessage.getTSIG();
                if (this.verifier.verify(parseMessage, recv) != 0) {
                    fail("TSIG failure");
                }
            }
            Record[] sectionArray = parseMessage.getSectionArray(FIRSTDATA);
            if (this.state == 0) {
                rcode = parseMessage.getRcode();
                if (rcode != 0) {
                    if (this.qtype == Type.IXFR && rcode == IXFR_ADDSOA) {
                        fallback();
                        doxfr();
                        return;
                    }
                    fail(Rcode.string(rcode));
                }
                Record question = parseMessage.getQuestion();
                if (!(question == null || question.getType() == this.qtype)) {
                    fail("invalid question section");
                }
                if (sectionArray.length == 0 && this.qtype == Type.IXFR) {
                    fallback();
                    doxfr();
                    return;
                }
            }
            for (rcode = INITIALSOA; rcode < sectionArray.length; rcode += FIRSTDATA) {
                parseRR(sectionArray[rcode]);
            }
            if (!(this.state != END || this.verifier == null || parseMessage.isVerified())) {
                fail("last message must be signed");
            }
        }
    }

    public void run(ZoneTransferHandler zoneTransferHandler) throws IOException, ZoneTransferException {
        this.handler = zoneTransferHandler;
        try {
            openConnection();
            doxfr();
        } finally {
            closeConnection();
        }
    }

    public List run() throws IOException, ZoneTransferException {
        BasicHandler basicHandler = new BasicHandler();
        run(basicHandler);
        if (basicHandler.axfr != null) {
            return basicHandler.axfr;
        }
        return basicHandler.ixfr;
    }

    private BasicHandler getBasicHandler() throws IllegalArgumentException {
        if (this.handler instanceof BasicHandler) {
            return (BasicHandler) this.handler;
        }
        throw new IllegalArgumentException("ZoneTransferIn used callback interface");
    }

    public boolean isAXFR() {
        return this.rtype == Type.AXFR;
    }

    public List getAXFR() {
        return getBasicHandler().axfr;
    }

    public boolean isIXFR() {
        return this.rtype == Type.IXFR;
    }

    public List getIXFR() {
        return getBasicHandler().ixfr;
    }

    public boolean isCurrent() {
        BasicHandler basicHandler = getBasicHandler();
        return basicHandler.axfr == null && basicHandler.ixfr == null;
    }
}
