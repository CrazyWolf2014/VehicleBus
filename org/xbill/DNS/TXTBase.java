package org.xbill.DNS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.Tokenizer.Token;

abstract class TXTBase extends Record {
    private static final long serialVersionUID = -4319510507246305931L;
    protected List strings;

    protected TXTBase() {
    }

    protected TXTBase(Name name, int i, int i2, long j) {
        super(name, i, i2, j);
    }

    protected TXTBase(Name name, int i, int i2, long j, List list) {
        super(name, i, i2, j);
        if (list == null) {
            throw new IllegalArgumentException("strings must not be null");
        }
        this.strings = new ArrayList(list.size());
        for (String byteArrayFromString : list) {
            try {
                this.strings.add(Record.byteArrayFromString(byteArrayFromString));
            } catch (TextParseException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    protected TXTBase(Name name, int i, int i2, long j, String str) {
        this(name, i, i2, j, Collections.singletonList(str));
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.strings = new ArrayList(2);
        while (dNSInput.remaining() > 0) {
            this.strings.add(dNSInput.readCountedString());
        }
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.strings = new ArrayList(2);
        while (true) {
            Token token = tokenizer.get();
            if (token.isString()) {
                try {
                    this.strings.add(Record.byteArrayFromString(token.value));
                } catch (TextParseException e) {
                    throw tokenizer.exception(e.getMessage());
                }
            }
            tokenizer.unget();
            return;
        }
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = this.strings.iterator();
        while (it.hasNext()) {
            stringBuffer.append(Record.byteArrayToString((byte[]) it.next(), true));
            if (it.hasNext()) {
                stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            }
        }
        return stringBuffer.toString();
    }

    public List getStrings() {
        List arrayList = new ArrayList(this.strings.size());
        for (int i = 0; i < this.strings.size(); i++) {
            arrayList.add(Record.byteArrayToString((byte[]) this.strings.get(i), false));
        }
        return arrayList;
    }

    public List getStringsAsByteArrays() {
        return this.strings;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        for (byte[] writeCountedString : this.strings) {
            dNSOutput.writeCountedString(writeCountedString);
        }
    }
}
