package org.xbill.DNS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class OPTRecord extends Record {
    private static final long serialVersionUID = -6254521894809367938L;
    private List options;

    OPTRecord() {
    }

    Record getObject() {
        return new OPTRecord();
    }

    public OPTRecord(int i, int i2, int i3, int i4, List list) {
        super(Name.root, 41, i, 0);
        Record.checkU16("payloadSize", i);
        Record.checkU8("xrcode", i2);
        Record.checkU8(AlixDefine.VERSION, i3);
        Record.checkU16("flags", i4);
        this.ttl = ((((long) i2) << 24) + (((long) i3) << 16)) + ((long) i4);
        if (list != null) {
            this.options = new ArrayList(list);
        }
    }

    public OPTRecord(int i, int i2, int i3, int i4) {
        this(i, i2, i3, i4, null);
    }

    public OPTRecord(int i, int i2, int i3) {
        this(i, i2, i3, 0, null);
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        if (dNSInput.remaining() > 0) {
            this.options = new ArrayList();
        }
        while (dNSInput.remaining() > 0) {
            this.options.add(EDNSOption.fromWire(dNSInput));
        }
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        throw tokenizer.exception("no text format defined for OPT");
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.options != null) {
            stringBuffer.append(this.options);
            stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        }
        stringBuffer.append(" ; payload ");
        stringBuffer.append(getPayloadSize());
        stringBuffer.append(", xrcode ");
        stringBuffer.append(getExtendedRcode());
        stringBuffer.append(", version ");
        stringBuffer.append(getVersion());
        stringBuffer.append(", flags ");
        stringBuffer.append(getFlags());
        return stringBuffer.toString();
    }

    public int getPayloadSize() {
        return this.dclass;
    }

    public int getExtendedRcode() {
        return (int) (this.ttl >>> 24);
    }

    public int getVersion() {
        return (int) ((this.ttl >>> 16) & 255);
    }

    public int getFlags() {
        return (int) (this.ttl & 65535);
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        if (this.options != null) {
            for (EDNSOption toWire : this.options) {
                toWire.toWire(dNSOutput);
            }
        }
    }

    public List getOptions() {
        if (this.options == null) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(this.options);
    }

    public List getOptions(int i) {
        if (this.options == null) {
            return Collections.EMPTY_LIST;
        }
        List list = Collections.EMPTY_LIST;
        for (EDNSOption eDNSOption : this.options) {
            if (eDNSOption.getCode() == i) {
                if (list == Collections.EMPTY_LIST) {
                    list = new ArrayList();
                }
                list.add(eDNSOption);
            }
        }
        return list;
    }
}
