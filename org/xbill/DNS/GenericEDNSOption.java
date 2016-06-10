package org.xbill.DNS;

import java.io.IOException;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.utils.base16;

public class GenericEDNSOption extends EDNSOption {
    private byte[] data;

    GenericEDNSOption(int i) {
        super(i);
    }

    public GenericEDNSOption(int i, byte[] bArr) {
        super(i);
        this.data = Record.checkByteArrayLength("option data", bArr, InBandBytestreamManager.MAXIMUM_BLOCK_SIZE);
    }

    void optionFromWire(DNSInput dNSInput) throws IOException {
        this.data = dNSInput.readByteArray();
    }

    void optionToWire(DNSOutput dNSOutput) {
        dNSOutput.writeByteArray(this.data);
    }

    String optionToString() {
        return "<" + base16.toString(this.data) + ">";
    }
}
