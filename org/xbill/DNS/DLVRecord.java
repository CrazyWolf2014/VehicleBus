package org.xbill.DNS;

import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.utils.base16;

public class DLVRecord extends Record {
    public static final int SHA1_DIGEST_ID = 1;
    public static final int SHA256_DIGEST_ID = 1;
    private static final long serialVersionUID = 1960742375677534148L;
    private int alg;
    private byte[] digest;
    private int digestid;
    private int footprint;

    DLVRecord() {
    }

    Record getObject() {
        return new DLVRecord();
    }

    public DLVRecord(Name name, int i, long j, int i2, int i3, int i4, byte[] bArr) {
        super(name, Type.DLV, i, j);
        this.footprint = Record.checkU16("footprint", i2);
        this.alg = Record.checkU8("alg", i3);
        this.digestid = Record.checkU8("digestid", i4);
        this.digest = bArr;
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.footprint = dNSInput.readU16();
        this.alg = dNSInput.readU8();
        this.digestid = dNSInput.readU8();
        this.digest = dNSInput.readByteArray();
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.footprint = tokenizer.getUInt16();
        this.alg = tokenizer.getUInt8();
        this.digestid = tokenizer.getUInt8();
        this.digest = tokenizer.getHex();
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.footprint);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.alg);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.digestid);
        if (this.digest != null) {
            stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            stringBuffer.append(base16.toString(this.digest));
        }
        return stringBuffer.toString();
    }

    public int getAlgorithm() {
        return this.alg;
    }

    public int getDigestID() {
        return this.digestid;
    }

    public byte[] getDigest() {
        return this.digest;
    }

    public int getFootprint() {
        return this.footprint;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU16(this.footprint);
        dNSOutput.writeU8(this.alg);
        dNSOutput.writeU8(this.digestid);
        if (this.digest != null) {
            dNSOutput.writeByteArray(this.digest);
        }
    }
}
