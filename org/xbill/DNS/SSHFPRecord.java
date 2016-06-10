package org.xbill.DNS;

import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.utils.base16;

public class SSHFPRecord extends Record {
    private static final long serialVersionUID = -8104701402654687025L;
    private int alg;
    private int digestType;
    private byte[] fingerprint;

    public static class Algorithm {
        public static final int DSS = 2;
        public static final int RSA = 1;

        private Algorithm() {
        }
    }

    public static class Digest {
        public static final int SHA1 = 1;

        private Digest() {
        }
    }

    SSHFPRecord() {
    }

    Record getObject() {
        return new SSHFPRecord();
    }

    public SSHFPRecord(Name name, int i, long j, int i2, int i3, byte[] bArr) {
        super(name, 44, i, j);
        this.alg = Record.checkU8("alg", i2);
        this.digestType = Record.checkU8("digestType", i3);
        this.fingerprint = bArr;
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.alg = dNSInput.readU8();
        this.digestType = dNSInput.readU8();
        this.fingerprint = dNSInput.readByteArray();
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.alg = tokenizer.getUInt8();
        this.digestType = tokenizer.getUInt8();
        this.fingerprint = tokenizer.getHex(true);
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.alg);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.digestType);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(base16.toString(this.fingerprint));
        return stringBuffer.toString();
    }

    public int getAlgorithm() {
        return this.alg;
    }

    public int getDigestType() {
        return this.digestType;
    }

    public byte[] getFingerPrint() {
        return this.fingerprint;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU8(this.alg);
        dNSOutput.writeU8(this.digestType);
        dNSOutput.writeByteArray(this.fingerprint);
    }
}
