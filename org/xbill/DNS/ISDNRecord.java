package org.xbill.DNS;

import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.Tokenizer.Token;

public class ISDNRecord extends Record {
    private static final long serialVersionUID = -8730801385178968798L;
    private byte[] address;
    private byte[] subAddress;

    ISDNRecord() {
    }

    Record getObject() {
        return new ISDNRecord();
    }

    public ISDNRecord(Name name, int i, long j, String str, String str2) {
        super(name, 20, i, j);
        try {
            this.address = Record.byteArrayFromString(str);
            if (str2 != null) {
                this.subAddress = Record.byteArrayFromString(str2);
            }
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.address = dNSInput.readCountedString();
        if (dNSInput.remaining() > 0) {
            this.subAddress = dNSInput.readCountedString();
        }
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        try {
            this.address = Record.byteArrayFromString(tokenizer.getString());
            Token token = tokenizer.get();
            if (token.isString()) {
                this.subAddress = Record.byteArrayFromString(token.value);
            } else {
                tokenizer.unget();
            }
        } catch (TextParseException e) {
            throw tokenizer.exception(e.getMessage());
        }
    }

    public String getAddress() {
        return Record.byteArrayToString(this.address, false);
    }

    public String getSubAddress() {
        if (this.subAddress == null) {
            return null;
        }
        return Record.byteArrayToString(this.subAddress, false);
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeCountedString(this.address);
        if (this.subAddress != null) {
            dNSOutput.writeCountedString(this.subAddress);
        }
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Record.byteArrayToString(this.address, true));
        if (this.subAddress != null) {
            stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            stringBuffer.append(Record.byteArrayToString(this.subAddress, true));
        }
        return stringBuffer.toString();
    }
}
