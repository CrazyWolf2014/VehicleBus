package org.xbill.DNS;

import java.io.IOException;
import java.security.PublicKey;
import org.xbill.DNS.DNSSEC.Algorithm;
import org.xbill.DNS.DNSSEC.DNSSECException;

public class DNSKEYRecord extends KEYBase {
    private static final long serialVersionUID = -8679800040426675002L;

    public static class Flags {
        public static final int REVOKE = 128;
        public static final int SEP_KEY = 1;
        public static final int ZONE_KEY = 256;

        private Flags() {
        }
    }

    public static class Protocol {
        public static final int DNSSEC = 3;

        private Protocol() {
        }
    }

    public /* bridge */ /* synthetic */ int getAlgorithm() {
        return super.getAlgorithm();
    }

    public /* bridge */ /* synthetic */ int getFlags() {
        return super.getFlags();
    }

    public /* bridge */ /* synthetic */ int getFootprint() {
        return super.getFootprint();
    }

    public /* bridge */ /* synthetic */ byte[] getKey() {
        return super.getKey();
    }

    public /* bridge */ /* synthetic */ int getProtocol() {
        return super.getProtocol();
    }

    public /* bridge */ /* synthetic */ PublicKey getPublicKey() throws DNSSECException {
        return super.getPublicKey();
    }

    DNSKEYRecord() {
    }

    Record getObject() {
        return new DNSKEYRecord();
    }

    public DNSKEYRecord(Name name, int i, long j, int i2, int i3, int i4, byte[] bArr) {
        super(name, 48, i, j, i2, i3, i4, bArr);
    }

    public DNSKEYRecord(Name name, int i, long j, int i2, int i3, int i4, PublicKey publicKey) throws DNSSECException {
        super(name, 48, i, j, i2, i3, i4, DNSSEC.fromPublicKey(publicKey, i4));
        this.publicKey = publicKey;
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.flags = tokenizer.getUInt16();
        this.proto = tokenizer.getUInt8();
        String string = tokenizer.getString();
        this.alg = Algorithm.value(string);
        if (this.alg < 0) {
            throw tokenizer.exception("Invalid algorithm: " + string);
        }
        this.key = tokenizer.getBase64();
    }
}
