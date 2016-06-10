package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Message implements Cloneable {
    public static final int MAXLENGTH = 65535;
    static final int TSIG_FAILED = 4;
    static final int TSIG_INTERMEDIATE = 2;
    static final int TSIG_SIGNED = 3;
    static final int TSIG_UNSIGNED = 0;
    static final int TSIG_VERIFIED = 1;
    private static RRset[] emptyRRsetArray;
    private static Record[] emptyRecordArray;
    private Header header;
    private TSIGRecord querytsig;
    private List[] sections;
    int sig0start;
    private int size;
    int tsigState;
    private int tsigerror;
    private TSIG tsigkey;
    int tsigstart;

    static {
        emptyRecordArray = new Record[TSIG_UNSIGNED];
        emptyRRsetArray = new RRset[TSIG_UNSIGNED];
    }

    private Message(Header header) {
        this.sections = new List[TSIG_FAILED];
        this.header = header;
    }

    public Message(int i) {
        this(new Header(i));
    }

    public Message() {
        this(new Header());
    }

    public static Message newQuery(Record record) {
        Message message = new Message();
        message.header.setOpcode(TSIG_UNSIGNED);
        message.header.setFlag(7);
        message.addRecord(record, TSIG_UNSIGNED);
        return message;
    }

    public static Message newUpdate(Name name) {
        return new Update(name);
    }

    Message(DNSInput dNSInput) throws IOException {
        this(new Header(dNSInput));
        boolean z = this.header.getOpcode() == 5;
        boolean flag = this.header.getFlag(6);
        int i = TSIG_UNSIGNED;
        while (i < TSIG_FAILED) {
            try {
                int count = this.header.getCount(i);
                if (count > 0) {
                    this.sections[i] = new ArrayList(count);
                }
                for (int i2 = TSIG_UNSIGNED; i2 < count; i2 += TSIG_VERIFIED) {
                    int current = dNSInput.current();
                    Record fromWire = Record.fromWire(dNSInput, i, z);
                    this.sections[i].add(fromWire);
                    if (i == TSIG_SIGNED) {
                        if (fromWire.getType() == Type.TSIG) {
                            this.tsigstart = current;
                        }
                        if (fromWire.getType() == 24 && ((SIGRecord) fromWire).getTypeCovered() == 0) {
                            this.sig0start = current;
                        }
                    }
                }
                i += TSIG_VERIFIED;
            } catch (WireParseException e) {
                if (!flag) {
                    throw e;
                }
            }
        }
        this.size = dNSInput.current();
    }

    public Message(byte[] bArr) throws IOException {
        this(new DNSInput(bArr));
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return this.header;
    }

    public void addRecord(Record record, int i) {
        if (this.sections[i] == null) {
            this.sections[i] = new LinkedList();
        }
        this.header.incCount(i);
        this.sections[i].add(record);
    }

    public boolean removeRecord(Record record, int i) {
        if (this.sections[i] == null || !this.sections[i].remove(record)) {
            return false;
        }
        this.header.decCount(i);
        return true;
    }

    public void removeAllRecords(int i) {
        this.sections[i] = null;
        this.header.setCount(i, TSIG_UNSIGNED);
    }

    public boolean findRecord(Record record, int i) {
        return this.sections[i] != null && this.sections[i].contains(record);
    }

    public boolean findRecord(Record record) {
        int i = TSIG_VERIFIED;
        while (i <= TSIG_SIGNED) {
            if (this.sections[i] != null && this.sections[i].contains(record)) {
                return true;
            }
            i += TSIG_VERIFIED;
        }
        return false;
    }

    public boolean findRRset(Name name, int i, int i2) {
        if (this.sections[i2] == null) {
            return false;
        }
        for (int i3 = TSIG_UNSIGNED; i3 < this.sections[i2].size(); i3 += TSIG_VERIFIED) {
            Record record = (Record) this.sections[i2].get(i3);
            if (record.getType() == i && name.equals(record.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean findRRset(Name name, int i) {
        return findRRset(name, i, TSIG_VERIFIED) || findRRset(name, i, TSIG_INTERMEDIATE) || findRRset(name, i, TSIG_SIGNED);
    }

    public Record getQuestion() {
        List list = this.sections[TSIG_UNSIGNED];
        if (list == null || list.size() == 0) {
            return null;
        }
        return (Record) list.get(TSIG_UNSIGNED);
    }

    public TSIGRecord getTSIG() {
        int count = this.header.getCount(TSIG_SIGNED);
        if (count == 0) {
            return null;
        }
        Record record = (Record) this.sections[TSIG_SIGNED].get(count - 1);
        if (record.type != Type.TSIG) {
            return null;
        }
        return (TSIGRecord) record;
    }

    public boolean isSigned() {
        return this.tsigState == TSIG_SIGNED || this.tsigState == TSIG_VERIFIED || this.tsigState == TSIG_FAILED;
    }

    public boolean isVerified() {
        return this.tsigState == TSIG_VERIFIED;
    }

    public OPTRecord getOPT() {
        Record[] sectionArray = getSectionArray(TSIG_SIGNED);
        for (int i = TSIG_UNSIGNED; i < sectionArray.length; i += TSIG_VERIFIED) {
            if (sectionArray[i] instanceof OPTRecord) {
                return (OPTRecord) sectionArray[i];
            }
        }
        return null;
    }

    public int getRcode() {
        int rcode = this.header.getRcode();
        OPTRecord opt = getOPT();
        if (opt != null) {
            return rcode + (opt.getExtendedRcode() << TSIG_FAILED);
        }
        return rcode;
    }

    public Record[] getSectionArray(int i) {
        if (this.sections[i] == null) {
            return emptyRecordArray;
        }
        List list = this.sections[i];
        return (Record[]) list.toArray(new Record[list.size()]);
    }

    private static boolean sameSet(Record record, Record record2) {
        return record.getRRsetType() == record2.getRRsetType() && record.getDClass() == record2.getDClass() && record.getName().equals(record2.getName());
    }

    public RRset[] getSectionRRsets(int i) {
        if (this.sections[i] == null) {
            return emptyRRsetArray;
        }
        List linkedList = new LinkedList();
        Record[] sectionArray = getSectionArray(i);
        Set hashSet = new HashSet();
        int i2 = TSIG_UNSIGNED;
        while (i2 < sectionArray.length) {
            Object obj;
            Name name = sectionArray[i2].getName();
            if (hashSet.contains(name)) {
                for (int size = linkedList.size() - 1; size >= 0; size--) {
                    RRset rRset = (RRset) linkedList.get(size);
                    if (rRset.getType() == sectionArray[i2].getRRsetType() && rRset.getDClass() == sectionArray[i2].getDClass() && rRset.getName().equals(name)) {
                        rRset.addRR(sectionArray[i2]);
                        obj = TSIG_UNSIGNED;
                        break;
                    }
                }
            }
            obj = TSIG_VERIFIED;
            if (obj != null) {
                linkedList.add(new RRset(sectionArray[i2]));
                hashSet.add(name);
            }
            i2 += TSIG_VERIFIED;
        }
        return (RRset[]) linkedList.toArray(new RRset[linkedList.size()]);
    }

    void toWire(DNSOutput dNSOutput) {
        this.header.toWire(dNSOutput);
        Compression compression = new Compression();
        for (int i = TSIG_UNSIGNED; i < TSIG_FAILED; i += TSIG_VERIFIED) {
            if (this.sections[i] != null) {
                for (int i2 = TSIG_UNSIGNED; i2 < this.sections[i].size(); i2 += TSIG_VERIFIED) {
                    ((Record) this.sections[i].get(i2)).toWire(dNSOutput, i, compression);
                }
            }
        }
    }

    private int sectionToWire(DNSOutput dNSOutput, int i, Compression compression, int i2) {
        int size = this.sections[i].size();
        int current = dNSOutput.current();
        int i3 = TSIG_UNSIGNED;
        Record record = null;
        int i4 = TSIG_UNSIGNED;
        while (i3 < size) {
            Record record2 = (Record) this.sections[i].get(i3);
            if (!(record == null || sameSet(record2, record))) {
                current = dNSOutput.current();
                i4 = i3;
            }
            record2.toWire(dNSOutput, i, compression);
            if (dNSOutput.current() > i2) {
                dNSOutput.jump(current);
                return size - i4;
            }
            i3 += TSIG_VERIFIED;
            record = record2;
        }
        return TSIG_UNSIGNED;
    }

    private boolean toWire(DNSOutput dNSOutput, int i) {
        if (i < 12) {
            return false;
        }
        Header header = null;
        if (this.tsigkey != null) {
            i -= this.tsigkey.recordLength();
        }
        int current = dNSOutput.current();
        this.header.toWire(dNSOutput);
        Compression compression = new Compression();
        for (int i2 = TSIG_UNSIGNED; i2 < TSIG_FAILED; i2 += TSIG_VERIFIED) {
            if (this.sections[i2] != null) {
                int sectionToWire = sectionToWire(dNSOutput, i2, compression, i);
                if (sectionToWire != 0) {
                    if (TSIG_UNSIGNED == null) {
                        header = (Header) this.header.clone();
                    }
                    if (i2 != TSIG_SIGNED) {
                        header.setFlag(6);
                    }
                    header.setCount(i2, header.getCount(i2) - sectionToWire);
                    for (i2 += TSIG_VERIFIED; i2 < TSIG_FAILED; i2 += TSIG_VERIFIED) {
                        header.setCount(i2, TSIG_UNSIGNED);
                    }
                    dNSOutput.save();
                    dNSOutput.jump(current);
                    header.toWire(dNSOutput);
                    dNSOutput.restore();
                    if (this.tsigkey != null) {
                        TSIGRecord generate = this.tsigkey.generate(this, dNSOutput.toByteArray(), this.tsigerror, this.querytsig);
                        if (header == null) {
                            header = (Header) this.header.clone();
                        }
                        generate.toWire(dNSOutput, TSIG_SIGNED, compression);
                        header.incCount(TSIG_SIGNED);
                        dNSOutput.save();
                        dNSOutput.jump(current);
                        header.toWire(dNSOutput);
                        dNSOutput.restore();
                    }
                    return true;
                }
            }
        }
        if (this.tsigkey != null) {
            TSIGRecord generate2 = this.tsigkey.generate(this, dNSOutput.toByteArray(), this.tsigerror, this.querytsig);
            if (header == null) {
                header = (Header) this.header.clone();
            }
            generate2.toWire(dNSOutput, TSIG_SIGNED, compression);
            header.incCount(TSIG_SIGNED);
            dNSOutput.save();
            dNSOutput.jump(current);
            header.toWire(dNSOutput);
            dNSOutput.restore();
        }
        return true;
    }

    public byte[] toWire() {
        DNSOutput dNSOutput = new DNSOutput();
        toWire(dNSOutput);
        this.size = dNSOutput.current();
        return dNSOutput.toByteArray();
    }

    public byte[] toWire(int i) {
        DNSOutput dNSOutput = new DNSOutput();
        toWire(dNSOutput, i);
        this.size = dNSOutput.current();
        return dNSOutput.toByteArray();
    }

    public void setTSIG(TSIG tsig, int i, TSIGRecord tSIGRecord) {
        this.tsigkey = tsig;
        this.tsigerror = i;
        this.querytsig = tSIGRecord;
    }

    public int numBytes() {
        return this.size;
    }

    public String sectionToString(int i) {
        if (i > TSIG_SIGNED) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        Record[] sectionArray = getSectionArray(i);
        for (int i2 = TSIG_UNSIGNED; i2 < sectionArray.length; i2 += TSIG_VERIFIED) {
            Record record = sectionArray[i2];
            if (i == 0) {
                stringBuffer.append(";;\t" + record.name);
                stringBuffer.append(", type = " + Type.string(record.type));
                stringBuffer.append(", class = " + DClass.string(record.dclass));
            } else {
                stringBuffer.append(record);
            }
            stringBuffer.append(SpecilApiUtil.LINE_SEP);
        }
        return stringBuffer.toString();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (getOPT() != null) {
            stringBuffer.append(this.header.toStringWithRcode(getRcode()) + SpecilApiUtil.LINE_SEP);
        } else {
            stringBuffer.append(this.header + SpecilApiUtil.LINE_SEP);
        }
        if (isSigned()) {
            stringBuffer.append(";; TSIG ");
            if (isVerified()) {
                stringBuffer.append("ok");
            } else {
                stringBuffer.append("invalid");
            }
            stringBuffer.append('\n');
        }
        for (int i = TSIG_UNSIGNED; i < TSIG_FAILED; i += TSIG_VERIFIED) {
            if (this.header.getOpcode() != 5) {
                stringBuffer.append(";; " + Section.longString(i) + ":\n");
            } else {
                stringBuffer.append(";; " + Section.updString(i) + ":\n");
            }
            stringBuffer.append(sectionToString(i) + SpecilApiUtil.LINE_SEP);
        }
        stringBuffer.append(";; Message size: " + numBytes() + " bytes");
        return stringBuffer.toString();
    }

    public Object clone() {
        Message message = new Message();
        for (int i = TSIG_UNSIGNED; i < this.sections.length; i += TSIG_VERIFIED) {
            if (this.sections[i] != null) {
                message.sections[i] = new LinkedList(this.sections[i]);
            }
        }
        message.header = (Header) this.header.clone();
        message.size = this.size;
        return message;
    }
}
