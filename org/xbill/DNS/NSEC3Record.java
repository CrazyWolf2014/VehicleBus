package org.xbill.DNS;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.xbill.DNS.utils.base16;
import org.xbill.DNS.utils.base32;
import org.xbill.DNS.utils.base32.Alphabet;

public class NSEC3Record extends Record {
    public static final int SHA1_DIGEST_ID = 1;
    private static final base32 b32;
    private static final long serialVersionUID = -7123504635968932855L;
    private int flags;
    private int hashAlg;
    private int iterations;
    private byte[] next;
    private byte[] salt;
    private TypeBitmap types;

    public static class Digest {
        public static final int SHA1 = 1;

        private Digest() {
        }
    }

    public static class Flags {
        public static final int OPT_OUT = 1;

        private Flags() {
        }
    }

    static {
        b32 = new base32(Alphabet.BASE32HEX, false, false);
    }

    NSEC3Record() {
    }

    Record getObject() {
        return new NSEC3Record();
    }

    public NSEC3Record(Name name, int i, long j, int i2, int i3, int i4, byte[] bArr, byte[] bArr2, int[] iArr) {
        super(name, 50, i, j);
        this.hashAlg = Record.checkU8("hashAlg", i2);
        this.flags = Record.checkU8("flags", i3);
        this.iterations = Record.checkU16("iterations", i4);
        if (bArr != null) {
            if (bArr.length > KEYRecord.PROTOCOL_ANY) {
                throw new IllegalArgumentException("Invalid salt");
            } else if (bArr.length > 0) {
                this.salt = new byte[bArr.length];
                System.arraycopy(bArr, 0, this.salt, 0, bArr.length);
            }
        }
        if (bArr2.length > KEYRecord.PROTOCOL_ANY) {
            throw new IllegalArgumentException("Invalid next hash");
        }
        this.next = new byte[bArr2.length];
        System.arraycopy(bArr2, 0, this.next, 0, bArr2.length);
        this.types = new TypeBitmap(iArr);
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
        this.next = dNSInput.readByteArray(dNSInput.readU8());
        this.types = new TypeBitmap(dNSInput);
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU8(this.hashAlg);
        dNSOutput.writeU8(this.flags);
        dNSOutput.writeU16(this.iterations);
        if (this.salt != null) {
            dNSOutput.writeU8(this.salt.length);
            dNSOutput.writeByteArray(this.salt);
        } else {
            dNSOutput.writeU8(0);
        }
        dNSOutput.writeU8(this.next.length);
        dNSOutput.writeByteArray(this.next);
        this.types.toWire(dNSOutput);
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.hashAlg = tokenizer.getUInt8();
        this.flags = tokenizer.getUInt8();
        this.iterations = tokenizer.getUInt16();
        if (tokenizer.getString().equals("-")) {
            this.salt = null;
        } else {
            tokenizer.unget();
            this.salt = tokenizer.getHexString();
            if (this.salt.length > KEYRecord.PROTOCOL_ANY) {
                throw tokenizer.exception("salt value too long");
            }
        }
        this.next = tokenizer.getBase32String(b32);
        this.types = new TypeBitmap(tokenizer);
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
        stringBuffer.append(' ');
        stringBuffer.append(b32.toString(this.next));
        if (!this.types.empty()) {
            stringBuffer.append(' ');
            stringBuffer.append(this.types.toString());
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

    public byte[] getNext() {
        return this.next;
    }

    public int[] getTypes() {
        return this.types.toArray();
    }

    public boolean hasType(int i) {
        return this.types.contains(i);
    }

    static byte[] hashName(Name name, int i, int i2, byte[] bArr) throws NoSuchAlgorithmException {
        switch (i) {
            case SHA1_DIGEST_ID /*1*/:
                MessageDigest instance = MessageDigest.getInstance("sha-1");
                byte[] bArr2 = null;
                for (int i3 = 0; i3 <= i2; i3 += SHA1_DIGEST_ID) {
                    instance.reset();
                    if (i3 == 0) {
                        instance.update(name.toWireCanonical());
                    } else {
                        instance.update(bArr2);
                    }
                    if (bArr != null) {
                        instance.update(bArr);
                    }
                    bArr2 = instance.digest();
                }
                return bArr2;
            default:
                throw new NoSuchAlgorithmException("Unknown NSEC3 algorithmidentifier: " + i);
        }
    }

    public byte[] hashName(Name name) throws NoSuchAlgorithmException {
        return hashName(name, this.hashAlg, this.iterations, this.salt);
    }
}
