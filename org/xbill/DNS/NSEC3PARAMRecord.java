package org.xbill.DNS;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.xbill.DNS.utils.base16;

public class NSEC3PARAMRecord extends Record {
    private static final long serialVersionUID = -8689038598776316533L;
    private int flags;
    private int hashAlg;
    private int iterations;
    private byte[] salt;

    NSEC3PARAMRecord() {
    }

    Record getObject() {
        return new NSEC3PARAMRecord();
    }

    public NSEC3PARAMRecord(Name name, int i, long j, int i2, int i3, int i4, byte[] bArr) {
        super(name, 51, i, j);
        this.hashAlg = Record.checkU8("hashAlg", i2);
        this.flags = Record.checkU8("flags", i3);
        this.iterations = Record.checkU16("iterations", i4);
        if (bArr == null) {
            return;
        }
        if (bArr.length > KEYRecord.PROTOCOL_ANY) {
            throw new IllegalArgumentException("Invalid salt length");
        } else if (bArr.length > 0) {
            this.salt = new byte[bArr.length];
            System.arraycopy(bArr, 0, this.salt, 0, bArr.length);
        }
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.hashAlg = dNSInput.readU8();
        this.flags = dNSInput.readU8();
        this.iterations = dNSInput.readU16();
        int readU8 = dNSInput.readU8();
        if (readU8 > 0) {
            this.salt = dNSInput.readByteArray(readU8);
        } else {
            this.salt = null;
        }
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU8(this.hashAlg);
        dNSOutput.writeU8(this.flags);
        dNSOutput.writeU16(this.iterations);
        if (this.salt != null) {
            dNSOutput.writeU8(this.salt.length);
            dNSOutput.writeByteArray(this.salt);
            return;
        }
        dNSOutput.writeU8(0);
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.hashAlg = tokenizer.getUInt8();
        this.flags = tokenizer.getUInt8();
        this.iterations = tokenizer.getUInt16();
        if (tokenizer.getString().equals("-")) {
            this.salt = null;
            return;
        }
        tokenizer.unget();
        this.salt = tokenizer.getHexString();
        if (this.salt.length > KEYRecord.PROTOCOL_ANY) {
            throw tokenizer.exception("salt value too long");
        }
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.hashAlg);
        stringBuffer.append(' ');
        stringBuffer.append(this.flags);
        stringBuffer.append(' ');
        stringBuffer.append(this.iterations);
        stringBuffer.append(' ');
        if (this.salt == null) {
            stringBuffer.append(SignatureVisitor.SUPER);
        } else {
            stringBuffer.append(base16.toString(this.salt));
        }
        return stringBuffer.toString();
    }

    public int getHashAlgorithm() {
        return this.hashAlg;
    }

    public int getFlags() {
        return this.flags;
    }

    public int getIterations() {
        return this.iterations;
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public byte[] hashName(Name name) throws NoSuchAlgorithmException {
        return NSEC3Record.hashName(name, this.hashAlg, this.iterations, this.salt);
    }
}
