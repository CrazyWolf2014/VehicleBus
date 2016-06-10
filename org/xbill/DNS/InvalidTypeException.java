package org.xbill.DNS;

public class InvalidTypeException extends IllegalArgumentException {
    public InvalidTypeException(int i) {
        super("Invalid DNS type: " + i);
    }
}
