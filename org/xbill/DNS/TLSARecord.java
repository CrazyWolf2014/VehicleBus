package org.xbill.DNS;

import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.utils.base16;

public class TLSARecord extends Record {
    private static final long serialVersionUID = 356494267028580169L;
    private byte[] certificateAssociationData;
    private int certificateUsage;
    private int matchingType;
    private int selector;

    public static class CertificateUsage {
        public static final int CA_CONSTRAINT = 0;
        public static final int DOMAIN_ISSUED_CERTIFICATE = 3;
        public static final int SERVICE_CERTIFICATE_CONSTRAINT = 1;
        public static final int TRUST_ANCHOR_ASSERTION = 2;

        private CertificateUsage() {
        }
    }

    public static class MatchingType {
        public static final int EXACT = 0;
        public static final int SHA256 = 1;
        public static final int SHA512 = 2;

        private MatchingType() {
        }
    }

    public static class Selector {
        public static final int FULL_CERTIFICATE = 0;
        public static final int SUBJECT_PUBLIC_KEY_INFO = 1;

        private Selector() {
        }
    }

    TLSARecord() {
    }

    Record getObject() {
        return new TLSARecord();
    }

    public TLSARecord(Name name, int i, long j, int i2, int i3, int i4, byte[] bArr) {
        super(name, 52, i, j);
        this.certificateUsage = Record.checkU8("certificateUsage", i2);
        this.selector = Record.checkU8("selector", i3);
        this.matchingType = Record.checkU8("matchingType", i4);
        this.certificateAssociationData = Record.checkByteArrayLength("certificateAssociationData", bArr, InBandBytestreamManager.MAXIMUM_BLOCK_SIZE);
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.certificateUsage = dNSInput.readU8();
        this.selector = dNSInput.readU8();
        this.matchingType = dNSInput.readU8();
        this.certificateAssociationData = dNSInput.readByteArray();
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.certificateUsage = tokenizer.getUInt8();
        this.selector = tokenizer.getUInt8();
        this.matchingType = tokenizer.getUInt8();
        this.certificateAssociationData = tokenizer.getHex();
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.certificateUsage);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.selector);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.matchingType);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(base16.toString(this.certificateAssociationData));
        return stringBuffer.toString();
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU8(this.certificateUsage);
        dNSOutput.writeU8(this.selector);
        dNSOutput.writeU8(this.matchingType);
        dNSOutput.writeByteArray(this.certificateAssociationData);
    }

    public int getCertificateUsage() {
        return this.certificateUsage;
    }

    public int getSelector() {
        return this.selector;
    }

    public int getMatchingType() {
        return this.matchingType;
    }

    public final byte[] getCertificateAssociationData() {
        return this.certificateAssociationData;
    }
}
