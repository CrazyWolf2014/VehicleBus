package org.xbill.DNS;

import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class PXRecord extends Record {
    private static final long serialVersionUID = 1811540008806660667L;
    private Name map822;
    private Name mapX400;
    private int preference;

    PXRecord() {
    }

    Record getObject() {
        return new PXRecord();
    }

    public PXRecord(Name name, int i, long j, int i2, Name name2, Name name3) {
        super(name, 26, i, j);
        this.preference = Record.checkU16("preference", i2);
        this.map822 = Record.checkName("map822", name2);
        this.mapX400 = Record.checkName("mapX400", name3);
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.preference = dNSInput.readU16();
        this.map822 = new Name(dNSInput);
        this.mapX400 = new Name(dNSInput);
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.preference = tokenizer.getUInt16();
        this.map822 = tokenizer.getName(name);
        this.mapX400 = tokenizer.getName(name);
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.preference);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.map822);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.mapX400);
        return stringBuffer.toString();
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU16(this.preference);
        this.map822.toWire(dNSOutput, null, z);
        this.mapX400.toWire(dNSOutput, null, z);
    }

    public int getPreference() {
        return this.preference;
    }

    public Name getMap822() {
        return this.map822;
    }

    public Name getMapX400() {
        return this.mapX400;
    }
}
