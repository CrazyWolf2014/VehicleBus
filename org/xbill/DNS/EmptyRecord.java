package org.xbill.DNS;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;

class EmptyRecord extends Record {
    private static final long serialVersionUID = 3601852050646429582L;

    EmptyRecord() {
    }

    Record getObject() {
        return new EmptyRecord();
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
    }

    String rrToString() {
        return XmlPullParser.NO_NAMESPACE;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
    }
}
