package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.Util;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ARecord extends Record {
    private static final long serialVersionUID = -2172609200849142323L;
    private int addr;

    ARecord() {
    }

    Record getObject() {
        return new ARecord();
    }

    private static final int fromArray(byte[] bArr) {
        return ((((bArr[0] & KEYRecord.PROTOCOL_ANY) << 24) | ((bArr[1] & KEYRecord.PROTOCOL_ANY) << 16)) | ((bArr[2] & KEYRecord.PROTOCOL_ANY) << 8)) | (bArr[3] & KEYRecord.PROTOCOL_ANY);
    }

    private static final byte[] toArray(int i) {
        return new byte[]{(byte) ((i >>> 24) & KEYRecord.PROTOCOL_ANY), (byte) ((i >>> 16) & KEYRecord.PROTOCOL_ANY), (byte) ((i >>> 8) & KEYRecord.PROTOCOL_ANY), (byte) (i & KEYRecord.PROTOCOL_ANY)};
    }

    public ARecord(Name name, int i, long j, InetAddress inetAddress) {
        super(name, 1, i, j);
        if (Address.familyOf(inetAddress) != 1) {
            throw new IllegalArgumentException("invalid IPv4 address");
        }
        this.addr = fromArray(inetAddress.getAddress());
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.addr = fromArray(dNSInput.readByteArray(4));
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.addr = fromArray(tokenizer.getAddress(1).getAddress());
    }

    String rrToString() {
        return Address.toDottedQuad(toArray(this.addr));
    }

    public InetAddress getAddress() {
        try {
            return InetAddress.getByAddress(this.name.toString(), toArray(this.addr));
        } catch (UnknownHostException e) {
            return null;
        }
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU32(((long) this.addr) & Util.MAX_32BIT_VALUE);
    }
}
