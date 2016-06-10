package org.xbill.DNS;

import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class MINFORecord extends Record {
    private static final long serialVersionUID = -3962147172340353796L;
    private Name errorAddress;
    private Name responsibleAddress;

    MINFORecord() {
    }

    Record getObject() {
        return new MINFORecord();
    }

    public MINFORecord(Name name, int i, long j, Name name2, Name name3) {
        super(name, 14, i, j);
        this.responsibleAddress = Record.checkName("responsibleAddress", name2);
        this.errorAddress = Record.checkName("errorAddress", name3);
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.responsibleAddress = new Name(dNSInput);
        this.errorAddress = new Name(dNSInput);
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.responsibleAddress = tokenizer.getName(name);
        this.errorAddress = tokenizer.getName(name);
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.responsibleAddress);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.errorAddress);
        return stringBuffer.toString();
    }

    public Name getResponsibleAddress() {
        return this.responsibleAddress;
    }

    public Name getErrorAddress() {
        return this.errorAddress;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        this.responsibleAddress.toWire(dNSOutput, null, z);
        this.errorAddress.toWire(dNSOutput, null, z);
    }
}
