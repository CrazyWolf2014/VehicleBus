package org.xbill.DNS;

import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class NAPTRRecord extends Record {
    private static final long serialVersionUID = 5191232392044947002L;
    private byte[] flags;
    private int order;
    private int preference;
    private byte[] regexp;
    private Name replacement;
    private byte[] service;

    NAPTRRecord() {
    }

    Record getObject() {
        return new NAPTRRecord();
    }

    public NAPTRRecord(Name name, int i, long j, int i2, int i3, String str, String str2, String str3, Name name2) {
        super(name, 35, i, j);
        this.order = Record.checkU16("order", i2);
        this.preference = Record.checkU16("preference", i3);
        try {
            this.flags = Record.byteArrayFromString(str);
            this.service = Record.byteArrayFromString(str2);
            this.regexp = Record.byteArrayFromString(str3);
            this.replacement = Record.checkName("replacement", name2);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.order = dNSInput.readU16();
        this.preference = dNSInput.readU16();
        this.flags = dNSInput.readCountedString();
        this.service = dNSInput.readCountedString();
        this.regexp = dNSInput.readCountedString();
        this.replacement = new Name(dNSInput);
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.order = tokenizer.getUInt16();
        this.preference = tokenizer.getUInt16();
        try {
            this.flags = Record.byteArrayFromString(tokenizer.getString());
            this.service = Record.byteArrayFromString(tokenizer.getString());
            this.regexp = Record.byteArrayFromString(tokenizer.getString());
            this.replacement = tokenizer.getName(name);
        } catch (TextParseException e) {
            throw tokenizer.exception(e.getMessage());
        }
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.order);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.preference);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(Record.byteArrayToString(this.flags, true));
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(Record.byteArrayToString(this.service, true));
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(Record.byteArrayToString(this.regexp, true));
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.replacement);
        return stringBuffer.toString();
    }

    public int getOrder() {
        return this.order;
    }

    public int getPreference() {
        return this.preference;
    }

    public String getFlags() {
        return Record.byteArrayToString(this.flags, false);
    }

    public String getService() {
        return Record.byteArrayToString(this.service, false);
    }

    public String getRegexp() {
        return Record.byteArrayToString(this.regexp, false);
    }

    public Name getReplacement() {
        return this.replacement;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU16(this.order);
        dNSOutput.writeU16(this.preference);
        dNSOutput.writeCountedString(this.flags);
        dNSOutput.writeCountedString(this.service);
        dNSOutput.writeCountedString(this.regexp);
        this.replacement.toWire(dNSOutput, null, z);
    }

    public Name getAdditionalName() {
        return this.replacement;
    }
}
