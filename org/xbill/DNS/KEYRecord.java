package org.xbill.DNS;

import java.io.IOException;
import java.security.PublicKey;
import java.util.StringTokenizer;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.DNSSEC.Algorithm;
import org.xbill.DNS.DNSSEC.DNSSECException;

public class KEYRecord extends KEYBase {
    public static final int FLAG_NOAUTH = 32768;
    public static final int FLAG_NOCONF = 16384;
    public static final int FLAG_NOKEY = 49152;
    public static final int OWNER_HOST = 512;
    public static final int OWNER_USER = 0;
    public static final int OWNER_ZONE = 256;
    public static final int PROTOCOL_ANY = 255;
    public static final int PROTOCOL_DNSSEC = 3;
    public static final int PROTOCOL_EMAIL = 2;
    public static final int PROTOCOL_IPSEC = 4;
    public static final int PROTOCOL_TLS = 1;
    private static final long serialVersionUID = 6385613447571488906L;

    public static class Flags {
        public static final int EXTEND = 4096;
        public static final int FLAG10 = 32;
        public static final int FLAG11 = 16;
        public static final int FLAG2 = 8192;
        public static final int FLAG4 = 2048;
        public static final int FLAG5 = 1024;
        public static final int FLAG8 = 128;
        public static final int FLAG9 = 64;
        public static final int HOST = 512;
        public static final int NOAUTH = 32768;
        public static final int NOCONF = 16384;
        public static final int NOKEY = 49152;
        public static final int NTYP3 = 768;
        public static final int OWNER_MASK = 768;
        public static final int SIG0 = 0;
        public static final int SIG1 = 1;
        public static final int SIG10 = 10;
        public static final int SIG11 = 11;
        public static final int SIG12 = 12;
        public static final int SIG13 = 13;
        public static final int SIG14 = 14;
        public static final int SIG15 = 15;
        public static final int SIG2 = 2;
        public static final int SIG3 = 3;
        public static final int SIG4 = 4;
        public static final int SIG5 = 5;
        public static final int SIG6 = 6;
        public static final int SIG7 = 7;
        public static final int SIG8 = 8;
        public static final int SIG9 = 9;
        public static final int USER = 0;
        public static final int USE_MASK = 49152;
        public static final int ZONE = 256;
        private static Mnemonic flags;

        private Flags() {
        }

        static {
            flags = new Mnemonic("KEY flags", SIG2);
            flags.setMaximum(InBandBytestreamManager.MAXIMUM_BLOCK_SIZE);
            flags.setNumericAllowed(false);
            flags.add(NOCONF, "NOCONF");
            flags.add(NOAUTH, "NOAUTH");
            flags.add(USE_MASK, "NOKEY");
            flags.add(FLAG2, "FLAG2");
            flags.add(EXTEND, "EXTEND");
            flags.add(FLAG4, "FLAG4");
            flags.add(FLAG5, "FLAG5");
            flags.add(USER, "USER");
            flags.add(ZONE, "ZONE");
            flags.add(HOST, "HOST");
            flags.add(OWNER_MASK, "NTYP3");
            flags.add(FLAG8, "FLAG8");
            flags.add(FLAG9, "FLAG9");
            flags.add(FLAG10, "FLAG10");
            flags.add(FLAG11, "FLAG11");
            flags.add(USER, "SIG0");
            flags.add(SIG1, "SIG1");
            flags.add(SIG2, "SIG2");
            flags.add(SIG3, "SIG3");
            flags.add(SIG4, "SIG4");
            flags.add(SIG5, "SIG5");
            flags.add(SIG6, "SIG6");
            flags.add(SIG7, "SIG7");
            flags.add(SIG8, "SIG8");
            flags.add(SIG9, "SIG9");
            flags.add(SIG10, "SIG10");
            flags.add(SIG11, "SIG11");
            flags.add(SIG12, "SIG12");
            flags.add(SIG13, "SIG13");
            flags.add(SIG14, "SIG14");
            flags.add(SIG15, "SIG15");
        }

        public static int value(String str) {
            int parseInt;
            try {
                parseInt = Integer.parseInt(str);
                if (parseInt < 0 || parseInt > InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) {
                    return -1;
                }
                return parseInt;
            } catch (NumberFormatException e) {
                StringTokenizer stringTokenizer = new StringTokenizer(str, "|");
                parseInt = USER;
                while (stringTokenizer.hasMoreTokens()) {
                    int value = flags.getValue(stringTokenizer.nextToken());
                    if (value < 0) {
                        return -1;
                    }
                    parseInt |= value;
                }
                return parseInt;
            }
        }
    }

    public static class Protocol {
        public static final int ANY = 255;
        public static final int DNSSEC = 3;
        public static final int EMAIL = 2;
        public static final int IPSEC = 4;
        public static final int NONE = 0;
        public static final int TLS = 1;
        private static Mnemonic protocols;

        private Protocol() {
        }

        static {
            protocols = new Mnemonic("KEY protocol", EMAIL);
            protocols.setMaximum(ANY);
            protocols.setNumericAllowed(true);
            protocols.add(NONE, "NONE");
            protocols.add(TLS, "TLS");
            protocols.add(EMAIL, "EMAIL");
            protocols.add(DNSSEC, "DNSSEC");
            protocols.add(IPSEC, "IPSEC");
            protocols.add(ANY, "ANY");
        }

        public static String string(int i) {
            return protocols.getText(i);
        }

        public static int value(String str) {
            return protocols.getValue(str);
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

    KEYRecord() {
    }

    Record getObject() {
        return new KEYRecord();
    }

    public KEYRecord(Name name, int i, long j, int i2, int i3, int i4, byte[] bArr) {
        super(name, 25, i, j, i2, i3, i4, bArr);
    }

    public KEYRecord(Name name, int i, long j, int i2, int i3, int i4, PublicKey publicKey) throws DNSSECException {
        super(name, 25, i, j, i2, i3, i4, DNSSEC.fromPublicKey(publicKey, i4));
        this.publicKey = publicKey;
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        String identifier = tokenizer.getIdentifier();
        this.flags = Flags.value(identifier);
        if (this.flags < 0) {
            throw tokenizer.exception("Invalid flags: " + identifier);
        }
        identifier = tokenizer.getIdentifier();
        this.proto = Protocol.value(identifier);
        if (this.proto < 0) {
            throw tokenizer.exception("Invalid protocol: " + identifier);
        }
        identifier = tokenizer.getIdentifier();
        this.alg = Algorithm.value(identifier);
        if (this.alg < 0) {
            throw tokenizer.exception("Invalid algorithm: " + identifier);
        } else if ((this.flags & FLAG_NOKEY) == FLAG_NOKEY) {
            this.key = null;
        } else {
            this.key = tokenizer.getBase64();
        }
    }
}
