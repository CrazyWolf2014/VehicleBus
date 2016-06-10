package org.xbill.DNS;

public class NameTooLongException extends WireParseException {
    public NameTooLongException(String str) {
        super(str);
    }
}
