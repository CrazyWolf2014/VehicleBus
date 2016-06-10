package org.xbill.DNS;

import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class RPRecord extends Record {
    private static final long serialVersionUID = 8124584364211337460L;
    private Name mailbox;
    private Name textDomain;

    RPRecord() {
    }

    Record getObject() {
        return new RPRecord();
    }

    public RPRecord(Name name, int i, long j, Name name2, Name name3) {
        super(name, 17, i, j);
        this.mailbox = Record.checkName("mailbox", name2);
        this.textDomain = Record.checkName("textDomain", name3);
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.mailbox = new Name(dNSInput);
        this.textDomain = new Name(dNSInput);
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.mailbox = tokenizer.getName(name);
        this.textDomain = tokenizer.getName(name);
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.mailbox);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.textDomain);
        return stringBuffer.toString();
    }

    public Name getMailbox() {
        return this.mailbox;
    }

    public Name getTextDomain() {
        return this.textDomain;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        this.mailbox.toWire(dNSOutput, null, z);
        this.textDomain.toWire(dNSOutput, null, z);
    }
}
