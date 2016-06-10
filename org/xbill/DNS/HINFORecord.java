package org.xbill.DNS;

import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class HINFORecord extends Record {
    private static final long serialVersionUID = -4732870630947452112L;
    private byte[] cpu;
    private byte[] os;

    HINFORecord() {
    }

    Record getObject() {
        return new HINFORecord();
    }

    public HINFORecord(Name name, int i, long j, String str, String str2) {
        super(name, 13, i, j);
        try {
            this.cpu = Record.byteArrayFromString(str);
            this.os = Record.byteArrayFromString(str2);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.cpu = dNSInput.readCountedString();
        this.os = dNSInput.readCountedString();
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        try {
            this.cpu = Record.byteArrayFromString(tokenizer.getString());
            this.os = Record.byteArrayFromString(tokenizer.getString());
        } catch (TextParseException e) {
            throw tokenizer.exception(e.getMessage());
        }
    }

    public String getCPU() {
        return Record.byteArrayToString(this.cpu, false);
    }

    public String getOS() {
        return Record.byteArrayToString(this.os, false);
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeCountedString(this.cpu);
        dNSOutput.writeCountedString(this.os);
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Record.byteArrayToString(this.cpu, true));
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(Record.byteArrayToString(this.os, true));
        return stringBuffer.toString();
    }
}
