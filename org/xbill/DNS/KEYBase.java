package org.xbill.DNS;

import java.io.IOException;
import java.security.PublicKey;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.DNSSEC.DNSSECException;
import org.xbill.DNS.utils.base64;

abstract class KEYBase extends Record {
    private static final long serialVersionUID = 3469321722693285454L;
    protected int alg;
    protected int flags;
    protected int footprint;
    protected byte[] key;
    protected int proto;
    protected PublicKey publicKey;

    protected KEYBase() {
        this.footprint = -1;
        this.publicKey = null;
    }

    public KEYBase(Name name, int i, int i2, long j, int i3, int i4, int i5, byte[] bArr) {
        super(name, i, i2, j);
        this.footprint = -1;
        this.publicKey = null;
        this.flags = Record.checkU16("flags", i3);
        this.proto = Record.checkU8("proto", i4);
        this.alg = Record.checkU8("alg", i5);
        this.key = bArr;
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.flags = dNSInput.readU16();
        this.proto = dNSInput.readU8();
        this.alg = dNSInput.readU8();
        if (dNSInput.remaining() > 0) {
            this.key = dNSInput.readByteArray();
        }
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.flags);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.proto);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.alg);
        if (this.key != null) {
            if (Options.check("multiline")) {
                stringBuffer.append(" (\n");
                stringBuffer.append(base64.formatString(this.key, 64, "\t", true));
                stringBuffer.append(" ; key_tag = ");
                stringBuffer.append(getFootprint());
            } else {
                stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                stringBuffer.append(base64.toString(this.key));
            }
        }
        return stringBuffer.toString();
    }

    public int getFlags() {
        return this.flags;
    }

    public int getProtocol() {
        return this.proto;
    }

    public int getAlgorithm() {
        return this.alg;
    }

    public byte[] getKey() {
        return this.key;
    }

    public int getFootprint() {
        int i = 0;
        if (this.footprint >= 0) {
            return this.footprint;
        }
        DNSOutput dNSOutput = new DNSOutput();
        rrToWire(dNSOutput, null, false);
        byte[] toByteArray = dNSOutput.toByteArray();
        if (this.alg == 1) {
            i = ((toByteArray[toByteArray.length - 3] & KEYRecord.PROTOCOL_ANY) << 8) + (toByteArray[toByteArray.length - 2] & KEYRecord.PROTOCOL_ANY);
        } else {
            int i2 = 0;
            while (i < toByteArray.length - 1) {
                i2 += ((toByteArray[i] & KEYRecord.PROTOCOL_ANY) << 8) + (toByteArray[i + 1] & KEYRecord.PROTOCOL_ANY);
                i += 2;
            }
            if (i < toByteArray.length) {
                i2 += (toByteArray[i] & KEYRecord.PROTOCOL_ANY) << 8;
            }
            i = ((i2 >> 16) & InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) + i2;
        }
        this.footprint = i & InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        return this.footprint;
    }

    public PublicKey getPublicKey() throws DNSSECException {
        if (this.publicKey != null) {
            return this.publicKey;
        }
        this.publicKey = DNSSEC.toPublicKey(this);
        return this.publicKey;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU16(this.flags);
        dNSOutput.writeU8(this.proto);
        dNSOutput.writeU8(this.alg);
        if (this.key != null) {
            dNSOutput.writeByteArray(this.key);
        }
    }
}
