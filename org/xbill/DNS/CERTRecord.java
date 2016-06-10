package org.xbill.DNS;

import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.DNSSEC.Algorithm;
import org.xbill.DNS.utils.base64;

public class CERTRecord extends Record {
    public static final int OID = 254;
    public static final int PGP = 3;
    public static final int PKIX = 1;
    public static final int SPKI = 2;
    public static final int URI = 253;
    private static final long serialVersionUID = 4763014646517016835L;
    private int alg;
    private byte[] cert;
    private int certType;
    private int keyTag;

    public static class CertificateType {
        public static final int ACPKIX = 7;
        public static final int IACPKIX = 8;
        public static final int IPGP = 6;
        public static final int IPKIX = 4;
        public static final int ISPKI = 5;
        public static final int OID = 254;
        public static final int PGP = 3;
        public static final int PKIX = 1;
        public static final int SPKI = 2;
        public static final int URI = 253;
        private static Mnemonic types;

        private CertificateType() {
        }

        static {
            types = new Mnemonic("Certificate type", SPKI);
            types.setMaximum(InBandBytestreamManager.MAXIMUM_BLOCK_SIZE);
            types.setNumericAllowed(true);
            types.add(PKIX, "PKIX");
            types.add(SPKI, "SPKI");
            types.add(PGP, "PGP");
            types.add(PKIX, "IPKIX");
            types.add(SPKI, "ISPKI");
            types.add(PGP, "IPGP");
            types.add(PGP, "ACPKIX");
            types.add(PGP, "IACPKIX");
            types.add(URI, "URI");
            types.add(OID, "OID");
        }

        public static String string(int i) {
            return types.getText(i);
        }

        public static int value(String str) {
            return types.getValue(str);
        }
    }

    CERTRecord() {
    }

    Record getObject() {
        return new CERTRecord();
    }

    public CERTRecord(Name name, int i, long j, int i2, int i3, int i4, byte[] bArr) {
        super(name, 37, i, j);
        this.certType = Record.checkU16("certType", i2);
        this.keyTag = Record.checkU16("keyTag", i3);
        this.alg = Record.checkU8("alg", i4);
        this.cert = bArr;
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.certType = dNSInput.readU16();
        this.keyTag = dNSInput.readU16();
        this.alg = dNSInput.readU8();
        this.cert = dNSInput.readByteArray();
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        String string = tokenizer.getString();
        this.certType = CertificateType.value(string);
        if (this.certType < 0) {
            throw tokenizer.exception("Invalid certificate type: " + string);
        }
        this.keyTag = tokenizer.getUInt16();
        string = tokenizer.getString();
        this.alg = Algorithm.value(string);
        if (this.alg < 0) {
            throw tokenizer.exception("Invalid algorithm: " + string);
        }
        this.cert = tokenizer.getBase64();
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.certType);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.keyTag);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.alg);
        if (this.cert != null) {
            if (Options.check("multiline")) {
                stringBuffer.append(" (\n");
                stringBuffer.append(base64.formatString(this.cert, 64, "\t", true));
            } else {
                stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                stringBuffer.append(base64.toString(this.cert));
            }
        }
        return stringBuffer.toString();
    }

    public int getCertType() {
        return this.certType;
    }

    public int getKeyTag() {
        return this.keyTag;
    }

    public int getAlgorithm() {
        return this.alg;
    }

    public byte[] getCert() {
        return this.cert;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU16(this.certType);
        dNSOutput.writeU16(this.keyTag);
        dNSOutput.writeU8(this.alg);
        dNSOutput.writeByteArray(this.cert);
    }
}
